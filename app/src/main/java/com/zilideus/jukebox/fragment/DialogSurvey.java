package com.zilideus.jukebox.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.zilideus.jukebox.R;
import com.zilideus.jukebox.flags.JKeys;
import com.zilideus.jukebox.model.StationAddedManually;

/**
 * Created by sandeeprana on 09/01/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public class DialogSurvey extends DialogFragment implements View.OnClickListener {
    public static final String TITLE = "DIALOG_FRAGMENT";
    private static final String TAG = "DialogSurveyFragment";
    static DialogSurvey frag;
    EditText mEditName, mEditDescription, mEditImage, mEditUriLink;
    private Button buttonSave;
    private Bundle bundle;
    private String stationId;
    private OnNewStationSaved onSaveListener;

    public DialogSurvey() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogSurvey getSingletonInstance(String title) {
        frag = new DialogSurvey();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bundle = getArguments();
        return inflater.inflate(R.layout.fragment_manual_favourite, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view


        mEditName = (EditText) view.findViewById(R.id.edit_name_manualfavourite);
        mEditDescription = (EditText) view.findViewById(R.id.edit_description_manualfavourite);
        mEditImage = (EditText) view.findViewById(R.id.edit_image_manualfavourite);
        mEditUriLink = (EditText) view.findViewById(R.id.edit_urilink_manualfavourite);

        if (bundle != null) {
            mEditName.setText(bundle.getString(JKeys.NAME, "No name"));
            mEditDescription.setText(bundle.getString(JKeys.DESCRIPTION, ""));
            mEditImage.setText(bundle.getString(JKeys.LOGO, ""));
            mEditUriLink.setText(bundle.getString(JKeys.URI, ""));
            stationId = bundle.getString(JKeys.ID, null);
        }


        buttonSave = (Button) view.findViewById(R.id.save_fragment_manualfavourite);
        buttonSave.setOnClickListener(this);

        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_fragment_manualfavourite:
                getAndSaveStation(v);
                break;
            default:
                break;
        }
    }

    private void getAndSaveStation(View v) {
        String name = mEditName.getText().toString();
        String descrption = mEditDescription.getText().toString();
        String image = mEditImage.getText().toString();
        String uriLink = mEditUriLink.getText().toString();
        boolean errorFlag = false;

        if (name.isEmpty()) {
            mEditName.setError("required!");
            errorFlag = true;
        }

        if (uriLink.isEmpty()) {
            mEditUriLink.setError("required!");
            errorFlag = true;
        }

        if (errorFlag) {
            //Some error occured
            return;
        }
        Log.e(TAG, name + descrption + image + uriLink);

        StationAddedManually stationAddedManually = new StationAddedManually();
        stationAddedManually.setName(name);
        stationAddedManually.setCtqueryString(descrption);
        stationAddedManually.setLogo(image);
        stationAddedManually.setUrlstation(uriLink);
        stationAddedManually.setStationId(stationId);
        stationAddedManually.save(name);
        this.dismissAllowingStateLoss();
        if (this.onSaveListener != null) {
            this.onSaveListener.onStationSaved(stationAddedManually);
        }

    }

    public void setOnSaveListener(OnNewStationSaved onSaveListener) {
        this.onSaveListener = onSaveListener;
    }
}
