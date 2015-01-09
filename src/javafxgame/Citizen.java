package javafxgame;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Citizen is a guy, who keeps traveling between his native city and other cities, has the power to heal/strenghten a city by making PowerSouces in a city stronger.
 * He is extreamly fragile and easy to kill. He cant fight. Sometimes he suspends on the road and can cause a roadblock, so watch out!
 * @author adwin_
 */
public class Citizen extends Guy {

    private String surname;
    private City nativeCity;
    private static final String colour = "blue";
    private static final int hp = 10;
    /**
     * Initializes a citizen.
     * @param nativeCity the native city, where the citizen was born and he grew up, where he lives and has family
     * @param graph the graph that the citizen belongs to
     */ 
    public Citizen(City nativeCity,Graph graph) {
        super(hp, nativeCity.getCircle().getCenterX(), nativeCity.getCircle().getCenterY(), nativeCity.getPane(), nativeCity,graph.getNameGetter().getFemaleName());
        this.nativeCity = nativeCity;
        updateCityInfo();
        
       
      
    }
    /**
     * 
     * @return the info about the Citizen in a string
     */
    @Override
    public String toString() {
        return "Citizen "+super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    

    @Override
    public boolean isCitizen() {
        return true;
    }
    /**
     * Starts a thread, travels between cities and while being in a city, works on the PowerSources to make them stronger.
     */
    @Override
    public void run() {
        boolean inNativeCity=true;
        while (true) {

            try {
                while (isBusyCuzWorking()) {
                    synchronized(this){
                    
                    if(isStopped()) break;
                }
                    getThrd().sleep(1000);
                };
                synchronized(this){
                    while(isSuspended()){
                        wait();
                    }
                    if(isStopped()) break;
                }
                sleepAndUpdatePowerSources();
                boolean succeded=true;
                do{
                    synchronized(this){
                    while(isSuspended()){
                        wait();
                    }
                    if(isStopped()) break;
                }
                    getThrd().sleep(100);
                    
                    try{
                        if(getCurrentNode() == nativeCity){
                            if(isChangeDirection() &&getDestination()!=null)
                                go(getGraph().findPathBetweenCities(getCurrentNode(), getDestination()));
                            else
                go(getGraph().findPathBetweenCities(getCurrentNode(), getGraph().getRandomCity(getCurrentNode())));
                        }
                        else{
                            
                            if(nativeCity.isIsDefeated() && (getCurrentNode() instanceof City)){
                                nativeCity=(City)getCurrentNode();
                            }
                            go(getGraph().findPathBetweenCities(getCurrentNode(), nativeCity));
                        }
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

    

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the nativeCity
     */
    public City getNativeCity() {
        //nativeCity might have been destroyed, add checking this l8er
        return nativeCity;
    }

    /**
     * @param nativeCity the nativeCity to set
     */
    public void setNativeCity(City nativeCity) {
        this.nativeCity = nativeCity;
    }
}
