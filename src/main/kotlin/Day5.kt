package juanchovelezpro

import java.io.File

fun main() {
    val file = File(ClassLoader.getSystemResource("day5.txt").file)
    val lines = file.readLines()
    println(lowestLocation(lines))
    //println(lowestLocationWithSeedRangesByBruteForce(lines))

}

fun lowestLocation(lines: List<String>): Long {
    val seeds = lines.find { it.contains("seeds:") }!!.split(":")[1].trim().split(" ").map { it.toLong() }
    val mappingsList = getMappings(lines)
    return computeLocations(seeds, mappingsList).min()
}


fun lowestLocationWithSeedRangesByBruteForce(lines: List<String>): Long {
    val mappingsList = getMappings(lines)

    val seedsRanges = lines.find { it.contains("seeds:") }!!.split(":")[1].trim().split(" ").map { it.toLong() }
    val ranges = mutableListOf<LongRange>()

    for (i in 0..<seedsRanges.lastIndex step 2) {
        ranges.add(seedsRanges[i]..<(seedsRanges[i] + seedsRanges[i + 1]))
    }

    println(ranges)

    val minLocationsPerRange = mutableListOf<Long>()

    val threads = mutableListOf<Thread>()

    for (range in ranges) {
        threads.add(Thread {
            var min = Long.MAX_VALUE
            for (seed in range) {
                val location = computeLocation(seed, mappingsList)
                println("Processing from $location  from -> ${Thread.currentThread()}")
                if (location < min) min = location
            }
            minLocationsPerRange.add(min)
        })
    }

    threads.forEach { it.start() }

    threads.forEach { it.join() }

    println(minLocationsPerRange)

    return minLocationsPerRange.min()
}

fun computeLocations(seeds: List<Long>, mappingsList: List<List<Mapping>>): List<Long> {
    val locations = mutableListOf<Long>()
    for (seed in seeds) {
        locations.add(computeLocation(seed, mappingsList))
    }
    return locations
}

fun computeLocation(seed: Long, mappingsList: List<List<Mapping>>): Long {
    var location: Long = 0
    var mapValue = seed
    for ((indexM, mappings) in mappingsList.withIndex()) {
        for ((indexR, mapping) in mappings.withIndex()) {
            if (mapValue in mapping.sourceRange()) {
                val mappingDelta = mapping.destRangeStart - mapping.sourceRangeStart
                mapValue += mappingDelta
                if (indexM == mappingsList.lastIndex) {
                    location = mapValue
                }
                break
            } else if (indexM == mappingsList.lastIndex && indexR == mappings.lastIndex) {
                location = mapValue
            } else {
                continue
            }
        }
    }
    return location
}

fun computeSeeds(seeds: List<Long>, mappingsList: List<List<Mapping>>): List<Long> {
    val locations = mutableListOf<Long>()
    for (seed in seeds) {
        var mapValue = seed
        for ((indexM, mappings) in mappingsList.withIndex()) {
            for ((indexR, mapping) in mappings.withIndex()) {
                if (mapValue in mapping.destRange()) {
                    val mappingDelta = mapping.sourceRangeStart - mapping.destRangeStart
                    mapValue += mappingDelta
                    if (indexM == mappingsList.lastIndex) {
                        locations.add(mapValue)
                    }
                    break
                } else if (indexM == mappingsList.lastIndex && indexR == mappings.lastIndex) {
                    locations.add(mapValue)
                } else {
                    continue
                }
            }
        }
    }
    return locations
}

fun getMappings(lines: List<String>): List<List<Mapping>> {
    val allMappings = mutableListOf<List<Mapping>>()
    val mapsToCreate = listOf(
        "seed-to-soil map:",
        "soil-to-fertilizer map:",
        "fertilizer-to-water map:",
        "water-to-light map:",
        "light-to-temperature map:",
        "temperature-to-humidity map:",
        "humidity-to-location map:"
    )
    for (map in mapsToCreate) {
        var index = lines.indexOf(map) + 1
        val mappings = mutableListOf<Mapping>()
        while (index <= lines.size - 1 && lines[index].isNotBlank()) {
            val ranges = lines[index].split(" ")
            mappings.add(Mapping(ranges[0].toLong(), ranges[1].toLong(), ranges[2].toLong()))
            index++
        }
        allMappings.add(mappings)
    }
    return allMappings
}

data class Mapping(
    val destRangeStart: Long,
    val sourceRangeStart: Long,
    val range: Long
) {
    fun sourceRange() = sourceRangeStart..<sourceRangeStart + range
    fun destRange() = destRangeStart..<destRangeStart + range
}

