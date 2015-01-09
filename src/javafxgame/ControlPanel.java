

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
    Label entityInfoLabel,numOfCitiesAlive,numOfGuysAlive;
    Entity currentEntity;
    Button btnStop,btnSuspend,btnResume,btnCreateCitizen,btnCreateSuperhero,btnChangeDestination;
    VBox vBox;
    HBox guyButtons,cityButtons,changingDestinationButtons;
    Graph graph;
    ComboBox citiesComboBox;
    
    public ControlPanel(Pane pane,Graph graph) {
        this.graph=graph;
        
        currentEntity=null;
        btnStop=new Button("Delete");
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentEntity!=null){
                    currentEntity.myStop();
                    entityInfoLabel.setText("click on an entity to see info");
                    btnResume.setDisable(true);
        btnStop.setDisable(true);
        btnSuspend.setDisable(true);
                }
            }            
        });
        citiesComboBox=new ComboBox();
        
        btnChangeDestination=new Button("Change destination");
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
        entityInfoLabel=new Label("click on an entity to see info");
        numOfCitiesAlive=new Label("num of cities alive"+graph.getNumOfCitiesAlive());
        setButtonsDisabled();
        changingDestinationButtons=new HBox();
        changingDestinationButtons.getChildren().addAll(citiesComboBox,btnChangeDestination);
        vBox=new VBox();
        vBox.getChildren().addAll(numOfCitiesAlive,entityInfoLabel,guyButtons,cityButtons,changingDestinationButtons);
        vBox.setLayoutX(900);
        vBox.setLayoutY(20);
        pane.getChildren().add(vBox);
    }

    private void setButtonsDisabled() {
        btnResume.setDisable(true);
        btnStop.setDisable(true);
        btnSuspend.setDisable(true);
        btnChangeDestination.setDisable(true);
        btnCreateCitizen.setDisable(true);
        btnCreateSuperhero.setDisable(true);
        entityInfoLabel.setText("click on an entity to see info");
    }
    
    public void setCities(){
        for(Node it: graph.getNodes()){
            if(it instanceof City)
                citiesComboBox.getItems().add(it.getName());
        }
        
        btnChangeDestination.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentEntity!=null){
                    if(citiesComboBox.getValue()!=null){
                        Node destination=null;
                        for(Node it: graph.getNodes())
                            if(it.getName().equals(citiesComboBox.getValue()))
                            {
                                destination=it;
                                break;
                            }
                        
                        if(destination!=null)
                    ((Guy)currentEntity).setDestination(destination);
                    }
                }
            }            
        });
    }
    public void updateNumOfCitiesAlive(int num){
         Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                           numOfCitiesAlive.setText("Num of cities alive: "+num);
                        }
                    });
        
    }
    public synchronized void displayEntity(){
        if(currentEntity!=null){
        displayEntity(currentEntity);
        }
    }
    public synchronized void displayEntity(Entity e){
        currentEntity=e;
        boolean flag=true;
        if(currentEntity instanceof Guy)
            if(((Guy) currentEntity).isStopped())
            {
                setButtonsDisabled();
                flag=false;
                }
            
            if(flag){
        entityInfoLabel.setText(e.toString());
        boolean option=(e instanceof Citizen || e instanceof Superhero);
       
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
            }
    }
    
}
