// TO DO:
// - Emoji output like the real Wordle
// - Give a warning if there are not very many words of the specified length in the dictionary

// KNOWN ISSUES:
// - If the word is guessed correctly on the FIRST try, it will not turn green
import java.util.*;
class Main {
  public static void main(String[] args) throws InterruptedException {
    
    Scanner scan = new Scanner(System.in);
    int wordLength = 5; // default: 5
    int numGuesses = 6; // default: 6

    if (wordLength > 29) {
      wordLength = 5; // reset to default if word length is too long
      System.out.println(Text.YELLOW + "Warning: Word length is too long! Defaulting to 5." + Text.RESET);
    }

    System.out.println(Text.CLEAR + "Wordle: You have 6 tries to guess the " + wordLength + " letter word!\n");
    SpellChecker checker = new SpellChecker(wordLength);

    String word = checker.getWordleAnswer();
    String alphabet = "QWERTYUIOP\nASDFGHJKL\n ZXCVBNM\n";
    String[] allGuesses = new String[numGuesses];
    //String result = "";

    // guess loop
    int guessIndex;
    for (guessIndex = 0; guessIndex < numGuesses; guessIndex++) {
      showHints(allGuesses, alphabet, guessIndex);
      System.out.print((guessIndex+1) + "/" + numGuesses + ": ");
      String guess = scan.nextLine().toUpperCase();
      if (!guess.matches("[A-Z]{"+word.length()+"}") || !checker.isWord(guess)) {
        guessIndex++;
        showHints(allGuesses, alphabet, guessIndex);
        Text.showError("Guess must be a real " + word.length() + "-letter word\n");
        Thread.sleep(1000); // waits 1000 milliseconds (1 second)
        guessIndex-=2;  continue; // returns to the same guess index after error
      }
      
      // guess is compared to wordle answer
      allGuesses[guessIndex] = checker.analyzeGuess(guess, alphabet)[0];
      alphabet = checker.analyzeGuess(guess, alphabet)[1];
      if (guess.equals(word)) {
        showHints(allGuesses, alphabet, guessIndex);
        System.out.print("\nCongratulations!\n\nGuesses: " + (guessIndex+1) + "/" + numGuesses);
        break;
      } else if (guessIndex+1 == numGuesses) {
        showHints(allGuesses, alphabet, guessIndex);
        System.out.print("You ran out of guesses!\nThe word is " + word);
      }
      
    } // end of guess loop
    
    scan.close();
  } // end of main method

  // show updated hints
  private static void showHints(String[] hints, String alphabet ,int guessIndex) {
    if (guessIndex > 0) {
      System.out.print(Text.CLEAR);
      System.out.println(alphabet);
      for (String s : hints) {
        if (s != null) {
          System.out.println(s);
        }
      }
    }
  }
  
  
}