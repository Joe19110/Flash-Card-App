import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class NewCardFrame extends DefaultFrame implements FramesWithDeckBox{
    public NewCardFrame(Deck deck, ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {

        //Sets layout for the window
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

        //Creating the center card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new CompoundBorder(new EmptyBorder(30, 30, 50,30), regBorder));
        Font font = cardPanel.getFont().deriveFont(Font.PLAIN, 18);

        //Creating front part of the card
        JPanel wordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wordPanel.setBorder(new EmptyBorder(50,60,50,0));
        JLabel wordLabel = new JLabel("Word/Phrase:");
        wordLabel.setFont(font);
        wordLabel.setPreferredSize(new Dimension(130, 120));
        wordLabel.setVerticalAlignment(SwingConstants.TOP);
        JTextArea wordTextbox = new JTextArea(7,0);
        wordTextbox.setBorder(regBorder);
        wordTextbox.setPreferredSize(new Dimension(400, 120));
        wordTextbox.setLineWrap(true);
        wordTextbox.setWrapStyleWord(true);
        wordPanel.add(wordLabel);
        wordPanel.add(wordTextbox);

        //Creating back part of the card
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
        defPanel.add(defLabel);
        defPanel.add(defTextbox);
        cardPanel.add(wordPanel);
        cardPanel.add(defPanel);

        //Creating bottom button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonPanel.setBorder(new EmptyBorder(0,0,30,0));
        DefaultButton backButton = new DefaultButton("Back");
        DefaultButton saveNewCardButton = new DefaultButton("Save");
        buttonPanel.add(backButton);
        buttonPanel.add(saveNewCardButton);

        //Creating combobox to display decks
        DefaultComboBox<String> deckBox = new DefaultComboBox<>(getDeckNames(decks));
        if (deck != null) {
            deckBox.setSelectedIndex(decks.indexOf(deck));
        }
        buttonPanel.add(deckBox);

        getContentPane().add(cardPanel, topConstraints);
        getContentPane().add(buttonPanel, bottomConstraints);

        saveNewCardButton.addActionListener(e -> {
            if (!Objects.equals(wordTextbox.getText(), "") && !Objects.equals(defTextbox.getText(), "")) {
                decks.get(deckBox.getSelectedIndex()).addCard(wordTextbox.getText(), defTextbox.getText());
                JOptionPane.showMessageDialog(null,"Card successfully added.");
                wordTextbox.setText("");
                defTextbox.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Word or Definition cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            if (deck != null) {
                new DeckFrame(deck, decks, deadlines);
            } else {
                new HomeFrame(decks, deadlines);
            }
        });

    }
}
