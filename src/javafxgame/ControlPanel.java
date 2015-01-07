

package javafxgame;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class ControlPanel {
    Label label,numOfCitiesAlive;
    Entity currentEntity;
    Button btnStop,btnSuspend,btnResume,btnCreateCitizen,btnCreateSuperhero,btnChangeDestination;
    VBox vBox;
    HBox guyButtons,cityButtons,changingDestinationButtons;
    Graph graph;
    ComboBox citiesComboBox;
    
    public ControlPanel(Pane pane,Graph graph) {
        this.graph=graph;
        btnStop=new Button("Delete");
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentEntity!=null){
                    currentEntity.myStop();
                    label.setText("click on an entity to see info");
                    btnResume.setDisable(true);
        btnStop.setDisable(true);
        btnSuspend.setDisable(true);
                }
            }            
        });
        citiesComboBox=new ComboBox();
        citiesComboBox.getItems().addAll("City 1",
                "City 2","City 3","City 4","City 5","City 6","City 7","City 8","City 9","Capital");
        btnChangeDestination=new Button("Change destination");
        btnChangeDestination.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentEntity!=null){
                    if(citiesComboBox.getValue()!=null){
                        Node destination=null;
                        if(citiesComboBox.getValue().equals("Capital")){
                            for(Node it: graph.getNodes())
                                 if(it instanceof Capital)
                            destination=it;
                        }
                        else{
                            for(int i=1;i<10;i++)
                            if(citiesComboBox.getValue().equals("City "+i)){
                             
                                int numOfCities=0;
                                for(Node it: graph.getNodes()){
                                    if((it instanceof City) && !(it instanceof Capital)
                                            ){
                                        numOfCities++;
                                        if(numOfCities==i)
                                        {
                                            destination=it;
                                            System.err.println("City "+i+" ma wsp: "+destination.getCircle());
                                            break;
                                        }
                                        
                                    }
                                }
                            }
                        }
                        if(destination!=null)
                    ((Guy)currentEntity).setDestination(destination);
                    }
                }
            }            
        });
        btnSuspend=new Button("suspend");
        btnSuspend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentEntity!=null){
                    currentEntity.mySuspend();
                btnSuspend.setDisable(true);
                        
                btnResume.setDisable(false);
                }
            }
        });
        btnResume=new Button("resume");
        btnResume.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentEntity!=null){
                    currentEntity.myResume();
                    btnSuspend.setDisable(false);
                        
                btnResume.setDisable(true);
                }
            }
        });
        btnCreateCitizen=new Button("create citizen");
        btnCreateCitizen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentEntity!=null){//if its a City
                    if(currentEntity instanceof City)
                        ((City)currentEntity).createCitizen();//moze rzucac wyjatkami concurrent modification i trzeba lapac
                }
            }            
        });
        btnCreateSuperhero=new Button("create superhero");
        btnCreateSuperhero.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentEntity!=null){//if its a Capital
                    if(currentEntity instanceof Capital);
                    ((Capital)currentEntity).createSuperhero();
                        
                }
            }            
        });
        guyButtons=new HBox();
        guyButtons.getChildren().addAll(btnSuspend,btnResume,btnStop);
        cityButtons=new HBox();
        cityButtons.getChildren().addAll(btnCreateCitizen,btnCreateSuperhero);
        label=new Label("click on an entity to see info");
        numOfCitiesAlive=new Label("num of cities alive"+graph.getNumOfCitiesAlive());
        btnResume.setDisable(true);
        btnStop.setDisable(true);
        btnSuspend.setDisable(true);
        changingDestinationButtons=new HBox();
        changingDestinationButtons.getChildren().addAll(citiesComboBox,btnChangeDestination);
        vBox=new VBox();
        vBox.getChildren().addAll(numOfCitiesAlive,label,guyButtons,cityButtons,changingDestinationButtons);
        vBox.setLayoutX(900);
        vBox.setLayoutY(200);
        pane.getChildren().add(vBox);
    }
    public void updateNumOfCitiesAlive(int num){
         Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                           numOfCitiesAlive.setText("Num of cities alive: "+num);
                        }
                    });
        
    }
    public synchronized void displayEntity(Entity e){
        currentEntity=e;
        label.setText(e.toString());
        boolean option=e.isStoppable();
       
        btnStop.setDisable(!option);
        btnSuspend.setDisable(!option);
        btnResume.setDisable(!option);
        btnCreateCitizen.setDisable(true);
        if(e instanceof City)
            if(!((City)e).isIsDefeated())       
            btnCreateCitizen.setDisable(false);
        btnCreateSuperhero.setDisable(!(e instanceof Capital));
        btnChangeDestination.setDisable(!(e instanceof Citizen) && !(e instanceof Superhero));
        citiesComboBox.setDisable(!(e instanceof Citizen) && !(e instanceof Superhero));
        if(option){
             Citizen g=(Citizen) e;
            btnSuspend.setDisable(g.isSuspended());
         btnResume.setDisable(!g.isSuspended());
        }
    }
    
}
