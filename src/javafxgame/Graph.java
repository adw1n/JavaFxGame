package javafxgame;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.shape.Line;

public class Graph {

    private ArrayList<Node> nodes;
    private int numOfCitizensAlive;
    private long startTime;
    ArrayList<Line> roads;
    private NameGetter nameGetter;
    private ArrayList<Guy> guys;
    private ArrayList<BadGuy> badGuys;
    ArrayList<Integer> citiesNumbers;//potem trzeba bedzie usuwac
    private Pane pane;
    private boolean adj[][];
    private boolean visited[];//for dfs
    private static final int roadWidth = 16;
    private ControlPanel controlPanel;
    private int numOfCitiesAlive;
    private BadGuysCreator badGuysCreator;
    public Graph(Pane pane) {
        numOfCitiesAlive=0;
        numOfCitizensAlive=0;
        nameGetter=new NameGetter();
//        stosProcesow=new Stack<>();
        this.pane = pane;
        nodes = new ArrayList<>();
        roads = new ArrayList<>();
        
        guys=new ArrayList<>();
        adj = new boolean[100][100];
        for (boolean[] adj1 : adj) {
            for (boolean a : adj1) {
                a = false;
            }
        }
        startTime=System.nanoTime();
        badGuys=new ArrayList<>();
//        initializeGraph(pane);
        
    }

    public void initializeGraph() {
        controlPanel=new ControlPanel(pane,this);
        addNode(new City(50, 300, getPane(), this));
        addNode(new Crossroads(200, 300, getPane(), this));
        addNode(new Crossroads(200, 100, getPane(), this));
        addNode(new Crossroads(200, 500, getPane(), this));
        addEdge(1, 2);
        addEdge(1, 3);
        addEdge(0, 1);
        addNode(new City(350, 500, getPane(), this));
        addEdge(3, 4);
        addNode(new City(350, 100, getPane(), this));
        addEdge(2, 5);
        addNode(new Crossroads(600, 300, getPane(), this));
        addEdge(6, 1);
        addNode(new Crossroads(600, 100, getPane(), this));
        addEdge(6, 7);
        addNode(new City(450, 100, getPane(), this));
        addEdge(7, 8);
        addNode(new City(750, 100, getPane(), this));
        addEdge(7, 9);
        addNode(new City(50, 500, getPane(), this));
        addEdge(3, 10);
        addNode(new City(50, 100, getPane(), this));
        addEdge(2, 11);
        addNode(new Crossroads(600, 500, getPane(), this));
        addEdge(6, 12);
        addNode(new City(450, 500, getPane(), this));
        addEdge(12, 13);
        addNode(new City(750, 500, getPane(), this));
        addEdge(12, 14);
        addNode(new Capital(750, 300, getPane(), this));
        addEdge(6, 15);
        Random randomGenerator = new Random();
        for(Node it: getNodes())
        {
            if(it instanceof Capital)
                ((Capital)it).createSuperhero();
            else if(it instanceof City){
                for(int numOfCitizen=0;numOfCitizen<randomGenerator.nextInt(3);numOfCitizen++)
                    ((City)it).createCitizen();
            }
                
        }
        
        controlPanel.setCities();
        badGuysCreator=new BadGuysCreator(this);
    }
    public synchronized void displayEntity(Entity e){
        getControlPanel().displayEntity(e);
    }
    public synchronized  void displayEntity(){
        getControlPanel().displayEntity();
    }
    public synchronized void addGuy(Guy guy){
        getGuys().add(guy);
    }
    public synchronized void addBadGuy(BadGuy guy){
        try{getBadGuys().add(guy);}
        catch(ConcurrentModificationException e){};//not gonna happen
    }
    public void addNode(Node node) {
        if(node instanceof City) increaseNumOfCitiesAlive();
        //if(node.isCity()) citiesNumbers.add(nodes.size());
        node.setNodeNumber(getNodes().size());
        node.setGraph(this);
        getNodes().add(node);

    }
    
