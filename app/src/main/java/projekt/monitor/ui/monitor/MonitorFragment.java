package projekt.monitor.ui.monitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionInflater;
import projekt.monitor.Camera;
import projekt.monitor.MainViewModel;

import projekt.monitor.R;

public class MonitorFragment extends Fragment
{
    private static String ip = "";
    private static String username = "";
    private static String password = "";

    private Camera camera;

    private final int delta = 2;
    private final int xMin = 0+delta;
    private final int xMax = 180-delta;
    private final int yMin = 0+delta;
    private final int yMax = 90-delta;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private MjpegView mjpegView;
    private View rootView;

    private MainViewModel mainViewModel;
    private MonitorViewModel monitorViewModel;

    private ProgressBar loading_spinner;
    private ImageButton imageButtonNoConnection;
    private ImageView imageView_smartphone;
    private ImageView imageView_cross;
    private ImageView imageView_camera;
    private ImageView imageView_line_left;
    private ImageView imageView_line_right;
    private ImageView imageViewArrowR;
    private ImageView imageViewArrowL;
    private ImageView imageViewArrowU;
    private ImageView imageViewArrowD;
    private ImageView imageView_info;
    private ImageButton imageButtonAddLocation;
    private View divider;

    private TextView textView_tild;
    private TextView textView_pan;
    private TextView textView_tild_value;
    private TextView textView_pan_value;
    private TextView textView_camera_not_connected;

    private Class[] fragments = {
            ButtonsFragment.class,
            JoystickFragment.class,
            InputFragment.class,
            PositionsFragment.class
    };

    private int[] navIcons = {
            R.drawable.ic_tab_1_inactive,
            R.drawable.ic_tab_2_inactive,
            R.drawable.ic_tab_3_inactive,
            R.drawable.ic_tab_4_inactive
    };
    private int[] navLabels = {
            R.string.nav_buttons,
            R.string.nav_joystick,
            R.string.nav_input,
            R.string.nav_positions
    };
    private int[] navIconsActive = {
            R.drawable.ic_tab_1_active,
            R.drawable.ic_tab_2_active,
            R.drawable.ic_tab_3_active,
            R.drawable.ic_tab_4_active
    };

