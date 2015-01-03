

package javafxgame;

import javafx.scene.layout.Pane;


public class Crossroads extends Node{
    private static final int radius =16;
    Crossroads(int x,int y,Pane pane,Graph graph){
        super(x,y,pane,radius,graph);
    }
    public boolean isCity(){
        return false;
    }
}
