package javafxgame;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class Citizen extends Guy {

    private String surname;
    private City nativeCity;
    private static final String colour = "blue";
    private static final int hp = 10;
    
    public Citizen(City nativeCity) {
        super(hp, nativeCity.getCircle().getCenterX(), nativeCity.getCircle().getCenterY(), nativeCity.getPane(), nativeCity);
//        running = true;
        this.nativeCity = nativeCity;
        thisEntity=this;
       getCircle().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("kliknales mnie bro"+getCircle());
                //myStop();
                getGraph().displayEntity(thisEntity);
//                if(!suspended) mySuspend();
//                else {myResume();System.out.println("wznowilem bro!");}
//                setRunning(false);
//                thrd.stop();
            }
        });
      
    }

    @Override
    public void run() {
        while (true) {

            try {
                while (zajety) {
                    thrd.sleep(1000);
//            System.out.println("ludzik zajety bro!");
                };
//                if(!isRunning()) { System.out.println("wybrejkowales mnie bro");break;}
                synchronized(this){
                    while(suspended){
                        wait();
                    }
                    if(stopped) break;
                }
//                System.out.println("niezajety! znow jade");
                Random randomGenerator = new Random();
                Thread.sleep(randomGenerator.nextInt(10000)+1000);
                boolean succeded=true;
                do{
                    thrd.sleep(100);
                    try{
                go(getGraph().findPathBetweenCities(currentNode, getGraph().getRandomCity(currentNode)));
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
//               getGraph().getGuys().remove(this); tu jest problem bo inne watki iteruja po tej liscie!
        JavaFxGame.runAndWait( new Thread(){
            public void run(){

                getCircle().setCenterX(-100);
                                                getPane().getChildren().remove(getCircle());

            }
        });
 

        
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
