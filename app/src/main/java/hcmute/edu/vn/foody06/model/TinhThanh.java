package hcmute.edu.vn.foody06.model;

public class TinhThanh {
    boolean isSelected;
    String tenTinhThanh;

    public TinhThanh(boolean isSelected, String tenTinhThanh) {
        this.isSelected = isSelected;
        this.tenTinhThanh = tenTinhThanh;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTenTinhThanh() {
        return tenTinhThanh;
    }

    public void setTenTinhThanh(String tenTinhThanh) {
        this.tenTinhThanh = tenTinhThanh;
    }
}
