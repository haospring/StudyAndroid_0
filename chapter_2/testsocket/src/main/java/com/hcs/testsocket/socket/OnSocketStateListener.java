package com.hcs.testsocket.socket;

public interface OnSocketStateListener {
    /**
     * Socket start
     */
    void onClientStart();

    /**
     * Socket close
     */
    void onClientClose();

    /**
     * Socket exception
     */
    void onClientException(Exception e);
}
