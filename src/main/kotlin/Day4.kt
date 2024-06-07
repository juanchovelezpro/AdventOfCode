package juanchovelezpro

import java.math.BigInteger
import java.security.MessageDigest

fun main() {
    val lines = readInput("day4.txt")
    println(lowestPositiveNumberForMD5Hash(lines[0], 5))
    println(lowestPositiveNumberForMD5Hash(lines[0], 6))

}

fun lowestPositiveNumberForMD5Hash(secret: String, leadingZeros: Int): Long {
    var number = 1L
    while (true) {
        val secretWithNumber = secret + number
        val messageInBytes = secretWithNumber.toByteArray()
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(messageInBytes)
        val md5Result = BigInteger(1, digest).toString(16)
        if (md5Result.length != 32 - leadingZeros) number++
        else break
    }
    return number
}