package com.lang.social.memorygame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lang.social.R;

public class CardFrontTextFragment extends CardFrontFragment implements OnClickListener {
	
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
  
}