    private synchronized void increaseNumOfCitiesAlive(){
        numOfCitiesAlive++;
        controlPanel.updateNumOfCitiesAlive(numOfCitiesAlive);
    }
    public synchronized void decreaseNumOfCitiesAlive(){
        numOfCitiesAlive--;
        numOfCitiesAlive=Math.max(0,numOfCitiesAlive);
        controlPanel.updateNumOfCitiesAlive(numOfCitiesAlive);
//        Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//if(numOfCitiesAlive==8) pane.getChildren().clear();                        }
//                    });
        
        
    }
    
    public synchronized int getNumOfCitiesAlive(){
        return numOfCitiesAlive;
    };
//tu moze byc blad bo na samym poczatku tworze miasto, ale jeszcze nie dodalem do nodes, miasto tworzy mieszkanca a on wywoluje ta fun i moze byc pusto
    //moze byc tak ze nie ma juz niepokonnanego miasta wtedy na samym koncu mam 200% cpu ale to nie jest wielki problem
    //dorobic nowy exception ze nie ma wiecej miast
    public Node getRandomCity(Node n) {
        while (true) {
            Random randomGenerator = new Random();
            if(getNodes().size()<8) throw new TryLater();
            //if all cities are defeated
            int numOfDefeated=0;
            int numOfCities=0;
            for(Node it: getNodes()){
                if(it instanceof City){
                    if(it.isIsDefeated()) numOfDefeated++;
                    numOfCities++;
                }
            }
            
            int generatedNumber = randomGenerator.nextInt(getNodes().size());
            
            if (getNodes().get(generatedNumber).isCity() && getNodes().get(generatedNumber) != n)
                if( !nodes.get(generatedNumber).isIsDefeated() || numOfCities==numOfDefeated) {
                return getNodes().get(generatedNumber);
            }
             
        }
//        for(Node it: nodes){
//            if(it!=n && it.isCity()){
//                System.out.println(it.getCircle());
//                return it;
//            }
//        }
//        return null;
    }
    public Node getCityForSuperhero(Node n){
        for(Node it: getNodes()){
            if(it instanceof City){
                if(((City)it).isBadGuyIsGoingToThisCity() && !((City)it).isIsDefeated()){
                    return it;
                }
            }
        }
        return getRandomCity(n);
    }
    //creates road lanes

    public void addEdge(int poczatek, int koniec) {

        Line edge = new Line();
        adj[poczatek][koniec] = true;
        adj[koniec][poczatek] = true;
        edge.setStartX(getNodes().get(poczatek).getCircle().getCenterX());
        edge.setStartY(getNodes().get(poczatek).getCircle().getCenterY());
        edge.setEndX(getNodes().get(koniec).getCircle().getCenterX());
        edge.setEndY(getNodes().get(koniec).getCircle().getCenterY());
        roads.add(edge);
        getPane().getChildren().add(edge);

        if (getNodes().get(poczatek).getCircle().getCenterX() == getNodes().get(koniec).getCircle().getCenterX()) {
            edge = new Line();
            edge.setStartX(getNodes().get(poczatek).getCircle().getCenterX() + roadWidth);
            edge.setStartY(getNodes().get(poczatek).getCircle().getCenterY());
            edge.setEndX(getNodes().get(koniec).getCircle().getCenterX() + roadWidth);
            edge.setEndY(getNodes().get(koniec).getCircle().getCenterY());
            roads.add(edge);
            getPane().getChildren().add(edge);

            edge = new Line();
            edge.setStartX(getNodes().get(poczatek).getCircle().getCenterX() - roadWidth);
            edge.setStartY(getNodes().get(poczatek).getCircle().getCenterY());
            edge.setEndX(getNodes().get(koniec).getCircle().getCenterX() - roadWidth);
            edge.setEndY(getNodes().get(koniec).getCircle().getCenterY());
            roads.add(edge);
            getPane().getChildren().add(edge);

        } else {
            edge = new Line();
            edge.setStartX(getNodes().get(poczatek).getCircle().getCenterX());
            edge.setStartY(getNodes().get(poczatek).getCircle().getCenterY() + roadWidth);
            edge.setEndX(getNodes().get(koniec).getCircle().getCenterX());
            edge.setEndY(getNodes().get(koniec).getCircle().getCenterY() + roadWidth);
            roads.add(edge);
            getPane().getChildren().add(edge);

            edge = new Line();
            edge.setStartX(getNodes().get(poczatek).getCircle().getCenterX());
            edge.setStartY(getNodes().get(poczatek).getCircle().getCenterY() - roadWidth);
            edge.setEndX(getNodes().get(koniec).getCircle().getCenterX());
            edge.setEndY(getNodes().get(koniec).getCircle().getCenterY() - roadWidth);
            roads.add(edge);
            getPane().getChildren().add(edge);

        }
    }

