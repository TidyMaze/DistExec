package MonTableau;

import javax.swing.JButton;

import BD.Commande;

public class JButtonCommande extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Commande commande;
	
	public JButtonCommande( String titre , Commande commande ) {
		super(titre);
		
		this.commande = commande;
		
	}
	
	public Commande getCommande() {
		return this.commande;
	}
	
}
