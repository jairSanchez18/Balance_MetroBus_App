package com.example.balance_metrobus.Alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.balance_metrobus.Interface.Refresh;
import com.example.balance_metrobus.MainActivity;
import com.example.balance_metrobus.Models.CardInformation;
import com.example.balance_metrobus.R;

public class DeleteAlert extends DialogFragment {

    String cardnumber;
    Refresh refresh;
    private Refresh listener;

    public DeleteAlert(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Borrar Tarjeta")
                .setMessage("¿Desea borrar esta tarjeta?")
                .setPositiveButton("si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CardInformation cardInformation = new CardInformation();
                        cardInformation.DeleteCards(getContext().getApplicationContext(), cardnumber);
                        listener.RefreshCards();
                        listener.DeleteInfo();
                        Toast.makeText(getContext().getApplicationContext(), "Tarjeta Borrada correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
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
