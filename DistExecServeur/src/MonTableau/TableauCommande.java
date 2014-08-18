package MonTableau;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import BD.Commande;



public class TableauCommande extends JTable {
	
	private ActionListener controleur;
	
	public TableauCommande( List<Commande> listeCommande , ActionListener controleur ) {
		super( new TableModelCommande( listeCommande , controleur ) );
				
		this.getColumn("Modifier").setCellRenderer( new TableauJButtonRenderer() );
		this.getColumn("Modifier").setCellEditor( new TableauJButtonEditor() ); 
		
		
		this.getColumn("Supprimer").setCellRenderer( new TableauJButtonRenderer() );
		this.getColumn("Supprimer").setCellEditor( new TableauJButtonEditor() ); 
		
		
		this.setPreferredScrollableViewportSize( this.getPreferredSize());//thanks mKorbel +1 http://stackoverflow.com/questions/10551995/how-to-set-jscrollpane-layout-to-be-the-same-as-jtable
		this.getColumn("Modifier").setPreferredWidth(100);//so buttons will fit and not be shown butto..
		this.getColumn("Supprimer").setPreferredWidth(120);	
		
		
		this.controleur = controleur;
	}	
	
}