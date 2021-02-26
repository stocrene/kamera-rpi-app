package projekt.monitor.ui.monitor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.niqdev.mjpeg.MjpegView;

import projekt.monitor.Camera;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import projekt.monitor.R;


public class JoystickFragment extends Fragment
{
    private ImageView imageViewArrowR;
    private ImageView imageViewArrowL;
    private ImageView imageViewArrowU;
    private ImageView imageViewArrowD;
    private MjpegView mjpegView;

    private float x;
    private float y;
    private Camera camera;
    private int interval = 100;

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
        camera = new Camera(monitorViewModel.ip);
        camera.addPositionObserver((MonitorFragment)getParentFragment());

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_joystick, container, false);
        parentView = getParentFragment().getView();

        imageViewArrowR = (ImageView)parentView.findViewById(R.id.imageViewArrowR);
        imageViewArrowL = (ImageView)parentView.findViewById(R.id.imageViewArrowL);
        imageViewArrowU = (ImageView)parentView.findViewById(R.id.imageViewArrowU);
        imageViewArrowD = (ImageView)parentView.findViewById(R.id.imageViewArrowD);
        mjpegView = (MjpegView)parentView.findViewById(R.id.mjpegView);

        JoystickView joystick = (JoystickView)rootView.findViewById(R.id.joystick);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener()
        {
            @Override
            public void onMove(int angle, int strength)
            {
                if(strength > 20)
                {
                    x = (float)Math.cos(Math.toRadians(angle))*strength;
                    y = (float) Math.sin(Math.toRadians(angle))*strength;

                    if (mjpegView.isStreaming())
                    {
                        if(x > 0)
                        {
                            imageViewArrowR.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            imageViewArrowR.setVisibility(View.INVISIBLE);
                        }

                        if (x < 0)
                        {
                            imageViewArrowL.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            imageViewArrowL.setVisibility(View.INVISIBLE);
                        }

                        if(y > 0)
                        {
                            imageViewArrowU.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            imageViewArrowU.setVisibility(View.INVISIBLE);
                        }

                        if (y < 0)
                        {
                            imageViewArrowD.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            imageViewArrowD.setVisibility(View.INVISIBLE);
                        }
                        camera.sendDirection((int)x, (int)y);
                    }
                }
                else
                {
                    imageViewArrowL.setVisibility(View.INVISIBLE);
                    imageViewArrowR.setVisibility(View.INVISIBLE);
                    imageViewArrowU.setVisibility(View.INVISIBLE);
                    imageViewArrowD.setVisibility(View.INVISIBLE);
                }
            }
        }, interval);

        return rootView;
    }
}