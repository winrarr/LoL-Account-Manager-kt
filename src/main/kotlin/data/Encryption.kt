package data

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

object Encryption {
    fun hash(string: String): String {
        val bytes = string.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return byteArrayToHexString(digest)
    }

    fun byteArrayToHexString(array: ByteArray): String {
        return DatatypeConverter.printHexBinary(array)
    }

    fun hexStringToByteArray(string: String): ByteArray {
        return DatatypeConverter.parseHexBinary(string)
    }

    fun encrypt(plainText: String, key: ByteArray = DataHandler.key): String {
        val clean = plainText.toByteArray()

        // Generating IV.
        val ivSize = 16
        val iv = ByteArray(ivSize)
        val random = SecureRandom()
        random.nextBytes(iv)
        val ivParameterSpec = IvParameterSpec(iv)

        // Set key.
        val secretKeySpec = SecretKeySpec(key, "AES")

        // Encrypt.
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encrypted = cipher.doFinal(clean)

        // Combine IV and encrypted part.
        val encryptedIVAndText = ByteArray(ivSize + encrypted.size)
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize)
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.size)
        return byteArrayToHexString(encryptedIVAndText)
    }

    fun decrypt(encryptedIvText: String, key: ByteArray = DataHandler.key): String {
        val encryptedIvTextArray = hexStringToByteArray(encryptedIvText)
        val ivSize = 16

        // Extract IV.
        val iv = ByteArray(ivSize)
        System.arraycopy(encryptedIvTextArray, 0, iv, 0, iv.size)
        val ivParameterSpec = IvParameterSpec(iv)

        // Extract encrypted part.
        val encryptedSize = encryptedIvTextArray.size - ivSize
        val encryptedBytes = ByteArray(encryptedSize)
        System.arraycopy(encryptedIvTextArray, ivSize, encryptedBytes, 0, encryptedSize)

        // Set key.
        val secretKeySpec = SecretKeySpec(key, "AES")

        // Decrypt.
        val cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        val decrypted = cipherDecrypt.doFinal(encryptedBytes)
        return String(decrypted)
    }
}