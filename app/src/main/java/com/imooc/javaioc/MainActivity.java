package com.imooc.javaioc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.imooc.base.CheckNet;
import com.imooc.base.OnClick;
import com.imooc.base.ViewById;
import com.imooc.base.ViewUtils;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.tv_view)
    TextView textView;

    @ViewById(R.id.image_view)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 注入Activity
        ViewUtils.inject(this);
        textView.setText("IOC");
    }

    @OnClick({R.id.tv_view,R.id.image_view})
    @CheckNet
    void onClick(View view){
        if (view.getId() == R.id.tv_view){
            textOnclick();
        }else{
            imageOnclick();
        }

    }
    @CheckNet
    private void textOnclick() {
        Toast.makeText(this,"textOnclick", Toast.LENGTH_SHORT).show();
    }
    private void imageOnclick() {
        Toast.makeText(this,"imageOnclick", Toast.LENGTH_SHORT).show();
    }
}