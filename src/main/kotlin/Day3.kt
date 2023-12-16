package juanchovelezpro

import java.io.File

fun main() {
    val file = File(ClassLoader.getSystemResource("day3.txt").file)
    val lines = file.readLines()
    println(sumPartNumbersEngineSchematic(lines))
    println(sumGearRatios(lines))
}

fun sumPartNumbersEngineSchematic(lines: List<String>): Int {
    val schematic = buildSchematic(lines)
    val partNumbers = mutableListOf<Int>()

    var number = ""
    var firstIndex = -1
    var lastIndex = -1
    for (i in schematic.indices) {
        for (j in schematic[0].indices) {
            val content = schematic[i][j][0]
            if (content.isDigit()) {
                number += content
                if (firstIndex == -1) firstIndex = j

                if (number != "" && j == schematic[0].lastIndex) {
                    lastIndex = schematic[0].lastIndex
                    val neighbors = getNeighbors(schematic, i, firstIndex, lastIndex)
                    val isPartNumber = neighbors.any { isSymbol(it.content[0]) }
                    if (isPartNumber) partNumbers.add(number.toInt())
                    firstIndex = -1
                    number = ""
                }

            } else {
                if (number != "") {
                    lastIndex = j - 1
                    val neighbors = getNeighbors(schematic, i, firstIndex, lastIndex)
                    val isPartNumber = neighbors.any { isSymbol(it.content[0]) }
                    if (isPartNumber) partNumbers.add(number.toInt())
                    firstIndex = -1
                    number = ""
                }
            }
        }

    }
    return partNumbers.sum()
}

fun sumGearRatios(lines: List<String>): Int {
    val schematic = buildSchematicGear(lines)
    var gearRatio = 0

    for (i in schematic.indices) {
        for (j in schematic[0].indices) {
            if (schematic[i][j].content[0] == '*') {
                val numbers = getNumbersNeighborsGear(schematic, i, j, j)
                if (numbers.size == 2) {
                    val power = numbers[0] * numbers[1]
                    gearRatio += power
                }
            }
        }
    }
    return gearRatio
}


fun getNeighbors(
    schematic: Array<Array<String>>,
    row: Int,
    numberFirstIndex: Int,
    numberLastIndex: Int
): List<Neighbor> {

    val neighbors = mutableListOf<Neighbor>()

    val hasUp = row != 0
    val hasDown = row != schematic.size - 1
    val hasLeft = numberFirstIndex != 0
    val hasRight = numberLastIndex != schematic[0].size - 1

    if (hasUp) {
        var aux = numberFirstIndex
        while (aux <= numberLastIndex) {
            neighbors.add(Neighbor(row - 1, aux, schematic[row - 1][aux]))
            aux++
        }
    }

    if (hasDown) {
        var aux = numberFirstIndex
        while (aux <= numberLastIndex) {
            neighbors.add(Neighbor(row + 1, aux, schematic[row + 1][aux]))
            aux++
        }
    }

    if (hasRight) {
        neighbors.add(Neighbor(row, numberLastIndex + 1, schematic[row][numberLastIndex + 1]))
    }

    if (hasLeft) {
        neighbors.add(Neighbor(row, numberFirstIndex - 1, schematic[row][numberFirstIndex - 1]))
    }

    if (hasUp && hasLeft) {
        neighbors.add(Neighbor(row - 1, numberFirstIndex - 1, schematic[row - 1][numberFirstIndex - 1]))
    }

    if (hasUp && hasRight) {
        neighbors.add(Neighbor(row - 1, numberLastIndex + 1, schematic[row - 1][numberLastIndex + 1]))
    }

    if (hasDown && hasLeft) {
        neighbors.add(Neighbor(row + 1, numberFirstIndex - 1, schematic[row + 1][numberFirstIndex - 1]))
    }

    if (hasDown && hasRight) {
        neighbors.add(Neighbor(row + 1, numberLastIndex + 1, schematic[row + 1][numberLastIndex + 1]))
    }

    return neighbors
}

