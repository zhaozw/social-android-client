package com.lang.social.memorygame;

import com.lang.social.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CardBackFragment extends Fragment implements OnClickListener {
	
	private int i;
	private int j;
	
	private MemoryGameListener  memoryGameListener;
	
	private ImageView ivCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memory_game_card_back_fragment_item_layout, container, false);
        
        ivCard = (ImageView) view.findViewById(R.id.cardimage);
        
        ivCard.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.socialcard));
        
        ivCard.setOnClickListener(this);      
        
        return view;
    }
    
    public void SetRow(int row){
    	this.i = row;
    }
    
    public void SetCol(int col){
    	this.j = col;
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
