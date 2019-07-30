package com.codecool.weatherapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codecool.weatherapp.utilities.ConnectUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class DetailDialogBox extends DialogFragment {

    private static final String ARG_DATE = "date";

    private Uri mUri;

    public static DetailDialogBox newInstance(ArrayList data) {
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_DATE, data);

        DetailDialogBox fragment = new DetailDialogBox();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.daily_details_fragment,null);

//        String detail = (String) getArguments().getString(ARG_DATE);
//        TextView mTextView = (TextView) v.findViewById(R.id.details_data);
//        mTextView.setText(detail);
//
//        mUri = (Uri) getArguments().get(ARG_DATE);


        ArrayList<String> test = getArguments().getStringArrayList(ARG_DATE);

        TextView mTextView1 = (TextView) v.findViewById(R.id.details_data);
        mTextView1.setText(test.get(0));

        TextView mTextView2 = (TextView) v.findViewById(R.id.details_description);
        mTextView2.setText(test.get(6));

        TextView mTextView3 = (TextView) v.findViewById(R.id.details_humidity);
        mTextView3.setText(test.get(4));

        TextView mTextView4 = (TextView) v.findViewById(R.id.details_pressure);
        mTextView4.setText(test.get(5));

        ImageView imageView = (ImageView) v.findViewById(R.id.details_icon);
        String icon = test.get(3);

        URL weatherIcon = ConnectUtils.buildImageUrl(icon);

        Picasso.get().load(weatherIcon.toString()).into(imageView);

        return new AlertDialog.Builder(getActivity()).setView(v).create();
    }




}
