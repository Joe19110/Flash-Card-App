import javax.swing.*;
import java.awt.*;

public class DefaultScrollPanel extends JScrollPane {
    public DefaultScrollPanel(Component view) {
        super(view);
        setBackground(DefaultFrame.systemColor);
    }
}
