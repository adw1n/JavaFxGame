package javafxgame;

import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Citizen extends Guy {

    private String surname;
    private City nativeCity;
    private static final String colour = "blue";
    private static final int hp = 10;
    
    public Citizen(City nativeCity,Graph graph) {
        super(hp, nativeCity.getCircle().getCenterX(), nativeCity.getCircle().getCenterY(), nativeCity.getPane(), nativeCity,graph.getNameGetter().getFemaleName());
//        running = true;
        this.nativeCity = nativeCity;
        updateCityInfo();
        
       
      
    }

    @Override
    public String toString() {
        return "Citizen "+super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    

    @Override
    public boolean isCitizen() {
        return true;
    }
    
    @Override
    public void run() {
        boolean inNativeCity=true;
        while (true) {

            try {
                while (zajety) {
                    synchronized(this){
                    
                    if(isStopped()) break;
                }
                    thrd.sleep(1000);
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
                    thrd.sleep(100);
                    
                    try{
                        if(currentNode == nativeCity){//to nie zadziala, bo koles bedzie kombinowal zeby uniknac
                            if(isChangeDirection() &&getDestination()!=null)
                                go(getGraph().findPathBetweenCities(currentNode, getDestination()));
                            else
                go(getGraph().findPathBetweenCities(currentNode, getGraph().getRandomCity(currentNode)));
//                inNativeCity=false;
                        }
                        else{
                            
                            if(nativeCity.isIsDefeated() && (currentNode instanceof City)){
                                nativeCity=(City)currentNode;
                            }
                            go(getGraph().findPathBetweenCities(currentNode, nativeCity));
//                            inNativeCity=true;
                            
                        }
                    }
                    catch(TryLater e){
                        succeded=false;
                        System.out.println("polecial wyjatek bro!");
                    }
                }
                while (!succeded);
                
//                System.out.println("wysleepowalem sie");
//            //goToCity();
//            }
            } catch (InterruptedException ex) {
                Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("juz nie runnuje");
//        for(Iterator<Guy> iter=getGraph().getGuys().iterator();iter.hasNext();)
////                        for (Guy iterator : graph.getGuys()) 
//                        {
//                            Guy iterator=iter.next();
//                            if(iterator==this){
//                                iter.remove();
//                                System.out.println("usunalem bro!");
//                            }
//        }
                                
               getGraph().getGuys().remove(this);// tu jest problem bo inne watki iteruja po tej liscie!
               System.out.println("ilosc ludzikow to: "+getGraph().getGuys().size());
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

    void stop() {

    }

    City selectCityToGoTo() {
        return null;
    }

    /**
     * @param nativeCity the nativeCity to set
     */
    public void setNativeCity(City nativeCity) {
        this.nativeCity = nativeCity;
    }
}
