package petState;

/**
 * Created by 林尤辉 on 2017/11/19.
 */

public class HealthState extends PetState {
    public void addHealth(int number){
        this.getAction().addHealth(number);
    }

    public void subHealth(int number){
        this.getAction().subHealth(number);
    }

    public void addMood(int number){
        this.getAction().addMood(number);
    }

    public void subMood(int number){
        this.getAction().subMood(number);
    }
}
