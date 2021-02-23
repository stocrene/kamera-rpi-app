package projekt.monitor.ui.monitor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonitorViewModel extends ViewModel
{
    public String ip = "";
    public MutableLiveData<Integer> x, y;

    public MonitorViewModel()
    {
        x = new MutableLiveData<>();
        y = new MutableLiveData<>();
    }

    public void setX(int x)
    {
        this.x.setValue(x);
    }

    public void setY(int y)
    {
        this.y.setValue(y);
    }

    public LiveData<Integer> getX()
    {
        return x;
    }

    public LiveData<Integer> getY()
    {
        return y;
    }
}