package juanchovelezpro

import java.io.File
import kotlin.math.abs

fun main() {
    val file = File(ClassLoader.getSystemResource("day6.txt").file)
    val lines = file.readLines()
    println(numberOfWaysToBeatRecord(lines))
}


fun numberOfWaysToBeatRecord(lines: List<String>): Int {
    val times = lines[0].split(":")[1].trim().split(" ").filterNot { it.isBlank() }.map { it.toInt() }
    val records = lines[1].split(":")[1].trim().split(" ").filterNot { it.isBlank() }.map { it.toInt() }

    var numberWays = 1

    for (index in times.indices) {

        val time = times[index]
        val record = records[index]
        val start = (record / time) + 1
        val startRemainingTime = time - start
        val startDistance = start * startRemainingTime
        var defStart = start
        var defRemainingTime = startRemainingTime

        if (startDistance <= record) {
            for (newStart in start..record) {
                val newRemainingTime = time - newStart
                val newDistance = newStart * newRemainingTime
                if (newDistance > record) {
                    defStart = newStart
                    defRemainingTime = newRemainingTime
                }
            }
        }

        numberWays *= abs(defStart - defRemainingTime) + 1

    }

    return numberWays
}