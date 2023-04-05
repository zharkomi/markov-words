# Fake words generator

Based on a dictionary crates multilevel Markov model. And then generates new words which 'look like' words from the dictionary. 

# MarkovMultilevelModel class

Constructor takes two arguments:
- alphabet - first letter of alphabet and length
- depth - how many previous letters should be used for learning

loadDictionary(words: Set<String>) - builds model from a set of words

getNextWord(length: Int) - generates next word of a specified length

# WordsGenerator class

Driver class arguments:

    --language, -l -> Language (EN/RU) (always required) { String }
    --dictionaryDir, -d [dicts] -> Dictionary root folder { String }
    --dictionaryFile, -f -> Dictionary file name (always required) { String }
    --modelDir, -m [models] -> Models storage root dir (models - default) { String }
    --generate, -g [0] -> Generate model from dictionary with specified number of levels { Int }
    --run, -r -> Run generated model and crate words of specified length (always required) { Int }
    --wordCount, -w -> Number of words to generate (always required) { Int }
    --help, -h -> Usage info 

Example: 

WordsGenerator -l EN -f all_words -g 4 -r 7 -w 10000

Gradle:

gradle run --args="-l EN -f all_words -g 4 -r 7 -w 10000"

# Parameters explanation

- The most important parameter is -g, it specifies depth of the model. Means that how many letters to take into account for generating next one. If number of levels is too small, 1 or 2 for examole, words will not follow dictionary patterns. If it is too big, close to words length, variability of results will be smaller, more generated words will be found in dictionary. 
- If both parameters -g and -r are used, then model will be crated, stored and used for generating words.
- If model is present it will be loaded from disk

# Dictionaries and models

Dictionaries are used to create  models, they are stored in dicts/{alphabet}/{file_name}.txt. Several dictionaries already included. For each dictionary model is stored in models/{alphabet}/{file_name}_{depth}.model