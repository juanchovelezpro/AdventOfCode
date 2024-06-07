package juanchovelezpro

fun main() {
    val lines = readInput("day2.txt")
    val dimensions = dimensions(lines)
    println(calcSquareFeetWrappingPaper(dimensions))
    println(calcFeetOfRibbon(dimensions))
}

fun dimensions(lines: List<String>): List<Dimension> {
    val dimensions = arrayListOf<Dimension>()

    for (line in lines) {
        val lwh = line.split("x")
        val length = lwh[0].toInt()
        val width = lwh[1].toInt()
        val height = lwh[2].toInt()

        dimensions.add(Dimension(length, width, height))
    }

    return dimensions
}

fun calcSquareFeetWrappingPaper(dimensions: List<Dimension>): Int {
    var sqFeetWrappingPaper = 0
    for (dimension in dimensions) {
        val dims = arrayListOf<Int>()
        dims.add(dimension.length)
        dims.add(dimension.height)
        dims.add(dimension.width)
        dims.sort()

        val slack = dims[0] * dims[1]

        sqFeetWrappingPaper += 2 * dimension.length * dimension.width + 2 * dimension.width * dimension.height + 2 * dimension.height * dimension.length + slack

    }
    return sqFeetWrappingPaper
}

fun calcFeetOfRibbon(dimensions: List<Dimension>): Int {
    var feetOfRibbon = 0
    for (dimension in dimensions) {
        val dims = arrayListOf<Int>()
        dims.add(dimension.length)
        dims.add(dimension.height)
        dims.add(dimension.width)
        dims.sort()

        val perimeter = dims[0] * 2 + dims[1] * 2
        val cube = dimension.length * dimension.width * dimension.height

        feetOfRibbon += perimeter + cube
    }
    return feetOfRibbon
}

data class Dimension(val length: Int, val width: Int, val height: Int)

