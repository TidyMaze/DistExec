package com.example.distexec;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.distexec.BD.DatabaseHelper;
import com.example.distexec.BD.Serveur;
import com.j256.ormlite.dao.Dao;


public class ListeServeurs extends Activity {

    private ListView listeServeurs;
    
    private void buildList(){
    	DatabaseHelper dbh = new DatabaseHelper(ListeServeurs.this);
		Dao<Serveur, Integer> serveurDAO = dbh.getServeurDao();
		
		try {
			List<Serveur> serveurs = serveurDAO.queryForAll();
			
			List<StringItem> listeLignes = new ArrayList<StringItem>();
			
			for(Serveur s : serveurs){
				listeLignes.add(new StringItem(s.getNom() + " " + s.getIp(), s.getId()));
			}
			
			listeServeurs.setAdapter(new ArrayAdapter<StringItem>(getBaseContext(), android.R.layout.simple_list_item_1, listeLignes));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
    }

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_serveurs);
        
        Button boutonAjouter = (Button)findViewById(R.id.ajoutServ);
        boutonAjouter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListeServeurs.this, AjoutServeur.class);
				startActivity(intent);
			}
		});
        
        listeServeurs = (ListView)findViewById(R.id.listeServeurs);
        listeServeurs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				if( !NetworkUtil.isConnected(getApplicationContext()) ) {
					System.out.println("ListeServeur.class : android non connecté ! (clic sur un serveur)");
					Toast.makeText(getApplicationContext(), "Vous devez connecter votre appareil à internet", Toast.LENGTH_SHORT).show();
					return;
				}
				
				int idServ = ((StringItem)listeServeurs.getItemAtPosition(position)).second;
				Intent i = new Intent(ListeServeurs.this, ListeCommandes.class);
				i.putExtra("idserv", idServ);
				startActivity(i);
			}
		});
        
        listeServeurs.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				int idServ = ((StringItem)listeServeurs.getItemAtPosition(position)).second;
				
		        DatabaseHelper dbh = new DatabaseHelper(ListeServeurs.this);
				Dao<Serveur, Integer> serveurDAO = dbh.getServeurDao();
				
				try {
					serveurDAO.deleteById(idServ);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				buildList();
				
				return false;
			}
		});
    }
	
	

    @Override
	protected void onResume() {
		super.onResume();
		
		this.buildList();
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
