import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public class EditCardFrame extends DefaultFrame implements FramesWithDeckBox {
    public EditCardFrame(Card card, Deck deck, ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {

        //Sets the layout of the window
        setLayout(new GridBagLayout());
        GridBagConstraints topConstraints = new GridBagConstraints();
        topConstraints.gridx = 0;
        topConstraints.gridy = 0;
        topConstraints.weightx = 1.0;
        topConstraints.weighty = 8.0;
        topConstraints.fill = GridBagConstraints.BOTH;
        GridBagConstraints bottomConstraints = new GridBagConstraints();
        bottomConstraints.gridx = 0;
        bottomConstraints.gridy = 1;
        bottomConstraints.weightx = 1.0;
        bottomConstraints.weighty = 1.0;
        bottomConstraints.fill = GridBagConstraints.BOTH;

        //Creating center panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new CompoundBorder(new EmptyBorder(30, 30, 50,30), regBorder));
        Font font = cardPanel.getFont().deriveFont(Font.PLAIN, 18);

        //Creating word panel
        JPanel wordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wordPanel.setBorder(new EmptyBorder(50,60,50,0));
        JLabel wordLabel = new JLabel("Word/Phrase:");
        wordLabel.setFont(font);
        wordLabel.setPreferredSize(new Dimension(130, 120));
        wordLabel.setVerticalAlignment(SwingConstants.TOP);
        JTextArea wordTextbox = new JTextArea(7,0);
        wordTextbox.setPreferredSize(new Dimension(400, 120));
        wordTextbox.setLineWrap(true);
        wordTextbox.setWrapStyleWord(true);
        wordTextbox.setBorder(regBorder);
        wordTextbox.setText(card.getWord());
        wordPanel.add(wordLabel);
        wordPanel.add(wordTextbox);

        //Creating definition panel
        JPanel defPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        defPanel.setBorder(new EmptyBorder(0,60,50,0));
        JLabel defLabel = new JLabel("Definition:");
        defLabel.setFont(font);
        defLabel.setPreferredSize(new Dimension(130, 120));
        defLabel.setVerticalAlignment(SwingConstants.TOP);
        JTextArea defTextbox = new JTextArea(7,0);
        defTextbox.setBorder(regBorder);
        defTextbox.setPreferredSize(new Dimension(400, 120));
        defTextbox.setLineWrap(true);
        defTextbox.setWrapStyleWord(true);
        defTextbox.setText(card.getDefinition());
        defPanel.add(defLabel);
        defPanel.add(defTextbox);

        cardPanel.add(wordPanel);
        cardPanel.add(defPanel);

        //Creating bottom button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonPanel.setBorder(new EmptyBorder(0,0,30,0));
        DefaultButton backButton = new DefaultButton("Back");
        DefaultButton prevButton = new DefaultButton("<");
        prevButton.setPreferredSize(new Dimension(50, 50));
        DefaultButton nextButton = new DefaultButton(">");
        nextButton.setPreferredSize(new Dimension(50, 50));
        DefaultButton saveCardButton = new DefaultButton("Save");

        //Adding the buttons
        buttonPanel.add(backButton);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(saveCardButton);

        //Creating combobox for deck names
        DefaultComboBox<String> deckBox = new DefaultComboBox<>(getDeckNames(decks));
        if (deck != null) {
            deckBox.setSelectedIndex(decks.indexOf(deck));
        } else {
            for (Deck selectedDeck:decks) {
                if (selectedDeck.getCards().contains(card)) {
                    deckBox.setSelectedIndex(decks.indexOf(selectedDeck));
                }
            }
        }
        buttonPanel.add(deckBox);

        getContentPane().add(cardPanel, topConstraints);
        getContentPane().add(buttonPanel, bottomConstraints);
        AtomicReference<Deck> currentDeck = new AtomicReference<>(decks.get(deckBox.getSelectedIndex()));
        AtomicInteger currentIndex = new AtomicInteger(currentDeck.get().getCards().indexOf(card));

        saveCardButton.addActionListener(e -> {
            if (!Objects.equals(wordTextbox.getText(), "") && !Objects.equals(defTextbox.getText(), "")) {
                card.setWord(wordTextbox.getText());
                card.setDefinition(defTextbox.getText());
                JOptionPane.showMessageDialog(null,"Card successfully saved.");
            } else {
                JOptionPane.showMessageDialog(null, "Word or Definition cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            if (deck != null) {
                dispose();
                new DeckFrame(deck, decks, deadlines);
            } else {
                dispose();
                new HomeFrame(decks, deadlines);
            }
        });

        prevButton.addActionListener(e -> {
            if (currentDeck.get().getCards().size() > 1) {
                if (currentIndex.get() != 0) {
                    currentIndex.getAndDecrement();
                } else {
                    currentIndex.set(currentDeck.get().getCards().size() - 1);
                }
                setText(wordTextbox, defTextbox, currentDeck, currentIndex);
            }
        });

        nextButton.addActionListener(e -> {
            if (currentDeck.get().getCards().size() > 1) {
                if (currentIndex.get() != currentDeck.get().getCards().size() - 1) {
                    currentIndex.getAndIncrement();
                } else {
                    currentIndex.set(0);
                }
                setText(wordTextbox, defTextbox, currentDeck, currentIndex);
            }
        });

        deckBox.addActionListener(e -> {
            if (!decks.get(deckBox.getSelectedIndex()).getCards().isEmpty()) {
                currentDeck.set(decks.get(deckBox.getSelectedIndex()));
                currentIndex.set(0);
                setText(wordTextbox, defTextbox, currentDeck, currentIndex);
            } else {
                JOptionPane.showMessageDialog(null, "Deck contains no cards.", "Error", JOptionPane.ERROR_MESSAGE);
                deckBox.setSelectedIndex(decks.indexOf(currentDeck.get()));
            }
        });

    }
    void setText(JTextArea wordTextbox, JTextArea defTextbox, AtomicReference<Deck> currentDeck, AtomicInteger currentIndex) {
        wordTextbox.setText(currentDeck.get().getCards().get(currentIndex.get()).getWord());
        defTextbox.setText(currentDeck.get().getCards().get(currentIndex.get()).getDefinition());
    }

}
