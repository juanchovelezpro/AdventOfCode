package juanchovelezpro

import java.io.File

fun main() {
    val file = File(ClassLoader.getSystemResource("day7.txt").file)
    val lines = file.readLines()
    println(totalWinnings(lines))
    println(totalWinnings(lines, withJoker = true))
}


fun totalWinnings(lines: List<String>, withJoker: Boolean = false): Long {

    var totalWinnings: Long = 0
    val hands = getHands(lines, withJoker)
    hands.sort()
    println(hands)

    for ((index, hand) in hands.withIndex()) {
        totalWinnings += hand.bid * (index + 1)
    }
    return totalWinnings
}

// Remapping A K Q J T as numbers 14 13 12 11 10 to be able to compare with numbers 9 8 7 6 5 4 3 2
fun getHands(lines: List<String>, withJoker: Boolean = false): MutableList<Hand> {
    val hands = mutableListOf<Hand>()
    val mapping = mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'J' to if (!withJoker) 11 else 1,
        'T' to 10
    )
    for (line in lines) {
        val split = line.split(" ")
        val cardsString = split[0]
        val type = handType(cardsString, mapping, withJoker)
        val cards = cardsString.toList().map {
            if (it.isDigit()) CamelCard(it, it.digitToInt())
            else {
                CamelCard(it, mapping[it]!!)
            }
        }
        val bid = split[1].toLong()
        hands.add(Hand(cards, type, bid))
    }

    return hands
}

fun handType(cards: String, cardMapping: Map<Char, Int>, withJoker: Boolean = false): Int {

    val map = mutableMapOf<Char, Int>()

    for (card in cards) {
        if (!map.containsKey(card)) map[card] = 1
        else {
            map[card] = map[card]!! + 1
        }
    }

    if (withJoker && cards.contains("J")) {
        val sortedMapAsList = map.toList().filter { it.first != 'J' }.sortedBy { it.second }
        val jokerTimes = map.toList().filter { it.first == 'J' }[0].second
        val numGreatestRepeating =
            sortedMapAsList.filter { it.second == sortedMapAsList[sortedMapAsList.lastIndex].second }

        if (numGreatestRepeating.size == 1) {
            val keyToIncrement = numGreatestRepeating[0].first
            map[keyToIncrement] = map[keyToIncrement]!! + jokerTimes
            map.remove('J')
        } else if (numGreatestRepeating.size > 1) {
            val camelCards = numGreatestRepeating.map {
                CamelCard(
                    it.first,
                    if (it.first.isDigit()) it.first.digitToInt() else cardMapping[it.first]!!
                )
            }
            val keyToIncrement = camelCards.sorted()[camelCards.lastIndex].label
            map[keyToIncrement] = map[keyToIncrement]!! + jokerTimes
            map.remove('J')
        }


    }

    return when (map.size) {
        1 -> 6
        2 -> {
            if (map.any { entry -> entry.value == 4 }) 5
            else 4
        }

        3 -> {
            if (map.any { entry -> entry.value == 3 }) 3
            else 2
        }

        4 -> 1
        else -> 0
    }
}

class Hand(
    val cards: List<CamelCard>,
    val type: Int,
    val bid: Long,
) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        return if (type > other.type) 1
        else if (type < other.type) -1
        else {
            var equality = 0
            for (index in cards.indices) {
                val currentPointCard = cards[index].pointValue
                val otherPointCard = other.cards[index].pointValue
                if (currentPointCard > otherPointCard) {
                    equality = 1
                    break
                } else if (currentPointCard < otherPointCard) {
                    equality = -1
                    break
                } else continue
            }
            equality
        }
    }

    override fun toString(): String {
        return "$cards- $type"
    }
}

class CamelCard(
    val label: Char,
    val pointValue: Int,
) : Comparable<CamelCard> {
    override fun compareTo(other: CamelCard): Int {
        return if (pointValue > other.pointValue) 1
        else if (pointValue < other.pointValue) -1
        else 0
    }

    override fun toString(): String {
        return label + ""
    }
}

