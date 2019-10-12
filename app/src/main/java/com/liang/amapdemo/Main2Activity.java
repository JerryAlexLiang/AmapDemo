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
    private String userIcon3 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570685761527&di=242567a8fbb3b2d1b82233f5b4ef25ea&imgtype=0&src=http%3A%2F%2F00imgmini.eastday.com%2Fmobile%2F20190907%2F20190907172442_7db7e5407ac69b339ae40b3090632250_1.jpeg";
    private String userIcon4 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570685798443&di=a7e17871d4c5a659e3cfb907030da273&imgtype=0&src=http%3A%2F%2Fp.store.itangyuan.com%2Fp%2Fbook%2Fcover%2F4B6v4gbueA%2FEg6TEBES4BjwEtMwEtMsETuReHemJhyVhA.jpg";
    private String userIcon5 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570685823607&di=68c8e5c4d1f467f742b782b09532bb17&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201806%2F18%2F20180618235127_yglui.jpg";
    private String userIcon6 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570685871918&di=51e080ce2ccd92e9ea958d173273f9d1&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201808%2F21%2F20180821212620_hwmnx.thumb.700_0.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        Glide.with(this).load(userIcon6).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(ivIcon);
    }
}
