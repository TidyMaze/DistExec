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
	public void miseAjour(  ) {
		this.setChanged();
		this.notifyObservers(  );
	}
	
	
	public ServeurDistExec getServeur() {
		return this.serveur;
	}
	
	
	public List<Commande> getListeCommande() throws SQLException {
		JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:sqlite:bd.sqlite");
		DatabaseHelper dbh = new DatabaseHelper(cs);
		return dbh.getCommandeDao().queryForAll();
	}
	
}
