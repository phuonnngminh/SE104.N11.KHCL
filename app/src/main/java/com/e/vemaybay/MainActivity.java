package com.e.vemaybay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.e.vemaybay.ui.main.SectionsPagerAdapter;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    DBHelper DB;
    Button Home;
    Button Logout;
    ViewPager viewPager;
    TabLayout tabs;
    AppBarLayout appBarLayout;
    String mcbBook = "";
    String mcbEdit = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        Home = findViewById(R.id.btn_home);
        Logout = findViewById(R.id.btn_logout);
        appBarLayout = findViewById(R.id.appbar);
        DB = new DBHelper(this);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        if (viewPager.getCurrentItem() == 0)
        {
            tabs.setTabTextColors(Color.WHITE, Color.YELLOW);
            tabs.setSelectedTabIndicatorColor(Color.YELLOW);
            appBarLayout.setBackgroundColor(Color.parseColor("#d62f6c"));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem() == 0)
                {
                    tabs.setTabTextColors(Color.WHITE, Color.YELLOW);
                    //#eb34c3

                    appBarLayout.setBackgroundColor(Color.parseColor("#d62f6c"));
                }
                else
                if (viewPager.getCurrentItem() == 1)
                {

                    appBarLayout.setBackgroundColor(Color.parseColor("#db6b1a"));


                }
                else
                if (viewPager.getCurrentItem() == 2)
                {

                    appBarLayout.setBackgroundColor(Color.parseColor("#db6b1a"));
                }
                else
                if (viewPager.getCurrentItem() == 3)
                {

                    appBarLayout.setBackgroundColor(Color.parseColor("#DC9C54"));
                }
                else
                if (viewPager.getCurrentItem() == 4)
                {

                    appBarLayout.setBackgroundColor(Color.parseColor("#DC9C54"));
                }

                else
                {
                    appBarLayout.setBackgroundColor(Color.parseColor("#aba226"));
                    //367386
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(v.getContext());
                mydialog.setTitle("X??c nh???n ????ng xu???t?");
                mydialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("Th??ng tin th??nh vi??n \n");
                buffer.append("L?? Ph????ng Minh - MSSV: 20521602 \n");
                buffer.append("L?? Th??? Thi???p - MSSV: 20521955 \n");
                buffer.append("Nguy???n Th??? Ph????ng Quy??n - MSSV: 20521820 \n");
                buffer.append("H??? Nguy???n Gia Huy - MSSV: 20521386\n\n");
                buffer.append("Th??ng tin c??c quy ?????nh c?? b???n (c?? th??? ch???nh s???a) \n");
                Cursor c = DB.getRule();
                String MaxInter = null;
                String Mindelay = null;
                String MaxDelay = null;
                String MinFly = null;
                String Cancel = null;
                String delete = null;
                while (c.moveToNext())
                {
                    MaxInter = c.getString(1);
                    Mindelay = c.getString(2);
                    MaxDelay = c.getString(3);
                    MinFly = c.getString(4);
                    Cancel = c.getString(6);
                    delete = c.getString(7);
                }
                buffer.append("S??? l?????ng s??n bay trung gian t???i ??a: "+ MaxInter+ "\n");
                buffer.append("Th???i gian d???ng t???i thi???u: "+ Mindelay+ " ph??t\n");
                buffer.append("Th???i gian d???ng t???i ??a: "+ MaxDelay+ " ph??t \n");
                buffer.append("Th???i gian bay t???i thi???u: "+ MinFly+ " ph??t \n");
                buffer.append("Th???i gian kh??a ?????t v??: "+ Cancel+ " ng??y tr?????c kh???i h??nh \n");
                buffer.append("Th???i gian x??a v?? ?????t: "+ delete+ " ng??y tr?????c kh???i h??nh \n");
                AlertDialog.Builder builder =new  AlertDialog.Builder(view.getContext());
                builder.setCancelable(true);
                builder.setTitle("Th??ng tin ????? ??n");
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
    public String getMCBBook()
    {
        return mcbBook;
    }
    public void setMCBBook(String newmcb)
    {
        mcbBook = newmcb;
    }
    public String getMCBEdit()
    {
        return mcbEdit;
    }
    public void setMCBEdit(String newmcb)
    {
        mcbEdit = newmcb;
    }
}