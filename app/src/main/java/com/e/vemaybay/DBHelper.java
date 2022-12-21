package com.e.vemaybay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "VeMayBay.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table CHUYENBAY(MaChuyenBay TEXT primary key not null, GiaVe TEXT, MaSanBayDi TEXT, MaSanBayDen TEXT, Date TEXT, Time TEXT, DoanhThu TEXT, MaTuyenBay, foreign key (MaTuyenBay) references TUYENBAY(MaTuyenBay))");
        DB.execSQL("create Table SANBAY(MaSanBay TEXT primary key not null, TenSanBay TEXT, Tinh TEXT, Nuoc TEXT)");
        DB.execSQL("create Table TUYENBAY(MaTuyenBay TEXT primary key not null, MaSanBayDi TEXT, MaSanBayDen TEXT, foreign key (MaSanBayDi) references SANBAY(MaSanBay), foreign key (MaSanBayDen) references SANBAY(MaSanBay))");
        DB.execSQL("create Table TRUNGGIAN(MaChuyenBay TEXT not null, MaSanBay TEXT not null, ThoiGianDung TEXT,GhiChu TEXT, primary key(MaChuyenBay, MaSanBay), foreign key (MaChuyenBay) references CHUYENBAY(MaChuyenBay), foreign key (MaSanBay) references SANBAY(MaSanBay))");
        DB.execSQL("create Table HANGVE(MaHangVe TEXT primary key not null, TenHangVe TEXT, TiLe TEXT)");
        DB.execSQL("create Table SOLUONGVE (MaHangVe TEXT not null, MaChuyenBay TEXT not null, SoLuong TEXT, SoGheDat TEXT, SoGheTrong TEXT, TienVe TEXT, primary key(MaHangVe, MaChuyenBay), foreign key (MaHangVe) references HANGVE(MaHangVe), foreign key (MaChuyenBay) references CHUYENBAY(MaChuyenBay))");
        DB.execSQL("create Table QuyDinh(MaQuyDinh TEXT primary key not null,TGToiDa TEXT, DungToiThieu TEXT, DungToiDa TEXT, BayToiThieu TEXT, MaBaoMat TEXT, HuyVe TEXT, Xoa TEXT)");
        DB.execSQL("create Table PHANQUYEN(Username TEXT primary key not null, Password TEXT, UserType TEXT)");
        DB.execSQL("create Table KHACHHANG(CMND TEXT not null, MaChuyenBay TEXT not null, MaHangVe Text, Ten TEXT, SDT TEXT,ThanhToan TEXT, tienNo TEXT, ngayDatVe TEXT, TenNguoiDat TEXT, SDTNguoiDat TEXT, primary key(CMND, MaChuyenBay), foreign key (MaHangVe) references HANGVE(MaHangVe), foreign key (MaChuyenBay) references CHUYENBAY(MaChuyenBay))");
        DB.execSQL("create Table BAOCAOTHANG(MaChuyenBay primary key not null, Thang TEXT, Nam TEXT, SoVe TEXT, DoanhThu TEXT,foreign key (MaChuyenBay) references CHUYENBAY(MaChuyenBay))");
        DB.execSQL("create Table BAOCAONAM(Thang TEXT not null, Nam TEXT not null, SoChuyenBay TEXT, DoanhThu TEXT, primary key(Thang, Nam))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists BANVE");
    }

    public Boolean UpdateValueTiicket(String MaVe, String  MaChuyenBay, String newVal, String newVal1 )
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SoGheDat", newVal);
        contentValues.put("SoGheTrong", newVal1);
        Cursor  cursor = DB.rawQuery("Select * from SOLUONGVE where MaHangVe= ? and MaChuyenBay= ?", new String[]{MaVe, MaChuyenBay});
        if (cursor.getCount() > 0) {
            long result = DB.update("SOLUONGVE", contentValues, "MaHangVe= ? and MaChuyenBay= ?", new String[]{MaVe, MaChuyenBay});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }

    public Boolean UpdateTuyenBay(String MaTuyenBay, String MaSanbayDi, String  MaSanBayDen)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaSanBayDi", MaSanbayDi);
        contentValues.put("MaSanBayDen", MaSanBayDen);
        Cursor  cursor = DB.rawQuery("Select * from TUYENBAY where MaTuyenBay= ?", new String[]{MaTuyenBay});
        if (cursor.getCount() > 0) {
            long result = DB.update("TUYENBAY", contentValues, "MaTuyenBay= ?", new String[]{MaTuyenBay});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean UpdateCancelDelete(String HuyVe, String  Delete)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("HuyVe", HuyVe);
        contentValues.put("Xoa", Delete);
        Cursor  cursor = DB.rawQuery("Select * from QuyDinh where MaQuyDinh= ?", new String[]{"001"});
        if (cursor.getCount() > 0) {
            long result = DB.update("QuyDinh", contentValues, "MaQuyDinh= ?", new String[]{"001"});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean UpdateDiDen(String MaChuyenbay, String MaSanBayDi, String  MaSanBayDen)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaSanBayDi", MaSanBayDi);
        contentValues.put("MaSanBayDen", MaSanBayDen);
        Cursor  cursor = DB.rawQuery("Select * from CHUYENBAY where MaChuyenBay= ?", new String[]{MaChuyenbay});
        if (cursor.getCount() > 0) {
            long result = DB.update("CHUYENBAY", contentValues, "MaChuyenBay= ?", new String[]{MaChuyenbay});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean AddFlight (String MaChuyenBay, String GiaVe, String MaSanBayDi, String MaSanBayDen, String Date, String Time, String MaTuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaChuyenBay",MaChuyenBay);
        contentValues.put("GiaVe", GiaVe);
        contentValues.put("MaSanBayDi", MaSanBayDi);
        contentValues.put("MaSanBayDen", MaSanBayDen);
        contentValues.put("Date", Date);
        contentValues.put("Time", Time);
        contentValues.put("DoanhThu", "0");
        contentValues.put("MaTuyenBay", MaTuyenBay);
        long result = DB.insert("CHUYENBAY",null, contentValues);
        if (result == -1)
        {
            return false;
        }else
        {
            return true;
        }
    }
    public Boolean AddBaoCaoThang (String MaChuyenBay , String Thang, String Nam)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaChuyenBay",MaChuyenBay);
        contentValues.put("Thang", Thang);
        contentValues.put("Nam", Nam);
        contentValues.put("SoVe", "0");
        contentValues.put("DoanhThu", "0");
        long result = DB.insert("BAOCAOTHANG",null, contentValues);
        if (result == -1)
        {
            return false;
        }else
        {
            return true;
        }
    }
    public Boolean AddBaoCaoNam (String Thang, String Nam)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Thang",Thang);
        contentValues.put("Nam", Nam);
        contentValues.put("SoChuyenBay", "1");
        contentValues.put("DoanhThu", "0");
        long result = DB.insert("BAOCAONAM",null, contentValues);
        if (result == -1)
        {
            return false;
        }else
        {
            return true;
        }
    }
    public Boolean AddGuest (String CMND, String MaChuyenBay, String MaHangVe, String Ten, String SDT, String ThanhToan, String tienNo, String ngayDatVe, String TenNguoiDat, String SDTNguoiDat)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CMND",CMND);
        contentValues.put("MaChuyenBay", MaChuyenBay);
        contentValues.put("MaHangVe", MaHangVe);
        contentValues.put("Ten", Ten);
        contentValues.put("SDT", SDT);
        contentValues.put("ThanhToan", ThanhToan);
        contentValues.put("tienNo", tienNo);
        contentValues.put("ngayDatVe", ngayDatVe);
        contentValues.put("TenNguoiDat", TenNguoiDat);
        contentValues.put("SDTNguoiDat", SDTNguoiDat);
        long result = DB.insert("KHACHHANG",null, contentValues);
        if (result == -1)
        {
            return false;
        }else
        {
            return true;
        }
    }
    public Boolean deleteAirport (String MaSanBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SANBAY where MaSanBay = ?", new String[] {MaSanBay});
        if (cursor.getCount() > 0) {
            long result = DB.delete("SANBAY", "MaSanBay=?", new String[]{MaSanBay});
            if (result == -1)
                return false;
            else return true;
        }
        else return  false;
    }
    public Boolean deleteTuyenBay (String MaTuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from TUYENBAY where MaTuyenBay = ?", new String[] {MaTuyenBay});
        if (cursor.getCount() > 0) {
            long result = DB.delete("TUYENBAY", "MaTuyenBay=?", new String[]{MaTuyenBay});
            if (result == -1)
                return false;
            else return true;
        }
        else return  false;
    }
    public Boolean deleteChuyenBay (String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from CHUYENBAY where MaChuyenBay = ?", new String[] {MaChuyenBay});
        if (cursor.getCount() > 0) {
            long result = DB.delete("CHUYENBAY", "MaChuyenBay=?", new String[]{MaChuyenBay});
            if (result == -1)
                return false;
            else return true;
        }
        else return  false;
    }
    public Boolean deleteTrungGian (String MaChuyenBay, String MaSanBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from TRUNGGIAN where MaChuyenBay = ? and MaSanBay =? ", new String[] {MaChuyenBay, MaSanBay});
        if (cursor.getCount() > 0) {
            long result = DB.delete("TRUNGGIAN", "MaChuyenBay=? and MaSanBay=?", new String[]{MaChuyenBay, MaSanBay});
            if (result == -1)
                return false;
            else return true;
        }
        else return  false;
    }
    public Boolean changeSecuritycode(String code)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaBaoMat", code);
        Cursor cursor = DB.rawQuery("Select * from QuyDinh where MaQuyDinh = ?", new String[] {"001"});
        if (cursor.getCount() > 0) {
            long result = DB.update("QuyDinh", contentValues, "MaQuyDinh=?", new String[]{"001"});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean updateAirport(String maSanBay, String TenSanBay, String Tinh, String Nuoc)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenSanBay", TenSanBay);
        contentValues.put("Tinh", Tinh);
        contentValues.put("Nuoc", Nuoc);
        Cursor cursor = DB.rawQuery("Select * from SANBAY where MaSanBay = ?", new String[] {maSanBay});
        if (cursor.getCount() > 0) {
            long result = DB.update("SANBAY", contentValues, "MaSanBay=?", new String[]{maSanBay});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean updateDoanhThu(String DoanhThu, String MaFlight)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DoanhThu", DoanhThu);
        Cursor cursor = DB.rawQuery("Select * from CHUYENBAY where MaChuyenBay = ?", new String[] {MaFlight});
        if (cursor.getCount() > 0) {
            long result = DB.update("CHUYENBAY", contentValues, "MaChuyenBay=?", new String[]{MaFlight});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean updateDoanhThuThang(String DoanhThu, String MaFlight)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DoanhThu", DoanhThu);
        Cursor cursor = DB.rawQuery("Select * from BAOCAOTHANG where MaChuyenBay = ?", new String[] {MaFlight});
        if (cursor.getCount() > 0) {
            long result = DB.update("BAOCAOTHANG", contentValues, "MaChuyenBay=?", new String[]{MaFlight});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean updateDoanhThuNam(String thang, String nam, String DoanhThu)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DoanhThu", DoanhThu);
        Cursor cursor = DB.rawQuery("Select * from BAOCAONAM where Thang = ? and Nam = ?", new String[] {thang, nam});
        if (cursor.getCount() > 0) {
            long result = DB.update("BAOCAONAM", contentValues, "Thang = ? and Nam = ?", new String[]{thang, nam});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean updateSLVThang(String SoVe, String MaFlight)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SoVe", SoVe);
        Cursor cursor = DB.rawQuery("Select * from BAOCAOTHANG where MaChuyenBay = ?", new String[] {MaFlight});
        if (cursor.getCount() > 0) {
            long result = DB.update("BAOCAOTHANG", contentValues, "MaChuyenBay=?", new String[]{MaFlight});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean updateTicketlevel(String maHangVe, String tenHangVe, String chietKhau)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenHangVe", tenHangVe);
        contentValues.put("TiLe", chietKhau);
        Cursor cursor = DB.rawQuery("Select * from HANGVE where MaHangVe = ?", new String[] {maHangVe});
        if (cursor.getCount() > 0) {
            long result = DB.update("HANGVE", contentValues, "MaHangVe=?", new String[]{maHangVe});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean deleteTicketlevel (String MaHangVe)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from HANGVE where MaHangVe = ?", new String[] {MaHangVe});
        if (cursor.getCount() > 0) {
            long result = DB.delete("HANGVE", "MaHangVe=?", new String[]{MaHangVe});
            if (result == -1)
                return false;
            else return true;
        }
        else return  false;
    }
    public Boolean deleteSoLuongVe (String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SOLUONGVE where MaChuyenBay = ?", new String[] {MaChuyenBay});
        if (cursor.getCount() > 0) {
            long result = DB.delete("SOLUONGVE", "MaChuyenBay=?", new String[]{MaChuyenBay});
            if (result == -1)
                return false;
            else return true;
        }
        else return  false;
    }
    public Boolean deleteTrungGian(String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from TRUNGGIAN where MaChuyenBay = ?", new String[] {MaChuyenBay});
        if (cursor.getCount() > 0) {
            long result = DB.delete("TRUNGGIAN", "MaChuyenBay=?", new String[]{MaChuyenBay});
            if (result == -1)
                return false;
            else return true;
        }
        else return  false;
    }
    public Boolean deleteGuest(String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from KHACHHANG where MaChuyenBay = ?", new String[] {MaChuyenBay});
        if (cursor.getCount() > 0) {
            long result = DB.delete("KHACHHANG", "MaChuyenBay=?", new String[]{MaChuyenBay});
            if (result == -1)
                return false;
            else return true;
        }
        else return  false;
    }
    public Boolean deleteBCT(String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from BAOCAOTHANG where MaChuyenBay = ?", new String[] {MaChuyenBay});
        if (cursor.getCount() > 0) {
            long result = DB.delete("BAOCAOTHANG", "MaChuyenBay=?", new String[]{MaChuyenBay});
            if (result == -1)
                return false;
            else return true;
        }
        else return  false;
    }
    public Boolean AddTuyenBay (String MaTuyenBay, String MaSanbayDi, String MaSanbayDen)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaTuyenBay", MaTuyenBay);
        contentValues.put("MaSanBayDi", MaSanbayDi);
        contentValues.put("MaSanBayDen", MaSanbayDen);
        long result = DB.insert("TUYENBAY", null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Boolean AddAirport (String MaSanbay, String TenSanbay, String Tinh, String Nuoc)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaSanBay", MaSanbay);
        contentValues.put("TenSanBay", TenSanbay);
        contentValues.put("Tinh", Tinh);
        contentValues.put("Nuoc", Nuoc);
        long result = DB.insert("SANBAY", null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Boolean CreateDefaultRule ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("delete from QuyDinh");
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaQuyDinh", "001");
        contentValues.put("TGToiDa", "2");
        contentValues.put("DungToiThieu", "10");
        contentValues.put("DungToiDa", "20");
        contentValues.put("BayToiThieu", "30");
        contentValues.put("MaBaoMat", "2468");
        contentValues.put("HuyVe", "1");
        contentValues.put("Xoa", "0");
        long result = DB.insert("QuyDinh", null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Boolean AddIntemidiate (String MaChuyenBay, String MaSanBay, String ThoiGianDung, String GhiChu)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaChuyenBay", MaChuyenBay);
        contentValues.put("MaSanBay", MaSanBay);
        contentValues.put("ThoiGianDung", ThoiGianDung);
        contentValues.put("GhiChu", GhiChu);
        long result = DB.insert("TRUNGGIAN", null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Boolean ThanhToan(String MaChuyenBay, String MaHangVe, String CMND, String newDate)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ThanhToan", "1");
        contentValues.put("tienNo", "0");
        contentValues.put("ngayDatVe", newDate);
        Cursor cursor = DB.rawQuery("Select * from KHACHHANG where MaChuyenBay = ? and MaHangVe= ? and CMND = ?", new String[] {MaChuyenBay, MaHangVe, CMND});
        if (cursor.getCount() > 0) {
            long result = DB.update("KHACHHANG", contentValues, "MaChuyenBay = ? and MaHangVe= ? and CMND = ?", new String[]{MaChuyenBay, MaHangVe, CMND});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean HuyVe(String MaChuyenBay, String MaHangVe, String CMND)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ThanhToan", "2");
        contentValues.put("tienNo", "0");
        Cursor cursor = DB.rawQuery("Select * from KHACHHANG where MaChuyenBay = ? and MaHangVe= ? and CMND = ?", new String[] {MaChuyenBay, MaHangVe, CMND});
        if (cursor.getCount() > 0) {
            long result = DB.update("KHACHHANG", contentValues, "MaChuyenBay = ? and MaHangVe= ? and CMND = ?", new String[]{MaChuyenBay, MaHangVe, CMND});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean AddUser ( String Username, String Password, String UserType)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username", Username);
        contentValues.put("Password", Password);
        contentValues.put("UserType", UserType);
        long result = DB.insert("PHANQUYEN", null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Boolean AddTicketLevel (String MaHangVe, String TenHangve, String ChietKhau)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaHangVe", MaHangVe);
        contentValues.put("TenHangVe", TenHangve);
        contentValues.put("TiLe", ChietKhau);
        long result = DB.insert("HANGVE", null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Cursor getUser (String Username)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from PHANQUYEN where Username= ?", new String[]{Username});
        return cursor;
    }
    public Cursor getBCT (String Thang, String Nam)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from BAOCAOTHANG where Thang= ? and Nam= ?", new String[]{Thang, Nam});
        return cursor;
    }
    public Cursor getBCN (String Thang, String Nam)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from BAOCAONAM where Thang= ? and Nam= ?", new String[]{Thang, Nam});
        return cursor;
    }
    public Cursor checkAirportTB (String MaSanBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from TUYENBAY where MaSanbayDi= ? or MaSanBayDen = ?", new String[]{MaSanBay, MaSanBay});
        return cursor;
    }
    public Cursor checkAirportTG (String MaSanBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from TRUNGGIAN where MaSanbay= ? ", new String[]{MaSanBay});
        return cursor;
    }
    public Cursor checkTuyenBayCB (String MaTuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from CHUYENBAY where MaTuyenBay= ? ", new String[]{MaTuyenBay});
        return cursor;
    }
    public Cursor checkHangVeSLV (String MaHangVe)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SOLUONGVE where MaHangVe= ? ", new String[]{MaHangVe});
        return cursor;
    }
    public Cursor checkHangVeGuest (String MaHangVe)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from KHACHHANG where MaHangVe= ? ", new String[]{MaHangVe});
        return cursor;
    }
    public Cursor getTrungGian (String MaChuyenBay, String MaSanBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from TRUNGGIAN where MaChuyenBay= ? and MaSanBay = ?", new String[]{MaChuyenBay, MaSanBay});
        return cursor;
    }
    public Cursor getAllTrungGian (String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from TRUNGGIAN where MaChuyenBay= ? ", new String[]{MaChuyenBay});
        return cursor;
    }
    public Cursor getAllTB ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from TUYENBAY", null);
        return cursor;
    }
    public Cursor getCustomer (String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from KHACHHANG where MaChuyenBay= ?", new String[]{MaChuyenBay});
        return cursor;
    }
    public Cursor getalldataairport ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SANBAY", null);
        return cursor;
    }
    public Cursor getRule ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from QuyDinh", null);
        return cursor;
    }
    public Cursor getLevelTicket ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from HANGVE", null);
        return cursor;
    }
    public Boolean ChangeQuantityOfIntermidiate(String TGToiDa)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TGToiDa", TGToiDa);
        Cursor cursor = DB.rawQuery("Select * from QuyDinh where MaQuyDinh = ?", new String[] {"001"});
        if (cursor.getCount() > 0) {
            long result = DB.update("QuyDinh", contentValues, "MaQuyDinh=?", new String[]{"001"});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean ChangeMinMaxDelay(String MinDelay, String MaxDelay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DungToiThieu", MinDelay);
        contentValues.put("DungToiDa", MaxDelay);
        Cursor cursor = DB.rawQuery("Select * from QuyDinh where MaQuyDinh = ?", new String[] {"001"});
        if (cursor.getCount() > 0) {
            long result = DB.update("QuyDinh", contentValues, "MaQuyDinh=?", new String[]{"001"});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean ChangeMinFlyTime(String MinFlyTime)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("BayToiThieu", MinFlyTime);
        Cursor cursor = DB.rawQuery("Select * from QuyDinh where MaQuyDinh = ?", new String[] {"001"});
        if (cursor.getCount() > 0) {
            long result = DB.update("QuyDinh", contentValues, "MaQuyDinh=?", new String[]{"001"});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean UpdateSoChuyenBayNam(String Thang, String Nam, String newSCB)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SoChuyenBay", newSCB);
        Cursor cursor = DB.rawQuery("Select * from BAOCAONAM where Thang = ? and Nam = ?", new String[] {Thang, Nam});
        if (cursor.getCount() > 0) {
            long result = DB.update("BAOCAONAM", contentValues, "Thang=? and Nam=?", new String[]{Thang, Nam});
            if (result == -1) {
                return false;
            }
            else{
                return true;
            }
        }

        else
        {return  false;}
    }
    public Boolean AddSelectedAirport (String TenSanBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("MaSanBay", "Ma" + TenSanBay);
        contentValues.put("TenSanBay", TenSanBay);
        long result = DB.insert("SelectedAirport", null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Cursor getalldataselectedairport ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SelectedAirport", null);
        return cursor;
    }
    public Cursor getallFlight ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from CHUYENBAY", null);
        return cursor;
    }
    public Cursor GetAllBaoCaoThang ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from BAOCAOTHANG", null);
        return cursor;
    }
    public Cursor GetAllBaoCaoNam ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from BAOCAONAM", null);
        return cursor;
    }
    public Cursor getFlight (String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from CHUYENBAY where MaChuyenBay= ?", new String[]{MaChuyenBay});
        return cursor;
    }
    public Cursor getAirportByName (String TenSanBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SANBAY where TenSanBay= ?", new String[]{TenSanBay});
        return cursor;
    }
    public Cursor getAirportInfo (String MaSanBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SANBAY where MaSanBay= ?", new String[]{MaSanBay});
        return cursor;
    }
    public Cursor getGuestInfo (String ID)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from KHACHHANG where CMND= ?", new String[]{ID});
        return cursor;
    }
    public Boolean deleteselectedairport (String TenSanBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SelectedAirport where TenSanBay = ?", new String[] {TenSanBay});
        if (cursor.getCount() > 0) {
            long result = DB.delete("SelectedAirport", "TenSanBay=?", new String[]{TenSanBay});
            if (result == -1)
                return false;
            else return true;
        }
        else return  false;
    }
    public Boolean AddTicketQuantity (String MaHangVe, String MaChuyenBay, String SoLuong, String SoGheTrong, String TienVe)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MaHangVe", MaHangVe);
        contentValues.put("MaChuyenBay", MaChuyenBay);
        contentValues.put("SoLuong", SoLuong);
        contentValues.put("SoGheDat", "0");
        contentValues.put("SoGheTrong", SoGheTrong);
        contentValues.put("TienVe", TienVe);
        long result = DB.insert("SOLUONGVE", null, contentValues);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Cursor GetQuantityOfTickets ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SOLUONGVE", null);
        return cursor;
    }
    public Cursor GetBaoCaoNam (String Thang, String Nam)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from BAOCAONAM where Thang= ? and Nam= ?", new String[] {Thang, Nam});
        return cursor;
    }
    public Cursor GetBaoCaoThang (String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from BAOCAOTHANG where MaChuyenBay = ?", new String[] {MaChuyenBay});
        return cursor;
    }
    public Cursor GetTicketOfFlight (String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SOLUONGVE where MaChuyenBay= ?", new String[]{MaChuyenBay});
        return cursor;
    }
    public Cursor GetInfoOfTicket (String MaHangVe)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from HANGVE where MaHangVe= ?", new String[]{MaHangVe});
        return cursor;
    }
    public Cursor GetInfoOfTicketByName (String TenHangVe)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from HANGVE where TenHangVe= ?", new String[]{TenHangVe});
        return cursor;
    }
    public Cursor GetValueOfTicket (String MaHangVe, String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SOLUONGVE where MaHangVe= ? and MaChuyenBay= ?", new String[]{MaHangVe, MaChuyenBay});
        return cursor;
    }
    public int GetGiaveOfFlight (String MaChuyenBay)
    {
        int gia=0;
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from CHUYENBAY where MaChuyenBay= ?", new String[]{MaChuyenBay});
        while (cursor.moveToNext())
        {
            gia = Integer.parseInt(cursor.getString(1));
        }
        return gia;
    }
    public Cursor GetInterInfo (String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from TRUNGGIAN where MaChuyenBay= ?", new String[]{MaChuyenBay});
        return cursor;
    }
    public Cursor GetNameAirport(String MaSanBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from SANBAY where MaSanBay= ?", new String[]{MaSanBay});
        return cursor;
    }
    public Cursor GetTimeOfFlight(String MaChuyenBay)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor  cursor = DB.rawQuery("Select * from CHUYENBAY where MaChuyenBay= ?", new String[]{MaChuyenBay});
        return cursor;
    }
}
