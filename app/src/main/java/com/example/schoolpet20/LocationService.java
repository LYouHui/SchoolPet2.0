package com.example.schoolpet20;
 
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

public class LocationService extends Service implements SensorEventListener {
    LatLng classroom = new LatLng(31.887942979600695F,118.82358588324652F );
    LatLng canteen = new LatLng(31.884076877170138F,118.82623399522569F);
    LatLng sportroom = new LatLng(31.888611F,118.821388F);//不准
    LatLng playground= new LatLng(31.885022F,118.821533F );//应该不会准
    LatLng restroom=new LatLng(31.882306F,118.826413F);

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    public String data=new String();
    private CallBack callback;

    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    String data=new String();
                    LatLng location = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude() );
                    if(AMapUtils.calculateLineDistance(location,classroom)<200) {
                        //教室
                        data="classroom";
                    }else if(AMapUtils.calculateLineDistance(location,canteen)<150)
                    {
                        //食堂
                        data="cartoon";
                    }else if(AMapUtils.calculateLineDistance(location,sportroom)<20)
                    {
                        //钢菊
                        data="playroom";
                    }else if(AMapUtils.calculateLineDistance(location,playground)<100)
                    {
                        //操场
                        data="playground";
                    }else if(AMapUtils.calculateLineDistance(location,restroom)<100) {
                        //宿舍
                        data="restroom";
                    }
                    else{
                        data="everywhere";
                    }
                    if(callback!=null){
                        LocationService.this.data=data;
                        callback.onDataChange(data);
                    }
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }

            }
        }
    };
    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，低耗电模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationOption.setOnceLocation(true);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class);
        Notification notification=new Notification.Builder(this.getApplicationContext())
                .setContentText("仔仔活在后台，主人不要终止服务")
                .setContentTitle("校宠乐园")
                .setSmallIcon(R.mipmap.app)
                .setWhen(System.currentTimeMillis())
                .build();
        startForeground(110,notification);
        return super.onStartCommand(intent, flags, Service.START_REDELIVER_INTENT);
    }

    public class Binder extends android.os.Binder{
        public void setData(String data){
            LocationService.this.data = data;
        }
        public LocationService getService(){
            return LocationService.this;
        }
    }
    public void setCallback(CallBack callback) {
        this.callback = callback;
    }

    public CallBack getCallback() {
        return callback;
    }

    public static  interface CallBack{
        void onDataChange(String data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        stopForeground(true);
    }

    private long lastUpdateTime;
    private float lastX;
    private float lastY;
    private float lastZ;
    @Override
    public void onSensorChanged(SensorEvent event) {
//        Log.i("加速度", "onSensorChanged: " + event.values[0] + ", " + event.values[1] + ", " + event.values[2]);
        long currentUpdateTime = System.currentTimeMillis();

        long timeInterval = currentUpdateTime - lastUpdateTime;

        if (timeInterval < 100) {
            return;
        }

        lastUpdateTime = currentUpdateTime;
        float[] values = event.values;

        // 获得x,y,z加速度
        float x = values[0];
        float y = values[1];
        float z = values[2];

        // 获得x,y,z加速度的变化值
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        // 将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;

        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000;

        if (speed > 200)
        {
            Log.i("速度", ""+speed);
//            mLocationClient = new AMapLocationClient(getApplicationContext());
//            //设置定位回调监听
//            mLocationClient.setLocationListener(mLocationListener);
//            //初始化AMapLocationClientOption对象
//            mLocationOption = new AMapLocationClientOption();
//            //设置定位模式为AMapLocationMode.Hight_Accuracy，低耗电模式。
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//            //给定位客户端对象设置定位参数
//            mLocationClient.setLocationOption(mLocationOption);
//            mLocationOption.setOnceLocation(true);
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
