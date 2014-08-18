package MonTableau;

import javax.swing.JButton;

import BD.Commande;

public class JButtonCommande extends JButton {

	private Commande commande;
	
	public JButtonCommande( String titre , Commande commande ) {
		super(titre);
		
		this.commande = commande;
		
	}
	
	public Commande getCommande() {
		return this.commande;
	}
	
}
