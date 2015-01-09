

package javafxgame;

import java.util.Random;
import javafx.scene.layout.Pane;


public class Capital extends City{
        private static final int radius = 40;
       private final Random randomGenerator;
    public Capital(int x, int y, Pane pane, Graph graph) {
        super(x, y, pane, graph,radius,graph.getNameGetter().getCapitalName());
//        for(int i=0;i<2;i++)
//        createSuperhero();
        randomGenerator=new Random();
    }
    public Superhero createSuperhero(){
       
//        Citizen c=new Citizen(this);
        Superhero c = new Superhero(randomGenerator.nextInt(BadGuy.getMaxHP()), this, getPane(), getGraph());
        
//        citizens.add(c);
        getGraph().addGuy(c);
    return c;
    }
}
