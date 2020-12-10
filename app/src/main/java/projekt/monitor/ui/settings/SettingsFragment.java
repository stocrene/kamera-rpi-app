package projekt.monitor.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import androidx.navigation.Navigation;
import projekt.monitor.MainActivity;
import projekt.monitor.R;

public class SettingsFragment extends Fragment
{
    private static String ip = "";
    private static String port = "";
    private static String username = "";
    private static String passwort = "";

    private EditText editText_ip;
    private EditText editText_port;
    private EditText editText_username;
    private EditText editText_passwort;
    private Button button;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private View root;
    private SettingsViewModel settingsViewModel;

    private final String LOG_TAG = SettingsFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        pref = PreferenceManager.getDefaultSharedPreferences(root.getContext());
        editor = pref.edit();

        editText_ip       = (EditText)root.findViewById(R.id.editText_ip);
        editText_port     = (EditText)root.findViewById(R.id.editText_port);
        editText_username = (EditText)root.findViewById(R.id.editText_username);
        editText_passwort = (EditText)root.findViewById(R.id.editText_password);
        button = (Button)root.findViewById(R.id.button);

        /*editText_ip.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                button.setVisibility(View.VISIBLE);
            }
        });

        editText_port.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                button.setVisibility(View.VISIBLE);
            }
        });

        editText_username.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                button.setVisibility(View.VISIBLE);
            }
        });

        editText_passwort.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                button.setVisibility(View.VISIBLE);
            }
        });*/

        ip       = pref.getString("ip", "");
        port     = pref.getString("port", "");
        username = pref.getString("username", "");
        passwort = pref.getString("passwort", "");

        editText_ip.setText(ip);
        editText_port.setText(port);
        editText_username.setText(username);
        editText_passwort.setText(passwort);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ip       = editText_ip.getText().toString();
                port     = editText_port.getText().toString();
                username = editText_username.getText().toString();
                passwort = editText_passwort.getText().toString();

                Log.d(LOG_TAG, ip);

                if(!port.equals("") && !ip.equals("") && !username.equals("") && !passwort.equals(""))
                {
                    editor.putString("ip", ip);
                    editor.putString("port", port);
                    editor.putString("username", username);
                    editor.putString("passwort", passwort);

                    editor.commit();

                    Navigation.findNavController(root).navigate(R.id.action_nav_settings_to_nav_monitor2);
                }
                else
                {
                    Toast.makeText(root.getContext(), "Nicht alle Felder ausgefüllt!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}