import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Hangman implements ActionListener {
   JFrame frame;
   JButton guessButton, playAgain, giveUp;
   JLabel label, guessedLabel, revealTries, livesLeft;
   JTextField textField;

   String computerWords[] = {
       "WATER",         "FIRE",       "EARTH",     "AIR",       "AIRBENDER",
       "GENTLEMAN",     "WOMAN",      "DEATH",     "LUCK",      "CUP",
       "KEYBOARD",      "MOUSE",      "COMPUTER",  "LAPTOP",    "HUMAN",
       "DOG",           "FUZZY",      "FAFFED",    "BUZZ",      "FOX",
       "BUMMING",       "OVERJOYING", "SUFFERING", "STAFF",     "COZY",
       "QUEUING",       "SYNTHETIC",  "PUZZLE",    "BUBBLY",    "WELL",
       "BOOK",          "BOOKSHELF",  "FAFF",      "LIVID",     "PHYLUM",
       "DIZZY",         "SADIQ",      "CRYPT",     "ENCRYPT",   "DECRYPT",
       "CALLIGRAPHY",   "EQUIP",      "ABYSS",     "COBWEB",    "SOAPY",
       "BOXCAR",        "DWARVES",    "COCKINESS", "TRIANGLE",  "BIOLOGY",
       "FISHHOOK",      "BASTARD",    "GROSS",     "FERVID",    "HARDCORE",
       "PIXEL",         "MEMORY",     "POETRY",    "WRITE",     "UGANDA",
       "NEIGHBOURHOOD", "MEMENTO",    "BLIZZARD",  "SHAGGINESS"};
   char proposedLetter;
   int lives = 6, attempts = 0;
   Random random = new Random();

   String word = computerWords[random.nextInt(computerWords.length)];
   LinkedList<Character> wordLetters = new LinkedList<>();
   LinkedList<Character> guessedSoFar = new LinkedList<>();
   LinkedList<Character> guesses = new LinkedList<>();

   Hangman() {
      Font buttonFont = new Font("Constantia", Font.PLAIN, 35);
      Font labelFont = new Font("Constantia", Font.PLAIN, 20);

      textField = new JTextField("Enter a letter");
      textField.setFont(buttonFont);

      label = new JLabel();
      label.setFont(labelFont);

      livesLeft = new JLabel();
      livesLeft.setFont(labelFont);

      revealTries = new JLabel();
      revealTries.setFont(labelFont);
      revealTries.setText(guesses.toString());

      guessedLabel = new JLabel();
      guessedLabel.setText(guessedSoFar.toString());
      guessedLabel.setFont(labelFont);

      guessButton = new JButton("Guess the letter");
      guessButton.addActionListener(this);
      guessButton.setFont(buttonFont);
      guessButton.setBackground(Color.white);
      guessButton.setFocusable(false);

      playAgain = new JButton("Play again");
      playAgain.addActionListener(this);
      playAgain.setFont(buttonFont);
      playAgain.setBackground(Color.white);
      playAgain.setVisible(false);
      playAgain.setFocusable(false);

      giveUp = new JButton("Give up");
      giveUp.addActionListener(this);
      giveUp.setFont(buttonFont);
      giveUp.setBackground(Color.white);
      giveUp.setFocusable(false);

      frame = new JFrame();
      frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
      frame.setSize(new Dimension(700, 360));
      frame.setBackground(Color.white);

      for (char letter : word.toCharArray()) {
         wordLetters.add(letter);
      }
      for (char a : word.toCharArray()) {
         guessedSoFar.add('_');
      }
      for (int i = 0; i < wordLetters.size(); i++) {
         Character firstLetter = wordLetters.get(0);
         if (wordLetters.get(i).equals(firstLetter)) {
            guessedSoFar.set(i, firstLetter);
            // This gives out the first letter throughout the entire word as a hint.
         }
      }
      guesses.add(wordLetters.get(0));
      guessedLabel.setText(guessedSoFar.toString());

      frame.add(guessedLabel);
      frame.add(livesLeft);
      frame.add(textField);
      frame.add(guessButton);
      frame.add(giveUp);
      frame.add(playAgain);
      frame.add(label);
      frame.add(revealTries);
      frame.setVisible(true);
   }
  public void actionPerformed(ActionEvent e) {
      boolean guessedTheLetter = false;
      if (e.getSource() == guessButton) {
         if (textField.getText().length() > 1 || textField.getText().matches("[0-9]") ||
             textField.getText().matches("[a-z]") || textField.getText().equals(" ") ||
             textField.getText().equals("")) {
            label.setText("Please enter a capital letter [A] only.");
            return;
         }
         label.setText("");
         char proposedLetter = textField.getText().charAt(0);

         if (guesses.contains(proposedLetter)) {
            label.setText("This letter has been tried before, try something else.");
            return;
         } else {
            guesses.add(proposedLetter);
            attempts++;
         }

         for (int i = 0; i <= wordLetters.size() - 1; i++) {
            if (wordLetters.get(i) == proposedLetter) {
               guessedSoFar.set(i, proposedLetter);
               guessedTheLetter = true;
            }
         }

         if (guessedTheLetter) {
            label.setText("The letter " + proposedLetter + " is part of the word.");
         } else {
            label.setText("The letter " + proposedLetter + " isn't part of the word.");
            lives = lives - 1;
         }
         textField.setText("");

         if (lives == 0) {
            label.setText("The word was: " + word);
            livesLeft.setText("Lives left: 0");
            textField.setEnabled(false);
            guessButton.setVisible(false);
            giveUp.setVisible(false);
            playAgain.setVisible(true);
            return;
         }
         if (guessedSoFar.toString().equals(wordLetters.toString())) {
            label.setText("Congratulations, you have won! The word was: " + word);
         }
         if (guessedSoFar.toString().equals(wordLetters.toString())) {
            textField.setEnabled(false);
            guessButton.setVisible(false);
            giveUp.setVisible(false);
            playAgain.setVisible(true);
         }

         revealTries.setText("Attempts [" + attempts + "]: " + guesses.toString());
         guessedLabel.setText("Progress: " + guessedSoFar.toString());
         livesLeft.setText("Lives left: " + lives);
      }

      if (e.getSource() == giveUp) {
         textField.setEnabled(false);
         playAgain.setVisible(true);
         giveUp.setVisible(false);
         guessButton.setVisible(false);
         attempts = 0;
         label.setText("The word was: " + word);
      }
      if (e.getSource() == playAgain) {
         guessedSoFar.clear();
         guesses.clear();
         wordLetters.clear();

         attempts = 0;
         lives = 6;

         random = new Random();
         word = computerWords[random.nextInt(computerWords.length)];
         for (char letter : word.toCharArray()) {
            wordLetters.add(letter);
         }
         for (char a : word.toCharArray()) {
            guessedSoFar.add('_');
         }
         for (int i = 0; i < wordLetters.size(); i++) {
            Character firstLetter = wordLetters.get(0);
            if (wordLetters.get(i).equals(firstLetter)) {
               guessedSoFar.set(i, firstLetter);
            }
         }
         guesses.add(wordLetters.get(0));
         guessedLabel.setText(guessedSoFar.toString());

         revealTries.setText("");
         guessedLabel.setText(guessedSoFar.toString());
         label.setText("");
         livesLeft.setText("");

         guessButton.setVisible(true);
         giveUp.setVisible(true);
         textField.setEnabled(true);
         playAgain.setVisible(false);
      }
   }
}
