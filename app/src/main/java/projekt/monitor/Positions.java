package projekt.monitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import projekt.monitor.ui.monitor.MonitorFragment;

public class Positions
{
    private final String KEY = "listPositions";
    private final String LIST_NAME = "positions";
    private final String LOG_TAG = Positions.class.getSimpleName();

    public void addPosition(String position, int x, int y, Context mContext)
    {
        String pos = toJSON(position, x, y);

        List<String> positionsList = new ArrayList<String>();
        if(getPositions(mContext) != null)
        {
            positionsList = getPositions(mContext);
        }
        positionsList.add(pos);
        saveList(positionsList, mContext);
    }

    public void removePosition(String position, int x, int y, Context mContext)
    {
        String pos = toJSON(position, x, y);

        List<String> positionsList = new ArrayList<String>();
        if(getPositions(mContext) != null)
        {
            positionsList = getPositions(mContext);
        }
        positionsList.remove(positionsList.indexOf(pos));
        saveList(positionsList, mContext);
    }

    public List<String> getPositions(Context mContext)
    {
        SharedPreferences prefs = mContext.getSharedPreferences(KEY, 0);
        int size = prefs.getInt(LIST_NAME + "_size", 0);
        if(size == 0) {return null;}
        List<String> positionsList = new ArrayList<String>();
        for(int i = 0; i < size; i++)
        {
            positionsList.add(prefs.getString(LIST_NAME + "_" + i, null));
        }
        return positionsList;
    }

    private void saveList(List<String> positionsList, Context mContext)
    {
        SharedPreferences prefs = mContext.getSharedPreferences(KEY, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(LIST_NAME +"_size", positionsList.size());
        for(int i = 0; i < positionsList.size(); i++)
        {
            editor.putString(LIST_NAME + "_" + i, positionsList.get(i));
        }
        editor.commit();
    }


    private String toJSON(String position, int x, int y)
    {
        final JSONObject object = new JSONObject();

        try
        {
            //Erstelle die JSON-Datei
            object.put("position", position);
            object.put("x", x);
            object.put("y", y);
        }
        catch(JSONException e)
        {
            Log.e(LOG_TAG, "Failed to create JSONObject", e);
        }
        return object.toString();
    }
}
