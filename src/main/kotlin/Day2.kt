package juanchovelezpro

import kotlin.math.abs

fun main() {
    val lines = readInput("day2.txt")
    println(safeReportsCount(lines))
    println(safeReportsWithTolerationCount(lines))
}

fun reports(lines: List<String>): List<Report> {
    val reports = mutableListOf<Report>()
    var counter = 1
    for (line in lines) {
        val levels = line.split(" ").map {
            it.toInt()
        }
        reports.add(Report(levels, counter))
        counter++
    }
    return reports
}

fun safeReportsCount(lines: List<String>): Int {
    val reports = reports(lines)
    var safeReports = 0

    for (report in reports) {
        if (report.isSafe()) {
            safeReports++
        }
    }
    return safeReports
}

fun safeReportsWithTolerationCount(lines: List<String>): Int {
    val reports = reports(lines)
    var safeReports = 0

    for (report in reports) {
        if (report.isSafeWithToleration()) safeReports++
    }
    return safeReports
}

class Report(val levels: List<Int>, val id: Int) {

    fun isSafe(pLevels: List<Int> = listOf<Int>()): Boolean {

        val theLevels = if (pLevels.isNotEmpty()) pLevels else levels

        var safe = true
        var increasing = false
        var decreasing = false
        for (i in 0..theLevels.lastIndex - 1) {
            if (abs(theLevels[i] - theLevels[i + 1]) > 3) {
                safe = false
                break
            }
            if (theLevels[i] < theLevels[i + 1]) increasing = true
            if (theLevels[i] > theLevels[i + 1]) decreasing = true
            if (theLevels[i] == theLevels[i + 1]) {
                safe = false
                break
            }
        }

        if (increasing == decreasing) safe = false

        return safe
    }

    fun isSafeWithToleration(): Boolean {
        var safe = true
        val increasing = mutableListOf<Pair<Int, Int>>()
        val decreasing = mutableListOf<Pair<Int, Int>>()
        val equalsPairs = mutableListOf<Pair<Int, Int>>()
        val differPairs = mutableListOf<Pair<Int, Int>>()

        for (i in 0..levels.lastIndex - 1) {
            if (abs(levels[i] - levels[i + 1]) > 3) differPairs.add(i to i + 1)
            if (levels[i] < levels[i + 1]) increasing.add(i to i + 1)
            if (levels[i] > levels[i + 1]) decreasing.add(i to i + 1)
            if (levels[i] == levels[i + 1]) equalsPairs.add(i to i + 1)
        }

        val allSituations = mutableListOf<Situation>(
            Situation("inc", increasing),
            Situation("dec", decreasing),
            Situation("eq", equalsPairs),
            Situation("diff", differPairs)
        )

        val situations = allSituations.filter { it.pairs.isNotEmpty() }
        val situationsSorted = situations.sortedBy { it.pairs.size }
        if (situations.size > 2) {
            val firstFix = situationsSorted[0]
            val secondFix = situationsSorted[1]
            safe = tryFix(firstFix) || tryFix(secondFix)
        } else if (situations.size == 1 && situations[0].id != "eq") {
            safe = true
        } else if (situations.size == 2) {
            val toFix = situationsSorted[0]
            safe = tryFix(toFix)
        }
        println("Inc: ${increasing.size}, Dec: ${decreasing.size}, Eq: ${equalsPairs.size}, diff: ${differPairs.size} safe: $safe ")
        return safe
    }

    fun tryFix(toFix: Situation): Boolean {
        var safe = true
        val leftCopy = mutableListOf<Int>()
        leftCopy.addAll(levels)
        val rightCopy = mutableListOf<Int>()
        rightCopy.addAll(levels)
        if (toFix.pairs.size > 1) safe = false
        else {
            leftCopy.removeAt(toFix.pairs[0].first)
            val removeLeft = isSafe(leftCopy)
            rightCopy.removeAt(toFix.pairs[0].second)
            val removeRight = isSafe(rightCopy)
            safe = removeRight || removeLeft
        }
        return safe
    }
}

class Situation(val id: String, val pairs: List<Pair<Int, Int>>)