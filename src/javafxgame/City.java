

package javafxgame;

import java.util.ArrayList;
import javafx.scene.layout.Pane;


public class City extends Node{ //city na razie ma tylko wiekszy promien
    private static final int radius=32;
    ArrayList<Citizen> citizens;
    static int ile=0;
    public City(int x, int y, Pane pane,Graph graph) {
        super(x, y, pane,radius,graph);
        citizens=new ArrayList<>();
        ile++;
        if(ile%3==0)
        createCitizen();
    }
    public void createCitizen(){
//        Citizen c=new Citizen(this);
        Citizen c=new Citizen(this);
        c.setGraph(getGraph());
        citizens.add(c);
    }
    public boolean isCity(){
        return true;
    }
    
}
