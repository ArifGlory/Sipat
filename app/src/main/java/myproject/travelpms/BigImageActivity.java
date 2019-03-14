package myproject.travelpms;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.piasy.biv.view.BigImageView;

public class BigImageActivity extends AppCompatActivity {

    BigImageView bigImageView;
    private String url;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BigImageViewer.initialize(GlideImageLoader.with(this));
        setContentView(R.layout.activity_big_image);


        i = getIntent();
        url =  i.getStringExtra("url");

        bigImageView = findViewById(R.id.mBigImage);
        bigImageView.showImage(Uri.parse(url));
    }
}
