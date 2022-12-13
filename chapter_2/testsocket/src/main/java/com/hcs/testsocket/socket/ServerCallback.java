package com.hcs.testsocket.socket;

public interface ServerCallback {
    /**
     * 接收客户端的消息
     *
     * @param success success
     * @param msg     message
     */
    void receiveClientMsg(boolean success, String msg);

    /**
     * 其他消息
     *
     * @param host host
     * @param msg  message
     */
    void otherMsg(String host, String msg);
}

