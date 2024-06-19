import javax.swing.*;
import java.awt.*;

public class DefaultList<E> extends JList<E> {
    public DefaultList(E[] array) {
        super(array);
        setCellRenderer(new NewDefaultListCellRenderer());
        setBackground(DefaultFrame.systemColor);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
 class NewDefaultListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        renderer.setBackground(DefaultFrame.bgColor);
        renderer.setBorder(DefaultFrame.cellBorder);
        return renderer;
    }
}
