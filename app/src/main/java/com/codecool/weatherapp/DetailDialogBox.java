package com.codecool.weatherapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DetailDialogBox extends DialogFragment {

    private static final String ARG_DATE = "date";

    public static DetailDialogBox newInstance(String data) {
        Bundle args = new Bundle();
        args.putString(ARG_DATE, data);

        DetailDialogBox fragment = new DetailDialogBox();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.daily_details_fragment,null);

        String detail = (String) getArguments().getString(ARG_DATE);
        TextView mTextView = (TextView) v.findViewById(R.id.details_data);
        mTextView.setText(detail);

        return new AlertDialog.Builder(getActivity()).setView(v).create();
    }


}
