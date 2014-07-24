package com.lang.social.teachstudy.lesson;

import org.json.JSONObject;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lang.social.R;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.utils.ImageUtils;
import com.lang.social.utils.JSONUtils;

public class LessonFragment extends Fragment implements LessonFragmentListener {
	private ImageView ivCurrentPhoto;
	private TextView tvCurrentWord;
	
	private Animation animation;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_lesson_layout, container, false);
		ivCurrentPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
		tvCurrentWord = (TextView) view.findViewById(R.id.tvPhotoTranslation);

		
		
		
		
		IOCallBackHandler.getInstance().setLessonFragmentListener(this);
		
		return view;
	}
	
	protected void onDestory() {
		super.onDestroy();
		IOCallBackHandler.getInstance().setLessonFragmentListener(null);
	}

	@Override
	public void onImageResponse(JSONObject jsonResponse) {
		final String data = JSONUtils.getStringFromJSON(jsonResponse, "image", null);
		final String word = JSONUtils.getStringFromJSON(jsonResponse, "word", null);
		animation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
		animation.setDuration(2000);
		Log.d("LessonFragment", "RecivedImage");
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = ImageUtils.decodeToImage(data);
				ivCurrentPhoto.startAnimation(animation);
				tvCurrentWord.startAnimation(animation);
				ivCurrentPhoto.setImageBitmap(bitmap);
				tvCurrentWord.setText(word);

			}
		});
		
	}
}
