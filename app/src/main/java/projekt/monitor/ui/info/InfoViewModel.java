package projekt.monitor.ui.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    String text = "Diese App dient zur Steuerung und Anzeige des Livestreams der Kameraeinheit auf Raspberry Pi Basis. \n\n\n\n\n\nProjektarbeit von\n\n\nAndreas Kraus\n\nChristian Geyer\n\nRene Stöcklein";

    public InfoViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue(text);
    }

    public LiveData<String> getText()
    {
        return mText;
    }
}