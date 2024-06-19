import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DeckFrame extends DefaultFrame {
    public DeckFrame(Deck deck, ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {

        //Sets layout of general window
        setLayout(new GridBagLayout());
        GridBagConstraints leftConstraints = new GridBagConstraints();
        leftConstraints.gridx = 0;
        leftConstraints.gridy = 0;
        leftConstraints.weightx = 1.0;
        leftConstraints.weighty = 1.0;
        leftConstraints.fill = GridBagConstraints.BOTH;
        GridBagConstraints rightConstraints = new GridBagConstraints();
        rightConstraints.gridx = 1;
        rightConstraints.gridy = 0;
        rightConstraints.weightx = 5.0;
        rightConstraints.weighty = 1.0;
        rightConstraints.fill = GridBagConstraints.BOTH;

        //Left section

        //Sets layout of left section
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints Constraints1 = new GridBagConstraints();
        Constraints1.gridx = 0;
        Constraints1.gridy = 0;
        Constraints1.weightx = 1.0;
        Constraints1.weighty = 1.0;
        GridBagConstraints Constraints2 = new GridBagConstraints();
        Constraints2.gridx = 0;
        Constraints2.gridy = 1;
        Constraints2.weightx = 1.0;
        Constraints2.weighty = 1.0;
        GridBagConstraints Constraints3 = new GridBagConstraints();
        Constraints3.gridx = 0;
        Constraints3.gridy = 2;
        Constraints3.weightx = 1.0;
        Constraints3.weighty = 1.0;
        GridBagConstraints Constraints4 = new GridBagConstraints();
        Constraints4.gridx = 0;
        Constraints4.gridy = 3;
        Constraints4.weightx = 1.0;
        Constraints4.weighty = 1.0;
        GridBagConstraints Constraints5 = new GridBagConstraints();
        Constraints5.gridx = 0;
        Constraints5.gridy = 4;
        Constraints5.weightx = 1.0;
        Constraints5.weighty = 4.0;

        //Creating buttons
        Border border = BorderFactory.createCompoundBorder(new MatteBorder(2,2,0,2, systemColor), regBorder);
        Border borderBottom = BorderFactory.createCompoundBorder(new MatteBorder(2,2,2,2, systemColor), regBorder);
        DefaultButton newWordButton = new DefaultButton("New Word");
        DefaultButton studyModeButton = new DefaultButton("Study");
        DefaultButton flashcardModeButton = new DefaultButton("View Flashcards");
        DefaultButton backButton = new DefaultButton("Back");
        newWordButton.setPreferredSize(new Dimension(150, 70));
        studyModeButton.setPreferredSize(new Dimension(150, 70));
        flashcardModeButton.setPreferredSize(new Dimension(150, 70));
        backButton.setPreferredSize(new Dimension(150, 70));
        JPanel bottomSpace = new JPanel();
        newWordButton.setBorder(border);
        studyModeButton.setBorder(border);
        flashcardModeButton.setBorder(border);
        backButton.setBorder(borderBottom);
        leftPanel.add(newWordButton, Constraints1);
        leftPanel.add(studyModeButton, Constraints2);
        leftPanel.add(flashcardModeButton, Constraints3);
        leftPanel.add(backButton, Constraints4);
        leftPanel.add(bottomSpace, Constraints5);
        add(leftPanel, leftConstraints);

        //Right section

        //Creating list of flashcards
        String[] flashcards = new String[deck.getCards().size()];
        for (int i = 0; i < deck.getCards().size(); i++) {
            flashcards[i] = deck.getCards().get(i).getWord() + "avalidseparator" + deck.getCards().get(i).getDefinition();
        }
        DefaultList<String> cardList = new DefaultList<>(flashcards);
        cardList.setCellRenderer(new CardListCellRenderer());
        cardList.setBorder(new EmptyBorder(0,10,0,10));

        //Set up right panel
        JScrollPane rightPanel = new JScrollPane(cardList);
        Border cardBorder = BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(new EmptyBorder(6, 3,6,6), regBorder), // Border color
                "CARDS",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                rightPanel.getFont().deriveFont(Font.BOLD, 20),
                borderColor
        );
        rightPanel.setBorder(cardBorder);
        add(rightPanel, rightConstraints);


        newWordButton.addActionListener(e -> {
            dispose();
            new NewCardFrame(deck, decks, deadlines);
        });
        studyModeButton.addActionListener(f -> {
            dispose();
            new StudyCardFrame(deck, decks, deadlines);
        });
        flashcardModeButton.addActionListener(f -> {
            dispose();
            new ViewCardFrame(deck, decks, deadlines);
        });
        backButton.addActionListener(f -> {
            dispose();
            new HomeFrame(decks, deadlines);
        });

       cardList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            int selectedCard = cardList.getSelectedIndex();
            if (selectedCard != -1) { //Checks if there is a selected card
                if (SwingUtilities.isRightMouseButton(e)) {

                    //Creating popup menu
                    JPopupMenu cardMenu = new JPopupMenu();
                    JMenuItem delItem = new JMenuItem("Delete Card");
                    JMenuItem editItem = new JMenuItem("Edit Card");
                    cardMenu.add(delItem);
                    cardMenu.add(editItem);

                    delItem.addActionListener(e1 -> {
                        int delAns = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Delete", JOptionPane.YES_NO_OPTION);
                        if (delAns == 0) {
                            deck.getCards().remove(selectedCard);
                            dispose();
                            new DeckFrame(deck, decks, deadlines);
                        }
                    });

                    editItem.addActionListener(e1 -> {
                        dispose();
                        new EditCardFrame(deck.getCards().get(selectedCard), deck, decks, deadlines);
                    });

                    cardMenu.show(cardList, e.getX(), e.getY()); //Ensures popup menu appears near cursor
                }
            }
            }
       });
    }

    //Edits appearance of list of cards in Deck window
    static class CardListCellRenderer extends NewDefaultListCellRenderer implements CellRendererOfCard {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String[] values = value.toString().split("avalidseparator");
            setText("<html><body><div style='padding: 10px'><div style='width: 500px; display: flex; justify-content: space-between; align-items: center; font-size: 12px;'>" + values[0] + "</div><div style='font-size: 10px; width: 420px; word-wrap: break-word'>" + values[1] + "</div></div></body></html>");
            renderer.setPreferredSize(new Dimension(500, getPreferredHeight(renderer)));
            if (cellHasFocus) {
                renderer.setBorder(selectedBorder);
            }
            return renderer;
        }
    }
}
