package io.goahead.tuk.common.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

interface DeviceKeyHasher {
    fun hash(deviceId: String): String
}

@Component
class HmacDeviceKeyHasher(
    @Value("\${security.device-key-hmac-secret:local-development-only-secret}")
    secret: String,
) : DeviceKeyHasher {
    private val secretKey = SecretKeySpec(
        secret.also { require(it.length >= 16) { "Device key HMAC secret must have at least 16 characters" } }
            .toByteArray(StandardCharsets.UTF_8),
        ALGORITHM,
    )

    override fun hash(deviceId: String): String {
        require(DEVICE_ID_PATTERN.matches(deviceId)) { "Invalid device identifier" }

        val mac = Mac.getInstance(ALGORITHM)
        mac.init(secretKey)
        return mac.doFinal(deviceId.toByteArray(StandardCharsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val ALGORITHM = "HmacSHA256"
        private val DEVICE_ID_PATTERN = Regex("[A-Za-z0-9_-]{1,128}")
    }
}
