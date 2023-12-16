package juanchovelezpro

import java.io.File
import java.util.*
import kotlin.math.pow

fun main() {
    val file = File(ClassLoader.getSystemResource("day4.txt").file)
    val lines = file.readLines()
    println(worthPoints(lines))
    println(processScratchcards(lines))
}

fun worthPoints(lines: List<String>): Int {
    var points = 0
    for (line in lines) {
        val splitLine = line.split(":")
        val splitNumbers = splitLine[1].split("|")
        val winningNumbers = splitNumbers[0].split(" ").filterNot { it.isBlank() }
        val numbers = splitNumbers[1].split(" ").filterNot { it.isBlank() }
        points += 2.0.pow(numbers.intersect(winningNumbers.toSet()).size - 1).toInt()
    }
    return points
}

fun processScratchcards(lines: List<String>): Int {
    val scratchcards = getCards(lines)
    val mapCards = mutableMapOf<Int, Card>()
    for (card in scratchcards) {
        mapCards[card.id] = card
    }

    var numCards = scratchcards.size

    while (scratchcards.isNotEmpty()) {
        val card = scratchcards.poll()
        if (card.numMatching != 0) {
            var cardIndex = card.id + 1
            val untilCardIndex = cardIndex + card.numMatching
            while (cardIndex < untilCardIndex) {
                scratchcards.add(mapCards[cardIndex]!!)
                numCards++
                cardIndex++
            }
        }
    }

    return numCards
}

fun getCards(lines: List<String>): Queue<Card> {
    val scratchcards: Queue<Card> = LinkedList()
    for (line in lines) {
        val splitLine = line.split(":")
        val cardId = splitLine[0].split(" ").filterNot { it.isBlank() }[1]
        val splitNumbers = splitLine[1].split("|")
        val winningNumbers = splitNumbers[0].split(" ").filterNot { it.isBlank() }
        val numbers = splitNumbers[1].split(" ").filterNot { it.isBlank() }
        val numMatching = numbers.intersect(winningNumbers.toSet()).size
        scratchcards.add(Card(cardId.toInt(), numMatching))
    }
    return scratchcards
}


data class Card(
    val id: Int,
    var numMatching: Int
)