package hcmute.edu.vn.foody06;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class QuanActivity extends AppCompatActivity {

    private TextView txtTitle, txtDes, txtCate;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_quan );

        txtTitle = (TextView) findViewById( R.id.txtTilte_id );
        txtDes = (TextView) findViewById( R.id.txtDes_id );
        txtCate = (TextView) findViewById( R.id.txtCategory_id );
        img= (ImageView) findViewById( R.id.quanthumbnail );

        //receive data
        Intent intent = getIntent();
        String title = intent.getExtras().getString( "Title" );
        String des = intent.getExtras().getString( "Description" );
        int image = intent.getExtras().getInt( "Thumbnail" );

        //setting value

        txtTitle.setText( title );
        txtDes.setText( des );
        img.setImageResource( image );
    }
}
