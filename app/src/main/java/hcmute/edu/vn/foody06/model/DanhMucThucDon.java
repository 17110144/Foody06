package hcmute.edu.vn.foody06.model;

public class DanhMucThucDon {
    private String TenDanhMuc;
    private int idQuan;

    public DanhMucThucDon() {
    }

    public DanhMucThucDon(String tenDanhMuc, int idQuan) {
        TenDanhMuc = tenDanhMuc;
        this.idQuan = idQuan;
    }

    public String getTenDanhMuc() {
        return TenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        TenDanhMuc = tenDanhMuc;
    }

    public int getIdQuan() {
        return idQuan;
    }

    public void setIdQuan(int idQuan) {
        this.idQuan = idQuan;
    }
}
