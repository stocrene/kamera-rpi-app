package projekt.monitor.ui.monitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.DialogFragment;
import projekt.monitor.R;
import projekt.monitor.Positions;



public class DialogLocationFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        setCancelable(false);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_dialog_add_location, null))
                .setCancelable(false)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        TextInputLayout textInputLayout = (TextInputLayout) getDialog().findViewById(R.id.outlinedTextField_name);
                        String posName = textInputLayout.getEditText().getText().toString();
                        if(posName == "")
                        {
                            textInputLayout.setErrorEnabled(true);
                            textInputLayout.setError(getResources().getString(R.string.error_no_name));
                        }
                        else
                        {
                            Positions positions = new Positions();
                            positions.addPosition(posName, getContext());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

