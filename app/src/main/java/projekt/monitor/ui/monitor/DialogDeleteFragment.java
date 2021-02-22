package projekt.monitor.ui.monitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.DialogFragment;
import projekt.monitor.Positions;
import projekt.monitor.R;

public class DialogDeleteFragment extends DialogFragment
{
    private String position;
    private PositionsFragment positionsFragment;
    private final String LOG_TAG = DialogDeleteFragment.class.getSimpleName();

    public DialogDeleteFragment(String position, PositionsFragment positionsFragment)
    {
        this.position = position;
        this.positionsFragment = positionsFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);
        String message = "Position " + "<b>" + position + "</b>" + " wirklich löschen?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_dialog_delete_position, null))
                .setCancelable(false)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Positions positions = new Positions();
                        positions.removePosition(position, getContext());
                        //positionsFragment.updateList(2);
                        Toast.makeText(getContext(), getResources().getString(R.string.toast_item_deleted), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                TextView textView_message = (TextView) getDialog().findViewById(R.id.textView_message);
                textView_message.setText(Html.fromHtml(message));
            }
        });
        return dialog;
    }
}