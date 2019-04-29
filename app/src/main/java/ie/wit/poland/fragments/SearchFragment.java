package ie.wit.poland.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.ActionMode;

import com.google.firebase.database.Query;

import ie.wit.poland.R;
import ie.wit.poland.adapters.LandmarkFilter;

public class SearchFragment extends LandmarkFragment
        implements AdapterView.OnItemSelectedListener{

    String selected;
View v;
    SearchView searchView;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

         v = inflater.inflate(R.layout.fragment_search, container, false);
        setListView(v);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.landmarkTypes,
                        android.R.layout.simple_spinner_item);

        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = ((Spinner) v.findViewById(R.id.searchSpinner));
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);


        listView = (ListView) v.findViewById(R.id.searchList); //Bind to the list on our Search layout

        setListView(listView);
        listView.setTextFilterEnabled(true);
        searchView = v.findViewById(R.id.searchView);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    listView.clearTextFilter();
                } else {
                        listView.setFilterText(newText);
                        landmarkFilter.filter(newText);
                }
                return true;
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context c) { super.onAttach(c); }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void checkSelected(String selected)
    {
        if(landmarkFilter == null)
            landmarkFilter = new LandmarkFilter(query,"all",listAdapter,this);


            if (selected !=null)
            if (selected.equals("All Types")) {
                landmarkFilter.setFilter("all");
            } else if (selected.equals("Favourites")) {
                landmarkFilter.setFilter("favourites");
            }
        String filterText = ((SearchView)getActivity()
                .findViewById(R.id.searchView)).getQuery().toString();

        if(filterText.length() > 0)
            landmarkFilter.filter(filterText);
        else
            landmarkFilter.filter("");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected = parent.getItemAtPosition(position).toString();
        checkSelected(selected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void deleteLandmarks(ActionMode actionMode) {
        super.deleteLandmarks(actionMode);
        checkSelected(selected);}
}