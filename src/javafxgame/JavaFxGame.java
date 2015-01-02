
package javafxgame;

import java.util.Stack;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Przemyslaw
 */
public class JavaFxGame extends Application {
    Graph graf;
    @Override
    public void start(Stage primaryStage) {
       
        
        Pane root = new Pane();
        
        Scene scene = new Scene(root, 1200, 700);
//        City c=new City(100,100,root);
//        c.createCitizen();
//        Citizen citizen=new Citizen(c);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        graf = new Graph(root);
        City c1=new City(50,300,root);
        graf.addNode(c1);
        graf.addNode(new Crossroads(200,300,root));
        graf.addNode(new Crossroads(200,100,root));
        graf.addNode(new Crossroads(200,500,root));
        graf.addEdge(1,2);
        graf.addEdge(1,3);
        graf.addEdge(0, 1);
        Node c2=new City(350,500,root);
        graf.addNode(c2);
        graf.addEdge(3, 4);
        graf.addNode(new City(350,100,root));
        graf.addEdge(2, 5);
        graf.addNode(new Crossroads(600,300,root));
        graf.addEdge(6, 1);
        graf.addNode(new Crossroads(600,100,root));
        graf.addEdge(6, 7);
        graf.addNode(new City(450,100,root));
        graf.addEdge(7, 8);
        Stack<Node> s=graf.findPathBetweenCities(c1,c2),s2;
        Citizen c=new Citizen(c1);
        c.go(s);
        Citizen cc=new Citizen((City) c2);
        s2=graf.findPathBetweenCities(c2, c1);
        cc.go(s2);
//        cc.go(s);
//        c.go(s);
        for(Node a : s)
            System.out.println(a.getCircle().getCenterX()+ " "+a.getCircle().getCenterY());
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
