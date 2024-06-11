package juanchovelezpro

fun main() {
    val lines = readInput("day5.txt")
    println(countNiceStrings(lines))
}

fun countNiceStrings(lines: List<String>): Int {
    var count = 0
    for (line in lines) {
        if (isNiceString(line)) count++
    }
    return count
}

fun isNiceString(text: String): Boolean {
    return hasAtLeastThreeVowels(text) && hasNoForbiddenStrings(text) && hasAtLeastOneLetterTwiceInARow(text)
}

fun hasAtLeastThreeVowels(text: String): Boolean {
    var times = 0
    for (letter in text) {
        if (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u') {
            times++
        }
    }
    return times >= 3
}

fun hasAtLeastOneLetterTwiceInARow(text: String): Boolean {
    var times = 1
    var currentLetter = text[0]
    for (index in 1..text.lastIndex) {
        if (currentLetter == text[index]) {
            times++
        } else {
            currentLetter = text[index]
        }
        if (times == 2) {
            break
        }
    }
    return times == 2
}

fun hasNoForbiddenStrings(text: String): Boolean {
    return !text.contains("ab") && !text.contains("cd") && !text.contains("pq") && !text.contains("xy")
}




