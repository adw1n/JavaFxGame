

package javafxgame;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class PowerSourcesSummator implements Runnable {
    private Graph graph;
    private Thread thrd;
    private Button createCitizen,createSuperhero;
    private Label sumOfPowerSources;
    public PowerSourcesSummator(Graph graph,Label sumOfPowerSources,Button createCitizen,Button createSuperhero) {
        this.graph=graph;
        this.createCitizen=createCitizen;
        this.createSuperhero=createSuperhero;
        this.sumOfPowerSources=sumOfPowerSources;
        thrd=new Thread();
        thrd.setDaemon(true);
        thrd.start();
    }

    @Override
    public void run() {
        while(true){
            try {
                thrd.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(PowerSourcesSummator.class.getName()).log(Level.SEVERE, null, ex);
            }
            //sum all energy of 
        }
        
    }
    
}
