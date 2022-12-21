package com.e.vemaybay;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ListFlight extends Fragment {
    AdapterListFlight adapterListFlight;
    DBHelper DB;
    Spinner SanBayDi;
    Spinner SanBayDen;
    EditText startDay;
    Button del;

    String listSanBayDi[], listSanBayDen[];
    private RecyclerView listFlight;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=  inflater.inflate(R.layout.listflight,container,false);
        DB = new DBHelper(getContext());
        SanBayDi = v.findViewById(R.id.listflight_spin_sbdi);
        SanBayDen = v.findViewById(R.id.listflight_spin_sbden);
        startDay = v.findViewById(R.id.listflight_ed_start);
        listFlight = v.findViewById(R.id.recycler_listflight);
        del = v.findViewById(R.id.listflight_btn_delete);
        Cursor c = DB.getalldataairport();
        listSanBayDi = new String[c.getCount()+1];
        listSanBayDen = new String[c.getCount()+1];

        int i=1;
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDay.setText("");
                SanBayDen.setSelection(0);
                SanBayDi.setSelection(0);
            }
        });
        listSanBayDi[0] = "Không chọn";
        listSanBayDen[0] = "Không chọn";
        while (c.moveToNext())
        {
            listSanBayDi[i] = c.getString(1)+ " ("+c.getString(2)+", "+c.getString(3)+")";
            listSanBayDen[i] = c.getString(1)+ " ("+c.getString(2)+", "+c.getString(3)+")";
            i++;
        }
        c.close();
        ArrayAdapter<String> sbdi = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listSanBayDi);
        SanBayDi.setAdapter(sbdi);
        ArrayAdapter<String> sbden = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listSanBayDen);
        SanBayDen.setAdapter(sbden);
        SanBayDi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (SanBayDi.getSelectedItem().toString().equals(SanBayDen.getSelectedItem().toString()) && !SanBayDi.getSelectedItem().toString().equals("Không chọn"))
                {

                    if (SanBayDi.getSelectedItemPosition() == SanBayDi.getCount()-1) {
                        SanBayDi.setSelection(1);

                    }
                    else
                    {
                        SanBayDi.setSelection(SanBayDi.getSelectedItemPosition()+1);


                    }
                }
                else
                {

                }
                refresh();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SanBayDen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (SanBayDi.getSelectedItem().toString().equals(SanBayDen.getSelectedItem().toString()) && !SanBayDen.getSelectedItem().toString().equals("Không chọn"))
                {
                    if (SanBayDen.getSelectedItemPosition() == SanBayDen.getCount()-1) {
                        SanBayDen.setSelection(1);

                    }
                    else
                    {

                        SanBayDen.setSelection(SanBayDen.getSelectedItemPosition()+1);

                    }

                }
                else
                {

                }
                refresh();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        startDay.setInputType(InputType.TYPE_NULL);
        startDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(startDay);
            }
        });
        adapterListFlight = new AdapterListFlight(getContext(), DB, SanBayDi.getSelectedItem().toString(), SanBayDen.getSelectedItem().toString(), startDay.getText().toString(), ListFlight.this);
        listFlight.setAdapter(adapterListFlight);
        listFlight.setLayoutManager(new LinearLayoutManager(getContext()));
        startDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refresh();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }
    public void showDateTimeDialog(final EditText date_time_in)
    {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfmonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
                date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void refresh()
    {
        String split5[] = SanBayDi.getSelectedItem().toString().split(" \\(");
        String split6[] = SanBayDen.getSelectedItem().toString().split(" \\(");
        adapterListFlight = new AdapterListFlight(getContext(), DB, split5[0], split6[0], startDay.getText().toString(), ListFlight.this);
        listFlight.setAdapter(adapterListFlight);
        listFlight.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}
