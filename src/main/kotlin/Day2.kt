package juanchovelezpro

import java.io.File

fun main() {
    val file = File(ClassLoader.getSystemResource("day2.txt").file)
    val lines = file.readLines()
    println(sumPossibleGamesIds(lines, 12, 13, 14))
    println(sumPowersOfMinimumCubes(lines))

}

fun readGames(lines: List<String>): List<Game> {
    val games = mutableListOf<Game>()

    for (line in lines) {
        val split = line.split(":")
        val gameId = split[0].split(" ")[1].toInt()
        val sets = split[1].split(";")
        val gameSets = mutableListOf<Set>()
        for (set in sets) {
            val colors = set.split(",")
            var red = 0
            var blue = 0
            var green = 0
            for (color in colors) {
                val splitColor = color.trim().split(" ")
                val quantity = splitColor[0].toInt()
                val theColor = splitColor[1]
                when (theColor) {
                    "blue" -> blue += quantity
                    "red" -> red += quantity
                    "green" -> green += quantity
                }
            }
            gameSets.add(Set(red, green, blue))
        }
        games.add(Game(gameId, gameSets))
    }
    return games
}

fun sumPossibleGamesIds(lines: List<String>, redQuantity: Int, greenQuantity: Int, blueQuantity: Int): Int {
    val games = readGames(lines)
    return games.filter {
        val setsFiltered = it.sets.filter { set ->
            set.red <= redQuantity && set.green <= greenQuantity && set.blue <= blueQuantity
        }
        setsFiltered.size == it.sets.size
    }.sumOf { it.id }
}

fun sumPowersOfMinimumCubes(lines: List<String>): Int {
    val games = readGames(lines)
    var power = 0

    for (game in games) {
        val maxRed = game.sets.maxOf { it.red }
        val maxBlue = game.sets.maxOf { it.blue }
        val maxGreen = game.sets.maxOf { it.green }
        power += maxRed * maxBlue * maxGreen
    }

    return power
}

data class Game(
    val id: Int,
    val sets: List<Set>
)

data class Set(
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0
)