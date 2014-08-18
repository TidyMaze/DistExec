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
			
			// a supprimer par la suite
			dbh.getCommandeDao().create( new Commande("nom test" , "descrip test" , "script test" ) );
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		Model model = new Model();
		Controleur controleur = new Controleur( model );
		Vue1 vue = new Vue1( model , controleur );
		controleur.addVue(vue);
		
		try {
			for( Commande c : model.getListeCommande() ) {
				System.out.println( c.getNom() + " " + c.getScript() + " " + c.getDescription() );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

}
