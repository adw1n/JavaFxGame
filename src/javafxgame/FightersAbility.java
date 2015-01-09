

package javafxgame;

import static java.lang.Math.max;

/**
 * Stores all the info about an ability / a skill. How developed it is, and what does it do and stuff like that.
 * @author adwin_
 */
public class FightersAbility {
    private Ability ability;
    private float abilityAttribute;
    /** 
     * Creates an ability
     * @param ability the ability that abilityAttribute belongs to 
     * @param abilityAttribute how strong is that ability
     */
    public FightersAbility(Ability ability,float abilityAttribute) {
        this.ability=ability;
        this.abilityAttribute=abilityAttribute;
    }

    /**
     * @return the abilityAttribute
     */
    public float getAbilityAttribute() {
        return abilityAttribute;
    }

    @Override
    public String toString() {
        return ability.name()+"\nability attribute: "+abilityAttribute+"\n"; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @param abilityAttribute the abilityAttribute to set
     */
    public void setAbilityAttribute(float abilityAttribute) {
        this.abilityAttribute = abilityAttribute;
        this.abilityAttribute=max(this.abilityAttribute,0);
    }
    /**
     * Makes the ability stronger.
     * @param difference by how much did the abilityAttribute change
     */
    public void increaseAbilityAttribute(float difference){
        abilityAttribute+=Math.abs(difference);
    }
    /**
     * Makes the ability weaker.
     * @param difference  by how much did the abilityAttribute change
     */
    public void decreaseAbilityAttribute(float difference){
        abilityAttribute-=Math.abs(difference);
        abilityAttribute=max(abilityAttribute,0);
    }

    /**
     * @return the ability
     */
    public Ability getAbility() {
        return ability;
    }

    /**
     * @param ability the ability to set
     */
    public void setAbility(Ability ability) {
        this.ability = ability;
    }
    
}
