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
import java.net.Socket;

import androidx.lifecycle.ViewModelProvider;
import projekt.monitor.R;
import projekt.monitor.Camera;


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
    private int x = 70;
    private int y = 70;

    private Camera camera;

    public static boolean button_is_press = false;
    public static String ip;

    private final String LOG_TAG = ButtonsFragment.class.getSimpleName();

    private View rootView;
    private View parentView;

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
        ip = monitorViewModel.ip;
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_buttons, container, false);
        parentView = getParentFragment().getView();

        camera = new Camera(ip);
        camera.addPositionObserver((MonitorFragment)getParentFragment());

        imageButtonL = (ImageButton) rootView.findViewById(R.id.imageButtonL);
        imageButtonR = (ImageButton) rootView.findViewById(R.id.imageButtonR);
        imageButtonU = (ImageButton) rootView.findViewById(R.id.imageButtonU);
        imageButtonD = (ImageButton) rootView.findViewById(R.id.imageButtonD);
        imageViewArrowR = (ImageView)parentView.findViewById(R.id.imageViewArrowR);
        imageViewArrowL = (ImageView)parentView.findViewById(R.id.imageViewArrowL);
        imageViewArrowU = (ImageView)parentView.findViewById(R.id.imageViewArrowU);
        imageViewArrowD = (ImageView)parentView.findViewById(R.id.imageViewArrowD);
        mjpegView = (MjpegView)parentView.findViewById(R.id.mjpegView);


        //Taste RECHTS
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
                        button_is_press = true;
                        new Repeat(x,0, camera).start();
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        imageViewArrowR.setVisibility(View.INVISIBLE);
                        Log.d(LOG_TAG, "Button Right Release");
                        button_is_press = false;
                    }
                }
                return false;
            }
        });


        //Taste LINKS
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
                        button_is_press = true;
                        new Repeat(-x,0, camera).start();
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        imageViewArrowL.setVisibility(View.INVISIBLE);
                        Log.d(LOG_TAG, "Button Left Release");
                        button_is_press = false;
                    }
                }
                return false;
            }
        });

        //Taste UP (HOCH)
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
                        button_is_press = true;
                        new Repeat(0,y, camera).start();
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        imageViewArrowU.setVisibility(View.INVISIBLE);
                        Log.d(LOG_TAG, "Button Up Release");
                        button_is_press = false;
                    }
                }
                return false;
            }
        });

        //Taste DOWN
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
                        button_is_press = true;
                        new Repeat(0,-y, camera).start();
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        imageViewArrowD.setVisibility(View.INVISIBLE);
                        Log.d(LOG_TAG, "Button Down Release");
                        button_is_press = false;
                    }
                }
                return false;
            }
        });

        return rootView;
    }
}


//Sendet in regelmäßigen Abständen die Information, dass die Taste gedrückt gehalten wird
//Dies dient dazu, falls die Verbindung unterbrochen wird, dass dies auf Server-Seite erkannt wird
//und der Servomotor stoppt
class Repeat extends Thread
{
    private final String LOG_TAG = ButtonsFragment.class.getSimpleName();
    ButtonsFragment buttonsFragment = new ButtonsFragment();
    private int x, y;
    private final int interval = 100;
    private Camera camera;


    Repeat(int x, int y, Camera camera)
    {
        this.x = x;
        this.y = y;
        this.camera = camera;
    }

    public void run()
    {
        while(buttonsFragment.button_is_press)
        {
            camera.sendDirection(x, y);
            try
            {
                Thread.sleep(interval);
            }
            catch (Exception e)
            {
                Log.e(LOG_TAG, "Thread error");
            }
        }
    }
}