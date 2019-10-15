package window;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.Control;
import objects.Answer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class GameBoard extends JDialog {
	private Random random = new Random(); 
	private Answer answer = new Answer();
	private ArrayList<String> wordList;
	private Control control = Control.getInstance();
	private int cntWrongAnswers = 0;
	private JLabel lblCntWrongAnswers;
	private String word;
	private JPanel contentPane;
	private JPanel plhPanel;
	private JLabel[] placeholders;
	private JTextField txtInput;
	private JPanel imgPanel;
	private JLabel lblImg;
	
	 	

	public GameBoard(JFrame forelder) {
		super(forelder, "Play Hangman", true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(730, 300, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
	
	
		plhPanel = new JPanel();
		plhPanel.setBounds(69, 145, 310, 35);
		contentPane.add(plhPanel);
		
		imgPanel = new JPanel();
		imgPanel.setBounds(69, 30, 163, 118);
		contentPane.add(imgPanel);
		
		lblImg = new JLabel(new ImageIcon("images/hangman" + cntWrongAnswers + ".png"));
		GridBagConstraints gbc_lblImg = new GridBagConstraints();
		imgPanel.add(lblImg, gbc_lblImg);
	
		
		JLabel lblWrongAnswers = new JLabel("Wrong answers: ");
		lblWrongAnswers.setBounds(250, 30, 180, 15);
		contentPane.add(lblWrongAnswers);
		
		lblCntWrongAnswers = new JLabel("0");
		lblCntWrongAnswers.setBounds(368, 30, 66, 15);
		contentPane.add(lblCntWrongAnswers);
		
		
		GridBagLayout gbl_imgPanel = new GridBagLayout();
		gbl_imgPanel.columnWidths = new int[]{0, 0};
		gbl_imgPanel.rowHeights = new int[]{0, 0};
		gbl_imgPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_imgPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		imgPanel.setLayout(gbl_imgPanel);
		
		
		
		wordList = control.getList();
		// Checking that the list has content
		if(wordList.size() > 0) {
			int randomNumber = random.nextInt(wordList.size()); 
			word = wordList.get(randomNumber);
			
			placeholders = new JLabel[word.length()];
			addLabels();
			
			JLabel lblGuessLetter = new JLabel("Guess letter:");
			lblGuessLetter.setBounds(181, 191, 88, 15);
			contentPane.add(lblGuessLetter);		
			
			txtInput = new JTextField();
			txtInput.setBounds(203, 214, 40, 19);
			txtInput.setHorizontalAlignment(JTextField.CENTER);
			contentPane.add(txtInput);
			txtInput.setColumns(10);
			
			JButton btnTry = new JButton("Try!");
			btnTry.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String guessedLetter = "";
					
					guessedLetter = txtInput.getText();
					// Checks if the input is correct (any letter from the alphabet)
					if(guessedLetter.matches("^[a-zA-Z]+$") && guessedLetter.length() == 1) {
						boolean repeatLetter = answer.checkRepeatLetter(guessedLetter);
						// Checks if user have tried the letter before
						if(!repeatLetter) {
							int[] matchedIndex = answer.checkLetter(guessedLetter, word);
							// Checks if the mached index is null. This means that user found the word and game is over
							if(matchedIndex == null){
								
								answer.clearBoard();
								removeLabels();
								JOptionPane.showMessageDialog(null, "Congrats, you found the word!\n"
										+ "The word was: " + word);
																
								int randomNumber = random.nextInt(wordList.size()); 
								word = wordList.get(randomNumber);
								cntWrongAnswers = 0;
								changeImg();
								updateWrongAnswerCnt();
								addLabels();
								
								
							// Checks if the first entry in list is 0. If it is it means it didn't find the letter in the word
							}else if (matchedIndex[0] == 0) {
								cntWrongAnswers++;
								changeImg();
								updateWrongAnswerCnt();
								if(cntWrongAnswers == 6) {
									JOptionPane.showMessageDialog(null, "Sorry, you have used all your chances.\n"
											+ "The word was: " + word);
									cntWrongAnswers = 0;
									setVisible(false);
								}
							}else {
								for(int i = 1; i <= matchedIndex[0]; i++) {
									int index = matchedIndex[i];
									placeholders[index].setText(guessedLetter.toLowerCase());
								}
							}
						}
					}else {
						JOptionPane.showMessageDialog(null, "Please enter a single letter from the alphabet");
						txtInput.setText("");
						
					}

					txtInput.setText("");
					txtInput.requestFocus();
				}

				
			});
			btnTry.setBounds(5, 243, 436, 25);
			contentPane.add(btnTry);
			this.getRootPane().setDefaultButton(btnTry);
			setVisible(true);
		}else {
			JOptionPane.showMessageDialog(null, "Add something to the word-list.");
		}
		
	}
	
	

	private void removeLabels() {
		for(int i = 0; i < word.length(); i++) {
			plhPanel.remove(placeholders[i]);
			
		}
		updatePanel();
		
	}
	
	// Adds the labels with asterix' on the board
	private void addLabels() {
		placeholders = new JLabel[word.length()];
		String asterix = "*";
		for(int i = 0; i < word.length(); i++) {
			if(word.charAt(i) == ' ') {
				asterix = " ";
			}else {
				asterix = "* ";
			}
			placeholders[i] = new JLabel(asterix);
			plhPanel.add(placeholders[i]);
		}
		updatePanel();
		
	}
	
	// Forces the panel to update after adding or removing elements
	public void updatePanel() {
		revalidate();
		repaint();
	}
	
	// Updates the counter for wrong answer on the board
	public void updateWrongAnswerCnt() {
		lblCntWrongAnswers.setText(String.valueOf(cntWrongAnswers));
	}
	
	private void changeImg() {
		try {
			lblImg.setIcon( new ImageIcon(ImageIO.read( new File("images/hangman" + cntWrongAnswers + ".png") ) ) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
