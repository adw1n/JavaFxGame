

package javafxgame;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;

/**
 * Superhero - the only man brave enough to fight against BadGuys. Think of him, as a cop.
 * @author adwin_
 */
public class Superhero extends Fighter{

   private final Capital capital;
   /**
    * Creates a superhero.
    * @param hp number of health points of the object
    * @param capital the capital where superhero is spawn
    * @param pane the pane that superhero is drawn on
    * @param graph the graph that the superhero belongs to
    */
    public Superhero(int hp, Capital capital, Pane pane, Graph graph) {
        super(hp, capital.getCircle().getCenterX(), capital.getCircle().getCenterY(), pane, null,graph.getNameGetter().getFemaleName());
        setGraph(graph);
        setCurrentNode(capital);        
        this.capital=capital;
        getCircle().setFill(valueOf("green"));
        graph.getControlPanel().displayEntity();
        updateCityInfo();
    }

    @Override
    public String toString() {
        return "Superhero " + super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCitizen() {
        return false;
    }
    /**
     * Makes the superhero fight BadGuys.
     */
    @Override
    public void run() {
        while (true) {

            try {
                while (isBusyCuzWorking() ) {
                    getThrd().sleep(1000);
                };
                synchronized(this){
                    while(isSuspended()){
                        wait();
                    }
                    if(isStopped()) break;
                }
                sleepAndUpdatePowerSources();
                synchronized(this){
                    while(isSuspended()){
                        wait();
                    }
                    if(isStopped()) break;
                }
                boolean succeded=true;
                do{
                    getThrd().sleep(100);
                    try{
                go(getGraph().findPathBetweenCities(getCurrentNode(), getGraph().getCityForSuperhero(getCurrentNode())));
                    }
                    catch(TryLater e){
                        succeded=false;
                    }
                }
                while (!succeded);
            } catch (InterruptedException ex) {
                Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                                getGraph().getGuys().remove(this);
        deleteFromPane();
 

        
    }
}
