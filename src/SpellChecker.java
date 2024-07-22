import java.io.BufferedReader;
//import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpellChecker {
    private String[] dictionary;
    private String[] wordleList;
    private String wordleAnswer;
  
    public String[] getDictionary() { return dictionary; }
    public String getWordleAnswer() { return wordleAnswer; }



    public SpellChecker(int wordLength) {
      try {
        dictionary = readLines("Words/dictionary.txt");
        wordleList = readLines("Words/WordleList.txt");
        if (wordLength == 5){
          // pick a random 5-letter word from the wordle answer list
          wordleAnswer = wordleList[(int)(Math.random() * wordleList.length)].toUpperCase();
        } else {
          // filter to only words of the specified length
          List<String> filteredList = new ArrayList<>();
          for (String word : dictionary) {
            if (word.length() == wordLength) {
              filteredList.add(word);
            }
          }
          wordleAnswer = filteredList.get((int)(Math.random() * filteredList.size())).toUpperCase(); // get answer?
        }
        //System.out.println("the ANSWER is: " + wordleAnswer);
      } catch (IOException e) {
        // print out the exception that occurred
        System.out.println("Unable to access "+e.getMessage());
      }
      
    }

    // check if input is a real english word
    public boolean isWord(String word) {
      for (String str: dictionary) {
        if (str.equalsIgnoreCase(word)) { return true; }
      }
      return false;
    }

    // analyze wordle guess by comparing it to the wordle answer
    public String[] analyzeGuess(String guess, String alphabet) {
      String result = "";
      for (int charIndex = 0; charIndex < wordleAnswer.length(); charIndex++) {
        char wordChar = wordleAnswer.charAt(charIndex);
        char guessChar = guess.charAt(charIndex);
        // if the character is guessed correctly, it turns green
        if (guessChar == wordChar) {
          result = result + Text.GREEN + wordChar + Text.RESET;
          alphabet = alphabet.replace(wordChar+"", Text.GREEN + wordChar + Text.RESET);
        }
        // if the character is in the word somewhere it wasn't guessed, it turns yellow
        else if (wordleAnswer.contains(guessChar+"") && !guessedAll(guessChar,guess)) {
          result = result + Text.YELLOW + guessChar + Text.RESET;
          if (!alphabet.contains(Text.GREEN + guessChar)) {
            alphabet = alphabet.replace(guessChar+"", Text.YELLOW + guessChar + Text.RESET);
          }
        }
        // else: the character is not in the word
        else {
          result = result + guessChar;
          if (!alphabet.contains(Text.GREEN + guessChar)) {
          alphabet = alphabet.replace(guessChar+"",Text.BLACK + guessChar + Text.RESET);
          }
        }
      }
      return new String[]{result,alphabet};
    }

    // count the number of times a character appears in a String
    private int countChar(char chr, String str) {
      int count = 0;
      for (int i=0; i<str.length(); i++) {
        if (str.charAt(i) == chr) {
          count++;
        }
      }
      return count;
    }

    // check if all occurrences of a specific character have been correctly guessed
    private boolean guessedAll(char chr, String guess) {
      int numGuessed = 0;
      for (int i=0; i<guess.length(); i++) {
        if (guess.charAt(i) == chr && guess.charAt(i) == wordleAnswer.charAt(i)) {
          numGuessed++;
        }
      }
      return numGuessed == countChar(chr, wordleAnswer);
    }

    // get contents of text files
    public static String[] readLines(String filename) throws IOException {
      //FileReader fileReader = new FileReader(filename);
      InputStream inputStream = Main.class.getResourceAsStream(filename);

      if (inputStream != null) {
          BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
          List<String> lines = new ArrayList<String>();
          String line;

          while ((line = reader.readLine()) != null) {
              lines.add(line);
          }

          reader.close();
          return lines.toArray(new String[lines.size()]);
      } else {
          Text.showError("An unknown error occurred when accessing the word list.");
          return null;
      }
    }
      
    
  }