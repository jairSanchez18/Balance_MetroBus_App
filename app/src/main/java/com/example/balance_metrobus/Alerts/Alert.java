package com.example.balance_metrobus.Alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.balance_metrobus.Adapter.AdapterCard;
import com.example.balance_metrobus.Interface.Refresh;
import com.example.balance_metrobus.MainActivity;
import com.example.balance_metrobus.Models.CardInformation;
import com.example.balance_metrobus.R;

import org.json.JSONObject;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class Alert extends DialogFragment {

    private List<CardInformation> cardInformationList = new ArrayList<>();
    private EditText cardname;
    private EditText cardnumber;
    Refresh refresh;
    private Refresh listener;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext().getApplicationContext());
        final View alerttemplate = inflater.inflate(R.layout.alert_template, null);
        cardnumber = (EditText) alerttemplate.findViewById(R.id.cardNumber);
        cardname = (EditText) alerttemplate.findViewById(R.id.cardName);

        builder.setView(alerttemplate)
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*Intent intent = new Intent(getContext().getApplicationContext(), MainActivity.class);
                        intent.putExtra("number", cardnumber.getText().toString());
                        intent.putExtra("name", cardname.getText().toString());
                        startActivity(intent);*/
                        String cardn = cardnumber.getText().toString();
                        String cardna = cardname.getText().toString();
                        listener.AddCard(cardn, cardna);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

        return alert;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (Refresh) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "debe implementarse el listener");
        }
    }
}
