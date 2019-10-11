package com.liang.amapdemo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.AoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.RegeocodeRoad;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMapTouchListener {
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.iv_btn)
    ImageView ivBtn;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;

    private String userIcon = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3706852963,2399513353&fm=26&gp=0.jpg";

    public static final LatLng HANGZHOU = new LatLng(30.295877, 120.124046);// 杭州市经纬度

    /**
     * android 6.0 或以上权限申请
     */
    private static final int REQUEST_CODE = 0;                        //权限请求码

    //配置需要取的权限
    static final String[] PERMISSION = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private boolean mIsPermissionGanted = false;                    //是否授权成功
    private CheckPermission mCheckPermission;
    private UiSettings uiSettings;
    private List<Marker> markers;

    private boolean followMove = true;
    private Marker currentMarker;

    private Map<String, LatLng> poaitionMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //此方法必须重写
        //在activity执行onCreate时执行mapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        //初始化权限
        initPermission();
        getLocalData();
        if (mIsPermissionGanted) {
            initMapLocation();
        }
    }

    private void getLocalData() {
        LatLng baiduToGaode1 = TransBaiduGaodePoint.baidu_to_gaode(new LatLng(30.297316, 120.122481));
        LatLng baiduToGaode2 = TransBaiduGaodePoint.baidu_to_gaode(new LatLng(30.29748, 120.124008));
        LatLng baiduToGaode3 = TransBaiduGaodePoint.baidu_to_gaode(new LatLng(30.296825, 120.120635));
        LatLng baiduToGaode4 = TransBaiduGaodePoint.baidu_to_gaode(new LatLng(30.295304, 120.122315));
        LatLng baiduToGaode5 = TransBaiduGaodePoint.baidu_to_gaode(new LatLng(30.296142, 120.119764));

        User user1 = new User();
        user1.setHeadImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570685761527&di=242567a8fbb3b2d1b82233f5b4ef25ea&imgtype=0&src=http%3A%2F%2F00imgmini.eastday.com%2Fmobile%2F20190907%2F20190907172442_7db7e5407ac69b339ae40b3090632250_1.jpeg");
        user1.setName("马天宇");
        User user2 = new User();
        user2.setHeadImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570685798443&di=a7e17871d4c5a659e3cfb907030da273&imgtype=0&src=http%3A%2F%2Fp.store.itangyuan.com%2Fp%2Fbook%2Fcover%2F4B6v4gbueA%2FEg6TEBES4BjwEtMwEtMsETuReHemJhyVhA.jpg");
        user2.setName("马嘉琪");
        User user3 = new User();
        user3.setHeadImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570685823607&di=68c8e5c4d1f467f742b782b09532bb17&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201806%2F18%2F20180618235127_yglui.jpg");
        user3.setName("肖战");
        User user4 = new User();
        user4.setHeadImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570685871918&di=51e080ce2ccd92e9ea958d173273f9d1&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201808%2F21%2F20180821212620_hwmnx.thumb.700_0.jpg");
        user4.setName("丁程鑫");
        User user5 = new User();
        user5.setHeadImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570685489375&di=4ec083facefe99c5df83326e6fb3a169&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn12%2F300%2Fw1620h1080%2F20180816%2Ffab8-hhvciiv7482028.jpg");
        user5.setName("姚景元");

        ImActivity imActivity1 = new ImActivity();
        imActivity1.setTitle("马天宇");
        imActivity1.setDescribe("我本浪人，游吟世间");
        imActivity1.setPublisher(user1);
        imActivity1.setLatitude(baiduToGaode1.latitude);
        imActivity1.setLongitude(baiduToGaode1.longitude);

        ImActivity imActivity2 = new ImActivity();
        imActivity2.setTitle("马嘉琪");
        imActivity2.setDescribe("依然爱你");
        imActivity2.setPublisher(user2);
        imActivity2.setLatitude(baiduToGaode2.latitude);
        imActivity2.setLongitude(baiduToGaode2.longitude);

        ImActivity imActivity3 = new ImActivity();
        imActivity3.setTitle("肖战");
        imActivity3.setDescribe("无羁");
        imActivity3.setPublisher(user3);
        imActivity3.setLatitude(baiduToGaode3.latitude);
        imActivity3.setLongitude(baiduToGaode3.longitude);

        ImActivity imActivity4 = new ImActivity();
        imActivity4.setTitle("丁程鑫");
        imActivity4.setDescribe("For Him");
        imActivity4.setPublisher(user4);
        imActivity4.setLatitude(baiduToGaode4.latitude);
        imActivity4.setLongitude(baiduToGaode4.longitude);

        ImActivity imActivity5 = new ImActivity();
        imActivity5.setTitle("姚景元");
        imActivity5.setDescribe("我本浪人，游吟世间");
        imActivity5.setPublisher(user5);
        imActivity5.setLatitude(baiduToGaode5.latitude);
        imActivity5.setLongitude(baiduToGaode5.longitude);

        final List<ImActivity> localImActivities = new ArrayList<>();
        localImActivities.add(0, imActivity1);
        localImActivities.add(1, imActivity2);
        localImActivities.add(2, imActivity3);
        localImActivities.add(3, imActivity4);
        localImActivities.add(4, imActivity5);

        if (markers != null) {
            for (Marker marker : markers) {
                marker.remove();
            }
        }
        markers = new ArrayList<>();

        for (int i = 0; i < localImActivities.size(); i++) {

            if (!TextUtils.isEmpty(localImActivities.get(i).getPublisher().getHeadImageUrl())) {

                BitmapUtil.drawMark(localImActivities.get(i), localImActivities.get(i).getPublisher().getHeadImageUrl(),
                        144, true, new BitmapUtil.OnGetMapHeadListener() {
                            @Override
                            public void success(Bitmap bitmap, Object object) {
                                ImActivity imActivity = (ImActivity) object;
                                markers.add(aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap))));
                                markers.get(markers.size() - 1).setPosition(new LatLng(imActivity
                                        .getLatitude(), imActivity.getLongitude()));
                                startGrowAnimation(markers.get(markers.size() - 1));
                                //可以给定位点绑定一个信息对象
                                markers.get(markers.size() - 1).setObject(imActivity);
                                markers.get(markers.size() - 1).setInfoWindowEnable(true);
                                markers.get(markers.size() - 1).setTitle(imActivity.getPublisher().getName());
                                markers.get(markers.size() - 1).setSnippet(imActivity.getDescribe());
                                markers.get(markers.size() - 1).hideInfoWindow();
//                                markers.get(markers.size() - 1).showInfoWindow();
                            }

                            @Override
                            public void fail(Object object) {
                                ImActivity imActivity = (ImActivity) object;
                                markers.add(aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.drawMark(BitmapFactory
                                        .decodeResource(MainActivity.this.getResources(), R.mipmap.ic_launcher), 144, true)))));
                                markers.get(markers.size() - 1).setPosition(new LatLng(imActivity
                                        .getLatitude(), imActivity.getLongitude()));
                                startGrowAnimation(markers.get(markers.size() - 1));
                                //可以给定位点绑定一个信息对象
                                markers.get(markers.size() - 1).setObject(imActivity);
                                markers.get(markers.size() - 1).setInfoWindowEnable(true);
                                markers.get(markers.size() - 1).setTitle(imActivity.getPublisher().getName());
                                markers.get(markers.size() - 1).setSnippet(imActivity.getDescribe());
                                markers.get(markers.size() - 1).hideInfoWindow();
