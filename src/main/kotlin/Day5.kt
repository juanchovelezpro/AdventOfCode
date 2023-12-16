package juanchovelezpro

import java.io.File

fun main() {
    val file = File(ClassLoader.getSystemResource("day5.txt").file)
    val lines = file.readLines()
    println(readAlmanac(lines))
}

fun readAlmanac(lines: List<String>): Long {
    val seeds = lines.find { it.contains("seeds:") }!!.split(":")[1].trim().split(" ").map { it.toLong() }
    val mappingsList = getMappings(
        lines,
        listOf(
            "seed-to-soil map:",
            "soil-to-fertilizer map:",
            "fertilizer-to-water map:",
            "water-to-light map:",
            "light-to-temperature map:",
            "temperature-to-humidity map:",
            "humidity-to-location map:"
        )
    )

    val locations = mutableListOf<Long>()

    for (seed in seeds) {
        var mapValue = seed
        for ((indexM, mappings) in mappingsList.withIndex()) {
            for ((indexR, mapping) in mappings.withIndex()) {
                if (mapValue in mapping.sourceRange()) {
                    val mappingDelta = mapping.destRangeStart - mapping.sourceRangeStart
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
    return locations.min()
}

fun getMappings(lines: List<String>, mapsToCreate: List<String>): List<List<Mapping>> {
    val allMappings = mutableListOf<List<Mapping>>()
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
}

