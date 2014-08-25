package com.example.distexec;



import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

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
}