//                                markers.get(markers.size() - 1).showInfoWindow();
                            }
                        });
            } else {
                markers.add(aMap.addMarker(new MarkerOptions().icon(
                        BitmapDescriptorFactory.fromBitmap(BitmapUtil.drawMark(BitmapFactory.decodeResource(MainActivity.this
                                .getResources(), R.mipmap.ic_launcher), 144, true)))));
                markers.get(markers.size() - 1).setPosition(new LatLng(localImActivities.get(markers.size() - 1).getLatitude(),
                        localImActivities.get(markers.size() - 1).getLongitude()));
                startGrowAnimation(markers.get(markers.size() - 1));
                //可以给定位点绑定一个信息对象
                markers.get(markers.size() - 1).setObject(localImActivities.get(i));
                markers.get(markers.size() - 1).setInfoWindowEnable(true);
                markers.get(markers.size() - 1).setTitle(localImActivities.get(i).getPublisher().getName());
                markers.get(markers.size() - 1).setSnippet(localImActivities.get(i).getDescribe());
                markers.get(markers.size() - 1).hideInfoWindow();
//                markers.get(markers.size() - 1).showInfoWindow();
            }
        }

        Log.e("TAG", "本地数据成功!" + "\n" + new Gson().toJson(localImActivities));

    }

    private void initPermission() {
        //SDK版本小于23时候不做检测
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        mCheckPermission = new CheckPermission(this);
        //缺少权限时，进入权限设置页面
        if (mCheckPermission.permissionSet(PERMISSION)) {
            startPermissionActivity();
        } else {
            //定位权限已授权
            mIsPermissionGanted = true;
        }
    }

    private void startPermissionActivity() {
        PermissionActivity.startActivityForResult(this, REQUEST_CODE, PERMISSION);
    }

    private void initMapLocation() {

        //AMap类是地图的控制器类，用来操作地图。
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        //实例化UiSettings类对象
        uiSettings = aMap.getUiSettings();
        //缩放按钮(默认显示)
        uiSettings.setZoomControlsEnabled(true);
        //指南针(默认不显示)
        uiSettings.setCompassEnabled(true);

        //定位按钮
        //设置默认定位按钮是否显示，非必需设置。
        uiSettings.setMyLocationButtonEnabled(true);
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);


        myLocationStyle = new MyLocationStyle();

        //自定义蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));

        //精度圆圈的自定义：
        myLocationStyle.strokeColor(Color.argb(180, 3, 145, 255));//设置定位蓝点精度圆圈的边框颜色的方法
        myLocationStyle.radiusFillColor(Color.argb(10, 0, 0, 180));//设置定位蓝点精度圆圈的填充颜色的方法
        //精度圈边框宽度自定义方法
        myLocationStyle.strokeWidth(5);

        //初始化定位蓝点样式类
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        ////以下三种模式从5.1.0版本开始提供
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。

        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);

        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);

        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);

        //禁止地图旋转手势
        uiSettings.setRotateGesturesEnabled(false);
        //禁止倾斜手势
        uiSettings.setTiltGesturesEnabled(false);

        //比例尺控件
        uiSettings.setScaleControlsEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));

        //地图Logo
        uiSettings.setLogoLeftMargin(10000); // 隐藏logo

        //获取经纬度信息：
        aMap.setOnMyLocationChangeListener(this);

        aMap.setOnMapTouchListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mapView.onResume ()，重新绘制加载地图
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mapView.onPause ()，暂停地图的绘制
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mapView.onDestroy()，销毁地图
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mapView.onSaveInstanceState (outState)，保存地图当前的状态
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //权限请求码
            if (resultCode == PermissionActivity.PERMISSION_DENIEG) {
                //拒绝时，没有获取到权限，无法运行，关闭页面
                finish();
            } else {
                //定位权限授权成功
                mIsPermissionGanted = true;
                initMapLocation();
            }
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）
//        Log.d("MainActivity", "onMyLocationChange: " + new Gson().toJson(location) + "\n");

        //获取地址描述数据 - 逆地理编码（坐标转地址）
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        final LatLng latLng = new LatLng(latitude, longitude);
        final LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);


        if (followMove) {
            aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        }

        Log.e("定位点坐标", "onMyLocationChange: " + new Gson().toJson(latLonPoint));

        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);

        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);


        LatLng mapLng = poaitionMap.get("position");

        Log.e("数据分析", "onMyLocationChange: " + "\n" + "当前定位:  " + latLng + "\n" + "上次定位: " + mapLng);

        if (latitude == 0 && longitude == 0) {
            //定位失败
            Log.e("LocationChange", "onMyLocationChange: 定位失败  " + latLng);
        } else {
            if (!latLng.equals(mapLng)) {
                if (currentMarker != null) {
                    currentMarker.remove();
                }

                User currentUser = new User();
                currentUser.setName("TYT台风少年团");
                currentUser.setHeadImageUrl(userIcon);

                final ImActivity currentImActivity = new ImActivity();
                currentImActivity.setTitle("TYT台风少年团");
                currentImActivity.setDescribe("不做像谁一样的小孩\n" + "我只要像我一样存在");
                currentImActivity.setPublisher(currentUser);

                BitmapUtil.drawMark(userIcon, 144, false, new BitmapUtil.OnGetMapHeadListenerByUrl() {

                    @Override
                    public void success(Bitmap bitmap, final String imageUrl) {
                        Log.e("url", imageUrl);
                        currentMarker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(latLng));
                        startGrowAnimation(currentMarker);
                        //可以给定位点绑定一个信息对象
                        currentMarker.setObject(currentImActivity);
                        currentMarker.setInfoWindowEnable(true);
                        currentMarker.setTitle(currentImActivity.getTitle());
                        currentMarker.setSnippet(currentImActivity.getDescribe());
                        currentMarker.hideInfoWindow();
//                    currentMarker.showInfoWindow();
                    }

                    @Override
                    public void fail() {
                        Log.e("url", "解析图片失败");
                        Marker marker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)).position(latLng));
                        startGrowAnimation(marker);
                        //可以给定位点绑定一个信息对象
                        currentMarker.setObject(currentImActivity);
                        currentMarker.setInfoWindowEnable(true);
                        currentMarker.setTitle(currentImActivity.getTitle());
                        currentMarker.setSnippet(currentImActivity.getDescribe());
                        currentMarker.hideInfoWindow();
//                    currentMarker.showInfoWindow();
                    }
                });
                Log.e("LocationChange", "onMyLocationChange: 定位成功  " + "位置变化~");
            } else {
                Log.e("LocationChange", "onMyLocationChange: 定位成功  " + "位置没有变化~");
            }
            //存储本次定位经纬度
            poaitionMap.put("position", latLng);
        }


    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        RegeocodeQuery regeocodeQuery = regeocodeResult.getRegeocodeQuery();

        Log.d("Country", "onRegeocodeSearched: " + regeocodeAddress.getCountry());//国家
        Log.d("Province", "onRegeocodeSearched: " + regeocodeAddress.getProvince());//省
        Log.d("City", "onRegeocodeSearched: " + regeocodeAddress.getCity());//市
        Log.d("District", "onRegeocodeSearched: " + regeocodeAddress.getDistrict());//区行政单位
        Log.d("Township", "onRegeocodeSearched: " + regeocodeAddress.getTownship());//街道

        for (RegeocodeRoad road : regeocodeAddress.getRoads()) {
            Log.d("Roads", "onRegeocodeSearched: " + new Gson().toJson(road)); //路口街区
        }

        //浙江省杭州市西湖区古荡街道浙江深大智能科技有限公司天堂软件园
        Log.d("FormatAddress", "onRegeocodeSearched: " + regeocodeAddress.getFormatAddress());

        for (AoiItem aois : regeocodeAddress.getAois()) {
            Log.d("Aois", "onRegeocodeSearched: " + aois.getAoiName());
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        //用户拖动地图后，不再跟随移动，需要跟随移动时再把这个改成true
        if (followMove) {
            followMove = false;
        }
    }

    @OnClick(R.id.iv_btn)
    public void onViewClicked() {
        startActivity(new Intent(MainActivity.this, Main2Activity.class));
    }

    /**
     * 地上生长的Marker
     */
    private void startGrowAnimation(Marker growMarker) {
        if (growMarker != null) {
            Animation animation = new ScaleAnimation(0, 1, 0, 1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            //设置动画
            growMarker.setAnimation(animation);
            //开始动画
            growMarker.startAnimation();
        }
    }
}
