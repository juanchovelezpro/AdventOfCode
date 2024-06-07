package juanchovelezpro

fun main() {
    val lines = readInput("day3.txt")
    println(calcHousesReceivingAtLeastOnePresent(lines[0]))
    println(calcHousesReceivingAtLeastOnePresentWithRobot(lines[0]))
}

fun calcHousesReceivingAtLeastOnePresent(path: String): Int {

    var x = 0
    var y = 0
    val coordinates = HashMap<String, Boolean>()
    coordinates["0,0"] = true

    for (instruction in path) {
        when (instruction) {
            '^' -> {
                y++
            }

            '>' -> {
                x++
            }

            '<' -> {
                x--
            }

            'v' -> {
                y--
            }
        }
        if (!coordinates.containsKey("$x,$y")) coordinates["$x,$y"] = true
    }

    return coordinates.size
}

fun calcHousesReceivingAtLeastOnePresentWithRobot(path: String): Int {

    var xSanta = 0
    var ySanta = 0
    var xRobot = 0
    var yRobot = 0
    val coordinates = HashMap<String, Boolean>()
    coordinates["0,0"] = true

    for (instruction in path.withIndex()) {
        when (instruction.value) {
            '^' -> {
                if (instruction.index % 2 == 0) ySanta++
                else yRobot++
            }

            '>' -> {
                if (instruction.index % 2 == 0) xSanta++
                else xRobot++
            }

            '<' -> {
                if (instruction.index % 2 == 0) xSanta--
                else xRobot--
            }

            'v' -> {
                if (instruction.index % 2 == 0) ySanta--
                else yRobot--
            }
        }
        if (!coordinates.containsKey("$xSanta,$ySanta")) coordinates["$xSanta,$ySanta"] = true
        if (!coordinates.containsKey("$xRobot,$yRobot")) coordinates["$xRobot,$yRobot"] = true
    }

    return coordinates.size
}
