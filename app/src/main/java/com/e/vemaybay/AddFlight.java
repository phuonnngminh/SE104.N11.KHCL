package com.e.vemaybay;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddFlight extends Fragment {
    EditText MaChuyenBay;
    EditText GiaVe;
    EditText NgayGio;
    EditText ThoiGianBay;
    Spinner SanBayDi;
    Spinner SanBayDen;
    Spinner ListTB;
    DBHelper DB;
    Button Add;
    Button AddInfo;
    Button AddInfoInter;
    int next;
    AdapterListHangVe adapterListHangVe;
    SwipeRefreshLayout swipeRefreshLayout;
    AdapterIntermidiate adapterIntermidiate;
    String listSanBayDi[], listSanBayDen[], listTuyenBay[];
    int vtDi;
    int vtDen;
    String tenDi;
    String tenDen;
    String maTuyenBay;
    TextView quitUpdate;
    private int soon;
    private int maxInter;
    private int minDelay;
    private int maxDelay;
    private int minFlyTime;
    private String maSBDI;
    private String maSNDEN;
    ViewPager vp;
    private ArrayList<String> listInter;
    private ArrayList<String> listWaitingtime;
    private ArrayList<String> listVe;
    private ArrayList<String> listNote;
    private ArrayList<String> selectedAirport;
    private ArrayList<String> mainAirport;
    private ArrayList<String> quantityTicket;
    private RecyclerView listHangVe;
    private RecyclerView listIntermidiate;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = inflater.inflate(R.layout.addflight, container, false);
        DB = new DBHelper(getContext());
        final DatePickerDialog.OnDateSetListener setListener;
        listInter = new ArrayList<String>();
        listNote = new ArrayList<String>();
        selectedAirport = new ArrayList<String>();
        mainAirport = new ArrayList<String>();
        listVe = new ArrayList<String>();
        listWaitingtime = new ArrayList<String>();
        quantityTicket = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            mainAirport.add("n");
        }
        MaChuyenBay = v.findViewById(R.id.ed_add_machuyenbay);
        swipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        GiaVe = v.findViewById(R.id.ed_add_giave);
        Add = v.findViewById(R.id.btn_add);
        AddInfo= v.findViewById(R.id.btn_add_infor);
        AddInfoInter= v.findViewById(R.id.btn_add_infor_inter);
        NgayGio = v.findViewById(R.id.ed_add_ngaygio);
        ThoiGianBay = v.findViewById(R.id.ed_add_thoigianbay);
        SanBayDi = v.findViewById(R.id.spin_sanbaydi);
        SanBayDen = v.findViewById(R.id.spin_sanbayden);
        ListTB = v.findViewById(R.id.spin_tuyenbay);
        SanBayDi.setEnabled(false);
        SanBayDen.setEnabled(false);
        listIntermidiate = v.findViewById(R.id.recycler_listtrunggian);
        listHangVe = v.findViewById(R.id.recycler_listhangve);
        quitUpdate = v.findViewById(R.id.txt_add_title);
        adapterIntermidiate = new AdapterIntermidiate(getContext(), DB, listInter, listNote, selectedAirport,mainAirport, listWaitingtime);
        adapterListHangVe = new AdapterListHangVe(getContext(), DB, listVe, quantityTicket);
        listIntermidiate.setAdapter(adapterIntermidiate);
        listIntermidiate.setLayoutManager(new LinearLayoutManager(getContext()));
        listHangVe.setAdapter(adapterListHangVe);
        listHangVe.setLayoutManager(new LinearLayoutManager(getContext()));
        vp = getActivity().findViewById(R.id.view_pager);

        Cursor cursor2 = DB.getallFlight();
        if (cursor2.getCount() == 0)
        {
            MaChuyenBay.setText("VN1");
            cursor2.close();
        }
        else
        {
            cursor2.moveToPosition(cursor2.getCount()-1);
            String raw = cursor2.getString(0);
            String split[] = raw.split("VN");
            next = Integer.parseInt(split[1])+1;
            MaChuyenBay.setText("VN"+String.valueOf(next));
            cursor2.close();
        }
        Cursor c1 = DB.getAllTB();
        listTuyenBay = new String[c1.getCount()];
        while (c1.moveToNext())
        {
            String tenSBDi = null;
            String tenSBDen = null;
            Cursor c2 = DB.getalldataairport();
            while (c2.moveToNext())
            {
                if (c1.getString(1).equals(c2.getString(0)))
                    tenSBDi = c2.getString(1)+ " ("+c2.getString(2)+", "+c2.getString(3)+")";
                if (c1.getString(2).equals(c2.getString(0)))
                    tenSBDen = c2.getString(1)+ " ("+c2.getString(2)+", "+c2.getString(3)+")";
            }
            listTuyenBay[c1.getPosition()] = c1.getString(0) +": "+tenSBDi+"->"+tenSBDen;
        }
        c1.close();
        ArrayAdapter<String> listtuyenbay = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listTuyenBay);
        ListTB.setAdapter(listtuyenbay);
        Cursor c = DB.getalldataairport();
        listSanBayDi = new String[c.getCount()];
        listSanBayDen = new String[c.getCount()];
        int i=0;
        while (c.moveToNext())
        {
            listSanBayDi[i] = c.getString(1)+ " ("+c.getString(2)+", "+c.getString(3)+")";
            listSanBayDen[i] = c.getString(1)+ " ("+c.getString(2)+", "+c.getString(3)+")";
            i++;
        }
        c.close();
        final Cursor cursor = DB.getRule();
        while (cursor.moveToNext())
        {
            maxInter= Integer.parseInt(cursor.getString(1));
            minDelay= Integer.parseInt(cursor.getString(2));
            maxDelay= Integer.parseInt(cursor.getString(3));
            minFlyTime= Integer.parseInt(cursor.getString(4));
            soon= Integer.parseInt(cursor.getString(6));
        }
        cursor.close();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ArrayAdapter<String> sbdi = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listSanBayDi);
        SanBayDi.setAdapter(sbdi);
        ArrayAdapter<String> sbden = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listSanBayDen);
        SanBayDen.setAdapter(sbden);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final AlertDialog.Builder mydialog = new AlertDialog.Builder(getView().getContext());
                mydialog.setTitle("Refresh sẽ xóa hết thông tin. Tiếp tục? ");
                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();

                        ft.detach(AddFlight.this).attach(AddFlight.this).commit();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        swipeRefreshLayout.setRefreshing(false);
                        dialogInterface.cancel();
                    }
                });
                mydialog.show();

            }
        });
        NgayGio.setInputType(InputType.TYPE_NULL);
        NgayGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDatePickerDialog();
                showDateTimeDialog(NgayGio);
            }
        });
        ListTB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vtDi = 0;
                vtDen = 0;
                String raw = ListTB.getSelectedItem().toString();
                String split[] = raw.split(": ");
                maTuyenBay = split[0];
                String sb[] =  split[1].split("->");
                String raw1[] = sb[0].split(" \\(");
                String raw2[] = sb[1].split(" \\(");
                tenDi = raw1[0];
                tenDen = raw2[0];
                Cursor cursor = DB.getalldataairport();
                while (cursor.moveToNext())
                {
                    if (tenDi.equals(cursor.getString(1)))
                        vtDi = cursor.getPosition();
                    if (tenDen.equals(cursor.getString(1)))
                        vtDen = cursor.getPosition();
                }
                cursor.close();
                SanBayDi.setSelection(vtDi);
                SanBayDen.setSelection(vtDen);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursorr1 = DB.getAirportByName(SanBayDi.getSelectedItem().toString().split(" \\(")[0]);
                while (cursorr1.moveToNext())
                {
                    maSBDI = cursorr1.getString(0);
                }
                cursorr1.close();
                Cursor cursorr2 = DB.getAirportByName(SanBayDen.getSelectedItem().toString().split(" \\(")[0]);
                while (cursorr2.moveToNext())
                {
                    maSNDEN = cursorr2.getString(0);
                }
                cursorr2.close();
                if (ListTB.getCount() == 0 || ListTB.getSelectedItem().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Chưa có tuyến bay", Toast.LENGTH_SHORT).show();
                }
                else if (MaChuyenBay.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Chưa điền mã sân bay", Toast.LENGTH_SHORT).show();
                } else if (GiaVe.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Chưa điền giá vé", Toast.LENGTH_SHORT).show();
                } else if (NgayGio.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Chưa có thời gian", Toast.LENGTH_SHORT).show();
                } else if (ThoiGianBay.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Chưa điền thời gian bay", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(ThoiGianBay.getText().toString()) < minFlyTime) {
                    Toast.makeText(getContext(), "Thời gian bay ít nhất " + minFlyTime + " phút", Toast.LENGTH_SHORT).show();
                } else if (listVe.size() == 0) {
                    Toast.makeText(getContext(), "Chưa có thông tin về vé.", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
                    Date now = calendar.getTime();
                    Date create = null;
                    long total = 0;
                    try {
                        now = simpleDateFormat.parse(simpleDateFormat.format(now.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        create = simpleDateFormat.parse(NgayGio.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    total = daysBetween(create, now);
                    if ( total< soon) {
                        Toast.makeText(getContext(), "Thời gian phải sớm hơn thời gian hủy đặt vé ("+String.valueOf(soon)+" ngày).", Toast.LENGTH_SHORT).show();
                    } else {
                        android.app.AlertDialog.Builder mydialog = new android.app.AlertDialog.Builder(v.getContext());
                        mydialog.setTitle("Bạn đã kiểm tra thông tin kĩ chưa?");
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("Mã chuyến bay: "+MaChuyenBay.getText().toString() +"\n");
                        buffer.append("Giá vé (Giá sàn, chưa tính tỉ lệ): "+ GiaVe.getText().toString() + "đ\n");
                        buffer.append("Sân bay đi: "+SanBayDi.getSelectedItem().toString()+"\n");
                        buffer.append("Sân bay đến: "+SanBayDen.getSelectedItem().toString()+"\n");
                        buffer.append("Ngày khời hành: "+NgayGio.getText().toString()+"\n");
                        buffer.append("Thời gian bay dự kiến: "+ThoiGianBay.getText().toString()+" phút \n\n");
                        for (int i=0; i<listInter.size(); i++)
                        {
                            buffer.append("Sân bay trung gian: "+ listInter.get(i)+"\n");
                            if (listNote.get(i).isEmpty())
                                buffer.append("Ghi chú: Không có \n");
                            else
                                buffer.append("Ghi chú: "+ listNote.get(i)+"\n");
                            buffer.append("Thời gian chờ: "+ listWaitingtime.get(i)+" phút \n\n");
                        }
                        for (int i=0; i<listVe.size(); i++)
                        {
                            buffer.append("Hạng vé: "+ listVe.get(i)+"\n");
                            buffer.append("Số lượng: "+ quantityTicket.get(i)+" vé \n\n");
                        }
                        mydialog.setMessage(buffer.toString());
                        mydialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Boolean isAdd;

                                isAdd = DB.AddFlight(MaChuyenBay.getText().toString(), GiaVe.getText().toString(), maSBDI, maSNDEN, NgayGio.getText().toString(), ThoiGianBay.getText().toString(),maTuyenBay);
                                if (isAdd) {

                                    Toast.makeText(getContext(), "Thêm thông tin thành công", Toast.LENGTH_SHORT).show();
                                    int i = 0;
                                    int tile = 0;
                                    while (i < listVe.size()) {
                                        Boolean isAddVe;
                                        String ma;

                                        ma = new String();
                                        Cursor cursor1 = DB.getLevelTicket();
                                        while (cursor1.moveToNext()) {
                                            if (cursor1.getString(1).equals(listVe.get(i))) {
                                                ma = cursor1.getString(0);
                                                tile = Integer.parseInt(cursor1.getString(2));
                                                break;

                                            }
                                        }
                                        cursor1.close();
                                        int Gia = (Integer.parseInt(GiaVe.getText().toString())*tile)/100;
                                        isAddVe = DB.AddTicketQuantity(ma, MaChuyenBay.getText().toString(), quantityTicket.get(i), quantityTicket.get(i), String.valueOf(Gia));
                                        i++;
                                    }
                                    if (listInter.size() != 0) {
                                        int k = 0;
                                        String ma;
                                        ma = new String();
                                        while (k < listInter.size()) {

                                            Cursor cursor1 = DB.getalldataairport();
                                            while (cursor1.moveToNext()) {
                                                if (cursor1.getString(1).equals(listInter.get(k))) {
                                                    ma = cursor1.getString(0);
                                                    //break;
                                                }
                                            }
                                            cursor1.close();
                                            Boolean isAddInter;
                                            isAddInter = DB.AddIntemidiate(MaChuyenBay.getText().toString(), ma, listWaitingtime.get(k), listNote.get(k));

                                            k++;
                                        }

                                    }
                                    String Ngay []= NgayGio.getText().toString().split(" ");
                                    String thangNam[] = Ngay[0].split("-");
                                    String thang = thangNam[1];
                                    String nam  = thangNam[2];
                                    DB.AddBaoCaoThang(MaChuyenBay.getText().toString(), thang, nam);
                                    Cursor cursor1 = DB.GetBaoCaoNam(thang, nam);
                                    if (cursor1.getCount() == 0)
                                    {
                                        DB.AddBaoCaoNam(thang, nam);
                                        cursor1.close();
                                    }
                                    else
                                    {
                                        int oldSCB = 0;
                                        while (cursor1.moveToNext())
                                        {
                                            oldSCB = Integer.parseInt(cursor1.getString(2));
                                        }
                                        cursor1.close();
                                        oldSCB+=1;
                                        DB.UpdateSoChuyenBayNam(thang, nam, String.valueOf(oldSCB));
                                    }
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                                    ft.detach(AddFlight.this).attach(AddFlight.this).commit();
                                    Cursor cursor4 = DB.getallFlight();
                                    if (cursor4.getCount() == 0)
                                    {
                                        MaChuyenBay.setText("VN1");
                                        cursor4.close();
                                    }
                                    else
                                    {
                                        cursor4.moveToPosition(cursor4.getCount()-1);
                                        String raw = cursor4.getString(0);
                                        String split[] = raw.split("VN");
                                        next = Integer.parseInt(split[1])+1;
                                        MaChuyenBay.setText("VN"+String.valueOf(next));
                                        cursor4.close();
                                    }
                                    swipeRefreshLayout.setRefreshing(false);
                                    GiaVe.setText("");
                                    ThoiGianBay.setText("");
                                    NgayGio.setText("");
                                } else {
                                    Toast.makeText(getContext(), "Thêm thông tin thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        mydialog.show();

                    }
                }
            }
        });
        AddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mydialog = new AlertDialog.Builder(v.getContext());
                View mView = inflater.inflate(R.layout.selectticketlevel, null);
                mydialog.setTitle("Thêm hạng vé");
                final Spinner spin_listHangVe = mView.findViewById(R.id.spin_add_hangve);
                final EditText ed_quantity = mView.findViewById(R.id.ed_quantity_ticket);
                Cursor c = DB.getLevelTicket();
                final String str_listVe[];
                str_listVe = new String[c.getCount()];
                int i=0;
                while (c.moveToNext())
                {
                    str_listVe[i] = c.getString(1) + " (Tỉ lệ: " + c.getString(2) +"%)"  ;
                    i++;
                }
                c.close();
                ArrayAdapter<String> sbdi = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1,str_listVe);
                spin_listHangVe.setAdapter(sbdi);
                mydialog.setView(mView);
                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String need = spin_listHangVe.getSelectedItem().toString();
                        String[] separated = need.split(" \\(");
                        if  (ed_quantity.getText().toString().isEmpty())
                        {
                            Toast.makeText(getContext(), "Chưa điền số lượng", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (listVe.contains(separated[0])) {
                                Toast.makeText(getContext(), "Hạng vé này đã chọn", Toast.LENGTH_SHORT).show();
                            } else {
                                listVe.add(separated[0]);
                                quantityTicket.add(ed_quantity.getText().toString());
                                adapterListHangVe.notifyDataSetChanged();
                            }
                        }
                    }
                });
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                mydialog.show();
            }
        });
        AddInfoInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listInter.size() == maxInter) {
                    Toast.makeText(getContext(), "Đã đạt số lượng sân bay trung gian tối đa", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder mydialog = new AlertDialog.Builder(v.getContext());
                    View mView = inflater.inflate(R.layout.addinter, null);
                    mydialog.setTitle("Thêm sân bay trung gian");
                    final Spinner listAirport = mView.findViewById(R.id.spin_add_intermidiate);
                    final EditText note = mView.findViewById(R.id.ed_add_note);
                    final EditText waitTime = mView.findViewById(R.id.ed_add_waitingtime);
                    Cursor c = DB.getalldataairport();
                    String listSanBay[];
                    listSanBay = new String[c.getCount()];
                    int i = 0;
                    while (c.moveToNext()) {
                        listSanBay[i] = c.getString(1)+ " ("+c.getString(2)+", "+c.getString(3)+")";
                        listSanBay[i] = c.getString(1)+ " ("+c.getString(2)+", "+c.getString(3)+")";
                        i++;
                    }
                    c.close();
                    ArrayAdapter<String> sbdi = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, listSanBay);
                    listAirport.setAdapter(sbdi);
                    mydialog.setView(mView);
                    mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (waitTime.getText().toString().isEmpty())
                            {
                                Toast.makeText(getContext(), "Chưa điền thời gian chờ", Toast.LENGTH_SHORT).show();
                            }else
                            if (listAirport.getSelectedItem().toString().equals(mainAirport.get(0)) || listAirport.getSelectedItem().toString().equals(mainAirport.get(1))) {
                                Toast.makeText(getContext(), "Không thể trùng sân bay gốc và các sân bay trung gian đã chọn", Toast.LENGTH_SHORT).show();
                            } else if (Integer.parseInt(waitTime.getText().toString()) < minDelay || Integer.parseInt(waitTime.getText().toString()) > maxDelay)
                            {
                                Toast.makeText(getContext(), "Thời gian chờ giới hạn từ "+minDelay+"-"+maxDelay+" phút", Toast.LENGTH_SHORT).show();
                            }
                            else if (listInter.contains(listAirport.getSelectedItem().toString().split(" \\(")[0])) {
                                Toast.makeText(getContext(), "Các sân bay trung gian không thể trùng nhau", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                listInter.add(listAirport.getSelectedItem().toString().split(" \\(")[0]);
                                listNote.add(note.getText().toString());
                                listWaitingtime.add((waitTime.getText().toString()));
                                selectedAirport.add(listAirport.getSelectedItem().toString());
                                adapterIntermidiate.notifyDataSetChanged();
                            }
                        }
                    });
                    mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    mydialog.show();
                }
            }
        });

        ThoiGianBay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Cursor c  = DB.getRule();
                int min = 0;
                while (c.moveToNext())
                {
                    min = Integer.parseInt(c.getString(4));
                }
                c.close();
                if (!ThoiGianBay.getText().toString().isEmpty()) {
                    if (Integer.parseInt(ThoiGianBay.getText().toString()) < min) {
                        ThoiGianBay.setTextColor(Color.RED);
                    } else ThoiGianBay.setTextColor(Color.WHITE);
                }
            }
        });
        SanBayDi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (SanBayDi.getSelectedItem().toString().equals(SanBayDen.getSelectedItem().toString()))
                {

                    if (SanBayDi.getSelectedItemPosition() == SanBayDi.getCount()-1) {
                        SanBayDi.setSelection(0);
                        mainAirport.set(0, SanBayDi.getSelectedItem().toString());
                    }
                    else
                    {
                        SanBayDi.setSelection(SanBayDi.getSelectedItemPosition()+1);
                        mainAirport.set(0, SanBayDi.getSelectedItem().toString());


                    }

                }
                else
                {
                    mainAirport.set(0, SanBayDi.getSelectedItem().toString());
                    adapterIntermidiate.notifyDataSetChanged();

                }
                int position = listInter.indexOf(SanBayDi.getSelectedItem().toString());
                listInter.remove(SanBayDi.getSelectedItem().toString().split(" \\(")[0]);
                adapterIntermidiate.notifyItemRemoved(position);
                adapterIntermidiate.notifyItemRangeChanged(position, listInter.size());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SanBayDen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (SanBayDi.getSelectedItem().toString().equals(SanBayDen.getSelectedItem().toString()))
                {
                    if (SanBayDen.getSelectedItemPosition() == SanBayDen.getCount()-1) {
                        SanBayDen.setSelection(0);
                        mainAirport.set(1, SanBayDen.getSelectedItem().toString());
                        adapterIntermidiate.notifyDataSetChanged();
                    }
                    else
                    {

                        SanBayDen.setSelection(SanBayDen.getSelectedItemPosition()+1);
                        mainAirport.set(1, SanBayDen.getSelectedItem().toString());
                        adapterIntermidiate.notifyDataSetChanged();

                    }
                }
                else
                {
                    mainAirport.set(1, SanBayDen.getSelectedItem().toString());
                    adapterIntermidiate.notifyDataSetChanged();

                }
                int position = listInter.indexOf(SanBayDen.getSelectedItem().toString());
                listInter.remove(SanBayDen.getSelectedItem().toString().split(" \\(")[0]);
                adapterIntermidiate.notifyItemRemoved(position);
                adapterIntermidiate.notifyItemRangeChanged(position, listInter.size());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (vp.getCurrentItem() == 0)
                {

                    final MainActivity b = (MainActivity) getActivity();
                    if (!b.getMCBEdit().isEmpty())
                    {
                        quitUpdate.setText("Click vào đây để thoát chế độ cập nhật");
                        quitUpdate.setPaintFlags(quitUpdate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        quitUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = getActivity().getIntent();
                                getActivity().finish();
                                startActivity(intent);
                            }
                        });
                        Add.setText("Cập nhật");
                        Add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {Cursor cursorr1 = DB.getAirportByName(SanBayDi.getSelectedItem().toString().split(" \\(")[0]);
                                while (cursorr1.moveToNext())
                                {
                                    maSBDI = cursorr1.getString(0);
                                }
                                cursorr1.close();
                                Cursor cursorr2 = DB.getAirportByName(SanBayDen.getSelectedItem().toString().split(" \\(")[0]);
                                while (cursorr2.moveToNext())
                                {
                                    maSNDEN = cursorr2.getString(0);
                                }
                                cursorr2.close();
                                if (ListTB.getCount() == 0)
                                {
                                    Toast.makeText(getContext(), "Chưa có tuyến bay", Toast.LENGTH_SHORT).show();
                                }
                                else if (MaChuyenBay.getText().toString().isEmpty()) {
                                    Toast.makeText(getContext(), "Chưa điền mã sân bay", Toast.LENGTH_SHORT).show();
                                } else if (GiaVe.getText().toString().isEmpty()) {
                                    Toast.makeText(getContext(), "Chưa điền giá vé", Toast.LENGTH_SHORT).show();
                                } else if (NgayGio.getText().toString().isEmpty()) {
                                    Toast.makeText(getContext(), "Chưa có thời gian", Toast.LENGTH_SHORT).show();
                                } else if (ThoiGianBay.getText().toString().isEmpty()) {
                                    Toast.makeText(getContext(), "Chưa điền thời gian bay", Toast.LENGTH_SHORT).show();
                                } else if (Integer.parseInt(ThoiGianBay.getText().toString()) < minFlyTime) {

                                    // total = daysBetween(create, now) ;
                                    Toast.makeText(getContext(), "Thời gian bay ít nhất " + minFlyTime + " phút", Toast.LENGTH_SHORT).show();
                                } else if (listVe.size() == 0) {
                                    Toast.makeText(getContext(), "Chưa có thông tin về vé.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
                                    Date now = calendar.getTime();
                                    Date create = null;
                                    long total = 0;
                                    try {
                                        now = simpleDateFormat.parse(simpleDateFormat.format(now.getTime()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        create = simpleDateFormat.parse(NgayGio.getText().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    total = daysBetween(create, now);
                                    if ( total< soon) {
                                        Toast.makeText(getContext(), "Thời gian phải sớm hơn thời gian hủy đặt vé ("+String.valueOf(soon)+" ngày).", Toast.LENGTH_SHORT).show();
                                    } else {
                                        android.app.AlertDialog.Builder mydialog = new android.app.AlertDialog.Builder(v.getContext());
                                        mydialog.setTitle("Bạn đã kiểm tra thông tin kĩ chưa?");
                                        StringBuffer buffer = new StringBuffer();
                                        buffer.append("Mã chuyến bay: "+MaChuyenBay.getText().toString() +"\n");
                                        buffer.append("Giá vé (Giá sàn, chưa tính tỉ lệ): "+ GiaVe.getText().toString() + "đ\n");
                                        buffer.append("Sân bay đi: "+SanBayDi.getSelectedItem().toString()+"\n");
                                        buffer.append("Sân bay đến: "+SanBayDen.getSelectedItem().toString()+"\n");
                                        buffer.append("Ngày khời hành: "+NgayGio.getText().toString()+"\n");
                                        buffer.append("Thời gian bay dự kiến: "+ThoiGianBay.getText().toString()+" phút \n\n");
                                        for (int i=0; i<listInter.size(); i++)
                                        {
                                            buffer.append("Sân bay trung gian: "+ listInter.get(i)+"\n");
                                            if (listNote.get(i).isEmpty())
                                                buffer.append("Ghi chú: Không có \n");
                                            else
                                                buffer.append("Ghi chú: "+ listNote.get(i)+"\n");
                                            buffer.append("Thời gian chờ: "+ listWaitingtime.get(i)+" phút \n\n");
                                        }
                                        for (int i=0; i<listVe.size(); i++)
                                        {
                                            buffer.append("Hạng vé: "+ listVe.get(i)+"\n");
                                            buffer.append("Số lượng: "+ quantityTicket.get(i)+" vé \n\n");
                                        }
                                        mydialog.setMessage(buffer.toString());
                                        mydialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Boolean isAdd;
                                                DB.deleteTrungGian(MaChuyenBay.getText().toString());
                                                DB.deleteSoLuongVe(MaChuyenBay.getText().toString());
                                                DB.deleteGuest(MaChuyenBay.getText().toString());
                                                DB.deleteBCT(MaChuyenBay.getText().toString());
                                                DB.deleteChuyenBay(MaChuyenBay.getText().toString());
                                                isAdd = DB.AddFlight(MaChuyenBay.getText().toString(), GiaVe.getText().toString(), maSBDI, maSNDEN, NgayGio.getText().toString(), ThoiGianBay.getText().toString(),maTuyenBay);
                                                if (isAdd) {

                                                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                    int i = 0;
                                                    int tile = 0;
                                                    while (i < listVe.size()) {
                                                        Boolean isAddVe;
                                                        String ma;

                                                        ma = new String();
                                                        Cursor cursor1 = DB.getLevelTicket();
                                                        while (cursor1.moveToNext()) {
                                                            if (cursor1.getString(1).equals(listVe.get(i))) {
                                                                ma = cursor1.getString(0);
                                                                tile = Integer.parseInt(cursor1.getString(2));
                                                                break;

                                                            }
                                                        }
                                                        cursor1.close();
                                                        int Gia = (Integer.parseInt(GiaVe.getText().toString())*tile)/100;
                                                        isAddVe = DB.AddTicketQuantity(ma, MaChuyenBay.getText().toString(), quantityTicket.get(i), quantityTicket.get(i), String.valueOf(Gia));

                                                        i++;
                                                    }
                                                    if (listInter.size() != 0) {
                                                        int k = 0;
                                                        String ma;
                                                        ma = new String();
                                                        while (k < listInter.size()) {

                                                            Cursor cursor1 = DB.getalldataairport();
                                                            while (cursor1.moveToNext()) {
                                                                if (cursor1.getString(1).equals(listInter.get(k))) {
                                                                    ma = cursor1.getString(0);
                                                                    //break;
                                                                }
                                                            }
                                                            cursor1.close();
                                                            Boolean isAddInter;
                                                            isAddInter = DB.AddIntemidiate(MaChuyenBay.getText().toString(), ma, listWaitingtime.get(k), listNote.get(k));

                                                            k++;
                                                        }

                                                    }
                                                    String Ngay []= NgayGio.getText().toString().split(" ");
                                                    String thangNam[] = Ngay[0].split("-");
                                                    String thang = thangNam[1];
                                                    String nam  = thangNam[2];
                                                    DB.AddBaoCaoThang(MaChuyenBay.getText().toString(), thang, nam);
                                                    Cursor cursor1 = DB.GetBaoCaoNam(thang, nam);
                                                    if (cursor1.getCount() == 0)
                                                    {
                                                        DB.AddBaoCaoNam(thang, nam);
                                                        cursor1.close();
                                                    }
                                                    else
                                                    {
                                                        int oldSCB = 0;
                                                        while (cursor1.moveToNext())
                                                        {
                                                            oldSCB = Integer.parseInt(cursor1.getString(2));
                                                        }
                                                        cursor1.close();
                                                        oldSCB+=1;
                                                        DB.UpdateSoChuyenBayNam(thang, nam, String.valueOf(oldSCB));
                                                    }
                                                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                                                    ft.detach(AddFlight.this).attach(AddFlight.this).commit();
                                                    Cursor cursor4 = DB.getallFlight();
                                                    swipeRefreshLayout.setRefreshing(false);
                                                    GiaVe.setText("");
                                                    ThoiGianBay.setText("");
                                                    NgayGio.setText("");
                                                    b.setMCBEdit("");
                                                    Intent intent = getActivity().getIntent();
                                                    getActivity().finish();
                                                    startActivity(intent);

                                                } else {
                                                    Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        mydialog.show();

                                    }
                                }
                            }


                        });
                        MaChuyenBay.setText(b.getMCBEdit());
                        Cursor cursor1 = DB.getFlight(b.getMCBEdit());
                        String matb = null;
                        while (cursor1.moveToNext())
                        {
                            GiaVe.setText(cursor1.getString(1));
                            NgayGio.setText(cursor1.getString(4));
                            ThoiGianBay.setText(cursor1.getString(5));
                            matb = cursor1.getString(7);
                        }
                        Cursor cursor3 = DB.getAllTB();
                        int vt=0;
                        while (cursor3.moveToNext())
                        {
                            if (matb.equals(cursor3.getString(0)))
                                vt=cursor3.getPosition();
                        }
                        ListTB.setSelection(vt);
                        Cursor cursor4 = DB.GetTicketOfFlight(b.getMCBEdit());
                        resetArrayList(listVe);
                        resetArrayList(quantityTicket);
                        resetArrayList(listNote);
                        resetArrayList(listWaitingtime);
                        resetArrayList(selectedAirport);
                        while (cursor4.moveToNext())
                        {

                            quantityTicket.add(cursor4.getString(2));
                            Cursor cursor5 = DB.GetInfoOfTicket(cursor4.getString(0));
                            while (cursor5.moveToNext()) {
                                listVe.add(cursor5.getString(1));

                            }
                            adapterListHangVe.notifyDataSetChanged();
                            cursor5.close();
                        }
                        cursor4.close();
                        Cursor cursor5 = DB.getAllTrungGian(b.getMCBEdit());
                        while (cursor5.moveToNext()) {
                            Cursor cursor6 = DB.getAirportInfo(cursor5.getString(1));
                            while (cursor6.moveToNext())
                            {
                                listInter.add(cursor6.getString(1));
                            }
                            cursor6.close();

                            listNote.add(cursor5.getString(3));
                            listWaitingtime.add((cursor5.getString(2)));
                            selectedAirport.add((cursor5.getString(2)));
                            adapterIntermidiate.notifyDataSetChanged();
                        }
                        cursor5.close();
                        b.setMCBEdit("");
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(getContext(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),false).show();
            }
        };
        new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void RefreshSelected()
    {
        Cursor c = DB.getalldataselectedairport();
        while (c.moveToNext())
        {
            if (!c.getString(1).equals(SanBayDen.getSelectedItem().toString()) && !c.getString(1).equals(SanBayDi.getSelectedItem().toString()))
            {
                DB.deleteselectedairport(c.getString(1));
            }

        }
        c.close();
    }
    public void TestRefreshSelected()
    {
        Cursor c = DB.getalldataselectedairport();
        while (c.moveToNext())
        {
            Toast.makeText(getContext(), c.getString(1) , Toast.LENGTH_SHORT).show();
        }
        c.close();
    }
    public void resetArrayList(ArrayList<String> a)
    {
        int i =0;
        while (i<a.size())
        {
            a.remove(i);
        }
    }
    public  long daysBetween(Date create, Date now)
    {
        long differennce = (create.getTime()-now.getTime())/86400000;
        return differennce;
    }


}
