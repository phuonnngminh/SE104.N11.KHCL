package com.e.vemaybay;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
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
import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class ChangeRules extends Fragment {
    Button AddAirport;
    Button AddTicketLevel;
    Button AddTuyenBay;
    Button ChangeMinFlyTime;
    Button changeSecurityCode;
    Button mngAirport;
    Button mngTicketLevel;
    Button mngFlight;
    Button deleteAirport;
    Button deleteTuyenBay;
    Button updateAirport;
    Button deleteTicketLevel;
    Button updateTicketLevel;
    Button updateTuyenBay;
    Button ChangeMaxIntermidiate;
    Button ChangeMinMaxDelayTime;
    Button ChangeBookCancelTime;
    Button mngTuyenBay;
    Button Reset;
    EditText MaSanBay;
    EditText TenSanBay;
    EditText Tinh;
    EditText Nuoc;
    EditText Mahang;
    EditText TenHang;
    EditText TiLe;
    EditText MinDelayTime;
    EditText MaxDelayTime;
    EditText LatestDayBook;
    EditText LatestDayCancel;
    EditText MinFlyTime;
    EditText NewIntermidiate;
    Spinner SanBayDi;
    Spinner SanBayDen;
    EditText MaTuyenBay;
    String newSBDi;
    String newSBDen;
    TextView minFly;
    TextView maxInter;
    TextView cancel;
    TextView delay;
    DBHelper DB;
    ViewPager vp;

    int vtDi;
    int vtDen;
    String tenDi;
    String tenDen;
    private Executor executor;
    BiometricPrompt biometricPrompt;
    boolean check1 =false;
    BiometricPrompt.PromptInfo promptInfo;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.changerules,container,false);
        ChangeMinFlyTime = v.findViewById(R.id.btn_changemintime);
        ChangeMaxIntermidiate = v.findViewById(R.id.btn_changeminintermidiate);
        ChangeMinMaxDelayTime = v.findViewById(R.id.btn_changeminmaxdelaytime);
        mngAirport = v.findViewById(R.id.btn_mngairport);
        mngTicketLevel = v.findViewById(R.id.btn_mngticketlevel);
        mngTuyenBay = v.findViewById(R.id.btn_mngtuyenbay);
        mngFlight = v.findViewById(R.id.btn_mngfl);

        ChangeBookCancelTime = v.findViewById(R.id.btn_changebookcanceltime);
        changeSecurityCode = v.findViewById(R.id.btn_change_securitycode);
        minFly = v.findViewById(R.id.tv_minfly);
        maxInter = v.findViewById(R.id.tv_maxInter);
        cancel = v.findViewById(R.id.tv_cancel);
        delay = v.findViewById(R.id.tv_delay);
        Reset = v.findViewById(R.id.btn_reset);
        DB = new DBHelper(getContext());
        vp=(ViewPager) getActivity().findViewById(R.id.view_pager);
        executor = ContextCompat.getMainExecutor(ChangeRules.this.getContext());
        biometricPrompt = new BiometricPrompt(ChangeRules.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(ChangeRules.this.getContext(), "Lỗi!, thiết bị không có nhận diện vân tay, hãy nhập mã bảo mật để tiếp tục", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder mydialog = new AlertDialog.Builder(ChangeRules.this.getContext());
                mydialog.setTitle("Nhập mã bảo mật");
                final EditText secure = new EditText(ChangeRules.this.getContext());
                secure.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                mydialog.setView(secure);
                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean check = false;
                        Cursor c = DB.getRule();
                        while (c.moveToNext()) {
                            if (secure.getText().toString().equals(c.getString(5))) {
                                vp.setCurrentItem(5);
                                check = true;
                                check1 = true;
                            }
                        }
                        c.close();
                        if (!check) {
                            Toast.makeText(ChangeRules.this.getContext(), "Sai mã bảo mật", Toast.LENGTH_SHORT).show();
                            vp.setCurrentItem(4);
                        }
                    }
                });
                mydialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (!check1) {
                            vp.setCurrentItem(4);
                        }
                        else
                        {
                            check1 = false;
                        }
                    }
                });
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        vp.setCurrentItem(4);
                    }
                });
                mydialog.show();

            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                vp.setCurrentItem(4);
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Xác thực sinh trắc học").setSubtitle("Hãy đặt vân tay vào ô tròn").setNegativeButtonText("Dùng mật khẩu").build();
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (vp.getCurrentItem() == 5)
                {
                    biometricPrompt.authenticate(promptInfo);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Cursor c12 = DB.getRule();
        while (c12.moveToNext())
        {
            minFly.setText("Thời gian bay tối thiểu: "+ c12.getString(4)+" phút");
            maxInter.setText("Số lượng sân bay trung gian tối đa: "+ c12.getString(1));
            cancel.setText("Thời gian đặt/hủy vé: "+ c12.getString(6)+" ngày/"+c12.getString(7)+" ngày");
            delay.setText("Thời gian dừng tối thiểu/tối đa: "+ c12.getString(2)+" phút/"+c12.getString(3)+" phút");
        }
        c12.close();
        mngFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View mView = getLayoutInflater().inflate(R.layout.mngflight, null);
                final AlertDialog mydialog = new AlertDialog.Builder(getContext()).setTitle("Lựa chọn hạng mục")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setView(mView).show();
                Button addfl = mView.findViewById(R.id.btn_mng_addfl);
                Button delfl = mView.findViewById(R.id.btn_mng_deletefl);
                Button updfl = mView.findViewById(R.id.btn_mng_updatefl);
                addfl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vp.setCurrentItem(0);
                        mydialog.cancel();
                    }
                });
                delfl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayAdapter<String> flights;
                        AlertDialog.Builder mydialog = new AlertDialog.Builder(v.getContext());
                        View mView = getLayoutInflater().inflate(R.layout.deleteflight, null);
                        mydialog.setTitle("Chọn chuyến bay cần xóa");
                        final Spinner listFlight = mView.findViewById(R.id.spin_deleteflight_listflight);
                        Cursor cursor = DB.getallFlight();
                        String list[];
                        list = new String[cursor.getCount()];
                        int i=0;
                        while (cursor.moveToNext())
                        {
                            Cursor cursorr1 = DB.GetNameAirport(cursor.getString(2));
                            Cursor cursorr2 = DB.GetNameAirport(cursor.getString(3) );
                            String madi = "null";
                            String maden = "null";
                            while (cursorr1.moveToNext())
                            {
                                madi = cursorr1.getString(1)+ " ("+cursorr1.getString(2)+", "+cursorr1.getString(3)+")";
                            }
                            cursorr1.close();
                            while (cursorr2.moveToNext())
                            {
                                maden = cursorr2.getString(1)+ " ("+cursorr2.getString(2)+", "+cursorr2.getString(3)+")";
                            }
                            cursorr2.close();
                            list[i] = cursor.getString(0) + ": "+madi+" -> "+maden ;
                            i++;
                        }
                        cursor.close();
                        flights = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list);
                        listFlight.setAdapter(flights);
                        mydialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String maChuyenBay = listFlight.getSelectedItem().toString().split(": ")[0];
                                Cursor cursor1 = DB.getFlight(maChuyenBay);
                                float dt = 0;
                                while (cursor1.moveToNext())
                                {
                                    dt = Float.parseFloat(cursor1.getString(6));
                                }
                                if (dt==0)
                                {
                                    DB.deleteTrungGian(maChuyenBay);
                                    DB.deleteSoLuongVe(maChuyenBay);
                                    DB.deleteGuest(maChuyenBay);
                                    DB.deleteBCT(maChuyenBay);
                                    boolean check = DB.deleteChuyenBay(maChuyenBay);
                                    if (check)
                                    {
                                        Toast.makeText(ChangeRules.this.getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(ChangeRules.this.getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(ChangeRules.this.getContext(), "Chuyến bay đã có doanh thu, không thể xóa.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        mydialog.setView(mView);
                        mydialog.show();
                    }
                });
                updfl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayAdapter<String> flights;
                        final AlertDialog.Builder mydialog1 = new AlertDialog.Builder(v.getContext());
                        View mView = getLayoutInflater().inflate(R.layout.updatechuyenbay, null);
                        mydialog1.setTitle("Chọn chuyến bay cần cập nhật");
                        final Spinner listFlight = mView.findViewById(R.id.spin_updateflight_listflight);
                        Cursor cursor = DB.getallFlight();
                        String list[];
                        vp=(ViewPager) ChangeRules.this.getActivity().findViewById(R.id.view_pager);
                        final MainActivity b = (MainActivity) ChangeRules.this.getActivity();
                        list = new String[cursor.getCount()];
                        int i=0;
                        while (cursor.moveToNext())
                        {
                            Cursor cursorr1 = DB.GetNameAirport(cursor.getString(2));
                            Cursor cursorr2 = DB.GetNameAirport(cursor.getString(3) );
                            String madi = "null";
                            String maden = "null";
                            while (cursorr1.moveToNext())
                            {
                                madi = cursorr1.getString(1)+ " ("+cursorr1.getString(2)+", "+cursorr1.getString(3)+")";
                            }
                            cursorr1.close();
                            while (cursorr2.moveToNext())
                            {
                                maden = cursorr2.getString(1)+ " ("+cursorr2.getString(2)+", "+cursorr2.getString(3)+")";
                            }
                            cursorr2.close();
                            list[i] = cursor.getString(0) + ": "+madi+" -> "+maden ;
                            i++;
                        }
                        cursor.close();
                        flights = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list);
                        listFlight.setAdapter(flights);
                        mydialog1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String maChuyenBay = listFlight.getSelectedItem().toString().split(": ")[0];
                                Cursor cursor1 = DB.getFlight(maChuyenBay);
                                float dt = 0;
                                while (cursor1.moveToNext())
                                {
                                    dt = Float.parseFloat(cursor1.getString(6));
                                }
                                if (dt==0)
                                {
                                    mydialog.cancel();
                                    b.setMCBEdit(listFlight.getSelectedItem().toString().split(": ")[0]);
                                    vp.setCurrentItem(0);
                                }
                                else
                                {
                                    Toast.makeText(ChangeRules.this.getContext(), "Chuyến bay đã có doanh thu, không thể cập nhật.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        mydialog1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });

                        mydialog1.setView(mView);
                        mydialog1.show();

                    }
                });
            }
        });
        mngTuyenBay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.mgntuyenbay, null);
                mydialog.setTitle("Lựa chọn hạng mục");
                AddTuyenBay = mView.findViewById(R.id.btn_mng_addtuyenbay);
                deleteTuyenBay = mView.findViewById(R.id.btn_mng_deletetuyenbay);
                updateTuyenBay = mView.findViewById(R.id.btn_mng_updatetuyenbay);
                AddTuyenBay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.addtuyenbay, null);
                        mydialog.setTitle("Nhập thông tin tuyến bay");
                        MaTuyenBay = mView.findViewById(R.id.ed_matuyenbay);
                        SanBayDi = mView.findViewById(R.id.spin_addtuyenbay_di);
                        SanBayDen = mView.findViewById(R.id.spin_addtuyenbay_den);
                        String listSanBayDi[], listSanBayDen[];
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
                        ArrayAdapter<String> sbdi = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listSanBayDi);
                        SanBayDi.setAdapter(sbdi);
                        ArrayAdapter<String> sbden = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listSanBayDen);
                        SanBayDen.setAdapter(sbden);
                        SanBayDi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                if (SanBayDi.getSelectedItem().toString().equals(SanBayDen.getSelectedItem().toString()))
                                {

                                    if (SanBayDi.getSelectedItemPosition() == SanBayDi.getCount()-1) {
                                        SanBayDi.setSelection(0);
                                    }
                                    else
                                    {
                                        SanBayDi.setSelection(SanBayDi.getSelectedItemPosition()+1);

                                    }

                                }

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
                                    }
                                    else
                                    {

                                        SanBayDen.setSelection(SanBayDen.getSelectedItemPosition()+1);

                                    }
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        mydialog.setView(mView);
                        Cursor cursor = DB.getAllTB();
                        if (cursor.getCount()==0)
                        {
                            MaTuyenBay.setText("TB1");
                            cursor.close();
                        }
                        else
                        {
                            cursor.moveToPosition(cursor.getCount()-1);
                            String raw = cursor.getString(0);
                            String split[] = raw.split("TB");
                            int a = Integer.parseInt(split[1]);
                            MaTuyenBay.setText("TB"+String.valueOf(a+1));
                            cursor.close();
                        }
                        mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (MaTuyenBay.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getContext(), "Thiếu thông tin!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String MaSBDi = null;
                                    String MaSBDen = null;
                                    String split[] = SanBayDi.getSelectedItem().toString().split(" \\(");
                                    String tensbdi = split[0];
                                    String split1[] = SanBayDen.getSelectedItem().toString().split(" \\(");
                                    String tensbden = split1[0];
                                    Cursor cursor = DB.getAirportByName(tensbdi);
                                    while (cursor.moveToNext()) {
                                        MaSBDi = cursor.getString(0);
                                    }
                                    cursor.close();
                                    Cursor cursor1 = DB.getAirportByName(tensbden);
                                    while (cursor1.moveToNext()) {
                                        MaSBDen = cursor1.getString(0);
                                    }
                                    cursor1.close();
                                    boolean check = DB.AddTuyenBay(MaTuyenBay.getText().toString(), MaSBDi, MaSBDen);
                                    if (check) {
                                        Toast.makeText(getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
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
                updateTuyenBay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Spinner listTB;
                        final TextView maTB;
                        String listTuyenBay[];

                        final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.updatetuyenbay, null);
                        SanBayDi = mView.findViewById(R.id.spin_upd_sbdi);
                        SanBayDen = mView.findViewById(R.id.spin_upd_sbden);
                        listTB = mView.findViewById(R.id.spin_updtb_listtb);
                        maTB = mView.findViewById(R.id.txt_updtb_matb);
                        mydialog.setTitle("Cập nhật thông tin tuyến bay");
                        String listSanBayDi[], listSanBayDen[];
                        Cursor c = DB.getalldataairport();
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
                            c2.close();
                            listTuyenBay[c1.getPosition()] = c1.getString(0) +": "+tenSBDi+"->"+tenSBDen;
                        }
                        c1.close();
                        ArrayAdapter<String> listtuyenbay = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listTuyenBay);
                        listTB.setAdapter(listtuyenbay);
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
                        listTB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                vtDi = 0;
                                vtDen = 0;
                                String raw = listTB.getSelectedItem().toString();
                                String split[] = raw.split(": ");
                                String sb[] =  split[1].split("->");
                                String tenDiraw[] = sb[0].split(" \\(");
                                String tenDenraw[] = sb[1].split(" \\(");
                                tenDi = tenDiraw[0];
                                tenDen = tenDenraw[0];
                                Cursor cursor = DB.getalldataairport();
                                while (cursor.moveToNext())
                                {
                                    if (tenDi.equals(cursor.getString(1)))
                                        vtDi = cursor.getPosition();
                                    if (tenDen.equals(cursor.getString(1)))
                                        vtDen = cursor.getPosition();
                                }
                                cursor.close();
                                maTB.setText(split[0]);
                                SanBayDi.setSelection(vtDi);
                                SanBayDen.setSelection(vtDen);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        ArrayAdapter<String> sbdi = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listSanBayDi);
                        SanBayDi.setAdapter(sbdi);
                        ArrayAdapter<String> sbden = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listSanBayDen);
                        SanBayDen.setAdapter(sbden);
                        SanBayDi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                if (SanBayDi.getSelectedItem().toString().equals(SanBayDen.getSelectedItem().toString()))
                                {

                                    if (SanBayDi.getSelectedItemPosition() == SanBayDi.getCount()-1) {
                                        SanBayDi.setSelection(0);
                                        String raw[] = SanBayDi.getSelectedItem().toString().split(" \\(");
                                        Cursor cursor = DB.getAirportByName(raw[0]);
                                        while (cursor.moveToNext())
                                        {
                                            newSBDi = cursor.getString(0);
                                        }
                                        cursor.close();
                                    }
                                    else
                                    {
                                        SanBayDi.setSelection(SanBayDi.getSelectedItemPosition()+1);
                                        String raw[] = SanBayDi.getSelectedItem().toString().split(" \\(");
                                        Cursor cursor = DB.getAirportByName(raw[0]);
                                        while (cursor.moveToNext())
                                        {
                                            newSBDi = cursor.getString(0);
                                        }
                                        cursor.close();

                                    }

                                }
                                String raw[] = SanBayDi.getSelectedItem().toString().split(" \\(");
                                Cursor cursor = DB.getAirportByName(raw[0]);
                                while (cursor.moveToNext())
                                {
                                    newSBDi = cursor.getString(0);
                                }
                                cursor.close();

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
                                        String raw[] = SanBayDen.getSelectedItem().toString().split(" \\(");
                                        Cursor cursor = DB.getAirportByName(raw[0]);
                                        while (cursor.moveToNext())
                                        {
                                            newSBDen = cursor.getString(0);
                                        }
                                        cursor.close();
                                    }
                                    else
                                    {

                                        SanBayDen.setSelection(SanBayDen.getSelectedItemPosition()+1);
                                        String raw[] = SanBayDen.getSelectedItem().toString().split(" \\(");
                                        Cursor cursor = DB.getAirportByName(raw[0]);
                                        while (cursor.moveToNext())
                                        {
                                            newSBDen = cursor.getString(0);
                                        }
                                        cursor.close();
                                    }
                                }
                                String raw[] = SanBayDen.getSelectedItem().toString().split(" \\(");
                                Cursor cursor = DB.getAirportByName(raw[0]);
                                while (cursor.moveToNext())
                                {
                                    newSBDen = cursor.getString(0);
                                }
                                cursor.close();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        mydialog.setView(mView);
                        mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean check = DB.UpdateTuyenBay(maTB.getText().toString(), newSBDi, newSBDen);
                                if (check)
                                {
                                    Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    Cursor cursor = DB.getallFlight();
                                    while (cursor.moveToNext())
                                    {
                                        if (cursor.getString(7).equals(maTB.getText().toString()))
                                        {
                                            DB.UpdateDiDen(cursor.getString(0), newSBDi, newSBDen);
                                            Cursor cursor1 = DB.getTrungGian(cursor.getString(0), newSBDi);
                                            Cursor cursor2 = DB.getTrungGian(cursor.getString(0), newSBDen);
                                            if (cursor1.getCount()>0)
                                                DB.deleteTrungGian(cursor.getString(0), newSBDi);
                                            if (cursor2.getCount()>0)
                                                DB.deleteTrungGian(cursor.getString(0), newSBDen);
                                        }
                                    }
                                    cursor.close();
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
                deleteTuyenBay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.deletetuyenbay, null);
                        mydialog.setTitle("Chọn tuyến bay cần xóa");
                        final Spinner listTB;
                        listTB = mView.findViewById(R.id.spin_mng_deletetuyenbay);
                        String listTuyenBay[];
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
                                    tenSBDi =  c2.getString(1)+ " ("+c2.getString(2)+", "+c2.getString(3)+")";
                                if (c1.getString(2).equals(c2.getString(0)))
                                    tenSBDen = c2.getString(1)+ " ("+c2.getString(2)+", "+c2.getString(3)+")";
                            }
                            c2.close();
                            listTuyenBay[c1.getPosition()] = c1.getString(0) +": "+tenSBDi+"->"+tenSBDen;
                        }
                        c1.close();
                        ArrayAdapter<String> listtuyenbay = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listTuyenBay);
                        listTB.setAdapter(listtuyenbay);
                        mydialog.setView(mView);
                        mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String raw = listTB.getSelectedItem().toString();
                                String [] matuyenbay = raw.split(":");
                                Cursor cursor = DB.checkTuyenBayCB(matuyenbay[0]);
                                if (cursor.getCount() == 0) {
                                    boolean check = DB.deleteTuyenBay(matuyenbay[0]);
                                    if (check) {
                                        Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                    cursor.close();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Xóa thất bại, tuyến bay này đang tồn tại trong dữ liệu, không thể xóa", Toast.LENGTH_SHORT).show();
                                    cursor.close();
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
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                mydialog.setView(mView);
                mydialog.show();
            }
        });
        mngAirport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.mngairport, null);
                mydialog.setTitle("Lựa chọn hạng mục");
                AddAirport = mView.findViewById(R.id.btn_mng_addairport);
                deleteAirport = mView.findViewById(R.id.btn_mng_deleteairport);
                updateAirport = mView.findViewById(R.id.btn_mng_updateairport);
                AddAirport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.addairport, null);
                        mydialog.setTitle("Nhập thông tin sân bay");
                        MaSanBay = mView.findViewById(R.id.ed_masanbay);
                        TenSanBay = mView.findViewById(R.id.ed_tensanbay);
                        Tinh = mView.findViewById(R.id.ed_tinh);
                        Nuoc = mView.findViewById(R.id.ed_nuoc);
                        mydialog.setView(mView);
                        Cursor cursor = DB.getalldataairport();
                        if (cursor.getCount()==0)
                        {
                            MaSanBay.setText("SB1");
                            cursor.close();
                        }
                        else
                        {
                            cursor.moveToPosition(cursor.getCount()-1);
                            String raw = cursor.getString(0);
                            String split[] = raw.split("SB");
                            int a = Integer.parseInt(split[1]);
                            MaSanBay.setText("SB"+String.valueOf(a+1));
                            cursor.close();
                        }
                        mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Cursor c = DB.getalldataairport();
                                if (MaSanBay.getText().toString().isEmpty() || TenSanBay.getText().toString().isEmpty() || Tinh.getText().toString().isEmpty() || Nuoc.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getContext(), "Thiếu thông tin!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {

                                    while (c.moveToNext())
                                    {
                                        if (MaSanBay.getText().toString().equals(c.getString(0)) || TenSanBay.getText().toString().equals(c.getString(1)))
                                        {
                                            Toast.makeText(getContext(), "Tạo thất bại, đã tồn tại mã hoặc tên sân bay!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    c.close();
                                    Boolean res = DB.AddAirport(MaSanBay.getText().toString(), TenSanBay.getText().toString(), Tinh.getText().toString(), Nuoc.getText().toString());
                                    if (res )
                                    {
                                        Toast.makeText(getContext(), "Tạo thành công", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "Tạo thất bại, đã tồn tại mã hoặc tên sân bay!", Toast.LENGTH_SHORT).show();
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
                deleteAirport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.deleteairport, null);
                        mydialog.setTitle("Chọn sân bay cần xóa");
                        final Spinner listSanBay = mView.findViewById(R.id.spin_mng_deleteairport);
                        ArrayList<String> Airport;
                        String listAirport[];
                        Cursor c = DB.getalldataairport();
                        listAirport = new String[c.getCount()];
                        int i = 0;
                        while (c.moveToNext()) {
                            listAirport[i] = c.getString(0) + ": "+ c.getString(1)+", "+c.getString(2)+"-"+c.getString(3);
                            i++;
                        }
                        c.close();
                        ArrayAdapter<String> sbdi = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, listAirport);
                        listSanBay.setAdapter(sbdi);
                        mydialog.setView(mView);
                        mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String raw = listSanBay.getSelectedItem().toString();
                                String [] masanbay = raw.split(":");
                                Cursor cursor = DB.checkAirportTB(masanbay[0]);
                                Cursor cursor1 = DB.checkAirportTG(masanbay[0]);
                                if (cursor.getCount() == 0 && cursor1.getCount() == 0) {
                                    boolean check = DB.deleteAirport(masanbay[0]);
                                    if (check) {
                                        Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                    cursor.close();
                                    cursor1.close();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Xóa thất bại, sân bay này đang tồn tại trong dữ liệu, không thể xóa", Toast.LENGTH_SHORT).show();
                                    cursor.close();
                                    cursor1.close();
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
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                updateAirport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.updateairport, null);
                        mydialog.setTitle("Cập nhật thông tin sân bay");
                        final EditText ten;
                        final EditText ma;
                        final EditText tinh;
                        final EditText nuoc;
                        ma = mView.findViewById(R.id.ed_updateairport_masanbay);
                        ten = mView.findViewById(R.id.ed_updateairport_tensanbay);
                        tinh = mView.findViewById(R.id.ed_updateairport_tinh);
                        nuoc = mView.findViewById(R.id.ed_updateairport_nuoc);
                        final Spinner listSanBay = mView.findViewById(R.id.spin_updateairport_listairport);
                        ArrayList<String> Airport;
                        String listAirport[];
                        final Cursor c = DB.getalldataairport();
                        listAirport = new String[c.getCount()];
                        int i = 0;
                        while (c.moveToNext()) {
                            listAirport[i] = c.getString(0) + ": "+ c.getString(1)+", "+c.getString(2)+"-"+c.getString(3);
                            i++;
                        }
                        c.close();
                        ArrayAdapter<String> sbdi = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, listAirport);
                        listSanBay.setAdapter(sbdi);
                        listSanBay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String raw = listSanBay.getSelectedItem().toString();
                                String [] need = raw.split(": ");
                                ma.setText(need[0]);
                                String need2[] = need[1].split(", ");
                                ten.setText(need2[0]);
                                String need3[] = need2[1].split("-");
                                tinh.setText(need3[0]);
                                nuoc.setText(need3[1]);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        mydialog.setView(mView);
                        mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               boolean check = DB.updateAirport(ma.getText().toString(), ten.getText().toString(), tinh.getText().toString(), nuoc.getText().toString());
                               if (check)
                               {
                                   Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                               }
                               else
                               {
                                   Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
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
                mydialog.setView(mView);
                mydialog.show();
            }
        });
        mngTicketLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.mngticketlevel, null);
                mydialog.setTitle("Lựa chọn hạng mục");
                AddTicketLevel = mView.findViewById(R.id.btn_mng_addticketlevel);
                deleteTicketLevel = mView.findViewById(R.id.btn_mng_deleteticketlevel);
                updateTicketLevel = mView.findViewById(R.id.btn_mng_updateticketlevel);
                AddTicketLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.addticketlevel, null);
                        mydialog.setTitle("Nhập thông tin hạng vé");
                        Mahang = mView.findViewById(R.id.ed_mahang);
                        TenHang = mView.findViewById(R.id.ed_tenhang);
                        TiLe = mView.findViewById(R.id.ed_chietkhau);
                        mydialog.setView(mView);
                        Cursor cursor = DB.getLevelTicket();
                        if (cursor.getCount()==0)
                        {
                            Mahang.setText("HV1");
                            cursor.close();
                        }
                        else
                        {
                            cursor.moveToPosition(cursor.getCount()-1);
                            String raw = cursor.getString(0);
                            String split[] = raw.split("HV");
                            int a = Integer.parseInt(split[1]);
                            Mahang.setText("HV"+String.valueOf(a+1));
                            cursor.close();
                        }
                        mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Cursor c = DB.getLevelTicket();
                                //dấu ngoặc thừa, nếu bị bug thì thêm lại
                                    if (Mahang.getText().toString().isEmpty() || TenHang.getText().toString().isEmpty() || TiLe.getText().toString().isEmpty() )
                                    {
                                        Toast.makeText(getContext(), "Thiếu thông tin", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        while (c.moveToNext())
                                        {
                                            if (Mahang.getText().toString().equals(c.getString(0)) || TenHang.getText().toString().equals(c.getString(1)))
                                            {
                                                Toast.makeText(getContext(), "Thêm thất bại, đã tồn tại hạng vé", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                        c.close();
                                        Boolean res = DB.AddTicketLevel(Mahang.getText().toString(), TenHang.getText().toString(), TiLe.getText().toString());
                                        if (res )
                                        {
                                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getContext(), "Thêm thất bại, đã tồn tại hạng vé", Toast.LENGTH_SHORT).show();
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
                deleteTicketLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.deleteticketlevel, null);
                        mydialog.setTitle("Chọn hạng vé cần xóa");
                        final Spinner listVe = mView.findViewById(R.id.spin_mng_deleteticketlevel);
                        ArrayList<String> hangve;
                        final String listhangve[];
                        Cursor c = DB.getLevelTicket();
                        listhangve = new String[c.getCount()];
                        int i = 0;
                        while (c.moveToNext()) {
                            listhangve[i] = c.getString(0) + ": "+ c.getString(1) + " | Tỉ lệ: "+ c.getString(2);
                            i++;
                        }
                        c.close();
                        ArrayAdapter<String> sbdi = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, listhangve);
                        listVe.setAdapter(sbdi);
                        mydialog.setView(mView);
                        mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String raw = listVe.getSelectedItem().toString();
                                String [] mahangve = raw.split(": ");
                                Cursor cursor = DB.checkHangVeGuest(mahangve[0]);
                                Cursor cursor1 = DB.checkHangVeSLV(mahangve[0]);
                                if (cursor.getCount() == 0 && cursor1.getCount() == 0) {
                                    boolean check = DB.deleteTicketlevel(mahangve[0]);
                                    if (check) {
                                        Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                    cursor.close();
                                    cursor1.close();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Xóa thất bại, đã tồn tại hạng vé này trong dữ liệu", Toast.LENGTH_SHORT).show();
                                    cursor.close();
                                    cursor1.close();
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
                updateTicketLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.updateticketlevel, null);
                        mydialog.setTitle("Cập nhật thông tin hạng vé");
                        final EditText ten;
                        final EditText ma;
                        final EditText TiLe;
                        ma = mView.findViewById(R.id.ed_updateticket_mahangve);
                        ten = mView.findViewById(R.id.ed_updateticket_tenhangve);
                        TiLe =  mView.findViewById(R.id.ed_updateticket_chietkhau);
                        final Spinner listHangve = mView.findViewById(R.id.spin_updateticket_listticketlevel);
                        ArrayList<String> Airport;
                        String list[];
                        final Cursor c = DB.getLevelTicket();
                        list = new String[c.getCount()];
                        int i = 0;
                        while (c.moveToNext()) {
                            list[i] = c.getString(0) + ": "+ c.getString(1) + " | Tỉ lệ: "+ c.getString(2);
                            i++;
                        }
                        c.close();
                        ArrayAdapter<String> sbdi = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, list);
                        listHangve.setAdapter(sbdi);
                        listHangve.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String raw = listHangve.getSelectedItem().toString();
                                String [] need = raw.split(": ");
                                ma.setText(need[0]);
                                ten.setText(need[1].split(" \\| ")[0]);
                                TiLe.setText(raw.split(": ")[2]);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        mydialog.setView(mView);
                        mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean check = DB.updateTicketlevel(ma.getText().toString(), ten.getText().toString(), TiLe.getText().toString());
                                if (check)
                                {
                                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
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
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                mydialog.setView(mView);
                mydialog.show();
            }
        });

        changeSecurityCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText oldPass;
                final EditText newPass;
                final EditText confirmPass;
                final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.changesecurecode, null);
                mydialog.setTitle("Thay đổi mã bảo mật mới");
                oldPass = mView.findViewById(R.id.ed_changesecurecode_oldpass);
                newPass = mView.findViewById(R.id.ed_changesecurecode_newpass);
                confirmPass = mView.findViewById(R.id.ed_changesecurecode_confirmnewpass);
                mydialog.setView(mView);
                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Cursor c = DB.getRule();
                        String old = null;
                        while (c.moveToNext())
                        {
                            old = c.getString(5);
                        }
                        c.close();
                        if (!oldPass.getText().toString().equals(old))
                        {
                            Toast.makeText(getContext(), "Sai mã bảo mật!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if (!newPass.getText().toString().equals(confirmPass.getText().toString()))
                            {
                                Toast.makeText(getContext(), "Mật mã xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                boolean check = DB.changeSecuritycode(newPass.getText().toString());
                                if (check)
                                {
                                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                }
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
        ChangeMaxIntermidiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.changequantityofintermidiate, null);
                mydialog.setTitle("Nhập số lượng sân bay trung gian tối đa");
                NewIntermidiate = mView.findViewById(R.id.ed_newintermidiate);;
                mydialog.setView(mView);
                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (NewIntermidiate.getText().toString().isEmpty())
                        {
                            Toast.makeText(getContext(), "Thiếu thông tin", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Boolean res = DB.ChangeQuantityOfIntermidiate(String.valueOf(NewIntermidiate.getText()));
                            if (res) {
                                Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();

                                ft.detach(ChangeRules.this).attach(ChangeRules.this).commit();
                            } else {
                                Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
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
        ChangeMinMaxDelayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.changeminmaxdelaytime, null);
                mydialog.setTitle("Nhập thời gian chờ tối thiểu và tối đa");
                MinDelayTime = mView.findViewById(R.id.ed_mindelay);
                MaxDelayTime = mView.findViewById(R.id.ed_maxdelay);
                mydialog.setView(mView);
                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (MinDelayTime.getText().toString().isEmpty() || MaxDelayTime.getText().toString().isEmpty())
                        {
                            Toast.makeText(getContext(), "Thiếu thông tin!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if (Integer.parseInt(MaxDelayTime.getText().toString())<Integer.parseInt(MinDelayTime.getText().toString()))
                            {
                                Toast.makeText(getContext(), "Thời gian tối đa phải lơn hơn tối thiểu", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Boolean check = DB.ChangeMinMaxDelay(MinDelayTime.getText().toString(), MaxDelayTime.getText().toString());
                                if (check) {
                                    Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                                    ft.detach(ChangeRules.this).attach(ChangeRules.this).commit();
                                } else {
                                    Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                                }
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
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean c = DB.CreateDefaultRule();
                if (c)
                {
                    Toast.makeText(getContext(), "Reset thành công!", Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    ft.detach(ChangeRules.this).attach(ChangeRules.this).commit();
                }
                else
                {
                    Toast.makeText(getContext(), "Reset thất bại!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ChangeBookCancelTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.changebookcanceltime, null);
                mydialog.setTitle("Thay đổi thời gian chậm trễ đặt và hủy vé");
                LatestDayBook = mView.findViewById(R.id.ed_latestdaybook);
                LatestDayCancel = mView.findViewById(R.id.ed_latestdaycancel);
                mydialog.setView(mView);
                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (LatestDayBook.getText().toString().isEmpty() || LatestDayCancel.getText().toString().isEmpty())
                        {
                            Toast.makeText(getContext(), "Thiếu thông tin!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Boolean check = DB.UpdateCancelDelete(LatestDayBook.getText().toString(), LatestDayCancel.getText().toString());
                            if (check) {
                                Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();

                                ft.detach(ChangeRules.this).attach(ChangeRules.this).commit();
                            } else {
                                Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
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
        ChangeMinFlyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mydialog = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.changeminflytime, null);
                mydialog.setTitle("Thay đổi thời gian bay tối thiếu");
                MinFlyTime = mView.findViewById(R.id.ed_newflytime);
                mydialog.setView(mView);
                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (MinFlyTime.getText().toString().isEmpty())
                        {
                            Toast.makeText(getContext(), "Chưa nhập thông tin!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Boolean res = DB.ChangeMinFlyTime(MinFlyTime.getText().toString());
                            if (res)
                            {
                                Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();

                                ft.detach(ChangeRules.this).attach(ChangeRules.this).commit();
                            }
                            else  Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
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
        return v;

    }
}

