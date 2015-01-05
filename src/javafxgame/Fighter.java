

package javafxgame;

import javafx.scene.layout.Pane;


public abstract class Fighter extends Guy{

    public Fighter(int hp, double x, double y, Pane pane, Node currentNode) {
        super(hp, x, y, pane, currentNode);
    }

}
