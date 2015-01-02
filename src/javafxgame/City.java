

package javafxgame;

import java.util.ArrayList;
import javafx.scene.layout.Pane;


public class City extends Node{ //city na razie ma tylko wiekszy promien
    private static final int radius=32;
    ArrayList<Citizen> citizens;
    public City(int x, int y, Pane pane) {
        super(x, y, pane,radius);
        citizens=new ArrayList<>();
    }
    public void createCitizen(){
//        Citizen c=new Citizen(this);
        
        citizens.add(new Citizen(this));
    }
    
}
