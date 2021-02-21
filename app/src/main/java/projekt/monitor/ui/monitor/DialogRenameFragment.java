package projekt.monitor.ui.monitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.DialogFragment;
import projekt.monitor.Positions;
import projekt.monitor.R;

public class DialogRenameFragment extends DialogFragment
{
    private String position;
    private TextInputLayout textInputLayout;
    private final String LOG_TAG = DialogRenameFragment.class.getSimpleName();

    public DialogRenameFragment(String position)
    {
        this.position = position;
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
        dialog.show();
        textInputLayout = (TextInputLayout) getDialog().findViewById(R.id.outlinedTextField_rename_position);
        //textInputLayout.getEditText().setText(position);
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
                    positions.removePosition(position, getContext());
                    positions.addPosition(posName, getContext());
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_item_renamed), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return dialog;
    }
}

