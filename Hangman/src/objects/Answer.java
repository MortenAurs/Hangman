package objects;

import javax.swing.JOptionPane;

public class Answer {
	private String word;
	private int[] matchedIndex;
	private int totalFound = 0;
	private int letterCount = 0;
	private String[] usedLetters = new String[29];
	

	// Checking if letter is in the word
	public int[] checkLetter(String guessedLetter, String word) {
		usedLetters[letterCount] = guessedLetter;
		letterCount++;
		matchedIndex = new int[word.length()];
		int cntMatched = 0;
		for(int i = 0; i < word.length(); i++) {
			if(guessedLetter.toLowerCase().equals(String.valueOf(word.charAt(i)).toLowerCase())) {
				cntMatched++;
				totalFound++;
				matchedIndex[cntMatched] = i;	
			}
			// Sets first value in list to amount of letters found
			matchedIndex[0] = cntMatched;
		}
		
		// Checking if the total letters found are same length as the word
		if(totalFound == word.length()) {
			return null;
		}
		return matchedIndex;
	}
	
	// Setting counters and usedLetter-list to null
	public void clearBoard() {
		usedLetters = new String[29];
		totalFound = 0;
		letterCount = 0;
	}

	// Checking if letter has been searched before
	public boolean checkRepeatLetter(String guessedLetter) {
		if(usedLetters != null) {
			for(int i = 0; i < usedLetters.length; i++) {
				if(guessedLetter.equals(usedLetters[i])){
					JOptionPane.showMessageDialog(null, "You have already tried this letter. Please try again!");
					return true;
				}
			}
		}
		return false;
	}
	
	
	
}
