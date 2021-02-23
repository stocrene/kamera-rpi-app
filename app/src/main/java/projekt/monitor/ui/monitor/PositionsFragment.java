package projekt.monitor.ui.monitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import projekt.monitor.R;
import projekt.monitor.Positions;


public class PositionsFragment extends Fragment
{
    private View rootView;
    private View parentView;
    private ImageButton button_add_position;
    private boolean button_add_position_visible = false;
    private Positions positions = new Positions();

    private List<String> positionsList;
    private ArrayAdapter<String> listAdapter;
    private SwipeMenuListView listView;
    //private String[] listPositions = {"Position 1", "Position 2", "Position 3", "Position 4", "Position 5", "Position 6", "Position 7", "Position 8", "Position 9"};
    private final String LOG_TAG = PositionsFragment.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_positions, container, false);
        parentView = getParentFragment().getView();
        button_add_position = (ImageButton)parentView.findViewById(R.id.imageButton_add_position);
        if(button_add_position.getVisibility() == View.VISIBLE)
        {
            button_add_position.setVisibility(View.INVISIBLE);
            button_add_position_visible = true;
        }

        PositionsFragment asdf = this;

        positionsList = positions.getPositions(getContext());
        if(positionsList != null)
        {
            //sort list alphabetically
            Collections.sort(positionsList, String.CASE_INSENSITIVE_ORDER);

            listAdapter = new ArrayAdapter<>(
                    getContext(),
                    R.layout.list_item_position,
                    R.id.list_item_position_textview,
                    positionsList);

            listView = (SwipeMenuListView) rootView.findViewById(R.id.listView_positions);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id)
                {
                    String position = (String)adapterView.getItemAtPosition(pos);

                }
            });


            SwipeMenuCreator creator = new SwipeMenuCreator()
            {

                @Override
                public void create(SwipeMenu menu)
                {
                    // create "edit" item
                    SwipeMenuItem editItem = new SwipeMenuItem(getContext());
                    // set item background
                    //editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                    //editItem.setBackground(new ColorDrawable(Color.rgb(0x3c, 0x3c, 0x3c)));
                    editItem.setBackground(new ColorDrawable(Color.rgb(0xa4, 0xa9, 0xad)));
                    // set item width
                    editItem.setWidth(dp2px(90));
                    // set item title
                    editItem.setIcon(R.drawable.ic_edit);
                    // add to menu
                    menu.addMenuItem(editItem);

                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                    // set item background
                    //deleteItem.setBackground(new ColorDrawable(Color.rgb(0xff, 0x33, 0x33)));
                    //deleteItem.setBackground(new ColorDrawable(Color.rgb(0xff, 0x5a, 0x5f)));
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xef, 0x5b, 0x5b)));
                    // set item width
                    deleteItem.setWidth(dp2px(90));
                    // set a icon
                    deleteItem.setIcon(R.drawable.ic_delete);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            };
            //set creator
            listView.setMenuCreator(creator);
            listView.setCloseInterpolator(new BounceInterpolator());

            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index)
                {
                    String pos = positionsList.get(position);

                    try
                    {
                        JSONObject object1 = new JSONObject(pos);

                        pos = object1.get("position").toString();
                        int x = Integer.valueOf(object1.get("x").toString());
                        int y = Integer.valueOf(object1.get("y").toString());

                        Log.d(LOG_TAG, "index: " + String.valueOf(index));
                        switch (index)
                        {
                            case 0:
                                //rename
                                Log.d(LOG_TAG, "rename");
                                DialogRenameFragment dialogRenameFragment = new DialogRenameFragment(pos, x, y);
                                dialogRenameFragment.show(getActivity().getSupportFragmentManager(), "renamePosition");
                                updateList(position);
                                break;
                            case 1:
                                //delete
                                Log.d(LOG_TAG, "delete");
                                DialogFragment dialogDeleteFragment = new DialogDeleteFragment(pos, x, y);
                                dialogDeleteFragment.show(getActivity().getSupportFragmentManager(), "deletePosition");
                                positionsList.clear();
                                positionsList = positions.getPositions(getContext());
                                //positionsList.remove(position);
                                listView.setAdapter(listAdapter);
//                            listAdapter.notifyDataSetChanged();
//                            listView.setAdapter(listAdapter);
//                            AlertDialog dialog = dialogDeleteFragment.getDialog();
//                            dialog.show();
//                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(View v)
//                                {
//                                    TextInputLayout textInputLayout = (TextInputLayout) getDialog().findViewById(R.id.outlinedTextField_name);
//                                    String posName = textInputLayout.getEditText().getText().toString();
//                                    if(posName.equals(""))
//                                    {
//                                        Log.d(LOG_TAG, "TextField empty");
//                                        textInputLayout.setErrorEnabled(true);
//                                        textInputLayout.setError(getResources().getString(R.string.error_no_name));
//                                    }
//                                    else
//                                    {
//                                        dialog.dismiss();
//                                        Positions positions = new Positions();
//                                        positions.addPosition(posName, getContext());
//                                        Toast.makeText(getContext(), getResources().getString(R.string.toast_item_added), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                            String message = "Position " + "<b>" + positionsList.get(position) + "</b>" + " wirklich löschen?";
//
//                                public void onClick(DialogInterface dialog, int which)
//                                {
//                                    positions.removePosition(pos, getContext());
//                                    positionsList.remove(position);
//                                    listView.setAdapter(listAdapter);
//                                    listView.setSelection(position-1);
//                                    Toast.makeText(getContext(), getResources().getString(R.string.toast_item_deleted), Toast.LENGTH_SHORT).show();
//                                }});
//                            builder.show();
                                break;
                        }
                     }
                    catch (JSONException e)
                    {
                        Log.e(LOG_TAG, "Failed to create JSONObject", e);
                    }


                    return false;
                }
            });
        }
        else
        {
            rootView.findViewById(R.id.textView_no_positions).setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    public void updateList(int position)
    {
        positionsList = positions.getPositions(getContext());
        if(positionsList != null)
        {
            //sort list alphabetically
            Collections.sort(positionsList, String.CASE_INSENSITIVE_ORDER);
            listAdapter.notifyDataSetChanged();
            listView.invalidateViews();
            listView.setAdapter(listAdapter);
        }
        else
        {
            rootView.findViewById(R.id.textView_no_positions).setVisibility(View.VISIBLE);
        }
    }

    private int dp2px(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(button_add_position_visible)
        {
            button_add_position.setVisibility(View.VISIBLE);
        }
    }
}
