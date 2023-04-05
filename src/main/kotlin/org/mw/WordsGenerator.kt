package org.mw

import org.mw.model.MarkovMultilevelModel
import org.mw.model.Word
import java.io.BufferedReader
import java.io.InputStream
import java.util.*

object WordsGenerator {

    @JvmStatic
    fun main(args: Array<String>) {
        val parameters = GeneratorParameters.parseArguments(args)
        val (model, words) = createModel(parameters)
        generateWords(model, words, parameters)
    }

    private fun createModel(parameters: GeneratorParameters): Pair<MarkovMultilevelModel, Set<String>> {
        if (!parameters.modelExists()) {
            val words = readWordsFromFiles(parameters.getDictionaryStream())
            val model = MarkovMultilevelModel(parameters.generate, parameters.getAlphabet())
            model.loadDictionary(words)
            model.saveToFile(parameters.getOutputModelFile())
            return Pair(model, words)
        }
        return Pair(MarkovMultilevelModel.readFromFile(parameters.getInputModelFile()), Collections.emptySet())
    }

    private fun readWordsFromFiles(inputStream: InputStream): Set<String> {
        val reader = BufferedReader(inputStream.reader())
        val result = HashSet<String>();
        reader.use { reader ->
            var line = reader.readLine()
            while (line != null) {
                result.add(line.lowercase())
                line = reader.readLine()
            }
        }
        return result;
    }

    private fun generateWords(
        model: MarkovMultilevelModel,
        dictionary: Set<String>,
        parameters: GeneratorParameters
    ) {
        if (parameters.run <= 0) {
            return
        }
        val wordsSet = TreeSet<Word>();
        var exists = 0;
        for (i in 0 until parameters.wordCount) {
            val word = model.getNextWord(parameters.run)
            if (dictionary.contains(word.value)) {
                exists++
            }
            wordsSet.add(word)
        }
        for (word in wordsSet) {
            println(word)
        }
        println("\n" + wordsSet.size + " unique items, $exists generated words were present in dictionary of size " + dictionary.size)
        val scores = wordsSet.groupBy { w -> w.score }
        println("Scores statistics: ")
        scores.entries.stream()
            .sorted(Comparator.comparing { (k, _) -> k })
            .forEach { (k, v) -> println("$k: " + v.size) }
    }
}