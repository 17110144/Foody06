package hcmute.edu.vn.foody06.adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.model.Mon;

public class ThucDonMonAdapter extends BaseAdapter {
    Fragment dsMon;
    List<Mon> Mons;
    LayoutInflater inflater;

    public ThucDonMonAdapter(Fragment dsMon, List<Mon> Mons) {
        this.dsMon      = dsMon;
        this.Mons       = Mons;
        inflater        = dsMon.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return Mons.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ThucDonMonAdapter.ThucDonMonViewHolder holder = null;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_view_thucdon,parent,false);
            holder = new ThucDonMonAdapter.ThucDonMonViewHolder();
            holder.tenMon   =  convertView.findViewById(R.id.txttenmon_listview_thucdon);
            holder.giaMon   =  convertView.findViewById(R.id.txtgia_listview_thucdon);
            convertView.setTag(holder);
        }else{
            holder = (ThucDonMonAdapter.ThucDonMonViewHolder) convertView.getTag();
        }

        Mon Mon = Mons.get(position);

        holder.tenMon.setText(Mon.getTenMon());
        holder.giaMon.setText(Mon.getGia()+ " đ");

        return convertView;

    }

    private static class ThucDonMonViewHolder{
        TextView tenMon;
        TextView giaMon;
    }
}
