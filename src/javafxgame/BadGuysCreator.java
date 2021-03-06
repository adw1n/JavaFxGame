
package javafxgame;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 * Class responsible for spawning enemies, over time spawning more and more of
 * them.
 *
 * @author adwin_
 */
public class BadGuysCreator implements Runnable {

    private final Random randomGenerator;
    
    private final Thread thrd;
    private final Graph graph;
    private int x, y;
    private int howManyBadGuysCreatedSoFar;
    private static final int maxSleepingTime = 10000;//10s

    /**
     * Constructs an object of class BadGuy
     *
     * @param graph the graph that you add badguy to
     */
    public BadGuysCreator(Graph graph) {
        randomGenerator = new Random();
        this.graph = graph;
        howManyBadGuysCreatedSoFar = 0;
        thrd = new Thread(this);
        thrd.setDaemon(true);
        thrd.start();
    }

    /**
     * Creates new enemies, and refreashes ControlPanel.
     */
    @Override
    public void run() {
        
        while (true) {
            
            x = 0;
            y = 0;
            howManyBadGuysCreatedSoFar++;
            try {
                final int numOfParts = 50;
                int sleepingTime = (randomGenerator.nextInt(maxSleepingTime) + maxSleepingTime / 4) / numOfParts;
                for (int i = 0; i < numOfParts; i++) {
                    Thread.sleep(sleepingTime);
                    Platform.runLater(new Runnable() {
                        
                        @Override
                        public void run() {
                            graph.displayEntity();
                        }
                    });
                    
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(BadGuysCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (int i = 0; i <= howManyBadGuysCreatedSoFar / 20; i++) {
                int option = randomGenerator.nextInt(4);
                if (option == 0) {//enemy comes from the top of the screen
                    //y=0, x=random from <0,rightScreenEdge
                    x = randomGenerator.nextInt(JavaFxGame.getRightScreenEdge() + 1);
                    y = -10;
                } else if (option == 1)//enemy comes from the right
                {
                    y = randomGenerator.nextInt(JavaFxGame.getDownScreenEdge() + 1);
                    x = JavaFxGame.getRightScreenEdge();
                } else if (option == 2) {//enemy comes from the bottom
                    y = JavaFxGame.getDownScreenEdge() + 10;
                    x = randomGenerator.nextInt(JavaFxGame.getRightScreenEdge() + 1);
                } else if (option == 3) {
                    x = -10;
                    y = randomGenerator.nextInt(JavaFxGame.getDownScreenEdge() + 1);
                }
                JavaFxGame.runAndWait(new Runnable() {
                    
                    @Override
                    public void run() {
                        BadGuy b;
//                    for(int i=0;i<=20;i++){
//                    
                        b = new BadGuy(randomGenerator.nextInt(BadGuy.getMaxHP()), x, y, graph.getPane(), graph);
                        graph.addBadGuy(b);
                        b.startThread();
//                    }
                    }
                });
            }
        }
        
    }
    
}
