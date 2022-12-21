package com.e.vemaybay;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.channels.FileLock;
import java.text.DecimalFormat;

public class AdapterStatistic extends RecyclerView.Adapter<AdapterStatistic.ViewHolder>{
    private LayoutInflater layoutInflater;
    private DBHelper DB;
    String typeSta;
    String month;
    String year;
    String monthyear;
    AdapterStatistic(Context context, DBHelper DB, String typeSta, String month, String year, String monthyear)
    {
        this.layoutInflater = LayoutInflater.from(context);
        this.DB = DB;
        this.typeSta = typeSta;
        this.month = month;
        this.year = year;
        this.monthyear = monthyear;
    }
    @NonNull
    @Override
    public AdapterStatistic.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.month_statistic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterStatistic.ViewHolder holder, int position) {
        int i=0;
        if (typeSta.equals("Theo tháng"))
        {
            Cursor c =DB.GetAllBaoCaoThang();
            while (c.moveToNext())
            {
                if (i>position)
                {
                    break;
                }
                else {
                    if ((Integer.parseInt(c.getString(1)) == Integer.parseInt(month)) && (Integer.parseInt(c.getString(2)) == Integer.parseInt(monthyear))) {
                        holder.stt.setText(String.valueOf(i + 1));
                        holder.chuyenbay.setText(c.getString(0));
                        holder.sove.setText(c.getString(3));
                        holder.doanhthu.setText(c.getString(4));
                        float dt = Float.parseFloat(c.getString(4));
                        float tl = (dt / holder.summonthyear) * 100;
                        holder.tile.setText(new DecimalFormat("##.##").format(tl) + "%");
                        i++;
                    }
                }
            }
            c.close();
        }
        if (typeSta.equals("Theo năm"))
        {
            Cursor cursor =DB.GetAllBaoCaoNam();
            while (cursor.moveToNext())
            {
                if (i>position)
                {
                    break;
                }
                else {
                    if (cursor.getString(1).equals(year))
                    {
                        holder.stt.setText(String.valueOf(i+1));
                        holder.chuyenbay.setText(cursor.getString(0));
                        holder.sove.setText(cursor.getString(2));
                        holder.doanhthu.setText(cursor.getString(3));
                        float dt = Float.parseFloat(cursor.getString(3));
                        float tl = (dt / holder.sumyear) * 100;
                        holder.tile.setText(new DecimalFormat("##.##").format(tl) + "%");
                        i++;
                    }
                }
            }
            cursor.close();
        }

    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (typeSta.equals("Theo tháng"))
        {
            Cursor c = DB.GetAllBaoCaoThang();
            while (c.moveToNext())
            {
                if ((Integer.parseInt(c.getString(1)) == Integer.parseInt(month)) && (Integer.parseInt(c.getString(2))==Integer.parseInt(monthyear)))
                    i++;
            }
            c.close();

        }
        if (typeSta.equals("Theo năm"))
        {
            int j = 1;
            while (j<13)
            {
                String searchMonth;
                if (j<10)
                {
                    searchMonth = "0" + String.valueOf(j);
                }
                else searchMonth = String.valueOf(j);
                Cursor cursor = DB.GetBaoCaoNam(searchMonth, year);
                i+=cursor.getCount();
                j++;
            }

        }

        return i;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        float summonthyear = 0;
        float sumyear = 0;
        TextView stt = itemView.findViewById(R.id.txt_month_stt);
        TextView chuyenbay = itemView.findViewById(R.id.txt_month_chuyenbay);
        TextView sove = itemView.findViewById(R.id.txt_month_sove);
        TextView doanhthu = itemView.findViewById(R.id.txt_month_doanhthu);
        TextView tile = itemView.findViewById(R.id.txt_month_tile);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Cursor c = DB.GetAllBaoCaoThang();
            try {

                while (c.moveToNext()) {
                    if ((Integer.parseInt(c.getString(1)) == Integer.parseInt(month)) && (Integer.parseInt(c.getString(2)) == Integer.parseInt(monthyear))) {
                        summonthyear += Float.parseFloat(c.getString(4));
                    }
                }
                c.close();
            } catch (NumberFormatException numberFormatException)
            {
                System.out.println("Error " + numberFormatException.getMessage());
            }
            Cursor cursor = DB.GetAllBaoCaoNam();
            while (cursor.moveToNext())
            {
                if (cursor.getString(1).equals(year))
                {
                    sumyear += Float.parseFloat(cursor.getString(3));
                }
            }
            cursor.close();
        }
    }
}
