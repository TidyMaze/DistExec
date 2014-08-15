import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

import BD.Commande;
import BD.DatabaseHelper;


public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		// création de la BD
		try {
			JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:sqlite:bd.sqlite");
			DatabaseHelper dbh = new DatabaseHelper(cs);
			dbh.create();
			
			//dbh.getCommandeDao().createIfNotExists( new Commande("commande 1", "description commande 1", "C:\\Users\\Maxime\\Documents\\Programmation\\batch\\test.bat") );
			List<Commande> listeCommandes = dbh.getCommandeDao().queryForAll();
			System.out.println(" --  Serveur : Commandes Existantes : ");
			for( Commande c : listeCommandes ) {
				System.out.println( " 	- "  + c.getId() +  " ; " + c.getNom() + " ; " + c.getDescription() + " ; " + c.getScript() );
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		// Exécution du serveur
		ServeurDistExec serveur = new ServeurDistExec();
		serveur.start();
		
	}

}
