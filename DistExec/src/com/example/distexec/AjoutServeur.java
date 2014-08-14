package com.example.distexec;

import java.sql.SQLException;

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
import android.widget.EditText;
import android.widget.Toast;

public class AjoutServeur extends Activity {

	private String nom;
	private String ip;
	private EditText champNom;
	private EditText champIp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ajout_serveur);

		Button boutonAnnuler = (Button) findViewById(R.id.annuler);
		boutonAnnuler.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED);
				finish();
			}
		});

		Button boutonValider = (Button) findViewById(R.id.valider);

		champNom = (EditText) findViewById(R.id.ajoutServNom);
		champIp = (EditText) findViewById(R.id.ajoutServIp);

		boutonValider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nom = champNom.getText().toString();
				ip = champIp.getText().toString();

				if (verif()) {
					DatabaseHelper dbh = new DatabaseHelper(AjoutServeur.this);
					Dao<Serveur, Integer> serveurDAO = dbh.getServeurDao();
					try {
						serveurDAO.create(new Serveur(nom, ip));

						finish();

					} catch (SQLException e) {
						e.printStackTrace();
						Toast.makeText(AjoutServeur.this, e.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(AjoutServeur.this, "champs invalides",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	protected boolean verif() {
		return nom.length() > 0 && ip.length() > 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ajout_serveur, menu);
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
