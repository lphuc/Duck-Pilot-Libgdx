package com.phucle.fadu;

/*
 * Created by Phuc Engineer on 11/25/2018.
 */
public interface IGooglePlayService {

    boolean checkIsSignIn();

    void login();

    void logout();

    void submitScore(int score);

    void unlockAchievement(String achievementId);

    void getLeaderBoard();

    void getAchievement();
}
