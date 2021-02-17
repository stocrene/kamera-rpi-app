package projekt.monitor.ui.monitor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import projekt.monitor.R;
import projekt.monitor.Positions;


public class PositionsFragment extends Fragment
{
    private View rootView;
    private View parentView;
    private ImageButton button_add_position;
    private ArrayAdapter<String> listAdapter;
    private Positions positions;
    private String[] listPositions = {"Position 1", "Position 2", "Position 3", "Position 4", "Position 5", "Position 6", "Position 7", "Position 8", "Position 9"};


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
        button_add_position.setVisibility(View.GONE);


        List<String> positionsList = new ArrayList<>(Arrays.asList(listPositions));

        listAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.list_item_position,
                R.id.list_item_position_textview,
                positionsList);

        ListView listView = (ListView) rootView.findViewById(R.id.listView_positions);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id)
            {
                String position = (String)adapterView.getItemAtPosition(pos);

            }
        });




        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        button_add_position.setVisibility(View.VISIBLE);
    }
}