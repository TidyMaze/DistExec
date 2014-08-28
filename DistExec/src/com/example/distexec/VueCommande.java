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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VueCommande extends Activity {

	private static final int PORTMIN = 9301;
	private static final int PORTMAX = 9305;
	private Serveur serveur;
	private Commande commande;

	private Handler handler;
	private static Status[] valeurStatus = Status.values();



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commande);

		DatabaseHelper dbh = new DatabaseHelper(VueCommande.this);


		// récupération des données concernant le serveur (id...)
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
				execuerCommande();
			}
		});


		// initialisation du handler
		this.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (valeurStatus[msg.what]) {

				case NO_INTERNET:
					makeToast("Vous devez connecter votre appareil à internet");
					break;

				case ConnexionException:
					makeToast("Ce serveur n'est pas à l'écoute sur aucun des ports");
					break;

				default:
					makeToast("Une grande erreur est survenue");
					break;
				}
			}
		};

	}

	public void execuerCommande() {
		ThreadCommande connexion_serveur = new ThreadCommande( this);
		connexion_serveur.start();
	}





	public Serveur getServeur() {
		return serveur;
	}

	public Commande getCommande() {
		return commande;
	}
	
	public Handler getHandler() {
		return this.handler;
	}
	
	public void makeToast(String message) {
		Toast.makeText(VueCommande.this, message, Toast.LENGTH_SHORT).show();
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
