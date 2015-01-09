package javafxgame;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.shape.Circle;
/**
 * Reperesents a vertex in a graph.
 * @author adwin_
 */
public abstract class Node extends Entity {

    private int x, y;
    private Circle circle;
    private Pane pane;
    private int nodeNumber;
    private Graph graph;
    private boolean isDefeated;
    /**
     * Creates a Node.
     * @param x
     * @param y
     * @param pane
     * @param radius
     * @param graph
     * @param name 
     */
    public Node(int x, int y, Pane pane, int radius, Graph graph,String name) {
        super(name);
        this.nodeNumber = nodeNumber;
        this.x = x;
        this.y = y;
        circle = new Circle(x, y, radius);
        this.pane = pane;
        setGraph(graph);
        isDefeated=false;
        pane.getChildren().add(circle);
        getCircle().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getGraph().displayEntity(thisEntity);
            }
        });
    }
    /**
     * Creates a node.
     * @param x
     * @param y
     * @param pane
     * @param radius
     * @param color
     * @param graph
     * @param name 
     */
    public Node(int x, int y, Pane pane, int radius, String color, Graph graph,String name) {
        this(x, y, pane, radius, graph,name);
        circle.setFill(valueOf(color));
    }


    /**
     * @param t
     * @return the circle
     */
    public Circle getCircle() {
        return circle;
    }
    /**
     * 
     * @return returns the current pane that node is drawn on
     */
    public Pane getPane() {
        return pane;
    }
    /**
     * 
     * @return returns the unique number of the node associated with it in the graph
     */
    public int getNodeNumber() {
        return nodeNumber;
    }
    /**
     * 
     * @param number sets number
     */
    public void setNodeNumber(int number) {
        nodeNumber = number;
    }
    /**
     * Checks whether it is a city, equivalent to this instanceof City 
     * @return 
     */
    public abstract boolean isCity();

    /**
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @param graph the graph to set
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * @return the isDefeated
     */
    public boolean isIsDefeated() {
        return isDefeated;
    }

    /**
     * @param isDefeated the isDefeated to set
     */
    public void setIsDefeated(boolean isDefeated) {
        this.isDefeated = isDefeated;
        JavaFxGame.runAndWait(new Thread(){
            @Override
            public void run(){
                circle.setFill(valueOf("brown"));
            }
        }
        );
    }

}
