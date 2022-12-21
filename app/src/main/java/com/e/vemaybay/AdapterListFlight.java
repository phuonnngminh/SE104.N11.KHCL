package com.e.vemaybay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdapterListFlight extends RecyclerView.Adapter<AdapterListFlight.ViewHolder> {
    private LayoutInflater layoutInflater;
    private DBHelper DB;
    private String sanBayDi;
    private String sanBayDen;
    private String startTime;
    private Fragment a;
    private int temp=0;
    AdapterListFlight(Context context, DBHelper DB, String sanBayDi, String sanBayDen, String startTime, Fragment a)
    {
        this.layoutInflater = LayoutInflater.from(context);
        this.DB = DB;
        this.sanBayDen = sanBayDen;
        this.sanBayDi = sanBayDi;
        this.startTime = startTime;
        this.a = a;
    }
    @NonNull
    @Override
    public AdapterListFlight.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.infoflight,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListFlight.ViewHolder holder, int position) {
        String sbdi = null;
        String sbden = null;
        Cursor cursorr1 = DB.getAirportByName(sanBayDi);
        Cursor cursorr2 = DB.getAirportByName(sanBayDen);
        String madi = "null";
        String maden = "null";
        while (cursorr1.moveToNext())
        {
            madi = cursorr1.getString(0);
        }
        cursorr1.close();
        while (cursorr2.moveToNext())
        {
            maden = cursorr2.getString(0);
        }
        cursorr2.close();
        int i = 0;
        if (sanBayDi.equals("Không chọn") && sanBayDen.equals("Không chọn") ) {
            if (startTime.isEmpty()) {
                Cursor cursor = DB.getallFlight();
                while (cursor.moveToNext()) {
                    if (i > position) {
                        break;
                    } else {
                        Cursor cursor1 = DB.GetNameAirport(cursor.getString(2));
                        while (cursor1.moveToNext())
                        {
                            sbdi = cursor1.getString(1)+ " ("+cursor1.getString(2)+", "+cursor1.getString(3)+")";
                        }
                        cursor1.close();
                        Cursor cursor2 = DB.GetNameAirport(cursor.getString(3));
                        while (cursor2.moveToNext())
                        {
                            sbden = cursor2.getString(1)+ " ("+cursor2.getString(2)+", "+cursor2.getString(3)+")";
                        }
                        cursor2.close();

                            String time = null;
                            Cursor cursor4 = DB.getFlight(cursor.getString(0));
                            while (cursor4.moveToNext())
                            {
                                time = cursor4.getString(4);
                            }
                            cursor4.close();
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
                            if (total<holder.soon) {
                                holder.bookNow.setVisibility(View.GONE);
                            }
                            else
                            {
                                holder.bookNow.setVisibility(View.VISIBLE);
                            }
                        holder.maChuyenBay.setText("Mã chuyến bay: " + cursor.getString(0));
                        holder.thoiGianBay.setText("Thời gian bay: " + cursor.getString(5) + " phút");
                        holder.sanBayDen.setText("Sân bay đến: " + sbden);
                        holder.batDau.setText("Bắt đầu: " + cursor.getString(4));
                        holder.sanBayDi.setText("Sân bay đi: " + sbdi);
                        holder.giaVe.setText("Giá Vé: " + cursor.getString(1));
                    }
                    i++;
                }
                cursor.close();
            }
            else {
                Cursor cursor = DB.getallFlight();
                while (cursor.moveToNext()) {
                    if (i > position) {
                        break;
                    } else {
                        String split[] = cursor.getString(4).split(" ");
                        String starttime = split[0];
                        if (starttime.equals(startTime)) {
                            Cursor cursor1 = DB.GetNameAirport(cursor.getString(2));
                            while (cursor1.moveToNext())
                            {
                                sbdi = cursor1.getString(1)+ " ("+cursor1.getString(2)+", "+cursor1.getString(3)+")";
                            }
                            cursor1.close();
                            Cursor cursor2 = DB.GetNameAirport(cursor.getString(3));
                            while (cursor2.moveToNext())
                            {
                                sbden = cursor2.getString(1)+ " ("+cursor2.getString(2)+", "+cursor2.getString(3)+")";
                            }
                            cursor2.close();
                                String time = null;
                                Cursor cursor4 = DB.getFlight(cursor.getString(0));
                                while (cursor4.moveToNext())
                                {
                                    time = cursor4.getString(4);
                                }
                                cursor4.close();
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
                                if (total<holder.soon) {
                                    holder.bookNow.setVisibility(View.GONE);
                                }
                                else
                                {
                                    holder.bookNow.setVisibility(View.VISIBLE);
                                }

                            holder.maChuyenBay.setText("Mã chuyến bay: " + cursor.getString(0));
                            holder.thoiGianBay.setText("Thời gian bay: " + cursor.getString(5) + " phút");
                            holder.sanBayDen.setText("Sân bay đến: " + sbden);
                            holder.batDau.setText("Bắt đầu: " + cursor.getString(4));
                            holder.sanBayDi.setText("Sân bay đi: " + sbdi);
                            holder.giaVe.setText("Giá Vé: " + cursor.getString(1));
                            i++;
                        }
                    }

                }
                cursor.close();
            }
        }
        else
        {
            Cursor cursor = DB.getallFlight();
            while (cursor.moveToNext()) {
                if (i > position) {
                    break;
                } else {
                    if (!sanBayDi.equals("Không chọn") && !sanBayDen.equals("Không chọn")) {

                        if (startTime.isEmpty()) {
                            String split[] = cursor.getString(4).split(" ");
                            String starttime = split[0];
                            Cursor cursor1 = DB.GetNameAirport(cursor.getString(2));
                            while (cursor1.moveToNext())
                            {
                                sbdi = cursor1.getString(1)+ " ("+cursor1.getString(2)+", "+cursor1.getString(3)+")";
                            }
                            cursor1.close();
                            Cursor cursor2 = DB.GetNameAirport(cursor.getString(3));
                            while (cursor2.moveToNext())
                            {
                                sbden = cursor2.getString(1)+ " ("+cursor2.getString(2)+", "+cursor2.getString(3)+")";
                            }
                            cursor2.close();
                                String time = null;
                                Cursor cursor4 = DB.getFlight(cursor.getString(0));
                                while (cursor4.moveToNext())
                                {
                                    time = cursor4.getString(4);
                                }
                                cursor4.close();
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
                                if (total<holder.soon) {
                                    holder.bookNow.setVisibility(View.GONE);
                                }
                                else
                                {
                                    holder.bookNow.setVisibility(View.VISIBLE);
                                }


                            if (maden.equals(cursor.getString(3)) && madi.equals(cursor.getString(2))) {
                                holder.maChuyenBay.setText("Mã chuyến bay: " + cursor.getString(0));
                                holder.thoiGianBay.setText("Thời gian bay: " + cursor.getString(5) + " phút");
                                holder.sanBayDen.setText("Sân bay đến: " + sbden);
                                holder.batDau.setText("Bắt đầu: " + cursor.getString(4));
                                holder.sanBayDi.setText("Sân bay đi: " + sbdi);
                                holder.giaVe.setText("Giá Vé: " + cursor.getString(1));
                                i++;
                            }
                        }
                        else
                        {
                            String split[] = cursor.getString(4).split(" ");
                            String starttime = split[0];
                            Cursor cursor1 = DB.GetNameAirport(cursor.getString(2));
                            while (cursor1.moveToNext())
                            {
                                sbdi = cursor1.getString(1)+ " ("+cursor1.getString(2)+", "+cursor1.getString(3)+")";
                            }
                            cursor1.close();
                            Cursor cursor2 = DB.GetNameAirport(cursor.getString(3));
                            while (cursor2.moveToNext())
                            {
                                sbden = cursor2.getString(1)+ " ("+cursor2.getString(2)+", "+cursor2.getString(3)+")";
                            }
                            cursor2.close();
                                String time = null;
                                Cursor cursor4 = DB.getFlight(cursor.getString(0));
                                while (cursor4.moveToNext())
                                {
                                    time = cursor4.getString(4);
                                }
                                cursor4.close();
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
                                if (total<holder.soon) {
                                    holder.bookNow.setVisibility(View.GONE);
                                }
                                else
                                {
                                    holder.bookNow.setVisibility(View.VISIBLE);
                                }


                            if (maden.equals(cursor.getString(3)) && madi.equals(cursor.getString(2)) && starttime.equals(startTime)) {
                                holder.maChuyenBay.setText("Mã chuyến bay: " + cursor.getString(0));
                                holder.thoiGianBay.setText("Thời gian bay: " + cursor.getString(5) + " phút");
                                holder.sanBayDen.setText("Sân bay đến: " + sbden);
                                holder.batDau.setText("Bắt đầu: " + cursor.getString(4));
                                holder.sanBayDi.setText("Sân bay đi: " + sbdi);
                                holder.giaVe.setText("Giá Vé: " + cursor.getString(1));
                                i++;
                            }
                        }
                    }
                    else if (sanBayDi.equals("Không chọn") || sanBayDen.equals("Không chọn"))
                    {
                        if (startTime.isEmpty()) {
                            String split[] = cursor.getString(4).split(" ");
                            String starttime = split[0];
                            if (maden.equals(cursor.getString(3)) || madi.equals(cursor.getString(2))) {
                                Cursor cursor1 = DB.GetNameAirport(cursor.getString(2));
                                while (cursor1.moveToNext())
                                {
                                    sbdi = cursor1.getString(1)+ " ("+cursor1.getString(2)+", "+cursor1.getString(3)+")";
                                }
                                cursor1.close();
                                Cursor cursor2 = DB.GetNameAirport(cursor.getString(3));
                                while (cursor2.moveToNext())
                                {
                                    sbden = cursor2.getString(1)+ " ("+cursor2.getString(2)+", "+cursor2.getString(3)+")";
                                }
                                cursor2.close();
                                    String time = null;
                                    Cursor cursor4 = DB.getFlight(cursor.getString(0));
                                    while (cursor4.moveToNext())
                                    {
                                        time = cursor4.getString(4);
                                    }
                                    cursor4.close();
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
                                    if (total<holder.soon) {
                                        holder.bookNow.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        holder.bookNow.setVisibility(View.VISIBLE);
                                    }
                                holder.maChuyenBay.setText("Mã chuyến bay: " + cursor.getString(0));
                                holder.thoiGianBay.setText("Thời gian bay: " + cursor.getString(5) + " phút");
                                holder.sanBayDen.setText("Sân bay đến: " + sbden);
                                holder.batDau.setText("Bắt đầu: " + cursor.getString(4));
                                holder.sanBayDi.setText("Sân bay đi: " + sbdi);
                                holder.giaVe.setText("Giá Vé: " + cursor.getString(1));
                                i++;
                            }
                        }
                        else
                        {
                            String split[] = cursor.getString(4).split(" ");
                            String starttime = split[0];
                            if ((maden.equals(cursor.getString(3)) || madi.equals(cursor.getString(2))) && starttime.equals(startTime)) {
                                if (starttime.equals(startTime)) {
                                    Cursor cursor1 = DB.GetNameAirport(cursor.getString(2));
                                    while (cursor1.moveToNext())
                                    {
                                        sbdi = cursor1.getString(1)+ " ("+cursor1.getString(2)+", "+cursor1.getString(3)+")";
                                    }
                                    cursor1.close();
                                    Cursor cursor2 = DB.GetNameAirport(cursor.getString(3));
                                    while (cursor2.moveToNext())
                                    {
                                        sbden = cursor2.getString(1)+ " ("+cursor2.getString(2)+", "+cursor2.getString(3)+")";
                                    }
                                    cursor2.close();
                                        String time = null;
                                        Cursor cursor4 = DB.getFlight(cursor.getString(0));
                                        while (cursor4.moveToNext())
                                        {
                                            time = cursor4.getString(4);
                                        }
                                        cursor4.close();
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
                                        if (total<holder.soon) {
                                            holder.bookNow.setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            holder.bookNow.setVisibility(View.VISIBLE);
                                        }
                                    holder.maChuyenBay.setText("Mã chuyến bay: " + cursor.getString(0));
                                    holder.thoiGianBay.setText("Thời gian bay: " + cursor.getString(5) + " phút");
                                    holder.sanBayDen.setText("Sân bay đến: " +sbden);
                                    holder.batDau.setText("Bắt đầu: " + cursor.getString(4));
                                    holder.sanBayDi.setText("Sân bay đi: " + sbdi);
                                    holder.giaVe.setText("Giá Vé: " + cursor.getString(1));
                                    i++;
                                }
                            }
                        }

                    }
                }

            }
            cursor.close();
        }
    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (sanBayDi.equals("Không chọn") && sanBayDen.equals("Không chọn") )
        {
            if (startTime.isEmpty()) {
                Cursor cursor = DB.getallFlight();
                return cursor.getCount();
            }
            else
            {
                Cursor cursor = DB.getallFlight();
                while (cursor.moveToNext()) {
                    String split[] = cursor.getString(4).split(" ");
                    String starttime = split[0];
                    if (starttime.equals(startTime)) {
                        i++;
                    }

                }
                cursor.close();
            }
        }

        else {
            Cursor cursorr1 = DB.getAirportByName(sanBayDi);
            Cursor cursorr2 = DB.getAirportByName(sanBayDen);
            String madi = "null";
            String maden = "null";
            while (cursorr1.moveToNext())
            {
                madi = cursorr1.getString(0);
            }
            cursorr1.close();
            while (cursorr2.moveToNext())
            {
                maden = cursorr2.getString(0);
            }
            cursorr2.close();
            if (!sanBayDi.equals("Không chọn") && !sanBayDen.equals("Không chọn")) {
                if (startTime.isEmpty()) {
                    Cursor cursor = DB.getallFlight();
                    while (cursor.moveToNext()) {
                        if (maden.equals(cursor.getString(3)) && madi.equals(cursor.getString(2)) ) {
                            i++;
                        }

                    }
                    cursor.close();
                }
                else
                {
                    Cursor cursor = DB.getallFlight();
                    while (cursor.moveToNext()) {
                        String split[] = cursor.getString(4).split(" ");
                        String starttime = split[0];
                        if (maden.equals(cursor.getString(3)) && madi.equals(cursor.getString(2))  && starttime.equals(startTime)) {
                            i++;
                        }

                    }
                    cursor.close();
                }
            }
            if (sanBayDi.equals("Không chọn") || sanBayDen.equals("Không chọn")) {
                if (startTime.isEmpty()) {
                    Cursor cursor = DB.getallFlight();
                    while (cursor.moveToNext()) {
                        if (maden.equals(cursor.getString(3)) || madi.equals(cursor.getString(2)) ) {
                            i++;
                        }

                    }
                    cursor.close();
                }
                else
                {
                    Cursor cursor = DB.getallFlight();
                    while (cursor.moveToNext()) {
                        String split[] = cursor.getString(4).split(" ");
                        String starttime = split[0];
                        if (maden.equals(cursor.getString(3)) || madi.equals(cursor.getString(2)) ) {
                            if (startTime.equals(starttime))
                            {
                                i++;
                            }

                        }

                    }
                    cursor.close();
                }
            }

        }
        return i;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView maChuyenBay;
        TextView giaVe;
        TextView sanBayDi;
        TextView sanBayDen;
        TextView batDau;
        TextView thoiGianBay;
        Button seeInter;
        Button seeTicket;
        int soon;
        Button bookNow;
        ViewPager vp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            maChuyenBay = itemView.findViewById(R.id.txt_code_flight);
            giaVe = itemView.findViewById(R.id.txt_cost_flight);
            sanBayDi = itemView.findViewById(R.id.txt_from_flight);
            sanBayDen = itemView.findViewById(R.id.txt_arrive_flight);
            batDau = itemView.findViewById(R.id.txt_time_flight);
            thoiGianBay = itemView.findViewById(R.id.txt_flytime_flight);
            seeInter = itemView.findViewById(R.id.btn_moreinfo_inter);
            seeTicket = itemView.findViewById(R.id.btn_moreinfo_hangve);
            bookNow = itemView.findViewById(R.id.btn_moreinfo_booknow);
            bookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vp=(ViewPager) a.getActivity().findViewById(R.id.view_pager);

                    MainActivity b = (MainActivity) a.getActivity();
                    b.setMCBBook(maChuyenBay.getText().toString().split(": ")[1]);
                    vp.setCurrentItem(2);
                }
            });
            Cursor cursor3 = DB.getRule();
            while (cursor3.moveToNext())
            {
                soon= Integer.parseInt(cursor3.getString(6));
            }
            cursor3.close();
            seeTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuffer buffer = new StringBuffer();
                    String main = maChuyenBay.getText().toString();
                    String[] seperate = main.split(": ");
                    Cursor cursor = DB.GetTicketOfFlight(seperate[1]);
                    while (cursor.moveToNext())
                    {
                        String tenHangVe = null;
                        Cursor cursor1 = DB.GetInfoOfTicket(cursor.getString(0));
                        while (cursor1.moveToNext())
                        {
                            tenHangVe = cursor1.getString(1);
                        }
                        buffer.append("Tên hạng vé: "+tenHangVe+"\n");
                        buffer.append("Số lượng: "+cursor.getString(2)+"\n");
                        buffer.append("Còn lại: "+String.valueOf(Integer.parseInt(cursor.getString(2))- Integer.parseInt(cursor.getString(3)))+"\n\n");
                    }
                    cursor.close();
                    AlertDialog.Builder builder =new  AlertDialog.Builder(v.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Thông tin các hạng vé:");
                    builder.setMessage(buffer.toString());
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
            });
            seeInter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuffer buffer = new StringBuffer();
                    String main = maChuyenBay.getText().toString();
                    String[] seperate = main.split(": ");
                    Cursor cursor = DB.GetInterInfo(seperate[1]);
                    if (cursor.getCount() == 0)
                    {
                        buffer.append("Không có sân bay trung gian \n");
                        cursor.close();
                    }
                    else {
                        while (cursor.moveToNext()) {
                            String tenSanBay = null;
                            Cursor cursor1 = DB.GetNameAirport(cursor.getString(1));

                            while (cursor1.moveToNext()) {
                                tenSanBay = cursor1.getString(1);
                            }
                            cursor1.close();
                            buffer.append("Tên Sân bay: " + tenSanBay + "\n");
                            buffer.append("Thời gian dừng: " + cursor.getString(2) + " phút \n");
                            buffer.append("Ghi chú: " + cursor.getString(3) + "\n\n");
                        }
                        cursor.close();
                    }
                    AlertDialog.Builder builder =new  AlertDialog.Builder(v.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Thông tin các sân bay trung gian:");
                    builder.setMessage(buffer.toString());
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
    }
    public  long daysBetween(Date create, Date now)
    {
        long differennce = (create.getTime()-now.getTime())/86400000;
        return differennce;
    }
}
