import javax.swing.*;

public interface CardPanel {
    default void flipCard(JLabel wordLabel, JLabel defLabel) {
        if (defLabel.isVisible()) {
            resetDefault(wordLabel, defLabel);
        } else {
            defLabel.setVisible(true);
            wordLabel.setVisible(false);
        }
    }
    default void resetDefault(JLabel wordLabel, JLabel defLabel) {
        defLabel.setVisible(false);
        wordLabel.setVisible(true);
    }
}
