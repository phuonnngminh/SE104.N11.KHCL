package com.e.vemaybay;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
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

public class AdapterIntermidiate extends RecyclerView.Adapter<AdapterIntermidiate.ViewHolder> {
    private LayoutInflater layoutInflater;
    private DBHelper DB;
    private ArrayList<String> listInter;
    private ArrayList<String> listNote;
    private ArrayList<String> selectedAirport;
    private ArrayList<String> mainAirport;
    private ArrayList<String> listWaitingtime;
    private String temp;
    AdapterIntermidiate(Context context, DBHelper DB, ArrayList<String> listInter, ArrayList<String> listNote,
                        ArrayList<String> selectedAirport ,ArrayList<String> mainAirport, ArrayList<String> listWaitingtime) {
        this.listInter =listInter;
        this.listNote = listNote;
        this.selectedAirport = selectedAirport;
        this.mainAirport = mainAirport;
        this.layoutInflater = LayoutInflater.from(context);
        this.listWaitingtime = listWaitingtime;
        this.DB = DB;

    }

    @NonNull
    @Override
    public AdapterIntermidiate.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.selectintermidiate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterIntermidiate.ViewHolder holder, int position) {

        holder.tvName.setText(listInter.get(position));
        holder.Note.setText("Ghi chú: "+listNote.get(position));
        holder.waitTime.setText("Thời gian chờ: "+ listWaitingtime.get(position)+ " phút");
        temp = listInter.get(position);
    }

    @Override
    public int getItemCount() {
        return listInter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView Note;
        TextView waitTime;
        Button update;
        Button delete;
        String listSanBay[];

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_intermidiate_name);
            Note = itemView.findViewById(R.id.txt_note_intermidiate);
            update = itemView.findViewById(R.id.btn_update_intermidiate);
            delete = itemView.findViewById(R.id.btn_delete_intermidiate);
            waitTime =  itemView.findViewById(R.id.txt_waittime);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AlertDialog.Builder mydialog = new AlertDialog.Builder(itemView.getContext());
                    View mView = layoutInflater.inflate(R.layout.updateintermidiate, null);
                    mydialog.setTitle("Thay đổi thông tin sân bay trung gian");
                    final EditText newNote = mView.findViewById(R.id.ed_new_note);
                    final EditText newWait = mView.findViewById(R.id.ed_new_waittime);
                    final Spinner listAirport;
                    listAirport = mView.findViewById(R.id.spin_new_intermidiate);
                    Cursor c = DB.getalldataairport();
                    listSanBay = new String[c.getCount()];
                    int i=0;
                    while (c.moveToNext())
                    {
                        listSanBay[i] = c.getString(1);
                        listSanBay[i] = c.getString(1);
                        i++;
                    }
                    c.close();
                    ArrayAdapter<String> sbdi = new ArrayAdapter<String>(itemView.getContext(), android.R.layout.simple_list_item_1,listSanBay);
                    listAirport.setAdapter(sbdi);
                    mydialog.setView(mView);
                    mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if ((listInter.contains(listAirport.getSelectedItem().toString()) || mainAirport.contains(listAirport.getSelectedItem().toString())) && (!listAirport.getSelectedItem().toString().equals(tvName.getText().toString())))
                            {
                                Toast.makeText(itemView.getContext(), "Sân bay trung gian này đã chọn hoặc bị trùng sân bay chính", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                int position = getAdapterPosition();
                                listInter.set(position, listAirport.getSelectedItem().toString());
                                listNote.set(position, newNote.getText().toString());
                                selectedAirport.set(position, listAirport.getSelectedItem().toString());
                                tvName.setText(listAirport.getSelectedItem().toString());
                                Note.setText(newNote.getText().toString());
                                waitTime.setText(newWait.getText().toString());
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
                    listInter.remove(position);
                    listNote.remove(position);
                    listWaitingtime.remove(position);
                    selectedAirport.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listInter.size());
                }
            });
        }

    }

}

