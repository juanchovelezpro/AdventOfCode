package juanchovelezpro

import kotlin.math.abs

fun main() {
    val lines = readInput("day1.txt")
    print(day1(lines))
}

fun day1(input: List<String>) {

    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()

    for (numbers in input) {
        val split = numbers.split("   ")
        left.add(split[0].toInt())
        right.add(split[1].toInt())
    }

    left.sort()
    right.sort()

    println(totalDistance(left, right))
    println(similarityScore(left, right))

}


fun totalDistance(left: List<Int>, right: List<Int>): Int {
    var totalDistance = 0
    for (x in left.indices) {
        totalDistance += abs(left[x] - right[x])
    }

    return totalDistance
}

fun similarityScore(left: List<Int>, right: List<Int>): Int {

    val mapTimes = mutableMapOf<Int, Int>()
    var similarity = 0

    for (x in left) {
        if (!mapTimes.contains(x))
            mapTimes[x] = 0
    }

    for (i in right) {
        if (mapTimes.contains(i))
            mapTimes[i] = mapTimes[i]!! + 1
    }

    for (i in left) {
        similarity += i * mapTimes[i]!!
    }

    return similarity

}


