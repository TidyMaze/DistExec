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
	
	
	/* partie serveur */
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
	
	public int getPort() {
		return this.serveur.getPort();
	}
	
	public String getIpLocal() {
		return this.serveur.getIpLocal();
	}
	
	
	// commandes / acces BD
	public List<Commande> getListeCommande() throws SQLException {
		JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:sqlite:bd.sqlite");
		DatabaseHelper dbh = new DatabaseHelper(cs);
		return dbh.getCommandeDao().queryForAll();
	}
	
	public void ajouterCommande( Commande commande ) throws SQLException {
		
		JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:sqlite:bd.sqlite");
		DatabaseHelper dbh = new DatabaseHelper(cs);
		dbh.getCommandeDao().create( commande );
		
		this.miseAjour( Code.UPDATE_LIST_CREATE_COMMANDE );
	}
	
	public void supprimerCommande( Commande commande ) throws SQLException {
		JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:sqlite:bd.sqlite");
		DatabaseHelper dbh = new DatabaseHelper(cs);
		dbh.getCommandeDao().delete(commande);
		
		this.miseAjour( Code.UPDATE_LIST_DELETE_COMMANDE );
	}
	
	public void modifierCommande( Commande commande ) throws SQLException {
		JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:sqlite:bd.sqlite");
		DatabaseHelper dbh = new DatabaseHelper(cs);
		dbh.getCommandeDao().update(commande);
		
		this.miseAjour( Code.UPDATE_LIST_UPDATE_COMMANDE );
	}
	
	
}
