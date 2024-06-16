import javax.swing.*;
import java.awt.*;

public class DefaultComboBox<String> extends JComboBox {
    public DefaultComboBox(String[] array) {
        super(array);
        setBackground(DefaultFrame.bgColor);
        setBorder(DefaultFrame.regBorder);
        setPreferredSize(new Dimension(200, 50));
    }

}
