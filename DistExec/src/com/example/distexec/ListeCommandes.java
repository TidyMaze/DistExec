package com.example.distexec;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.distexec.BD.DatabaseHelper;
import com.example.distexec.BD.Serveur;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ListeCommandes extends Activity {

	private static final String IP = null;
	private static final int PORTMIN = 9301;
	private static final int PORTMAX = 9305;
	private Serveur serveur;
	
	private void buildList(){
    	
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liste_commandes);
		
		int idserv = getIntent().getIntExtra("idserv", -1);
		
		DatabaseHelper dbh = new DatabaseHelper(ListeCommandes.this);
		Dao<Serveur, Integer> serveurDAO = dbh.getServeurDao();
		
		try {
			this.serveur = serveurDAO.queryForId(idserv);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
		        	OutputStreamWriter osw = null;
		    		
		    		try {
		    			Socket socket = NetworkUtil.findSocket(serveur.getIp(), PORTMIN, PORTMAX);
		    			System.out.println("port distant : " + socket.getPort());
		    			System.out.println("port local : " + socket.getLocalPort());
		    			OutputStream os = socket.getOutputStream();
		    			osw = new OutputStreamWriter(os);
		    			PrintWriter pw = new PrintWriter(osw);
		    			System.out.println("envoi HELO");
		    			pw.println("HELO");
		    			pw.println("test");
		    			pw.flush();
		    			System.out.println("HELO marqué et envoyé");
		    		
		    			
		    		} catch (ConnectException e) {
		    			Toast.makeText(ListeCommandes.this, "Hey, je ne trouve pas de serveur !", Toast.LENGTH_LONG).show();
		    			e.printStackTrace();
		    		} catch (IOException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		} finally {
		    			if(osw != null){
		    				try {
		    					osw.close();
		    				} catch (IOException e) {
		    					// TODO Auto-generated catch block
		    					e.printStackTrace();
		    				}
		    			}
		    		}
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		});

		thread.start(); 
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.liste_commandes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
