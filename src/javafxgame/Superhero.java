

package javafxgame;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;


public class Superhero extends Fighter{

   Capital capital;
    public Superhero(int hp, Capital capital, Pane pane, Graph graph) {
        super(hp, capital.getCircle().getCenterX(), capital.getCircle().getCenterY(), pane, null);
        setGraph(graph);
        currentNode=capital;
        
        this.capital=capital;
        getCircle().setFill(valueOf("green"));
    }

    @Override
    public boolean isCitizen() {
        return false;
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
                    while(isSuspended()){
                        wait();
                    }
                    if(isStopped()) break;
                }
//                System.out.println("niezajety! znow jade");
                Random randomGenerator = new Random();
                Thread.sleep(randomGenerator.nextInt(10000)+10000);
                synchronized(this){
                    while(isSuspended()){
                        wait();
                    }
                    if(isStopped()) break;
                }
                boolean succeded=true;
                do{
                    thrd.sleep(100);
                    try{
                        //powinien isc do tego miasta co jest zloczynca...
                go(getGraph().findPathBetweenCities(currentNode, getGraph().getCityForSuperhero(currentNode)));
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
}
