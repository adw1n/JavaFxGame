package javafxgame;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.shape.Circle;
/**
 * Guy implements many useful functions for traveling between Cities, supports multithreaded programming.
 * @author adwin_
 */
public abstract class Guy extends Entity implements Runnable {

    /**
     * @return the radius
     */
    public static int getRadius() {
        return radius;
    }
private final int numberOfUpdates=20;
    private final float factor=0.0001f;
    private float  hp;
    private Circle circle;
    private Thread thrd;
    private Node currentNode;
    private static final int luckyStopValue=1337;
    private Graph graph;
     private boolean busyCuzWorking;
    private static final int radius = 5;
    private volatile boolean running = true;
    private boolean suspended;
    private boolean stopped;
    private Pane pane;
    private boolean changeDirection;
    private Node destination;
    private boolean foundFlag = false;
               private Circle c;
               private Node crossroad = null;
               private boolean enteringFlag = false;
         private     Random randomGenerator ; 
         /**
          * Creates a guy.
          * @param hp num of health points
          * @param x x coordinate of the center of the guy
          * @param y y coordinate of the center of the guy
          * @param pane
          * @param currentNode
          * @param name 
          */
    public Guy(int hp, double x, double y, Pane pane, Node currentNode, String name) {
        super(name);
        changeDirection=false;
        busyCuzWorking = false;
        destination=null;
        suspended = false;
        stopped = false;
        this.hp = hp;
        this.currentNode = currentNode;
        thrd = new Thread(this);
        thrd.setDaemon(true);
        randomGenerator= new Random(); 
        circle = new Circle(x, y, getRadius());
        circle.setFill(valueOf("blue"));
        pane.getChildren().add(circle);
        this.pane=pane;
        thisEntity=this;
        getCircle().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getGraph().displayEntity(thisEntity);
            }
        });
                
    }
    /**
     * Start the thread.
     */
    public void startThread(){
        getThrd().start();
    }
    /**
     * Updates the PowerSources in a city.
     * @param city
     * @param howMuch 
     */
    public void updatePowerSources(City city,float howMuch){
        if(!city.isIsDefeated()){
        for(PowerSource it: city.getPowerSources()){
            it.increaseEnergy(howMuch);
        }
        JavaFxGame.runAndWait(new Runnable() {

            @Override
            public void run() {
                graph.getControlPanel().displayEntity();
            }
        });
        }
    }
    /**
     * Works on the PowerSources and makes them more powerful.
     * @throws InterruptedException 
     */
    public void sleepAndUpdatePowerSources() throws InterruptedException {
      
        int timeToSpendInTheCity=randomGenerator.nextInt(20000)+1000;
        if(this instanceof Superhero) //sleep much shorter
            timeToSpendInTheCity=randomGenerator.nextInt(2000)+1000;
        if(getCurrentNode()!=null){
        if(!currentNode.isIsDefeated()){
            for(int numOfTimeToUpdatePowerSources=0;numOfTimeToUpdatePowerSources<numberOfUpdates;numOfTimeToUpdatePowerSources++){
                    getThrd().sleep(timeToSpendInTheCity/numberOfUpdates);
                if( getCurrentNode() instanceof City)
                    updatePowerSources((City)getCurrentNode(), (timeToSpendInTheCity/numberOfUpdates)*factor);
            }
        }
        }
        
    }
   /**
    * Sends a request to update a city info after a change.
    */
    protected void updateCityInfo(){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                getGraph().getControlPanel().displayEntity();
            }
        });
    }
    /**
     * Deletes itself from view.
     */
    public void deleteFromPane(){
        JavaFxGame.runAndWait( new Thread(){
            @Override
            public void run(){

                getCircle().setCenterX(-100);
                                                getPane().getChildren().remove(getCircle());

            }
        });
    }
    /**
     * Alternative way of checking this instanceof Citizen, but faster one.
     * @return returns true when object is of class Citizen
     */
    public abstract boolean isCitizen();
    /**
     * Stops the thread.
     */
    synchronized void myStop() {
        setStopped(true);
        setSuspended(false);
        notify();
    }
    /**
     * Suspends the thread. It is possible to resume the thread later.
     */
    synchronized void mySuspend() {
        setSuspended(true);
//        notify();
    }
    /** 
     * Resumes work of the thread.
     */
    synchronized void myResume() {
        setSuspended(false);

        notify();
    }
    /**
     * Visits all the nodes given in a path. It is possible to change the path tho.
     * Uses checking for ConcurrentModificationException, another way to avoid it would be to use a semaphore on the list of guys or copy ArrayList to array and iterate over the array.
     * This option should be faster than both of them, although it's not elegant.
     * @param nodes nodes to visit
     */
    void go(Stack<Node> path) {
        setChangeDirection(false);
        destination=null;
        setBusyCuzWorking(true);
        if (!path.empty()) {
            setCurrentNode(path.get(0));
            path.remove(0);
            Thread renderer;
            renderer = new Thread() {
                @Override
                public void run() {
                    for (Node it : path) {
                        if(isChangeDirection()) { break;}//trzeba zwolnic crossroad albo i nie, samo sie moze zwolni
                        boolean spieprzone = false;
                        Thread t;
                        if (getCircle().getCenterY() == it.getCircle().getCenterY() && getCircle().getCenterX() < it.getCircle().getCenterX()) {//go right
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(100);
                                    }
                                    if (isStopped()) {
                                                                                        doStuffWhenWantsToEnterCrossroad(it);

                                        break;
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterY(getCircle().getCenterY() + Graph.getRoadWidth() / 2);

                                }
                            };
                            JavaFxGame.runAndWait(t);

                            int ile = Math.abs((int) (getCircle().getCenterX() - it.getCircle().getCenterX()));
                            for (int i = 0; i < ile; i++) {
                                try {
                                    synchronized (this) {
                                        while (isSuspended()) {
                                            Thread.sleep(100);
                                        }
                                        if (isStopped()) {
                                                                                            doStuffWhenWantsToEnterCrossroad(it);

                                            break;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                doStuffWhenWantsToEnterCrossroad(it);
                                checkIntersectionsWhileGoingRight();
                                t = new Thread() {
                                    public void run() {
                                        getCircle().setCenterX(getCircle().getCenterX() + 1);
                                        //to jest przy wchodzeniu, potrzebuje jeszcze do opuszczania
                                    }
                                };
                                JavaFxGame.runAndWait(t);

                            }
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(100);
                                    }
                                    if (isStopped()) {
                                                                                        doStuffWhenWantsToEnterCrossroad(it);

                                        break;
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterY(getCircle().getCenterY() - Graph.getRoadWidth() / 2);

                                }
                            };
                            JavaFxGame.runAndWait(t);
                        } else if (getCircle().getCenterY() == it.getCircle().getCenterY() && getCircle().getCenterX() > it.getCircle().getCenterX()) {//go left
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(100);
                                    }
                                    if (isStopped()) {
                                                                                        doStuffWhenWantsToEnterCrossroad(it);

                                        break;
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterY(getCircle().getCenterY() - Graph.getRoadWidth() / 2);

                                }
                            };
                            JavaFxGame.runAndWait(t);
                            int ile = Math.abs((int) (getCircle().getCenterX() - it.getCircle().getCenterX()));
                            for (int i = 0; i < ile; i++) {
                                try {
                                    synchronized (this) {
                                        while (isSuspended()) {
                                            Thread.sleep(100);
                                        }
                                        if (isStopped()) {
                                                                                            doStuffWhenWantsToEnterCrossroad(it);

                                            break;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                checkIntersectionsWhileGoingLeft();
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                doStuffWhenWantsToEnterCrossroad(it);
                                t = new Thread() {
                                    public void run() {
                                        getCircle().setCenterX(getCircle().getCenterX() - 1);

                                    }
                                };
                                JavaFxGame.runAndWait(t);

                            }
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(100);
                                    }
                                    if (isStopped()) {
                                                                                        doStuffWhenWantsToEnterCrossroad(it);

                                        break;
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterY(getCircle().getCenterY() + Graph.getRoadWidth() / 2);

                                }
                            };
                            JavaFxGame.runAndWait(t);
                        } else if (getCircle().getCenterY() < it.getCircle().getCenterY() && getCircle().getCenterX() == it.getCircle().getCenterX()) {//go down
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(100);
                                    }
                                    if (isStopped()) {
                                                                                        doStuffWhenWantsToEnterCrossroad(it);

                                        break;
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterX(getCircle().getCenterX() - Graph.getRoadWidth() / 2);

                                }
                            };
                            JavaFxGame.runAndWait(t);
                            int ile = (int) Math.abs(getCircle().getCenterY() - it.getCircle().getCenterY());

                            for (int i = 0; i < ile; i++) {
                                try {
                                    synchronized (this) {
                                        while (isSuspended()) {
                                            Thread.sleep(100);
                                        }
                                        if (isStopped()) {
                                                                                            doStuffWhenWantsToEnterCrossroad(it);

                                            break;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                checkIntersectionsWhileGoingDown();
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                doStuffWhenWantsToEnterCrossroad(it);
                                t = new Thread() {
                                    public void run() {
                                        getCircle().setCenterY(getCircle().getCenterY() + 1);

                                    }

                                };
                                JavaFxGame.runAndWait(t);

                            }
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(100);
                                    }
                                    if (isStopped()) {
                                                                                        doStuffWhenWantsToEnterCrossroad(it);

                                        break;
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterX(getCircle().getCenterX() + Graph.getRoadWidth() / 2);

                                }
                            };
                            JavaFxGame.runAndWait(t);
//

                        } else if (getCircle().getCenterY() > it.getCircle().getCenterY() && getCircle().getCenterX() == it.getCircle().getCenterX()) {//go up
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(100);
                                    }
                                    if (isStopped()) {
                                                                                        doStuffWhenWantsToEnterCrossroad(it);

                                        break;
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterX(getCircle().getCenterX() + Graph.getRoadWidth() / 2);

                                }
                            };
                            JavaFxGame.runAndWait(t);
                            int ile = (int) Math.abs(getCircle().getCenterY() - it.getCircle().getCenterY());
                            for (int i = 0; i < ile; i++) {
                                try {
                                    synchronized (this) {
                                        while (isSuspended()) {
                                            Thread.sleep(100);
                                        }
                                        if (isStopped()) {
                                                doStuffWhenWantsToEnterCrossroad(it);
                                            break;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                checkIntersectionsWhileGoingUp();
                                doStuffWhenWantsToEnterCrossroad(it);
                                t = new Thread() {
                                    public void run() {
                                        getCircle().setCenterY(getCircle().getCenterY() - 1);

                                    }
                                };
                                JavaFxGame.runAndWait(t);
                            }
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(100);
                                    }
                                    if (isStopped()) {
                                                                                        doStuffWhenWantsToEnterCrossroad(it);

                                        break;
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterX(getCircle().getCenterX() - Graph.getRoadWidth() / 2);

                                }
                            };
                            JavaFxGame.runAndWait(t);
                        } 
                        setCurrentNode(it);

                    }
                    if(!isChangeDirection() )
                    setBusyCuzWorking(false);
                    else { 
                        go(graph.findPathBetweenCities(getCurrentNode(), getDestination()));
                    }
                }

                ;

                private void checkIntersectionsWhileGoingLeft() {
                    c = new Circle(circle.getCenterX() - 1, circle.getCenterY(), getRadius());
                    randomSuspend();
                    do {
                        foundFlag = false;
                        
                        try{
                        for(Iterator<Guy> iter=graph.getGuys().iterator();iter.hasNext();)
//                        for (Guy iterator : graph.getGuys()) 
                                                {
                            Guy iterator=iter.next();
                            if (!isStopped() &&!iterator.isStopped() && iterator.getCircle() != circle && iterator.getCircle().getCenterX() <= c.getCenterX() && iterator.getCircle().getCenterY() == c.getCenterY() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                foundFlag = true;
                            }
                        }
                        }
                        catch(ConcurrentModificationException e){
                            foundFlag=true;
                        }
                    } while (foundFlag );
                }

                private void checkIntersectionsWhileGoingDown() {
                    c = new Circle(circle.getCenterX(), circle.getCenterY() + 1, getRadius());
                    randomSuspend();
                    do {
                        foundFlag = false;
                        try{
                        for(Iterator<Guy> iter=graph.getGuys().iterator();iter.hasNext();)
//                        for (Guy iterator : graph.getGuys()) 
                                                {
                            Guy iterator=iter.next();
                            if (!isStopped() &&!iterator.isStopped() &&  iterator.getCircle() != circle && iterator.getCircle().getCenterX() == c.getCenterX() && iterator.getCircle().getCenterY() >= c.getCenterY() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                foundFlag = true;
                            }
                        }
                        }
                        catch(ConcurrentModificationException e){
                            foundFlag=true;
                        }
                    } while (foundFlag);
                }

                private void checkIntersectionsWhileGoingUp() {
                    c = new Circle(circle.getCenterX(), circle.getCenterY() - 1, getRadius());
                    randomSuspend();
                    do {
                        foundFlag = false;
                        try{
                        for(Iterator<Guy> iter=graph.getGuys().iterator();iter.hasNext();)
//                        for (Guy iterator : graph.getGuys()) 
                                                {
                            Guy iterator=iter.next();
                            if (!isStopped() && !iterator.isStopped() && iterator.getCircle() != circle && iterator.getCircle().getCenterX() == c.getCenterX() && iterator.getCircle().getCenterY() <= c.getCenterY() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                foundFlag = true;
                            }
                        }
                        }
                        catch(ConcurrentModificationException e){
                            foundFlag=true;
                        }
                    } while (foundFlag);
                }

                private void checkIntersectionsWhileGoingRight() {
                    c = new Circle(circle.getCenterX() + 1, circle.getCenterY(), getRadius());
                    randomSuspend();
                    do {
                        foundFlag = false;
                        try{
                        for(Iterator<Guy> iter=graph.getGuys().iterator();iter.hasNext();)
//                        for (Guy iterator : graph.getGuys()) 
                                                {
                            Guy iterator=iter.next();
                            if (!isStopped() && !iterator.isStopped() && iterator.getCircle() != circle && iterator.getCircle().getCenterX() >= c.getCenterX() && iterator.getCircle().getCenterY() == c.getCenterY() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                foundFlag = true;
                            }
                        }
                        }
                        catch(ConcurrentModificationException e){
                            foundFlag=true;
                        }
                    } while (foundFlag);
                }

            private void doStuffWhenWantsToEnterCrossroad(Node it) {
                    if (!it.isCity() && !enteringFlag && getCircle().getBoundsInParent().intersects(it.getCircle().getBoundsInParent())) {
                        enteringFlag = true;
                        crossroad = it;
                        try {
                            ((Crossroads) it).getSem().acquire();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (enteringFlag & crossroad != null && !crossroad.isCity() && (!circle.getBoundsInParent().intersects(crossroad.getCircle().getBoundsInParent()) || isStopped() )) {
                        enteringFlag = false;
                        ((Crossroads) crossroad).getSem().release();

                        crossroad = null;

                    }
                }
            };
            renderer.setDaemon(true);
            renderer.start();
        }
    }
    /** 
     * Suspends the guy, once in a while.
     */
    public void randomSuspend() {
        if( this instanceof Citizen && randomGenerator.nextInt(10000)==luckyStopValue) mySuspend();
    }

    /**
     * @return the hp
     */
    public float getHp() {
        return hp;
    }

    /**
     * @param hp the hp to set
     */
    public void setHp(float hp) {
        this.hp = hp;
    }
    public synchronized void takeDmg(float hp){
        this.hp-=Math.abs(hp);
    }
    /**
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @param graph the graph to set
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * @return the circle
     */
    public Circle getCircle() {
        return circle;
    }

    /**
     * @param circle the circle to set
     */
    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    /**
     * @return the running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @param running the running to set
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * @return the pane
     */
    public Pane getPane() {
        return pane;
    }

    /**
     * @param pane the pane to set
     */
    public void setPane(Pane pane) {
        this.pane = pane;
    }

    /**
     * @return the suspended
     */
    public synchronized boolean isSuspended() {
        return suspended;
    }

    /**
     * @param suspended the suspended to set
     */
    public synchronized void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    /**
     * @return the stopped
     */
    public synchronized boolean isStopped() {
        return stopped;
    }

    /**
     * @param stopped the stopped to set
     */
    public synchronized void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    /**
     * @return the changeDirection
     */
    public synchronized boolean isChangeDirection() {
        return changeDirection;
    }

    /**
     * @param changeDirection the changeDirection to set
     */
    public synchronized void setChangeDirection(boolean changeDirection) {
        this.changeDirection = changeDirection;
    }

    /**
     * @return the destination
     */
    public Node getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public synchronized void setDestination(Node destination) {
        setChangeDirection(true);
        this.destination = destination;
    }

    /**
     * @return the currentNode
     */
    public Node getCurrentNode() {
        return currentNode;
    }

    /**
     * @param currentNode the currentNode to set
     */
    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    /**
     * @return the thrd
     */
    public Thread getThrd() {
        return thrd;
    }

    /**
     * @return the busyCuzWorking
     */
    public boolean isBusyCuzWorking() {
        return busyCuzWorking;
    }

    /**
     * @param busyCuzWorking the busyCuzWorking to set
     */
    public void setBusyCuzWorking(boolean busyCuzWorking) {
        this.busyCuzWorking = busyCuzWorking;
    }

}
