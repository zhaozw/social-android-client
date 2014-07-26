package com.lang.social.memorygame;

import com.lang.social.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CardFrontTextFragment extends Fragment implements OnClickListener {
	private int i;
	private int j;
	
	private MemoryGameListener  memoryGameListener;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memory_game_card_front_text_fragment_item_layout, container, false);
        
        String word = getArguments().getString(MemoryGameActivity.MemoryGameWord);
        TextView tv = (TextView) view.findViewById(R.id.word);
        if(word != null && tv != null){
            tv.setText(word);
        }

        return view;
    }
    
    public void SetRow(int row){
    	this.i = row;
    }
    
    public void SetCol(int col){
    	this.j = col;
    }
    
 
	public int getRow() {
		return i;
	}

	public int getCol() {
		return j;
	}

	@Override
	public void onClick(View v) {
		if(memoryGameListener != null){
			memoryGameListener.OnCardClick(i, j);
		}
	}
 
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		memoryGameListener = (MemoryGameActivity) activity;
	}
}
