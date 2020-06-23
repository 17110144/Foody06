package hcmute.edu.vn.foody06.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.adapter.ChonTinhThanhAdapter;
import hcmute.edu.vn.foody06.model.TinhThanh;

public class TinhThanhActivity extends AppCompatActivity {

    Button btnHuy,btnXacNhan;
    ListView lsvTinhThanh;
    List<TinhThanh> tinhthanhs;
    int preSelectedIndex = -1;
    String TinhThanhDuocChon = "TP HCM";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinh_thanh);

        btnHuy = (Button) findViewById(R.id.btnhuy_tim_tinhthanh);
        btnXacNhan = (Button) findViewById(R.id.btnxong_tim_tinhthanh);
        lsvTinhThanh = (ListView) findViewById(R.id.lstview_chon_tinhthanh);

        themTinhThanh();


        final ChonTinhThanhAdapter adapter = new ChonTinhThanhAdapter(this,tinhthanhs);
        lsvTinhThanh.setAdapter(adapter);

        lsvTinhThanh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TinhThanh tinhThanh = tinhthanhs.get(position);
                tinhThanh.setSelected(true);
                tinhthanhs.set(position,tinhThanh);
                if(preSelectedIndex > -1){
                    TinhThanh preRecord = tinhthanhs.get(preSelectedIndex);
                    preRecord.setSelected(false);

                    tinhthanhs.set(preSelectedIndex,preRecord);
                }
                preSelectedIndex = position;
                adapter.updateRecord(tinhthanhs);
                TinhThanhDuocChon = tinhThanh.getTenTinhThanh();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.tenTinhThanh=TinhThanhDuocChon;
                finish();
            }
        });
    }


    private void themTinhThanh()
    {
        tinhthanhs = new ArrayList<>();
        tinhthanhs.add(new TinhThanh(false,"TP HCM"));
        tinhthanhs.add(new TinhThanh(false,"Hà Nội"));
        tinhthanhs.add(new TinhThanh(false,"Hải Phòng"));
        tinhthanhs.add(new TinhThanh(false,"Đà Nẵng"));
        tinhthanhs.add(new TinhThanh(false,"Cần Thơ"));
        tinhthanhs.add(new TinhThanh(false,"An Giang"));
        tinhthanhs.add(new TinhThanh(false,"Bà Rịa"));
        tinhthanhs.add(new TinhThanh(false,"Vũng Tàu"));
        tinhthanhs.add(new TinhThanh(false,"Bắc Giang"));
        tinhthanhs.add(new TinhThanh(false,"Bắc Kạn"));
        tinhthanhs.add(new TinhThanh(false,"Bạc Liêu"));
        tinhthanhs.add(new TinhThanh(false,"Bắc Ninh"));
        tinhthanhs.add(new TinhThanh(false,"Bến Tre"));
        tinhthanhs.add(new TinhThanh(false,"Bình Định"));
        tinhthanhs.add(new TinhThanh(false,"Bình Dương"));
        tinhthanhs.add(new TinhThanh(false,"Bình Phước"));
        tinhthanhs.add(new TinhThanh(false,"Bình Thuận"));
        tinhthanhs.add(new TinhThanh(false,"Cà Mau"));
        tinhthanhs.add(new TinhThanh(false,"Cao Bằng"));
        tinhthanhs.add(new TinhThanh(false,"Đắk Lắk"));
        tinhthanhs.add(new TinhThanh(false,"Đắk Nông"));
        tinhthanhs.add(new TinhThanh(false,"Điện Biên"));
        tinhthanhs.add(new TinhThanh(false,"Đồng Nai"));
        tinhthanhs.add(new TinhThanh(false,"Đồng Tháp"));
        tinhthanhs.add(new TinhThanh(false,"Gia Lai"));
        tinhthanhs.add(new TinhThanh(false,"Hà Giang"));
        tinhthanhs.add(new TinhThanh(false,"Hà Nam"));
        tinhthanhs.add(new TinhThanh(false,"Hà Tĩnh"));
        tinhthanhs.add(new TinhThanh(false,"Hải Dương"));
        tinhthanhs.add(new TinhThanh(false,"Hậu Giang"));
        tinhthanhs.add(new TinhThanh(false,"Hòa Bình"));
        tinhthanhs.add(new TinhThanh(false,"Hưng Yên"));
        tinhthanhs.add(new TinhThanh(false,"Khánh Hòa"));
        tinhthanhs.add(new TinhThanh(false,"Kiên Giang"));
        tinhthanhs.add(new TinhThanh(false,"Kon Tum"));
        tinhthanhs.add(new TinhThanh(false,"Lai Châu"));
        tinhthanhs.add(new TinhThanh(false,"Lâm Đồng"));
        tinhthanhs.add(new TinhThanh(false,"Lạng Sơn"));
        tinhthanhs.add(new TinhThanh(false,"Lào Cai"));
        tinhthanhs.add(new TinhThanh(false,"Long An"));
        tinhthanhs.add(new TinhThanh(false,"Nam Định"));
        tinhthanhs.add(new TinhThanh(false,"Nghệ An"));
        tinhthanhs.add(new TinhThanh(false,"Ninh Bình"));
        tinhthanhs.add(new TinhThanh(false,"Ninh Thuận"));
        tinhthanhs.add(new TinhThanh(false,"Phú Thọ"));
        tinhthanhs.add(new TinhThanh(false,"Quảng Bình"));
        tinhthanhs.add(new TinhThanh(false,"Quảng Nam"));
        tinhthanhs.add(new TinhThanh(false,"Quảng Ngãi"));
        tinhthanhs.add(new TinhThanh(false,"Quảng Ninh"));
        tinhthanhs.add(new TinhThanh(false,"Quảng Trị"));
        tinhthanhs.add(new TinhThanh(false,"Sóc Trăng"));
        tinhthanhs.add(new TinhThanh(false,"Sơn La"));
        tinhthanhs.add(new TinhThanh(false,"Tây Ninh"));
        tinhthanhs.add(new TinhThanh(false,"Thái Bình"));
        tinhthanhs.add(new TinhThanh(false,"Thái Nguyên"));
        tinhthanhs.add(new TinhThanh(false,"Thanh Hóa"));
        tinhthanhs.add(new TinhThanh(false,"Thừa Thiên Huế"));
        tinhthanhs.add(new TinhThanh(false,"Tiền Giang"));
        tinhthanhs.add(new TinhThanh(false,"Trà Vinh"));
        tinhthanhs.add(new TinhThanh(false,"Tuyên Quang"));
        tinhthanhs.add(new TinhThanh(false,"Vĩnh Long"));
        tinhthanhs.add(new TinhThanh(false,"Vĩnh Phúc"));
        tinhthanhs.add(new TinhThanh(false,"Yên Bái"));
        tinhthanhs.add(new TinhThanh(false,"Phú Yên"));
    }
}