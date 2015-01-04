

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
    Button btnStop,btnSuspend,btnResume;
    VBox vBox;
    HBox buttons;
    public ControlPanel(Pane pane) {
        btnStop=new Button("stop");
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentEntity!=null)
                    currentEntity.myStop();
            }
        });
        btnSuspend=new Button("suspend");
        btnSuspend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentEntity!=null)
                    currentEntity.mySuspend();
            }
        });
        btnResume=new Button("resume");
        btnResume.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentEntity!=null)
                    currentEntity.myResume();
            }
        });
        buttons=new HBox();
        buttons.getChildren().addAll(btnSuspend,btnResume,btnStop);
        label=new Label();
        vBox=new VBox();
        vBox.getChildren().addAll(label,buttons);
        vBox.setLayoutX(900);
        vBox.setLayoutY(200);
        pane.getChildren().add(vBox);
    }
    public synchronized void displayEntity(Entity e){
        currentEntity=e;
        label.setText(e.toString());
    }
    
}
