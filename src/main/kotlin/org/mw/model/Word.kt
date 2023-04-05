package org.mw.model

class Word(val score: Int, val value: String) : Comparable<Word> {

    override fun compareTo(other: Word): Int {
        if(score != other.score) {
            return score - other.score;
        }
        return value.compareTo(other.value)
    }

    override fun toString(): String {
        return "$score $value";
    }

    override fun equals(other: Any?): Boolean {
        return value == (other as Word).value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}