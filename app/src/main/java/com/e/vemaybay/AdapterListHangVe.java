package com.e.vemaybay;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterListHangVe extends RecyclerView.Adapter<AdapterListHangVe.ViewHolder> {
    private LayoutInflater layoutInflater;
    private DBHelper DB;
    private ArrayList<String> listVe;
    private ArrayList<String> quantityTicket;
    AdapterListHangVe (Context context, DBHelper DB, ArrayList<String> listVe, ArrayList<String> quantityTicket)
    {
        this.layoutInflater = LayoutInflater.from(context);
        this.DB = DB;
        this.listVe = listVe;
        this.quantityTicket = quantityTicket;
    }

    @NonNull
    @Override
    public AdapterListHangVe.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.quantityticket,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListHangVe.ViewHolder holder, int position) {
        holder.TenHangVe.setText(listVe.get(position));
        holder.Quantity.setText("Số lượng: " +quantityTicket.get(position));
    }

    @Override
    public int getItemCount() {
        return listVe.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView TenHangVe;
        TextView Quantity;
        Button delete;
        Button update;
        String listHangVe[];
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            TenHangVe = itemView.findViewById(R.id.txt_tenhangve);
            Quantity = itemView.findViewById(R.id.txt_quantity);
            delete = itemView.findViewById(R.id.btn_delete_hangve);
            update = itemView.findViewById(R.id.btn_update_hangve);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AlertDialog.Builder mydialog = new AlertDialog.Builder(itemView.getContext());
                    View mView = layoutInflater.inflate(R.layout.updatehangve, null);
                    mydialog.setTitle("Thay đổi thông tin hạng vé");
                    final EditText newVal = mView.findViewById(R.id.ed_new_value_hangve);
                    final Spinner list_Hangve;
                    list_Hangve = mView.findViewById(R.id.spin_new_hangve);
                    Cursor c = DB.getLevelTicket();
                    listHangVe = new String[c.getCount()];
                    int i=0;
                    while (c.moveToNext())
                    {
                        listHangVe[i] = c.getString(1) + " (Chiết khấu: " + c.getString(2) +"%)" ;
                        i++;
                    }
                    c.close();
                    ArrayAdapter<String> sbdi = new ArrayAdapter<String>(itemView.getContext(), android.R.layout.simple_list_item_1,listHangVe);
                    list_Hangve.setAdapter(sbdi);
                    mydialog.setView(mView);
                    mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String need = list_Hangve.getSelectedItem().toString();
                            String[] separated = need.split(" \\(");
                            if (newVal.getText().toString().isEmpty())
                            {
                                Toast.makeText(itemView.getContext(), "Chưa điền số lượng", Toast.LENGTH_SHORT).show();
                            }
                            else if (listVe.contains(separated[0]) && !separated[0].equals(TenHangVe.getText().toString()))
                            {
                                Toast.makeText(itemView.getContext(), "Hạng vé này đã chọn", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                int position = getAdapterPosition();

                                listVe.set(position,separated[0]);
                                quantityTicket.set(position, newVal.getText().toString());
                                TenHangVe.setText(separated[0]);
                                Quantity.setText("Số lượng: "+newVal.getText().toString());
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
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    listVe.remove(position);
                    quantityTicket.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listVe.size());
                }
            });
        }
    }
}
