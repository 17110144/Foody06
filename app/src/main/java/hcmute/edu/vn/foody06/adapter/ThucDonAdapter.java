package hcmute.edu.vn.foody06.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.model.Mon;

public class ThucDonAdapter extends BaseAdapter {
    Activity activity;
    List<Mon> Mons;
    LayoutInflater inflater;


    public ThucDonAdapter(Activity activity) {
        this.activity = activity;
    }

    public ThucDonAdapter(Activity activity, List<Mon> Mons) {
        this.activity   = activity;
        this.Mons       = Mons;
        inflater        = activity.getLayoutInflater();
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
        ThucDonAdapter.ViewHolder holder = null;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_view_thucdon,parent,false);
            holder = new ThucDonAdapter.ViewHolder();
            holder.tenMon   =  convertView.findViewById(R.id.txttenmon_listview_thucdon);
            holder.giaMon   =  convertView.findViewById(R.id.txtgia_listview_thucdon);
            convertView.setTag(holder);
        }else{
            holder = (ThucDonAdapter.ViewHolder) convertView.getTag();
        }

        Mon Mon = Mons.get(position);

        holder.tenMon.setText(Mon.getTenMon());
        holder.giaMon.setText(Mon.getGia()+ " đ");

        return convertView;

    }

    private static class ViewHolder{
        TextView tenMon;
        TextView giaMon;
    }
}
