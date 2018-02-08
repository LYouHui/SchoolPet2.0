package floatWindow;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.schoolpet20.LocationService;
import com.example.schoolpet20.R;

import java.util.ArrayList;

public class FloatService extends Service {
    private static final String TAG = "MainService";
    private MyServiceConnection myServiceConnection;
    private LocationService.Binder binder;
    public ConstraintLayout toucherLayout;
    public ConstraintLayout float_window_button1;
    public ConstraintLayout float_window_button2;
    public ConstraintLayout float_window_button3;
    public ConstraintLayout float_window_button4;
    public ImageView imageView;
    public ImageButton imageButton1;
    public ImageButton imageButton2;
    public ImageButton imageButton3;
    public ImageButton imageButton4;

    private ArrayList<WindowManager.LayoutParams> params=new ArrayList<WindowManager.LayoutParams>();
    private WindowManager.LayoutParams mainWindow_params = new WindowManager.LayoutParams();
    public WindowManager windowManager;
    private int animNumber=R.drawable.anim_common;
    private static String Location;
    private int statusBarHeight = -1;    //状态栏高度.
    private int startX;
    private int startY;
    private int floatButtonAmount=4;
    private boolean haveFloatWindowButton=false;
    private boolean isMoving=false;
    private AlphaAnimation alphaAnim0_1 = new AlphaAnimation(0.0f, 1.0f);

    private MyHander handler = new MyHander();
    private AnimHander animHander=new AnimHander();
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        myServiceConnection=new MyServiceConnection();
        FloatService.this.bindService(new Intent(FloatService.this,LocationService.class),myServiceConnection,Context.BIND_IMPORTANT);
        purposeFloatButton();
        createMainWindow();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createMainWindow()
    {
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        mainWindow_params.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置效果为背景透明.
        mainWindow_params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        mainWindow_params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        mainWindow_params.gravity = Gravity.LEFT | Gravity.TOP;
        mainWindow_params.x = 0;
        mainWindow_params.y = 0;

        //设置悬浮窗口长宽数据.
        mainWindow_params.width = 300;
        mainWindow_params.height = 300;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = (ConstraintLayout) inflater.inflate(R.layout.float_window_main,null);
        imageView = (ImageView) toucherLayout.findViewById(R.id.floatWindow_imageview);
        //添加toucherlayout
        windowManager.addView(toucherLayout,mainWindow_params);

        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        final int mainWindow_width=windowManager.getDefaultDisplay().getWidth();
        if (resourceId > 0)
        {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            long[] hints = new long[2];
            @Override
            public void onClick(View v) {
                System.arraycopy(hints,1,hints,0,hints.length -1);
                hints[hints.length -1] = SystemClock.uptimeMillis();
                if (SystemClock.uptimeMillis() - hints[0] >= 850)
                {

                }else {
                    if (haveFloatWindowButton == false) {
                        createFloatButton();
                        new Thread() {
                            public void run() {
                                try {
                                    SystemClock.sleep(4000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                animHander.sendMessage(msg);

                                try {
                                    SystemClock.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                windowManager.removeView(float_window_button1);
                                windowManager.removeView(float_window_button2);
                                windowManager.removeView(float_window_button3);
                                windowManager.removeView(float_window_button4);
                                haveFloatWindowButton = false;
                            }
                        }.start();
                    }
                }
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int oldStartX=startX;
                int oldStartY=startY;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();

                        int dx = newX - startX;
                        int dy = newY - startY;

                        mainWindow_params.x += dx;
                        mainWindow_params.y += dy;
                        for(int i=0;i<floatButtonAmount;i++){
                            params.get(i).x+=dx;
                            params.get(i).y+=dy;
                        }
                        if (mainWindow_params.y < 0)
                            mainWindow_params.y = 0;
                        if(params.get(0).y<0)
                            params.get(0).y=0;
                        if(params.get(1).y<0)
                            params.get(1).y=0;
                        if(params.get(2).y<0)
                            params.get(2).y=0;
                        if(params.get(3).y<0)
                            params.get(3).y=0;

                        windowManager.updateViewLayout(toucherLayout, mainWindow_params);
                        try {
                            windowManager.updateViewLayout(float_window_button1, params.get(0));
                            windowManager.updateViewLayout(float_window_button2, params.get(1));
                            windowManager.updateViewLayout(float_window_button3, params.get(2));
                            windowManager.updateViewLayout(float_window_button4, params.get(3));
                        }catch (IllegalArgumentException e){

                        }
                        startX = newX;
                        startY = newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mainWindow_params.x == 0 || mainWindow_params.x < 0) {
                            //靠屏幕左边，应该把身体收进去，没有画相应的动画只好随便表示
                            imageView.setBackgroundResource(R.drawable.anim_sleep);
                            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
                            animationDrawable.start();
                            //windowManager.updateViewLayout(toucherLayout, params);
                        } else if (mainWindow_params.x + 300 >= mainWindow_width) {
                            //靠屏幕右边，应该把身体收进去，没有画相应的动画只好随便表示
                            imageView.setBackgroundResource(R.drawable.anim_sleep);
                            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
                            animationDrawable.start();
                            //windowManager.updateViewLayout(toucherLayout, params);
                        } else {
                            //如果没有靠屏幕边就什么都不做
                            imageView.setBackgroundResource(animNumber);
                            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
                            animationDrawable.start();
                            //windowManager.updateViewLayout(toucherLayout, params);
                        }
                        oldStartX=startX;
                        oldStartY=startY;
                        break;
                }
                return false;
            }
        });

    }

