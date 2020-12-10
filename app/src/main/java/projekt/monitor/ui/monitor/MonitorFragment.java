package projekt.monitor.ui.monitor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.os.AsyncTask;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegView;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import projekt.monitor.R;
import rx.Subscription;

public class MonitorFragment extends Fragment
{
    private static String ip = "";
    private static String port = "";
    private static String username = "";
    private static String passwort = "";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private MjpegView mjpegView;

    private MonitorViewModel monitorViewModel;

    private ImageButton imageButtonL;
    private ImageButton imageButtonR;

    private final String LOG_TAG = MonitorFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        monitorViewModel = ViewModelProviders.of(this).get(MonitorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_monitor, container, false);

        pref = PreferenceManager.getDefaultSharedPreferences(root.getContext());
        editor = pref.edit();

        ip       = pref.getString("ip", "");
        port     = pref.getString("port", "");
        username = pref.getString("username", "");
        passwort = pref.getString("passwort", "");


        //String vidAddress = "http://192.168.178.42/testvideo.mp4";
        String vidAddress = "http://192.168.178.34:8081";
        //String vidAddress = "https://archive.org/download/ace_200911_04/00130d67f8ea0c43de91d30cdc13386c.mts-mp430-272.mp4";
        //String vidAddress = "https://msfs-cdn.azureedge.net/wp-content/uploads/2020/07/Icon-5-Back-Country-Mtn-Lake.mp4?_=1";

        mjpegView = (MjpegView)root.findViewById(R.id.mjpegView);
/*
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        vidView.start();
*/
        //mjpegView.setCustomBackgroundColor(Color.WHITE);
        /*monitorViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {
                textView.setText(s);
            }
        });*/
        imageButtonL = (ImageButton)root.findViewById(R.id.imageButtonL);
        imageButtonR = (ImageButton)root.findViewById(R.id.imageButtonR);

        imageButtonL.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View w)
            {
                Log.d(LOG_TAG, "Button Links");
                Log.d(LOG_TAG, "rotatCamera(links)");
                rotateCamera("links");
            }
        });

        imageButtonR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View w)
            {
                Log.d(LOG_TAG, "Button Rechts");
                Log.d(LOG_TAG, "rotatCamera(rechts)");
                rotateCamera("rechts");
            }
        });

        return root;
    }



    /*public void sshBefehl(final String ip, final int port, final String username, final String passwort)
    {
        final String befehl = "echo " + passwort + " | sudo -S python Schrittmotor.py g 360 links 2";

        new AsyncTask()
        {
            @Override
            protected boolean doInBackground(String... params)
            {
                try
                {
                   sshVerbindung(ip, port, username, passwort, befehl);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                return true;
            }

            protected void onPostExecute(boolean ausgefuehrt){}

        }.execute("");
    }*/


    public void rotateCamera(String direction)
    {
        final String command = "sudo -S python Schrittmotor.py g 360 " + direction + " 2";
        Log.d(LOG_TAG, "command: " + command);

        SshConnection sshConnection = new SshConnection();
        Log.d(LOG_TAG, "sshConnection.execute(command)");
        sshConnection.execute(command);
    }


    public class SshConnection extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(String... command)
        {
            try
            {
                Log.d(LOG_TAG, "doInBackground: sshCommand()");
                Log.d(LOG_TAG, "command: " + command[0]);
                sshCommand(command[0]);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return true;
        }
    }


    public static boolean sshCommand(String command) throws Exception
    {
        JSch jsch = new JSch();

        byte[] buffer = new byte[1024];
        ArrayList lines = new ArrayList<>();

        Session session = jsch.getSession(username, ip, Integer.parseInt(port));
        session.setPassword(passwort);

        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        ChannelExec channelssh = (ChannelExec)session.openChannel("exec");
        channelssh.setCommand(command);
        channelssh.connect();

        try
        {
            InputStream in = channelssh.getInputStream();
            String line = "";

            while(true)
            {
                while(in.available() > 0)
                {
                    int i = in.read(buffer, 0, 1024);

                    if(i < 0)
                        break;

                    line = new String(buffer, 0, i);
                    lines.add(line);
                }

                if(line.contains("logout"))
                    break;

                if(channelssh.isClosed())
                    break;
            }
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e){}
        }
        catch (Exception ee){}

        channelssh.disconnect();
        session.disconnect();

                        /*while(!lines.isEmpty())
                        {
                            Log.d(LOG_TAG, lines.get(0));
                            lines.remove(0);
                        }*/
        return true;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        int TIMEOUT = 3;
        Mjpeg.newInstance()
                .open("http://" + ip + ":8081/", TIMEOUT)
                .subscribe(inputStream -> {
                            mjpegView.setSource(inputStream);
                            mjpegView.setDisplayMode(DisplayMode.BEST_FIT);
                            mjpegView.showFps(false);
                        },
                        throwable -> {
                            Log.e(LOG_TAG, "mjpeg error", throwable);
                        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mjpegView.stopPlayback();
    }
}