package javafxgame;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.shape.Circle;

public abstract class Node extends Entity {

    private int x, y;
    private Circle circle;
    private Pane pane;
    private int nodeNumber;
    private Graph graph;
    private boolean isDefeated;
    public Node(int x, int y, Pane pane, int radius, Graph graph,String name) {
        super(name);
//        edges = new ArrayList<>();
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
                System.out.println("kliknales mnie bro"+getCircle());
                //myStop();
                getGraph().displayEntity(thisEntity);
//                if(!suspended) mySuspend();
//                else {myResume();System.out.println("wznowilem bro!");}
//                setRunning(false);
//                thrd.stop();
            }
        });
    }

    public Node(int x, int y, Pane pane, int radius, String color, Graph graph,String name) {
        this(x, y, pane, radius, graph,name);
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
            public void run(){
                circle.setFill(valueOf("brown"));
            }
        }
        );
    }

}
