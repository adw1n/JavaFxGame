

package javafxgame;

import javafx.scene.layout.Pane;


public class BadGuy extends Fighter{
    Graph graph;
    public BadGuy(int hp, double x, double y, Pane pane, Graph graph) {
        super(hp, x, y, pane, null);
        this.graph=graph;
    }
    @Override
    public void run(){
        //find closest city to go to
        Node closest;
        double distance,minDistance=1000000;
        for(Node it: graph.getNodes()){
            if(it.isCity() && !it.isIsDefeated()){
                distance=(getCircle().getCenterX()-it.getCircle().getCenterX())*(getCircle().getCenterX()-it.getCircle().getCenterX())
                        +(getCircle().getCenterY()-it.getCircle().getCenterY())*(getCircle().getCenterY()-it.getCircle().getCenterY());
                if(distance<minDistance){
                    minDistance=distance;
                    closest=it;
                }
            }
        }
        //go to the closest
        
    }

}
