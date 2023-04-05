package org.mw.model

import java.io.*

class MarkovMultilevelModel(
    private val depth: Int,
    alphabet: Alphabet
) : Serializable {
    private val root: ModelLevel = ModelLevel(depth, alphabet)

    fun loadDictionary(words: Set<String>) {
        for (w in words) {
            root.parse(w)
        }
        root.calculateProbabilities()
    }

    fun getNextWord(length: Int): Word {
        val word = CharArray(length)
        var score = 0;
        for (index in 0 until length) {
            for (level in depth downTo 0) {
                val minLevel = Math.min(level, index)
                val letter = root.getLetter(word, index, minLevel)
                if (letter.code != 0) {
                    word[index] = letter
                    score += minLevel
                    break;
                }
            }
        }
        return Word(score, String(word))
    }

    fun saveToFile(modelFile: OutputStream) {
        modelFile.use {
            val oos = ObjectOutputStream(it)
            oos.writeObject(this);
        }
    }

    companion object {
        fun readFromFile(modelFile: InputStream): MarkovMultilevelModel {
            modelFile.use {
                val ois = ObjectInputStream(it)
                return ois.readObject() as MarkovMultilevelModel
            }
        }
    }
}