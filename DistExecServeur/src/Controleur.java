import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import BD.Commande;
import MonTableau.JButtonCommande;



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

		// on récupère le bouton 
		JButton bouton  = (JButton) e.getSource();
		
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
		
		
		// bouton ajouter/modifier/annuler (édition d'une commande)
		else if( e.getSource() == vue.bouton_ok ) {

			if( vue.getChampNom().length() <= 0 ) {
				System.out.println("champ nom vide");
				JOptionPane.showMessageDialog( JOptionPane.getFrameForComponent(bouton), "Vous n'avez pas donner de nom à votre commande" );
			}
			else if ( vue.getChampScript().length() <= 0 ) {
				System.out.println("pas de script");
				JOptionPane.showMessageDialog( JOptionPane.getFrameForComponent(bouton), "Vous n'avez pas choisis de script" );
			}
			else {
				
				String nom = vue.getChampNom();
				String description = vue.getChampDescription();
				String script = vue.getChampScript();
				
				try {
					// on modifie une commande déja existante
					if( vue.modifieUneCommande() ) {
						Commande c = vue.getCommandeAModifier();
						c.setNom( nom );
						c.setDescription(description);
						c.setScript(script);
						this.model.modifierCommande( c );
					}
					else // on ajoute une nouvelle commande
					{	
						this.model.ajouterCommande( new Commande( nom , description , script ) );
					}
					
				}
				catch( SQLException e1 ) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog( JOptionPane.getFrameForComponent(bouton), "Ce nom a déja été prit" );
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
		
		// bouton modifier/supprimer du tableau listant les commandes
		else if( bouton.getText().equals("Modifier") ) {
			// on prépare les champs pour que l'utilisateur puisse les modifier
			System.out.println("Modifier une commande");
			Commande commande = ( (JButtonCommande)bouton ).getCommande();
			vue.setChamps( commande );
		}
		else if( bouton.getText().equals("Supprimer") ) {
			System.out.println("Supprimer une commande");
			
			Commande commande = ( (JButtonCommande)bouton ).getCommande();
			try {
				this.model.supprimerCommande( commande );
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else  {
			// ne sais pas d'ou il vient
		}
		
		
	}
	
	
}
