import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import BD.Commande;
import MonTableau.JButtonCommande;
import MonTableau.TableModelCommande;



public class Controleur implements ActionListener {
	
	// attributs MVC
	private Model model;
	private ArrayList<Vue> vues;
	
	public Controleur( Model m ) {
		
		this.model = m;
		this.vues = new ArrayList<Vue>();
	}
	
	public void addVue( Vue vue ) {
		
		// ajout de la vue
		this.vues.add( vue );
		
		// ajout des listeners sur la vue
		
		vue.bouton_start.addActionListener( this );
		vue.bouton_stop.addActionListener( this );
		vue.bouton_restart.addActionListener( this );
		
		vue.bouton_ok.addActionListener( this );
		vue.bouton_annuler.addActionListener( this );
		vue.choisir_script.addActionListener( this );
		
		// afficher la vue
		vue.setVisible( true );
	}

	
	@Override
	public void actionPerformed(ActionEvent e ) {
		// TODO Auto-generated method stub
		
		JButton bouton  = (JButton) e.getSource();	// bouton qui a été actionné
		
		// on récupère la vue source de l'action 
		JFrame frame = (JFrame) SwingUtilities.getRoot(bouton);
		Vue vue = (Vue) frame;
		
		// bouton start/stop serveur
		if( e.getSource() == vue.bouton_start ) {
			this.model.startServer();
		}
		else if( e.getSource() == vue.bouton_stop ) {
			this.model.stopServer();
		}
		else if( e.getSource() == vue.bouton_restart ) {
			this.model.resartServer();
		}
		// bouton ajout/supp commande
		else if( e.getSource() == vue.bouton_ok ) {

			if( vue.getChampNom().length() <= 0 ) {
				System.out.println("champ nom vide");
				// prévenir l'user via l'ihm
			}
			else if ( vue.getChampScript().length() <= 0 ) {
				System.out.println("pas de script");
			}
			else {
				
				String nom = vue.getChampNom();
				String description = vue.getChampDescription();
				String script = vue.getChampScript();
				
				if( vue.modifieUneCommande() ) {
					
					Commande c = vue.getCommandeAModifier();
					c.setNom( nom );
					c.setDescription(description);
					c.setScript(script);
					
					try {
						this.model.modifierCommande( c );
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				else {
					
					try {
						this.model.ajouterCommande( new Commande( nom , description , script ) );
					} catch (SQLException exception ) {
						// impossible de créer la nouvelle commande dans la BD !!!
						// prévenir le client via l'interface
						// le nom est déja utilisé/pris
						exception.printStackTrace();
					}
					
				}			
			}
			
		}
		else if( e.getSource() == vue.bouton_annuler ) {
			vue.viderChamps();
			
		}
		else if( e.getSource() == vue.choisir_script) {
			
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog( vue );
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            System.out.println("Opening: " + file.getName() );
	            vue.champ_script.setText( file.toURI().toString() );
	        } else {
	        	System.out.println("Open command cancelled by user." );
	        	vue.champ_script.setText( "" );
	        }
			
		}
		else if( bouton.getText().equals("Modifier") ) {
			System.out.println("Modifier");
			Commande commande = ( (JButtonCommande)bouton ).getCommande();
			vue.modificationCommande( commande );
		}
		else if( bouton.getText().equals("Supprimer") ) {
			System.out.println("Supprimer");
			
			Commande commande = ( (JButtonCommande)bouton ).getCommande();
			try {
				this.model.supprimerCommande( commande );
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else  {

		}
		
		
	}
	
	
}
