package hcmute.edu.vn.foody06.view;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.adapter.ThucDonMonAdapter;
import hcmute.edu.vn.foody06.model.Mon;

import static hcmute.edu.vn.foody06.view.MainActivity.db;

public class ThucDonDSMon  extends Fragment {
    private View mRootView;
    ThucDonMonAdapter adapter;
    ArrayList<Mon> arrayMon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_thucdon_dsmon,container,false);
        initView();
        return mRootView;
    }
    private void initView() {
        ListView lsvThucDon = (ListView) mRootView.findViewById(R.id.lstviewthucdon);
        arrayMon = new ArrayList<>();
        getAllDataMonTheoQuan();
        adapter = new ThucDonMonAdapter(this, arrayMon);
        lsvThucDon.setAdapter(adapter);
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
