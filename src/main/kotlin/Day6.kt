package juanchovelezpro

import java.io.File
import kotlin.math.abs

fun main() {
    val file = File(ClassLoader.getSystemResource("day6.txt").file)
    val lines = file.readLines()
    println(numberOfWaysToBeatRecord(lines))
    println(part2(lines))
}


fun numberOfWaysToBeatRecord(lines: List<String>): Long {
    val times = lines[0].split(":")[1].trim().split(" ").filterNot { it.isBlank() }.map { it.toLong() }
    val records = lines[1].split(":")[1].trim().split(" ").filterNot { it.isBlank() }.map { it.toLong() }

    var numberWays: Long = 1

    for (index in times.indices) {
        numberWays *= findNumberOfWaysToBeatRecord(times[index], records[index])
    }

    return numberWays
}

fun part2(lines: List<String>): Long {
    val time = lines[0].split(":")[1].replace(" ", "").toLong()
    val record = lines[1].split(":")[1].replace(" ", "").toLong()
    return findNumberOfWaysToBeatRecord(time, record)
}

fun findNumberOfWaysToBeatRecord(time: Long, record: Long): Long {
    val start = (record / time) + 1
    val startRemainingTime = time - start
    val startDistance = start * startRemainingTime
    var defStart = start
    var defRemainingTime = startRemainingTime

    if (startDistance <= record) {
        for (newStart in start+1..record) {
            val newRemainingTime = time - newStart
            val newDistance = newStart * newRemainingTime
            if (newDistance > record) {
                defStart = newStart
                defRemainingTime = newRemainingTime
                break
            }
        }
    }

    return abs(defStart - defRemainingTime) + 1
}