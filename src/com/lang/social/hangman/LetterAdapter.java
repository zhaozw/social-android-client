package com.lang.social.hangman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.lang.social.R;

public class LetterAdapter extends BaseAdapter {
	
	private String[] letters;
	private LayoutInflater letterInf;
	
	public LetterAdapter(Context c) {
		letters = new String[26];
		for (int a = 0; a < letters.length; a++) {
		  letters[a] = "" + (char)(a+'A');
		}
		
		letterInf = LayoutInflater.from(c);
	}
	
	@Override
	public int getCount() {
		return letters.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		  //create a button for the letter at this position in the alphabet
		  Button letterBtn;
		  if (convertView == null) {
		    //inflate the button layout
		    letterBtn = (Button)letterInf.inflate(R.layout.letter, parent, false);
		  } else {
		    letterBtn = (Button) convertView;
		  }
		  //set the text to this letter
		  letterBtn.setText(letters[position]);
		  return letterBtn;
	}

}
