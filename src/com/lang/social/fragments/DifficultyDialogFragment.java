package com.lang.social.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.lang.social.R;
import com.lang.social.interfaces.DifficultyListener;

public class DifficultyDialogFragment extends DialogFragment {
	
	private DifficultyListener mDifficultyListener;
	private String[] difficultiesArr = new String[] {"Easy", "Medium", "Hard"};
	
    public static DifficultyDialogFragment newInstance(int title) {
    	DifficultyDialogFragment frag = new DifficultyDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        ListAdapter Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, difficultiesArr);
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setAdapter(Adapter, 
                		new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(mDifficultyListener != null) {
									mDifficultyListener.onDifficultyChoosen(difficultiesArr[which]);
								}
							}
						})
                .create();
    }
     
	
	public void setDifficultyListener(DifficultyListener i_DifficultyListener){
		mDifficultyListener = i_DifficultyListener;
	}
}
