package com.example.distexec;



import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	
	public static Socket findSocket(String dstName, int dstPortMin, int dstPortMax) throws ConnexionException {

		for(int i=dstPortMin;i<=dstPortMax;i++){
			try {
				Socket s = new Socket(dstName, i);
				return s;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
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


