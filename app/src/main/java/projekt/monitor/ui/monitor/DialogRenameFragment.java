package projekt.monitor.ui.monitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import projekt.monitor.Positions;
import projekt.monitor.R;

public class DialogRenameFragment extends DialogFragment
{
    List positionsList;
    ListView listView;
    ArrayAdapter listAdapter;
    private int posIndex;
    private String position;
    private int x, y;
    private TextInputLayout textInputLayout;
    private final String LOG_TAG = DialogRenameFragment.class.getSimpleName();

    public DialogRenameFragment(String position, int x, int y, List positionsList, ListView listView, ArrayAdapter listAdapter, int posIndex)
    {
        this.position = position;
        this.x = x;
        this.y = y;
        this.positionsList = positionsList;
        this.listView = listView;
        this.listAdapter = listAdapter;
        this.posIndex = posIndex;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_dialog_rename_position, null))
                .setCancelable(false)
                .setPositiveButton(R.string.rename, null)
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog)
            {
                textInputLayout = (TextInputLayout) getDialog().findViewById(R.id.outlinedTextField_rename_position);
                textInputLayout.getEditText().setText(position);

                TextView textView_pan_value = (TextView) getDialog().findViewById(R.id.textView_pan_value);
                TextView textView_tild_value = (TextView) getDialog().findViewById(R.id.textView_tild_value);
                textView_pan_value.setText(String.valueOf(x));
                textView_tild_value.setText(String.valueOf(y));
            }
        });
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String posName = textInputLayout.getEditText().getText().toString();
                if(posName.equals(""))
                {
                    Log.d(LOG_TAG, "TextField empty");
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getResources().getString(R.string.error_no_name));
                }
                else
                {
                    dialog.dismiss();

                    Positions positions = new Positions();
                    positions.removePosition(position, x, y, getContext());
                    positions.addPosition(posName, x, y, getContext());

                    updateList(posName);

                    Toast.makeText(getContext(), getResources().getString(R.string.toast_item_renamed), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return dialog;
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

    private void updateList(String posName)
    {
        positionsList.remove(positionsList.get(posIndex));
        positionsList.add(toJSON(posName, x, y));
        Collections.sort(positionsList, String.CASE_INSENSITIVE_ORDER);
        listView.setAdapter(listAdapter);
        listView.setSelection(posIndex-1);
    }
}

