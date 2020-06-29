package hcmute.edu.vn.foody06.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.adapter.ThucDonPageAdapter;


public class ThucDonActivity extends AppCompatActivity {

    private TextView txtTenQuan;
    public static int IdQuan;
    private String TenQuan;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thuc_don);

        viewPager = findViewById(R.id.viewpage_thucdon);
        viewPager.setAdapter(new ThucDonPageAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_thucdon);
        tabLayout.setupWithViewPager(viewPager);

        Button btnBack = findViewById(R.id.btnBack_thucdon);
        txtTenQuan =  findViewById(R.id.txttenquan_thucdon);


        nhanDuLieu();
        capnhaDuLieu();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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