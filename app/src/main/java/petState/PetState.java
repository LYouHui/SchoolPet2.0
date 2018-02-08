package petState;

/**
 * Created by 林尤辉 on 2017/11/19.
 */

public class PetState {
    private int health;
    private int mood;
    private PetAction action;

    private boolean isBreakfast;
    private boolean isLunch;
    private boolean isDinner;


    public PetState(){
        //好像没办法在父类的构造函数中直接转为子类对象，只能在用的时候强制转换了。

        //此处应该是从服务器获取的
        //此处应该是从服务器获取的
        //此处应该是从服务器获取的
        health=100;
        mood=100;
        isBreakfast=false;
        isDinner=false;
        isLunch=false;
    }
    public void setHealth(int health) {
        //所有的set函数应该是往服务器set
        this.health = health;
    }

    public void setMood(int mood) {
        //所有的set函数应该是往服务器set
        this.mood = mood;
    }

    public void setAction(PetAction action) {
        this.action = action;
    }

    public void setBreakfast(boolean breakfast) {
        //所有的set函数应该是往服务器set
        isBreakfast = breakfast;
    }

    public void setLunch(boolean lunch) {
        isLunch = lunch;
    }

    public void setDinner(boolean dinner) {
        isDinner = dinner;
    }

    public int getHealth() {
        return health;
    }

    public int getMood() {
        return mood;
    }

    public PetAction getAction() {
        return action;
    }

    public boolean isBreakfast() {
        return isBreakfast;
    }

    public boolean isLunch() {
        return isLunch;
    }

    public boolean isDinner() {
        return isDinner;
    }


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
