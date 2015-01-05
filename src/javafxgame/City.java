package javafxgame;

import java.util.ArrayList;
import javafx.scene.layout.Pane;

public class City extends Node { //city na razie ma tylko wiekszy promien

    private static final int radius = 32;
    ArrayList<Citizen> citizens;
    int numberOfCitizens;
    
    static int ile = 0;

    public City(int x, int y, Pane pane, Graph graph) {
        super(x, y, pane, radius, graph);
        citizens = new ArrayList<>();
        ile++;
//        if (ile  == 1) 
        for(int i=0;i<5;i++)
            createCitizen();
//        }
       
    }
    public City(int x, int y, Pane pane, Graph graph,int radius) {
        super(x, y, pane, radius, graph);
        citizens = new ArrayList<>();
        ile++;
//        if (ile  == 1) 
        for(int i=0;i<3;i++)
            createCitizen();
//        }
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

}
