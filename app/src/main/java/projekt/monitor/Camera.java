package projekt.monitor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.*;
import android.util.Log;
import java.net.Socket;


import projekt.monitor.ui.monitor.ButtonsFragment;


public class Camera
{
    private Socket socket;
    private int tcpPort = 10000;
    private boolean socketRunning = false;
    private String ip;
    private final String LOG_TAG = ButtonsFragment.class.getSimpleName();

    public Camera(String ip, int tcpPort)
    {
        this.tcpPort = tcpPort;
        this.ip = ip;
    }

    //Überträgt die gewünschte Soll-Position
    public void setTargetPosition(int x, int y)
    {
        final JSONObject object = new JSONObject();

        try {
            object.put("X", x);
            object.put("Y", y);
            //object.put("pos", "pos");

            try {
                byte[] data = object.toString().getBytes("utf-8");
                new Send(data, ip).start();
            } catch (UnsupportedEncodingException e) {
                //e.printStackTrace();
                Log.e(LOG_TAG, "Failed to create data", e);
            }


        } catch (JSONException e) {
            //e.printStackTrace();
            Log.e(LOG_TAG, "Failed to create JSONObject", e);
        }

    }

    //Überträgt die Richtung (z.B. Pfeiltasten & Joystick)
    public void sendDirection(int x,int y)
    {
        Log.d(LOG_TAG, "Send Direction");
        final JSONObject object = new JSONObject();
        try{
            object.put("X", x);
            object.put("Y", y);
            //object.put("speed", "speed");

            try {
                byte[] data = object.toString().getBytes("utf-8");
                Log.d(LOG_TAG, object.toString());
                new Send(data, ip).start();
            } catch (UnsupportedEncodingException e) {
                //e.printStackTrace();
                Log.e(LOG_TAG, "Failed to create data", e);
            }


        } catch(JSONException e){
            Log.e(LOG_TAG, "Failed to create JSONObject", e);
        }
    }

    //Fragt beim Raspberry aktuelle Position an
    public void requestPosition()
    {


    }

    //UNKLAR
    //Empfängt Daten - derzeit nur Position
    public void recieve()
    {

    }
    

}

class Send extends Thread {
    private String ip;
    private byte[] data;
    private final String LOG_TAG = ButtonsFragment.class.getSimpleName();
    private Socket socket;
    private int tcpPort = 10000;
    private boolean socketRunning = false;

    public Send(byte[] data, String ip ) {
        this.ip = ip;
        this.data = data;
    }


    public void run() {
        Log.d(LOG_TAG, "Dateien werden gesendet");
        try {
            socket = new Socket("192.168.1.14", 10000);
            socketRunning = true;

            try {
                Log.d(LOG_TAG, "Try send data");
                OutputStream output = socket.getOutputStream();
                output.write(data);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to get socket OutputStream", e);
            }

            socket.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Socket error", e);
        }

    }
}
