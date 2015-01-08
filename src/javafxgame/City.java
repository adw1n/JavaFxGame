package javafxgame;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class City extends Node { //city na razie ma tylko wiekszy promien

    private static final int radius = 32;
    private boolean badGuyIsGoingToThisCity;
    final int maxInitFightersAbilityAttributeValue=10;
//    ArrayList<Citizen> citizens;
    int numberOfCitizens;
    ArrayList<PowerSource> powerSources;
    static int ile = 0;
    private boolean dead;
    public City(int x, int y, Pane pane, Graph graph) {
        super(x, y, pane, radius, graph,graph.getNameGetter().getCityName());
        initializeVariables(graph);
    }

    private void initializeVariables(Graph graph) {
        dead=false;
        badGuyIsGoingToThisCity=false;
        ile++;
       initializePowerSources();
        System.out.println("power Sources size: "+powerSources.size()+powerSources.get(0));
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
            powerSources.add(new PowerSource(new FightersAbility(a,randomGenerator.nextInt(maxInitFightersAbilityAttributeValue)+1),getGraph() ));
                    
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
        ans+="\nNumber of citizens: "+numOfCitizens;
        for(PowerSource it: powerSources)
            ans+="\n"+it;
        return ans; //To change body of generated methods, choose Tools | Templates.
    }
    
    public City(int x, int y, Pane pane, Graph graph,int radius,String name) {
        super(x, y, pane, radius, graph,name);
        initializeVariables(graph);
    }
    public synchronized void getDrained(float ammount){
        int ammountOfDeadPowerSources=0;
        if(!isIsDefeated()){
        for(PowerSource it: powerSources){
            it.decreaseEnergy(ammount);
            if(it.getEnergy()<=0) ammountOfDeadPowerSources++;
        }
        if(ammountOfDeadPowerSources==powerSources.size()) {setDead(true);System.out.println("miasto umarlo");setIsDefeated(true);getGraph().decreaseNumOfCitiesAlive();}
        }
        
    }
    public void createCitizen() {
//        Citizen c=new Citizen(this);
        Citizen c = new Citizen(this,getGraph());
        c.setGraph(getGraph());
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
        this.badGuyIsGoingToThisCity = badGuyIsGoingToThisCity;
    }

}
