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
    start: Int,
    end: Int
): List<Neighbor> {

    val neighbors = mutableListOf<Neighbor>()

    val directions = arrayOf(1, -1, 1, -1) // Up, Down, Right, Left

    val startFixed = if (start - 1 < 0) 0 else start - 1
    val endFixed = if (end + 1 > schematic[0].lastIndex) end else end + 1

    for (index in directions.indices) {
        if (index == 0 || index == 1) {
            val x = row + directions[index]
            if (x in 0..schematic.lastIndex) {
                val range = startFixed..endFixed
                var aux = range.first
                while (aux in range) {
                    neighbors.add(Neighbor(x, aux, schematic[x][aux]))
                    aux++
                }
            }
        } else {
            val y = if (index == 2) endFixed else startFixed
            neighbors.add(Neighbor(row, y, schematic[row][y]))
        }
    }

    return neighbors
}

fun getNumbersNeighborsGear(
    schematic: Array<Array<Neighbor>>,
    row: Int,
    start: Int,
    end: Int
): List<Int> {

    val numbers = mutableListOf<Int>()
    val directions = arrayOf(1, -1, 1, -1) // Up, Down, Right, Left

    val startFixed = if (start - 1 < 0) 0 else start - 1
    val endFixed = if (end + 1 > schematic[0].lastIndex) end else end + 1

    for (index in directions.indices) {
        if (index == 0 || index == 1) {
            val x = row + directions[index]
            if (x in 0..schematic.lastIndex) {
                val range = startFixed..endFixed
                var aux = range.first
                while (aux in range) {
                    val current = schematic[x][aux]
                    if (!current.visited && current.content[0].isDigit()) {
                        numbers.add(buildNumber(schematic, x, aux))
                    }
                    aux++
                }
            }
        } else {
            val y = if (index == 2) endFixed else startFixed
            val current = schematic[row][y]
            if (!current.visited && current.content[0].isDigit()) {
                numbers.add(buildNumber(schematic, row, y))
            }
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