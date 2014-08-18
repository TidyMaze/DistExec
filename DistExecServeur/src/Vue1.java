import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import BD.Commande;

import MonTableau.TableModelCommande;
import MonTableau.TableauCommande;
import MonTableau.TableauJButtonEditor;
import MonTableau.TableauJButtonRenderer;



public class Vue1 extends Vue {


	public Vue1(Model m, Controleur c) {
		super(m, c);

		// partie graphique
		this.panneau = this.getContentPane();
		this.setSize( 500, 500 );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	// ferme la fenetre lors de l'appuis sur la croix rouge
		this.setLocationRelativeTo(null);							// on place la fenetre au centre de l'écran


		/*
		 *  panneau principale 
		 */
		GridBagLayout layout_principal = new GridBagLayout();
		this.panneau.setLayout(layout_principal);

		int hauteur_b1 = 30;
		int largeur_b1 = 50;

		// style de la bordure (couleur, forme ...)
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);


		GridBagLayout grille_b1 = new GridBagLayout();
		JPanel b1 = new JPanel(grille_b1);
		GridBagConstraints contrainte_b1 = new GridBagConstraints();
		donnerContrainte(contrainte_b1,0,0,1,1, largeur_b1, hauteur_b1 , GridBagConstraints.BOTH  );
		b1.setBorder( BorderFactory.createTitledBorder( loweredetched, "Serveur" )  );

		GridBagLayout grille_b2 = new GridBagLayout();
		JPanel b2 = new JPanel(grille_b2);
		GridBagConstraints contrainte_b2 = new GridBagConstraints();
		donnerContrainte(contrainte_b2,1,0,1,1, (100-largeur_b1) , (100-hauteur_b1) , GridBagConstraints.BOTH );
		b2.setBorder( BorderFactory.createTitledBorder( loweredetched, "Ajouter/Modifier" )  );


		GridBagLayout grille_b3 = new GridBagLayout();
		JPanel b3 = new JPanel(grille_b3);
		GridBagConstraints contrainte_b3 = new GridBagConstraints();
		donnerContrainte(contrainte_b3,0,1,2,1, 100, 100 );
		b3.setBorder( BorderFactory.createTitledBorder( loweredetched, "Liste des Commandes" )  );


		this.panneau.add( b1 , contrainte_b1 );
		this.panneau.add( b2 , contrainte_b2 );
		this.panneau.add( b3 , contrainte_b3 );

		/*
		 * panneau "Serveur"
		 */

		Label label_ipLocal = new Label( "ip local : " );
		Label label_port = new Label("port : ");
		

		GridBagLayout grille_b11 = new GridBagLayout(  );
		GridBagConstraints contrainte_b11 = new GridBagConstraints();
		JPanel b11 = new JPanel(grille_b11);
		donnerContrainte(contrainte_b11, 0,0, 1,1, 100 , 70 , GridBagConstraints.BOTH  );
		b1.add( b11 , contrainte_b11 );


		GridBagLayout grille_b12 = new GridBagLayout(  );
		GridBagConstraints contrainte_b12 = new GridBagConstraints();
		JPanel b12 = new JPanel(grille_b12);
		donnerContrainte(contrainte_b12, 0,1, 1,1, 100 , 5 , GridBagConstraints.BOTH );
		b1.add( b12 , contrainte_b12 );


		GridBagLayout grille_b13 = new GridBagLayout(  );
		GridBagConstraints contrainte_b13 = new GridBagConstraints();
		JPanel b13 = new JPanel(grille_b13);
		donnerContrainte(contrainte_b13, 0,2, 1,1, 100 , 25 , GridBagConstraints.BOTH  );
		b1.add( b13 , contrainte_b13 );


		// b11 contient les infos du serveur
		GridLayout grille_b111 = new GridLayout( 2 , 2 );
		JPanel b111 = new JPanel(grille_b111);
		b11.add( b111 );

		b111.add( label_ipLocal );
		b111.add( label_ipLocal_valeur );
		b111.add( label_port );
		b111.add( label_port_valeur );

		// b13 contient les boutons start/stop/restart?
		GridLayout grille_b131 = new GridLayout( 3 , 1 );
		JPanel b131 = new JPanel(grille_b131);

		GridBagConstraints contrainte_b113 = new GridBagConstraints();
		donnerContrainte(contrainte_b113, 0,0, 1,1, 100 , 100 , GridBagConstraints.BOTH );
		b13.add( b131 , contrainte_b113 );

		b131.add( bouton_start );
		b131.add( bouton_stop );
		b131.add( bouton_restart );


		/*
		 * panneau ajouter/modifier commande
		 */
		GridLayout grille_b21 = new GridLayout( 4 , 2 , 5 , 10 );
		JPanel b21 = new JPanel(grille_b21);
		GridBagConstraints contrainte_b21 = new GridBagConstraints();
		donnerContrainte(contrainte_b21, 1,0, 1,1, 100 , 100 , GridBagConstraints.BOTH );
		b2.add( b21 , contrainte_b21 );


		Label label_nom = new Label("nom : ");
		Label label_description = new Label("description :");
		//JFileChooser champ_script = new JFileChooser();
		

		b21.add( label_nom );
		b21.add( this.champ_nom );
		b21.add( label_description );
		b21.add( this.champ_description );
		b21.add( this.choisir_script );
		b21.add( new JScrollPane( this.champ_script , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ) );
		b21.add( this.bouton_ok );
		b21.add( this.bouton_annuler );


		/*
		 * panneau liste des commandes
		 */
		try {
			
			
			List<Commande> listeCommande = this.model.getListeCommande();
			
			TableauCommande tableau = new TableauCommande( listeCommande , this.controleur  );			
			JScrollPane scroll = new JScrollPane( tableau );
			
			GridBagConstraints contrainte_tableau = new GridBagConstraints();
			donnerContrainte(contrainte_tableau, 0,0, 1,1, 100 , 100 , GridBagConstraints.BOTH );
			b3.add( scroll , contrainte_tableau );
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		

		// réarrange la taille de la fenetre
		this.pack();

	}

}
