package com.e.vemaybay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Statistic extends Fragment {
    EditText month;
    EditText month_year;
    EditText year;
    Spinner sel;
    Button monthOK;
    Button yearOK;
    Button graphMonth;
    Button graphYear;
    String listSelect[];
    TextView stt;
    TextView chuyenBay;
    TextView soVe;
    TextView doanhThu;
    TextView doanhThuNam;
    TextView tiLe;
    TextView tiLeNam;
    TextView soChuyenBay;
    TextView thang;
    AdapterStatistic adapterStatistic;
    RecyclerView recyclerView;
    DBHelper DB;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.statistic,container,false);
        month = v.findViewById(R.id.statistic_ed_month);
        month_year = v.findViewById(R.id.statistic_ed_month_year);
        year = v.findViewById(R.id.statistic_ed_year);
        sel = v.findViewById(R.id.statistic_spin_select);
        DB = new DBHelper(getContext());
        monthOK = v.findViewById(R.id.statistic_btn_month_ok);
        yearOK = v.findViewById(R.id.statistic_btn_year_ok);
        stt = v.findViewById(R.id.txt_month_stt);
        chuyenBay = v.findViewById(R.id.txt_month_chuyenbay);
        soVe = v.findViewById(R.id.txt_month_sove);
        doanhThu = v.findViewById(R.id.txt_month_doanhthu);
        doanhThuNam = v.findViewById(R.id.txt_year_doanhthu);
        tiLe = v.findViewById(R.id.txt_month_tile);
        graphMonth = v.findViewById(R.id.statistic_btn_month_graph);
        graphYear = v.findViewById(R.id.statistic_btn_year_graph);
        tiLeNam = v.findViewById(R.id.txt_year_tile);
        soChuyenBay = v.findViewById(R.id.txt_year_sochuyenbay);
        thang = v.findViewById(R.id.txt_year_thang);
        recyclerView = v.findViewById(R.id.recycler_statistic);
        listSelect = new String[2];
        listSelect[0] = "Theo tháng";
        listSelect[1] = "Theo năm";
        ArrayAdapter<String> select = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,listSelect);
        sel.setAdapter(select);
        //refresh();
        yearOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (year.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Thiếu thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    adapterStatistic = new AdapterStatistic(getContext(), DB, sel.getSelectedItem().toString(), month.getText().toString(), year.getText().toString(), month_year.getText().toString());
                    recyclerView.setAdapter(adapterStatistic);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }
        });
        monthOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (month.getText().toString().isEmpty() || month_year.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Thiếu thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Integer.parseInt(month.getText().toString()) > 12 || Integer.parseInt(month.getText().toString()) < 0) {
                        Toast.makeText(getContext(), "Tháng không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        adapterStatistic = new AdapterStatistic(getContext(), DB, sel.getSelectedItem().toString(), month.getText().toString(), year.getText().toString(), month_year.getText().toString());
                        recyclerView.setAdapter(adapterStatistic);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }

            }
        });

        sel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //refresh();
                if (sel.getSelectedItem().toString().equals("Theo tháng"))
                {
                    if (!month.getText().toString().isEmpty() && !month_year.getText().toString().isEmpty())
                    {
                        refresh();
                    }
                    else
                    {
                        adapterStatistic = new AdapterStatistic(getContext(),DB, sel.getSelectedItem().toString(), "0", "0", "0");
                        recyclerView.setAdapter(adapterStatistic);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                    yearOK.setVisibility(View.GONE);
                    year.setVisibility(View.GONE);
                    month.setVisibility(View.VISIBLE);
                    month_year.setVisibility(View.VISIBLE);
                    monthOK.setVisibility(View.VISIBLE);
                    soVe.setVisibility(View.VISIBLE);
                    chuyenBay.setVisibility(View.VISIBLE);
                    thang.setVisibility(View.GONE);
                    soChuyenBay.setVisibility(View.GONE);
                    doanhThu.setVisibility(View.VISIBLE);
                    tiLe.setVisibility(View.VISIBLE);
                    doanhThuNam.setVisibility(View.GONE);
                    tiLeNam.setVisibility(View.GONE);
                    graphMonth.setVisibility(View.VISIBLE);
                    graphYear.setVisibility(View.GONE);
                }
                else
                {
                    if (!year.getText().toString().isEmpty())
                    {
                        refresh();
                    }
                    else
                    {
                        adapterStatistic = new AdapterStatistic(getContext(),DB, sel.getSelectedItem().toString(), "0", "0", "0");
                        recyclerView.setAdapter(adapterStatistic);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                    yearOK.setVisibility(View.VISIBLE);
                    year.setVisibility(View.VISIBLE);
                    month.setVisibility(View.GONE);
                    month_year.setVisibility(View.GONE);
                    monthOK.setVisibility(View.GONE);
                    soVe.setVisibility(View.GONE);
                    chuyenBay.setVisibility(View.GONE);
                    thang.setVisibility(View.VISIBLE);
                    soChuyenBay.setVisibility(View.VISIBLE);
                    doanhThu.setVisibility(View.GONE);
                    tiLe.setVisibility(View.GONE);
                    doanhThuNam.setVisibility(View.VISIBLE);
                    tiLeNam.setVisibility(View.VISIBLE);
                    graphMonth.setVisibility(View.GONE);
                    graphYear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        graphMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(Statistic.this.getContext());
                mydialog.setTitle("Báo cáo tháng");

                final View mView = getLayoutInflater().inflate(R.layout.graphlayout, null);
                Button monthGraph = mView.findViewById(R.id.btn_start_plot);
                final EditText inputMonth = mView.findViewById(R.id.ed_graph_month);
                final EditText inputYear = mView.findViewById(R.id.ed_graph_year);

                monthGraph.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (inputMonth.getText().toString().isEmpty() || inputYear.getText().toString().isEmpty())
                        {
                            Toast.makeText(Statistic.this.getContext(), "Thiếu thông tin", Toast.LENGTH_SHORT).show();
                        }
                        else if ((Integer.parseInt(inputMonth.getText().toString()))<1 || (Integer.parseInt(inputMonth.getText().toString()))>12)
                        {
                            Toast.makeText(Statistic.this.getContext(), "Tháng không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            GraphView graphView = (GraphView) mView.findViewById(R.id.graph);
                            graphView.removeAllSeries();
                            graphView.setBackgroundColor(Color.parseColor("#f2f7ff"));
                            graphView.getViewport().setDrawBorder(true);
                            graphView.getViewport().setBorderColor(Color.parseColor("#000000"));
                            graphView.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
                            graphView.getViewport().setScrollable(true);  // activate horizontal scrolling
                            graphView.getViewport().setScalable(true);  // activate horizontal and vertical zooming and scrolling
                            graphView.getViewport().setScrollableY(true);  // activate vertical scrolling
                            graphView.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
                            graphView.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
                            graphView.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
                            graphView.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
                            graphView.getGridLabelRenderer().setGridColor(Color.BLACK);
                            graphView.getGridLabelRenderer().reloadStyles();
                            graphView.getGridLabelRenderer().setHorizontalAxisTitle("Chuyến bay");
                            graphView.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLACK);
                            graphView.getGridLabelRenderer().setVerticalAxisTitle("Doanh Thu");
                            graphView.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLACK);
                            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                            Float y;
                            final String inmonth;
                            if (Integer.parseInt(inputMonth.getText().toString())<10)
                            {
                                inmonth = "0" + String.valueOf(Integer.parseInt(inputMonth.getText().toString()));
                            }
                            else inmonth = inputMonth.getText().toString();
                            final Cursor cursor = DB.getBCT(inmonth, inputYear.getText().toString());
                            int i=0 ;

                            while (cursor.moveToNext())
                            {
                                y = Float.parseFloat(cursor.getString(4));
                                series.appendData(new DataPoint(i, y), true, cursor.getCount());
                                i++;
                            }
                            cursor.close();
                            series.setColor(Color.parseColor("#e60e0e")); //встановити колір кривої
                            series.setTitle("Curve 1");
                            series.setDrawDataPoints(true);
                            series.setDataPointsRadius(5);
                            series.setThickness(2);
                            graphView.addSeries(series);
                            graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
                            {
                                @Override
                                public String formatLabel(double value, boolean isValueX) {
                                    if (isValueX) {

                                        Cursor cursor1 = DB.getBCT(inmonth, inputYear.getText().toString());
                                        if (cursor1.getCount() > 0) {
                                            cursor1.moveToPosition((int) value);
                                            if ((value - (int) value) == 0) {
                                                try {

                                                    return cursor1.getString(0);
                                                }
                                                catch (Exception e)
                                                {

                                                    System.out.println(e.getMessage());
                                                    return super.formatLabel(value, isValueX);
                                                }
                                            } else return "";
                                        }
                                        else
                                        {
                                            return super.formatLabel(value, isValueX);
                                        }
                                    }
                                    else return super.formatLabel(value, isValueX);
                                }
                            });
                        }
                    }
                });
                if (!month.getText().toString().isEmpty() && !month_year.getText().toString().isEmpty())
                {
                    if ((Integer.parseInt(month.getText().toString())>0) && (Integer.parseInt(month.getText().toString())<13))
                    {
                        inputMonth.setText(month.getText().toString());
                        inputYear.setText(month_year.getText().toString());
                        monthGraph.performClick();
                        monthGraph.setPressed(true);
                    }
                }
                mydialog.setView(mView);
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                mydialog.show();
            }
        });
        graphYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(Statistic.this.getContext());
                mydialog.setTitle("Báo cáo năm");
                final View mView = getLayoutInflater().inflate(R.layout.graphyear, null);
                final EditText inputNam = mView.findViewById(R.id.ed_graphyear_year);
                Button statPlot = mView.findViewById(R.id.btn_year_start_plot);
                statPlot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (inputNam.getText().toString().isEmpty())
                        {
                            Toast.makeText(Statistic.this.getContext(),"Thiếu thông tin", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            GraphView graphView = (GraphView) mView.findViewById(R.id.graph_year);
                            graphView.removeAllSeries();
                            graphView.setBackgroundColor(Color.parseColor("#f2f7ff"));
                            graphView.getViewport().setDrawBorder(true);
                            graphView.getViewport().setBorderColor(Color.parseColor("#000000"));
                            graphView.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
                            graphView.getViewport().setScrollable(true);  // activate horizontal scrolling
                            graphView.getViewport().setScalable(true);  // activate horizontal and vertical zooming and scrolling
                            graphView.getViewport().setScrollableY(true);  // activate vertical scrolling
                            graphView.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
                            graphView.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
                            graphView.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
                            graphView.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
                            graphView.getGridLabelRenderer().setGridColor(Color.BLACK);
                            graphView.getGridLabelRenderer().reloadStyles();
                            graphView.getGridLabelRenderer().setHorizontalAxisTitle("Tháng");
                            graphView.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLACK);
                            graphView.getGridLabelRenderer().setVerticalAxisTitle("Doanh Thu");
                            graphView.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLACK);
                            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                            int i=1;
                            while (i<=12)
                            {
                                String inputthang;
                                if (i<10)
                                {
                                    inputthang = "0"+String.valueOf(i);
                                }
                                else
                                {
                                    inputthang = String.valueOf(i);
                                }
                                Cursor cursor = DB.getBCN(inputthang, inputNam.getText().toString());
                                if (cursor.getCount() == 0)
                                {
                                    series.appendData(new DataPoint(i, 0), true, 12);
                                }
                                else
                                {
                                    while (cursor.moveToNext()) {
                                        series.appendData(new DataPoint(i, Double.parseDouble(cursor.getString(3))), true, 12);
                                    }
                                    cursor.close();
                                }
                                i++;
                            }
                            series.setColor(Color.parseColor("#e60e0e")); //встановити колір кривої
                            series.setTitle("Curve 1");
                            series.setDrawDataPoints(true);
                            series.setDataPointsRadius(10);
                            series.setThickness(2);
                            graphView.addSeries(series);
                        }
                    }
                });
                if (!year.getText().toString().isEmpty())
                {
                        inputNam.setText(year.getText().toString());
                        statPlot.performClick();
                        statPlot.setPressed(true);
                }
                mydialog.setView(mView);
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                mydialog.show();
            }
        });

        return v;
    }
    public void refresh()
    {
        adapterStatistic = new AdapterStatistic(getContext(),DB, sel.getSelectedItem().toString(), month.getText().toString(), year.getText().toString(), month_year.getText().toString());
        recyclerView.setAdapter(adapterStatistic);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
