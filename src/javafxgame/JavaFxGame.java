
package javafxgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Przemyslaw
 */
public class JavaFxGame extends Application {
    
    @Override
    public void start(Stage primaryStage) {
       
        
        Pane root = new Pane();
        
        Scene scene = new Scene(root, 1200, 700);
        City c=new City(100,100,root);
        c.createCitizen();
//        Citizen citizen=new Citizen(c);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
