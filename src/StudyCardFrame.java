import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class StudyCardFrame extends DefaultFrame implements CardPanel {
    public StudyCardFrame(Deck deck, ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {

        //Getting cards from deck(s)
        ArrayList<Card> cards;
        if (deck == null) { //Checks if study all or study from one deck
            cards = new ArrayList<>();
            for (Deck eachDeck:decks) {
                cards.addAll(eachDeck.getCards());
            }
        } else {
            cards = deck.getCards();
        }

        //Filtering for cards to study
        ArrayList<Card> cardsToStudy = new ArrayList<>();
        int i;
        for (i = 0; i < cards.size(); i++) {
            if (cards.get(i).getStudyTime().isBefore(ZonedDateTime.now())) {
                cardsToStudy.add(cards.get(i));
            }
        }
        Collections.shuffle(cardsToStudy); //Shuffling of cards to study

        if (cardsToStudy.isEmpty()) { //If there is no card to study, window will be redirected back
            JOptionPane.showMessageDialog(null, "No cards to study, come back later!");
            dispose();
            if (deck != null) { //Checks from which window the study card window was launched from
                new DeckFrame(deck, decks, deadlines);
            } else {
                new HomeFrame(decks, deadlines);
            }
        } else {
            //Creating general panel
            JPanel generalPanel = new JPanel();
            generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));

            //Creating top panel containing back button
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            DefaultButton backButton = new DefaultButton("Back");
            topPanel.setBorder(new EmptyBorder(20,20,0,0));
            topPanel.add(backButton);
            generalPanel.add(topPanel);

            //Creating card panel
            JPanel cardPanel = new JPanel(new GridBagLayout());
            cardPanel.setPreferredSize(new Dimension(100, 100));
            Border border = BorderFactory.createCompoundBorder(new MatteBorder(100, 150, 100, 150, systemColor), regBorder);
            cardPanel.setBorder(border);
            cardPanel.setBackground(bgColor);

            //Creating card panel contents
            AtomicInteger index = new AtomicInteger();
            JLabel wordLabel = new JLabel();
            JLabel defLabel = new JLabel();
            ViewCardFrame.setWordAndDefinitionText(cardsToStudy, index, wordLabel, defLabel);
            resetDefault(wordLabel, defLabel); //Only word is shown
            cardPanel.add(wordLabel);
            cardPanel.add(defLabel);
            generalPanel.add(cardPanel);

            cardPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                flipCard(wordLabel, defLabel);
                }
            });

            //Creating bottom row of buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
            DefaultButton blankButton = new DefaultButton("???");
            DefaultButton badButton = new DefaultButton("Bad");
            DefaultButton okayButton = new DefaultButton("Okay");
            DefaultButton goodButton = new DefaultButton("Good");
            DefaultButton perfectButton = new DefaultButton("Perfect");
            buttonPanel.add(blankButton);
            buttonPanel.add(badButton);
            buttonPanel.add(okayButton);
            buttonPanel.add(goodButton);
            buttonPanel.add(perfectButton);
            generalPanel.add(buttonPanel);
            add(generalPanel);

            backButton.addActionListener(e -> {
                dispose();
                if (deck != null) {
                    new DeckFrame(deck, decks, deadlines);
                } else {
                    new HomeFrame(decks, deadlines);
                }
            });
            blankButton.addActionListener(e -> {
                updateCardStudyTime(0, index, cardsToStudy, wordLabel, defLabel, deck, decks, deadlines);

            });
            badButton.addActionListener(e -> {
                updateCardStudyTime(1, index, cardsToStudy, wordLabel, defLabel, deck, decks, deadlines);
            });
            okayButton.addActionListener(e -> {
                updateCardStudyTime(5, index, cardsToStudy, wordLabel, defLabel, deck, decks, deadlines);
            });
            goodButton.addActionListener(e -> {
                updateCardStudyTime(10, index, cardsToStudy, wordLabel, defLabel, deck, decks, deadlines);
            });
            perfectButton.addActionListener(e -> {
                int streak = cardsToStudy.get(index.get()).getPerfectStreak();
                if (streak == 0) {
                    cardsToStudy.get(index.get()).setStudyTime(ZonedDateTime.now().plusDays(1));
                } else {
                    cardsToStudy.get(index.get()).setStudyTime(ZonedDateTime.now().plusDays(streak * 2L));
                }
                cardsToStudy.get(index.get()).incrementPerfectStreak();
                checkOutOfBounds(index, cardsToStudy, wordLabel, defLabel, deck, decks, deadlines);
            });
        }
    }
    //Proceeds to the next card to study if it exists, and shows error if not.
    public void checkOutOfBounds(AtomicInteger index, ArrayList<Card> cardsToStudy, JLabel wordLabel, JLabel defLabel, Deck deck, ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {
        if (index.get() != cardsToStudy.size() - 1) {
            index.addAndGet(1);
            ViewCardFrame.setWordAndDefinitionText(cardsToStudy, index, wordLabel, defLabel);
            resetDefault(wordLabel, defLabel);
        } else {
            JOptionPane.showMessageDialog(null, "Study session is done.");
            dispose();
            if (deck != null) {
                new DeckFrame(deck, decks, deadlines);
            } else {
                new HomeFrame(decks, deadlines);
            }
        }

    }
    public void updateCardStudyTime(int min, AtomicInteger index, ArrayList<Card> cardsToStudy, JLabel wordLabel, JLabel defLabel, Deck deck, ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {
        cardsToStudy.get(index.get()).setStudyTime(ZonedDateTime.now().plusMinutes(min));
        checkOutOfBounds(index, cardsToStudy, wordLabel, defLabel, deck, decks, deadlines);
        decrementPerfectStreak(cardsToStudy, index);
    }
    //Checks if there is a perfect streak and decrements if it exists
    public void decrementPerfectStreak(ArrayList<Card> cardsToStudy, AtomicInteger index) {
        if (cardsToStudy.get(index.get()).getPerfectStreak() != 0){
            cardsToStudy.get(index.get()).decrementPerfectStreak();
        }
    }
}
