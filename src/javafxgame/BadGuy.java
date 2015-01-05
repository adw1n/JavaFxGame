
package javafxgame;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.shape.Circle;
//dodac pole ileKilledCitizens i potem to wyswietlac w statystykach!!
//raczej tez moze rzucic wyjatkiem Concurrent...
//bad guys nachodza na siebie
public class BadGuy extends Fighter {

    Node closest = null;
    static final private int inf = 1000000;
//wywalic z konstruktora hp - zbedne....
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
    public boolean isCitizen() {
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
            //while nie stopped
            //drain city
            if(currentNode!=null){
                while(!isStopped() && !isSuspended()){
                    while(zajety) try {
                        Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(BadGuy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                              
                while(currentNode instanceof City &&!((City)currentNode).isDead()){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BadGuy.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //check for collision with Superhero
                    try {
                                    synchronized (this) {
                                        while (isSuspended()) {
                                            Thread.sleep(100);
                                        }
                                        if (isStopped()) {
                                            break;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                    boolean znalezione=false;
                    do {
                        znalezione = false;
                        try{
                        for(Iterator<Guy> iter=getGraph().getGuys().iterator();iter.hasNext();)
                        {
                            Guy iterator=iter.next();
                            if (iterator instanceof Superhero &&!isStopped() &&!iterator.isStopped()  && getCircle().getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {                              
                                fight((Superhero)iterator);
                                System.out.println("Walka!");
                            }
                        }
                        }
                        catch(ConcurrentModificationException e){
                            znalezione=true;
                        }
                    } while (znalezione);
                    //drain city
                    try {
                                    synchronized (this) {
                                        while (isSuspended()) {
                                            Thread.sleep(100);
                                        }
                                        if (isStopped()) {
                                            break;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                    ((City)currentNode).getDrained((float)-0.1);
                    boolean zabity=false;
                    znalezione=false;
                    //zabij citizena
                    try {
                                    synchronized (this) {
                                        while (isSuspended()) {
                                            Thread.sleep(100);
                                        }
                                        if (isStopped()) {
                                            break;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                }
                    do {
                        znalezione = false;
                        try{
                        for(Iterator<Guy> iter=getGraph().getGuys().iterator();iter.hasNext();)
                        {
                            Guy iterator=iter.next();
                            if (!zabity &&iterator instanceof Citizen &&!isStopped() &&!iterator.isStopped()  && getCircle().getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {                              
                                iterator.myStop();
                                zabity=true;
                                
                            }
                        }
                        }
                        catch(ConcurrentModificationException e){
                            znalezione=true;
                        }
                    } while (znalezione && !zabity);
                    
                }
                System.out.println("miasto umarlo");
            //go to the next city
            try{
                go(getGraph().findPathBetweenCities(currentNode, getGraph().getRandomCity(currentNode)));
                    }
                    catch(TryLater e){
//                        succeded=false;
                        System.out.println("polecial wyjatek bro!");
                    }
            }
            }
            
        }
        
        System.out.println("umarl bad guy");
        deleteFromPane();
    }
        /**
     * @return true if you have safely arrived to the destination to be
     * implemented
     */
    void go(Stack<Node> path) {
        zajety = true;
        //jezeli jestem w srodku miasta
        if (!path.empty()) {
            currentNode = path.get(0);
            path.remove(0);
            Thread renderer;
            renderer = new Thread() {
                boolean znalezione = false;
                Circle c;
                Node skrzyzowanie = null;
                boolean wchodze = false;
                @Override
                public void run() {
                    for (Node it : path) {
                        boolean spieprzone = false;
                        Thread t;
                        if (getCircle().getCenterY() == it.getCircle().getCenterY() && getCircle().getCenterX() < it.getCircle().getCenterX()) {//go right
                            try {
                                synchronized (this) {
                                    while (isSuspended()) {
                                        Thread.sleep(100);
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
                                            Thread.sleep(100);
                                        }
                                        if (isStopped()) {
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
                                checkIntersectionsWhileGoingRight(); //tu trzeba sprawdzic do zabijania
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
                                        Thread.sleep(100);
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
                                        Thread.sleep(100);
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
                                            Thread.sleep(100);
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
                                    Thread.sleep(50);
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
                                        Thread.sleep(100);
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
                                        Thread.sleep(100);
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
                                            Thread.sleep(100);
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
                                    Thread.sleep(50);
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
                                        Thread.sleep(100);
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
                                        Thread.sleep(100);
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
                                            Thread.sleep(100);
                                        }
                                        if (isStopped()) {
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
                        } else {
                            System.out.println("cos spieprzylem!");
                        }
                        currentNode = path.lastElement();
                    }
                    zajety = false;
                }
                private void checkIntersectionsWhileGoingLeft() {
                    c = new Circle(getCircle().getCenterX() - 1, getCircle().getCenterY(), getRadius());
                    
                    do {
                        znalezione = false;
                        try{
                        for(Iterator<Guy> iter=getGraph().getGuys().iterator();iter.hasNext();)
                        {
                            Guy iterator=iter.next();
                            if (!(iterator instanceof BadGuy) && !isStopped() &&!iterator.isStopped() && iterator.getCircle() != getCircle()   && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                if(iterator.isCitizen())
                                iterator.myStop();
                                else fight((Superhero)iterator);
                            }
                            if (!(iterator instanceof BadGuy) &&!isStopped() &&!iterator.isStopped() && iterator.getCircle() != getCircle() && Math.abs(iterator.getCircle().getCenterX() - c.getCenterX())<=2 && Math.abs(iterator.getCircle().getCenterY() - c.getCenterY())<=2*Graph.getRoadWidth() ) {
                                if(iterator.isCitizen())
                                iterator.myStop();
                                else fight((Superhero)iterator);
                            }
                        }
                        }
                        catch(ConcurrentModificationException e){
                            znalezione=true;
                        }
                    } while (znalezione);
                }

                private void checkIntersectionsWhileGoingDown() {
                    c = new Circle(getCircle().getCenterX(), getCircle().getCenterY() + 1, getRadius());
                    
                    do {
                        znalezione = false;
                        try{
                        for(Iterator<Guy> iter=getGraph().getGuys().iterator();iter.hasNext();)
                        {
                            Guy iterator=iter.next();
                            if (!(iterator instanceof BadGuy) &&!isStopped() &&!iterator.isStopped()   &&iterator.getCircle() != getCircle()&& c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {                              
                                if(iterator.isCitizen())
                                iterator.myStop();
                                else fight((Superhero)iterator);
   
                            }
                            else if (!(iterator instanceof BadGuy) &&!isStopped()  &&!iterator.isStopped() &&  Math.abs(iterator.getCircle().getCenterX() - c.getCenterX())<=2*Graph.getRoadWidth() && Math.abs(iterator.getCircle().getCenterY() - c.getCenterY())<=2 ) {
                                                                if(iterator.isCitizen())
                                iterator.myStop();
                                else fight((Superhero)iterator);
                            }
                        }
                        }
                        catch(ConcurrentModificationException e){
                            znalezione=true;
                        }
                    } while (znalezione);
                }

                private void checkIntersectionsWhileGoingUp() {
                    c = new Circle(getCircle().getCenterX(), getCircle().getCenterY() - 1, getRadius());
                    
                    do {
                        znalezione = false;
                    try{
                        for(Iterator<Guy> iter=getGraph().getGuys().iterator();iter.hasNext();) 
                        {
                            Guy iterator=iter.next();
                            if (!(iterator instanceof BadGuy) &&!isStopped() &&!iterator.isStopped() &&  iterator.getCircle() != getCircle() && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                if(iterator.isCitizen())
                                iterator.myStop();
                                else fight((Superhero)iterator);
                            }
                            else if (!(iterator instanceof BadGuy)&&!isStopped()  &&!iterator.isStopped() &&  Math.abs(iterator.getCircle().getCenterX() - c.getCenterX())<=2*Graph.getRoadWidth() && Math.abs(iterator.getCircle().getCenterY() - c.getCenterY())<=2) {
                                if(iterator.isCitizen())
                                iterator.myStop();
                                else fight((Superhero)iterator);
                            }  
                        }
                    }
                    catch(ConcurrentModificationException e){
                            znalezione=true;
                        }
                    } while (znalezione);
                }

                private void checkIntersectionsWhileGoingRight() {
                    c = new Circle(getCircle().getCenterX() + 1, getCircle().getCenterY(), getRadius());
                    
                    do {
                        znalezione = false;
                    try{
                        for(Iterator<Guy> iter=getGraph().getGuys().iterator();iter.hasNext();)
                        {
                            Guy iterator=iter.next();
                            if (!(iterator instanceof BadGuy) &&!isStopped() &&!iterator.isStopped() &&  iterator.getCircle() != getCircle()  && c.getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                if(iterator.isCitizen())
                                iterator.myStop();
                                else fight((Superhero)iterator);
                            }
                            else if (!(iterator instanceof BadGuy) &&!isStopped()  &&!iterator.isStopped() &&  Math.abs(iterator.getCircle().getCenterX() - c.getCenterX())<=2 && Math.abs(iterator.getCircle().getCenterY() - c.getCenterY())<=2*Graph.getRoadWidth() ) {
                                if(iterator.isCitizen())
                                iterator.myStop();
                                else fight((Superhero)iterator);
                            }
                        }
                    }
                    catch(ConcurrentModificationException e){
                            znalezione=true;
                        }
                    } while (znalezione);
                }
            };
//        
            renderer.setDaemon(true);
            renderer.start();
        }
    }
    
}
