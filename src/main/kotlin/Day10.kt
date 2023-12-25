package juanchovelezpro

fun main() {
    val lines = readInput("day10.txt")
    println(findFarthestPosition(lines))
}

fun findFarthestPosition(lines: List<String>): Int {
    val maze = buildMaze(lines)
    val startingPosition = findStartingPosition(maze)
    val farthest = travelMaze(maze[startingPosition.first][startingPosition.second], maze)

    return farthest.maxOf { it.stepsFromStart }

}

fun travelMaze(start: Node, maze: Array<Array<Node>>): List<Node> {

    val paths = discovery(start, maze)
    val nodes = mutableListOf<Node>()
    var pendingToDiscover = true

    start.visited = true

    var stepsOne = 1
    var stepsTwo = 1

    var pathOne = paths[0]
    var pathTwo = paths[1]

    while (pendingToDiscover) {
        if (pathOne.x == pathTwo.x && pathOne.y == pathTwo.y) {
            pathOne.visited = true
            pathOne.stepsFromStart = stepsOne
            nodes.add(pathOne)
            break
        } else {
            if (!pathOne.visited) {
                pathOne.visited = true
                pathOne.stepsFromStart = stepsOne
                nodes.add(pathOne)
                stepsOne++
                val path = discovery(pathOne, maze)
                if (path.isNotEmpty()) {
                    pathOne = path[0]
                }
            }
            if (!pathTwo.visited) {
                pathTwo.visited = true
                pathTwo.stepsFromStart = stepsTwo
                nodes.add(pathTwo)
                stepsTwo++
                val path = discovery(pathTwo, maze)
                if (path.isNotEmpty()) {
                    pathTwo = path[0]
                }
            }
            pendingToDiscover = !pathTwo.visited || !pathOne.visited
        }
    }
    printMaze(maze)
    return nodes
}

fun discovery(node: Node, maze: Array<Array<Node>>): MutableList<Node> {
    val adj = mutableListOf<Node>()
    val canGoDown = "|LJ"
    val canGoUp = "|F7"
    val canGoLeft = "L-F"
    val canGoRight = "7-J"

    val up = (node.x - 1) to node.y
    val down = (node.x + 1) to node.y
    val left = node.x to (node.y - 1)
    val right = node.x to (node.y + 1)

    val upInBounds = isInBounds(up, maze)
    val downInBounds = isInBounds(down, maze)
    val leftInBounds = isInBounds(left, maze)
    val rightInBounds = isInBounds(right, maze)

    val destUp = if (upInBounds) maze[up.first][up.second] else null
    val destDown = if (downInBounds) maze[down.first][down.second] else null
    val destLeft = if (leftInBounds) maze[left.first][left.second] else null
    val destRight = if (rightInBounds) maze[right.first][right.second] else null

    when (node.label) {
        '|' -> {
            if (upInBounds && !destUp!!.visited && canGoUp.any { it == destUp.label }) adj.add(destUp)
            if (downInBounds && !destDown!!.visited && canGoDown.any { it == destDown.label }) adj.add(destDown)
        }

        'L' -> {
            if (upInBounds && !destUp!!.visited && canGoUp.any { it == destUp.label }) adj.add(destUp)
            if (rightInBounds && !destRight!!.visited && canGoRight.any { it == destRight.label }) adj.add(destRight)
        }

        'J' -> {
            if (upInBounds && !destUp!!.visited && canGoUp.any { it == destUp.label }) adj.add(destUp)
            if (leftInBounds && !destLeft!!.visited && canGoLeft.any { it == destLeft.label }) adj.add(destLeft)
        }

        'F' -> {
            if (downInBounds && !destDown!!.visited && canGoDown.any { it == destDown.label }) adj.add(destDown)
            if (rightInBounds && !destRight!!.visited && canGoRight.any { it == destRight.label }) adj.add(destRight)
        }

        '7' -> {
            if (downInBounds && !destDown!!.visited && canGoDown.any { it == destDown.label }) adj.add(destDown)
            if (leftInBounds && !destLeft!!.visited && canGoLeft.any { it == destLeft.label }) adj.add(destLeft)
        }

        '-' -> {
            if (leftInBounds && !destLeft!!.visited && canGoLeft.any { it == destLeft.label }) adj.add(destLeft)
            if (rightInBounds && !destRight!!.visited && canGoRight.any { it == destRight.label }) adj.add(destRight)
        }

        'S' -> {
            if (leftInBounds && !destLeft!!.visited && canGoLeft.any { it == destLeft.label }) adj.add(destLeft)
            if (rightInBounds && !destRight!!.visited && canGoRight.any { it == destRight.label }) adj.add(destRight)
            if (downInBounds && !destDown!!.visited && canGoDown.any { it == destDown.label }) adj.add(destDown)
            if (upInBounds && !destUp!!.visited && canGoUp.any { it == destUp.label }) adj.add(destUp)
        }
    }
    return adj
}

fun isInBounds(pos: Pair<Int, Int>, maze: Array<Array<Node>>): Boolean {
    return pos.first in 0..maze.lastIndex && pos.second in 0..maze[0].lastIndex
}

fun findStartingPosition(maze: Array<Array<Node>>): Pair<Int, Int> {
    var x = -1
    var y = -1
    var found = false
    for (i in 0..maze.lastIndex) {
        for (j in 0..maze[0].lastIndex) {
            if (maze[i][j].label == 'S') {
                x = i
                y = j
                found = true
                break
            }
        }
        if (found) break
    }
    return x to y
}

fun buildMaze(lines: List<String>): Array<Array<Node>> {
    return Array(lines.size) { x -> Array(lines[x].length) { y -> Node(lines[x][y], x, y) } }
}

fun printMaze(maze: Array<Array<Node>>) {
    for (index in 0..maze.lastIndex) {
        println(maze[index].contentToString())
    }
}

data class Node(
    var label: Char,
    val x: Int,
    val y: Int,
    var visited: Boolean = false,
    var stepsFromStart: Int = 0
) {
    override fun toString(): String {
        return "" + if (visited) 'X'
        else label
    }
}