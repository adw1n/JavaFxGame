
package javafxgame;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;

public class BadGuy extends Fighter {

    Node closest = null;
    static final private int inf = 1000000;

    public BadGuy(int hp, double x, double y, Pane pane, Graph graph) {
        super(hp, x, y, pane, null);
        setGraph(graph);
        getCircle().setFill(valueOf("red"));
    }

    @Override
    public boolean isStoppable() {
        return false;
    }
    
    @Override
    public void run() {
        try {
            //find closest city to go to
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(BadGuy.class.getName()).log(Level.SEVERE, null, ex);
        }
        double distance, minDistance = 1000000;
        for (Node it : getGraph().getNodes()) {
            if (it.isCity() && !it.isIsDefeated()) {
                distance = (getCircle().getCenterX() - it.getCircle().getCenterX()) * (getCircle().getCenterX() - it.getCircle().getCenterX())
                        + (getCircle().getCenterY() - it.getCircle().getCenterY()) * (getCircle().getCenterY() - it.getCircle().getCenterY());
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = it;
                }
            }
        }
        if (minDistance != inf) {
            //go to the closest
//        Thread t=new Thread(){
//            public void run(){
            int addX, addY;
            if (getCircle().getCenterX() < closest.getCircle().getCenterX()) {
                addX = 1;
            } else {
                addX = -1;
            }
            if (getCircle().getCenterY() < closest.getCircle().getCenterY()) {
                addY = 1;
            } else {
                addY = -1;
            }
            while (getCircle().getCenterX() != closest.getCircle().getCenterX()
                    || getCircle().getCenterY() != closest.getCircle().getCenterY()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BadGuy.class.getName()).log(Level.SEVERE, null, ex);
                }
                Thread t=new Thread(){
                    public void run(){
                        if (getCircle().getCenterX() != closest.getCircle().getCenterX()) {
                    getCircle().setCenterX(getCircle().getCenterX() + addX);
                }
                    }
                };
                JavaFxGame.runAndWait(t);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BadGuy.class.getName()).log(Level.SEVERE, null, ex);
                }
                t=new Thread(){
                    public void run(){
                        if (getCircle().getCenterY() != closest.getCircle().getCenterY()) {
                    getCircle().setCenterY(getCircle().getCenterY() + addY);
                }
                    }
                };
                JavaFxGame.runAndWait(t);
                
            }
            currentNode=closest;
//            try{
//                go(getGraph().findPathBetweenCities(currentNode, getGraph().getRandomCity(currentNode)));
//                    }
//                    catch(TryLater e){
////                        succeded=false;
//                        System.out.println("polecial wyjatek bro!");
//                    }
        }
    }
    
}
