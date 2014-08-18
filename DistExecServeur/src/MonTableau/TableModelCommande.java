package MonTableau;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import BD.Commande;

public class TableModelCommande extends AbstractTableModel {

	private static final String[] nomColonne = new String[] { "Nom" , "Description" , "Script" , "Modifier" , "Supprimer" };
	private List<Commande> data;
	private ActionListener controleur;
	
	public TableModelCommande( List<Commande> listeCommande , ActionListener controleur ) {
		super();
		this.data = listeCommande;
		this.controleur = controleur;
	}
	
	@Override public String getColumnName(int columnIndex) {
        return nomColonne[columnIndex];
    }
	
	@Override
	public int getColumnCount() {
		return nomColonne.length;
	}

	@Override
	public int getRowCount() {
		return this.data.size();
	}

	@Override  
    public void fireTableDataChanged() {  
        super.fireTableDataChanged();  
    }  
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch( columnIndex ) 
		{
		case 0:
			return this.data.get(rowIndex).getNom();
		case 1:
			return this.data.get(rowIndex).getDescription();
		case 2:
			return this.data.get(rowIndex).getScript();
		case 3:
			final JButtonCommande buttonModifier = new JButtonCommande( "Modifier" , this.data.get(rowIndex) );
			buttonModifier.addActionListener( this.controleur );
			/*
			buttonModifier.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                	System.out.println("ModifierModifierModifierModifier");
                    JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(buttonModifier), 
                            "Button clicked for row " + buttonModifier.getCommande().getNom() );
                }
            });
            */
            return buttonModifier;
            
		case 4:
			final JButtonCommande buttonSupprimer = new JButtonCommande( "Supprimer" , this.data.get(rowIndex) );
			buttonSupprimer.addActionListener( this.controleur );
            return buttonSupprimer;
            
		default:
			return "Erreur";
		}
		
	}
	
	@Override
    public boolean isCellEditable( int row, int column ) {
       // seulement les colonnes 3 et 4, car contiennent les boutons !
       return column == 3 || column == 4;
    }
	
	public List<Commande> getData() {
		return this.data;
	}
		
}
