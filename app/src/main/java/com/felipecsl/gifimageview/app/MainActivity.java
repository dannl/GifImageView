package com.felipecsl.gifimageview.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.felipecsl.gifimageview.library.ByteArrayGifImageView;
import com.felipecsl.gifimageview.library.StreamGifImageView;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ByteArrayGifImageView gifImageView;
    private StreamGifImageView streamImageView;
    private Button btnToggle;
    private Button btnBlur;
    private boolean shouldBlur = false;
    private Blur blur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gifImageView = (ByteArrayGifImageView) findViewById(R.id.gifImageView);
        streamImageView = (StreamGifImageView) findViewById(R.id.streamGifImageView);
        btnToggle = (Button) findViewById(R.id.btnToggle);
        btnBlur = (Button) findViewById(R.id.btnBlur);
        final Button btnClear = (Button) findViewById(R.id.btnClear);

        blur = Blur.newInstance(this);
        gifImageView.setOnFrameAvailable(new ByteArrayGifImageView.OnFrameAvailable() {
            @Override
            public Bitmap onFrameAvailable(Bitmap bitmap) {
                if (shouldBlur) {
                    return blur.blur(bitmap);
                }
                return bitmap;
            }
        });

        btnToggle.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnBlur.setOnClickListener(this);

        try {
            IOUtils.copy(getAssets().open("sample.gif"), new FileOutputStream("/sdcard/sample.gif"));
        } catch (IOException e) {
        }

        final FileInputStream in_0;
        try {
            in_0 = new FileInputStream("/sdcard/sample.gif");
            final int length = in_0.available();
            final byte[] buffer = new byte[length];
            in_0.read(buffer);
            IOUtils.closeQuietly(in_0);
            gifImageView.setBytes(buffer);
            gifImageView.startAnimation();;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final FileInputStream in_1;
        try {
            in_1 = new FileInputStream("/sdcard/sample.gif");
            streamImageView.setBytes(in_1);
            streamImageView.startAnimation();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //        new GifDataDownloader() {
//            @Override
//            protected void onPostExecute(final byte[] bytes) {
//                gifImageView.setBytes(bytes);
//                gifImageView.startAnimation();
//                Log.d(TAG, "GIF width is " + gifImageView.getGifWidth());
//                Log.d(TAG, "GIF height is " + gifImageView.getGifHeight());
//            }
//        }.execute("http://katemobile.ru/tmp/sample3.gif");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(final View v) {
        if (v.equals(btnToggle)) {
            if (gifImageView.isAnimating()) {
                gifImageView.stopAnimation();
            } else {
                gifImageView.startAnimation();
            }
        } else if (v.equals(btnBlur)) {
            shouldBlur = !shouldBlur;
        } else {
            gifImageView.clear();
        }
    }
}
