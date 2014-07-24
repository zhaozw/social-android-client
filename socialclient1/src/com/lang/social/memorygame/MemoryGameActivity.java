package com.lang.social.memorygame;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;

import com.lang.social.R;

public class MemoryGameActivity extends Activity implements MemoryGameListener {

	Fragment[][] cardFrags = new Fragment[4][4];
	int[][] cardsFragsIds = new int[4][4];
	boolean[][] isFrontCards = new boolean[4][4];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memory_game_activity);

        if (savedInstanceState == null) {
        	initAllCardContainersId();
        	initAllCardFragment();
        }
        
       // flipCard(1, 1);
	}
	

	private void initAllCardContainersId() {
		cardsFragsIds[0][0] = R.id.card00;
		cardsFragsIds[0][1] = R.id.card01;
		cardsFragsIds[0][2] = R.id.card02;
		cardsFragsIds[0][3] = R.id.card03;
		cardsFragsIds[1][0] = R.id.card10;
		cardsFragsIds[1][1] = R.id.card11;
		cardsFragsIds[1][2] = R.id.card12;
		cardsFragsIds[1][3] = R.id.card13;
		cardsFragsIds[2][0] = R.id.card20;
		cardsFragsIds[2][1] = R.id.card21;
		cardsFragsIds[2][2] = R.id.card22;
		cardsFragsIds[2][3] = R.id.card23;
		cardsFragsIds[3][0] = R.id.card30;
		cardsFragsIds[3][1] = R.id.card31;
		cardsFragsIds[3][2] = R.id.card32;
		cardsFragsIds[3][3] = R.id.card33;

	}
	
	private void flipCard(int i, int j) {
	    getFragmentManager()
	            .beginTransaction()
	            .setCustomAnimations(
	                    R.anim.card_flip_right_in, R.anim.card_flip_right_out,
	                    R.anim.card_flip_left_in, R.anim.card_flip_left_out)
	            .replace(cardsFragsIds[i][j], new CardBackFragment())
	            .commit();
	}

	private void initAllCardFragment() {
		for (int i = 0; i < cardFrags.length; i++) {
			cardFrags[i] = new CardFrontImageFragment[4];
		}
		
		for (int i = 0; i < cardFrags.length; i++) {
			for (int j = 0; j < cardFrags[i].length; j++) {
				Bundle b = new Bundle();
				b.putInt("x", i);
				b.putInt("y", j);
				cardFrags[i][j] = new CardFrontImageFragment();
				cardFrags[i][j].setArguments(b);
				isFrontCards[i][j] = true;
			}
		}

		for (int i = 0; i < cardFrags.length; i++) {
			for (int j = 0; j < cardFrags[i].length; j++) {
		      getFragmentManager()
                .beginTransaction()
                .add(cardsFragsIds[i][j], cardFrags[i][j])
                .commit();
			}
		}		
	}

	@Override
	public void OnCardClick(int row, int col) {
		Toast.makeText(this, row + "  " + col , Toast.LENGTH_SHORT).show();	
		flipCard(row, col);
	}
}
