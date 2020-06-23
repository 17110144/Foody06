package hcmute.edu.vn.foody06.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.model.TinhThanh;

public class ChonTinhThanhAdapter extends BaseAdapter {

    Activity activity;
    List<TinhThanh> tinhthanhs;
    LayoutInflater inflater;


    public ChonTinhThanhAdapter(Activity activity) {
        this.activity = activity;
    }

    public ChonTinhThanhAdapter(Activity activity, List<TinhThanh> tinhthanhs) {
        this.activity   = activity;
        this.tinhthanhs = tinhthanhs;
        inflater        = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return tinhthanhs.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_view_tinhthanh,parent,false);
            holder = new ViewHolder();
            holder.tenTinhThanh   = (TextView) convertView.findViewById(R.id.txttentinhthanh_listview_tinhthanh);
            holder.imgCheckBox    = (ImageView) convertView.findViewById(R.id.imgcheckbox_listview_tinhthanh);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        TinhThanh tinhThanh = tinhthanhs.get(position);

        holder.tenTinhThanh.setText(tinhthanhs.get(position).getTenTinhThanh());
        if(tinhThanh.isSelected())
            holder.imgCheckBox.setBackgroundResource(R.drawable.ic_checked_24);
        else{
            holder.imgCheckBox.setBackgroundResource(R.drawable.ic_null);
        }
        return convertView;

    }
    public void updateRecord(List<TinhThanh> tinhthanhs){
        this.tinhthanhs = tinhthanhs;
        notifyDataSetChanged();
    }
    private static class ViewHolder{
        TextView tenTinhThanh;
        ImageView imgCheckBox;
    }
}
