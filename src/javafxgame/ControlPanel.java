

package javafxgame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class ControlPanel {
    Label label;
    Entity currentEntity;
    Button btnStop,btnSuspend,btnResume,btnCreateCitizen,btnCreateSuperhero;
    VBox vBox;
    HBox guyButtons,cityButtons;
    public ControlPanel(Pane pane) {
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
                        
                }
            }            
        });
        guyButtons=new HBox();
        guyButtons.getChildren().addAll(btnSuspend,btnResume,btnStop);
        cityButtons=new HBox();
        cityButtons.getChildren().addAll(btnCreateCitizen,btnCreateSuperhero);
        label=new Label("click on an entity to see info");
        btnResume.setDisable(true);
        btnStop.setDisable(true);
        btnSuspend.setDisable(true);
        vBox=new VBox();
        vBox.getChildren().addAll(label,guyButtons,cityButtons);
        vBox.setLayoutX(900);
        vBox.setLayoutY(200);
        pane.getChildren().add(vBox);
    }
    public synchronized void displayEntity(Entity e){
        currentEntity=e;
        label.setText(e.toString());
        boolean option=e.isStoppable();
       
        btnStop.setDisable(!option);
        btnSuspend.setDisable(!option);
        btnResume.setDisable(!option);
        btnCreateCitizen.setDisable(option);
        btnCreateSuperhero.setDisable(option);
        if(option){
             Citizen g=(Citizen) e;
            btnSuspend.setDisable(g.isSuspended());
        btnResume.setDisable(!g.isSuspended());
        }
    }
    
}
