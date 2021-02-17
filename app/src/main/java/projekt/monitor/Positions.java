package projekt.monitor;

import android.content.Context;
import android.content.SharedPreferences;

public class Positions
{
    private static String KEY = "arrayPositions";
    private static String ARRAY_NAME = "positions";

    public boolean addPosition(String position, Context mContext)
    {
        String array[] = getArray(mContext);

    }

    public boolean removePosition(String position, Context mContext)
    {
        String array[] = getArray(mContext);

    }

    public String[] getPositions(Context mContext)
    {
        SharedPreferences prefs = mContext.getSharedPreferences(KEY, 0);
        int size = prefs.getInt(ARRAY_NAME + "_size", 0);
        if(size == 0) {return null;}
        String array[] = new String[size];
        for(int i = 0; i < size; i++)
        {
            array[i] = prefs.getString(ARRAY_NAME + "_" + i, null);
        }
        return array;
    }

    private boolean saveArray(String[] array, Context mContext)
    {
        SharedPreferences prefs = mContext.getSharedPreferences(KEY, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ARRAY_NAME +"_size", array.length);
        for(int i = 0; i < array.length; i++)
        {
            editor.putString(ARRAY_NAME + "_" + i, array[i]);
        }
        return editor.commit();
    }
}
