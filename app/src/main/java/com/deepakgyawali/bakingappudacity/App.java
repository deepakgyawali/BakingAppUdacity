package com.deepakgyawali.bakingappudacity;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

/**
 * App Class
 * Created by Deepak Gyawali, www.deepakgyawali.com.np
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 2018
 */


public class App extends Application {
    private static App instance;
    private Gson gson;
    private EventBus eventBus;

    public App() {
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createGson();
        createEventBus();
    }

    private void createEventBus() {
        eventBus = EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .build();
    }

    private void createGson() {
        gson = new GsonBuilder().create();
    }

    public Gson getGson() {
        return gson;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}

