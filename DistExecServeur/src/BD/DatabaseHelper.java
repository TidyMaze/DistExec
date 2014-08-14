package BD;



import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper {

	private Dao<Commande, Integer> commandeDao;

	private JdbcConnectionSource cs;

	public DatabaseHelper(JdbcConnectionSource cs) {
		this.cs = cs;
	}

	public void create() {
		try {
			TableUtils.createTableIfNotExists(cs, Commande.class);
		} catch (SQLException e) {
            System.out.println("Can't create database");
            throw new RuntimeException(e);
        }
	}
	
    public Dao<Commande, Integer> getCommandeDao() {
        if (null == this.commandeDao) {
            try {
                this.commandeDao = DaoManager.createDao(cs, Commande.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return this.commandeDao;
    }
}
