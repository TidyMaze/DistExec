package com.example.distexec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.distexec.BD.Commande;
import com.example.distexec.BD.DatabaseHelper;
import com.example.distexec.BD.Serveur;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VueCommande extends Activity {

	private static final int PORTMIN = 9301;
	private static final int PORTMAX = 9305;
	private Serveur serveur;
	private Commande commande;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commande);
		
		DatabaseHelper dbh = new DatabaseHelper(VueCommande.this);
		
		// récupé
		int idserv = getIntent().getIntExtra("id_serveur", -1);
		Dao<Serveur, Integer> serveurDAO = dbh.getServeurDao();
		try {
			this.serveur = serveurDAO.queryForId(idserv);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// récupération des données concernant la commande
		int id_commande = getIntent().getIntExtra("id_commande", -1);
		Dao<Commande, Integer> commandeDAO = dbh.getCommandeDao();
		
		
		// --------------------
		try {
			for( Commande c : commandeDAO.queryForAll() ) {
				System.out.println( c.getId() + " ; " + c.getNom() );
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// --------------------
		
		
		try {
			List<Commande> listeCommande = commandeDAO.query( commandeDAO.queryBuilder().where().eq( "idServeur" , id_commande ).prepare() );
			this.commande = listeCommande.get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("id commande recu : " + id_commande );
		System.out.println("commande nom : " + this.commande.getNom() );
		
		// affichage des informations
		TextView nom_commande = (TextView)findViewById(R.id.nom_commande);
		nom_commande.setText( this.commande.getNom() );
		
		
		TextView description = (TextView)findViewById(R.id.description);
		description.setText( this.commande.getDescription() );
		
		
		Button executer = (Button)findViewById(R.id.executer);
		executer.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				execuerCommande();
			}
		});
		
	}

	public void execuerCommande() {
		
		Thread connexion_serveur = new Thread( new Runnable() {
			@Override
			public void run() {
				
				// connexion au serveur
				try {

					Socket socket = NetworkUtil.findSocket( serveur.getIp(), PORTMIN, PORTMAX );
					System.out.println( "port distant : " + socket.getPort() );
					System.out.println( "port local : " + socket.getLocalPort() );

					// canaux d'écriture
					OutputStream os = socket.getOutputStream();
					OutputStreamWriter osw = new OutputStreamWriter(os);
					PrintWriter pw = new PrintWriter(osw);

					// canaux de lecture
					InputStream is = socket.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);

					// demande la liste des commandes du serveur
					/* Au format JSON :
					 * {
					 * 		etat: "executer"
					 * 		id: "id_commande"
					 * }
					 */
					JSONObject json_listerCommande = new JSONObject();
					json_listerCommande.put( "etat" , "executer" );
					json_listerCommande.put( "id" , commande.getIdServeur() );
					pw.println( json_listerCommande.toString() );
					pw.flush();	

				}
				catch( JSONException e ) {
					e.printStackTrace();
				} catch (ConnectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		connexion_serveur.start();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.commande, menu);
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
