package javafxgame;

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.shape.Circle;

public abstract class Guy extends Entity implements Runnable {
    
    private int hp;
    private Circle circle;
    Thread thrd;
    Node currentNode;
    private Graph graph;
    boolean zajety;
    private static final int radius = 5;
    private volatile boolean running = true;
    boolean suspended;
    boolean stopped;

    public Guy(int hp, double x, double y, Pane pane, Node currentNode) {
        zajety = false;
//        super(name);
        suspended = false;
        stopped = false;
        this.hp = hp;
        this.currentNode = currentNode;
        thrd = new Thread(this);
        thrd.setDaemon(true);
        thrd.start();
        circle = new Circle(x, y, radius);
        circle.setFill(valueOf("blue"));
        pane.getChildren().add(circle);
    }

    @Override
    public void run() {
        System.out.println("nowy watek hurra!" + toString());
        //goToCity();
    }

    synchronized void myStop() {
        stopped = true;
        suspended = false;
        notify();
    }

    synchronized void mySuspend() {
        suspended = true;
//        notify();
    }

    synchronized void myResume() {
        suspended = false;
        
        notify();
        System.out.println("notifajnalem");
    }

    /**
     * @return true if you have safely arrived to the destination to be
     * implemented
     */
    void go(Stack<Node> path) {
        zajety = true;
        //jezeli jestem w srodku miasta
        currentNode = path.get(0);
        path.remove(0);
        Thread renderer;
        renderer = new Thread() {
            Node skrzyzowanie = null;
            boolean wchodze = false;

            @Override
            public void run() {
                for (Node it : path) {

//                    final Circle previous ;
//                    previous=circle;
                    boolean spieprzone = false;
//                do
                    Thread t;
                    System.out.println("zaczynam runandwait");
                    if (getCircle().getCenterY() == it.getCircle().getCenterY() && getCircle().getCenterX() < it.getCircle().getCenterX()) {//go right
                        System.out.println("go right bro!" + getCircle());
                        try {
                                synchronized (this) {
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                        System.out.println("koniec waitowania");
                                    }
                                    if (stopped) 
                                        break;
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
                        System.out.println("ile to " + ile);
                        for (int i = 0; i < ile; i++) {
                            try {
                                synchronized (this) {
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                        System.out.println("koniec waitowania");
                                    }
                                    if (stopped) 
                                        break;
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
                            
                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterX(getCircle().getCenterX() + 1);
                                    //to jest przy wchodzeniu, potrzebuje jeszcze do opuszczania
                                }
                            };
                            JavaFxGame.runAndWait(t);

//                                    } 
                        }
                        try {
                                synchronized (this) {
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                        System.out.println("koniec waitowania");
                                    }
                                    if (stopped) 
                                        break;
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
                        //System.out.println("X : "+circle.getCenterX()+ " Y: "+circle.getCenterY());
                    } else if (getCircle().getCenterY() == it.getCircle().getCenterY() && getCircle().getCenterX() > it.getCircle().getCenterX()) {//go left
                        try {
                                synchronized (this) {
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                        System.out.println("koniec waitowania");
                                    }
                                    if (stopped) 
                                        break;
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
                        System.out.println("left ile: " + ile);
                        for (int i = 0; i < ile; i++) {
                            try {
                                synchronized (this) {
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                        System.out.println("koniec waitowania");
                                    }
                                    if (stopped) 
                                        break;
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
                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterX(getCircle().getCenterX() - 1);

                                }
                            };
                            JavaFxGame.runAndWait(t);

//                                    
                        }
                        try {
                                synchronized (this) {
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                    }
                                    if (stopped) 
                                        break;
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        t = new Thread() {
                            public void run() {
                                getCircle().setCenterY(getCircle().getCenterY() + Graph.getRoadWidth() / 2);
                                System.out.println("w lewo circle na koncu: " + getCircle());

                            }
                        };
                        JavaFxGame.runAndWait(t);
                    } else if (getCircle().getCenterY() < it.getCircle().getCenterY() && getCircle().getCenterX() == it.getCircle().getCenterX()) {//go down
                        try {
                                synchronized (this) {
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                    }
                                    if (stopped) 
                                        break;
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
                        System.out.println("w dol ile: " + ile);

                        for (int i = 0; i < ile; i++) {
                            try {
                                synchronized (this) {
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                        System.out.println("koniec waitowania");
                                    }
                                    if (stopped) 
                                        break;
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
                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterY(getCircle().getCenterY() + 1);

                                }

                            };
                            JavaFxGame.runAndWait(t);

                        }
                        try {
                                synchronized (this) {
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                    }
                                    if (stopped) 
                                        break;
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
                        //System.out.println("X : "+circle.getCenterX()+ " Y: "+circle.getCenterY());
//

                    } else if (getCircle().getCenterY() > it.getCircle().getCenterY() && getCircle().getCenterX() == it.getCircle().getCenterX()) {//go up
                        try {
                                synchronized (this) {
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                    }
                                    if (stopped) 
                                        break;
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
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                        System.out.println("koniec waitowania");
                                    }
                                    if (stopped) 
                                        break;
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
                            t = new Thread() {
                                public void run() {
                                    getCircle().setCenterY(getCircle().getCenterY() - 1);

                                }
                            };
                            JavaFxGame.runAndWait(t);
//                                    
                        }
                        try {
                                synchronized (this) {
                                    while (suspended) {
//                                       wait();
                                        Thread.sleep(100);
                                    }
                                    if (stopped) 
                                        break;
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
                    } else {
                        System.out.println("cos spieprzylem!");
                    }
//                            else spieprzone=true;

//                       while(spieprzone);
//                        if(it!=path.get(0))
//                    while( circle.getCenterX()!=it.getCircle().getCenterX() || circle.getCenterY()!=it.getCircle().getCenterY()) {System.out.println("zablokowany!");};
//                                        prev=it.getCircle();
                    currentNode = path.lastElement();
                    System.out.println("skonczylem!");

                }
                zajety = false;
            }

            ;

//                while (true) {
//                    try {
//                        Thread.sleep(40);
//                    } catch (InterruptedException ex) {
//                    }
//
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                           
//                        }
//                    });
//                }
//            }

            private void doStuffWhenWantsToEnterCrossroad(Node it) {
                //System.out.println("sleepuje");
                if (!it.isCity() && !wchodze && getCircle().getBoundsInParent().intersects(it.getCircle().getBoundsInParent())) {
                    System.out.println("Wszedlem na skrzyzowanie bro!!!");
                    wchodze = true;//potrzebuje jeszcze wychodzenie
                    skrzyzowanie = it;
                    try {
                        ((Crossroads) it).getSem().acquire();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (wchodze & skrzyzowanie != null && !skrzyzowanie.isCity() && !circle.getBoundsInParent().intersects(skrzyzowanie.getCircle().getBoundsInParent())) {
                    System.out.println("opuszczam bro!");
                    wchodze = false;
                    ((Crossroads) skrzyzowanie).getSem().release();

                    skrzyzowanie = null;

                }
            }
        };
//        
        renderer.setDaemon(true);
        renderer.start();

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

}
