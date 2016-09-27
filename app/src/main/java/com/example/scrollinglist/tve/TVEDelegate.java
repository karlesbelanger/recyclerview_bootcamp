package com.example.scrollinglist.tve;

/**
 * Created by belangek on 9/27/16.
 */
import android.support.v4.app.Fragment;
import android.util.Log;
import com.vmn.android.tveauthcomponent.component.TVEComponentDelegate;
import com.vmn.android.tveauthcomponent.error.TVEException;
import com.vmn.android.tveauthcomponent.error.TVEMessage;
import com.vmn.android.tveauthcomponent.model.TVESubscriber;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import static com.example.scrollinglist.tve.DeferredCallback.CODE_DISPLAY_MESSAGE;
import static com.example.scrollinglist.tve.DeferredCallback.CODE_ERROR;
import static com.example.scrollinglist.tve.DeferredCallback.CODE_FORM_PREPARED;
import static com.example.scrollinglist.tve.DeferredCallback.CODE_LOGOUT;
public class TVEDelegate implements TVEComponentDelegate {
    private static final String TAG = TVEComponentDelegate.class.getSimpleName();
    private Set<TveEventListener> listeners = new HashSet<>();
    private boolean isInitialized;
    private ArrayDeque<DeferredCallbackImpl> deferredCallbacks = new ArrayDeque<>();
    private TVESubscriber currentSubscriber;
    private String currentUserId;
    private Fragment authFragment;
    private static class Holder {
        private static final TVEDelegate tveDelegateInstance = new TVEDelegate();
    }
    public static TVEDelegate getInstance() {
        return Holder.tveDelegateInstance;
    }
    public boolean isInitialized() {
        return isInitialized;
    }
    public boolean isLoginFormAvailable() {
        return authFragment != null;
    }
    public Fragment getLoginForm() {
        Fragment result = authFragment;
        authFragment = null;
        return result;
    }
    public void setLoginForm(Fragment form) {
        authFragment = form;
    }
    @Override
    public void initializationCompleted(TVESubscriber subscriber) {
        Log.i(TAG, "* initializationCompleted: " + subscriber.isLoggedIn());
        currentSubscriber = subscriber;
        isInitialized = true;
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.initializationCompleted(subscriber);
            }
        }
    }
    @Override
    public void checkStatusCompleted(TVESubscriber subscriber) {
        Log.i(TAG, "* checkStatusCompleted: " + subscriber.isLoggedIn());
        currentSubscriber = subscriber;
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.checkStatusCompleted(subscriber);
            }
        }
    }
    @Override
    public void loginFormPrepared(Fragment tveFragment) {
        Log.i(TAG, "* loginFormPrepared: " + tveFragment);
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.loginFormPrepared(tveFragment);
            }
        } else {
            deferredCallbacks.addFirst(new
                    DeferredCallbackImpl.Builder(CODE_FORM_PREPARED)
                    .setLoginForm(tveFragment).build());
        }
    }
    @Override
    public void loginCompleted(TVESubscriber subscriber) {
        Log.i(TAG, "* loginCompleted: " + subscriber.isLoggedIn());
        currentSubscriber = subscriber;
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.loginCompleted(subscriber);
            }
        } else {
        }
    }
    @Override
    public void logoutCompleted() {
        Log.i(TAG, "* logoutCompleted");
        currentSubscriber = null;
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.logoutCompleted();
            }
        } else {
            deferredCallbacks.addFirst(new
                    DeferredCallbackImpl.Builder(CODE_LOGOUT).build());
        }
    }
    @Override
    public void errorHappened(TVEException error) {
        Log.i(TAG, "* errorHappened: " + error);
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.errorHappened(error);
            }
        } else {
            deferredCallbacks.addFirst(new DeferredCallbackImpl.Builder(CODE_ERROR)
                    .setException(error).build());
        }
    }
    @Override
    public void onDisplayMessage(TVEMessage message) {
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.onDisplayMessage(message);
            }
        } else {
            deferredCallbacks.addFirst(new
                    DeferredCallbackImpl.Builder(CODE_DISPLAY_MESSAGE)
                    .setMessage(message).build());
        }
    }
    @Override
    public void onUserIdChange(String userId) {
        Log.i(TAG, "* onUserIdChange: " + userId);
        currentUserId = userId;
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.onUserIdChanged(userId);
            }
        }
    }
    @Override
    public void learnMoreButtonClicked() {
        Log.i(TAG, "* learnMoreButtonClicked");
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.learnMoreButtonClicked();
            }
        }
    }
    @Override
    public void watchNowButtonClicked() {
        Log.i(TAG, "* watchNowButtonClicked");
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.watchNowButtonClicked();
            }
        }
    }
    @Override
    public void closeButtonClicked() {
        Log.i(TAG, "* closeButtonClicked");
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.closeButtonClicked();
            }
        }
    }
    @Override
    public void freePreviewHasJustExpired() {
        Log.i(TAG, "* freePreviewHasJustExpired");
        if (listeners.size() > 0) {
            for (TveEventListener listener : listeners) {
                listener.freePreviewHasJustExpired();
            }
        }
    }
    public interface TveEventListener {
        void initializationCompleted(TVESubscriber subscriber);
        void checkStatusCompleted(TVESubscriber subscriber);
        void loginFormPrepared(Fragment tveFragment);
        void loginCompleted(TVESubscriber subscriber);
        void logoutCompleted();
        void errorHappened(TVEException error);
        void onDisplayMessage(TVEMessage message);
        void onUserIdChanged(String userId);
        void learnMoreButtonClicked();
        void watchNowButtonClicked();
        void closeButtonClicked();
        void freePreviewHasJustExpired();
    }
    public void subscribeEventListener(TveEventListener listener) {
        listeners.add(listener);
        deliverCallbacks(listener);
    }
    public void unsubscribeEventListener(TveEventListener listener) {
        listeners.remove(listener);
    }
    private void deliverCallbacks(TveEventListener listener) {
        while (deferredCallbacks.size() > 0) {
            deferredCallbacks.pollLast().setListener(listener).onReceivedCallback();
        }
    }
    public TVESubscriber getCurrentTveSubscriber() {
        return currentSubscriber == null ? null : currentSubscriber;
    }
    public String getCurrentUserId() {
        return currentUserId;
    }
}