package com.example.scrollinglist.tve;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.scrollinglist.R;
import com.vmn.android.tveauthcomponent.component.TVEComponent;
import com.vmn.android.tveauthcomponent.constants.TVEAuthConstants;
import com.vmn.android.tveauthcomponent.error.TVEException;
import com.vmn.android.tveauthcomponent.error.TVEMessage;
import com.vmn.android.tveauthcomponent.model.TVESubscriber;

/**
 * Created by belangek on 9/27/16.
 */
public class LoginActivity extends AppCompatActivity implements TVEDelegate.TveEventListener {
    TVEDelegate tve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    @Override
    protected void onResume() {
        super.onResume();
        tve = TVEDelegate.getInstance();
        tve.subscribeEventListener(this);
        if(tve.isLoginFormAvailable()) {
            loginFormPrepared(tve.getLoginForm());
        } else {
            TVEComponent.getInstance().login();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        tve.unsubscribeEventListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TVEComponent.getInstance().onActivityResult(requestCode, resultCode, data);
    }
    public void notListedButtonClick(View v) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
//workaround for android issue https://code.google.com/p/android/issues/detail?id=40323
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment tveAuthFragment = fragmentManager
                .findFragmentByTag(TVEAuthConstants.TVE_FRAGMENT_TAG);
        if (tveAuthFragment != null &&
                tveAuthFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
            TVEComponent.getInstance().backButtonClick();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void initializationCompleted(TVESubscriber subscriber) {
    }
    @Override
    public void checkStatusCompleted(TVESubscriber subscriber) {
        System.out.println(subscriber);
    }
    @Override
    public void loginFormPrepared(Fragment tveFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.authFragmentContainer, tveFragment,
                        TVEAuthConstants.TVE_FRAGMENT_TAG)
                .commit();
    }
    @Override
    public void loginCompleted(TVESubscriber subscriber) {
    }
    @Override
    public void logoutCompleted() {
        finish();
    }
    @Override
    public void errorHappened(final TVEException error) {
        Log.e("NEW ERROR: ", "Localized " + error.getLocalizedMessage() +
                " Message: " + error.getMessage() + " Code: " + error.getCode() +
                " Provider: " + error.getProviderInvolved() +
                " Cause: " + error.getCause());
        Toast.makeText(getApplicationContext(), "errorHappened:" + error.getCode() +
                "\n" +
                error.getMessage(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDisplayMessage(TVEMessage message) {
        Toast.makeText(this, message.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onUserIdChanged(String userID) {
    }
    @Override
    public void learnMoreButtonClicked() {
        Toast.makeText(getApplicationContext(), "Learn more button clicked",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public void watchNowButtonClicked() {
        finish();
    }
    @Override
    public void closeButtonClicked() {
        finish();
    }
    @Override
    public void freePreviewHasJustExpired() {
        Toast.makeText(this, "free preview has just expired",
                Toast.LENGTH_SHORT).show();
    }
}