package hcmute.edu.vn.foody06.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.adapter.ChonTinhThanhAdapter;
import hcmute.edu.vn.foody06.adapter.ThucDonAdapter;
import hcmute.edu.vn.foody06.model.Mon;

import static hcmute.edu.vn.foody06.view.MainActivity.db;

public class ThucDonActivity extends AppCompatActivity {

    private TextView txtTenQuan;
    private int IdQuan;
    private String TenQuan;
    ArrayList<Mon> arrayMon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thuc_don);

        Button btnBack = (Button) findViewById(R.id.btnBack_thucdon);
        txtTenQuan = (TextView) findViewById(R.id.txttenquan_thucdon);
        ListView lsvThucDon = (ListView) findViewById(R.id.lstviewthucdon);
        nhanDuLieu();
        capnhaDuLieu();


        arrayMon = new ArrayList<>();
        getAllDataMonTheoQuan();

        ThucDonAdapter adapter = new ThucDonAdapter(this, arrayMon);
        lsvThucDon.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAllDataMonTheoQuan() {
        Cursor dataQuan = db.GetData("SELECT * FROM Mon Where IdQuan ="+IdQuan+";");
        arrayMon.clear();
        while (dataQuan.moveToNext()){
            String urlanh = dataQuan.getString(5);
            String gia = dataQuan.getString(4);
            String danhmuc = dataQuan.getString(3);
            int idquan = dataQuan.getInt(2);
            String tenmon = dataQuan.getString(1);
            int id = dataQuan.getInt(0);
            arrayMon.add(new Mon(id,tenmon,idquan,danhmuc,gia,urlanh));
        }
    }

    private void nhanDuLieu() {
        //Nhận dữ liệu từ Intent trước đó
        Intent intent = getIntent();
        IdQuan = Objects.requireNonNull(intent.getExtras()).getInt("IdQuan");
        TenQuan = intent.getExtras().getString("TenQuan");
    }
    private void capnhaDuLieu(){
        txtTenQuan.setText(TenQuan);
    }
}