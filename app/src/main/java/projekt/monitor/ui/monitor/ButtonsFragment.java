package projekt.monitor.ui.monitor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.niqdev.mjpeg.MjpegView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import androidx.lifecycle.ViewModelProvider;
import projekt.monitor.R;
import projekt.monitor.Camera;
import projekt.monitor.ui.monitor.MonitorViewModel;



public class ButtonsFragment extends Fragment
{

    private ImageButton imageButtonL;
    private ImageButton imageButtonR;
    private ImageButton imageButtonU;
    private ImageButton imageButtonD;
    private ImageView imageViewArrowR;
    private ImageView imageViewArrowL;
    private ImageView imageViewArrowU;
    private ImageView imageViewArrowD;
    private MjpegView mjpegView;

    private Socket socket;
    private int tcpPort = 10000;
    private boolean socketRunning = false;
    private int posX = 0;
    private int posY = 0;

    private final String LOG_TAG = ButtonsFragment.class.getSimpleName();

    private View rootView;
    private View parentView;
    //private MonitorViewModel monitorViewModel


    //Statische IP - Muss noch geändert werden, derzeit aber zu Testzwecke vorhanden
    //MonitorViewModel monitorViewModel = new ViewModelProvider(getParentFragment()).get(MonitorViewModel.class);
    //Camera camera = new Camera(monitorViewModel.ip, tcpPort);
    //Camera camera = new Camera("192.168.1.15", tcpPort);

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        MonitorViewModel monitorViewModel = new ViewModelProvider(getParentFragment()).get(MonitorViewModel.class);
        Camera camera = new Camera(monitorViewModel.ip, tcpPort);
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_buttons, container, false);
        parentView = getParentFragment().getView();

        imageButtonL = (ImageButton) rootView.findViewById(R.id.imageButtonL);
        imageButtonR = (ImageButton) rootView.findViewById(R.id.imageButtonR);
        imageButtonU = (ImageButton) rootView.findViewById(R.id.imageButtonU);
        imageButtonD = (ImageButton) rootView.findViewById(R.id.imageButtonD);
        imageViewArrowR = (ImageView)parentView.findViewById(R.id.imageViewArrowR);
        imageViewArrowL = (ImageView)parentView.findViewById(R.id.imageViewArrowL);
        imageViewArrowU = (ImageView)parentView.findViewById(R.id.imageViewArrowU);
        imageViewArrowD = (ImageView)parentView.findViewById(R.id.imageViewArrowD);
        mjpegView = (MjpegView)parentView.findViewById(R.id.mjpegView);


        imageButtonR.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (mjpegView.getSurfaceView().isShown())
                {
                    if(event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        imageViewArrowR.setVisibility(View.VISIBLE);
                        Log.d(LOG_TAG, "Button Right Touch");
                        if(true)
                        {
                            Log.d(LOG_TAG, "Set Position");
                            camera.sendDirection(100,0);


                            //new SetPositionThread(posX, posY).start();
                            //posX = -1*(posX-180);
                            //posY = -1*(posY-180);
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        imageViewArrowR.setVisibility(View.INVISIBLE);
                        Log.d(LOG_TAG, "Button Right Release");
                    }
                }
                return false;
            }
        });



        imageButtonL.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (mjpegView.getSurfaceView().isShown())
                {
                    if(event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        imageViewArrowL.setVisibility(View.VISIBLE);
                        Log.d(LOG_TAG, "Button Left Touch");
                        camera.sendDirection(-100,0);
                    } else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        imageViewArrowL.setVisibility(View.INVISIBLE);
                        Log.d(LOG_TAG, "Button Left Release");
                    }
                }
                return false;
            }
        });

        imageButtonU.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (mjpegView.getSurfaceView().isShown())
                {
                    if(event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        imageViewArrowU.setVisibility(View.VISIBLE);
                        Log.d(LOG_TAG, "Button Up Touch");
                        camera.sendDirection(0,100);
                    } else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        imageViewArrowU.setVisibility(View.INVISIBLE);
                        Log.d(LOG_TAG, "Button Up Release");
                    }
                }
                return false;
            }
        });

        imageButtonD.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (mjpegView.getSurfaceView().isShown())
                {
                    if(event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        imageViewArrowD.setVisibility(View.VISIBLE);
                        Log.d(LOG_TAG, "Button Down Touch");
                        camera.sendDirection(0,-100);
                    } else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        imageViewArrowD.setVisibility(View.INVISIBLE);
                        Log.d(LOG_TAG, "Button Down Release");
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    /*

    class SetPositionThread extends Thread
    {
        private int x;
        private int y;

        SetPositionThread(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public void run()
        {
            try
            {
                MonitorViewModel monitorViewModel = new ViewModelProvider(getParentFragment()).get(MonitorViewModel.class);
                socket = new Socket(monitorViewModel.ip, tcpPort);
                socketRunning = true;

                try
                {
                    final JSONObject object = new JSONObject();

                    object.put("X", x);
                    object.put("Y", y);

                    try
                    {
                        Log.d(LOG_TAG, "Try send data");
                        OutputStream output = socket.getOutputStream();
                        byte[] data = object.toString().getBytes("utf-8");
                        output.write(data);
                    }
                    catch (IOException e)
                    {
                        Log.e(LOG_TAG, "Failed to get socket OutputStream", e);
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
    */

}
