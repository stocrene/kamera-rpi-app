package projekt.monitor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.*;
import android.os.Bundle;

import android.util.Log;
import java.net.Socket;




import projekt.monitor.ui.monitor.ButtonsFragment;
import projekt.monitor.ui.monitor.MonitorViewModel;


public class Camera
{
    private int pos;
    private Socket socket;
    private int tcpPort = 10000;
    private boolean socketRunning = false;
    private int x = 0;
    private int y = 0;
    private String ip;
    private final String LOG_TAG = ButtonsFragment.class.getSimpleName();

    public Camera()
    {
        pos = 0;
        ip = "192.168.1.0";
    }

    //Überträgt die gewünschte Soll-Position
    public void setTargetPosition(int x, int y)
    {
        final JSONObject object = new JSONObject();

        try {
            object.put("X", x);
            object.put("Y", y);
            object.put("pos", "pos");

            try {
                byte[] data = object.toString().getBytes("utf-8");
                send(data, ip);
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
    public void sendDirection()
    {

    }

    //Fragt beim Raspberry aktuelle Position an
    public void requestPosition()
    {

        x = 0;
        y = 0;
    }

    //UNKLAR
    //Empfängt Daten - derzeit nur Position
    public void recieve()
    {

    }



    private void send(byte[] data, String  ip)
    {
        try {
            socket = new Socket(ip, tcpPort);
            socketRunning = true;

            try
            {
                Log.d(LOG_TAG, "Try send data");
                OutputStream output = socket.getOutputStream();
                output.write(data);

            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Failed to get socket OutputStream", e);
            }

        } catch (IOException e) {
            //e.printStackTrace();
            Log.e(LOG_TAG, "Socket error", e);
        }




    }


}
