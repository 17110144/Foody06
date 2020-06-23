package hcmute.edu.vn.foody06.model;

import java.sql.Time;

public class Mon {
    private int IdMon;
    private String TenMon;
    private int IdQuan;
    private String DanhMuc;
    private String Gia;
    private String URLHinhAnh;

    public Mon() {
    }

    public Mon(int idMon, String tenMon, int idQuan, String danhMuc, String gia, String URLHinhAnh) {
        IdMon = idMon;
        TenMon = tenMon;
        IdQuan = idQuan;
        DanhMuc = danhMuc;
        Gia = gia;
        this.URLHinhAnh = URLHinhAnh;
    }

    public int getIdMon() {
        return IdMon;
    }

    public void setIdMon(int idMon) {
        IdMon = idMon;
    }

    public String getTenMon() {
        return TenMon;
    }

    public void setTenMon(String tenMon) {
        TenMon = tenMon;
    }

    public int getIdQuan() {
        return IdQuan;
    }

    public void setIdQuan(int idQuan) {
        IdQuan = idQuan;
    }

    public String getGia() {
        return Gia;
    }

    public void setGia(String gia) {
        Gia = gia;
    }

    public String getURLHinhAnh() {
        return URLHinhAnh;
    }

    public void setURLHinhAnh(String URLHinhAnh) {
        this.URLHinhAnh = URLHinhAnh;
    }

    public String getDanhMuc() {
        return DanhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        DanhMuc = danhMuc;
    }
}
