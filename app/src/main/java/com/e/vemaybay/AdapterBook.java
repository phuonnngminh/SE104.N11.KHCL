package com.e.vemaybay;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;

public class AdapterBook extends RecyclerView.Adapter<AdapterBook.ViewHolder> {
    private LayoutInflater layoutInflater;
    private DBHelper DB;
    private String MaChuyenBay;
    private String Ten;
    private String CMND;
    private String SDT;
    private Fragment a;
    Context context;
    AdapterBook (Context context, DBHelper DB, String MaChuyenBay, Fragment a, String Ten, String CMND, String SDT)
    {
        this.layoutInflater = LayoutInflater.from(context);
        this.DB = DB;
        this.MaChuyenBay = MaChuyenBay;
        this.context = context;
        this.Ten= Ten;
        this.CMND = CMND;
        this.SDT = SDT;
        this.a =a ;
    }
    @NonNull
    @Override
    public AdapterBook.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.bookinfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBook.ViewHolder holder, int position) {
        if (SDT.isEmpty()) {
            if (Ten.isEmpty() && CMND.isEmpty()) {
                int i = 0;
                Cursor c = DB.getCustomer(MaChuyenBay);
                while (c.moveToNext()) {
                    if (i > position) {
                        break;
                    } else {
                        holder.namehelp.setText("Tên người đặt: " + c.getString(8));
                        holder.sdthelp.setText("SDT người đặt: " + c.getString(9));
                        holder.id.setText("Số CMND: " + c.getString(0));
                        holder.name.setText("Hành khách: " + c.getString(3));
                        holder.phone.setText("Số điện thoại: " + c.getString(4));
                        String TenVe = null;
                        Cursor cursor = DB.GetInfoOfTicket(c.getString(2));
                        while (cursor.moveToNext()) {
                            TenVe = cursor.getString(1);
                        }
                        cursor.close();
                        holder.hangve.setText("Hạng vé: " + TenVe);
                        if (Integer.parseInt(c.getString(5)) == 1) {
                            holder.staticpay.setText("Trạng thái thanh toán: Đã thanh toán");
                            holder.datepay.setText("Ngày thanh toán: " + c.getString(7));
                            holder.pay.setVisibility(View.GONE);

                        } else if (Integer.parseInt(c.getString(5)) == 2) {
                            holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                            holder.datepay.setText("Ngày đặt: " + c.getString(7));
                            holder.pay.setVisibility(View.GONE);
                        } else {
                            if (holder.total1 <= holder.huyVe) {
                                holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                holder.pay.setVisibility(View.GONE);
                                holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                            } else {
                                Cursor cursor1 = DB.GetInfoOfTicketByName(TenVe);
                                String mave = null;
                                boolean soldout = false;
                                while (cursor1.moveToNext())
                                {
                                    mave = cursor1.getString(0);
                                }
                                cursor1.close();
                                Cursor cursor2 = DB.GetValueOfTicket(mave, MaChuyenBay);
                                while (cursor2.moveToNext())
                                {
                                    if (Integer.parseInt(cursor2.getString(4)) == 0)
                                        soldout = true;
                                }
                                cursor2.close();
                                if (soldout) {
                                    holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                    holder.pay.setVisibility(View.VISIBLE);
                                    holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                    holder.pay.setVisibility(View.GONE);
                                    DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                                }
                                else
                                {
                                    holder.staticpay.setText("Trạng thái thanh toán: Chưa thanh toán");
                                    holder.pay.setVisibility(View.VISIBLE);
                                    holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                }
                            }
                        }

                    }
                    i++;
                }
                c.close();
            } else if (!Ten.isEmpty() && !CMND.isEmpty()) {
                int i = 0;
                Cursor c = DB.getCustomer(MaChuyenBay);
                while (c.moveToNext()) {
                    if (i > position) {
                        break;
                    } else {
                        if (Ten.equals(c.getString(3)) && CMND.equals(c.getString(0))) {
                            holder.namehelp.setText("Tên người đặt: " + c.getString(8));
                            holder.sdthelp.setText("SDT người đặt: " + c.getString(9));
                            holder.id.setText("Số CMND: " + c.getString(0));
                            holder.name.setText("Hành khách: " + c.getString(3));
                            holder.phone.setText("Số điện thoại: " + c.getString(4));
                            String TenVe = null;
                            Cursor cursor = DB.GetInfoOfTicket(c.getString(2));
                            while (cursor.moveToNext()) {
                                TenVe = cursor.getString(1);
                            }
                            cursor.close();
                            holder.hangve.setText("Hạng vé: " + TenVe);
                            if (Integer.parseInt(c.getString(5)) == 1) {
                                holder.staticpay.setText("Trạng thái thanh toán: Đã thanh toán");
                                holder.datepay.setText("Ngày thanh toán: " + c.getString(7));
                                holder.pay.setVisibility(View.GONE);

                            } else if (Integer.parseInt(c.getString(5)) == 2) {
                                holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                holder.pay.setVisibility(View.GONE);
                            } else {
                                if (holder.total1 <= holder.huyVe) {
                                    holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                    holder.pay.setVisibility(View.GONE);
                                    holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                    DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                                } else {
                                    Cursor cursor1 = DB.GetInfoOfTicketByName(TenVe);
                                    String mave = null;
                                    boolean soldout = false;
                                    while (cursor1.moveToNext())
                                    {
                                        mave = cursor1.getString(0);
                                    }
                                    cursor1.close();
                                    Cursor cursor2 = DB.GetValueOfTicket(mave, MaChuyenBay);
                                    while (cursor2.moveToNext())
                                    {
                                        if (Integer.parseInt(cursor2.getString(4)) == 0)
                                            soldout = true;
                                    }
                                    cursor2.close();
                                    if (soldout) {
                                        holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                        holder.pay.setVisibility(View.VISIBLE);
                                        holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                        holder.pay.setVisibility(View.GONE);
                                        DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                                    }
                                    else
                                    {
                                        holder.staticpay.setText("Trạng thái thanh toán: Chưa thanh toán");
                                        holder.pay.setVisibility(View.VISIBLE);
                                        holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                    }
                                }
                            }
                            i++;
                        }

                    }
                }
                c.close();
            } else {
                int i = 0;
                Cursor c = DB.getCustomer(MaChuyenBay);
                while (c.moveToNext()) {
                    if (i > position) {
                        break;
                    } else {
                        if (Ten.equals(c.getString(3)) || CMND.equals(c.getString(0))) {
                            holder.namehelp.setText("Tên người đặt: " + c.getString(8));
                            holder.sdthelp.setText("SDT người đặt: " + c.getString(9));
                            holder.id.setText("Số CMND: " + c.getString(0));
                            holder.name.setText("Hành khách: " + c.getString(3));
                            holder.phone.setText("Số điện thoại: " + c.getString(4));
                            String TenVe = null;
                            Cursor cursor = DB.GetInfoOfTicket(c.getString(2));
                            while (cursor.moveToNext()) {
                                TenVe = cursor.getString(1);
                            }
                            cursor.close();
                            holder.hangve.setText("Hạng vé: " + TenVe);
                            if (Integer.parseInt(c.getString(5)) == 1) {
                                holder.staticpay.setText("Trạng thái thanh toán: Đã thanh toán");
                                holder.datepay.setText("Ngày thanh toán: " + c.getString(7));
                                holder.pay.setVisibility(View.GONE);

                            } else if (Integer.parseInt(c.getString(5)) == 2) {
                                holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                holder.pay.setVisibility(View.GONE);
                            } else {
                                if (holder.total1 <= holder.huyVe) {
                                    holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                    holder.pay.setVisibility(View.GONE);
                                    holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                    DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                                } else {
                                    Cursor cursor1 = DB.GetInfoOfTicketByName(TenVe);
                                    String mave = null;
                                    boolean soldout = false;
                                    while (cursor1.moveToNext())
                                    {
                                        mave = cursor1.getString(0);
                                    }
                                    cursor1.close();
                                    Cursor cursor2 = DB.GetValueOfTicket(mave, MaChuyenBay);
                                    while (cursor2.moveToNext())
                                    {
                                        if (Integer.parseInt(cursor2.getString(4)) == 0)
                                            soldout = true;
                                    }
                                    cursor2.close();
                                    if (soldout) {
                                        holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                        holder.pay.setVisibility(View.VISIBLE);
                                        holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                        holder.pay.setVisibility(View.GONE);
                                        DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                                    }
                                    else
                                    {
                                        holder.staticpay.setText("Trạng thái thanh toán: Chưa thanh toán");
                                        holder.pay.setVisibility(View.VISIBLE);
                                        holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                    }
                                }
                            }
                            i++;
                        }

                    }
                }
                c.close();
            }
        }
        else
        {
            if (Ten.isEmpty() && CMND.isEmpty()) {
                int i = 0;
                Cursor c = DB.getCustomer(MaChuyenBay);
                while (c.moveToNext()) {
                    if (i > position) {
                        break;
                    } else {
                        if (SDT.equals(c.getString(4))) {
                            holder.namehelp.setText("Tên người đặt: " + c.getString(8));
                            holder.sdthelp.setText("SDT người đặt: " + c.getString(9));
                            holder.id.setText("Số CMND: " + c.getString(0));
                            holder.name.setText("Hành khách: " + c.getString(3));
                            holder.phone.setText("Số điện thoại: " + c.getString(4));
                            String TenVe = null;
                            Cursor cursor = DB.GetInfoOfTicket(c.getString(2));
                            while (cursor.moveToNext()) {
                                TenVe = cursor.getString(1);
                            }
                            cursor.close();
                            holder.hangve.setText("Hạng vé: " + TenVe);
                            if (Integer.parseInt(c.getString(5)) == 1) {
                                holder.staticpay.setText("Trạng thái thanh toán: Đã thanh toán");
                                holder.datepay.setText("Ngày thanh toán: " + c.getString(7));
                                holder.pay.setVisibility(View.GONE);

                            } else if (Integer.parseInt(c.getString(5)) == 2) {
                                holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                holder.pay.setVisibility(View.GONE);
                            } else {
                                if (holder.total1 <= holder.huyVe) {
                                    holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                    holder.pay.setVisibility(View.GONE);
                                    holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                    DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                                } else {
                                    Cursor cursor1 = DB.GetInfoOfTicketByName(TenVe);
                                    String mave = null;
                                    boolean soldout = false;
                                    while (cursor1.moveToNext())
                                    {
                                        mave = cursor1.getString(0);
                                    }
                                    cursor1.close();
                                    Cursor cursor2 = DB.GetValueOfTicket(mave, MaChuyenBay);
                                    while (cursor2.moveToNext())
                                    {
                                        if (Integer.parseInt(cursor2.getString(4)) == 0)
                                            soldout = true;
                                    }
                                    cursor2.close();
                                    if (soldout) {
                                        holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                        holder.pay.setVisibility(View.VISIBLE);
                                        holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                        holder.pay.setVisibility(View.GONE);
                                        DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                                    }
                                    else
                                    {
                                        holder.staticpay.setText("Trạng thái thanh toán: Chưa thanh toán");
                                        holder.pay.setVisibility(View.VISIBLE);
                                        holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                    }
                                }
                            }
                            i++;
                        }

                    }

                }
                c.close();
            } else if (!Ten.isEmpty() && !CMND.isEmpty()) {
                int i = 0;
                Cursor c = DB.getCustomer(MaChuyenBay);
                while (c.moveToNext()) {
                    if (i > position) {
                        break;
                    } else {
                        if (SDT.equals(c.getString(4)) && Ten.equals(c.getString(3)) && CMND.equals(c.getString(0))) {
                            holder.namehelp.setText("Tên người đặt: " + c.getString(8));
                            holder.sdthelp.setText("SDT người đặt: " + c.getString(9));
                            holder.id.setText("Số CMND: " + c.getString(0));
                            holder.name.setText("Hành khách: " + c.getString(3));
                            holder.phone.setText("Số điện thoại: " + c.getString(4));
                            String TenVe = null;
                            Cursor cursor = DB.GetInfoOfTicket(c.getString(2));
                            while (cursor.moveToNext()) {
                                TenVe = cursor.getString(1);
                            }
                            cursor.close();
                            holder.hangve.setText("Hạng vé: " + TenVe);
                            if (Integer.parseInt(c.getString(5)) == 1) {
                                holder.staticpay.setText("Trạng thái thanh toán: Đã thanh toán");
                                holder.datepay.setText("Ngày thanh toán: " + c.getString(7));
                                holder.pay.setVisibility(View.GONE);

                            } else if (Integer.parseInt(c.getString(5)) == 2) {
                                holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                holder.pay.setVisibility(View.GONE);
                            } else {
                                if (holder.total1 <= holder.huyVe) {
                                    holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                    holder.pay.setVisibility(View.GONE);
                                    holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                    DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                                } else {
                                    Cursor cursor1 = DB.GetInfoOfTicketByName(TenVe);
                                    String mave = null;
                                    boolean soldout = false;
                                    while (cursor1.moveToNext())
                                    {
                                        mave = cursor1.getString(0);
                                    }
                                    cursor1.close();
                                    Cursor cursor2 = DB.GetValueOfTicket(mave, MaChuyenBay);
                                    while (cursor2.moveToNext())
                                    {
                                        if (Integer.parseInt(cursor2.getString(4)) == 0)
                                            soldout = true;
                                    }
                                    cursor2.close();
                                    if (soldout) {
                                        holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                        holder.pay.setVisibility(View.VISIBLE);
                                        holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                        holder.pay.setVisibility(View.GONE);
                                        DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                                    }
                                    else
                                    {
                                        holder.staticpay.setText("Trạng thái thanh toán: Chưa thanh toán");
                                        holder.pay.setVisibility(View.VISIBLE);
                                        holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                    }
                                }
                            }
                            i++;
                        }

                    }
                }
                c.close();
            } else {
                int i = 0;
                Cursor c = DB.getCustomer(MaChuyenBay);
                while (c.moveToNext()) {
                    if (i > position) {
                        break;
                    } else {
                        if ((SDT.equals(c.getString(4)))&&(Ten.equals(c.getString(3)) || CMND.equals(c.getString(0)))) {
                            holder.namehelp.setText("Tên người đặt: " + c.getString(8));
                            holder.sdthelp.setText("SDT người đặt: " + c.getString(9));
                            holder.id.setText("Số CMND: " + c.getString(0));
                            holder.name.setText("Hành khách: " + c.getString(3));
                            holder.phone.setText("Số điện thoại: " + c.getString(4));
                            String TenVe = null;
                            Cursor cursor = DB.GetInfoOfTicket(c.getString(2));
                            while (cursor.moveToNext()) {
                                TenVe = cursor.getString(1);
                            }
                            cursor.close();
                            holder.hangve.setText("Hạng vé: " + TenVe);
                            if (Integer.parseInt(c.getString(5)) == 1) {
                                holder.staticpay.setText("Trạng thái thanh toán: Đã thanh toán");
                                holder.datepay.setText("Ngày thanh toán: " + c.getString(7));
                                holder.pay.setVisibility(View.GONE);

                            } else if (Integer.parseInt(c.getString(5)) == 2) {
                                holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                holder.pay.setVisibility(View.GONE);
                            } else {
                                if (holder.total1 <= holder.huyVe) {
                                    holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                    holder.pay.setVisibility(View.GONE);
                                    holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                    DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                                } else {
                                    Cursor cursor1 = DB.GetInfoOfTicketByName(TenVe);
                                    String mave = null;
                                    boolean soldout = false;
                                    while (cursor1.moveToNext())
                                    {
                                        mave = cursor1.getString(0);
                                    }
                                    cursor1.close();
                                    Cursor cursor2 = DB.GetValueOfTicket(mave, MaChuyenBay);
                                    while (cursor2.moveToNext())
                                    {
                                        if (Integer.parseInt(cursor2.getString(4)) == 0)
                                            soldout = true;
                                    }
                                    cursor2.close();
                                    if (soldout) {
                                        holder.staticpay.setText("Trạng thái thanh toán: Hủy vé");
                                        holder.pay.setVisibility(View.VISIBLE);
                                        holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                        holder.pay.setVisibility(View.GONE);
                                        DB.HuyVe(MaChuyenBay, c.getString(2), c.getString(0));
                                    }
                                    else
                                    {
                                        holder.staticpay.setText("Trạng thái thanh toán: Chưa thanh toán");
                                        holder.pay.setVisibility(View.VISIBLE);
                                        holder.datepay.setText("Ngày đặt: " + c.getString(7));
                                    }
                                }
                            }
                            i++;
                        }

                    }
                }
                c.close();
            }
        }


    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (SDT.isEmpty()) {
            Cursor cursor = DB.getCustomer(MaChuyenBay);
            if (Ten.isEmpty() && CMND.isEmpty()) {
                return cursor.getCount();
            } else if (!Ten.isEmpty() && !CMND.isEmpty()) {
                while (cursor.moveToNext()) {
                    if (Ten.equals(cursor.getString(3)) && CMND.equals(cursor.getString(0))) {
                        i++;
                    }
                }
            } else {
                while (cursor.moveToNext()) {
                    if (Ten.equals(cursor.getString(3)) || CMND.equals(cursor.getString(0))) {
                        i++;
                    }
                }
            }
            cursor.close();
        }
        else
        {
            Cursor cursor = DB.getCustomer(MaChuyenBay);
            if (Ten.isEmpty() && CMND.isEmpty()) {
                while (cursor.moveToNext()) {
                    if (SDT.equals(cursor.getString(4))) {
                        i++;
                    }
                }
            } else if (!Ten.isEmpty() && !CMND.isEmpty()) {
                while (cursor.moveToNext()) {
                    if (Ten.equals(cursor.getString(3)) && CMND.equals(cursor.getString(0)) && SDT.equals(cursor.getString(4))) {
                        i++;
                    }
                }
            } else {
                while (cursor.moveToNext()) {
                    if ((SDT.equals(cursor.getString(4))) && (Ten.equals(cursor.getString(3)) || CMND.equals(cursor.getString(0)))) {
                        i++;
                    }
                }
            }
            cursor.close();
        }
        return i;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView id;
        TextView phone;
        TextView hangve;
        TextView staticpay;
        TextView datepay;
        TextView namehelp;
        TextView sdthelp;
        Button pay;
        Boolean soldout = false;
        String time = null;
        long total;
        long total1;
        int huyVe;
        private Executor executor;
        BiometricPrompt biometricPrompt;
        BiometricPrompt.PromptInfo promptInfo;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            Cursor cursor1 = DB.getRule();
            while (cursor1.moveToNext())
            {
                huyVe = Integer.parseInt(cursor1.getString(7));
            }
            name = itemView.findViewById(R.id.tv_book_name);
            id = itemView.findViewById(R.id.tv_book_id);
            phone = itemView.findViewById(R.id.tv_book_phone);
            hangve = itemView.findViewById(R.id.tv_book_levelticket);
            staticpay = itemView.findViewById(R.id.tv_book_paysta);
            datepay = itemView.findViewById(R.id.tv_book_paydate);
            pay = itemView.findViewById(R.id.btn_bookinfo_pay);
            namehelp = itemView.findViewById(R.id.tv_book_namehelp);
            sdthelp = itemView.findViewById(R.id.tv_book_sdthelp);
            executor = ContextCompat.getMainExecutor(itemView.getContext());
            biometricPrompt = new BiometricPrompt(a, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(itemView.getContext(), "Lỗi!, thiết bị không có nhận diện vân tay, hãy nhập mã bảo mật để tiếp tục", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder mydialog = new AlertDialog.Builder(itemView.getContext());
                        mydialog.setTitle("Nhập mã bảo mật");
                        final EditText secure = new EditText(itemView.getContext());
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
                                    Toast.makeText(itemView.getContext(), "Sai mã bảo mật", Toast.LENGTH_SHORT).show();
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
            Cursor cursor = DB.getFlight(MaChuyenBay);
            while (cursor.moveToNext())
            {
                time = cursor.getString(4);
            }
            cursor.close();
            Calendar calendar0 = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
            Date now0 = calendar0.getTime();
            try {
                now0 = simpleDateFormat.parse(simpleDateFormat.format(now0.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date create = null;
            try {
                create = simpleDateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            total1 = daysBetween(create, now0) ;
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    biometricPrompt.authenticate(promptInfo);
                }

            });

        }
        public void ok()
        {
            String mave = null;
            String split1[] = hangve.getText().toString().split(": ");
            String split2[] = id.getText().toString().split(": ");
            Cursor c = DB.GetInfoOfTicketByName(split1[1]);
            while (c.moveToNext()) {
                mave = c.getString(0);
            }
            c.close();
            Calendar calendar0 = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
            Date now0 = calendar0.getTime();
            try {
                now0 = simpleDateFormat.parse(simpleDateFormat.format(now0.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date create = null;
            try {
                create = simpleDateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            total = daysBetween(create, now0) ;
            if (total <= huyVe) {
                Toast.makeText(layoutInflater.getContext(), "Vé đã bị hủy ", Toast.LENGTH_SHORT).show();
                staticpay.setText("Trạng thái thanh toán: Hủy vé");
                DB.HuyVe(MaChuyenBay, mave, split2[1]);
                //datepay.setText("Ngày thanh toán: " + now);
                pay.setVisibility(View.GONE);
                notifyDataSetChanged();
            } else {

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


                Cursor cursor = DB.GetValueOfTicket(mave, MaChuyenBay);
                while (cursor.moveToNext()) {
                    val = Integer.parseInt(cursor.getString(3));
                    max = Integer.parseInt(cursor.getString(2));
                    empty = Integer.parseInt(cursor.getString(4));
                }
                cursor.close();
                if (val < max) {

                    val += 1;
                    DB.UpdateValueTiicket(mave, MaChuyenBay, String.valueOf(val), String.valueOf(empty - 1));
                    Toast.makeText(layoutInflater.getContext(), "Thanh toán thành công ", Toast.LENGTH_SHORT).show();
                    staticpay.setText("Trạng thái thanh toán: Đã thanh toán");
                    datepay.setText("Ngày thanh toán: " + now);
                    pay.setVisibility(View.GONE);
                    Cursor cursor1 = DB.getGuestInfo(split2[1]);
                    float tienNo = 0;
                    while (cursor1.moveToNext()) {
                        tienNo = Float.parseFloat(cursor1.getString(6));
                    }
                    cursor1.close();
                    float doanhthucu = 0;
                    Cursor cursor2 = DB.getFlight(MaChuyenBay);
                    while (cursor2.moveToNext()) {
                        doanhthucu = Float.parseFloat(cursor2.getString(6));
                    }
                    cursor2.close();
                    DB.updateDoanhThu(String.valueOf(tienNo + doanhthucu), MaChuyenBay);
                    boolean isUpdate1 = DB.updateDoanhThuThang(String.valueOf(tienNo + doanhthucu), MaChuyenBay);
                    Cursor cursor3 = DB.GetBaoCaoThang(MaChuyenBay);
                    String thang = null;
                    String nam = null;
                    while (cursor3.moveToNext()) {
                        int val1;
                        val1 = Integer.parseInt(cursor3.getString(3));
                        val1++;
                        DB.updateSLVThang(String.valueOf(val1), MaChuyenBay);
                        thang = cursor3.getString(1);
                        nam = cursor3.getString(2);
                    }
                    cursor3.close();
                    DB.ThanhToan(MaChuyenBay, mave, split2[1], now);
                    Cursor upNam = DB.GetBaoCaoNam(thang, nam);
                    while (upNam.moveToNext()) {
                        String a = upNam.getString(3);
                        float upd = tienNo + Float.parseFloat(a);
                        DB.updateDoanhThuNam(thang, nam, String.valueOf(upd));
                    }
                    upNam.close();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(layoutInflater.getContext(), "Hết vé!", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
    public  long daysBetween(Date create, Date now)
    {
        long differennce = (create.getTime()-now.getTime())/86400000;
        return differennce;
    }
}
