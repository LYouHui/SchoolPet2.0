package petState;

import android.graphics.drawable.AnimationDrawable;

import com.example.schoolpet20.MainActivity;
import com.example.schoolpet20.R;

import java.util.Calendar;

/**
 * Created by 林尤辉 on 2017/11/19.
 */

public class Sleep extends PetAction {
    PetState petState;
    MainActivity activity=new MainActivity();

    public Sleep(PetState petState) {
        this.petState=petState;

        Calendar cal=Calendar.getInstance();
        int hour=cal.get(Calendar.HOUR_OF_DAY);
        int minute=cal.get(Calendar.MINUTE);
        //往服务器上传开始时间

        //播放动画
        activity.imageView_pet.setBackgroundResource(getAnim());
        AnimationDrawable anim = (AnimationDrawable)activity.imageView_pet.getBackground();
        anim.start();
    }

    @Override
    public void addHealth(int number) {
        petState.setHealth(petState.getHealth()+number);
    }

    @Override
    public void subHealth(int number) {
        petState.setHealth(petState.getMood()+number);
    }

    @Override
    public void addMood(int number) {
        petState.setMood(petState.getMood()+number);
    }

    @Override
    public void subMood(int number) {
        petState.setMood(petState.getMood()+number);
    }

    @Override
    public int getAnim() {
        return R.drawable.anim_sleep;
    }

    @Override
    public void endAction() {
        //往服务器获取开始时间，计算时长，并结束
        Calendar cal=Calendar.getInstance();
        int hour=cal.get(Calendar.HOUR_OF_DAY);
        int minute=cal.get(Calendar.MINUTE);
        //计算时长
        int time=100;

        //由出宿舍的时间决定
        //根据时长增加或减少属性
        if(time<10)//总计学习时间小于10分钟，说明用户好不专心
            petState.subMood(1);
        else if(time>10&&time<60)
            petState.addMood(3);
        else if(time>60&&time<120)
            petState.addMood(5);
        else if(time>120)
            petState.addMood(10);
        else
            petState.addMood(0);

        petState.setAction(new commom());
    }
}
