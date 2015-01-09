package javafxgame;

import java.io.FileReader;
import java.io.InputStream;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *Sets up the game, declares screen size.
 * @author adwin_
 */
public class JavaFxGame extends Application {

    private Graph graph;
    private static final int rightScreenEdge=850;
    private static final int downScreenEdge=700;
    /**
     * Sets up the whole game in JavaFX, initializes the graph, which is responsible for the rest of the stuff.
     * @param primaryStage 
     */
    @Override
    public void start(Stage primaryStage) {

        Pane root = new Pane();
        
        Scene scene = new Scene(root, 1200, getDownScreenEdge());
        primaryStage.setTitle("JavaFxGame powered by adwin_");
        primaryStage.setScene(scene);
        primaryStage.show();
        graph = new Graph(root);
        graph.initializeGraph();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Runs the specified {@link Runnable} on the JavaFX application thread and
     * waits for completion.
     * @autor:http://news.kynosarges.org/2014/05/01/simulating-platform-runandwait/ (not me!!!)
     *
     * @param action the {@link Runnable} to run
     * @throws NullPointerException if {@code action} is {@code null}
     */
    public static void runAndWait(Runnable action) {
        if (action == null) {
            throw new NullPointerException("action");
        }

        // run synchronously on JavaFX thread
        if (Platform.isFxApplicationThread()) {
            action.run();
            return;
        }

        // queue on JavaFX thread and wait for completion
        final CountDownLatch doneLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                doneLatch.countDown();
            }
        });

        try {
            doneLatch.await();
        } catch (InterruptedException e) {
            // ignore exception
        }
    }

    /**
     * @return the rightScreenEdge
     */
    public static int getRightScreenEdge() {
        return rightScreenEdge;
    }

    /**
     * @return the downScreenEdge
     */
    public static int getDownScreenEdge() {
        return downScreenEdge;
    }

}
