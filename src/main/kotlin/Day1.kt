package juanchovelezpro

import java.io.File

fun main() {
    val file = File(ClassLoader.getSystemResource("day1.txt").file)
    val lines = file.readLines()
    println("Part 1: ${trebuchet(lines)}")
    println("Part 2: ${trebuchetPartTwo(lines)}")
}

fun trebuchet(lines: List<String>): Int {
    val numbers = arrayListOf<Int>()
    for (line in lines) {
        val first = findDigit(line)
        val second = findDigit(line.reversed())
        numbers.add("$first$second".toInt())
    }
    return numbers.sum()
}

fun trebuchetPartTwo(lines: List<String>): Int {
    val mapNumbers = mapOf(
        Pair("one", 1),
        Pair("two", 2),
        Pair("three", 3),
        Pair("four", 4),
        Pair("five", 5),
        Pair("six", 6),
        Pair("seven", 7),
        Pair("eight", 8),
        Pair("nine", 9)
    )

    var sum = 0

    val possibleOptions = mutableListOf<String>()
    for ((k, v) in mapNumbers.entries) {
        possibleOptions.add(k)
        possibleOptions.add(v.toString())
    }

    for (line in lines) {
        val first = line.findAnyOf(possibleOptions)?.second!!
        val second = line.findLastAnyOf(possibleOptions)?.second!!

        val firstValue = if (first.length > 1) mapNumbers[first] else first
        val secondValue = if (second.length > 1) mapNumbers[second] else second

        sum += "$firstValue$secondValue".toInt()

    }

    return sum
}

fun findDigit(text: String): Int {
    var number = -1
    for (char in text) {
        if (char.isDigit()) {
            number = char.digitToInt()
            break
        }
    }
    return number
}