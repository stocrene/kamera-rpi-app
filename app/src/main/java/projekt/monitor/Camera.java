package projekt.monitor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.*;
import android.util.Log;
import java.net.Socket;

import projekt.monitor.ui.monitor.ButtonsFragment;
import projekt.monitor.ui.monitor.MonitorFragment;


public class Camera
{
    private Socket socket;
    private int tcpPort = 10000;
    private boolean socketRunning = false;
    private String ip;
    private final String LOG_TAG = ButtonsFragment.class.getSimpleName();
    public int x_pos;
    public int y_pos;
    private MonitorFragment monitorFragment = null;



    public Camera(String ip, int tcpPort)
    {
        this.tcpPort = tcpPort;
        this.ip = ip;
        x_pos = 0;
        y_pos = 0;
    }

    //Überträgt die gewünschte Soll-Position
    public void setTargetPosition(int x, int y)
    {
        final JSONObject object = new JSONObject();

        try
        {
            object.put("X", x);
            object.put("Y", y);
            object.put("pos", true);

            try
            {
                byte[] data = object.toString().getBytes("utf-8");
                new Send(data, ip, tcpPort).start();
            }
            catch (UnsupportedEncodingException e)
            {
                //e.printStackTrace();
                Log.e(LOG_TAG, "Failed to create data", e);
            }


        }
        catch (JSONException e)
        {
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
            object.put("pos", false);

            try
            {
                byte[] data = object.toString().getBytes("utf-8");
                Log.d(LOG_TAG, object.toString());
                new Send(data, ip, tcpPort).start();
            }
            catch (UnsupportedEncodingException e)
            {
                //e.printStackTrace();
                Log.e(LOG_TAG, "Failed to create data", e);
            }


        }
        catch(JSONException e)
        {
            Log.e(LOG_TAG, "Failed to create JSONObject", e);
        }
    }

    //Fragt beim Raspberry aktuelle Position an
    public void requestPosition()
    {
        final JSONObject object = new JSONObject();

        try
        {

            object.put("REQUEST:", "position");

            //Erwarte: {A: position} {x = 11} {y = 22}
            try
            {
                byte[] data = object.toString().getBytes("utf-8");
                new Send(data, ip, 10001).start();
                receive();

                //Wenn antwort da
                //if ()
                //updatePosition();
            }
            catch (UnsupportedEncodingException e)
            {
                //e.printStackTrace();
                Log.e(LOG_TAG, "Failed to create request", e);
            }


        }
        catch (JSONException e)
        {
            //e.printStackTrace();
            Log.e(LOG_TAG, "Failed to create JSONObject", e);
        }
    }

    //UNKLAR
    //Empfängt Daten - derzeit nur Position
    public void receive()
    {

    }

    public void addPositionObserver(MonitorFragment monitorFragment)
    {
        this.monitorFragment = monitorFragment;
    }

    private void updatePosition()
    {
        if (monitorFragment != null)
        {
            monitorFragment.updatePosition(x_pos, y_pos);
        }

    }

    private void removePositionObserver(MonitorFragment monitorFragment)
    {
        this.monitorFragment = null;
    }

}

class Receive extends Thread
{
    private String ip;
    private final String LOG_TAG = ButtonsFragment.class.getSimpleName();
    private byte[] data;
    private Socket socket;
    private int tcpPort;
    private boolean socketRunning = false;

    public Receive(String ip, int tcpPort)
    {
        this.ip = ip;
        this.tcpPort = tcpPort;
    }

    public void run()
    {
        Log.d(LOG_TAG, "Dateien werden empfangen");
        try
        {
            socket = new Socket(ip, tcpPort);
            socketRunning = true;

            try
            {
                InputStream input = socket.getInputStream();

            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Failed to get socket InputStream", e);
            }

            socket.close();
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "Socket error", e);
        }
    }

}

class Send extends Thread
{
    private String ip;
    private byte[] data;
    private final String LOG_TAG = ButtonsFragment.class.getSimpleName();
    private Socket socket;
    private int tcpPort;
    private boolean socketRunning = false;

    public Send(byte[] data, String ip, int tcpPort)
    {
        this.ip = ip;
        this.data = data;
        this.tcpPort = tcpPort;
    }


    public void run()
    {
        Log.d(LOG_TAG, "Dateien werden gesendet");
        try
        {
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

            socket.close();
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "Socket error", e);
        }
    }
}
