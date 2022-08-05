package com.example.balance_metrobus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.balance_metrobus.Adapter.AdapterCard;
import com.example.balance_metrobus.Alerts.Alert;
import com.example.balance_metrobus.Alerts.ChargingAlert;
import com.example.balance_metrobus.Alerts.ConnectionAlert;
import com.example.balance_metrobus.Alerts.DeleteAlert;
import com.example.balance_metrobus.Interface.Refresh;
import com.example.balance_metrobus.Models.CardInformation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Refresh {

    ListView lstcard;
    FloatingActionButton btnadd, btnrefresh;
    String URL = "https://saldometrobus.yizack.com/api/0/tarjeta/";
    ChargingAlert chargingAlert = new ChargingAlert();
    View internet, nointernet;
    List<CardInformation> cardInformationList;
    TextView delete_information;
    String number;
    ImageButton btnrefreshcard;
    AdapterCard adapterCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeControls();
        ValidateInternetConnection();
        AddNewCard();
        RefreshConnection();

        ShowCards();
        DeleteCard();
    }

    private void AddNewCard() {
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alert alerta = new Alert();
                alerta.show(getSupportFragmentManager(), "alertTag");
            }
        });
    }

    private void RefreshConnection() {
        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionAlert alert = new ConnectionAlert();
                alert.show(getSupportFragmentManager(), null);
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        alert.dismiss();
                    }
                };

                Timer timer = new Timer();
                timer.schedule(timerTask, 1000);
                ValidateInternetConnection();
            }
        });
    }

    private void WebService(String s, String name) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("ok")) {
                        String datos = response.getString("tarjeta");

                        JSONObject jsonObject = new JSONObject(datos);

                        CardInformation cardInformation = new CardInformation(
                                jsonObject.getString("numero"),
                                jsonObject.getString("saldo"),
                                jsonObject.getString("estado"),
                                jsonObject.getString("fecha"),
                                jsonObject.getString("tipo"),
                                name);

                        boolean insert = cardInformation.insert(getApplicationContext());
                        if (insert) {
                            Toast.makeText(getApplicationContext(), "tarjeta añadida: " + jsonObject.getString("numero"), Toast.LENGTH_SHORT).show();
                            chargingAlert.dismiss();
                            ShowCards();
                        } else {
                            Toast.makeText(MainActivity.this, "Ocurrio un error al guardar la tarjeta en la base de datos", Toast.LENGTH_SHORT).show();
                            chargingAlert.dismiss();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Error al encontrar el numero de tarjeta", Toast.LENGTH_SHORT).show();
                        chargingAlert.dismiss();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error al cargar tarjeta, intente nuevamente", Toast.LENGTH_SHORT).show();
                    chargingAlert.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al cargar tarjeta, intente nuevamente", Toast.LENGTH_SHORT).show();
                chargingAlert.dismiss();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void InitializeControls() {
        lstcard = (ListView) findViewById(R.id.lstCards);
        btnadd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnrefresh = (FloatingActionButton) findViewById(R.id.btnRefresh);
        internet = (View) findViewById(R.id.internet);
        nointernet = (View) findViewById(R.id.nointernet);
        delete_information = (TextView) findViewById(R.id.delete_information);
        btnrefreshcard = (ImageButton) findViewById(R.id.btnRefreshcard);
    }

    private void ValidateInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            nointernet.setVisibility(View.GONE);
            internet.setVisibility(View.VISIBLE);
        } else {
            internet.setVisibility(View.GONE);
            nointernet.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Sin conexion", Toast.LENGTH_SHORT).show();
        }
    }

    public void ShowCards() {
        cardInformationList = new CardInformation().GetCards(getApplicationContext());

        if (!cardInformationList.isEmpty()) {
            delete_information.setVisibility(View.VISIBLE);
        }

        adapterCard = new AdapterCard(getApplicationContext(), cardInformationList);
        lstcard.setAdapter(adapterCard);
        adapterCard.notifyDataSetChanged();
    }

    private void DeleteCard() {
        lstcard.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DeleteAlert deleteAlert = new DeleteAlert(cardInformationList.get(i).getCardnumber());
                deleteAlert.show(getSupportFragmentManager(), "DAlert");
                return false;
            }
        });
    }

    private long time;

    @Override
    public void onBackPressed() {
        if (time + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Vuelve a presionar atras para salir", Toast.LENGTH_SHORT).show();
        }
        time = System.currentTimeMillis();
    }

    @Override
    public void RefreshCards() {
        ShowCards();
    }

    @Override
    public void AddCard(String number, String name) {
        this.number = number;
        ValidateInternetConnection();
        if (!ValidateCardRepeat()) {
            chargingAlert.show(getSupportFragmentManager(), "Calert");
            WebService(URL + number, name);
        } else {
            Toast.makeText(this, "Esta tarjeta ya esta agregada", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void DeleteInfo() {
        if (cardInformationList.isEmpty()) {
            delete_information.setVisibility(View.INVISIBLE);
        }
    }

    private boolean ValidateCardRepeat() {
        CardInformation cardInformation = new CardInformation();
        return cardInformation.ValidateCards(this, number);
    }
}