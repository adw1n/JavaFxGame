package javafxgame;

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.shape.Circle;

public abstract class Guy extends Entity implements Runnable{ 
    private int hp;
    private Circle circle;
    Thread thrd;
    Node currentNode;
    private Graph graph;
    boolean zajety;
    private static final int radius=5;
    public Guy(int hp,double x,double y,Pane pane,Node currentNode) {
        zajety=false;
//        super(name);
        this.hp = hp;
        this.currentNode = currentNode;
        thrd=new Thread(this);
        thrd.setDaemon(true);
        thrd.start();
        circle=new Circle(x,y,radius);
        circle.setFill(valueOf("yellow"));
        pane.getChildren().add(circle);
    }
    @Override
    public void run(){
        System.out.println("nowy watek hurra!" + toString());
        //goToCity();
    }
    /** @return true if you have safely arrived to the destination
     * to be implemented
     */
    void go(Stack<Node> path){
        zajety=true;
            //jezeli jestem w srodku miasta
        currentNode=path.get(0);
        path.remove(0);
        Thread renderer;
        renderer = new Thread(){
            
            @Override
            public void run(){
                for(Node it: path){
                    
                            
//                    final Circle previous ;
//                    previous=circle;
                    
                        
                    
                    
                    
                    boolean spieprzone=false;
//                do
                   
                      Thread t;
                            System.out.println("zaczynam runandwait");
                            if(circle.getCenterY()==it.getCircle().getCenterY() &&circle.getCenterX()<it.getCircle().getCenterX() ){//go right
                                System.out.println("go right bro!" + circle);
                                t=new Thread(){
                                    public void run(){
                                         circle.setCenterY(circle.getCenterY()+Graph.getRoadWidth()/2);

                                    }
                                };
                                JavaFxGame.runAndWait(t);
                                       
                                
                                int ile=Math.abs((int) (circle.getCenterX()-it.getCircle().getCenterX()));
                                System.out.println("ile to "+ile);
                                for(int i=0;i<ile;i++){
                                    try {
                                        Thread.sleep(50);
                                    }
                                    catch (InterruptedException ex) {
                                        Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                        //System.out.println("sleepuje");
                                        t=new Thread(){
                                            public void run(){
                                                circle.setCenterX(circle.getCenterX()+1);
//                                                System.out.println("uaktualnilem pozycje "+circle);
                                            }
                                        };
                                        JavaFxGame.runAndWait(t);
                                                
                                           
//                                    } 
                                }
                                t=new Thread(){
                                    public void run(){
                                        circle.setCenterY(circle.getCenterY()-Graph.getRoadWidth()/2);

                                    }
                                };
                                JavaFxGame.runAndWait(t);
                                //System.out.println("X : "+circle.getCenterX()+ " Y: "+circle.getCenterY());
                            }
                            else if(circle.getCenterY()==it.getCircle().getCenterY() &&circle.getCenterX()>it.getCircle().getCenterX() ){//go left
                                t=new Thread(){
                                    public void run(){
                                        circle.setCenterY(circle.getCenterY()-Graph.getRoadWidth()/2);

                                    }
                                };
                                JavaFxGame.runAndWait(t);
                                int ile=Math.abs((int) (circle.getCenterX()-it.getCircle().getCenterX()));
                                System.out.println("left ile: "+ile);
                                for(int i=0;i<ile;i++)
                                {
                                    try {
                                        Thread.sleep(50);
                                        } catch (InterruptedException ex) {
                                        Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    t=new Thread(){
                                    public void run(){
                                                circle.setCenterX(circle.getCenterX()-1);

                                    }
                                };
                                JavaFxGame.runAndWait(t);
                                           
//                                    
                                } 
                                t=new Thread(){
                                    public void run(){
                                        circle.setCenterY(circle.getCenterY()+Graph.getRoadWidth()/2);
                                                System.out.println("w lewo circle na koncu: "+circle);

                                    }
                                };
                                JavaFxGame.runAndWait(t);
                            }
                            else if(circle.getCenterY()<it.getCircle().getCenterY() &&circle.getCenterX()==it.getCircle().getCenterX() ){//go down
                                t=new Thread(){
                                    public void run(){
                                        circle.setCenterX(circle.getCenterX()-Graph.getRoadWidth()/2);

                                    }
                                };
                                JavaFxGame.runAndWait(t);
                                int ile = (int) Math.abs(circle.getCenterY()-it.getCircle().getCenterY());
                                                                System.out.println("w dol ile: "+ile);

                                for(int i=0;i<ile;i++)
                                {
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                        t=new Thread(){
                                    public void run(){
                                                circle.setCenterY(circle.getCenterY()+1);

                                    }

                                };
                                                                                                        JavaFxGame.runAndWait(t);

                                }
                                t=new Thread(){
                                    public void run(){
                                        circle.setCenterX(circle.getCenterX()+Graph.getRoadWidth()/2);

                                    }
                                };
                                JavaFxGame.runAndWait(t);
                                        //System.out.println("X : "+circle.getCenterX()+ " Y: "+circle.getCenterY());
//
                                 
                            }
                            else if(circle.getCenterY()>it.getCircle().getCenterY() &&circle.getCenterX()==it.getCircle().getCenterX() ){//go up
                             t=new Thread(){
                                    public void run(){
                                        circle.setCenterX(circle.getCenterX()+Graph.getRoadWidth()/2);

                                    }
                                };
                                JavaFxGame.runAndWait(t);
                                int ile=(int) Math.abs(circle.getCenterY()-it.getCircle().getCenterY());
                                for(int i=0;i<ile;i++) {
                                    try {
                                        Thread.sleep(50);
                                        } catch (InterruptedException ex) {
                                        Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    t=new Thread(){
                                    public void run(){
                                                circle.setCenterY(circle.getCenterY()-1);

                                    }
                                };
                                JavaFxGame.runAndWait(t);
//                                    
                                }
                                t=new Thread(){
                                    public void run(){
                                        circle.setCenterX(circle.getCenterX()-Graph.getRoadWidth()/2);

                                    }
                                };
                                JavaFxGame.runAndWait(t);
                            }
                            else System.out.println("cos spieprzylem!");
//                            else spieprzone=true;
                
//                       while(spieprzone);
                            
                            
                        
//                        if(it!=path.get(0))
//                    while( circle.getCenterX()!=it.getCircle().getCenterX() || circle.getCenterY()!=it.getCircle().getCenterY()) {System.out.println("zablokowany!");};
                   
//                                        prev=it.getCircle();
                  
                    currentNode=path.lastElement();
                    System.out.println("skonczylem!");
                    
                }                                zajety=false;
};

//                while (true) {
//                    try {
//                        Thread.sleep(40);
//                    } catch (InterruptedException ex) {
//                    }
//
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                           
//                        }
//                    });
//                }
//            }
        };
//        
        renderer.setDaemon(true);
        renderer.start();
        
        
        

    }

    /**
     * @return the hp
     */
    public int getHp() {
        return hp;
    }

    /**
     * @param hp the hp to set
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

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
