package javafxgame;

import java.util.ArrayList;
import javafx.scene.layout.Pane;

public class City extends Node { //city na razie ma tylko wiekszy promien

    private static final int radius = 32;
    private boolean badGuyIsGoingToThisCity;
//    ArrayList<Citizen> citizens;
    int numberOfCitizens;
    ArrayList<PowerSource> powerSources;
    static int ile = 0;
    private boolean dead;
    public City(int x, int y, Pane pane, Graph graph) {
        super(x, y, pane, radius, graph);
        dead=false;
        badGuyIsGoingToThisCity=false;
//        citizens = new ArrayList<>();
        ile++;
//        if (ile  == 1) 
        for(int i=0;i<5;i++)
            createCitizen();
//        }
        powerSources=new ArrayList<>();
        powerSources.add(new PowerSource(new FightersAbility(Ability.POWER, 10)));
       
    }
    public City(int x, int y, Pane pane, Graph graph,int radius) {
        super(x, y, pane, radius, graph);
        dead=false;
//        citizens = new ArrayList<>();
        ile++;
//        citizens = new ArrayList<>();
        ile++;
//        if (ile  == 1) 
        for(int i=0;i<3;i++)
            createCitizen();
        powerSources=new ArrayList<>();
        powerSources.add(new PowerSource(new FightersAbility(Ability.POWER, 10)));
//        }
    }
    public synchronized void getDrained(float ammount){
        int ammountOfDeadPowerSources=0;
        for(PowerSource it: powerSources){
            it.decreaseEnergy(ammount);
            if(it.getEnergy()<=0) ammountOfDeadPowerSources++;
        }
        if(ammountOfDeadPowerSources==powerSources.size()) {setDead(true);setIsDefeated(true);getGraph().decreaseNumOfCitiesAlive();}
    }
    public void createCitizen() {
//        Citizen c=new Citizen(this);
        Citizen c = new Citizen(this);
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
