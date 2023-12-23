package juanchovelezpro

import java.io.File

fun readInput(fileName: String): List<String> {
    val file = File(ClassLoader.getSystemResource(fileName).file)
    return file.readLines()
}