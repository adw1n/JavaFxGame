package javafxgame;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Citizen extends Guy{
    private String surname;
    private City nativeCity;
    private static final String colour="yellow";
    private static final int hp=10;
    public Citizen(City nativeCity){
        super(hp,nativeCity.getCircle().getCenterX(),nativeCity.getCircle().getCenterY(),nativeCity.getPane(),nativeCity);
        
        this.nativeCity=nativeCity;
    }
    @Override
    public void run(){
        while(true){
                
        try {
            while(zajety){thrd.sleep(1000);
//            System.out.println("ludzik zajety bro!");
            };
                System.out.println("niezajety! znow jade");
            Random randomGenerator = new Random();
            Thread.sleep(randomGenerator.nextInt(10000));
            go(getGraph().findPathBetweenCities(currentNode,getGraph().getRandomCity(currentNode)));
            System.out.println("wysleepowalem sie");
//            //goToCity();
//            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
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
    void stop(){
        
    }
    City selectCityToGoTo(){
       return null;
    }
    /**
     * @param nativeCity the nativeCity to set
     */
    public void setNativeCity(City nativeCity) {
        this.nativeCity = nativeCity;
    }
}
