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

public class DialogDeleteFragment extends DialogFragment
{
    private final String LOG_TAG = DialogPositionFragment.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        setCancelable(false);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_dialog_delete, null))
                .setCancelable(false)
                .setPositiveButton(R.string.delete, null)
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }
}