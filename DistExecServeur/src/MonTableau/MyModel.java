package MonTableau;

import java.awt.event.*;  
import java.util.*;  
import javax.swing.table.*;  

import BD.Commande;

import java.util.List;  
   
public class MyModel extends AbstractTableModel {  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Commande> data = new ArrayList<Commande>();  
   
    @Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch( columnIndex ) 
		{
		case 0:
			return this.data.get(rowIndex).getNom();
		case 1:
			return this.data.get(rowIndex).getDescription();
		case 2:
			final JButtonCommande buttonModifier = new JButtonCommande( "Modifier" , this.data.get(rowIndex) );
			buttonModifier.addActionListener( new ActionListener(){   
	            public void actionPerformed(ActionEvent e) {  
	                 System.out.println("ok");
	                 JButtonCommande bouton = (JButtonCommande) e.getSource();
	                 deleteRow( bouton.getCommande() );
	            }  
	        }); 
            return buttonModifier;
		default:
			return "Erreur";
		}
		
	}
    
    @Override
    public boolean isCellEditable( int row, int column ) {
       // seulement les colonnes 3 et 4, car contiennent les boutons !
       return column == 2;
    }
    
    public int getColumnCount() {return nomColonne.length;}  
    public int getRowCount() {return data.size();}  
   
    public static String[] nomColonne = { "Nom" , "Description" , "Modifier" };
    
        
    public void addRow( Commande rowData ) {  
        int row = data.size();  
        data.add(rowData);  
        fireTableRowsInserted(row, row);  
        //fireTableDataChanged();
    }  
    
    
    public void deleteRow( Commande rowData ) {
    	data.remove( rowData );
    	//fireTableDataChanged();
    }
   
    @Override public String getColumnName(int columnIndex) {
        return nomColonne[columnIndex];
    }
    
    
    /*
    public static void main(String[] args) {  
        final MyModel model = new MyModel();  
        JTable tableau = new JTable(model);
        
		
        JButton button = new JButton("add row");  
        button.addActionListener(new ActionListener(){  
            int count;  
            public void actionPerformed(ActionEvent e) {  
                model.addRow(new  Commande("nom "+count++,"desc","script"));  
            	
            }  
        });  
        JFrame f = new JFrame("MyModel");  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        Container cp = f.getContentPane();  
        
        
        tableau.getColumn("Modifier").setCellRenderer( new TableauJButtonRenderer() );
        tableau.getColumn("Modifier").setCellEditor( new TableauJButtonEditor() ); 
        
        
        cp.add(new JScrollPane(tableau), BorderLayout.CENTER);  
        cp.add(button, BorderLayout.SOUTH);  
        f.pack();  
        f.setLocationRelativeTo(null);  
        f.setVisible(true);  
    } 
    */
    
} 