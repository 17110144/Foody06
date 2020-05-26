package hcmute.edu.vn.foody06;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Tạo database
        db = new Database(this,"foody.sqlite",null,1);

        //Tạo bảng theo tuần tự vì có quan hệ khóa ngoại
        db.QueryData("CREATE TABLE IF NOT EXISTS QUAN(Id INTEGER PRIMARY KEY NOT NULL UNIQUE, Ten STRING NOT NULL, DiaChi STRING NOT NULL, ThanhPho STRING NOT NULL, LoaiHinh STRING NOT NULL, GioMoCua TIME NOT NULL, GioDongCua TIME NOT NULL, Wifi STRING, MatKhauWifi STRING) WITHOUT ROWID;");
        db.QueryData("CREATE TABLE IF NOT EXISTS DANHMUCTHUCDON (Id INTEGER PRIMARY KEY UNIQUE NOT NULL, IdQuan INTEGER REFERENCES QUAN (Id) NOT NULL, TenDanhMuc STRING NOT NULL) WITHOUT ROWID;");
        db.QueryData("CREATE TABLE IF NOT EXISTS MON (Id INTEGER PRIMARY KEY UNIQUE NOT NULL, IdDanhMuc INTEGER NOT NULL REFERENCES DanhMucThucDon (Id), Gia INTEGER NOT NULL, HinhAnh STRING) WITHOUT ROWID;");

    }
}
