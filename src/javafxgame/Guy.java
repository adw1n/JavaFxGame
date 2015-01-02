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
    private static final int radius=5;
    public Guy(int hp,double x,double y,Pane pane) {
//        super(name);
        this.hp = hp;
//        this.currentCity = currentCity;
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
            //jezeli jestem w srodku miasta
        currentNode=path.get(0);
        path.remove(0);
        Thread renderer = new Thread(){
        
            @Override
            public void run(){
                Circle previous=circle;
                for(Node it: path){
                    
                    
//            while(circle.getCenterX()!=it.getCircle().getCenterX() || circle.getCenterY()!=it.getCircle().getCenterY()){
//                try {
//                    //                Platform.runLater(new Runnable() {
////                        @Override
////                        public void run() {
//                    thrd.sleep(10);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
//                }
                
                       if(circle.getCenterY()==it.getCircle().getCenterY() &&circle.getCenterX()<it.getCircle().getCenterX() ){//go right
                            Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                           circle.setCenterY(circle.getCenterY()+Graph.getRoadWidth()/2);}});
                            
                            int ile=Math.abs((int) (previous.getCenterX()-it.getCircle().getCenterX()));
                           for(int i=0;i<ile;i++){
                               try {
                                   Thread.sleep(50);
                                   System.out.println("sleepuje");
                                   Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                                   circle.setCenterX(circle.getCenterX()+1);
                        }});
                               } catch (InterruptedException ex) {
                                   Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                               }
                           }
                            Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            circle.setCenterY(circle.getCenterY()-Graph.getRoadWidth()/2);}});
                            System.out.println("X : "+circle.getCenterX()+ " Y: "+circle.getCenterY());
                       }
                            else if(circle.getCenterY()==it.getCircle().getCenterY() &&circle.getCenterX()>it.getCircle().getCenterX() ){//go left
                                 Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                                circle.setCenterY(circle.getCenterY()-Graph.getRoadWidth()/2);}});
                                 int ile=Math.abs((int) (previous.getCenterX()-it.getCircle().getCenterX()));
                                 for(int i=0;i<ile;i++)
                           {
                                    try {
                                        Thread.sleep(50);
                                         Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                                        circle.setCenterX(circle.getCenterX()-1);
                        }});
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                           } Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            circle.setCenterY(circle.getCenterY()+Graph.getRoadWidth()/2);}});
                            }
                            else if(circle.getCenterY()<it.getCircle().getCenterY() &&circle.getCenterX()==it.getCircle().getCenterX() ){//go down
                                circle.setCenterX(circle.getCenterX()-Graph.getRoadWidth()/2);
                                int ile = (int) Math.abs(previous.getCenterY()-it.getCircle().getCenterY());
                                for(int i=0;i<ile;i++)
                           {
                                    try {
                                        Thread.sleep(50);
                                         Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                                        circle.setCenterY(circle.getCenterY()+1);}});
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                           }
                            Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            circle.setCenterX(circle.getCenterX()+Graph.getRoadWidth()/2);
                        System.out.println("X : "+circle.getCenterX()+ " Y: "+circle.getCenterY());
//                            
                        }});
                            }
                            else if(circle.getCenterY()>it.getCircle().getCenterY() &&circle.getCenterX()==it.getCircle().getCenterX() ){//go up
                                 Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                               circle.setCenterX(circle.getCenterX()+Graph.getRoadWidth()/2);}});
                                 int ile=(int) Math.abs(previous.getCenterY()-it.getCircle().getCenterY());
                          for(int i=0;i<ile;i++) {
                                   try {
                                       Thread.sleep(50);
                                        Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                                       circle.setCenterY(circle.getCenterY()-1);}});
                                   } catch (InterruptedException ex) {
                                       Logger.getLogger(Guy.class.getName()).log(Level.SEVERE, null, ex);
                                   }
                           }
                            Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            circle.setCenterX(circle.getCenterX()-Graph.getRoadWidth()/2);}});
                            }
                  
                     
//            if(circle.getCenterY()==it.getCircle().getCenterY() &&circle.getCenterX()<it.getCircle().getCenterX() )
//                            circle.setCenterX(circle.getCenterX()+1);
//                            else if(circle.getCenterY()==it.getCircle().getCenterY() &&circle.getCenterX()>it.getCircle().getCenterX() )
//                            circle.setCenterX(circle.getCenterX()-1);
//                            else if(circle.getCenterY()<it.getCircle().getCenterY() &&circle.getCenterX()==it.getCircle().getCenterX() )
//                            circle.setCenterY(circle.getCenterY()+1);
//                            else if(circle.getCenterY()>it.getCircle().getCenterY() &&circle.getCenterX()==it.getCircle().getCenterX() )
//                           circle.setCenterY(circle.getCenterY()-1);
               
                    
//                        }
//                });
                       previous=it.getCircle();
                       if(it!=path.get(0))
                    while( circle.getCenterX()!=it.getCircle().getCenterX() || circle.getCenterY()!=it.getCircle().getCenterY()) {};
        }
                currentNode=path.lastElement();
        }
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
    
}
