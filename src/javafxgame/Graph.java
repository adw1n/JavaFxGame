

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
    ArrayList<Node> nodes;
    ArrayList<Line> roads;
    ArrayList<Integer> citiesNumbers;//potem trzeba bedzie usuwac
    private Pane pane;
    private boolean adj[][];
    private boolean visited[];//for dfs
    private static final int roadWidth=16;
    public Graph(Pane pane){
//        stosProcesow=new Stack<>();
        this.pane=pane;
        nodes = new ArrayList<>();
        roads = new ArrayList<>();
        
        adj = new boolean [100][100];
        for (boolean[] adj1 : adj) {
            for (boolean a: adj1) {
                a = false;
            }
        }
    }
    
    public void addNode(Node node){
        //if(node.isCity()) citiesNumbers.add(nodes.size());
        node.setNodeNumber(nodes.size());
        node.setGraph(this);
        nodes.add(node);
        
         
    }
    public Node getRandomCity( Node n){
        while(true){
         Random randomGenerator = new Random();
            int generatedNumber=randomGenerator.nextInt(nodes.size());
            if(nodes.get(generatedNumber).isCity() && nodes.get(generatedNumber)!= n) return nodes.get(generatedNumber);
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
    public void addEdge(int poczatek,int koniec){
        
        Line edge=new Line();
        adj[poczatek][koniec]=true;
        adj[koniec][poczatek]=true;
        edge.setStartX(nodes.get(poczatek).getCircle().getCenterX());
        edge.setStartY(nodes.get(poczatek).getCircle().getCenterY());
        edge.setEndX(nodes.get(koniec).getCircle().getCenterX());
        edge.setEndY(nodes.get(koniec).getCircle().getCenterY());
        roads.add(edge);
        pane.getChildren().add(edge);

        if(nodes.get(poczatek).getCircle().getCenterX()==nodes.get(koniec).getCircle().getCenterX()){
            edge=new Line();
            edge.setStartX(nodes.get(poczatek).getCircle().getCenterX()+roadWidth);
            edge.setStartY(nodes.get(poczatek).getCircle().getCenterY());
            edge.setEndX(nodes.get(koniec).getCircle().getCenterX()+roadWidth);
            edge.setEndY(nodes.get(koniec).getCircle().getCenterY());
            roads.add(edge);
            pane.getChildren().add(edge);

            edge=new Line();
            edge.setStartX(nodes.get(poczatek).getCircle().getCenterX()-roadWidth);
            edge.setStartY(nodes.get(poczatek).getCircle().getCenterY());
            edge.setEndX(nodes.get(koniec).getCircle().getCenterX()-roadWidth);
            edge.setEndY(nodes.get(koniec).getCircle().getCenterY());
            roads.add(edge);
            pane.getChildren().add(edge);

        }
        else{
            edge=new Line();
            edge.setStartX(nodes.get(poczatek).getCircle().getCenterX());
            edge.setStartY(nodes.get(poczatek).getCircle().getCenterY()+roadWidth);
            edge.setEndX(nodes.get(koniec).getCircle().getCenterX());
            edge.setEndY(nodes.get(koniec).getCircle().getCenterY()+roadWidth);
            roads.add(edge);
            pane.getChildren().add(edge);

            edge=new Line();
            edge.setStartX(nodes.get(poczatek).getCircle().getCenterX());
            edge.setStartY(nodes.get(poczatek).getCircle().getCenterY()-roadWidth);
            edge.setEndX(nodes.get(koniec).getCircle().getCenterX());
            edge.setEndY(nodes.get(koniec).getCircle().getCenterY()-roadWidth);
            roads.add(edge);
                    pane.getChildren().add(edge);

        }
    }
    public Stack<Node> findPathBetweenCities(Node begin,Node end){//przerobic na city
        Stack<Node> path=new Stack<>();
        visited=new boolean[100];
        for(boolean a: visited) a=false;
        dfs(begin,end,path,visited);
        return path;
    }
    private boolean dfs(Node wierzcholek, Node cel,Stack<Node> sciezka,boolean visited[]){
        System.out.println("wielkosc "+sciezka.size());
        sciezka.push(wierzcholek);
        System.out.println("udalo sie");
        visited[wierzcholek.getNodeNumber()]=true;
        boolean znaleziono=false;
        if(wierzcholek.getNodeNumber()== cel.getNodeNumber()) {
            return true;
        }
        else {
            for(int i=0;i<100;i++)
                if(i!=wierzcholek.getNodeNumber() && visited[i]==false && adj[wierzcholek.getNodeNumber()][i]==true){
                    if(dfs(nodes.get(i),cel,sciezka,visited))
                    {
                        znaleziono=true;
                        break;
                    }}
        }
        if(znaleziono==false) {
            sciezka.pop();
        }
        return znaleziono;
    }
    public Stack<Integer> findPathBetweenCities(int begin,int end){
        Stack<Integer> path=new Stack<>();
        visited=new boolean[100];
        for(boolean a: visited) a=false;
        dfs(begin,end,path,visited);
        return path;
    }
    private boolean dfs(int wierzcholek, int cel,Stack<Integer> sciezka,boolean visited[]){
        System.out.println("wielkosc "+sciezka.size());
        sciezka.push(wierzcholek);
        System.out.println("udalo sie");
        visited[wierzcholek]=true;
        boolean znaleziono=false;
        if(wierzcholek == cel) {
            return true;
        }
        else {
            for(int i=0;i<100;i++)
                if(i!=wierzcholek && visited[i]==false && adj[wierzcholek][i]==true){
                    if(dfs(i,cel,sciezka,visited))
                    {
                        znaleziono=true;
                        break;
                    }}
        }
        if(znaleziono==false) {
            sciezka.pop();
        }
        return znaleziono;
    }
    public static int getRoadWidth(){
        return roadWidth;
    }
}
