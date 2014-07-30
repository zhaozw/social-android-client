package com.lang.social.memorygame;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lang.social.R;

public class CardFrontImageFragment extends CardFrontFragment implements OnClickListener {
	

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
}
