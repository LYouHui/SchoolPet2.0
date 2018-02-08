package petState;

/**
 * Created by 林尤辉 on 2017/11/19.
 */

public class IllnessState extends PetState {
    public void addHealth(int number){
        this.getAction().addHealth(number+5);
    }

    public void subHealth(int number){
        this.getAction().subHealth(number-5);
    }

    public void addMood(int number){
        this.getAction().addMood(number+5);
    }

    public void subMood(int number){
        this.getAction().subMood(number-5);
    }
}
