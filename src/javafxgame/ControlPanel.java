package javafxgame;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ControlPanel {

    Label entityInfoLabel, numOfCitiesAlive, numOfGuysAlive, sumOfPowerSources;
    Entity currentEntity;
    Button btnStop, btnSuspend, btnResume, btnCreateCitizen, btnCreateSuperhero, btnChangeDestination;
    VBox vBox;
    HBox guyButtons, cityButtons, changingDestinationButtons;
    Graph graph;
    ComboBox citiesComboBox;
    private long timeOfLastGuyCreation;
    static final double minEnergyToCreateAHero=80;
    public ControlPanel(Pane pane, Graph graph) {
        this.graph = graph;
        timeOfLastGuyCreation=graph.getStartTime();
        sumOfPowerSources = new Label();
        currentEntity = null;
        btnStop = new Button("Delete");
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentEntity != null) {
                    currentEntity.myStop();
                    entityInfoLabel.setText("click on an entity to see info");
                    btnResume.setDisable(true);
                    btnStop.setDisable(true);
                    btnSuspend.setDisable(true);
                }
            }
        });
        citiesComboBox = new ComboBox();

        btnChangeDestination = new Button("Change destination");
        btnSuspend = new Button("suspend");
        btnSuspend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentEntity != null) {
                    currentEntity.mySuspend();
                    btnSuspend.setDisable(true);

                    btnResume.setDisable(false);
                }
            }
        });
        btnResume = new Button("resume");
        btnResume.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentEntity != null) {
                    currentEntity.myResume();
                    btnSuspend.setDisable(false);

                    btnResume.setDisable(true);
                }
            }
        });
        btnCreateCitizen = new Button("create citizen");
        btnCreateCitizen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentEntity != null) {//if its a City
                    if (currentEntity instanceof City) {
                        timeOfLastGuyCreation=System.nanoTime();
                        ((City) currentEntity).createCitizen();//moze rzucac wyjatkami concurrent modification i trzeba lapac
                    }
                }
            }
        });
        btnCreateSuperhero = new Button("create superhero");
        btnCreateSuperhero.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentEntity != null) {//if its a Capital
                    if (currentEntity instanceof Capital){
                    ((Capital) currentEntity).createSuperhero();
                    timeOfLastGuyCreation=System.nanoTime();
                    displayEntity();
                    }

                }
            }
        });
        guyButtons = new HBox();
        guyButtons.getChildren().addAll(btnSuspend, btnResume, btnStop);
        cityButtons = new HBox();
        cityButtons.getChildren().addAll(btnCreateCitizen, btnCreateSuperhero);
        entityInfoLabel = new Label("click on an entity to see info");
        numOfCitiesAlive = new Label("num of cities alive" + graph.getNumOfCitiesAlive());
        setButtonsDisabled();
        changingDestinationButtons = new HBox();
        changingDestinationButtons.getChildren().addAll(citiesComboBox, btnChangeDestination);
        vBox = new VBox();
        vBox.getChildren().addAll(numOfCitiesAlive, entityInfoLabel, guyButtons, cityButtons, changingDestinationButtons);
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

    public void setCities() {
        for (Node it : graph.getNodes()) {
            if (it instanceof City) {
                citiesComboBox.getItems().add(it.getName());
            }
        }

        btnChangeDestination.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentEntity != null) {
                    if (citiesComboBox.getValue() != null) {
                        Node destination = null;
                        for (Node it : graph.getNodes()) {
                            if (it.getName().equals(citiesComboBox.getValue())) {
                                destination = it;
                                break;
                            }
                        }

                        if (destination != null) {
                            ((Guy) currentEntity).setDestination(destination);
                        }
                    }
                }
            }
        });
    }

    public void updateNumOfCitiesAlive(int num) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                numOfCitiesAlive.setText("Num of cities alive: " + num);
            }
        });
        if (num <= 0)//koniec gry
        {
            Platform.runLater(new Runnable() {
                XMLEncoder e;
                XMLDecoder d;
                VBox vBox, wyniki;
                
                String nazwaPlikuXML = Paths.get(System.getProperty("user.dir")).toString()+"/bestScores.xml";

                @Override
                public void run() {
                    Stage stage = new Stage();
                    long timeElapsed = System.nanoTime() - graph.getStartTime();
                    double seconds = ((double) timeElapsed) / ((double) 1e9);

                    Button btn = new Button();
                    btn.setText("dodaj");
                    wyniki = new VBox();
                    vBox = new VBox();
                    HBox hBox = new HBox();
                    TextField imie;
                    imie = new TextField();

                    hBox.getChildren().addAll(new Label("Imie"), imie, btn);
                    vBox.getChildren().addAll(hBox, wyniki);
                    StackPane root = new StackPane();
                    root.getChildren().add(vBox);
                    Scene scene = new Scene(root, 600, 350);
                    ArrayList<Wynik> listaWynikow = new ArrayList<>();
                    
                        odczytajXML(listaWynikow);
                        
                    
                    btn.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            listaWynikow.add(new Wynik(imie.getText(), 0, seconds));
                            Collections.sort(listaWynikow);

                            
                                zapiszXML(listaWynikow);
                                odczytajXML(listaWynikow);
                                btn.setDisable(true);
                           
                        }
                    });
                    stage.setTitle("Best 5 scores");
                    stage.setScene(scene);

                    stage.show();
                }

                private void odczytajXML(ArrayList<Wynik> listaWynikow)  {
        
        try {
            d = new XMLDecoder(
                    new BufferedInputStream(
                            new FileInputStream(nazwaPlikuXML)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      
                        wyniki.getChildren().clear();
                        listaWynikow.clear();
                         Wynik w;
                         try{
                        while(true){
                            w = (Wynik) d.readObject();                                   
                            listaWynikow.add(w);            
                        }
                         }
                        catch(ArrayIndexOutOfBoundsException e){
                                System.out.println("koniec wczytywania bro");
                                }
                         Collections.sort(listaWynikow);
                         int ile=0;
                         for(Wynik it: listaWynikow){
                             if(ile>=5) break;
                              wyniki.getChildren().add(new Label(it.toString()));
                              ile++;
                         }
        d.close();
    }

                private void zapiszXML(ArrayList<Wynik> listaWynikow)  {
                    try {
                        e = new XMLEncoder(
                                new BufferedOutputStream(
                                        new FileOutputStream(nazwaPlikuXML)));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    int i=0;
                    for (Wynik w : listaWynikow) {
                        if(i>=5) break;
                        e.writeObject(w);
                        i++;
                    }
                    e.close();
                }

            });

        }

    }

    public synchronized void displayEntity() {
        if (currentEntity != null) {
            displayEntity(currentEntity);
        }
    }

    public synchronized void displayEntity(Entity e) {
        currentEntity = e;
        boolean flag = true;
        if (currentEntity instanceof Guy) {
            if (((Guy) currentEntity).isStopped()) {
                setButtonsDisabled();
                flag = false;
            }
        }

        if (flag) {
            entityInfoLabel.setText(e.toString());
            boolean option = (e instanceof Citizen || e instanceof Superhero);

            btnStop.setDisable(!option);
            if(option && ((Guy)e).isSuspended())
            btnSuspend.setDisable(true);
            else btnSuspend.setDisable(!option);
            if(option&& ((Guy)e).isSuspended() )
            btnResume.setDisable(false);
            else  btnResume.setDisable(true);
            btnCreateCitizen.setDisable(true);
            btnCreateSuperhero.setDisable(true);
            double seconds=((double)(System.nanoTime()-timeOfLastGuyCreation))/1e9;
            if(seconds>8)
            if (e instanceof City) {
                if (!((City) e).isIsDefeated()) {
                    btnCreateCitizen.setDisable(false);
                }
            }
            if(seconds>15){
                if(e instanceof Capital){
                    //sum all PowerSources
                    double powerSourcesEnergy=0;
                    for(Node it:graph.getNodes()){
                        if(it instanceof City){
                            for(PowerSource p: ((City)it).getPowerSources())
                                powerSourcesEnergy+=p.getEnergy();
                        }
                    }
                    System.err.println("power sources ene is "+powerSourcesEnergy);
                    btnCreateSuperhero.setDisable(powerSourcesEnergy+seconds<minEnergyToCreateAHero && powerSourcesEnergy>10);
                }
            }
            
            btnChangeDestination.setDisable(!(e instanceof Citizen) && !(e instanceof Superhero));
            citiesComboBox.setDisable(!(e instanceof Citizen) && !(e instanceof Superhero));
        }
    }

}
