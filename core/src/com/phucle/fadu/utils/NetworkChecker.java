package com.phucle.fadu.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.phucle.fadu.screens.AbstractGameScreen;

/**
 * Created by phuc on 1/19/2019.
 * Check if the game is currently connected to internet
 */
public class NetworkChecker {
    HttpRequestBuilder requestBuilder;
    Net.HttpRequest httpRequest;
    Net.HttpResponseListener responseListener;

    public void checkConnection() {
        requestBuilder = new HttpRequestBuilder();
        httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("https://google.com").build();
        responseListener = new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == 200 && status.getStatusCode() < 300) {
                    // success
                    AbstractGameScreen.networkConnected = true;
                } else {
                    // failed
                    AbstractGameScreen.networkConnected = false;
                }
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        };
        Gdx.net.sendHttpRequest(httpRequest, responseListener);
    }

}