fun getNumbersNeighborsGear(
    schematic: Array<Array<Neighbor>>,
    row: Int,
    numberFirstIndex: Int,
    numberLastIndex: Int
): List<Int> {

    val numbers = mutableListOf<Int>()

    val hasUp = row != 0
    val hasDown = row != schematic.size - 1
    val hasLeft = numberFirstIndex != 0
    val hasRight = numberLastIndex != schematic[0].size - 1

    if (hasUp) {
        var aux = numberFirstIndex
        while (aux <= numberLastIndex) {
            val current = schematic[row - 1][aux]
            if (!current.visited && current.content[0].isDigit()) {
                numbers.add(buildNumber(schematic, row - 1, aux))
            }
            aux++
        }
    }

    if (hasDown) {
        var aux = numberFirstIndex
        while (aux <= numberLastIndex) {
            val current = schematic[row + 1][aux]
            if (!current.visited && current.content[0].isDigit()) {
                numbers.add(buildNumber(schematic, row + 1, aux))
            }
            aux++
        }
    }

    if (hasRight) {
        val current = schematic[row][numberLastIndex + 1]
        if (!current.visited && current.content[0].isDigit()) {
            numbers.add(buildNumber(schematic, row, numberLastIndex + 1))
        }
    }

    if (hasLeft) {
        val current = schematic[row][numberFirstIndex - 1]
        if (!current.visited && current.content[0].isDigit()) {
            numbers.add(buildNumber(schematic, row, numberFirstIndex - 1))
        }
    }

    if (hasUp && hasLeft) {
        val current = schematic[row - 1][numberFirstIndex - 1]
        if (!current.visited && current.content[0].isDigit()) {
            numbers.add(buildNumber(schematic, row - 1, numberFirstIndex - 1))
        }
    }

    if (hasUp && hasRight) {
        val current = schematic[row - 1][numberLastIndex + 1]
        if (!current.visited && current.content[0].isDigit()) {
            numbers.add(buildNumber(schematic, row - 1, numberLastIndex + 1))
        }
    }

    if (hasDown && hasLeft) {
        val current = schematic[row + 1][numberFirstIndex - 1]
        if (!current.visited && current.content[0].isDigit()) {
            numbers.add(buildNumber(schematic, row + 1, numberFirstIndex - 1))
        }
    }

    if (hasDown && hasRight) {
        val current = schematic[row + 1][numberLastIndex + 1]
        if (!current.visited && current.content[0].isDigit()) {
            numbers.add(buildNumber(schematic, row + 1, numberLastIndex + 1))
        }
    }

    return numbers
}

fun buildNumber(schematic: Array<Array<Neighbor>>, row: Int, col: Int): Int {
    // Go left
    val list = mutableListOf<String>()
    var leftAux = col
    while (leftAux >= 0) {
        if (!schematic[row][leftAux].visited) {
            val content = schematic[row][leftAux].content[0]
            if (content.isDigit()) {
                list.add(0, content.toString())
                schematic[row][leftAux].visited = true
            } else {
                break
            }

        }
        leftAux--
    }
    // Go right
    var rightAux = col
    while (rightAux <= schematic[0].size - 1) {
        if (!schematic[row][rightAux].visited) {
            val content = schematic[row][rightAux].content[0]
            if (content.isDigit()) {
                list.add(content.toString())
                schematic[row][rightAux].visited = true
            } else {
                break
            }
        }
        rightAux++
    }

    var number = ""
    list.forEach { number += it }

    return number.toInt()
}

fun String.detectNumbers(): List<Int> {
    val numbers = mutableListOf<Int>()
    var number = ""
    for (char in this) {
        if (char.isDigit()) {
            number += char
        } else {
            if (number != "") {
                numbers.add(number.toInt())
                number = ""
            }
        }
    }
    return numbers
}

fun buildSchematic(lines: List<String>): Array<Array<String>> {
    val schematic = Array(lines.size) { Array(lines[0].length) { "" } }
    for ((i, line) in lines.withIndex()) {
        for ((j, char) in line.withIndex()) {
            schematic[i][j] = char.toString()
        }
    }
    return schematic
}

fun buildSchematicGear(lines: List<String>): Array<Array<Neighbor>> {
    val schematic = Array(lines.size) { x -> Array(lines[x].length) { y -> Neighbor(x, y, "") } }
    for ((i, line) in lines.withIndex()) {
        for ((j, char) in line.withIndex()) {
            schematic[i][j].content = char.toString()
        }
    }
    return schematic
}

fun isSymbol(char: Char): Boolean {
    return !char.isLetterOrDigit() && char != '.'
}

data class Neighbor(
    val x: Int,
    val y: Int,
    var content: String,
    var visited: Boolean = false
)