package com.lang.social.picturequizgame;

import java.util.ArrayList;

import org.lucasr.twowayview.TwoWayView;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;





import com.lang.social.R;
import com.lang.social.twoway.TwoWayListAdapter;
import com.lang.social.twoway.TwoWayPhotoItem;

public class PhotosQuizTwoWayFragment extends Fragment {
	
    private static final String LOGTAG = "TwoWayViewSample";

    private TwoWayView mListView;
    
    private Toast mToast;
    private String mClickMessage;
    private String mScrollMessage;
    private String mStateMessage;

    
	@SuppressLint("ShowToast")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_picture_quiz_game, container, false);

		  	mClickMessage = "";
	        mScrollMessage = "";
	        mStateMessage = "";

	        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
	        mToast.setGravity(Gravity.CENTER, 0, 0);

	        mListView = (TwoWayView) view.findViewById(R.id.twoWayPhotosList);
	        
	        mListView.setItemMargin(10);
	        mListView.setLongClickable(true);

	        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View child, int position,
	                    long id) {
	                mClickMessage = "Item clicked: " + position;
	                refreshToast();
	            }
	        });

	        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	            @Override
	            public boolean onItemLongClick(AdapterView<?> parent, View child,
	                    int position, long id) {
	                mClickMessage = "Item long pressed: " + position;
	                refreshToast();
	                return true;
	            }
	        });

	        mListView.setOnScrollListener(new TwoWayView.OnScrollListener() {
	            @Override
	            public void onScrollStateChanged(TwoWayView view, int scrollState) {
	                String stateName = "Undefined";
	                switch(scrollState) {
	                case SCROLL_STATE_IDLE:
	                    stateName = "Idle";
	                    break;

	                case SCROLL_STATE_TOUCH_SCROLL:
	                    stateName = "Dragging";
	                    break;

	                case SCROLL_STATE_FLING:
	                    stateName = "Flinging";
	                    break;
	                }

	                mStateMessage = "Scroll state changed: " + stateName;
	                refreshToast();
	            }

	            @Override
	            public void onScroll(TwoWayView view, int firstVisibleItem,
	                    int visibleItemCount, int totalItemCount) {
	                mScrollMessage = "Scroll (first: " + firstVisibleItem + ", count = " + visibleItemCount + ")";
	                refreshToast();
	            }
	        });

	        mListView.setRecyclerListener(new TwoWayView.RecyclerListener() {
	            @Override
	            public void onMovedToScrapHeap(View view) {
	                Log.d(LOGTAG, "View moved to scrap heap");
	            }
	        });

	        
	        ArrayList<TwoWayPhotoItem> photoItems = new ArrayList<TwoWayPhotoItem>();
//	        photoItems.add(new TwoWayPhotoItem("Tree", R.drawable.tree));
//	        photoItems.add(new TwoWayPhotoItem("Baby", R.drawable.baby));
//	        photoItems.add(new TwoWayPhotoItem("House", R.drawable.house));
//	        photoItems.add(new TwoWayPhotoItem("Dog", R.drawable.dog));
//	        photoItems.add(new TwoWayPhotoItem("Tree", R.drawable.tree));
//	        photoItems.add(new TwoWayPhotoItem("Baby", R.drawable.baby));
//	        photoItems.add(new TwoWayPhotoItem("House", R.drawable.house));
//	        photoItems.add(new TwoWayPhotoItem("Tree", R.drawable.tree));
//	        photoItems.add(new TwoWayPhotoItem("Baby", R.drawable.baby));
//	        photoItems.add(new TwoWayPhotoItem("House", R.drawable.house));
//	        photoItems.add(new TwoWayPhotoItem("Dog", R.drawable.dog));
//	        photoItems.add(new TwoWayPhotoItem("Tree", R.drawable.tree));
//	        photoItems.add(new TwoWayPhotoItem("Baby", R.drawable.baby));
//	        photoItems.add(new TwoWayPhotoItem("House", R.drawable.house));
	        
	        TwoWayListAdapter adapter = new TwoWayListAdapter(getActivity(), R.layout.quiz_photo_twoway_listitem, photoItems);
	        mListView.setAdapter(adapter);

//	        final Handler handler = new Handler();
//	        handler.postDelayed(new Runnable() {
//	          @Override
//	          public void run() {
//	        	  makeScrollRightAnimation();
//	          }
//	        }, 1000);
	        
	      
			return view;
	}
	
	
	

//	private void makeScrollRightAnimation() {
		
//  		ValueAnimator anim = ValueAnimator.ofInt(300, 0);
//  	    anim.addUpdateListener(new AnimatorUpdateListener() {
//  	      public void onAnimationUpdate(ValueAnimator animation) {
//  	           int value = (Integer) animation.getAnimatedValue();
//  	           mListView.setSelectionFromOffset(mListView.getCount()-1, value);
//  	      }
//  	    });
//  	    
//  	    anim.setDuration(5000);
//  	    anim.start();
		
//		new CountDownTimer(7000, 500) {  
//			 int position = 0;
//			 public void onTick(long millisUntilFinished) {             
//				 mListView.setSelectionFromOffset(position++ / 14, 0);  
//			 }          
//			 public void onFinish() {  
//			 }      
//		}.start(); 
//		
//	}
	
	
	private void refreshToast() {
	        StringBuffer buffer = new StringBuffer();
	
	        if (!TextUtils.isEmpty(mClickMessage)) {
	            buffer.append(mClickMessage);
	        }
	
	        if (!TextUtils.isEmpty(mScrollMessage)) {
	            if (buffer.length() != 0) {
	                buffer.append("\n");
	            }
	
	            buffer.append(mScrollMessage);
	        }
	
	        if (!TextUtils.isEmpty(mStateMessage)) {
	            if (buffer.length() != 0) {
	                buffer.append("\n");
	            }
	
	            buffer.append(mStateMessage);
	        }
	
	        mToast.setText(buffer.toString());
	        mToast.show();
	    }
	
}
