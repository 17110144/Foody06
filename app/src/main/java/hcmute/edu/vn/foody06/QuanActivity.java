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

        tvtitle = (TextView) findViewById(R.id.txttilte);
        tvdescription = (TextView) findViewById(R.id.txtdescription);
        tvcategory = (TextView) findViewById(R.id.txtcategory);
        img = (ImageView) findViewById(R.id.quanthumbnail);

        //Recive data
        Intent intent = getIntent();
        String Title = Objects.requireNonNull(intent.getExtras()).getString("Title");
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
