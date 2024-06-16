import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class HomeFrame extends DefaultFrame {
    public HomeFrame(ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {
        //Creates menubar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");
        JMenuItem importItem = new JMenuItem("Import");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem newDeckItem = new JMenuItem("New Deck");
        JMenuItem newCardItem = new JMenuItem("New Card");
        JMenuItem deadlineItem = new JMenuItem("New Deadline");
        JMenuItem viewItem = new JMenuItem("Search cards");
        fileMenu.add(importItem);
        fileMenu.add(exitItem);
        editMenu.add(newDeckItem);
        editMenu.add(newCardItem);
        editMenu.add(deadlineItem);
        viewMenu.add(viewItem);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);

        importItem.addActionListener(e -> {
            if (!decks.isEmpty()) {
                dispose();
                new ImportFrame(decks, deadlines); //Launches Import window
            } else {
                JOptionPane.showMessageDialog(null, "Create a deck first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        exitItem.addActionListener(e -> {
            System.exit(0); //Terminates the program
        });
        newDeckItem.addActionListener(e -> {
            //Launches window that prompts user to add a new deck
            String newDeck = JOptionPane.showInputDialog(null, "Enter a name for the new deck.");
            if (newDeck != null && !newDeck.isEmpty()) {
                decks.add(new Deck(newDeck, new ArrayList<>()));
            } else {
                JOptionPane.showMessageDialog(null, "Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
            new HomeFrame(decks, deadlines);
        });
        newCardItem.addActionListener(e -> {
            //Launches New Card window if a deck exists
            dispose();
            if (!decks.isEmpty()) {
                new NewCardFrame(null, decks, deadlines);
            } else {
                JOptionPane.showMessageDialog(null, "Please create a deck first.","Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        deadlineItem.addActionListener(e -> {
            //Launches window that prompts user to add a new deadline
            //Creating separate models for day, month, year and time
            String nameOfDeadline = JOptionPane.showInputDialog(null, "Enter deadline name:");
            if (!Objects.equals(nameOfDeadline, "")) {
                SpinnerNumberModel dayModel = new SpinnerNumberModel(ZonedDateTime.now().getDayOfMonth(), 1, 31, 1);
                String[] monthList = new String[12];
                monthList[0] = "December";
                monthList[1] = "November";
                monthList[2] = "October";
                monthList[3] = "September";
                monthList[4] = "August";
                monthList[5] = "July";
                monthList[6] = "June";
                monthList[7] = "May";
                monthList[8] = "April";
                monthList[9] = "March";
                monthList[10] = "February";
                monthList[11] = "January";
                SpinnerListModel monthModel = new SpinnerListModel(monthList);
                monthModel.setValue(monthList[12 - ZonedDateTime.now().getMonthValue()]);
                SpinnerNumberModel yearModel = new SpinnerNumberModel(ZonedDateTime.now().getYear(), 2024, null, 1);
                SpinnerDateModel timeModel = new SpinnerDateModel(Calendar.getInstance().getTime(), null, null, Calendar.MINUTE);

                //Creating a spinner component for each model
                JPanel datePanel = new JPanel();
                datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
                JSpinner daySpinner = new JSpinner(dayModel);
                JSpinner monthSpinner = new JSpinner(monthModel);
                alignRight(monthSpinner);
                JSpinner yearSpinner = new JSpinner(yearModel);
                JSpinner timeSpinner = new JSpinner(timeModel);
                JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
                timeSpinner.setEditor(timeEditor);
                alignRight(timeSpinner);

                //Ensures that year formatting does not have comma (,)
                NumberFormat format = NumberFormat.getIntegerInstance();
                format.setGroupingUsed(false);
                NumberFormatter formatter = new NumberFormatter(format);
                formatter.setValueClass(Integer.class);
                formatter.setAllowsInvalid(false);
                JFormattedTextField textField = ((JSpinner.NumberEditor) yearSpinner.getEditor()).getTextField();
                textField.setFormatterFactory(new DefaultFormatterFactory(formatter));

                //Adding of spinners to the window panel
                datePanel.add(daySpinner);
                datePanel.add(monthSpinner);
                datePanel.add(yearSpinner);
                datePanel.add(timeSpinner);

                int result = JOptionPane.showConfirmDialog(null, datePanel, "Select Date and Time", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {

                    //Converts user input into Date format
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    int i, index = 0;
                    for (i = 0; i < 12; i++) {
                        if (monthList[i] == monthSpinner.getValue()) {
                            index = 12 - i;
                        }
                    }
                    String dateString = String.format("%04d-%02d-%02d %s",
                            (Integer) yearSpinner.getValue(),
                            index,
                            (Integer) daySpinner.getValue(),
                            new SimpleDateFormat("HH:mm").format(timeSpinner.getValue()) + ":00");
                    Date selectedDate;
                    try {
                        selectedDate = dateTimeFormat.parse(dateString);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }

                    //Validation that date has not passed
                    if (selectedDate.toInstant().atZone(ZoneId.systemDefault()).isAfter(ZonedDateTime.now())) {
                        Deadline newDeadline = new Deadline(nameOfDeadline, selectedDate.toInstant().atZone(ZoneId.systemDefault()));
                        deadlines.add(newDeadline);
                    } else {
                        JOptionPane.showMessageDialog(null, "Deadline is impossible", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            //Refreshes the Home window with changes
            dispose();
            new HomeFrame(decks, deadlines);

        });
        viewItem.addActionListener(e -> {
            //Launches Search Card window
            new SearchCardFrame(decks, deadlines);
        });

        //Sets layout for left and right sections
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
        rightConstraints.weightx = 15.0;
        rightConstraints.weighty = 1.0;
        rightConstraints.fill = GridBagConstraints.BOTH;

        //Left section
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        setLayout(new GridBagLayout());
        GridBagConstraints topConstraints = new GridBagConstraints();
        topConstraints.gridx = 0;
        topConstraints.gridy = 0;
        topConstraints.weightx = 1.0;
        topConstraints.weighty = 6.0;
        topConstraints.fill = GridBagConstraints.BOTH;
        GridBagConstraints bottomConstraints = new GridBagConstraints();
        bottomConstraints.gridx = 0;
        bottomConstraints.gridy = 1;
        bottomConstraints.weightx = 1.0;
        bottomConstraints.weighty = 1.0;
        bottomConstraints.fill = GridBagConstraints.BOTH;

        //Creating the list of deadlines
        String[] deadlineNames = new String[deadlines.size()];
        for (int i = 0; i < deadlines.size(); i++) {
            deadlineNames[i] = deadlines.get(i).getName() + "avalidseparator" + deadlines.get(i).getTime();
        }
        DefaultList<String> deadlineList = new DefaultList<>(deadlineNames);
        deadlineList.setCellRenderer(new DeadlineCellRenderer());

        //Set up of deadline panel
        DefaultScrollPanel deadlinesPanel = new DefaultScrollPanel(deadlineList);
        Border deadlinesBorder = BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(new EmptyBorder(6, 6,3,3), regBorder),
                "DEADLINES",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                leftPanel.getFont().deriveFont(Font.BOLD, 20),
                borderColor
        );
        deadlinesPanel.setBorder(deadlinesBorder);

        //Ensures deadlines that are over are deleted immediately
        Timer timer = new Timer(1000, e -> {
            if (deadlines.removeIf(deadline -> deadline.getTime().isBefore(ZonedDateTime.now()))) {
                dispose();
                new HomeFrame(decks, deadlines);
            }
            deadlineList.repaint();
        });
        timer.start();

        //Set up of bottom left panel
        JPanel pendingPanel = new JPanel();
        pendingPanel.setLayout(new BoxLayout(pendingPanel, BoxLayout.Y_AXIS));
        Border pendingBorder = BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(new EmptyBorder(3, 6,6,3), regBorder), // Border color
                "PENDING",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                leftPanel.getFont().deriveFont(Font.BOLD, 20),
                borderColor
        );
        pendingPanel.setBorder(pendingBorder);

        //Counting number of pending cards
        int count = 0;
        for (Deck deck:decks) {
            for (Card card:deck.getCards()) {
                if (card.getStudyTime().isBefore(ZonedDateTime.now())) {
                    count++;
                }
            }
        }

        //Contents of bottom left panel
        JPanel textPanel = new JPanel();
        textPanel.setBorder(new EmptyBorder(10,0,0,0));
        JLabel pendingText = new JLabel(count + " cards pending");
        textPanel.add(pendingText);
        pendingPanel.add(textPanel);
        JPanel buttonPanel = new JPanel();
        DefaultButton studyAllButton = new DefaultButton("Study All");
        buttonPanel.add(studyAllButton);
        pendingPanel.add(buttonPanel);

        //Addition of the two panels to main left panel
        leftPanel.add(deadlinesPanel, topConstraints);
        leftPanel.add(pendingPanel, bottomConstraints);
        add(leftPanel, leftConstraints);

        studyAllButton.addActionListener(e -> {
            //Launches Study Card window
            dispose();
            new StudyCardFrame(null, decks, deadlines);
        });

        //Right section
        String[] deckNames = new String[decks.size()];
        for (int i = 0; i < decks.size(); i++) {
            deckNames[i] = decks.get(i).getName() + "avalidseparator" + decks.get(i).getCards().size();
        }
        DefaultList<String> deckList = new DefaultList<>(deckNames);
        deckList.setCellRenderer(new DeckListCellRenderer());
        deckList.setBorder(new EmptyBorder(4,0,0,0)); //Adjusts spacing for aesthetic purposes
        deckList.requestFocusInWindow(); //Ensures hovering of decks automatically creates a response

        //Creating main right panel
        JScrollPane rightPanel = new JScrollPane(deckList);
        Border deckBorder = BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(new EmptyBorder(6, 3,6,6),regBorder), // Border color
                "DECKS",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                rightPanel.getFont().deriveFont(Font.BOLD, 20),
                borderColor
        );
        rightPanel.setBorder(deckBorder);
        add(rightPanel, rightConstraints);

        //Refreshes the Home window
        revalidate();
        repaint();

        deckList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            int selectedDeckIndex = deckList.getSelectedIndex();
            Deck selectedDeck = decks.get(selectedDeckIndex);

            if (selectedDeckIndex != -1) { //Checks if there is a selected deck
                if (e.getClickCount() == 2) {
                    //Redirects user to Deck window according to the selected deck
                    new DeckFrame(selectedDeck, decks, deadlines);
                    dispose();
                }
                launchPopupMenuIfRightClick(deckList, decks, selectedDeck, decks, deadlines, e);
            }
            }
        });

        deckList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
            selectHoveredListItem(deckList, e);
            }
        });

        deadlineList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            int selectedDeadlineIndex = deadlineList.getSelectedIndex();
            Deadline selectedDeadline = deadlines.get(selectedDeadlineIndex);

            if (selectedDeadlineIndex != -1) { //Checks if there is a selected deadline
                launchPopupMenuIfRightClick(deadlineList, deadlines, selectedDeadline, decks, deadlines, e);
            }
            }
        });

        deadlineList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                selectHoveredListItem(deadlineList, e);
            }
        });

    }

    //Allows a popup menu to be created upon right click of a deck or deadline
    public void launchPopupMenuIfRightClick(JList list, ArrayList listItem, Object selected, ArrayList<Deck> decks, ArrayList<Deadline> deadlines, MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) { //Upon right-click

            //Creating popup menu
            JPopupMenu delMenu = new JPopupMenu();
            JMenuItem delItem = new JMenuItem("Delete");
            JMenuItem renameItem = new JMenuItem("Rename");
            delMenu.add(delItem);
            delMenu.add(renameItem);

            delItem.addActionListener(e1 -> {
                //Verification before deletion
                int delAns = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Delete", JOptionPane.YES_NO_OPTION);
                if (delAns == 0) {
                    listItem.remove(selected);
                    dispose();
                    new HomeFrame(decks, deadlines);
                }
            });

            renameItem.addActionListener(e1 -> {
                //Changes name of deck/deadline
                String newName = JOptionPane.showInputDialog(null, "Enter the new name.");
                if (!Objects.equals(newName, "")) {
                    if (listItem == decks) { //Checks whether it is a deck or deadline
                        Deck selectedDeck = (Deck) selected;
                        selectedDeck.setName(newName);
                    } else {
                        Deadline selectedDeadline = (Deadline) selected;
                        selectedDeadline.setName(newName);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                dispose();
                new HomeFrame(decks, deadlines);
            });

            delMenu.show(list, e.getX(), e.getY()); //Ensures popup menu appears near cursor
        }
    }

    //Ensures hovered list item is selected
    public void selectHoveredListItem(JList list, MouseEvent e) {
        int index = list.locationToIndex(e.getPoint());
        list.setSelectedIndex(index);
    }

    // Aligns spinner text to the right
    public void alignRight(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
        textField.setHorizontalAlignment(JTextField.RIGHT);
    }

    //Edits appearance of list of deadlines in Home window
    public static class DeadlineCellRenderer extends NewDefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String[] values = value.toString().split("avalidseparator");
            renderer.setPreferredSize(new Dimension(150, 70));
            ZonedDateTime deadlineTime = ZonedDateTime.parse(values[1], DateTimeFormatter.ISO_ZONED_DATE_TIME);
            Duration duration = Duration.of(ZonedDateTime.now().until(deadlineTime, ChronoUnit.SECONDS), ChronoUnit.SECONDS);
            long days = duration.toDays();
            long hours = duration.toHoursPart();
            long minutes = duration.toMinutesPart();
            long seconds = duration.toSecondsPart();
            String elapsedTimeString = String.format("%02d d %02d h %02d m %02d s", days, hours, minutes, seconds);
            renderer.setText("<html><body><div style='text-align: center; padding: 9px'>" + values[0] + "<br/>" + elapsedTimeString + "</div></body></html>");
            return renderer;
        }
    }
    //Edits appearance of list of decks in Home window
    static class DeckListCellRenderer extends NewDefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String[] values = value.toString().split("avalidseparator");
            setText("<html><body><div style='display: flex; justify-content: space-between; align-items: center; font-size: 12px;'>&nbsp;&nbsp;" + values[0] + "</div><div style='font-size: 10px'>&nbsp;&nbsp;&nbsp;" + values[1] + " card(s)</div></body></html>");
            renderer.setPreferredSize(new Dimension(500, renderer.getFontMetrics(renderer.getFont()).getHeight() + 40));
            if (cellHasFocus) {
                renderer.setBorder(selectedBorder);
            }
            return renderer;
        }
    }

}

