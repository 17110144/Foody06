package hcmute.edu.vn.foody06;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class QuanActivity extends AppCompatActivity {

    private TextView tvtitle, tvdescription, tvcategory;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan);

        tvtitle = (TextView) findViewById(R.id.txtTilte_id);
        tvdescription = (TextView) findViewById(R.id.txtDes_id);
        tvcategory = (TextView) findViewById(R.id.txtCategory_id);
        img = (ImageView) findViewById(R.id.quanthumbnail);

        //Recive data
        Intent intent = getIntent();
        String Title = Objects.requireNonNull(intent.getExtras()).getString("QuanTitle");
        String Description = intent.getExtras().getString("Description");
        String Category = intent.getExtras().getString("Category");
        int image = intent.getExtras().getInt("Thumbmail");

        //Setting values
        tvtitle.setText(Title);
        tvdescription.setText(Description);
        tvcategory.setText(Category);
        img.setImageResource(image);
    }
}
