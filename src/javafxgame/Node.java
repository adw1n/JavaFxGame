

package javafxgame;


import java.util.ArrayList;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.shape.Circle;


public abstract class Node {
    private int x,y;
    private Circle circle;
    private Pane pane;

    public Node(int x,int y,Pane pane,int radius){
//        edges = new ArrayList<>();
        this.x=x;
        this.y=y;
        circle=new Circle(x,y,radius);
        this.pane=pane;
        pane.getChildren().add(circle);
    }
    public Node(int x,int y,Pane pane,int radius,String color){
        this(x,y,pane,radius);
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
    public Pane getPane(){
        return pane;
    }
    
}
