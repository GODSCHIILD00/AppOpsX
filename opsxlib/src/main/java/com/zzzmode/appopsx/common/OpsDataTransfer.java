package com.zzzmode.appopsx.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zl on 2016/11/5.
 */

public class OpsDataTransfer{
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private OnRecvCallback callback;

    private boolean running=true;

    public OpsDataTransfer(OutputStream outputStream, InputStream inputStream, OnRecvCallback callback) {
        this.outputStream = new DataOutputStream(outputStream);
        this.inputStream = new DataInputStream(inputStream);
        this.callback=callback;
    }

    public OpsDataTransfer(OutputStream outputStream, InputStream inputStream) {
        this(outputStream,inputStream,null);
    }

    public void setCallback(OnRecvCallback callback) {
        this.callback = callback;
    }

    public void sendMsg(String text) throws IOException {
        if(text != null){
            sendMsg(text.getBytes());
        }
    }

    public void sendMsg(byte[] msg) throws IOException {
        if(msg != null){
            outputStream.writeInt(msg.length);
            outputStream.write(msg);
            outputStream.flush();
        }
    }

    public interface OnRecvCallback{
        void onMessage(byte[] bytes);
    }

    public void handleRecv() {
        try {
            while (running){
                int len = inputStream.readInt();
                byte[] bytes = new byte[len];
                if(inputStream.read(bytes, 0, len) == len){
                    onRecvMsg(bytes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onRecvMsg(byte[] bytes) {
        if (callback != null) {
            callback.onMessage(bytes);
        }
    }

    public void stop() {
        running = false;

        try {
            if (outputStream != null)
                outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (inputStream != null)
                inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}