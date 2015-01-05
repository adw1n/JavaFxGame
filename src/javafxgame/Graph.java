package javafxgame;

import java.util.ArrayList;
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
    ArrayList<Line> roads;
    private ArrayList<Guy> guys;
    ArrayList<Integer> citiesNumbers;//potem trzeba bedzie usuwac
    private Pane pane;
    private boolean adj[][];
    private boolean visited[];//for dfs
    private static final int roadWidth = 16;
    private ControlPanel controlPanel;
    public Graph(Pane pane) {
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
        controlPanel=new ControlPanel(pane);
    }
    public synchronized void displayEntity(Entity e){
        getControlPanel().displayEntity(e);
    }
    public void addGuy(Guy guy){
        getGuys().add(guy);
    }
    public void addNode(Node node) {
        //if(node.isCity()) citiesNumbers.add(nodes.size());
        node.setNodeNumber(getNodes().size());
        node.setGraph(this);
        getNodes().add(node);

    }
//tu moze byc blad bo na samym poczatku tworze miasto, ale jeszcze nie dodalem do nodes, miasto tworzy mieszkanca a on wywoluje ta fun i moze byc pusto
    //moze byc tak ze nie ma juz niepokonnanego miasta wtedy na samym koncu mam 200% cpu ale to nie jest wielki problem
    //dorobic nowy exception ze nie ma wiecej miast
    public Node getRandomCity(Node n) {
        while (true) {
            Random randomGenerator = new Random();
            if(getNodes().size()<8) throw new TryLater();
            int generatedNumber = randomGenerator.nextInt(getNodes().size());
            if (getNodes().get(generatedNumber).isCity() && getNodes().get(generatedNumber) != n&& !nodes.get(generatedNumber).isIsDefeated()) {
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
        pane.getChildren().add(edge);

        if (getNodes().get(poczatek).getCircle().getCenterX() == getNodes().get(koniec).getCircle().getCenterX()) {
            edge = new Line();
            edge.setStartX(getNodes().get(poczatek).getCircle().getCenterX() + roadWidth);
            edge.setStartY(getNodes().get(poczatek).getCircle().getCenterY());
            edge.setEndX(getNodes().get(koniec).getCircle().getCenterX() + roadWidth);
            edge.setEndY(getNodes().get(koniec).getCircle().getCenterY());
            roads.add(edge);
            pane.getChildren().add(edge);

            edge = new Line();
            edge.setStartX(getNodes().get(poczatek).getCircle().getCenterX() - roadWidth);
            edge.setStartY(getNodes().get(poczatek).getCircle().getCenterY());
            edge.setEndX(getNodes().get(koniec).getCircle().getCenterX() - roadWidth);
            edge.setEndY(getNodes().get(koniec).getCircle().getCenterY());
            roads.add(edge);
            pane.getChildren().add(edge);

        } else {
            edge = new Line();
            edge.setStartX(getNodes().get(poczatek).getCircle().getCenterX());
            edge.setStartY(getNodes().get(poczatek).getCircle().getCenterY() + roadWidth);
            edge.setEndX(getNodes().get(koniec).getCircle().getCenterX());
            edge.setEndY(getNodes().get(koniec).getCircle().getCenterY() + roadWidth);
            roads.add(edge);
            pane.getChildren().add(edge);

            edge = new Line();
            edge.setStartX(getNodes().get(poczatek).getCircle().getCenterX());
            edge.setStartY(getNodes().get(poczatek).getCircle().getCenterY() - roadWidth);
            edge.setEndX(getNodes().get(koniec).getCircle().getCenterX());
            edge.setEndY(getNodes().get(koniec).getCircle().getCenterY() - roadWidth);
            roads.add(edge);
            pane.getChildren().add(edge);

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
}
