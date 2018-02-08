package lockScreen;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.example.schoolpet20.R;

public class LockScreenActivity extends AppCompatActivity {
    public ImageView imageView_studypet;
    public Chronometer chronometer_studytime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在keyguard在前显示应用程序窗口
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        //彻底隐藏非安全验证的keyguard
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);
        setContentView(R.layout.activity_lock_screen);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        imageView_studypet=(ImageView)findViewById(R.id.activity_lock_screen_imageView_studypet);
        chronometer_studytime=(Chronometer)findViewById(R.id.activity_lock_screen_chronometer_studytime);

        imageView_studypet.setBackgroundResource(R.drawable.anim_study);
        AnimationDrawable animationDrawable=(AnimationDrawable)imageView_studypet.getBackground();
        animationDrawable.start();
    }
}
