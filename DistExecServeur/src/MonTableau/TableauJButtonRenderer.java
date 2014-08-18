package MonTableau;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TableauJButtonRenderer implements TableCellRenderer {        
    @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JButtonCommande button = (JButtonCommande)value;
        return button;  
    }
}