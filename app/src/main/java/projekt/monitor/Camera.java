package projekt.monitor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.*;
import android.util.Log;
import java.net.Socket;

import projekt.monitor.ui.monitor.MonitorFragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Camera
{
    private String ip;
    private final String LOG_TAG = Camera.class.getSimpleName();
    public int x_pos;
    public int y_pos;
    private MonitorFragment monitorFragment = null;
    

    public Camera(String ip)
    {
        this.ip = ip;
        x_pos = 0;
        y_pos = 0;

    }

    public Camera()
    {
        ip = "";
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
                new Send(data, ip).start();
            }
            catch (UnsupportedEncodingException e)
            {
                Log.e(LOG_TAG, "Failed to create data", e);
            }
        }
        catch (JSONException e)
        {
            Log.e(LOG_TAG, "Failed to create JSONObject", e);
        }

    }

    //Überträgt die Richtung (z.B. Pfeiltasten & Joystick)
    public void sendDirection(int x,int y)
    {
        Log.d(LOG_TAG, "sendDirection(x,y)");
        final JSONObject object = new JSONObject();
        try
        {
            object.put("X", x);
            object.put("Y", y);
            object.put("pos", false);

            try
            {
                byte[] data = object.toString().getBytes("utf-8");
                Log.d(LOG_TAG, object.toString());
                new Send(data, ip).start();
                //requestPosition();
            }
            catch (UnsupportedEncodingException e)
            {
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
        new Request(ip, this).start();
    }


    public void addPositionObserver(MonitorFragment monitorFragment)
    {
        this.monitorFragment = monitorFragment;
    }

    public void updatePosition(int x, int y)
    {

        if (monitorFragment != null)
        {
            monitorFragment.updatePosition(x, y);
        }
    }

    private void removePositionObserver(MonitorFragment monitorFragment)
    {
        this.monitorFragment = null;
    }

}

class Request extends Thread
{
    private String ip;
    private final String LOG_TAG = Request.class.getSimpleName();
    private Socket socket;
    private final int TCP_PORT_REQUEST = 10001;
    private Camera camera;
    private boolean socketRunning = false;
    private int x = 0;
    private int y = 0;

    public Request(String ip, Camera camera)
    {
        this.ip = ip;
        this.camera = camera;
    }

    public void run()
    {
        final JSONObject object = new JSONObject();

        try
        {
            //Verbindungsaufbau
            //Log.d(LOG_TAG, "Verbindungsaufbau");
            socket = new Socket(ip, TCP_PORT_REQUEST);
            socketRunning = true;
            //Log.d(LOG_TAG, "Verbindung aufgebaut");

            try
            {
                object.put("REQUEST", "position");
                try
                {
                    byte[] data = object.toString().getBytes("utf-8");
                    Log.d(LOG_TAG, object.toString());
                    OutputStream output = socket.getOutputStream();
                    output.write(data);

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String strInput = null;
                    while ((strInput = in.readLine()) != null)
                    {
                        Log.d(LOG_TAG, strInput);
                        JSONObject object1 = new JSONObject(strInput);

                        if ((object1.get("ANSWER").toString()).equals("position"))
                        {
                            x = Integer.valueOf(object1.get("x").toString());
                            y = Integer.valueOf(object1.get("y").toString());
                            camera.updatePosition(x,y);
                        }
                        else
                        {
                            Log.d(LOG_TAG, "Objekt passt nicht mit Erwartetem Wert überein");
                        }
                    }
                }
                catch (UnsupportedEncodingException e)
                {
                    Log.e(LOG_TAG, "Failed to create request", e);
                }

            }
            catch (JSONException e)
            {
                Log.e(LOG_TAG, "Failed to create JSONObject", e);
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
    private final String LOG_TAG = Send.class.getSimpleName();
    private final int TCP_PORT = 10000;
    private Socket socket;
    private boolean socketRunning = false;

    public Send(byte[] data, String ip)
    {
        this.ip = ip;
        this.data = data;
    }


    public void run()
    {
        Log.d(LOG_TAG, "Dateien werden gesendet");
        try
        {
            socket = new Socket(ip, TCP_PORT);
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