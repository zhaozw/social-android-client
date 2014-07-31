package com.lang.social.memorygame;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public class CardFrontFragment extends Fragment implements OnClickListener {
	
	protected int i;
	protected int j;
	protected MemoryGameListener  memoryGameListener;
	protected int cardPairId;
	
    public void SetCardPairId(int CardPairId){
    	this.cardPairId = CardPairId;
    }
    
	public int GetCardPairId() {
		return cardPairId;
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
