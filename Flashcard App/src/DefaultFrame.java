import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;

public class DefaultFrame extends JFrame {
    static Color systemColor = UIManager.getColor("control");
    static Color borderColor = Color.BLACK;
    static Color bgColor = Color.WHITE;
    static Border space = BorderFactory.createLineBorder(systemColor, 4);
    static Border regBorder = BorderFactory.createLineBorder(borderColor, 2);
    static Border cellBorder = BorderFactory.createCompoundBorder(space, regBorder);
    static Border selectedBorder = BorderFactory.createCompoundBorder(space, new LineBorder(borderColor, 3));

    public DefaultFrame() {
        setBounds(365, 13, 800, 800);
        setVisible(true);
        setBackground(bgColor);
        try {
            Image icon = ImageIO.read(new File("icon.png"));
            setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
