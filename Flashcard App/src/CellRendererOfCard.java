import javax.swing.*;
import java.awt.*;

public interface CellRendererOfCard {
    default int getPreferredHeight(JLabel renderer) {
        FontMetrics metrics = renderer.getFontMetrics(renderer.getFont());
        int textWidth = metrics.stringWidth(renderer.getText());
        int numLines = (int) Math.ceil((double) textWidth / 358);
        return 12 + 10 * numLines;
    }
}
