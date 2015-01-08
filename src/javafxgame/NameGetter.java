

package javafxgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class NameGetter {
    ArrayList<String> guysNames,citiesNames,powerSources;
    int numOfFemaleNames;
    int numOfCrossroads;
    int numOfCities,numOfPowerSources;
    NameGetter(){
        citiesNames=new ArrayList<>();
        powerSources=new ArrayList<>();
        this.numOfCities = 0;
        numOfFemaleNames=0;
        numOfPowerSources=0;
        numOfCrossroads=0;
        guysNames=new ArrayList<>();
        try {
//            /Users/Przemyslaw/Java/JavaFxGame/src/javafxgame
//                                for(String name : Files.readAllLines(Paths.get("/Users/Przemyslaw/Java/JavaFxGame/src/javafxgame/femaleNames.txt")))
String s=Paths.get(System.getProperty("user.dir")).toString();
//wycinam /dist
/*String fileName=new String();
System.out.println("koncowka to: "+s.substring(s.length()-5));
if(s.substring(s.length()-5).equals("/dist"))
    for(int pos=0;pos<s.length()-5;pos++)
        fileName+=s.charAt(pos);
else fileName=s;*/
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
    public synchronized String getPowerSourceName(){
        if(numOfPowerSources>=powerSources.size()) numOfPowerSources=0;
        numOfPowerSources++;
        return powerSources.get(numOfPowerSources-1);
    }
    public String getCapitalName(){
        String s="Trantor";
        return s;
    }
    public synchronized String getCrossroadsName(){
        numOfCrossroads++;
        String name="Crossroad "+numOfCrossroads;
        return name;
    }
    public synchronized String getFemaleName(){
        if(numOfFemaleNames>=guysNames.size()) numOfFemaleNames=0;
        numOfFemaleNames++;
        return guysNames.get(numOfFemaleNames-1);
    }
    public synchronized String getCityName(){
        if(numOfFemaleNames>=citiesNames.size()) numOfCities=0;
        numOfCities++;
        return citiesNames.get(numOfCities-1);
    }
    
}
