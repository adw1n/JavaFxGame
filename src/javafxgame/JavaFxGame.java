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
 *
 * @author Przemyslaw
 */
public class JavaFxGame extends Application {

    Graph graf;
    private static final int rightScreenEdge=850;
    private static final int downScreenEdge=700;
    @Override
    public void start(Stage primaryStage) {

        Pane root = new Pane();
        
        Scene scene = new Scene(root, 1200, getDownScreenEdge());
        primaryStage.setTitle("JavaFxGame powered by adwin_");
        primaryStage.setScene(scene);
        primaryStage.show();
        graf = new Graph(root);
        graf.initializeGraph();
//        City c1 = new City(50, 300, root, graf);
//        graf.addNode(c1);
//        graf.addNode(new Crossroads(200, 300, root, graf));
//        graf.addNode(new Crossroads(200, 100, root, graf));
//        graf.addNode(new Crossroads(200, 500, root, graf));
//        graf.addEdge(1, 2);
//        graf.addEdge(1, 3);
//        graf.addEdge(0, 1);
//        Node c2 = new City(350, 500, root, graf);
//        graf.addNode(c2);
//        graf.addEdge(3, 4);
//        graf.addNode(new City(350, 100, root, graf));
//        graf.addEdge(2, 5);
//        graf.addNode(new Crossroads(600, 300, root, graf));
//        graf.addEdge(6, 1);
//        graf.addNode(new Crossroads(600, 100, root, graf));
//        graf.addEdge(6, 7);
//        graf.addNode(new City(450, 100, root, graf));
//        graf.addEdge(7, 8);
//        graf.addNode(new City(750, 100, root, graf));
//        graf.addEdge(7, 9);
//        graf.addNode(new City(50, 500, root, graf));
//        graf.addEdge(3, 10); 
//        graf.addNode(new City(50, 100, root, graf));
//        graf.addEdge(2, 11); 
//        graf.addNode(new Crossroads(600, 500, root, graf));
//        graf.addEdge(6, 12); 
//        graf.addNode(new City(450, 500, root, graf));
//        graf.addEdge(12, 13); 
//        graf.addNode(new City(750, 500, root, graf));
//        graf.addEdge(12, 14); 
//        graf.addNode(new Capital(750, 300, root, graf));
//        graf.addEdge(6, 15); 
//        BadGuy b1=new BadGuy(1000, 100, 20, root, graf),b2;
//        b2=new BadGuy(1000, 1200, 700, root, graf);
//        b2=new BadGuy(1000, 700, 700, root, graf);
//        b2=new BadGuy(1000, 1100, 700, root, graf);
//        b1.fight(b2);
//        b2.fight(b1);
//        FightersAbility f=new FightersAbility(Ability.POWER, 10);
//        URL location = JavaFxGame.class.getProtectionDomain().getCodeSource().getLocation();
//        System.out.println(location.getFile());
//        System.out.println(System.getProperty("user.dir")+"src/javafxgame/femaleNames.txt");
//        InputStream imputStream;
//        imputStream=JavaFxGame.class.getResourceAsStream("femaleNames.txt").;
//        FileReader fileReader=new FileReader("feamleNames.txt");
        
        
//        NameGetter n=new NameGetter();
//        for(int i=0;i<200;i++){
//            System.out.println(n.getFemaleName()); 
//            System.out.println(n.getCrossroadsName());
//            System.out.println(n.getCityName());
//            System.out.println(n.getCapitalName());
//            System.out.println(n.getPowerSourceName());
//        }
//        Citizen c=new Citizen(c1);
//        c.go(s);
//        Citizen cc=new Citizen((City) c2);
//        s2=graf.findPathBetweenCities(c2, c1);
//        cc.go(s2);
//        cc.go(s);
//        c.go(s);
//        Circle cc=new Circle(200, 600, 30);
//                root.getChildren().add(cc);
//        Thread t=new Thread(){
//            @Override
//            public void run(){
//                for(int i=0;i<500;i++){
////                    try {
////                        sleep(100);
////                    } catch (InterruptedException ex) {
////                        Logger.getLogger(JavaFxGame.class.getName()).log(Level.SEVERE, null, ex);
////                    }
//                    try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException ex) {
//                                Logger.getLogger(JavaFxGame.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                    Thread h=new Thread(){
//                        public void run(){
//                           
//                            cc.setCenterX(cc.getCenterX()+1);
//                             
//                        }
//                    };
//                    runAndWait(h);
//                
//                }
//            }
//        };
//        t.setDaemon(true);
//        t.start();
//        for(Node a : s)
//            System.out.println(a.getCircle().getCenterX()+ " "+a.getCircle().getCenterY());

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
     * @autor:http://news.kynosarges.org/2014/05/01/simulating-platform-runandwait/
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
