package hcmute.edu.vn.foody06.model;


import java.sql.Time;

public class Quan {

    private int IdQuan;
    private String TenQuan;
    private String UrlAnhDaiDien;
    private String DiaChi;
    private String TinhThanh;
    private String LoaiHinh;
    private String GiaThapNhat;
    private String GiaCaoNhat;
    private String GioMoCua;
    private String GioDongCua;
    private String Wifi;
    private String MatKhauWifi;
    private String SDT;
    private String Description;

    public Quan() {
    }

    public Quan(int idQuan, String tenQuan, String urlAnhDaiDien, String diaChi, String tinhThanh, String loaiHinh, String giaThapNhat, String giaCaoNhat, String gioMoCua, String gioDongCua, String wifi, String matKhauWifi, String SDT, String description) {
        IdQuan = idQuan;
        TenQuan = tenQuan;
        UrlAnhDaiDien = urlAnhDaiDien;
        DiaChi = diaChi;
        TinhThanh = tinhThanh;
        LoaiHinh = loaiHinh;
        GiaThapNhat = giaThapNhat;
        GiaCaoNhat = giaCaoNhat;
        GioMoCua = gioMoCua;
        GioDongCua = gioDongCua;
        Wifi = wifi;
        MatKhauWifi = matKhauWifi;
        this.SDT = SDT;
        Description = description;
    }

    public int getIdQuan() {
        return IdQuan;
    }

    public void setIdQuan(int idQuan) {
        IdQuan = idQuan;
    }

    public String getTenQuan() {
        return TenQuan;
    }

    public void setTenQuan(String tenQuan) {
        TenQuan = tenQuan;
    }

    public String getUrlAnhDaiDien() {
        return UrlAnhDaiDien;
    }

    public void setUrlAnhDaiDien(String urlAnhDaiDien) {
        UrlAnhDaiDien = urlAnhDaiDien;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getTinhThanh() {
        return TinhThanh;
    }

    public void setTinhThanh(String tinhThanh) {
        TinhThanh = tinhThanh;
    }

    public String getLoaiHinh() {
        return LoaiHinh;
    }

    public void setLoaiHinh(String loaiHinh) {
        LoaiHinh = loaiHinh;
    }

    public String getGiaThapNhat() {
        return GiaThapNhat;
    }

    public void setGiaThapNhat(String giaThapNhat) {
        GiaThapNhat = giaThapNhat;
    }

    public String getGiaCaoNhat() {
        return GiaCaoNhat;
    }

    public void setGiaCaoNhat(String giaCaoNhat) {
        GiaCaoNhat = giaCaoNhat;
    }

    public String getGioMoCua() {
        return GioMoCua;
    }

    public void setGioMoCua(String gioMoCua) {
        GioMoCua = gioMoCua;
    }

    public String getGioDongCua() {
        return GioDongCua;
    }

    public void setGioDongCua(String gioDongCua) {
        GioDongCua = gioDongCua;
    }

    public String getWifi() {
        return Wifi;
    }

    public void setWifi(String wifi) {
        Wifi = wifi;
    }

    public String getMatKhauWifi() {
        return MatKhauWifi;
    }

    public void setMatKhauWifi(String matKhauWifi) {
        MatKhauWifi = matKhauWifi;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}