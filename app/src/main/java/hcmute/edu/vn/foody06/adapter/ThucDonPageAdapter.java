package hcmute.edu.vn.foody06.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import hcmute.edu.vn.foody06.view.ThucDonDSAnh;
import hcmute.edu.vn.foody06.view.ThucDonDSMon;

public class ThucDonPageAdapter extends FragmentStatePagerAdapter {
    private String[] listTab = {"Hình ảnh","Thực đơn"};
    private ThucDonDSAnh mThucDonDSAnh;
    private ThucDonDSMon mThucDonDSMon;

    public ThucDonPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mThucDonDSAnh = new ThucDonDSAnh();
        mThucDonDSMon = new ThucDonDSMon();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return mThucDonDSAnh;
        }
        else if (position == 1){
            return mThucDonDSMon;
        }
        return null;
    }

    @Override
    public int getCount() {
        return listTab.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTab[position];
    }
}
