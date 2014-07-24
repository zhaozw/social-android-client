package com.lang.social.teachstudy.lesson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.twoway.TwoWayListAdapter;
import com.lang.social.twoway.TwoWayPhotoItem;
import com.lang.social.utils.ImageUtils;
import com.lang.social.utils.JSONUtils;

public class StudentTeacherPhotosTwoWayFragment extends Fragment implements PhotosTwoWayFragmentListener {
	
    private static final String LOGTAG = "TwoWayViewSample";
    public static final int IMAGES_NUMBER = 20;

    private TwoWayView mListView;
    
    private ArrayList<TwoWayPhotoItem> photoItems;
    private TwoWayListAdapter adapter;
    
    private ProgressDialog progressDialog;
    int numOfImagesRecived;
    
    private Toast mToast;
    private String mClickMessage;
    private String mScrollMessage;
    private String mStateMessage;

    
	@SuppressLint("ShowToast")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_twoway_photos, container, false);

		  	mClickMessage = "";
	        mScrollMessage = "";
	        mStateMessage = "";

	        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
	        mToast.setGravity(Gravity.CENTER, 0, 0);

	        mListView = (TwoWayView) view.findViewById(R.id.lvItems);
	        
	        mListView.setItemMargin(10);
	        mListView.setLongClickable(true);

	        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View child, int position,
	                    long id) {
//	                mClickMessage = "Item clicked: " + position;
//	                refreshToast();
	            	JSONObject jsonToSend = new JSONObject();
	            	TwoWayPhotoItem photoItem = photoItems.get(position);
	            	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            	photoItem.getImageRes().compress(Bitmap.CompressFormat.PNG, 100, stream);
	            	byte[] byteArray = stream.toByteArray();
	            	String encoded = Base64.encodeToString(byteArray , 0); 
	            	try {
						stream.close();
						jsonToSend.put("image", encoded);
						jsonToSend.put("word", photoItem.getTitle());
						ServerController.sendJSONMessage("showImage", jsonToSend);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        });

	        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	            @Override
	            public boolean onItemLongClick(AdapterView<?> parent, View child,
	                    int position, long id) {
//	                mClickMessage = "Item long pressed: " + position;
//	                refreshToast();
	                return true;
	            }
	        });

	        mListView.setOnScrollListener(new TwoWayView.OnScrollListener() {
	            @Override
	            public void onScrollStateChanged(TwoWayView view, int scrollState) {
//	                String stateName = "Undefined";
//	                switch(scrollState) {
//	                case SCROLL_STATE_IDLE:
//	                    stateName = "Idle";
//	                    break;
//
//	                case SCROLL_STATE_TOUCH_SCROLL:
//	                    stateName = "Dragging";
//	                    break;
//
//	                case SCROLL_STATE_FLING:
//	                    stateName = "Flinging";
//	                    break;
//	                }

//	                mStateMessage = "Scroll state changed: " + stateName;
//	                refreshToast();
	            }

	            @Override
	            public void onScroll(TwoWayView view, int firstVisibleItem,
	                    int visibleItemCount, int totalItemCount) {
//	                mScrollMessage = "Scroll (first: " + firstVisibleItem + ", count = " + visibleItemCount + ")";
//	                refreshToast();
	            }
	        });

	        mListView.setRecyclerListener(new TwoWayView.RecyclerListener() {
	            @Override
	            public void onMovedToScrapHeap(View view) {
//	                Log.d(LOGTAG, "View moved to scrap heap");
	            }
	        });

	        
	        photoItems = new ArrayList<TwoWayPhotoItem>();
//	        photoItems.add(new TwoWayPhotoItem("Tree", R.drawable.tree));
//	        photoItems.add(new TwoWayPhotoItem("Baby", R.drawable.baby));
//	        photoItems.add(new TwoWayPhotoItem("House", R.drawable.house));
//	        photoItems.add(new TwoWayPhotoItem("Dog", R.drawable.dog));
//	        photoItems.add(new TwoWayPhotoItem("Tree", R.drawable.tree));
//	        photoItems.add(new TwoWayPhotoItem("Baby", R.drawable.baby));
//	        photoItems.add(new TwoWayPhotoItem("House", R.drawable.house));
//	        photoItems.add(new TwoWayPhotoItem("Cat", R.drawable.cat));
	        adapter = new TwoWayListAdapter(getActivity(), R.layout.twoway_listitem, photoItems);
	        mListView.setAdapter(adapter);
	        IOCallBackHandler.getInstance().setPhotosTwoWayFragmentListener(this);
	        createProgressDialog();
	        askServerForImages();
	        
			return view;
	}

	protected void onDestory() {
		super.onDestroy();
		IOCallBackHandler.getInstance().setPhotosTwoWayFragmentListener(null);
	}


	private void askServerForImages() {
		progressDialog.show();
		JSONObject jsonToSend = new JSONObject();
		try {
			jsonToSend.put("fileName", "dog1.png");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServerController.sendJSONMessage("getImage", jsonToSend);
		for(int i = 0; i < IMAGES_NUMBER; i++) {
			ServerController.sendJSONMessage("getImage", jsonToSend);
		}
		
	}
	
	private void createProgressDialog() {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading Images...");
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
	}
	
	@Override
	public void onImageRecived(JSONObject jsonResponse) {
		numOfImagesRecived++;
		final String data = JSONUtils.getStringFromJSON(jsonResponse, "data", null);
		Log.d("StudentTeacherPhotosTwoWayFragment", "RecivedImage");
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				photoItems.add(new TwoWayPhotoItem("image", ImageUtils.decodeToImage(data)));
				adapter.notifyDataSetChanged();
				if(numOfImagesRecived == IMAGES_NUMBER) {
					numOfImagesRecived = 0;
					progressDialog.dismiss();
				}
			}
		});
	}



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
