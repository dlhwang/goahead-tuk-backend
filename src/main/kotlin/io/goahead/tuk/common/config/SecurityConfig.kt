package io.goahead.tuk.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.access.intercept.AuthorizationFilter
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.time.Clock

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        publicWriteRateLimitFilter: io.goahead.tuk.common.security.PublicWriteRateLimitFilter,
    ): SecurityFilterChain {
        return http
            .cors { }
            .csrf { it.disable() }
            .headers {
                it.httpStrictTransportSecurity { hsts ->
                    hsts.includeSubDomains(true).maxAgeInSeconds(31536000)
                }
                it.frameOptions { frames -> frames.deny() }
                it.referrerPolicy { policy ->
                    policy.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                }
            }
            .addFilterBefore(publicWriteRateLimitFilter, AuthorizationFilter::class.java)
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                it.requestMatchers("/api/**").permitAll()
                it.anyRequest().permitAll()
            }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf(
                "http://localhost:5173",
                "https://goahead-tuk-frontend.vercel.app"
            )
            allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            allowedHeaders = listOf("Content-Type", "X-Device-Id", "X-Request-Id")
            exposedHeaders = listOf("X-Request-Id")
            allowCredentials = false
            maxAge = 3600
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/api/**", configuration)

        return source
    }

    @Bean
    fun applicationClock(): Clock = Clock.systemUTC()
}
