package com.example.scrollinglist.tve;

/**
 * Created by belangek on 9/27/16.
 */
import android.support.v4.app.Fragment;
import android.util.Log;

import com.vmn.android.tveauthcomponent.error.TVEException;
import com.vmn.android.tveauthcomponent.error.TVEMessage;
import com.vmn.android.tveauthcomponent.model.TVESubscriber;
public class DeferredCallbackImpl implements DeferredCallback {
    private static final String TAG = DeferredCallbackImpl.class.getSimpleName();
    private int code;
    private TVEMessage message;
    private TVEException exception;
    private TVESubscriber subscriber;
    private Fragment tveFragment;
    private TVEDelegate.TveEventListener listener;
    private DeferredCallbackImpl(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.exception = builder.exception;
        this.subscriber = builder.subscriber;
        this.tveFragment = builder.tveFragment;
    }
    public static class Builder {
        private int code;
        private TVEException exception;
        private TVESubscriber subscriber;
        private TVEMessage message;
        private Fragment tveFragment;
        public Builder(int code) {
            this.code = code;
        }
        public Builder setException(TVEException exception) {
            this.exception = exception;
            return this;
        }
        public Builder setSubscriber(TVESubscriber subscriber) {
            this.subscriber = subscriber;
            return this;
        }
        public Builder setMessage(TVEMessage message) {
            this.message = message;
            return this;
        }
        public Builder setLoginForm(Fragment form) {
            this.tveFragment = form;
            return this;
        }
        public DeferredCallbackImpl build() {
            return new DeferredCallbackImpl(this);
        }
    }
    public DeferredCallbackImpl setListener(TVEDelegate.TveEventListener listener) {
        this.listener = listener;
        return this;
    }
    @Override
    public void onReceivedCallback() {
        switch (code) {
            case DeferredCallback.CODE_LOGIN:
                listener.loginCompleted(subscriber);
                Log.d(TAG, "onReceivedCallback: ");
                break;
            case DeferredCallback.CODE_LOGOUT:
                listener.logoutCompleted();
                break;
            case DeferredCallback.CODE_ERROR:
                listener.errorHappened(exception);
                break;
            case DeferredCallback.CODE_FORM_PREPARED:
                listener.loginFormPrepared(tveFragment);
                break;
            case DeferredCallback.CODE_DISPLAY_MESSAGE:
                listener.onDisplayMessage(message);
                break;
        }
    }
}