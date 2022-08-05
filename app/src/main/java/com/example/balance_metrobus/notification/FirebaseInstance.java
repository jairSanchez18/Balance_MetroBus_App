package com.example.balance_metrobus.notification;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class FirebaseInstance extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}
