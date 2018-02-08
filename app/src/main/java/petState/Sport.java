package petState;

import android.graphics.drawable.AnimationDrawable;

import com.example.schoolpet20.MainActivity;
import com.example.schoolpet20.R;

import java.util.Calendar;

/**
 * Created by 林尤辉 on 2017/11/19.
 */

public class Sport extends PetAction {
    PetState petState;
    MainActivity activity=new MainActivity();

    public Sport(PetState petState) {
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
        return R.drawable.anim_sport;
    }

    @Override
    public void endAction() {
        //往服务器获取开始时间，计算时长，并结束
        Calendar cal=Calendar.getInstance();
        int hour=cal.get(Calendar.HOUR_OF_DAY);
        int minute=cal.get(Calendar.MINUTE);
        //计算时长
        int time=100;

        //根据时长增加或减少属性
        if(time<30){
            petState.addHealth(3);
            petState.addMood(3);
        }
        else if(time>30&&time<60){
            petState.addMood(5);
            petState.addHealth(10);
        }
        else if(time>60){
            petState.addHealth(12);
            petState.addMood(10);
        }
        else
            petState.addMood(0);

        petState.setAction(new commom());
    }
}
