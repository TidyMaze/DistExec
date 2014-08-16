import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;



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
		
		// afficher la vue
		vue.setVisible( true );
	}

	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		JButton bouton  = (JButton) arg0.getSource();	// bouton qui a été actionné
		
		// selon le nom du bouton :
		switch( bouton.getText() ) 
		{
		case "Start":
			this.model.startServer();
			break;
		case "Stop":
			this.model.stopServer();
			break;
			
		case "Restart":
			this.model.resartServer();	
			break;
			
		default:
			break;
		}
		
		if( bouton.getText().equals("start") ) {
			
		}
	}
	
	
}
