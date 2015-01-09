package javafxgame;

import java.util.concurrent.Semaphore;
import javafx.scene.layout.Pane;
/**
 * Connects roads between Nodes/Cities. Only one citizen can enter a Crossroad at a time.
 * @author adwin_
 */
public class Crossroads extends Node {

    private static final int radius = 16;
    private boolean isBeingUsed=false;
    private Semaphore sem;
    /**
     * Creates a Crossroad
     * @param x
     * @param y
     * @param pane
     * @param graph 
     */
    Crossroads(int x, int y, Pane pane, Graph graph) {
        super(x, y, pane, radius, graph,graph.getNameGetter().getCrossroadsName());
        sem=new Semaphore(1,true);
    }
    /**
     * Informs that this object is not a city.
     * @return 
     */
    public boolean isCity() {
        return false;
    }

    /**
     * 
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
     * Returns the semaphore, that is responible that only one citizen can enter the crossroads at the time.
     * @return the sem
     */
    public Semaphore getSem() {
        return sem;
    }

    @Override
    void myStop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    /**
     * @param sem the sem to set
     */
    public void setSem(Semaphore sem) {
        this.sem = sem;
    }
}
