package hcmute.edu.vn.foody06;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Quan> mData;

    public RecyclerViewAdapter(Context mcontext, List<Quan> mData)
    {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater nInflater = LayoutInflater.from(mContext);
        view= nInflater.inflate(R.layout.cardview_item_quan,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tv_quan_title.setText( mData.get( position ).getTitle() );
        holder.img_quan_thumbnail.setImageResource( mData.get( position ).getThumbnail() );
        //set click
        holder.cardView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mContext,QuanActivity.class );

                //passing data to the quan activity
                intent.putExtra( "Title",mData.get(position).getTitle() );
                intent.putExtra( "Description",mData.get( position ).getDescription() );
                intent.putExtra( "Thumbnail",mData.get( position ).getThumbnail() );

                //start the activity
                mContext.startActivity( intent );
            }
        } );



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_quan_title;
        ImageView img_quan_thumbnail;
        CardView cardView;


        public MyViewHolder(@NonNull View itemView) {
            super( itemView );

            tv_quan_title = (TextView) itemView.findViewById( R.id.quan_title_id );
            img_quan_thumbnail = (ImageView) itemView.findViewById( R.id.img_quan );
            cardView = (CardView) itemView.findViewById( R.id.cardview_id );
        }
    }


}
