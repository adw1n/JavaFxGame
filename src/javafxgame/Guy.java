package javafxgame;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.shape.Circle;

public abstract class Guy extends Entity implements Runnable{ 
    private int hp;
    private Circle circle;
    Thread thrd;
    private static final int radius=5;
    public Guy(int hp,double x,double y,Pane pane) {
//        super(name);
        this.hp = hp;
//        this.currentCity = currentCity;
        thrd=new Thread(this);
        thrd.setDaemon(true);
        thrd.start();
        circle=new Circle(x,y,radius);
        circle.setFill(valueOf("yellow"));
        pane.getChildren().add(circle);
    }
    @Override
    public void run(){
        System.out.println("nowy watek hurra!" + toString());
        goToCity();
    }
    /** @return true if you have safely arrived to the destination
     * to be implemented
     */
    void goToCity(){


    }

    /**
     * @return the hp
     */
    public int getHp() {
        return hp;
    }

    /**
     * @param hp the hp to set
     */
    public void setHp(int hp) {
        this.hp = hp;
    }
    
}
