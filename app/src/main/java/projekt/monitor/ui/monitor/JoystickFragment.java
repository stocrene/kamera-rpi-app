package projekt.monitor.ui.monitor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import projekt.monitor.R;


public class JoystickFragment extends Fragment
{
    private View rootView;

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

                if(strength > 10)
                {
                    if(angle < 20 || angle > 340)
                    {

                    }
                    else if(angle > 20 && angle < 70)
                    {

                    }
                }


            }
        });

        return rootView;
    }
}