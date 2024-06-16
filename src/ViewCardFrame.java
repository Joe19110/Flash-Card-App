import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewCardFrame extends DefaultFrame implements CardPanel {
    public ViewCardFrame(Deck deck, ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {

        //Creating general panel
        JPanel generalPanel = new JPanel();
        generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));

        //Creating center card panel
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setPreferredSize(new Dimension(100, 100));
        cardPanel.setBackground(bgColor);
        Border border = BorderFactory.createCompoundBorder(new MatteBorder(150,150,100,150, systemColor), regBorder);
        cardPanel.setBorder(border);

        ArrayList<Card> cards = deck.getCards();
        Collections.shuffle(cards); //Card order is shuffled
        AtomicInteger index = new AtomicInteger();

        //Creating card panel contents
        JLabel wordLabel = new JLabel();
        JLabel defLabel = new JLabel();
        setWordAndDefinitionText(cards, index, wordLabel, defLabel);
        resetDefault(wordLabel, defLabel);
        cardPanel.add(wordLabel);
        cardPanel.add(defLabel);
        generalPanel.add(cardPanel);

        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            //Allows card to be flipped upon mouse click
            flipCard(wordLabel, defLabel);
            }
        });

        //Creating bottom button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        DefaultButton prevButton = new DefaultButton("<");
        DefaultButton nextButton = new DefaultButton(">");
        DefaultButton backButton = new DefaultButton("Back");
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(backButton);
        generalPanel.add(buttonPanel);
        add(generalPanel);

        prevButton.addActionListener(e -> {
            //Gets previous card if it exists
            if (index.get() != 0) {
                index.addAndGet(-1);
                setWordAndDefinitionText(cards, index, wordLabel, defLabel);
                resetDefault(wordLabel, defLabel);
            }
        });
        nextButton.addActionListener(e -> {
            //Gets next card if it exists
            if (index.get() != cards.size() - 1) {
                index.addAndGet(1);
                setWordAndDefinitionText(cards, index, wordLabel, defLabel);
                resetDefault(wordLabel, defLabel);
            }
        });
        backButton.addActionListener(e -> {
            dispose();
            new DeckFrame(deck, decks, deadlines);
        });

    }
    static void setWordAndDefinitionText(ArrayList<Card> cards, AtomicInteger index, JLabel wordLabel, JLabel defLabel) {
        wordLabel.setText("<html><div style='width: 330px; text-align: center;'>" + cards.get(index.get()).getWord() + "</div></html>");
        defLabel.setText("<html><div style='width: 330px; text-align: center;'>" + cards.get(index.get()).getDefinition() + "</div></html>");
    }


}
