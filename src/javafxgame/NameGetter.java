

package javafxgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class NameGetter {
    private ArrayList<String> guysNames;
    private ArrayList<String> citiesNames;
    private ArrayList<String> powerSources;
    private int numOfFemaleNames;
    private int numOfCrossroads;
    private int numOfCities;
    private int numOfPowerSources;
    
    NameGetter(){
        citiesNames=new ArrayList<>();
        powerSources=new ArrayList<>();
        this.numOfCities = 0;
        numOfFemaleNames=0;
        numOfPowerSources=0;
        numOfCrossroads=0;
        guysNames=new ArrayList<>();
        try {
String s=Paths.get(System.getProperty("user.dir")).toString();
            for(String name : Files.readAllLines(Paths.get("powerSources.txt")))
                powerSources.add(name);
            for(String name : Files.readAllLines(Paths.get("femaleNames.txt")))
                guysNames.add(name);
            for(String name : Files.readAllLines(Paths.get("citiesNames.txt")))
                citiesNames.add(name);
        } catch (IOException ex) {
            Logger.getLogger(NameGetter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 
     * @return returns name for a new PowerSource
     */
    public synchronized String getPowerSourceName(){
        if(getNumOfPowerSources()>=getPowerSources().size()) setNumOfPowerSources(0);
        setNumOfPowerSources(getNumOfPowerSources() + 1);
        return getPowerSources().get(getNumOfPowerSources()-1);
    }
    /**
     * 
     * @return returns a unique name for the capital
     */
    public String getCapitalName(){
        String s="Trantor";
        return s;
    }
    /**
     * 
     * @return returns a name for crossroads
     */
    public synchronized String getCrossroadsName(){
        setNumOfCrossroads(getNumOfCrossroads() + 1);
        String name="Crossroad "+getNumOfCrossroads();
        return name;
    }
    /**
     * 
     * @return returns a female name from a file
     */
    public synchronized String getFemaleName(){
        if(getNumOfFemaleNames()>=getGuysNames().size()) setNumOfFemaleNames(0);
        setNumOfFemaleNames(getNumOfFemaleNames() + 1);
        return getGuysNames().get(getNumOfFemaleNames()-1);
    }
    /**
     * 
     * @return returns a city name from a file
     */
    public synchronized String getCityName(){
        if(getNumOfFemaleNames()>=getCitiesNames().size()) setNumOfCities(0);
        setNumOfCities(getNumOfCities() + 1);
        return getCitiesNames().get(getNumOfCities()-1);
    }

    /**
     * @return the guysNames
     */
    public ArrayList<String> getGuysNames() {
        return guysNames;
    }

    /**
     * @param guysNames the guysNames to set
     */
    public void setGuysNames(ArrayList<String> guysNames) {
        this.guysNames = guysNames;
    }

    /**
     * @return the citiesNames
     */
    public ArrayList<String> getCitiesNames() {
        return citiesNames;
    }

    /**
     * @param citiesNames the citiesNames to set
     */
    public void setCitiesNames(ArrayList<String> citiesNames) {
        this.citiesNames = citiesNames;
    }

    /**
     * @return the powerSources
     */
    public ArrayList<String> getPowerSources() {
        return powerSources;
    }

    /**
     * @param powerSources the powerSources to set
     */
    public void setPowerSources(ArrayList<String> powerSources) {
        this.powerSources = powerSources;
    }

    /**
     * @return the numOfFemaleNames
     */
    public int getNumOfFemaleNames() {
        return numOfFemaleNames;
    }

    /**
     * @param numOfFemaleNames the numOfFemaleNames to set
     */
    public void setNumOfFemaleNames(int numOfFemaleNames) {
        this.numOfFemaleNames = numOfFemaleNames;
    }

    /**
     * @return the numOfCrossroads
     */
    public int getNumOfCrossroads() {
        return numOfCrossroads;
    }

    /**
     * @param numOfCrossroads the numOfCrossroads to set
     */
    public void setNumOfCrossroads(int numOfCrossroads) {
        this.numOfCrossroads = numOfCrossroads;
    }

    /**
     * @return the numOfCities
     */
    public int getNumOfCities() {
        return numOfCities;
    }

    /**
     * @param numOfCities the numOfCities to set
     */
    public void setNumOfCities(int numOfCities) {
        this.numOfCities = numOfCities;
    }

    /**
     * @return the numOfPowerSources
     */
    public int getNumOfPowerSources() {
        return numOfPowerSources;
    }

    /**
     * @param numOfPowerSources the numOfPowerSources to set
     */
    public void setNumOfPowerSources(int numOfPowerSources) {
        this.numOfPowerSources = numOfPowerSources;
    }
    
}
