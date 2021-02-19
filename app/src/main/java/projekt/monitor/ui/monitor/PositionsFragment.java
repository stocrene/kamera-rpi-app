package projekt.monitor.ui.monitor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

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
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import projekt.monitor.R;
import projekt.monitor.Positions;


public class PositionsFragment extends Fragment
{
    private View rootView;
    private View parentView;
    private ImageButton button_add_position;
    private boolean button_add_position_visible = false;
    private ArrayAdapter<String> listAdapter;
    private Positions positions = new Positions();
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


        List<String> positionsList = positions.getPositions(getContext());
        if(positionsList != null)
        {
            //sort list alphabetically
            Collections.sort(positionsList, String.CASE_INSENSITIVE_ORDER);

            listAdapter = new ArrayAdapter<>(
                    getContext(),
                    R.layout.list_item_position,
                    R.id.list_item_position_textview,
                    positionsList);

            SwipeMenuListView listView = (SwipeMenuListView) rootView.findViewById(R.id.listView_positions);
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
                    // create "open" item
                    SwipeMenuItem editItem = new SwipeMenuItem(getContext());
                    // set item background
                    editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                            0xCE)));
                    // set item width
                    editItem.setWidth(dp2px(90));
                    // set item title
                    editItem.setIcon(R.drawable.ic_edit);
                    // add to menu
                    menu.addMenuItem(editItem);

                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xff, 0x33,
                            0x33)));
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
                    switch (index)
                    {
                        case 0:
                            //edit
                            //edit(item);
                            Log.d(LOG_TAG, "edit");
                            break;
                        case 1:
                            //delete
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setCancelable(false);
                            builder.setTitle("Position löschen");
                            String message = "Position " + "<b>" + positionsList.get(position) + "</b>" + " wirklich löschen?";
                            builder.setMessage(Html.fromHtml(message));
                            builder.setNegativeButton("Abbrechen", null);
                            builder.setPositiveButton("Löschen", new AlertDialog.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    positions.removePosition(pos, getContext());
                                    positionsList.remove(position);
                                    listView.setAdapter(listAdapter);
                                    listView.setSelection(position-1);
                                    Toast.makeText(getContext(), "Position gelöscht", Toast.LENGTH_SHORT).show();
                                }});
                            builder.show();
                            break;
                    }
                    return false;
                }
            });
            }
        else
        {
            rootView.findViewById(R.id.textView_no_positions).setVisibility(View.VISIBLE);
        }
        // Inflate the layout for this fragment
        return rootView;
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
