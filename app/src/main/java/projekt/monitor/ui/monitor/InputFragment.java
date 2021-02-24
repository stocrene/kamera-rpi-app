package projekt.monitor.ui.monitor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import androidx.lifecycle.ViewModelProvider;
import projekt.monitor.Camera;
import projekt.monitor.R;


public class InputFragment extends Fragment
{
    private View rootView;

    private Camera camera;
    private MonitorViewModel monitorViewModel;

    private Slider slider_pan;
    private Slider slider_tild;
    private TextInputLayout outlinedTextField_pan;
    private TextInputLayout outlinedTextField_tild;

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

        monitorViewModel = new ViewModelProvider(getParentFragment()).get(MonitorViewModel.class);

        camera = new Camera(monitorViewModel.ip);
        camera.addPositionObserver((MonitorFragment)getParentFragment());

         slider_pan = (Slider)rootView.findViewById(R.id.slider_pan);
         slider_tild = (Slider)rootView.findViewById(R.id.slider_tild);
         outlinedTextField_pan = (TextInputLayout)rootView.findViewById(R.id.outlinedTextField_pan);
         outlinedTextField_tild = (TextInputLayout)rootView.findViewById(R.id.outlinedTextField_tild);

         if(monitorViewModel.posX != -1)
         {
             slider_pan.setValue(monitorViewModel.posX);
         }
         if(monitorViewModel.posY != -1)
         {
             slider_tild.setValue(monitorViewModel.posY);
         }

//         outlinedTextField_pan.addOnEditTextAttachedListener
//        {
//
//        }

         slider_pan.addOnSliderTouchListener(new Slider.OnSliderTouchListener()
         {
             @Override
             public void onStartTrackingTouch(Slider slider) {}

             @Override
             public void onStopTrackingTouch(Slider slider)
             {
                 // Send message
                 camera.setTargetPosition((int)slider.getValue(), (int)slider_tild.getValue());

             }
         });
         slider_pan.addOnChangeListener(new Slider.OnChangeListener()
         {
             @Override
             public void onValueChange(Slider slider, float value, boolean fromUser)
             {
                 outlinedTextField_pan.getEditText().setText(String.valueOf((int)value));
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
                camera.setTargetPosition((int)slider_pan.getValue(), (int)slider.getValue());

            }
        });

        slider_tild.addOnChangeListener(new Slider.OnChangeListener()
        {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser)
            {
                outlinedTextField_tild.getEditText().setText(String.valueOf((int)value));
            }
        });





        return rootView;
    }
}