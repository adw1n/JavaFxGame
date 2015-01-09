

package javafxgame;

import java.util.Random;
import javafx.scene.layout.Pane;

/**
 * Capita, biggest and most important City. The only City that can create legendary Superheroes.
 * @author adwin_
 */
public class Capital extends City{
        private static final int radius = 40;
       private final Random randomGenerator;
       /**
        * Creates and draws a Capital.
        * @param x x coordinate of the center of the Capital
        * @param y y coordinate of the center of the Capital
        * @param pane pane that you want to draw the Capital on
        * @param graph  the graph that the Capital belongs to
        */
    public Capital(int x, int y, Pane pane, Graph graph) {
        super(x, y, pane, graph,radius,graph.getNameGetter().getCapitalName());
        randomGenerator=new Random();
    }
    /**
     * Creates one Superhero.
     * @return returns reference to the Superhero just created
     */
    public Superhero createSuperhero(){
        Superhero c = new Superhero(randomGenerator.nextInt(BadGuy.getMaxHP()), this, getPane(), getGraph());
        c.startThread();
        getGraph().addGuy(c);
    return c;
    }
}
