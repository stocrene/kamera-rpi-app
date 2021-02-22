package projekt.monitor.ui.monitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.DialogFragment;
import projekt.monitor.Positions;
import projekt.monitor.R;

public class DialogDeleteFragment extends DialogFragment
{
    private String position;
    private final String LOG_TAG = DialogPositionFragment.class.getSimpleName();

    public void DialogRenameFragment(String position)
    {
        this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        setCancelable(false);
        String message = "Position " + "<b>" + position + "</b>" + " wirklich löschen?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_dialog_delete, null))
                .setCancelable(false)
                .setMessage(Html.fromHtml(message))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                Positions positions = new Positions();
                                positions.removePosition(position, getContext());
                                Toast.makeText(getContext(), getResources().getString(R.string.toast_item_deleted), Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }
}