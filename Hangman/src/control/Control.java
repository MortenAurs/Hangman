package control;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Control {
	private Scanner file;
	private String fileName = "words.csv";
	private ArrayList<String> wordList = new ArrayList<>();
	private static Control single_instance = null; 
	
	public void readFile() {
		try {
            file = new Scanner(new File(fileName));
            String line = "";
            while(file.hasNextLine()){
                line = file.nextLine();
                wordList.add(line);
            }
		}catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }
	}
	
	public void addToList(String word) {
		wordList.add(word);
	}
	public void writeToFile(String word) {
		try {
			addToList(word);
			File file = new File("words.csv");
			FileWriter writer = new FileWriter("words.csv"); 
			for(String str: wordList) {
			  writer.write(str + System.lineSeparator());
			}
			writer.close();
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
	}
	public ArrayList<String> getList(){
		return wordList;
	}
	
	public static Control getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new Control(); 
  
        return single_instance; 
    } 
}
