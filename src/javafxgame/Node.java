package javafxgame;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.shape.Circle;

public abstract class Node {

    private int x, y;
    private Circle circle;
    private Pane pane;
    private int nodeNumber;
    private Graph graph;

    public Node(int x, int y, Pane pane, int radius, Graph graph) {
//        edges = new ArrayList<>();
        this.nodeNumber = nodeNumber;
        this.x = x;
        this.y = y;
        circle = new Circle(x, y, radius);
        this.pane = pane;
        setGraph(graph);
        pane.getChildren().add(circle);
    }

    public Node(int x, int y, Pane pane, int radius, String color, Graph graph) {
        this(x, y, pane, radius, graph);
        circle.setFill(valueOf(color));
    }
//    void addEdge(int koniec){
//        edges.add(koniec);
//    }

    /**
     * @param t
     * @return the circle
     */
    public Circle getCircle() {
        return circle;
    }

    public Pane getPane() {
        return pane;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int number) {
        nodeNumber = number;
    }

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

}
