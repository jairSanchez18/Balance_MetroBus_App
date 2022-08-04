package com.example.balance_metrobus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.balance_metrobus.Models.CardInformation;
import com.example.balance_metrobus.R;

import org.json.JSONObject;

import java.util.List;

public class AdapterCard extends ArrayAdapter<CardInformation> {

    private List<CardInformation> informationList;
    Context context;
    String URL = "https://saldometrobus.yizack.com/api/0/tarjeta/";
    CardInformation cardInformation;
    View vistaonclick;
    AlertDialog alert;

    public AdapterCard(Context context, List<CardInformation> cardInformationList) {
        super(context, R.layout.adapter_template, cardInformationList);

        informationList = cardInformationList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View item = inflater.inflate(R.layout.adapter_template, null);

        TextView cardname = (TextView) item.findViewById(R.id.lblCardname);
        TextView cardnumber = (TextView) item.findViewById(R.id.lblCardnumber);
        TextView cardstatus = (TextView) item.findViewById(R.id.lblCardstatus);
        TextView cardtype = (TextView) item.findViewById(R.id.lblCardtype);
        TextView cardbalance = (TextView) item.findViewById(R.id.lblCardBalance);
        ImageView cardimage = (ImageView) item.findViewById(R.id.imgCard);
        ImageButton updatecard = (ImageButton) item.findViewById(R.id.btnRefreshcard);

        cardname.setText(informationList.get(position).getCardname());
        cardnumber.setText("Número: " + informationList.get(position).getCardnumber());
        cardstatus.setText("Estado: " + informationList.get(position).getCardstatus());
        cardtype.setText("Tipo: " + informationList.get(position).getCardtype());
        cardbalance.setText("Saldo: B/. " + informationList.get(position).getCardbalance());
        updatecard.setFocusable(false);

        updatecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cardInformation = new CardInformation(informationList.get(position).getCardnumber());
                //cardInformation.Update(getContext().getApplicationContext(), "40");
                vistaonclick = view;
                AlertDialog.Builder builder = new AlertDialog.Builder(vistaonclick.getRootView().getContext());
                LayoutInflater inflater = LayoutInflater.from(getContext().getApplicationContext());
                final View alerttemplate = inflater.inflate(R.layout.updatealert_template, null);

                builder.setView(alerttemplate);

                alert = builder.create();
                alert.setCancelable(false);
                alert.setCanceledOnTouchOutside(false);

                alert.show();

                WebService(URL + informationList.get(position).getCardnumber());
            }
        });

        if (informationList.get(position).getCardtype().equals("Tarjeta Rapipass")) {
            cardimage.setImageResource(R.drawable.rapipass);
        } else if (informationList.get(position).getCardtype().equals("Tarjeta Normal al Portador b")) {
            cardimage.setImageResource(R.drawable.metrobus);
        } else {
            cardimage.setImageResource(R.drawable.logo_mb);
        }

        if (informationList.get(position).getCardtype().equals("Tarjeta Normal al Portador b")) {
            cardtype.setText("Tipo: Tarjeta Metro-Metrobus");
        }

        return item;
    }

    public void UpdateItem(List<CardInformation> list) {
        this.clear();
        this.addAll(list);
    }

    private void WebService(String URL) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");

                    if (status.equals("ok")) {
                        String datos = response.getString("tarjeta");

                        JSONObject jsonObject = new JSONObject(datos);

                        cardInformation = new CardInformation(jsonObject.getString("numero"));
                        cardInformation.Update(getContext().getApplicationContext(), jsonObject.getString("saldo"));

                        UpdateItem(cardInformation.GetCards(getContext().getApplicationContext()));
                        alert.dismiss();

                    } else {
                        Toast.makeText(getContext().getApplicationContext(), "Error al introducir el numero de tarjeta", Toast.LENGTH_SHORT).show();
                        alert.dismiss();
                    }

                } catch (Exception e) {
                    Toast.makeText(getContext().getApplicationContext(), "Error al cargar tarjeta, intente nuevamente", Toast.LENGTH_SHORT).show();
                    alert.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext().getApplicationContext(), "Error al cargar tarjeta, intente nuevamente", Toast.LENGTH_SHORT).show();
                alert.dismiss();
            }
        });

        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
}
