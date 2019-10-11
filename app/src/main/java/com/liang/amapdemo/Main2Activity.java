package com.liang.amapdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.iv_icon)
    ImageView ivIcon;

    private String userIcon1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570685489375&di=4ec083facefe99c5df83326e6fb3a169&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn12%2F300%2Fw1620h1080%2F20180816%2Ffab8-hhvciiv7482028.jpg";
    private String userIcon2 = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3706852963,2399513353&fm=26&gp=0.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        Glide.with(this).load(userIcon1).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(ivIcon);
    }
}
