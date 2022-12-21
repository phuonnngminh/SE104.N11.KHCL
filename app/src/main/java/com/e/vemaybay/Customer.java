package com.e.vemaybay;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Customer extends Fragment {
    //private Button book;
    AdapterBook adapterBook;
    DBHelper DB;
    private RecyclerView listBook;
    private Spinner ListFlight;
    private  EditText Ten;
    private  EditText SDT;
    private EditText CMND;
    String listSanBay[];
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.bookticketmanagement,container,false);
        DB = new DBHelper(v.getContext());
        listBook = v.findViewById(R.id.recycler_listbook);
        ListFlight = v.findViewById(R.id.spin_bookmng_listflight);
        Ten = v.findViewById(R.id.ed_mgn_sel_name);
        CMND = v.findViewById(R.id.ed_mgn_sel_id);
        SDT = v.findViewById(R.id.ed_mgn_sel_sdt);
        Cursor c = DB.getallFlight();
        listSanBay = new String[c.getCount()];
        int i = 0;
        while (c.moveToNext())
        {
            listSanBay[i] = c.getString(0);
            i++;
        }
        c.close();
        Ten.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    refresh();
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        CMND.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    refresh();
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    refresh();
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ArrayAdapter<String> sb = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1,listSanBay);
        ListFlight.setAdapter(sb);
        ListFlight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refresh();
                Ten.setText("");
                CMND.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }
    public void refresh()
    {
        adapterBook = new AdapterBook(getContext(), DB, ListFlight.getSelectedItem().toString(), this, Ten.getText().toString(), CMND.getText().toString(), SDT.getText().toString());
        listBook.setAdapter(adapterBook);
        listBook.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
