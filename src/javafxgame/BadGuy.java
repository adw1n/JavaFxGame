package javafxgame;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.shape.Circle;

/**
 * Enemy in the game, kills citizens, drains cities, fights with superheroes.
 *
 * @author adwin_
 */
public class BadGuy extends Fighter {

    private static final int maxHP = 1000;
    private City destination;

    /**
     * @return the maxHP
     */
    public static int getMaxHP() {
        return maxHP;
    }
    private Node closest = null;
    static final private int inf = 1000000;

    /**
     * Constructs an object of class BadGuy.
     *
     * @param hp health points of the BadGuy
     * @param x x center coordinate of the BadGuy
     * @param y x center coordinate of the BadGuy
     * @param pane the pane that u draw on
     * @param graph the graph that all the cities belong to
     */
    public BadGuy(int hp, double x, double y, Pane pane, Graph graph) {
        super(hp, x, y, pane, null, graph.getNameGetter().getFemaleName());
        setGraph(graph);
        getCircle().setFill(valueOf("red"));

    }

    private final int timeInterval = 100;

    @Override
    public boolean isCitizen() {
        return false;
    }

    /**
     * Makes bad guy attack cities, kill citizens, fight with superheroes.
     */
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
            if (closest instanceof City) {
                ((City) closest).setBadGuyIsGoingToThisCity(true);
            }
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
                    Thread.sleep(timeInterval / 2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BadGuy.class.getName()).log(Level.SEVERE, null, ex);
                }
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        if (getCircle().getCenterX() != closest.getCircle().getCenterX()) {
                            getCircle().setCenterX(getCircle().getCenterX() + addX);
                        }
                    }
                };
                JavaFxGame.runAndWait(t);
                try {
                    Thread.sleep(timeInterval / 2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BadGuy.class.getName()).log(Level.SEVERE, null, ex);
                }
                t = new Thread() {
                    public void run() {
                        if (getCircle().getCenterY() != closest.getCircle().getCenterY()) {
                            getCircle().setCenterY(getCircle().getCenterY() + addY);
                        }
                    }
                };
                JavaFxGame.runAndWait(t);

            }
            setCurrentNode(closest);

            if (getCurrentNode() != null) {

                while (!isStopped() && !isSuspended()) {
                    while (isBusyCuzWorking()) {
                        try {
                            Thread.sleep(timeInterval * 10);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(BadGuy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    while (getCurrentNode() instanceof City && !((City) getCurrentNode()).isDead()) {
                        try {
                            Thread.sleep(timeInterval);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(BadGuy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //check for collision with Superhero
                        try {
                            synchronized (this) {
                                while (isSuspended()) {
                                    Thread.sleep(timeInterval);
                                }
                                if (isStopped()) {
                                    break;
                                }
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        boolean exceptionCatched = false;
                        do {
                            exceptionCatched = false;
                            try {
                                for (Iterator<Guy> iter = getGraph().getGuys().iterator(); iter.hasNext();) {
                                    Guy iterator = iter.next();
                                    if (iterator instanceof Superhero && !isStopped() && !iterator.isStopped() && getCircle().getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                        fight((Superhero) iterator);
                                    }
                                }
                            } catch (ConcurrentModificationException e) {
                                exceptionCatched = true;
                            }
                        } while (exceptionCatched);
                        //drain city
                        try {
                            synchronized (this) {
                                while (isSuspended()) {
                                    Thread.sleep(timeInterval);
                                }
                                if (isStopped()) {
                                    break;
                                }
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        increaseAbility((float) 0.1 * (float) 0.1, ((City) getCurrentNode()).getDrained((float) -0.1));
                        boolean killed = false;
                        exceptionCatched = false;
                        try {
                            synchronized (this) {
                                while (isSuspended()) {
                                    Thread.sleep(timeInterval);
                                }
                                if (isStopped()) {
                                    break;
                                }
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        do {
                            exceptionCatched = false;
                            try {
                                for (Iterator<Guy> iter = getGraph().getGuys().iterator(); iter.hasNext();) {
                                    Guy iterator = iter.next();
                                    if (!killed && iterator instanceof Citizen && !isStopped() && !iterator.isStopped() && getCircle().getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                        iterator.myStop();
                                        killed = true;

                                    }
                                }
                            } catch (ConcurrentModificationException e) {
                                exceptionCatched = true;
                            }
                        } while (exceptionCatched && !killed);

                    }
                    if (getCurrentNode() instanceof City) {
                        ((City) getCurrentNode()).setBadGuyIsGoingToThisCity(false);
                    }
                    Random randomGenerator = new Random();
                    try {
                        Thread.sleep(randomGenerator.nextInt(400));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BadGuy.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //go to the next city
                    try {
                        go(getGraph().findPathBetweenCities(getCurrentNode(), getGraph().getRandomCity(getCurrentNode())));
                    } catch (TryLater e) {
                    }
                }
            }

        }
        if (destination != null) {
            destination.setBadGuyIsGoingToThisCity(false);
        }
        getGraph().getBadGuys().remove(this);
        deleteFromPane();
    }

    /**
     * Makes BadGuy visit all Nodes on the Stack.
     *
     * @param the following nodes on the path to visit
     */
    void go(Stack<Node> path) {
        setBusyCuzWorking(true);
        if (!path.empty()) {
            setCurrentNode(path.get(0));
            if (getCurrentNode() instanceof City) {
                ((City) getCurrentNode()).setBadGuyIsGoingToThisCity(false);
            }
            if (path.lastElement() instanceof City) {
                ((City) path.lastElement()).setBadGuyIsGoingToThisCity(true);
                destination = (City) path.lastElement();
            }
            path.remove(0);

            Thread renderer;
            renderer = new Thread() {
                private boolean found = false;
                private Circle c;

                @Override
                public void run() {
                    for (Node it : path) {

                        Thread t;
                        if (getCircle().getCenterY() == it.getCircle().getCenterY() && getCircle().getCenterX() < it.getCircle().getCenterX()) {//go right
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(timeInterval);
                                    }
                                    if (isStopped()) {
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
                                            Thread.sleep(timeInterval);
                                        }
                                        if (isStopped()) {
                                            break;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                try {
                                    Thread.sleep(timeInterval / 2);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                checkIntersectionsWhileGoingRight();
                                t = new Thread() {
                                    public void run() {
                                        getCircle().setCenterX(getCircle().getCenterX() + 1);
                                    }
                                };
                                JavaFxGame.runAndWait(t);
                            }
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(timeInterval);
                                    }
                                    if (isStopped()) {
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
                                        Thread.sleep(timeInterval);
                                    }
                                    if (isStopped()) {
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
                                            Thread.sleep(timeInterval);
                                        }
                                        if (isStopped()) {
                                            break;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                checkIntersectionsWhileGoingLeft();
                                try {
                                    Thread.sleep(timeInterval / 2);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
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
                                        Thread.sleep(timeInterval);
                                    }
                                    if (isStopped()) {
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
                                        Thread.sleep(timeInterval);
                                    }
                                    if (isStopped()) {
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
                                            Thread.sleep(timeInterval);
                                        }
                                        if (isStopped()) {
                                            break;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                checkIntersectionsWhileGoingDown();
                                try {
                                    Thread.sleep(timeInterval / 2);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
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
                                        Thread.sleep(timeInterval);
                                    }
                                    if (isStopped()) {
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
                        } else if (getCircle().getCenterY() > it.getCircle().getCenterY() && getCircle().getCenterX() == it.getCircle().getCenterX()) {//go up
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(timeInterval);
                                    }
                                    if (isStopped()) {
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
                                            Thread.sleep(timeInterval);
                                        }
                                        if (isStopped()) {
                                            break;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                try {
                                    Thread.sleep(timeInterval / 2);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                checkIntersectionsWhileGoingUp();
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
                                        Thread.sleep(timeInterval);
                                    }
                                    if (isStopped()) {
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
                        setCurrentNode(path.lastElement());

                    }
                    setBusyCuzWorking(false);
                }

                /**
                 * Checks for intersections and makes sure BadGuy can move
                 * forward.
                 */
                private void checkIntersectionsWhileGoingLeft() {
                    c = new Circle(getCircle().getCenterX() - 1, getCircle().getCenterY(), getRadius());

                    do {
                        found = false;
                        try {
                            for (Iterator<Guy> iter = getGraph().getGuys().iterator(); iter.hasNext();) {
                                Guy iterator = iter.next();
                                if (!(iterator instanceof BadGuy) && !isStopped() && !iterator.isStopped() && iterator.getCircle() != getCircle() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                    if (iterator.isCitizen()) {
                                        iterator.myStop();
                                    } else {
                                        fight((Superhero) iterator);
                                    }
                                }
                                if (!(iterator instanceof BadGuy) && !isStopped() && !iterator.isStopped() && iterator.getCircle() != getCircle() && Math.abs(iterator.getCircle().getCenterX() - c.getCenterX()) <= 2 && Math.abs(iterator.getCircle().getCenterY() - c.getCenterY()) <= 2 * Graph.getRoadWidth()) {
                                    if (iterator.isCitizen()) {
                                        iterator.myStop();
                                    } else {
                                        fight((Superhero) iterator);
                                    }
                                }
                            }
                        } catch (ConcurrentModificationException e) {
                            found = true;
                        }
                    } while (found);
                    do {
                        found = false;

                        try {
                            for (Iterator<BadGuy> iter = getGraph().getBadGuys().iterator(); iter.hasNext();) //                        for (Guy iterator : graph.getGuys()) 
                            {
                                Guy iterator = iter.next();
                                if (!isStopped() && !iterator.isStopped() && iterator.getCircle() != getCircle() && iterator.getCircle().getCenterX() <= c.getCenterX() && iterator.getCircle().getCenterY() == c.getCenterY() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                    try {
                                        Thread.sleep(timeInterval);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    found = true;
                                }
                            }
                        } catch (ConcurrentModificationException e) {

                            found = true;
                        }
                    } while (found);
                }

                /**
                 * Checks for intersections and makes sure BadGuy can move
                 * forward.
                 */
                private void checkIntersectionsWhileGoingDown() {
                    c = new Circle(getCircle().getCenterX(), getCircle().getCenterY() + 1, getRadius());

                    do {
                        found = false;
                        try {
                            for (Iterator<Guy> iter = getGraph().getGuys().iterator(); iter.hasNext();) {
                                Guy iterator = iter.next();
                                if (!(iterator instanceof BadGuy) && !isStopped() && !iterator.isStopped() && iterator.getCircle() != getCircle() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                    if (iterator.isCitizen()) {
                                        iterator.myStop();
                                    } else {
                                        fight((Superhero) iterator);
                                    }

                                } else if (!(iterator instanceof BadGuy) && !isStopped() && !iterator.isStopped() && Math.abs(iterator.getCircle().getCenterX() - c.getCenterX()) <= 2 * Graph.getRoadWidth() && Math.abs(iterator.getCircle().getCenterY() - c.getCenterY()) <= 2) {
                                    if (iterator.isCitizen()) {
                                        iterator.myStop();
                                    } else {
                                        fight((Superhero) iterator);
                                    }
                                }
                            }
                        } catch (ConcurrentModificationException e) {
                            found = true;
                        }
                    } while (found);
                    do {
                        found = false;
                        try {
                            for (Iterator<BadGuy> iter = getGraph().getBadGuys().iterator(); iter.hasNext();) //                        for (Guy iterator : graph.getGuys()) 
                            {
                                Guy iterator = iter.next();
                                if (!isStopped() && !iterator.isStopped() && iterator.getCircle() != getCircle() && iterator.getCircle().getCenterX() == c.getCenterX() && iterator.getCircle().getCenterY() >= c.getCenterY() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                    try {
                                        Thread.sleep(timeInterval);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    found = true;
                                }
                            }
                        } catch (ConcurrentModificationException e) {

                            found = true;
                        }
                    } while (found);

                }

                /**
                 * Checks for intersections and makes sure object can move
                 * forward.
                 */
                private void checkIntersectionsWhileGoingUp() {
                    c = new Circle(getCircle().getCenterX(), getCircle().getCenterY() - 1, getRadius());

                    do {
                        found = false;
                        try {
                            for (Iterator<Guy> iter = getGraph().getGuys().iterator(); iter.hasNext();) {
                                Guy iterator = iter.next();
                                if (!(iterator instanceof BadGuy) && !isStopped() && !iterator.isStopped() && iterator.getCircle() != getCircle() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                    if (iterator.isCitizen()) {
                                        iterator.myStop();
                                    } else {
                                        fight((Superhero) iterator);
                                    }
                                } else if (!(iterator instanceof BadGuy) && !isStopped() && !iterator.isStopped() && Math.abs(iterator.getCircle().getCenterX() - c.getCenterX()) <= 2 * Graph.getRoadWidth() && Math.abs(iterator.getCircle().getCenterY() - c.getCenterY()) <= 2) {
                                    if (iterator.isCitizen()) {
                                        iterator.myStop();
                                    } else {
                                        fight((Superhero) iterator);
                                    }
                                }
                            }
                        } catch (ConcurrentModificationException e) {
                            found = true;
                        }
                    } while (found);
                    do {
                        found = false;
                        try {
                            for (Iterator<BadGuy> iter = getGraph().getBadGuys().iterator(); iter.hasNext();) //                        for (Guy iterator : graph.getGuys()) 
                            {
                                Guy iterator = iter.next();
                                if (!isStopped() && !iterator.isStopped() && iterator.getCircle() != getCircle() && iterator.getCircle().getCenterX() == c.getCenterX() && iterator.getCircle().getCenterY() <= c.getCenterY() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                    try {
                                        Thread.sleep(timeInterval);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    found = true;
                                }
                            }
                        } catch (ConcurrentModificationException e) {
                            found = true;
                        }
                    } while (found);
                }

                /**
                 * Checks for intersections and makes sure object can move
                 * forward.
                 */
                private void checkIntersectionsWhileGoingRight() {
                    c = new Circle(getCircle().getCenterX() + 1, getCircle().getCenterY(), getRadius());

                    do {
                        found = false;
                        try {
                            for (Iterator<Guy> iter = getGraph().getGuys().iterator(); iter.hasNext();) {
                                Guy iterator = iter.next();
                                if (!(iterator instanceof BadGuy) && !isStopped() && !iterator.isStopped() && iterator.getCircle() != getCircle() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                    if (iterator.isCitizen()) {
                                        iterator.myStop();
                                    } else {
                                        fight((Superhero) iterator);
                                    }
                                } else if (!(iterator instanceof BadGuy) && !isStopped() && !iterator.isStopped() && Math.abs(iterator.getCircle().getCenterX() - c.getCenterX()) <= 2 && Math.abs(iterator.getCircle().getCenterY() - c.getCenterY()) <= 2 * Graph.getRoadWidth()) {
                                    if (iterator.isCitizen()) {
                                        iterator.myStop();
                                    } else {
                                        fight((Superhero) iterator);
                                    }
                                }
                            }
                        } catch (ConcurrentModificationException e) {
                            found = true;
                        }
                    } while (found);
                    do {
                        found = false;
                        try {
                            for (Iterator<BadGuy> iter = getGraph().getBadGuys().iterator(); iter.hasNext();) //                        for (Guy iterator : graph.getGuys()) 
                            {
                                Guy iterator = iter.next();
                                if (!isStopped() && !iterator.isStopped() && iterator.getCircle() != getCircle() && iterator.getCircle().getCenterX() >= c.getCenterX() && iterator.getCircle().getCenterY() == c.getCenterY() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                    try {
                                        Thread.sleep(timeInterval);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    found = true;
                                }
                            }
                        } catch (ConcurrentModificationException e) {

                            found = true;
                        }
                    } while (found);
                }
            };
//        
            renderer.setDaemon(true);
            renderer.start();
        }
    }

}
