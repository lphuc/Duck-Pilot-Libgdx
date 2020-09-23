package com.phucle.fadu;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.phucle.dupi.R;
import com.phucle.fadu.screens.AbstractGameScreen;
import com.phucle.fadu.screens.MainGameScreen;

public class AndroidLauncher extends AndroidApplication implements IGooglePlayService, com.phucle.fadu.IAdvertisement, RewardedVideoAdListener {
    //BA:3E:6A:93:FD:3D:E3:C7:FA:12:8B:76:32:03:BD:77:86:B6:B5:6F
    // Client used to sign in with Google APIs
    private GoogleSignInClient mGoogleSignInClient;

    // Client variables
    private AchievementsClient mAchievementsClient;
    private LeaderboardsClient mLeaderboardsClient;
    private PlayersClient mPlayersClient;

    // request codes we use when invoking an external activity
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;


    //for show Advertisement
    public RelativeLayout layout;

    private RewardedVideoAd videoAd;
    private InterstitialAd interstitialAd;
    private boolean isRewardShown;

    private static com.phucle.fadu.screens.MainGameScreen mainGame;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        callForInitializeForView();
        super.onCreate(savedInstanceState);
        initView();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new DupiMain(this, (IAdvertisement) this), config);

        initRewardedAd();
        initInterstitialAd();
        initGooglePlayService();

        // it is recommended to try and sign in silently from when the app resumes.
        if (isSignedIn()) {
            signInSilently();
        } else {
            startSignInIntent();
        }
    }

    /**
     * Create the client used to sign in to Google services.
     */
    private void initGooglePlayService() {
        mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

    }

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    private void signInSilently() {
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            onConnected(task.getResult());
                            Toast.makeText(getApplicationContext(), R.string.sign_in_success, Toast.LENGTH_SHORT).show();
                        } else {
                            onDisconnected();
                            Toast.makeText(getApplicationContext(), R.string.sign_in_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startSignInIntent() {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }


    /**
     * Setup InterstitialAd
     */
    private void initInterstitialAd() {
        interstitialAd = new InterstitialAd(this);
        if (BuildConfig.DEBUG) {
            interstitialAd.setAdUnitId(Consts.INTERSTITIAL_FOR_TEST);
        } else {
            interstitialAd.setAdUnitId(Consts.INTERSTITIAL_FOR_RELEASE);
        }

        // Create an ad request.
        final AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        // Optionally populate the ad request builder.
        adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

        // Set an AdListener.
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (BuildConfig.DEBUG) {
                    Toast.makeText(getApplicationContext(), "The interstitial is loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAdClosed() {
                if (BuildConfig.DEBUG) {
                    Toast.makeText(getApplicationContext(), "The interstitial is closed", Toast.LENGTH_SHORT).show();
                }

                interstitialAd.loadAd(adRequestBuilder.build());
            }
        });

        // Start loading the ad now so that it is ready by the time the user is ready to go to
        interstitialAd.loadAd(adRequestBuilder.build());
    }


    /**
     * all stuffs related to Admob are initialized here
     */
    private void initRewardedAd() {
        // init Ad
        if (BuildConfig.DEBUG) {
            MobileAds.initialize(this, Consts.VIDEO_ID_FOR_TEST);
        } else {
            MobileAds.initialize(this, Consts.VIDEO_ID_FOR_RELEASE);
        }

        // Use an activity context to get the rewarded video instance.
        videoAd = MobileAds.getRewardedVideoAdInstance(this);
        videoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();
    }


    protected void initView() {

        layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        View gameView = initializeForView(new DupiMain(this, this), config);
        RelativeLayout.LayoutParams gameViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        gameViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        gameViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

        gameView.setLayoutParams(gameViewParams);
        layout.addView(gameView);
        setContentView(layout);
    }


    /**
     * It is highly recommended that you call loadAd() as early as possible
     * (for example, in the onCreate() method of your Activity) to allow videos to be preloaded.
     */
    private void loadRewardedVideoAd() {
        if (BuildConfig.DEBUG) {
            videoAd.loadAd(Consts.VIDEO_ID_FOR_TEST, new AdRequest.Builder().build());
        } else {
            videoAd.loadAd(Consts.VIDEO_ID_FOR_RELEASE, new AdRequest.Builder().build());
        }
    }

    private void callForInitializeForView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        runOnUiThread(new Runnable() {
            public void run() {
                if (videoAd.isLoaded()) {
                    videoAd.destroy(getApplicationContext());
                } else {
                    if (BuildConfig.DEBUG) {
                        Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        runOnUiThread(new Runnable() {
            public void run() {
                if (videoAd.isLoaded()) {
                    videoAd.pause(getApplicationContext());
                } else {
                    if (BuildConfig.DEBUG) {
                        Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        runOnUiThread(new Runnable() {
            public void run() {
                if (videoAd.isLoaded()) {
                    videoAd.resume(getApplicationContext());
                } else {
                    if (BuildConfig.DEBUG) {
                        Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        if (request == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error);
                }

                onDisconnected();

                new AlertDialog.Builder(this)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }
    }

    @Override
    public boolean checkIsSignIn() {
        return isSignedIn();
    }

    @Override
    public void login() {
        if (com.phucle.fadu.screens.AbstractGameScreen.networkConnected) {
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        startSignInIntent();
                    }
                });
            } catch (final Exception e) {
                if (BuildConfig.DEBUG) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void logout() {
        if (!isSignedIn()) {
            return;
        }

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean successful = task.isSuccessful();
                        onDisconnected();
                    }
                });

    }

    @Override
    public void submitScore(final int score) {
        if (com.phucle.fadu.screens.AbstractGameScreen.networkConnected) {
            if (!isSignedIn()) {
                return;
            }
            try {
                mLeaderboardsClient.submitScore(getString(R.string.leaderboard_world_top), score);
            } catch (final Exception e) {
                // TODO: 2/4/2019 crash
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }


    @Override
    public void unlockAchievement(String achievementId) {
        if (com.phucle.fadu.screens.AbstractGameScreen.networkConnected) {
            if (!isSignedIn()) {
                return;
            }
            mAchievementsClient.unlock(achievementId);
        }
    }

    @Override
    public void getLeaderBoard() {
        if (AbstractGameScreen.networkConnected) {
            if (isSignedIn()) {
                mLeaderboardsClient.getLeaderboardIntent(getString(R.string.leaderboard_world_top))
                        .addOnSuccessListener(new OnSuccessListener<Intent>() {
                            @Override
                            public void onSuccess(Intent intent) {
                                if (BuildConfig.DEBUG)
                                    Toast.makeText(getApplicationContext(), "Get Leader board successfully!", Toast.LENGTH_SHORT).show();
                                startActivityForResult(intent, RC_UNUSED);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to get leader board: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                login();
            }
        }
    }


    @Override
    public void getAchievement() {
        if (isSignedIn()) {
            mAchievementsClient.getAchievementsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_UNUSED);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } else {
            login();
        }
    }

    @Override
    public void showVideoRewardAd(MainGameScreen gameScreen) {
        mainGame = null; //clear cache to avoid memory leak
        mainGame = gameScreen;
        isRewardShown = false;

        try {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (videoAd.isLoaded()) {
                        videoAd.show();
                    } else {
                        if (BuildConfig.DEBUG) {
                            Toast.makeText(getApplicationContext(), "Loading Video Ad", Toast.LENGTH_SHORT).show();
                        }
                        mainGame.endGame();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("AdMobHelper", "Exception in show Video Ad", e);
        }
    }

    @Override
    public void showInterstitialAd() {
        try {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    } else {
                        if (BuildConfig.DEBUG) {
                            Toast.makeText(getApplicationContext(), "Loading new Interstitial Ad..", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        } catch (Exception e) {

        }
    }


    @Override
    public void loadVideoReward() {
        loadRewardedVideoAd();
    }

    @Override
    public void destroy() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (videoAd.isLoaded()) {
                        videoAd.destroy(getApplicationContext());
                    }
                }
            });

        } catch (Exception e) {

        }

    }

    @Override
    public void pause() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (videoAd.isLoaded()) {
                        videoAd.pause(getApplicationContext());
                    }
                }
            });

        } catch (Exception e) {

        }
    }

    @Override
    public void resume() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (videoAd.isLoaded()) {
                        videoAd.resume(getApplicationContext());
                    }
                }
            });

        } catch (Exception e) {

        }
    }

    @Override
    public void onRewarded(RewardItem reward) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " + reward.getAmount(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saved duck successfully, keep moving!", Toast.LENGTH_SHORT).show();
        }

        isRewardShown = true;
        // load next video for reservation
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        if (BuildConfig.DEBUG)
            Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        if (BuildConfig.DEBUG)
            Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();

        if (isRewardShown) {
            mainGame.resumeCurrentGameState();
        } else {
            mainGame.endGame();
        }

        // destroy current video ad to free up memory
        try {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (videoAd.isLoaded()) {
                        videoAd.destroy(getApplicationContext());
                    } else {
                        if (BuildConfig.DEBUG) {
                            Toast.makeText(getApplicationContext(), "Loading new Video Ad..", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        } catch (Exception e) {

        }

        // Load the next rewarded video ad.
        loadRewardedVideoAd();

        DupiMain.isAdvLoaded = false;
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        DupiMain.isAdvLoaded = false;
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRewardedVideoCompleted() {
        Log.d("aaa", "aaa");
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        DupiMain.isAdvLoaded = true;
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRewardedVideoStarted() {
        if (BuildConfig.DEBUG)
            Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }


    private void onDisconnected() {
        mAchievementsClient = null;
        mLeaderboardsClient = null;
        mPlayersClient = null;
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {

        mAchievementsClient = Games.getAchievementsClient(this, googleSignInAccount);
        mLeaderboardsClient = Games.getLeaderboardsClient(this, googleSignInAccount);
        mPlayersClient = Games.getPlayersClient(this, googleSignInAccount);

        // Show sign-out button on main menu
        // TODO: 1/25/2019

        // Set the greeting appropriately on main menu
        mPlayersClient.getCurrentPlayer()
                .addOnCompleteListener(new OnCompleteListener<Player>() {
                    @Override
                    public void onComplete(@NonNull Task<Player> task) {
                        String displayName;
                        if (task.isSuccessful()) {
                            displayName = task.getResult().getDisplayName();
                            if (BuildConfig.DEBUG) {
                                Toast.makeText(getApplicationContext(), displayName + ", You are signed in with Google account!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Exception e = task.getException();
                            handleException(e, getString(R.string.players_exception));
                        }
                    }
                });

    }

    private void handleException(Exception e, String details) {
        int status = 0;

        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            status = apiException.getStatusCode();
        }

        String message = getString(R.string.status_exception_error, details, status, e);

        if (BuildConfig.DEBUG) {
            new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setNeutralButton(android.R.string.ok, null)
                    .show();
        }
    }

}
