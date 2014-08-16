import java.sql.SQLException;
import java.util.List;
import java.util.Observable;

import BD.Commande;
import BD.DatabaseHelper;

import com.j256.ormlite.jdbc.JdbcConnectionSource;



public class Model extends Observable {
	
	private ServeurDistExec serveur;
	
	public Model() {
		
		this.serveur = new ServeurDistExec();
	}
	
	
	
	/* partie Observable */
	public void miseAjour( Code code ) {
		this.setChanged();
		this.notifyObservers( code );
	}
	
		
	public void startServer() {
		this.serveur.start();
		this.miseAjour( Code.START_SERVER );
	}
	
	public void stopServer() {
		this.serveur.stop();
		this.miseAjour( Code.STOP_SERVER );
	}
	
	public void resartServer() {
		this.serveur.restart();
		this.miseAjour( Code.RESTART_SERVER );
	}
	
	
	public List<Commande> getListeCommande() throws SQLException {
		JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:sqlite:bd.sqlite");
		DatabaseHelper dbh = new DatabaseHelper(cs);
		return dbh.getCommandeDao().queryForAll();
	}
	
}
