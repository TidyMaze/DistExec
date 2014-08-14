package com.example.distexec;

import android.util.Pair;

public class StringItem extends Pair<String, Integer> {

	public StringItem(String first, Integer second) {
		super(first, second);
	}
	
	public String toString(){
		return first;
	}

}
