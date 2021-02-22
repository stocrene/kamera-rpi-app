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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


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


    //private Socket socket;

    public Camera(String ip, int tcpPort)
    {
        this.tcpPort = tcpPort;
        this.ip = ip;
        x_pos = 0;
        y_pos = 0;
/*
        try {
            socket = new Socket(ip, tcpPort);
            socketRunning = true;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Socket error", e);
        }
*/
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
                requestPosition();
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

        new Request("192.168.1.13", 10001).start();



        /*
        final JSONObject object = new JSONObject();

        try
        {
            object.put("REQUEST:", "position");
            //Erwarte: {A: position} {x = 11} {y = 22}
            try
            {
                byte[] data = object.toString().getBytes("utf-8");
                Log.d(LOG_TAG, object.toString());
                //new Send(data, ip, 10001).start();
                //receive();

                //Wenn antwort da
                //if ()
                //updatePosition();
            }
            catch (UnsupportedEncodingException e)
            {
                Log.e(LOG_TAG, "Failed to create request", e);
            }


        }
        catch (JSONException e)
        {
            //e.printStackTrace();
            Log.e(LOG_TAG, "Failed to create JSONObject", e);
        }

         */
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

class Request extends Thread
{
    private String ip;
    private final String LOG_TAG = ButtonsFragment.class.getSimpleName();
    private byte[] datain;
    private Socket socket;
    private int tcpPort;
    private boolean socketRunning = false;
    private Socket clientSocket;

    public Request(String ip, int tcpPort)
    {
        this.ip = ip;
        this.tcpPort = tcpPort;
    }

    public void run()
    {
        final JSONObject object = new JSONObject();

        try
        {
            //Verbindungsaufbau
            Log.d(LOG_TAG, "Verbindungsaufbau");
            socket = new Socket(ip, tcpPort);
            socketRunning = true;
            Log.d(LOG_TAG, "Verbindung aufgebaut");

            try
            {
                object.put("REQUEST:", "position");
                try
                {
                    byte[] data = object.toString().getBytes("utf-8");
                    Log.d(LOG_TAG, object.toString());
                    OutputStream output = socket.getOutputStream();
                    output.write(data);

                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String strInput = null;
                    while ((strInput = in.readLine()) != null)
                    {
                        Log.d(LOG_TAG, strInput);
                    }


                }
                catch (UnsupportedEncodingException e)
                {
                    Log.e(LOG_TAG, "Failed to create request", e);
                }

            }
            catch (JSONException e)
            {
                //e.printStackTrace();
                Log.e(LOG_TAG, "Failed to create JSONObject", e);
            }


            socket.close();
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "Socket error", e);
        }

        /*
        try
        {
            InputStream input = socket.getInputStream();
            input.read(datain);
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Failed to get socket InputStream", e);
        }
        */

    }
/*
    public byte[] get_data()
    {
        return datain;
    }
*/
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