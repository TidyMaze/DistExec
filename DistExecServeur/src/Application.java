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
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		Model model = new Model();
		Controleur controleur = new Controleur( model );
		Vue1 vue = new Vue1( model , controleur );
		controleur.addVue(vue);
		
		// Exécution du serveur
		//model.getServeur().start();
		
		//System.out.println( model.getServeur().getIpLocal() + " " + model.getServeur().getPort() );
		
	}

}
