import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

public class SearchCardFrame extends DefaultFrame {
    public SearchCardFrame(ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {
        //Appends all cards to one arraylist
        ArrayList<Card> allCards = new ArrayList<>();
        for (Deck deck:decks) {
            allCards.addAll(deck.getCards());
        }
        int i = 0;
        String[] cards = new String[allCards.size()];
        for (Card card:allCards) {
            cards[i] = card.getWord() + "avalidseparator" + card.getDefinition();
            i++;
        }
        if (!allCards.isEmpty()) {
            setBounds(515, 163, 500, 500); //Launches window at the center

            //Creating general panel
            JPanel generalPanel = new JPanel();
            generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));

            //Creating top search bar panel
            JPanel searchPanel = new JPanel();
            searchPanel.setMaximumSize(new Dimension(500, 30));
            DefaultButton backButton = new DefaultButton("Back");
            backButton.setPreferredSize(new Dimension(50, 25));
            searchPanel.add(backButton);

            //Search bar panel contents
            JTextArea searchBar = new JTextArea();
            searchBar.setBorder(regBorder);
            searchBar.setPreferredSize(new Dimension(400, 25));
            searchPanel.add(searchBar);
            generalPanel.add(searchPanel);


            //Creating card list panel
            DefaultList<String> cardList = new DefaultList<>(cards);
            cardList.setCellRenderer(new SearchCardListCellRenderer());
            DefaultScrollPanel cardListPanel = new DefaultScrollPanel(cardList);
            generalPanel.add(cardListPanel);
            add(generalPanel);
            backButton.addActionListener(f -> {
                dispose();
            });
            searchBar.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {

                    //Separate list for filtered cards
                    ArrayList<Card> searchedCards = new ArrayList<>();
                    for (Card card : allCards) {
                        if (card.getWord().startsWith(searchBar.getText())) {
                            searchedCards.add(card);
                        }
                    }

                    //List is converted to Array to be used in JList
                    int i = 0;
                    String[] searchedCardsArray = new String[searchedCards.size()];
                    for (Card card : searchedCards) {
                        searchedCardsArray[i] = card.getWord() + "avalidseparator" + card.getDefinition();
                        i++;
                    }
                    DefaultList<String> searchedCardList = new DefaultList<>(searchedCardsArray);
                    searchedCardList.setCellRenderer(new SearchCardListCellRenderer());
                    if (!searchBar.getText().isEmpty()) { //When search bar is empty, it returns to display previous unfiltered list
                        cardListPanel.setViewportView(searchedCardList);
                    } else {
                        cardListPanel.setViewportView(cardList);
                    }
                    searchedCardList.repaint(); //Refreshes the list
                    searchedCardList.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            popupCardMenu(e, searchedCardList, decks, deadlines, allCards);
                        }
                    });
                }
            });
            cardList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    popupCardMenu(e, cardList, decks, deadlines, allCards);
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "Create a card first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    void popupCardMenu(MouseEvent e, JList<String> cardList, ArrayList<Deck> decks, ArrayList<Deadline> deadlines, ArrayList<Card> allCards) {
        int selectedCardIndex = cardList.getSelectedIndex();
        Card selectedCard;
        if (selectedCardIndex != -1) {
            String[] values = cardList.getSelectedValue().split("avalidseparator");
            selectedCard = allCards.stream().filter(card -> Objects.equals(card.getWord(), values[0])).findFirst().orElse(null);
            if (SwingUtilities.isRightMouseButton(e)) {
                JPopupMenu cardMenu = new JPopupMenu();
                JMenuItem delItem = new JMenuItem("Delete Card");
                JMenuItem editItem = new JMenuItem("Edit Card");
                cardMenu.add(delItem);
                cardMenu.add(editItem);
                delItem.addActionListener(e1 -> {
                    int delAns = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Delete", JOptionPane.YES_NO_OPTION);
                    if (delAns == 0) {
                        for (Deck deck:decks) {
                            if (deck.getCards().contains(selectedCard)) {
                                deck.getCards().remove(selectedCard);
                                break;
                            }
                        }
                        for (Frame frame : Frame.getFrames()) {
                            frame.dispose();
                        }
                        new HomeFrame(decks, deadlines);
                        new SearchCardFrame(decks, deadlines);
                    }
                });
                editItem.addActionListener(e1 -> {
                    for (Frame frame : Frame.getFrames()) {
                        frame.dispose();
                    }
                    new EditCardFrame(selectedCard, null, decks, deadlines);
                });
                cardMenu.show(cardList, e.getX(), e.getY());
            }
        }

    }
    //Edits appearance of list of searched cards in Search Card window
    static class SearchCardListCellRenderer extends NewDefaultListCellRenderer implements CellRendererOfCard {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String[] values = value.toString().split("avalidseparator");
            setText("<html><body><div style='padding: 10px'><div style='width: 350px; display: flex; justify-content: space-between; align-items: center; font-size: 12px;'>" + values[0] + "</div><div style='font-size: 10px; width: 335px; word-wrap: break-word'>" + values[1] + "</div></div></body></html>");
            renderer.setPreferredSize(new Dimension(300, getPreferredHeight(renderer)));
            if (cellHasFocus) {
                renderer.setBorder(selectedBorder);
            }
            return renderer;
        }
    }
}
