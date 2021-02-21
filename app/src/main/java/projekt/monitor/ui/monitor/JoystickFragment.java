package projekt.monitor.ui.monitor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import projekt.monitor.Camera;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import projekt.monitor.R;


public class JoystickFragment extends Fragment
{
    private float x;
    private float y;
    private View rootView;
    //Statische IP - Muss noch geändert werden, derzeit aber zu Testzwecke vorhanden
    //MonitorViewModel monitorViewModel = new ViewModelProvider(getParentFragment()).get(MonitorViewModel.class);
    //Camera camera = new Camera(monitorViewModel.ip, tcpPort);
    Camera camera = new Camera("192.168.1.15", 10000);
    private final String LOG_TAG = MonitorFragment.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_joystick, container, false);

        JoystickView joystick = (JoystickView)rootView.findViewById(R.id.joystick);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener()
        {
            @Override
            public void onMove(int angle, int strength)
            {
                Log.d(LOG_TAG, "Joystick angle: " + String.valueOf(angle));

                if(strength > 20)
                {
                    x = (float)Math.cos(Math.toRadians(angle))*strength;
                    Log.d(LOG_TAG, "der X Wert ist: " + String.valueOf(x));
                    y = (float) Math.sin(Math.toRadians(angle))*strength;
                    Log.d(LOG_TAG, "der Y Wert ist: " + String.valueOf(y));
                    camera.sendDirection((int)x, (int)y);
                    /*
                    if(angle < 20 || angle > 340)
                    {
                        camera.sendDirection(1,0);
                    }
                    else if(angle > 20 && angle < 70)
                    {
                        camera.sendDirection(1,0);
                    }

                     */
                }


            }
        }, 200);

        return rootView;
    }
}