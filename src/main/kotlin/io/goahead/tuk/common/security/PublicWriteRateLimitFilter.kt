package io.goahead.tuk.common.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Clock
import java.util.concurrent.ConcurrentHashMap

@Component
class PublicWriteRateLimitFilter(
    private val deviceKeyHasher: DeviceKeyHasher,
    @Value("\${security.rate-limit.device-per-minute:10}")
    private val deviceLimit: Int,
    @Value("\${security.rate-limit.source-per-minute:60}")
    private val sourceLimit: Int,
    @Value("\${security.rate-limit.max-keys:10000}")
    private val maxKeys: Int,
    private val clock: Clock = Clock.systemUTC(),
) : OncePerRequestFilter() {
    private val deviceBuckets = ConcurrentHashMap<String, Bucket>()
    private val sourceBuckets = ConcurrentHashMap<String, Bucket>()

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val isConfessionWrite = request.method == "POST" && request.requestURI.endsWith("/api/confessions")
        val isReactionMutation = (request.method == "PUT" || request.method == "DELETE") &&
            request.requestURI.contains("/api/confessions/") &&
            request.requestURI.contains("/reactions/")
        return !isConfessionWrite && !isReactionMutation
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val rawDeviceId = request.getHeader("X-Device-Id")
        if (rawDeviceId == null) {
            filterChain.doFilter(request, response)
            return
        }

        val nowMinute = clock.instant().epochSecond / 60
        val allowedDevice = permit(deviceBuckets, deviceKeyHasher.hash(rawDeviceId), deviceLimit, nowMinute)
        val allowedSource = permit(sourceBuckets, request.remoteAddr ?: "unknown", sourceLimit, nowMinute)

        if (!allowedDevice || !allowedSource) {
            response.status = 429
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.writer.write("""{"message":"Too many requests"}""")
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun permit(
        buckets: ConcurrentHashMap<String, Bucket>,
        key: String,
        limit: Int,
        minute: Long,
    ): Boolean {
        if (buckets.size >= maxKeys) {
            buckets.entries.removeIf { it.value.minute < minute }
        }

        var allowed = false
        buckets.compute(key) { _, existing ->
            val bucket = if (existing == null || existing.minute != minute) {
                Bucket(minute, 0)
            } else {
                existing
            }
            allowed = bucket.count < limit
            if (allowed) bucket.copy(count = bucket.count + 1) else bucket
        }
        return allowed
    }

    private data class Bucket(val minute: Long, val count: Int)
}
