package projekt.monitor.ui.monitor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.niqdev.mjpeg.MjpegView;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import androidx.lifecycle.ViewModelProvider;
import projekt.monitor.Camera;
import projekt.monitor.R;


public class InputFragment extends Fragment
{
    private View rootView;
    private View parentView;

    private MjpegView mjpegView;

    private Camera camera;
    private MonitorViewModel monitorViewModel;

    private Slider slider_pan;
    private Slider slider_tild;
    private TextView textView_input_pan_value;
    private TextView textView_input_tild_value;

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
        rootView = inflater.inflate(R.layout.fragment_input, container, false);
        parentView = getParentFragment().getView();

        monitorViewModel = new ViewModelProvider(getParentFragment()).get(MonitorViewModel.class);

        camera = new Camera(monitorViewModel.ip);
        camera.addPositionObserver((MonitorFragment)getParentFragment());

        mjpegView = (MjpegView)parentView.findViewById(R.id.mjpegView);

        slider_pan = (Slider)rootView.findViewById(R.id.slider_pan);
        slider_tild = (Slider)rootView.findViewById(R.id.slider_tild);
        textView_input_pan_value = (TextView)rootView.findViewById(R.id.textView_input_pan_value);
        textView_input_tild_value = (TextView)rootView.findViewById(R.id.textView_input_tild_value);


        if(monitorViewModel.posX != -1)
        {
            slider_pan.setValue(monitorViewModel.posX);
            textView_input_pan_value.setText(String.valueOf(monitorViewModel.posX));
        }
        if(monitorViewModel.posY != -1)
        {
            slider_tild.setValue(monitorViewModel.posY);
            textView_input_tild_value.setText(String.valueOf(monitorViewModel.posY));
        }


        slider_pan.addOnSliderTouchListener(new Slider.OnSliderTouchListener()
        {
            @Override
            public void onStartTrackingTouch(Slider slider) {}

            @Override
            public void onStopTrackingTouch(Slider slider)
            {
                // Send message
                if(mjpegView.isStreaming())
                {
                    camera.setTargetPosition((int)slider.getValue(), (int)slider_tild.getValue());
                }
            }
        });
        slider_pan.addOnChangeListener(new Slider.OnChangeListener()
        {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser)
            {
                textView_input_pan_value.setText(String.valueOf((int)value));
            }
        });


        slider_tild.addOnSliderTouchListener(new Slider.OnSliderTouchListener()
        {
            @Override
            public void onStartTrackingTouch(Slider slider) {}

            @Override
            public void onStopTrackingTouch(Slider slider)
            {
                // Send message
                if(mjpegView.isStreaming())
                {
                    camera.setTargetPosition((int)slider_pan.getValue(), (int)slider.getValue());
                }
            }
        });
        slider_tild.addOnChangeListener(new Slider.OnChangeListener()
        {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser)
            {
                textView_input_tild_value.setText(String.valueOf((int)value));
            }
        });

        return rootView;
    }
}