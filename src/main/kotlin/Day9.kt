package juanchovelezpro

fun main() {
    val lines = readInput("day9.txt")
    println(sumExtrapolateValues(lines))
    println(sumExtrapolateValues(lines, backwards = true))
}

fun sumExtrapolateValues(lines: List<String>, backwards: Boolean = false): Long {
    val reports = getReports(lines)
    var sum = 0L
    for (report in reports) {
        if (!backwards) {
            val extrapolation = extrapolate(report, backwards)
            val last = extrapolation[extrapolation.lastIndex]
            sum += last[last.lastIndex]
        } else {
            val extrapolation = extrapolate(report, backwards)
            val last = extrapolation[extrapolation.lastIndex]
            sum += last[0]
        }
    }

    return sum
}

fun extrapolate(report: MutableList<Long>, backwards: Boolean = false): List<List<Long>> {
    val sequences = mutableListOf<MutableList<Long>>()
    var allZeros = false

    while (!allZeros) {
        val sequence = mutableListOf<Long>()
        if (sequences.isEmpty()) {
            for (index in 0..<report.lastIndex) {
                sequence.add(report[index + 1] - report[index])
            }
        } else {
            val subsequence = sequences[sequences.lastIndex]
            for (index in 0..<subsequence.lastIndex) {
                sequence.add(subsequence[index + 1] - subsequence[index])
            }
        }
        sequences.add(sequence)
        allZeros = sequences[sequences.lastIndex].all { it == 0L }
    }

    sequences.add(0, report)
    sequences.reverse()

    if (!backwards) {
        for ((index, sequence) in sequences.withIndex()) {
            if (index == 1) {
                sequence.add(sequence[sequence.lastIndex])
            } else if (index > 1) {
                val previous = sequences[index - 1]
                sequence.add(sequence[sequence.lastIndex] + previous[previous.lastIndex])
            }
        }
    } else {
        for ((index, sequence) in sequences.withIndex()) {
            if (index == 1) {
                sequence.add(0, sequence[0])
            } else if (index > 1) {
                val previous = sequences[index - 1]
                sequence.add(0, sequence[0] - previous[0])
            }
        }
    }

    return sequences
}

fun getReports(lines: List<String>): List<MutableList<Long>> {
    val reports = mutableListOf<MutableList<Long>>()
    for (line in lines) {
        reports.add(line.split(" ").map { it.toLong() }.toMutableList())
    }
    return reports
}