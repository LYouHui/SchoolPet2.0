package petState;

/**
 * Created by 林尤辉 on 2017/11/19.
 */

public abstract class PetAction {
    void addHealth(int number){};
    void subHealth(int number){};
    void addMood(int number){};
    void subMood(int number){};

    int getAnim(){return 0;};
   //void startAction();
    void endAction(){};
}
