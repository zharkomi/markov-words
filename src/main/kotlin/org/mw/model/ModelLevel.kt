package org.mw.model

import java.io.Serializable
import java.util.*

class ModelLevel(private val depth: Int, private val alphabet: Alphabet) : Serializable {
    companion object {
        val random = Random()
    }

    private val probabilities: IntArray = IntArray(alphabet.length)
    private val children: Array<ModelLevel?>? = if (depth != 0) {
        Array(alphabet.length) { null }
    } else {
        null
    }

    fun parse(word: String) {
        for (i in word.indices) {
            parse(word, i)
        }
    }

    private fun parse(word: String, index: Int) {
        if (index == word.length) {
            return
        }
        val letter = word[index].code - alphabet.firstChar;
        if (letter in 0 until alphabet.length) {
            probabilities[letter]++
            if (children != null) {
                var level = children[letter]
                if (level == null) {
                    level = ModelLevel(depth - 1, alphabet)
                    children[letter] = level
                }
                level.parse(word, index + 1)
            }
        }
    }

    fun calculateProbabilities() {
        for (i in 1 until alphabet.length) {
            probabilities[i] = probabilities[i] + probabilities[i - 1];
        }
        if (children != null) {
            for (l in children) {
                l?.calculateProbabilities()
            }
        }
    }

    fun getLetter(word: CharArray, charIndex: Int, level: Int): Char {
        if (level == 0) {
            return getNextLetter()
        } else if (children != null) {
            val index = charIndex - level
            val child = children[word[index].code - alphabet.firstChar]
            if (child != null) {
                return child.getLetter(word, charIndex, level - 1);
            }
        }
        return 0.toChar();
    }

    private fun getNextLetter(): Char {
        if (probabilities[alphabet.length - 1] == 0) {
            return 0.toChar()
        }
        val r = random.nextInt(probabilities[alphabet.length - 1] + 1)
        var nextLetter = probabilities.binarySearch(r);
        if (nextLetter < 0) {
            nextLetter = -nextLetter - 1
        } else {
            while (nextLetter > 0 && r == probabilities[nextLetter - 1]) {
                nextLetter--
            }
        }
        return (nextLetter + alphabet.firstChar).toChar()
    }
}