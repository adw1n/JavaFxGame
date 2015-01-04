

package javafxgame;

import javafx.scene.layout.Pane;


public class Capital extends City{
        private static final int radius = 40;

    public Capital(int x, int y, Pane pane, Graph graph) {
        super(x, y, pane, graph,radius);
    }

}
