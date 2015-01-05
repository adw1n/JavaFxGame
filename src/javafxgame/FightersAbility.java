

package javafxgame;


public class FightersAbility {
    Ability ability;
    private float abilityAttribute;//jak bardzo jest rozwiniete / potencjal, skill lvl

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

    /**
     * @param abilityAttribute the abilityAttribute to set
     */
    public void setAbilityAttribute(float abilityAttribute) {
        this.abilityAttribute = abilityAttribute;
    }
    public void increaseAbilityAttribute(float difference){
        abilityAttribute+=Math.abs(difference);
    }
    public void decreaseAbilityAttribute(float difference){
        abilityAttribute-=Math.abs(difference);
    }
    
}