    class MyServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder=(LocationService.Binder)service;
            binder.getService().setCallback(new LocationService.CallBack(){
                public void onDataChange(String data) {
                    if (Location != data) {
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        b.putString("data", data);
                        msg.setData(b);
                        handler.sendMessage(msg);
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            unbindService(myServiceConnection);
        }
    }
    @Override
    public void onDestroy()
    {
        if (imageView != null)
        {
            windowManager.removeView(toucherLayout);
        }
        super.onDestroy();
    }

    private void purposeFloatButton() {
        int i;
        for(i=0;i<floatButtonAmount;i++) {
            WindowManager.LayoutParams params1=new WindowManager.LayoutParams();
            params1.type = WindowManager.LayoutParams.TYPE_PHONE;//设置type.系统提示型窗口，一般都在应用程序窗口之上.
            params1.format = PixelFormat.RGBA_8888; //设置效果为背景透明.
            params1.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
            //设置悬浮窗口长宽数据.
            params1.width = 100;
            params1.height = 100;
            //设置窗口初始停靠位置.
            params1.gravity = Gravity.LEFT | Gravity.TOP;
            params1.x = 0;
            params1.y = 0;
            params.add(params1);
        }
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        //获取浮动窗口视图所在布局.
        float_window_button1 = (ConstraintLayout) inflater.inflate(R.layout.float_window_button1,null);
        imageButton1=(ImageButton)float_window_button1.findViewById(R.id.float_window_button1_imageButton);
        float_window_button2 = (ConstraintLayout) inflater.inflate(R.layout.float_window_button2,null);
        imageButton2=(ImageButton)float_window_button2.findViewById(R.id.float_window_button2_imageButton);
        float_window_button3 = (ConstraintLayout) inflater.inflate(R.layout.float_window_button3,null);
        imageButton3=(ImageButton)float_window_button3.findViewById(R.id.float_window_button3_imageButton);
        float_window_button4 = (ConstraintLayout) inflater.inflate(R.layout.float_window_button4,null);
        imageButton4=(ImageButton)float_window_button4.findViewById(R.id.float_window_button4_imageButton);

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatService.this,"button1",Toast.LENGTH_SHORT).show();
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatService.this,"button2",Toast.LENGTH_SHORT).show();
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatService.this,"button3",Toast.LENGTH_SHORT).show();
            }
        });
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatService.this,"button4",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void createFloatButton(){
        int i;
        for(i=0;i<floatButtonAmount;i++) {
            params.get(i).x = mainWindow_params.x+120*i;
            params.get(i).y = mainWindow_params.y+(int) Math.sqrt(160000 - 10000*i*i)-80;
        }
        try {
            windowManager.addView(float_window_button1,params.get(0));
            windowManager.addView(float_window_button2,params.get(1));
            windowManager.addView(float_window_button3,params.get(2));
            windowManager.addView(float_window_button4,params.get(3));
        }catch (IllegalStateException e){

        }
        alphaAnim0_1.setDuration(2000); //设置动画持续时长
        imageButton1.startAnimation(alphaAnim0_1);
        imageButton2.startAnimation(alphaAnim0_1);
        imageButton3.startAnimation(alphaAnim0_1);
        imageButton4.startAnimation(alphaAnim0_1);
        haveFloatWindowButton=true;
    }
    private class MyHander extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Location = msg.getData().getString("data");
            switch (Location) {
                case "classroom":
                    animNumber=R.drawable.anim_study;
                    break;
                case "cartoon":
                    animNumber=R.drawable.anim_eat;
                    break;
                case "restroom":
                    animNumber=R.drawable.anim_sleep;
                    break;
                case "playground":
                    animNumber=R.drawable.anim_sport;
                    break;
                case "playroom":
                    animNumber=R.drawable.anim_sport;
                    break;
                default:
                    animNumber=R.drawable.anim_common;
                    break;
            }
            imageView.setBackgroundResource(animNumber);
            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
            animationDrawable.start();
        }
    }
    private class AnimHander extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(1000); //设置动画持续时长
            imageButton1.startAnimation(alphaAnimation);
            imageButton2.startAnimation(alphaAnimation);
            imageButton3.startAnimation(alphaAnimation);
            imageButton4.startAnimation(alphaAnimation);
        }
    }
}
