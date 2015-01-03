package javafxgame;

import java.util.concurrent.Semaphore;
import javafx.scene.layout.Pane;

public class Crossroads extends Node {

    private static final int radius = 16;
    private boolean isBeingUsed=false;
    private Semaphore sem;
    Crossroads(int x, int y, Pane pane, Graph graph) {
        super(x, y, pane, radius, graph);
        sem=new Semaphore(1,true);
    }

    public boolean isCity() {
        return false;
    }

    /**
     * @return the isBeingUsed
     */
    public synchronized boolean isIsBeingUsed() {
        return isBeingUsed;
    }

    /**
     * @param isBeingUsed the isBeingUsed to set
     */
    public synchronized void setIsBeingUsed(boolean isBeingUsed) {
        this.isBeingUsed = isBeingUsed;
    }

    /**
     * @return the sem
     */
    public Semaphore getSem() {
        return sem;
    }

    /**
     * @param sem the sem to set
     */
    public void setSem(Semaphore sem) {
        this.sem = sem;
    }
}
