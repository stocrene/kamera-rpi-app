package projekt.monitor;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Positions
{
    private static String KEY = "listPositions";
    private static String LIST_NAME = "positions";

    public void addPosition(String position, Context mContext)
    {
        List<String> positionsList = new ArrayList<String>();
        if(getPositions(mContext) != null)
        {
            positionsList = getPositions(mContext);
        }
        positionsList.add(position);
        saveList(positionsList, mContext);
    }

    public void removePosition(String position, Context mContext)
    {
        List<String> positionsList = new ArrayList<String>();
        if(getPositions(mContext) != null)
        {
            positionsList = getPositions(mContext);
        }
        positionsList.remove(positionsList.indexOf(position));
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
}
