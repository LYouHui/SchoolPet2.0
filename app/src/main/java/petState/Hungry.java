package petState;

import android.graphics.drawable.AnimationDrawable;

import com.example.schoolpet20.MainActivity;
import com.example.schoolpet20.R;

import java.util.Calendar;

/**
 * Created by 林尤辉 on 2017/11/19.
 */

public class Hungry extends PetAction {
    PetState petState;
    MainActivity activity=new MainActivity();

    public Hungry(PetState petState) {
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
        return R.drawable.anim_illness;
    }

    @Override
    public void endAction() {
        //要么由Eat动作直接解除，或者过了饭点自动解除

        //往服务器获取开始时间，计算时长，并结束
        Calendar cal=Calendar.getInstance();
        int hour=cal.get(Calendar.HOUR_OF_DAY);
        int minute=cal.get(Calendar.MINUTE);
        //计算时长
        int time=100;
        //早午晚三餐bool变量的解除由Eat实现。

        //根据时长增加或减少属性
        //早餐从7点开始饿,午饭从10：30开始饿，晚饭从5点开始饿
        if(time<60)//总计学习时间小于10分钟，说明用户好不专心
            petState.subMood(2);
        else if(time>60&&time<120){
            petState.subMood(3);
            petState.subHealth(3);
        }
        else if(time>60&&time<120) {
            petState.subHealth(5);
            petState.subMood(5);
        }
        else{
            petState.subHealth(8);
            petState.subMood(8);
        }

        petState.setAction(new commom());
    }
}
