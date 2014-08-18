import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import BD.Commande;
import MonTableau.TableModelCommande;
import MonTableau.TableauCommande;


public abstract class Vue extends JFrame implements Observer {

	// partie MVC
	protected Model model;
	protected Controleur controleur;
	
	// partie Graphique
	protected Container panneau;
	
	protected JButton bouton_start;
	protected JButton bouton_stop;
	protected JButton bouton_restart;
	
	protected JLabel label_port_valeur;
	protected JLabel label_ipLocal_valeur;
	
	protected JButton bouton_ok = new JButton("OK");
	protected JButton bouton_annuler = new JButton("Annuler");
	protected TextField champ_nom;
	protected TextField champ_description;
	protected JButton choisir_script;
	protected JLabel champ_script;
	
	protected TableauCommande tableau;
	
	
	public Vue( Model m , Controleur c ) {
		super("DisExec Serveur");
		
		// partie MVC
		this.model = m;
		this.controleur = c;
		
		// partie Observeur
		this.model.addObserver( this );
	    
	    // this.setVisible(true) -> mis dans le controleur
		this.bouton_start = new JButton("Start");
		this.bouton_stop = new JButton("Stop");
		this.bouton_restart = new JButton("Restart");
		
		this.bouton_restart.setEnabled( false );
		this.bouton_stop.setEnabled( false );
		
		
		this.label_port_valeur = new JLabel("non définie");
		this.label_ipLocal_valeur = new JLabel("non définie");
		
		
		this.bouton_ok = new JButton("OK");
		this.bouton_annuler = new JButton("Annuler");
		this.champ_nom = new TextField();
		this.champ_description = new TextField();
		this.choisir_script = new JButton("Choisir script");
		this.champ_script = new JLabel("");
		
		
		try {
			List<Commande> listeCommande = this.model.getListeCommande();
			tableau = new TableauCommande( listeCommande , this.controleur  );	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 	
	}
	
	
	// partie Observeur
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		Model m = (Model)arg0;
		Code code = (Code)arg1;
		
		if( code == Code.START_SERVER ) {
			this.bouton_start.setEnabled(false);
			this.bouton_stop.setEnabled(true);
			this.bouton_restart.setEnabled(true);
			
			this.label_ipLocal_valeur.setText( "" + this.model.getIpLocal() );
			this.label_port_valeur.setText( "" + this.model.getPort() );
			
		}
		else if( code == Code.STOP_SERVER ) {
			this.bouton_start.setEnabled(true);
			this.bouton_stop.setEnabled(false);
			this.bouton_restart.setEnabled(false);
			
			this.label_ipLocal_valeur.setText( "non définie" );
			this.label_port_valeur.setText( "non définie" );
		}
		else if( code == Code.RESTART_SERVER ) {
			
			this.label_ipLocal_valeur.setText( "" + this.model.getIpLocal() );
			this.label_port_valeur.setText( "" + this.model.getPort() );
		}
		else if( code == Code.UPDATE_CREATE_COMMANDE ) {
			this.viderChamps();
			this.modifierDonneesTableau();
		}
		else if( code == Code.UPDATE_DELETE_COMMANDE ) {
			this.modifierDonneesTableau();		
		}
		else {

		}
	}
	
	public void modifierDonneesTableau() {
		System.out.println("modification tableau ");
		try {
			this.tableau.setModel( new TableModelCommande( this.model.getListeCommande() , controleur) );
			( (TableModelCommande) this.tableau.getModel() ).fireTableDataChanged();
			this.tableau.repaint();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void viderChamps() {
		this.champ_nom.setText("");
		this.champ_description.setText("");
		this.champ_script.setText("");
	}
	
	
	public String getChampNom() {
		return this.champ_nom.getText();
	}
	
	public String getChampDescription() {
		return this.champ_description.getText();
	}
	
	public String getChampScript() {
		return this.champ_script.getText();
	}
	
	
	public void donnerContrainte(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
		gbc.gridx=gx;
		gbc.gridy=gy;
		gbc.gridwidth=gw;
		gbc.gridheight=gh;
		gbc.weightx=wx;
		gbc.weighty=wy;
		gbc.fill=GridBagConstraints.BOTH;  
	}

	public void donnerContrainte(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy, int constraint) {  
		gbc.gridx=gx;
		gbc.gridy=gy;
		gbc.gridwidth=gw;
		gbc.gridheight=gh;
		gbc.weightx=wx;
		gbc.weighty=wy;
		gbc.fill=constraint;
	 }

}
