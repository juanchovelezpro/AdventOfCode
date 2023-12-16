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
        var indexOneFound = -1
        var indexTwoFound = -1
        var numberOne = ""
        var numberTwo = ""
        for ((index, value) in line.withIndex()) {
            numberOne = if (value.isDigit()) {
                indexOneFound = index
                value.toString()
            } else ""
            if (indexOneFound != -1) break
        }
        for ((index, value) in line.withIndex().reversed()) {
            numberTwo = if (value.isDigit()) {
                indexTwoFound = index
                value.toString()
            } else ""
            if (indexTwoFound != -1) break
        }
        val number = "$numberOne$numberTwo".toInt()
        numbers.add(number)
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

    val numbers = arrayListOf<Int>()

    for (line in lines) {

        val firstNumberAsDigit = firstNumberAsDigit(line)
        val firstNumberAsText = firstNumberAsText(line, mapNumbers)
        val lastNumberAsDigit = lastNumberAsDigit(line)
        val lastNumberAsText = lastNumberAsText(line, mapNumbers)

        val first = when {
            firstNumberAsDigit == null -> firstNumberAsText?.second
            firstNumberAsText == null -> firstNumberAsDigit.second
            else -> {
                if (firstNumberAsDigit.first < firstNumberAsText.first) firstNumberAsDigit.second else firstNumberAsText.second
            }
        }

        val second = when {
            lastNumberAsDigit == null -> lastNumberAsText?.second
            lastNumberAsText == null -> lastNumberAsDigit.second
            else -> {
                if (lastNumberAsDigit.first > lastNumberAsText.first) lastNumberAsDigit.second else lastNumberAsText.second
            }
        }
        numbers.add("$first$second".toInt())
    }

    return numbers.sum()
}

fun firstNumberAsDigit(text: String): Pair<Int, Int>? {
    var foundAt = -1
    var numberFound = -1
    for ((index, char) in text.withIndex()) {
        if (char.isDigit()) {
            foundAt = index
            numberFound = char.digitToInt()
            break
        }
    }
    if (foundAt == -1) return null
    return Pair(foundAt, numberFound)
}

fun lastNumberAsDigit(text: String): Pair<Int, Int>? {
    var foundAt = -1
    var numberFound = -1
    for ((index, char) in text.withIndex().reversed()) {
        if (char.isDigit()) {
            foundAt = index
            numberFound = char.digitToInt()
            break
        }
    }
    if (foundAt == -1) return null
    return Pair(foundAt, numberFound)
}

fun firstNumberAsText(text: String, mapNumbers: Map<String, Int>): Pair<Int, Int>? {
    val foundAt = mutableMapOf<String, Int>()
    for (number in mapNumbers.keys) {
        val index = text.indexOf(number)
        if (index != -1) foundAt[number] = index
    }

    if (foundAt.isEmpty()) return null

    var min = Int.MAX_VALUE
    var numberFound = -1
    for (keyPairs in foundAt.entries) {
        if (keyPairs.value < min) {
            min = keyPairs.value
            numberFound = mapNumbers[keyPairs.key] ?: -1
        }
    }

    return Pair(min, numberFound)
}

fun lastNumberAsText(text: String, mapNumbers: Map<String, Int>): Pair<Int, Int>? {
    val foundAt = mutableMapOf<String, Int>()
    for (number in mapNumbers.keys) {
        val index = text.lastIndexOf(number)
        if (index != -1) foundAt[number] = index
    }

    if (foundAt.isEmpty()) return null

    var max = -1
    var numberFound = -1
    for (keyPairs in foundAt.entries) {
        if (keyPairs.value > max) {
            max = keyPairs.value
            numberFound = mapNumbers[keyPairs.key] ?: -1
        }
    }

    return Pair(max, numberFound)
}