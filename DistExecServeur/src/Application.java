import java.sql.SQLException;

import BD.Commande;
import BD.DatabaseHelper;

import com.j256.ormlite.jdbc.JdbcConnectionSource;


public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ServeurDistExec serveur = new ServeurDistExec();
		
		try {
			JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:sqlite:bd.sqlite");
			DatabaseHelper dbh = new DatabaseHelper(cs);
			dbh.create();
			//dbh.getCommandeDao().createIfNotExists(new Commande("test", "desc", "script"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		serveur.start();
		
	}

}
