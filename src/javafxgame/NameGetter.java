

package javafxgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class NameGetter {
    ArrayList<String> names;
    int num;
    NameGetter(){
        num=0;
        names=new ArrayList<>();
        try {
//            /Users/Przemyslaw/Java/JavaFxGame/src/javafxgame
//                                for(String name : Files.readAllLines(Paths.get("/Users/Przemyslaw/Java/JavaFxGame/src/javafxgame/femaleNames.txt")))
String s=Paths.get(System.getProperty("user.dir")).toString();
//wycinam /dist
String fileName=new String();
System.out.println("koncowka to: "+s.substring(s.length()-5));
if(s.substring(s.length()-5).equals("/dist"))
    for(int pos=0;pos<s.length()-5;pos++)
        fileName+=s.charAt(pos);
else fileName=s;
            for(String name : Files.readAllLines(Paths.get(fileName+"/femaleNames.txt")))
                names.add(name);
        } catch (IOException ex) {
            Logger.getLogger(NameGetter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public synchronized String getFemaleName(){
        if(num>=names.size()) num=0;
        num++;
        return names.get(num-1);
    }
}
