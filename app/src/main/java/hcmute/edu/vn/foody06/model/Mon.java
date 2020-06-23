package hcmute.edu.vn.foody06.model;

import java.sql.Time;

public class Mon {
    private String TenMon;
    private int IdDanhMuc;
    private int Gia;
    private String URLHinhAnh;

    public Mon() {
    }

    public Mon(String tenMon, int idDanhMuc, int gia, String URLHinhAnh) {
        TenMon = tenMon;
        IdDanhMuc = idDanhMuc;
        Gia = gia;
        this.URLHinhAnh = URLHinhAnh;
    }

    public String getTenMon() {
        return TenMon;
    }

    public int getIdDanhMuc() {
        return IdDanhMuc;
    }

    public int getGia() {
        return Gia;
    }

    public String getURLHinhAnh() {
        return URLHinhAnh;
    }

    public void setTenMon(String tenMon) {
        TenMon = tenMon;
    }

    public void setIdDanhMuc(int idDanhMuc) {
        IdDanhMuc = idDanhMuc;
    }

    public void setGia(int gia) {
        Gia = gia;
    }

    public void setURLHinhAnh(String URLHinhAnh) {
        this.URLHinhAnh = URLHinhAnh;
    }
}
