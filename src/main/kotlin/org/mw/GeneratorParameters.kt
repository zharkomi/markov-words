package org.mw

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import org.mw.model.Alphabet
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths

class GeneratorParameters(
    private val language: String,
    private val dictionaryFile: String,
    private val dictionaryDir: String,
    private val modelDir: String,
    val generate: Int,
    val run: Int,
    val wordCount: Int
) {
    init {
        if(generate <= 0) {
            throw IllegalArgumentException("-g parameter should be grater than zero")
        }
    }
    fun getDictionaryStream(): InputStream {
        return FileInputStream(File("$dictionaryDir/$language/$dictionaryFile.txt"))
    }

    fun getAlphabet(): Alphabet {
        return Alphabet.valueOf(language);
    }

    fun getInputModelFile(): InputStream {
        return FileInputStream(File(getModelFileName()))
    }

    fun getOutputModelFile(): OutputStream {
        val file = File(getModelFileName())
        Files.createDirectories(Paths.get(getModelDir()));
        file.createNewFile()
        return FileOutputStream(file)
    }

    private fun getModelDir() = "$modelDir/$language"

    private fun getModelFileName() = getModelDir() + "/$dictionaryFile-$generate.model"
    fun modelExists(): Boolean {
        return File(getModelFileName()).exists()
    }

    companion object {
        fun parseArguments(args: Array<String>) : GeneratorParameters {
            val parser = ArgParser("WordsGenerator")
            val language by parser.option(ArgType.String, shortName = "l", description = "Language (EN/RU)").required()
            val dictionaryDir by parser.option(ArgType.String, shortName = "d", description = "Dictionary root folder").default("dicts")
            val dictionaryFile by parser.option(ArgType.String, shortName = "f", description = "Dictionary file name").required()
            val modelDir by parser.option(ArgType.String, shortName = "m", description = "Models storage root dir (models - default)").default("models")
            val generate by parser.option(ArgType.Int, shortName = "g", description = "Generate model from dictionary with specified number of levels").default(1)
            val run by parser.option(ArgType.Int, shortName = "r", description = "Run generated model and crate words of specified length").default(0)
            val wordCount by parser.option(ArgType.Int, shortName = "w", description = "Number of words to generate").required()
            parser.parse(args)
            return GeneratorParameters(language, dictionaryFile, dictionaryDir, modelDir, generate, run, wordCount)
        }
    }
}