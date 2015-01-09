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
/**
 * Creates all the Cities, connects them, creates citizens, displays info about stuff.
 * @author adwin_
 */
public class Graph {

    private ArrayList<Node> nodes;
    private final int numOfCitizensAlive;
    private final long startTime;
    private final ArrayList<Line> roads;
    private NameGetter nameGetter;
    private ArrayList<Guy> guys;
    private final ArrayList<BadGuy> badGuys;
    private ArrayList<Integer> citiesNumbers;
    private final Pane pane;
    private final boolean adj[][];
    private boolean visited[];//for dfs
    private static final int roadWidth = 16;
    private ControlPanel controlPanel;
    private int numOfCitiesAlive;
    private BadGuysCreator badGuysCreator;
    /**
     * Creates a graph, and starts a game by doing it.
     * @param pane 
     */
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
    /**
     * Initializes all the needed variables.
     */
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
    /**
     * Forwards the request to display an entity.
     * @param e 
     */
    public synchronized void displayEntity(Entity e){
        getControlPanel().displayEntity(e);
    }
    /**
     * Makes all the info in the user control panel to be updated.
     */
    public synchronized  void displayEntity(){
        getControlPanel().displayEntity();
    }
    /**
     * Adds a guy to the list.
     * @param guy 
     */
    public synchronized void addGuy(Guy guy){
        getGuys().add(guy);
    }
    /**
     * Adds a bad guy to the list of bad guys.
     * @param guy 
     */
    public synchronized void addBadGuy(BadGuy guy){
        try{getBadGuys().add(guy);}
        catch(ConcurrentModificationException e){};//not gonna happen
    }
    /**
     * Adds a node in the graph.
     * @param node 
     */
    public void addNode(Node node) {
        if(node instanceof City) increaseNumOfCitiesAlive();
        //if(node.isCity()) citiesNumbers.add(nodes.size());
        node.setNodeNumber(getNodes().size());
        node.setGraph(this);
        getNodes().add(node);

    }
    /**
     * Increases the displayed number of cities still alive.
     */
    private synchronized void increaseNumOfCitiesAlive(){
        numOfCitiesAlive++;
        controlPanel.updateNumOfCitiesAlive(numOfCitiesAlive);
    }
    /**
     * Decreases the displayed number of cities still alive.
     */
    public synchronized void decreaseNumOfCitiesAlive(){
        numOfCitiesAlive--;
        numOfCitiesAlive=Math.max(0,numOfCitiesAlive);
        controlPanel.updateNumOfCitiesAlive(numOfCitiesAlive);
        
        
    }
    /**
     * Returns the number of cities with still working PowerSources.
     * @return 
     */
    public synchronized int getNumOfCitiesAlive(){
        return numOfCitiesAlive;
    };
    /**
     * Returns a random City form the graph.
     * @param n
     * @return 
     */
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
    }
    /**
     * Provides a City for the superhero to go to. Hopefully a bad guy is near that city.
     * @param n
     * @return 
     */
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
    /**
     * Creates roads adds them to the internal adjacency matrix.
     * @param begin
     * @param end 
     */
    public void addEdge(int begin, int end) {

        Line edge = new Line();
        adj[begin][end] = true;
        adj[end][begin] = true;
        edge.setStartX(getNodes().get(begin).getCircle().getCenterX());
        edge.setStartY(getNodes().get(begin).getCircle().getCenterY());
        edge.setEndX(getNodes().get(end).getCircle().getCenterX());
        edge.setEndY(getNodes().get(end).getCircle().getCenterY());
        roads.add(edge);
        getPane().getChildren().add(edge);

        if (getNodes().get(begin).getCircle().getCenterX() == getNodes().get(end).getCircle().getCenterX()) {
            edge = new Line();
            edge.setStartX(getNodes().get(begin).getCircle().getCenterX() + roadWidth);
            edge.setStartY(getNodes().get(begin).getCircle().getCenterY());
            edge.setEndX(getNodes().get(end).getCircle().getCenterX() + roadWidth);
            edge.setEndY(getNodes().get(end).getCircle().getCenterY());
            roads.add(edge);
            getPane().getChildren().add(edge);

            edge = new Line();
            edge.setStartX(getNodes().get(begin).getCircle().getCenterX() - roadWidth);
            edge.setStartY(getNodes().get(begin).getCircle().getCenterY());
            edge.setEndX(getNodes().get(end).getCircle().getCenterX() - roadWidth);
            edge.setEndY(getNodes().get(end).getCircle().getCenterY());
            roads.add(edge);
            getPane().getChildren().add(edge);

        } else {
            edge = new Line();
            edge.setStartX(getNodes().get(begin).getCircle().getCenterX());
            edge.setStartY(getNodes().get(begin).getCircle().getCenterY() + roadWidth);
            edge.setEndX(getNodes().get(end).getCircle().getCenterX());
            edge.setEndY(getNodes().get(end).getCircle().getCenterY() + roadWidth);
            roads.add(edge);
            getPane().getChildren().add(edge);

            edge = new Line();
            edge.setStartX(getNodes().get(begin).getCircle().getCenterX());
            edge.setStartY(getNodes().get(begin).getCircle().getCenterY() - roadWidth);
            edge.setEndX(getNodes().get(end).getCircle().getCenterX());
            edge.setEndY(getNodes().get(end).getCircle().getCenterY() - roadWidth);
            roads.add(edge);
            getPane().getChildren().add(edge);

        }
    }
    /**
     * Performs a dfs and returns a stack of the nodes to visit to get from the begin Node to the end Node.
     * @param begin
     * @param end
     * @return 
     */
    public Stack<Node> findPathBetweenCities(Node begin, Node end) {//przerobic na city
        Stack<Node> path = new Stack<>();
        visited = new boolean[100];
        for (boolean a : visited) {
            a = false;
        }
        dfs(begin, end, path, visited);
        return path;
    }
    /**
     * Does a recursive dfs.
     * @param wierzcholek
     * @param cel
     * @param sciezka
     * @param visited
     * @return 
     */
    private boolean dfs(Node wierzcholek, Node cel, Stack<Node> sciezka, boolean visited[]) {
        sciezka.push(wierzcholek);
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
    /**
     * An overloaded func working on intigers instead of Node references. 
     * @param begin
     * @param end
     * @return returns the nodes numbers to visit
     */
    public Stack<Integer> findPathBetweenCities(int begin, int end) {
        Stack<Integer> path = new Stack<>();
        visited = new boolean[100];
        for (boolean a : visited) {
            a = false;
        }
        dfs(begin, end, path, visited);
        return path;
    }
    /**
     * Does recursive dfs.
     * @param wierzcholek
     * @param cel
     * @param sciezka
     * @param visited
     * @return 
     */
    private boolean dfs(int wierzcholek, int cel, Stack<Integer> sciezka, boolean visited[]) {
        sciezka.push(wierzcholek);
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
    /**
     * 
     * @return the road width
     */
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
