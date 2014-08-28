package com.example.distexec;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author Maxime
 *
 */
public class NetworkUtil {
	
	public static Socket findSocket( String dstName, int dstPortMin, int dstPortMax) throws ConnexionException {
		return findSocket(dstName, dstPortMin, dstPortMax, 1 );
	}
	
	/**
	 * @param dstName		
	 * @param dstPortMin
	 * @param dstPortMax	
	 * @param timeout 		en seconde
	 * @return
	 * @throws ConnexionException
	 */
	public static Socket findSocket( String dstName, int dstPortMin, int dstPortMax , int timeout ) throws ConnexionException {

		for(int i=dstPortMin;i<=dstPortMax;i++){
			Log.e("MONMESSAGE!!!!!", "recherche sur port " + i);
			try {
				//Socket s = new Socket(dstName, i);
				Socket socket = new Socket();
				socket.connect( new InetSocketAddress( dstName , i ) , timeout * 1000 ); // timeout = 1 seconde		
				return socket;
			} catch( SocketTimeoutException e ) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.e("MONMESSAGE!!!!!", "pas sur port " + i);
		}
		throw new ConnexionException("Pas de port dispo ou mauvaise connexion, rtfm noob");
	}
	
	
	
	public static boolean isConnected( Context context ) {
		//NetworkUtil.isConnected( getApplicationContext() )
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
		/* a utiliser avec
		 *   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
		 *  dans le manifest
		 *   http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-timeouts
		 */
	}
}


