package hcmute.edu.vn.foody06.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.adapter.TimKiemAdapter;
import hcmute.edu.vn.foody06.model.Quan;
import static hcmute.edu.vn.foody06.view.MainActivity.tenTinhThanh;

public class TimQuanActivity extends AppCompatActivity {

    EditText editTextTimQuan;
    Button btnChonTinhThanh,btnBack;

    RecyclerView rvkqtkQuan;
    TimKiemAdapter tkAdapter;

    ArrayList<Quan> dsQuanTheoTinhThanh;
    ArrayList<Quan> dsQuanTheoKQTK;
    ArrayList<Quan> dsQuanHienThiKQTK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_quan);

        editTextTimQuan = findViewById(R.id.txttimkiem_quan);
        btnChonTinhThanh = findViewById(R.id.btn_chonthanhpho_timkiem);
        btnBack = findViewById(R.id.btnBack_timkiem);
        rvkqtkQuan = findViewById(R.id.recyclerview_timkiem);

        btnChonTinhThanh.setText(tenTinhThanh);
        editTextTimQuan.requestFocus();



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnChonTinhThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chontinhthanh = new Intent(TimQuanActivity.this,TinhThanhActivity.class);
                startActivity(chontinhthanh);
            }
        });

        getAllDataQuanTheoTinhThanh();
        copyData();
        tkAdapter = new TimKiemAdapter(this,TimQuanActivity.this, dsQuanTheoTinhThanh);
        rvkqtkQuan.setLayoutManager(new GridLayoutManager(this, 1));
        rvkqtkQuan.setAdapter(tkAdapter);


        editTextTimQuan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                {
                    dsQuanTheoKQTK.clear();
                    tkAdapter = new TimKiemAdapter(TimQuanActivity.this,TimQuanActivity.this, dsQuanTheoKQTK);
                    rvkqtkQuan.setLayoutManager(new GridLayoutManager(TimQuanActivity.this, 1));
                    rvkqtkQuan.setAdapter(tkAdapter);
                }
                else{
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    protected void onPostResume() {
        btnChonTinhThanh.setText(tenTinhThanh);
        getAllDataQuanTheoTinhThanh();
        tkAdapter = new TimKiemAdapter(this,TimQuanActivity.this, dsQuanTheoTinhThanh);
        rvkqtkQuan.setLayoutManager(new GridLayoutManager(this, 1));
        rvkqtkQuan.setAdapter(tkAdapter);
        super.onPostResume();
    }

    private void getAllDataQuanTheoTinhThanh(){
        Cursor dataQuan = MainActivity.db.GetData("SELECT * FROM QUAN Where ThanhPho ='"+tenTinhThanh+"';");
        dsQuanTheoTinhThanh = new ArrayList<>();
        dsQuanTheoTinhThanh.clear();
        while (dataQuan.moveToNext()){
            String gioithieu = dataQuan.getString(13);
            String sdt = dataQuan.getString(12);
            String matkhauwifi = dataQuan.getString(11);
            String tenwifi = dataQuan.getString(10);
            String giodongcua = dataQuan.getString(9);
            String giomocua = dataQuan.getString(8);
            String giacaonhat = dataQuan.getString(7);
            String giathapnhat = dataQuan.getString(6);
            String loaihinh = dataQuan.getString(5);
            String thanhpho = dataQuan.getString(4);
            String diachi = dataQuan.getString(3);
            String anhdaidien = dataQuan.getString(2);
            String tenquan = dataQuan.getString(1);
            int id = dataQuan.getInt(0);
            dsQuanTheoTinhThanh.add(new Quan(id,tenquan,anhdaidien,diachi,thanhpho,loaihinh,giathapnhat,giacaonhat,giomocua,giodongcua,tenwifi,matkhauwifi,sdt,gioithieu));
        }
    }

    private void searchItem(String textToSearch) {
        textToSearch = textToSearch.toLowerCase();
        for(int i = 0; i < dsQuanTheoTinhThanh.size(); i++){
            if(dsQuanTheoTinhThanh.get(i).getTenQuan().toLowerCase().contains(textToSearch) || dsQuanTheoTinhThanh.get(i).getDiaChi().toLowerCase().contains(textToSearch)){
                dsQuanTheoKQTK.add(dsQuanTheoTinhThanh.get(i));
            }
        }
        dsQuanHienThiKQTK = new ArrayList<>(dsQuanTheoKQTK);
        tkAdapter = new TimKiemAdapter(this,TimQuanActivity.this, dsQuanHienThiKQTK);
        rvkqtkQuan.setLayoutManager(new GridLayoutManager(this, 1));
        rvkqtkQuan.setAdapter(tkAdapter);
        dsQuanTheoKQTK.clear();
    }

    private void copyData(){
        dsQuanTheoKQTK = new ArrayList<>(dsQuanTheoTinhThanh);
        dsQuanHienThiKQTK = new ArrayList<>(dsQuanTheoTinhThanh);
    }
}