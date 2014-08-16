import java.awt.Container;
import java.awt.GridBagConstraints;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;


public abstract class Vue extends JFrame implements Observer {

	// partie MVC
	private Model model;
	private Controleur controleur;
	
	// partie Graphique
	protected Container panneau;
	protected JButton bouton_start;
	protected JButton bouton_stop;
	protected JButton bouton_restart;
	
	
	
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
	}
	
	
	// partie Observeur
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
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
