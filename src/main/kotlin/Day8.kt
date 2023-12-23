package juanchovelezpro

fun main() {
    val lines = readInput("day8.txt")
    println(escapeWasteland(lines))
    println(escapeSimultaneously(lines))
}

fun escapeWasteland(lines: List<String>): Int {
    val map = getMap(lines)
    val instructions = lines[0]
    return navigate(map, instructions)
}

fun escapeSimultaneously(lines: List<String>): Long {
    val map = getMap(lines)
    val instructions = lines[0]
    return navigateSimultaneously(map, instructions)
}


fun navigate(map: Map<String, Destination>, instructions: String): Int {
    var steps = 0
    var currentDest = "AAA"
    var index = 0
    while (currentDest != "ZZZ") {
        if (index == instructions.lastIndex + 1) index = 0
        val instruction = instructions[index]
        currentDest = goTo(map, currentDest, instruction)
        index++
        steps++
    }
    return steps
}

fun navigateSimultaneously(map: Map<String, Destination>, instructions: String): Long {
    var steps = 0
    val startMap = map.filterKeys { it[it.lastIndex] == 'A' }
    val currentDests = startMap.map { entry -> entry.key }.toMutableList()
    val stepsList = currentDests.map { -1L }.toMutableList()

    var index = 0
    while (!currentDests.all { it[it.lastIndex] == 'Z' }) {
        if (index == instructions.lastIndex + 1) index = 0
        val instruction = instructions[index]

        for ((x, currentDest) in currentDests.withIndex()) {
            if (currentDest[currentDest.lastIndex] != 'Z') {
                currentDests[x] = goTo(map, currentDest, instruction)
                if (stepsList[x] == -1L && currentDests[x][currentDest.lastIndex] == 'Z') {
                    stepsList[x] = steps + 1L
                }
            }
        }
        index++
        steps++
    }

    return lcm(stepsList)
}

fun goTo(map: Map<String, Destination>, from: String, instruction: Char): String {
    return if (instruction == 'L') map[from]!!.left else map[from]!!.right
}

fun getMap(lines: List<String>): Map<String, Destination> {
    val map = mutableMapOf<String, Destination>()
    for (index in 2..lines.lastIndex) {
        val split = lines[index].split("=")
        val key = split[0].trim()
        val destinations = split[1]
            .replace("(", "")
            .replace(")", "")
            .replace(" ", "")
            .split(",")
        val left = destinations[0]
        val right = destinations[1]
        map[key] = Destination(left, right)
    }
    return map
}

fun gcd(x: Long, y: Long): Long {
    if (y == 0L)
        return x
    return gcd(y, x % y)
}

fun lcm(numbers: List<Long>): Long {
    var lcm = numbers[0]
    for (index in 1..numbers.lastIndex) {
        val x = lcm
        val y = numbers[index]
        val gcd = gcd(x, y)
        lcm = (lcm * y) / gcd
    }
    return lcm
}


class Destination(
    val left: String,
    val right: String
)