package javafxgame;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class City extends Node { //city na razie ma tylko wiekszy promien

    private static final int radius = 32;
    private boolean badGuyIsGoingToThisCity;
    final int maxInitFightersAbilityAttributeValue=10;
//    ArrayList<Citizen> citizens;
    private int numOfBadGuysGoingToThisCity;
    int numberOfCitizens;
    private Label cityNameLabel;
    private ArrayList<PowerSource> powerSources;
    static int ile = 0;
    private boolean dead;
    public City(int x, int y, Pane pane, Graph graph) {
        super(x, y, pane, radius, graph,graph.getNameGetter().getCityName());
        initializeVariables(graph);
        numOfBadGuysGoingToThisCity=0;
        
    }

    private void initializeVariables(Graph graph) {
        dead=false;
        badGuyIsGoingToThisCity=false;
        ile++;
       initializePowerSources();
        System.out.println("power Sources size: "+getPowerSources().size()+getPowerSources().get(0));
        cityNameLabel=new Label(getName());
        cityNameLabel.setLayoutX(Math.max(getCircle().getCenterX()-getCircle().getRadius(),0));
        cityNameLabel.setLayoutY(Math.max(getCircle().getCenterY()+getCircle().getRadius(), 0));
        getPane().getChildren().add(cityNameLabel);
    }
    private void initializePowerSources(){
        
        powerSources=new ArrayList<>();
        Random randomGenerator = new Random();
        int numOfPowerSourcesInTheCity=randomGenerator.nextInt(4)+1;
        
        for(int numOfPowerSource=0;numOfPowerSource<numOfPowerSourcesInTheCity;++numOfPowerSource){
            Ability a=Ability.SWIFTNESS;
            switch(randomGenerator.nextInt(5)){
                case 0: a=Ability.ENERGY;
                    break;
                case 1: a=Ability.FIGHTINGSKILLS;
                    break;
                case 2: a=Ability.INTELLIGENCE;
                    break;
                case 3: a=Ability.POWER;
                    break;
                case 4: a=Ability.STAMINA;
                    break;
            }
            getPowerSources().add(new PowerSource(new FightersAbility(a,randomGenerator.nextInt(maxInitFightersAbilityAttributeValue)+1),getGraph() ));
                    
        }
    }
    @Override
    public String toString() {
        String ans="City: ";
        if(this instanceof Capital)
            ans="Captial: ";
        ans+=super.toString();
        ans+="\nStatus: ";
        if(isIsDefeated()) ans+="defeated";
        else ans+="city alive";
        int numOfCitizens=0;
        boolean foundException;
        do {
                        foundException = false;
                        
                        try{
                        for(Iterator<Guy> iter=getGraph().getGuys().iterator();iter.hasNext();)
//                        for (Guy iterator : graph.getGuys()) 
                        {
                            Guy iterator=iter.next();
                            if (!iterator.isStopped() && getCircle().getBoundsInParent().intersects(iterator.getCircle().getBoundsInParent())) {
                                numOfCitizens++;
                            }
                        }
                        }
                        catch(ConcurrentModificationException e){
                            System.out.println("zlapalem dupka w city");
                            foundException=true;
                        }
                    } while (foundException );
        ans+="\nNumber of your guys in the City: "+numOfCitizens;
        for(PowerSource it: getPowerSources())
            ans+="\n"+it;
        return ans; //To change body of generated methods, choose Tools | Templates.
    }
    
    public City(int x, int y, Pane pane, Graph graph,int radius,String name) {
        super(x, y, pane, radius, graph,name);
        initializeVariables(graph);
    }
    public synchronized Ability getDrained(float ammount){
        int ammountOfDeadPowerSources=0;
        if(!isIsDefeated()){
        for(PowerSource it: getPowerSources()){
            if(it.getEnergy()>0){
            it.decreaseEnergy(ammount);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    getGraph().getControlPanel().displayEntity();
                }
            });
            
            return it.getEnhancedAbility().getAbility();
            }
            if(it.getEnergy()<=0) ammountOfDeadPowerSources++;
        }
        if(ammountOfDeadPowerSources==getPowerSources().size()) {setDead(true);setIsDefeated(true);getGraph().decreaseNumOfCitiesAlive();}
        }
        return null;
    }
    public void createCitizen() {
//        Citizen c=new Citizen(this);
        Citizen c = new Citizen(this,getGraph());
        c.setGraph(getGraph());
        c.startThread();
//        citizens.add(c);
        getGraph().addGuy(c);
    }

    @Override
    void myStop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean isCity() {
        return true;
    }

    /**
     * @return the dead
     */
    public synchronized boolean isDead() {
        return dead;
    }

    /**
     * @param dead the dead to set
     */
    public synchronized void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * @return the badGuyIsGoingToThisCity
     */
    public synchronized boolean isBadGuyIsGoingToThisCity() {
        return badGuyIsGoingToThisCity;
    }

    /**
     * @param badGuyIsGoingToThisCity the badGuyIsGoingToThisCity to set
     */
    public synchronized void setBadGuyIsGoingToThisCity(boolean badGuyIsGoingToThisCity) {
        if(badGuyIsGoingToThisCity) numOfBadGuysGoingToThisCity++;
        else numOfBadGuysGoingToThisCity--;
        this.badGuyIsGoingToThisCity = numOfBadGuysGoingToThisCity>0;
    }

    /**
     * @return the powerSources
     */
    public ArrayList<PowerSource> getPowerSources() {
        return powerSources;
    }

}
