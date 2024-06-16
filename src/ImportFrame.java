import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class ImportFrame extends DefaultFrame implements FramesWithDeckBox {
    public ImportFrame(ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {

        //Sets layout of the window
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

        //Creating instructions label
        JPanel labelPanel = new JPanel();
        JLabel instructions = new JLabel("Paste flashcards below. (Set to tab with \\n\\n)");
        instructions.setPreferredSize(new Dimension(600, 50));
        labelPanel.setMaximumSize(instructions.getPreferredSize());
        labelPanel.add(instructions);

        //Creating area of text
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(new CompoundBorder(new EmptyBorder(30, 30, 50, 30), regBorder));
        JTextArea textbox = new JTextArea(10, 0);
        textbox.setBorder(regBorder);
        DefaultScrollPanel textScrollPanel = new DefaultScrollPanel(textbox);
        textScrollPanel.setMaximumSize(new Dimension(600, 500));
        textPanel.add(labelPanel);
        textPanel.add(textScrollPanel);

        //Creating bottom panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonPanel.setBorder(new EmptyBorder(0, 0, 30, 0));
        DefaultButton backButton = new DefaultButton("Back");
        DefaultButton importButton = new DefaultButton("Import");
        buttonPanel.add(backButton);
        buttonPanel.add(importButton);

        DefaultComboBox<String> deckBox = new DefaultComboBox<>(getDeckNames(decks));
        buttonPanel.add(deckBox);

        getContentPane().add(textPanel, topConstraints);
        getContentPane().add(buttonPanel, bottomConstraints);

        importButton.addActionListener(e -> {
            String[] cardString = textbox.getText().split("\n\n");
            for (String card:cardString) {
                String[] splitString = card.split("\\t");
                decks.get(deckBox.getSelectedIndex()).addCard(splitString[0], splitString[1]);
            }
            JOptionPane.showMessageDialog(null, "Successfully Imported");
        });

        backButton.addActionListener(e -> {
            dispose();
            new HomeFrame(decks, deadlines);
        });
    }
}

