package javafxgame;

import javafx.scene.layout.Pane;

public class Citizen extends Guy{
    private String surname;
    private City nativeCity;
    private static final String colour="yellow";
    private static final int hp=10;
    public Citizen(City nativeCity){
        super(hp,nativeCity.getCircle().getCenterX(),nativeCity.getCircle().getCenterY(),nativeCity.getPane());
        this.nativeCity=nativeCity;
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
