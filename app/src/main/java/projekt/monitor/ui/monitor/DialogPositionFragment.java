package projekt.monitor.ui.monitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.DialogFragment;
import projekt.monitor.R;
import projekt.monitor.Positions;



public class DialogPositionFragment extends DialogFragment
{
    private final String LOG_TAG = DialogPositionFragment.class.getSimpleName();
    private int x, y;

    public DialogPositionFragment(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_dialog_add_position, null))
                .setCancelable(false)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog)
            {
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
                TextInputLayout textInputLayout = (TextInputLayout) getDialog().findViewById(R.id.outlinedTextField_add_position);
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
                    positions.addPosition(posName, x, y, getContext());
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_item_added), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return dialog;
    }
}

