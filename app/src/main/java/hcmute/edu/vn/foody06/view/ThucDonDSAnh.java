package hcmute.edu.vn.foody06.view;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.adapter.MonAdapter;
import hcmute.edu.vn.foody06.model.Mon;

import static hcmute.edu.vn.foody06.view.MainActivity.db;

public class ThucDonDSAnh extends Fragment {
    RecyclerView rvMon;
    ArrayList<Mon> arrayMon;
    MonAdapter monAdapter;

    private View mRootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_thucdon_hinhanh,container,false);
        initView();
        return mRootView;
    }
    private void initView() {
        rvMon = mRootView.findViewById(R.id.recyclerView_mon_thucdon);
        arrayMon = new ArrayList<>();
        getAllDataMonTheoQuan();
        monAdapter = new MonAdapter(this.getContext(),arrayMon);
        rvMon.setLayoutManager(new GridLayoutManager(this.getContext(),2));
        rvMon.setAdapter(monAdapter);
    }

    private void getAllDataMonTheoQuan() {
        Cursor dataQuan = db.GetData("SELECT * FROM Mon Where IdQuan ="+ThucDonActivity.IdQuan+";");
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
}