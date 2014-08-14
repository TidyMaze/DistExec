package com.example.distexec.BD;



import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "base.sqlite";

    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

	private Dao<Serveur, Integer> serveurDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Serveur.class);
		} catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, Serveur.class, false);
			TableUtils.createTable(connectionSource, Serveur.class);
		} catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
	}
	
    public Dao<Serveur, Integer> getServeurDao() {
        if (null == this.serveurDao) {
            try {
                this.serveurDao = getDao(Serveur.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return this.serveurDao;
    }
}
