package projekt.monitor.ui.monitor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonitorViewModel extends ViewModel
{
    public String ip = "";
    public boolean stream_conntected = false;

    public int posX = -1;
    public int posY = -1;
}