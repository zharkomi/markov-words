package org.mw.model

import java.io.Serializable

enum class Alphabet(
    val firstChar: Int,
    val length: Int
) : Serializable {
    EN('a'.code, 26),
    RU('а'.code, 33)
}