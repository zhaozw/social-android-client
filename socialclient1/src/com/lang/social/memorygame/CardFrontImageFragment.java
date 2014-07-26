package com.lang.social.memorygame;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lang.social.R;

public class CardFrontImageFragment extends Fragment implements OnClickListener {
	
	private int i;
	private int j;
	
	private MemoryGameListener  memoryGameListener;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memory_game_card_front_image_fragment_item_layout, container, false);
        
        Bitmap bitmap = (Bitmap) getArguments().getParcelable(MemoryGameActivity.MemoryGameImage);
        ImageView ivCard = (ImageView) view.findViewById(R.id.cardimage);
        if(bitmap != null && ivCard != null){
        	ivCard.setImageBitmap(bitmap);
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
