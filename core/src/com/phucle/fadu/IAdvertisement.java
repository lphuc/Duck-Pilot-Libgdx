package com.phucle.fadu;

import com.phucle.fadu.screens.MainGameScreen;

/*
 * Created by lhphuc on 11/26/2018.
 */
public interface IAdvertisement {

    void showVideoRewardAd(MainGameScreen gameScreen);

    void showInterstitialAd();

    void loadVideoReward();

    void destroy();

    void pause();

    void resume();


}
