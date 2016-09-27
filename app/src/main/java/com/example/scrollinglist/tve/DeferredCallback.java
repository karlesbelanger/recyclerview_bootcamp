package com.example.scrollinglist.tve;

/**
 * Created by belangek on 9/27/16.
 */

public interface DeferredCallback {
    int CODE_LOGIN = 0x1;
    int CODE_LOGOUT = 0x2;
    int CODE_ERROR = 0x3;
    int CODE_FORM_PREPARED = 0x4;
    int CODE_DISPLAY_MESSAGE = 0x5;
    void onReceivedCallback();
}
