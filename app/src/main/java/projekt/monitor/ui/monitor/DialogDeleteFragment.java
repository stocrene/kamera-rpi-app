package projekt.monitor.ui.monitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.fragment.app.DialogFragment;
import projekt.monitor.Positions;
import projekt.monitor.R;

public class DialogDeleteFragment extends DialogFragment
{
    List positionsList;
    ListView listView;
    ArrayAdapter listAdapter;
    private int posIndex;
    private String position;
    private int x, y;
    private final String LOG_TAG = DialogDeleteFragment.class.getSimpleName();

    public DialogDeleteFragment(String position, int x, int y, List positionsList, ListView listView, ArrayAdapter listAdapter, int posIndex)
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
        String message = "Position " + "<b>" + position + "</b>" + " wirklich löschen?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_dialog_delete_position, null))
                .setCancelable(false)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        Positions positions = new Positions();
                        positions.removePosition(position, x, y, getContext());

                        updateList();

                        Toast.makeText(getContext(), getResources().getString(R.string.toast_item_deleted), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog)
            {
                TextView textView_message = (TextView) getDialog().findViewById(R.id.textView_message);
                textView_message.setText(Html.fromHtml(message));
            }
        });
        return dialog;
    }

    private void updateList()
    {
        positionsList.remove(positionsList.get(posIndex));
        listView.setAdapter(listAdapter);
        listView.setSelection(posIndex-1);
    }
}