    private final String LOG_TAG = MonitorFragment.class.getSimpleName();
    private final int TEXT_SIZE = 17;
    private final int MOTION_PORT = 8081;
    private final int TIMEOUT = 5;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));
        Log.d(LOG_TAG, "onCreate()");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(LOG_TAG, "onCreateView()");
        rootView = inflater.inflate(R.layout.fragment_monitor, container, false);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        monitorViewModel = new ViewModelProvider(this).get(MonitorViewModel.class);

        pref = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        editor = pref.edit();
        ip       = pref.getString("ip", "");
        username = pref.getString("username", "");
        password = pref.getString("passwort", "");

        monitorViewModel.ip = ip;

        camera = new Camera(ip);
        camera.addPositionObserver(this);
        camera.requestPosition(1);

        if(savedInstanceState == null)
        {
            getChildFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, fragments[mainViewModel.tab_active], null)
                    .commit();
        }

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        for(int i = 0; i < tabLayout.getTabCount(); i++)
        {
            LinearLayout tab = (LinearLayout) this.getLayoutInflater().from(getContext()).inflate(R.layout.nav_tab, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);

            tab_label.setText(getResources().getString(navLabels[i]));
            if(i == mainViewModel.tab_active)
            {
                tab_label.setTextColor(getResources().getColor(R.color.tablayout_color_active));
                tab_icon.setImageResource(navIconsActive[i]);
                tabLayout.getTabAt(i).select();
            }
            else
            {
                tab_icon.setImageResource(navIcons[i]);
            }
            tabLayout.getTabAt(i).setCustomView(tab);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab)
                    {
                        getChildFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment_container_view, fragments[tab.getPosition()], null)
                                .commit();

                        View tabView = tab.getCustomView();

                        TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                        ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);

                        tab_label.setTextColor(getResources().getColor(R.color.tablayout_color_active));
                        tab_icon.setImageResource(navIconsActive[tab.getPosition()]);
                        mainViewModel.tab_active = tab.getPosition();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab)
                    {
                        View tabView = tab.getCustomView();
                        TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                        ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);

                        tab_label.setTextColor(getResources().getColor(R.color.tablayout_color_inactive));
                        tab_icon.setImageResource(navIcons[tab.getPosition()]);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {}
                }
        );



        loading_spinner = (ProgressBar) rootView.findViewById(R.id.loading_spinner);
        mjpegView = (MjpegView) rootView.findViewById(R.id.mjpegView);
        imageButtonNoConnection = (ImageButton) rootView.findViewById(R.id.imageButtonNoConnection);
        imageButtonAddLocation = (ImageButton) rootView.findViewById(R.id.imageButton_add_position);
        imageView_smartphone = (ImageView) rootView.findViewById(R.id.imageView_smartphone);
        imageView_cross = (ImageView) rootView.findViewById(R.id.imageView_cross);
        imageView_camera = (ImageView) rootView.findViewById(R.id.imageView_camera);
        imageView_line_left = (ImageView) rootView.findViewById(R.id.imageView_line_left);
        imageView_line_right = (ImageView) rootView.findViewById(R.id.imageView_line_right);
        imageViewArrowR = (ImageView) rootView.findViewById(R.id.imageViewArrowR);
        imageViewArrowL = (ImageView) rootView.findViewById(R.id.imageViewArrowL);
        imageViewArrowU = (ImageView) rootView.findViewById(R.id.imageViewArrowU);
        imageViewArrowD = (ImageView) rootView.findViewById(R.id.imageViewArrowD);
        imageView_info = (ImageView) rootView.findViewById(R.id.imageView_info);
        textView_tild = (TextView) rootView.findViewById(R.id.textView_tild);
        textView_pan = (TextView) rootView.findViewById(R.id.textView_pan);
        textView_tild_value = (TextView) rootView.findViewById(R.id.textView_tild_value);
        textView_pan_value = (TextView) rootView.findViewById(R.id.textView_pan_value);
        textView_camera_not_connected = (TextView) rootView.findViewById(R.id.textView_camera_not_connected);
        divider = (View) rootView.findViewById(R.id.divider);

        DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowR.getDrawable()).mutate(), Color.WHITE);
        DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowL.getDrawable()).mutate(), Color.WHITE);
        DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowU.getDrawable()).mutate(), Color.WHITE);
        DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowD.getDrawable()).mutate(), Color.WHITE);

        imageButtonNoConnection.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View w)
            {
                makeAlertDialog();
            }
        });

        imageButtonAddLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View w)
            {
                DialogPositionFragment dialogPositionFragment = new DialogPositionFragment(monitorViewModel.posX, monitorViewModel.posY);
                dialogPositionFragment.show(getActivity().getSupportFragmentManager(), "addPosition");
            }
        });

        return rootView;
    }



    public void updatePosition(int x, int y)
    {
        Log.d(LOG_TAG, "Position: " + String.valueOf(x) + " " + String.valueOf(y));

        monitorViewModel.posX = x;
        monitorViewModel.posY = y;

        String spaceX = "";
        String spaceY = " ";
        if(x < 10)
        {
            spaceX = "  ";
        }
        else if (x < 100)
        {
            spaceX = " ";
        }

        if(y < 10)
        {
            spaceY = "  ";
        }

        String panVal = spaceX + x;
        String tildVal = spaceY + y;
        ((Activity) getContext()).runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                textView_pan_value.setText(panVal);
                textView_tild_value.setText(tildVal);
                if(x >= xMax)
                {
                    DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowR.getDrawable()).mutate(), getResources().getColor(R.color.arrow_red));
                }
                else
                {
                    DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowR.getDrawable()).mutate(), Color.WHITE);
                }

                if(x <= xMin)
                {
                    DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowL.getDrawable()).mutate(), getResources().getColor(R.color.arrow_red));
                }
                else
                {
                    DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowL.getDrawable()).mutate(), Color.WHITE);
                }

                if(y >= yMax)
                {
                    DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowU.getDrawable()).mutate(), getResources().getColor(R.color.arrow_red));
                }
                else
                {
                    DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowU.getDrawable()).mutate(), Color.WHITE);
                }

                if(y <= yMin)
                {
                    DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowD.getDrawable()).mutate(), getResources().getColor(R.color.arrow_red));
                }
                else
                {
                    DrawableCompat.setTint(DrawableCompat.wrap(imageViewArrowD.getDrawable()).mutate(), Color.WHITE);
                }
            }
        });
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
    }


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

                        *//*while(!lines.isEmpty())
                        {
                            Log.d(LOG_TAG, lines.get(0));
                            lines.remove(0);
                        }*//*
        return true;
    }*/


    void makeAlertDialog()
    {
        AlertDialog alert = new AlertDialog.Builder(getContext())
                //.setTitle("Keine Verbindung")
                .setMessage(R.string.dialog_no_connection)
            .setPositiveButton(R.string.dialog_positive_button, null)
                //.setCancelable(false)
            //.setIcon(R.drawable.ic_dialog_alert)
            .create();
        alert.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog)
            {
                Button buttonPositive = alert.getButton(Dialog.BUTTON_POSITIVE);
                buttonPositive.setTextSize(TEXT_SIZE);
                //btnPositive.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });
        alert.show();
    }


    @Override
    public void onStart()
    {
        Log.d(LOG_TAG, "onStart()");
        super.onStart();
        Mjpeg.newInstance()
                .credential(username, password)
                .open("http://" + ip + ":" + MOTION_PORT + "/", TIMEOUT)
                .subscribe(inputStream -> {
                            monitorViewModel.stream_conntected = true;
                            mjpegView.setSource(inputStream);
                            mjpegView.setDisplayMode(DisplayMode.BEST_FIT);
                            mjpegView.showFps(false);
                            mjpegView.getSurfaceView().setVisibility(View.VISIBLE);
                            Log.d(LOG_TAG, "mjpegStream()");
                            loading_spinner.setVisibility(View.INVISIBLE);
                            textView_tild.setVisibility(View.VISIBLE);
                            textView_pan.setVisibility(View.VISIBLE);
                            textView_tild_value.setVisibility(View.VISIBLE);
                            textView_pan_value.setVisibility(View.VISIBLE);
                            divider.setVisibility(View.VISIBLE);
                            if(mainViewModel.tab_active != 3)
                            {
                                imageButtonAddLocation.setVisibility(View.VISIBLE);
                            }
                        },
                        throwable -> {
                            Log.e(LOG_TAG, "mjpeg error", throwable);
                            loading_spinner.setVisibility(View.INVISIBLE);
                            imageButtonNoConnection.setVisibility(View.VISIBLE);
                            imageView_smartphone.setVisibility(View.VISIBLE);
                            imageView_cross.setVisibility(View.VISIBLE);
                            imageView_camera.setVisibility(View.VISIBLE);
                            imageView_line_left.setVisibility(View.VISIBLE);
                            imageView_line_right.setVisibility(View.VISIBLE);
                            textView_camera_not_connected.setVisibility(View.VISIBLE);
                            imageView_info.setVisibility(View.VISIBLE);
                        });
    }


    @Override
    public void onStop()
    {
        super.onStop();
        mjpegView.stopPlayback();
    }
}









