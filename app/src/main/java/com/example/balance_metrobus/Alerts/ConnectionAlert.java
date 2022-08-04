package com.example.balance_metrobus.Alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.balance_metrobus.R;

public class ConnectionAlert extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext().getApplicationContext());
        final View alerttemplate = inflater.inflate(R.layout.connectionalert_template, null);

        builder.setView(alerttemplate);

        AlertDialog alert = builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);


        return alert;
    }
}
