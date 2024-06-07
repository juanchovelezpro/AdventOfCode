package juanchovelezpro

fun main() {
    val lines = readInput("day1.txt")
    println(findFloorForSanta(lines[0]))
    println(findCharPositionToEnterBasement(lines[0]))
}

fun findFloorForSanta(instructions: String): Int {
    var floor = 0
    for(instruction in instructions){
        if(instruction == '(') floor++
        else floor--
    }
    return floor
}

fun findCharPositionToEnterBasement(instructions: String): Int{
    var floor = 0
    var index = 0
    for(instruction in instructions){
        if(instruction == '(') floor++
        else floor--
        if(floor == -1) break
        index++
    }

    return index+1
}

