import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DefaultButton extends JButton {
    public DefaultButton(String text) {
        super(text);
        setPreferredSize(new Dimension(100, 50));
        setFocusable(false);
        setBorder(DefaultFrame.regBorder);
        setBackground(DefaultFrame.bgColor);
        setUI(new ButtonUI());

    }

}
class ButtonUI extends BasicButtonUI {
    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if (b.isContentAreaFilled()) {
            Color color = b.getBackground();
            g.setColor(color);
            g.fillRect(0, 0, b.getWidth(), b.getHeight());
        }
    }
    @Override
    protected void installListeners(AbstractButton button) {
        super.installListeners(button);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorder(new LineBorder(Color.BLACK, 3));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(DefaultFrame.regBorder);
            }
        });
    }

}

