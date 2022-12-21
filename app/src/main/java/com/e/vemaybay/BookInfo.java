package com.e.vemaybay;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;

public class BookInfo extends AppCompatActivity {
    EditText nameGuest;
    EditText idGuest;
    EditText phoneGuest;
    EditText giaGuest;
    Spinner listFlight;
    Spinner listTicket;
    String MaFlight;
    String MaTicket;
    Button Book;
    Button BookPay;
    TextView note;
    String date;
    private Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    int soon;
    EditText BookHelp;
    EditText SdtHelp;
    CheckBox isHelp;
    float finalVal = 0;
    String stringListFlight[];
    SwipeRefreshLayout swipeRefreshLayout;
    int giaVe=0;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookticket);
        DB = new DBHelper(this);
        nameGuest = findViewById(R.id.ed_name_guest);
        idGuest = findViewById(R.id.ed_id_guest);
        phoneGuest = findViewById(R.id.ed_phone_guest);
        giaGuest = findViewById(R.id.ed_money_guest);
        listFlight = findViewById(R.id.spin_bookticket_select_flight);
        listTicket =findViewById(R.id.spin_bookticket_selectticketlevel);
        BookHelp = findViewById(R.id.ed_name_book);
        SdtHelp = findViewById(R.id.ed_sdt_book);
        isHelp = findViewById(R.id.check_isHelp);
        Book = findViewById(R.id.btn_bookticket);
        BookPay = findViewById(R.id.btn_bookticket_pay);
        note = findViewById(R.id.tv_note);
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(BookInfo.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (idGuest.getText().toString().isEmpty() || nameGuest.getText().toString().isEmpty() || phoneGuest.getText().toString().isEmpty() || BookHelp.getText().toString().isEmpty() || SdtHelp.getText().toString().isEmpty()) {
                    Toast.makeText(BookInfo.this, "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookInfo.this, "Lỗi!, thiết bị không có nhận diện vân tay, hãy nhập mã bảo mật để tiếp tục", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder mydialog = new AlertDialog.Builder(BookInfo.this);
                    mydialog.setTitle("Nhập mã bảo mật");
                    final EditText secure = new EditText(BookInfo.this);
                    secure.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    mydialog.setView(secure);
                    mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean check = false;
                            Cursor c = DB.getRule();
                            while (c.moveToNext()) {
                                if (secure.getText().toString().equals(c.getString(5))) {
                                    ok();
                                    check = true;
                                }
                            }
                            c.close();
                            if (!check) {
                                Toast.makeText(BookInfo.this, "Sai mã bảo mật", Toast.LENGTH_SHORT).show();
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
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                ok();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Xác thực sinh trắc học").setSubtitle("Hãy đặt vân tay vào ô tròn").setNegativeButtonText("Dùng mật khẩu").build();
        swipeRefreshLayout = findViewById(R.id.swiperefreshbook);
        if (isHelp.isChecked())
        {
            BookHelp.setEnabled(true);
            SdtHelp.setEnabled(true);
        }
        else
        {
            BookHelp.setEnabled(false);
            SdtHelp.setEnabled(false);
        }
        isHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHelp.isChecked())
                {
                    BookHelp.setEnabled(true);
                    SdtHelp.setEnabled(true);
                }
                else
                {
                    BookHelp.setEnabled(false);
                    SdtHelp.setEnabled(false);
                    BookHelp.setText(nameGuest.getText().toString());
                    SdtHelp.setText(phoneGuest.getText().toString());
                }
            }
        });
        nameGuest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isHelp.isChecked())
                    BookHelp.setText(nameGuest.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneGuest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isHelp.isChecked())
                    SdtHelp.setText(phoneGuest.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Cursor cursor1 = DB.getRule();
        while (cursor1.moveToNext())
        {
            soon= Integer.parseInt(cursor1.getString(6));
        }
        cursor1.close();
        Cursor c = DB.getallFlight();
        String stringtemp[];
        stringtemp = new String[c.getCount()];
        note.setText("Kéo xuống để cập nhật thông tin các chuyến bay mới nhất. ");
        int i=0;
        while (c.moveToNext())
        {
            String time = null;
            Cursor cursor = DB.getFlight(c.getString(0));
            while (cursor.moveToNext())
            {
                time = cursor.getString(4);
            }
            cursor.close();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
            Date now = calendar.getTime();
            try {
                now = simpleDateFormat.parse(simpleDateFormat.format(now.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date create = null;
            try {
                create = simpleDateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long total = daysBetween(create, now) ;
            if (total>=soon) {
                Cursor cursorr1 = DB.GetNameAirport(c.getString(2));
                Cursor cursorr2 = DB.GetNameAirport(c.getString(3) );
                String madi = "null";
                String maden = "null";
                while (cursorr1.moveToNext())
                {
                    madi = cursorr1.getString(1);
                }
                cursorr1.close();
                while (cursorr2.moveToNext())
                {
                    maden = cursorr2.getString(1);
                }
                cursorr2.close();
                stringtemp[i] = c.getString(0) + ": "+madi+" -> "+maden ;
                i++;
            }

        }
        c.close();
        stringListFlight = new String[i];
        int u =0;
        while (u<i)
        {
            stringListFlight[u] = stringtemp[u];
            u++;
        }
        ArrayAdapter<String> flights = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,stringListFlight);
        listFlight.setAdapter(flights);
        listFlight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] separated = listFlight.getSelectedItem().toString().split(":");
                MaFlight = separated[0];
                giaVe = DB.GetGiaveOfFlight(separated[0]);
                Cursor cursor2 = DB.GetTimeOfFlight(separated[0]);
                while (cursor2.moveToNext())
                {
                    date = cursor2.getString(4);
                }
                cursor2.close();
                Cursor cursor = DB.GetTicketOfFlight(separated[0]);
                String stringListTicket[];
                stringListTicket = new String[cursor.getCount()];
                int j=0;
                while (cursor.moveToNext())
                {
                    Cursor cursor1 = DB.GetInfoOfTicket(cursor.getString(0));
                    while (cursor1.moveToNext()) {

                        stringListTicket[j] = cursor1.getString(1) + ": Tỉ lệ "+cursor1.getString(2)+"%";
                    }
                    cursor1.close();
                    j++;
                }
                cursor.close();
                ArrayAdapter<String> tickets = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,stringListTicket);
                listTicket.setAdapter(tickets);
                //listTicket.
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listTicket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] separated = listTicket.getSelectedItem().toString().split(":");
                String TenFlight = separated[0];
                Cursor cursor2 = DB.GetInfoOfTicketByName(TenFlight);
                while (cursor2.moveToNext())
                {
                    MaTicket = cursor2.getString(0);
                }
                cursor2.close();
                float chietkhau = giaVe;
                String percent = null;
                Cursor cursor = DB.GetInfoOfTicketByName(separated[0]);
                while (cursor.moveToNext())
                {
                    chietkhau = chietkhau/100*(Integer.parseInt(cursor.getString(2)));
                    percent = cursor.getString(2);
                }
                cursor.close();
                String val = String.format("%.0f", chietkhau);
                giaGuest.setText("Giá vé: " + val + " (="+giaVe+"*"+percent +"%)");
                finalVal = Float.parseFloat(val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                finish();
                startActivity(getIntent());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int val = 0;
                int max = 0;
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yy  HH:mm");
                Calendar calendar = Calendar.getInstance();
                Date now1 = calendar.getTime();
                try {
                    now1 = simpleDateFormat1.parse(simpleDateFormat1.format(now1.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String now = now1.toString();
                if (idGuest.getText().toString().isEmpty() || nameGuest.getText().toString().isEmpty() || phoneGuest.getText().toString().isEmpty() || BookHelp.getText().toString().isEmpty() || SdtHelp.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor cursor = DB.GetValueOfTicket(MaTicket, MaFlight);
                    while (cursor.moveToNext()) {
                        val = Integer.parseInt(cursor.getString(3));
                        max = Integer.parseInt(cursor.getString(2));
                    }
                    cursor.close();
                    if (val < max) {

                        String name = nameGuest.getText().toString();
                        String id = idGuest.getText().toString();
                        String phone = phoneGuest.getText().toString();
                        Cursor cursor1 = DB.GetInfoOfTicket(MaTicket);
                        Boolean check = DB.AddGuest(id, MaFlight, MaTicket, name, phone, "0", String.valueOf(finalVal), now,BookHelp.getText().toString(), SdtHelp.getText().toString());
                        if (check)
                        {
                            Toast.makeText(v.getContext(), "Đặt thành công ", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(v.getContext(), "Đặt thất bại, mỗi người chỉ đặt được 1 vé", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(v.getContext(), "Hết vé!", Toast.LENGTH_SHORT).show();

                    }
//
                }
            }
        });
        BookPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idGuest.getText().toString().isEmpty() || nameGuest.getText().toString().isEmpty() || phoneGuest.getText().toString().isEmpty() || BookHelp.getText().toString().isEmpty() || SdtHelp.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    biometricPrompt.authenticate(promptInfo);
                }

            }
        });
    }
    public void ok()
    {
        int val = 0;
        int max = 0;
        int empty = 0;
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yy HH:mm");
        Calendar calendar = Calendar.getInstance();
        Date now1 = calendar.getTime();
        try {
            now1 = simpleDateFormat1.parse(simpleDateFormat1.format(now1.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String now = now1.toString();
        if (idGuest.getText().toString().isEmpty() || nameGuest.getText().toString().isEmpty() || phoneGuest.getText().toString().isEmpty() || BookHelp.getText().toString().isEmpty() || SdtHelp.getText().toString().isEmpty()) {
            Toast.makeText(this, "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show();
        } else {
            Cursor cursor = DB.GetValueOfTicket(MaTicket, MaFlight);
            while (cursor.moveToNext()) {
                val = Integer.parseInt(cursor.getString(3));
                max = Integer.parseInt(cursor.getString(2));
                empty = Integer.parseInt(cursor.getString(4));
            }
            cursor.close();
            if (val < max) {

                String name = nameGuest.getText().toString();
                String id = idGuest.getText().toString();
                String phone = phoneGuest.getText().toString();
                String temp = listTicket.getSelectedItem().toString();
                String need[] = temp.split(": ");
                Cursor cursor1 = DB.GetInfoOfTicket(need[0]);
                Boolean check = DB.AddGuest(id, MaFlight, MaTicket, name, phone, "1","0", now, BookHelp.getText().toString(), SdtHelp.getText().toString());
                if (check) {
                    val += 1;
                    float beforeval = 0;
                    DB.UpdateValueTiicket(MaTicket, MaFlight, String.valueOf(val), String.valueOf(empty-1));
                    Toast.makeText(this, "Đặt thành công ", Toast.LENGTH_SHORT).show();
                    Cursor c = DB.getFlight(MaFlight);
                    while (c.moveToNext())
                    {
                        beforeval = Float.parseFloat(c.getString(6));
                    }
                    c.close();
                    beforeval = beforeval + finalVal;
                    boolean isUpdate = DB.updateDoanhThu(String.valueOf(beforeval), MaFlight);
                    boolean isUpdate1 = DB.updateDoanhThuThang(String.valueOf(beforeval), MaFlight);
                    Cursor cursor2 = DB.GetBaoCaoThang(MaFlight);
                    String thang = null;
                    String nam = null;
                    while (cursor2.moveToNext())
                    {
                        int val1;
                        val1 = Integer.parseInt(cursor2.getString(3));
                        val1++;
                        DB.updateSLVThang(String.valueOf(val1), MaFlight);
                        thang = cursor2.getString(1);
                        nam = cursor2.getString(2);
                    }
                    cursor2.close();
                    Cursor upNam = DB.GetBaoCaoNam(thang, nam);
                    while (upNam.moveToNext())
                    {
                        String a = upNam.getString(3);
                        float upd = finalVal + Float.parseFloat(a);
                        DB.updateDoanhThuNam(thang, nam, String.valueOf(upd));
                    }
                    upNam.close();
                }
                else
                {
                    Toast.makeText(this, "Đặt thất bại, mỗi người chỉ đặt được 1 vé", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Hết vé!", Toast.LENGTH_SHORT).show();

            }

        }
    }
    public  long daysBetween(Date create, Date now)
    {
        long differennce = (create.getTime()-now.getTime())/86400000;
        return differennce;
    }

}
