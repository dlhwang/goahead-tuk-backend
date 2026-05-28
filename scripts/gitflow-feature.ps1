param(
    [string]$Slug,
    [string]$CommitMessage,
    [string]$Title,
    [string[]]$Path,
    [switch]$DryRun,
    [switch]$Help
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Write-Usage {
    @"
Usage:
  powershell -NoProfile -ExecutionPolicy Bypass -File scripts/gitflow-feature.ps1 `
    -Slug "short-feature-name" `
    -CommitMessage "docs: add repository README" `
    -Title "docs: add repository README" `
    -Path README.md,AGENTS.md

Behavior:
  - Creates feature/<Slug> when currently on develop.
  - Keeps the current branch when already on feature/*.
  - Stages only files passed with -Path.
  - Runs git diff --check and markdownlint for staged Markdown files.
  - Creates a conventional commit unless -DryRun is supplied.
  - Writes a PR draft to .git/codex-pr-message.md.
  - Does not push or create a pull request.
"@
}

function Invoke-Git {
    param([Parameter(ValueFromRemainingArguments = $true)][string[]]$Args)

    if ($DryRun) {
        Write-Host "[dry-run] git $($Args -join ' ')"
        return ""
    }

    & git @Args
    if ($LASTEXITCODE -ne 0) {
        throw "git $($Args -join ' ') failed with exit code $LASTEXITCODE"
    }
}

if ($Help) {
    Write-Usage
    exit 0
}

if (-not $CommitMessage) {
    throw "-CommitMessage is required."
}

if (-not $Path -or $Path.Count -eq 0) {
    throw "-Path is required so the helper never stages unrelated changes."
}

$repoRoot = (& git rev-parse --show-toplevel).Trim()
if ($LASTEXITCODE -ne 0 -or -not $repoRoot) {
    throw "This command must run inside a Git repository."
}

Set-Location $repoRoot

$currentBranch = (& git branch --show-current).Trim()
if (-not $currentBranch) {
    throw "Detached HEAD is not supported by this helper."
}

if ($currentBranch -eq "develop") {
    if (-not $Slug) {
        throw "-Slug is required when creating a feature branch from develop."
    }

    $targetBranch = "feature/$Slug"
    Invoke-Git switch -c $targetBranch
    $currentBranch = $targetBranch
}
elseif ($currentBranch -notlike "feature/*") {
    throw "Current branch '$currentBranch' is not develop or feature/*."
}

foreach ($item in $Path) {
    Invoke-Git add -- $item
}

Invoke-Git diff --check

$stagedFiles = & git diff --cached --name-only
if ($LASTEXITCODE -ne 0) {
    throw "Unable to inspect staged files."
}

if (-not $stagedFiles) {
    throw "No staged changes found after adding the requested paths."
}

$markdownFiles = @($stagedFiles | Where-Object { $_ -match "\.md$" })
if ($markdownFiles.Count -gt 0) {
    if ($DryRun) {
        Write-Host "[dry-run] npx.cmd markdownlint-cli2 $($markdownFiles -join ' ')"
    }
    else {
        & npx.cmd markdownlint-cli2 @markdownFiles
        if ($LASTEXITCODE -ne 0) {
            throw "markdownlint failed with exit code $LASTEXITCODE"
        }
    }
}

if (-not $DryRun) {
    & git commit -m $CommitMessage
    if ($LASTEXITCODE -ne 0) {
        throw "git commit failed with exit code $LASTEXITCODE"
    }
}
else {
    Write-Host "[dry-run] git commit -m `"$CommitMessage`""
}

$head = if ($DryRun) { "DRY-RUN" } else { (& git rev-parse --short HEAD).Trim() }
$prTitle = if ($Title) { $Title } else { $CommitMessage }
$changedList = @($stagedFiles | ForEach-Object { "- $_" }) -join [Environment]::NewLine
$testPlan = "Validated with git diff --check"
if ($markdownFiles.Count -gt 0) {
    $testPlan += " and markdownlint-cli2 for staged Markdown files"
}

$gitDir = (& git rev-parse --git-dir).Trim()
$prDraftPath = Join-Path $gitDir "codex-pr-message.md"

$body = @"
# Summary

$prTitle

## Changes

$changedList

## User experience

No runtime user experience change unless described in the commit.

## Checklist

* [x] I have reviewed the [contributing guidelines](../CONTRIBUTING.md)
* [x] For backend delivery changes under ``src/``, the branch type and PR base follow the backend GitFlow guide
* [ ] For ``release/*`` or ``hotfix/*`` backend PRs, the required ``develop`` follow-up is documented
* [x] I have performed a self-review of this change
* [x] Changes have been tested
* [x] Changes are documented

## Test Plan

- $testPlan.

## Acknowledgment

By submitting this pull request, I confirm that you can use, modify, copy, and redistribute this contribution, under the terms of the [project license](../LICENSE).

---

Branch: $currentBranch
Commit: $head
"@

if ($DryRun) {
    Write-Host "[dry-run] write PR draft to $prDraftPath"
    Write-Host $body
}
else {
    Set-Content -LiteralPath $prDraftPath -Encoding UTF8 -Value $body
    Write-Host "PR draft written to $prDraftPath"
}
