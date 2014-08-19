package MonTableau;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class TableauJButtonEditor extends DefaultCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TableauJButtonEditor() {
		super( new JCheckBox() );
		// TODO Auto-generated constructor stub
	}

	@Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return (JButtonCommande)value;
    }

	/*
    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
        	System.out.println("touché");
            //JOptionPane.showMessageDialog( button , label + ": Ouch!");
        }
        isPushed = false;
        return isPushed;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
    */
}