    public Stack<Node> findPathBetweenCities(Node begin, Node end) {//przerobic na city
        Stack<Node> path = new Stack<>();
        visited = new boolean[100];
        for (boolean a : visited) {
            a = false;
        }
        dfs(begin, end, path, visited);
        return path;
    }

    private boolean dfs(Node wierzcholek, Node cel, Stack<Node> sciezka, boolean visited[]) {
//        System.out.println("wielkosc " + sciezka.size());
        sciezka.push(wierzcholek);
//        System.out.println("udalo sie");
        visited[wierzcholek.getNodeNumber()] = true;
        boolean znaleziono = false;
        if (wierzcholek.getNodeNumber() == cel.getNodeNumber()) {
            return true;
        } else {
            for (int i = 0; i < 100; i++) {
                if (i != wierzcholek.getNodeNumber() && visited[i] == false && adj[wierzcholek.getNodeNumber()][i] == true) {
                    if (dfs(getNodes().get(i), cel, sciezka, visited)) {
                        znaleziono = true;
                        break;
                    }
                }
            }
        }
        if (znaleziono == false) {
            sciezka.pop();
        }
        return znaleziono;
    }

    public Stack<Integer> findPathBetweenCities(int begin, int end) {
        Stack<Integer> path = new Stack<>();
        visited = new boolean[100];
        for (boolean a : visited) {
            a = false;
        }
        dfs(begin, end, path, visited);
        return path;
    }

    private boolean dfs(int wierzcholek, int cel, Stack<Integer> sciezka, boolean visited[]) {
        System.out.println("wielkosc " + sciezka.size());
        sciezka.push(wierzcholek);
        System.out.println("udalo sie");
        visited[wierzcholek] = true;
        boolean znaleziono = false;
        if (wierzcholek == cel) {
            return true;
        } else {
            for (int i = 0; i < 100; i++) {
                if (i != wierzcholek && visited[i] == false && adj[wierzcholek][i] == true) {
                    if (dfs(i, cel, sciezka, visited)) {
                        znaleziono = true;
                        break;
                    }
                }
            }
        }
        if (znaleziono == false) {
            sciezka.pop();
        }
        return znaleziono;
    }

    public static int getRoadWidth() {
        return roadWidth;
    }

    /**
     * @return the controlPanel
     */
    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    /**
     * @param controlPanel the controlPanel to set
     */
    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    /**
     * @return the guys
     */
    public synchronized ArrayList<Guy> getGuys() {
        return guys;
    }

    /**
     * @param guys the guys to set
     */
    public synchronized void setGuys(ArrayList<Guy> guys) {
        this.guys = guys;
    }

    /**
     * @return the nodes
     */
    public synchronized ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * @param nodes the nodes to set
     */
    public synchronized void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * @return the nameGetter
     */
    public NameGetter getNameGetter() {
        return nameGetter;
    }

    /**
     * @param nameGetter the nameGetter to set
     */
    public void setNameGetter(NameGetter nameGetter) {
        this.nameGetter = nameGetter;
    }

    /**
     * @return the pane
     */
    public Pane getPane() {
        return pane;
    }

    /**
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return the badGuys
     */
    public synchronized ArrayList<BadGuy> getBadGuys() {
        return badGuys;
    }
}
