

package javafxgame;

import java.util.ArrayList;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

/**
 * Class that implements some of the fighting abilities.
 * @author adwin_
 */
public abstract class Fighter extends Guy{
    private Fighter opponent;
    private ArrayList<FightersAbility> abilities;
    public Fighter(int hp, double x, double y, Pane pane, Node currentNode,String name) {
        super(hp, x, y, pane, currentNode,name);
        opponent=null;
        abilities=new ArrayList<>();
        Random randomGenerator = new Random();
        int generatedNumber = randomGenerator.nextInt(10)+6;
        abilities.add(new FightersAbility(Ability.SWIFTNESS, generatedNumber));
        generatedNumber = randomGenerator.nextInt(10)+6;
        abilities.add(new FightersAbility(Ability.POWER, generatedNumber));
        generatedNumber = randomGenerator.nextInt(10)+6;
        abilities.add(new FightersAbility(Ability.ENERGY, generatedNumber));
        generatedNumber = randomGenerator.nextInt(10)+6;
        abilities.add(new FightersAbility(Ability.FIGHTINGSKILLS, generatedNumber));
        generatedNumber = randomGenerator.nextInt(10)+6;
        abilities.add(new FightersAbility(Ability.INTELLIGENCE, generatedNumber));
        generatedNumber = randomGenerator.nextInt(5);//gwarantuje ze bedzie jakis dmg
        abilities.add(new FightersAbility(Ability.STAMINA, generatedNumber));
        
    }

    @Override
    public String toString() {
        
        String ans="";
        if(this instanceof Superhero)
            ans="Superhero: "+super.toString();
        else if(this instanceof BadGuy) ans="BadGuy "+super.toString();
                    ans+="\nHP: "+getHp();
                ans+="\nAbilities: \n";
        for(FightersAbility it: abilities)
            ans+=it;
        
        return ans; //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * Makes two guys fight each other. 
     * @param opponent 
     */
    public synchronized void fight(Fighter opponent){
        if(this.getOpponent()==null && opponent.getOpponent()==null){
            
            opponent.setOpponent(this);
            this.setOpponent(opponent);
            if(!isStopped() && !opponent.isStopped()){
            mySuspend();
            opponent.mySuspend();
            float mySwiftness=0,opponentsSwiftness=0,myFightingSkills=0,opponentsFightingSkills=0,myMultiplier=0,opponentsMultiplier=0,myStamina=0,opponentsStamina=0;
            float myDmg,opponentsDmg;
            for(FightersAbility ab: getAbilities()){
                if(ab.getAbility()==Ability.INTELLIGENCE || ab.getAbility()==Ability.ENERGY || ab.getAbility()==Ability.POWER)
                    myMultiplier=Math.max(myMultiplier, ab.getAbilityAttribute());
                if(ab.getAbility()==Ability.FIGHTINGSKILLS)
                    myFightingSkills=ab.getAbilityAttribute();
                if(ab.getAbility()==Ability.SWIFTNESS){
                    mySwiftness=ab.getAbilityAttribute();
                }
                if(ab.getAbility()==Ability.STAMINA){
                    myStamina=ab.getAbilityAttribute();
                }
            }
            for(FightersAbility ab: opponent.getAbilities()){
                if(ab.getAbility()==Ability.FIGHTINGSKILLS)
                    opponentsFightingSkills=ab.getAbilityAttribute();
                if(ab.getAbility()==Ability.SWIFTNESS){
                    opponentsSwiftness=ab.getAbilityAttribute();
                }
                if(ab.getAbility()==Ability.INTELLIGENCE || ab.getAbility()==Ability.ENERGY || ab.getAbility()==Ability.POWER)
                    opponentsMultiplier=Math.max(opponentsMultiplier, ab.getAbilityAttribute());
                if(ab.getAbility()==Ability.STAMINA){
                    opponentsStamina=ab.getAbilityAttribute();
                }
            }
            Fighter attacker[]= new Fighter [2];
            float dmg[]=new float [2];
            myDmg=myFightingSkills*myMultiplier-opponentsStamina;
            opponentsDmg=opponentsFightingSkills*opponentsMultiplier-myStamina;
            attacker[0]=this;
            dmg[0]=myDmg;
            attacker[1]=opponent;
            dmg[1]=opponentsDmg;
            if(mySwiftness<opponentsSwiftness){
                dmg[0]=opponentsDmg;
                attacker[0]=opponent;
                dmg[1]=myDmg;
                attacker[1]=this;
            }
            int currentTurn=0;
            while((attacker[0].getHp()>0) && (attacker[1].getHp()>0)){
                attacker[currentTurn^1].takeDmg(dmg[currentTurn]);
                currentTurn^=1;
            }
            attacker[0].myResume();
            attacker[1].myResume();
            if(attacker[0].getHp()<=0) attacker[0].myStop();
            
            if(attacker[1].getHp()<=0) attacker[1].myStop();
          
            opponent.setOpponent(null);
            this.opponent=null;
            }
        }
    }

    /**
     * @return the opponent
     */
    public synchronized Fighter getOpponent() {
        return opponent;
    }

    /**
     * @param opponent the opponent to set
     */
    public synchronized void setOpponent(Fighter opponent) {
        this.opponent = opponent;
    }
    /**
     * Increases the specific ability's power.
     * @param num the ammount you should increase the ability by
     * @param ab the ability of the fighter to increase
     */
    public void increaseAbility(float num,Ability ab){
        for(FightersAbility it: abilities)
            if(it.getAbility().equals(ab)){
            it.increaseAbilityAttribute(num);
                Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    getGraph().getControlPanel().displayEntity();
                }
            });
            }
    }
    /**
     * @return the abilities
     */
    public ArrayList<FightersAbility> getAbilities() {
        return abilities;
    }

    /**
     * @param abilities the abilities to set
     */
    public void setAbilities(ArrayList<FightersAbility> abilities) {
        this.abilities = abilities;
    }

}
