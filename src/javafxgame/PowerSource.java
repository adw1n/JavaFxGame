package javafxgame;
/**
 * Power source that is in each city, When a Villain drains powersource he becomes stronger. PowerSources are required to create a superhero.
 * @author adwin_
 */
public class PowerSource extends Entity{
    private static int ID_count=0;
    private int ID;
    
    private FightersAbility enhancedAbility;
    /**
     * Creates a power source.
     * @param enhancedAbility
     * @param graph 
     */
    public PowerSource(FightersAbility enhancedAbility,Graph graph) {
        super(graph.getNameGetter().getPowerSourceName());
        this.enhancedAbility=enhancedAbility;
        ID_count++;
        ID=ID_count;
    }

    @Override
    public String toString() {
        return "Power source: "+super.toString()+"\nenchanced ability:"
                +"\n"+enhancedAbility; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    void myStop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @return the energy aka potencjal
     */
    public float getEnergy() {
        return enhancedAbility.getAbilityAttribute();
    }

    /**
     * @param energy the energy to set
     */
    public void setEnergy(float energy) {
        enhancedAbility.setAbilityAttribute(energy);
    }
    /**
     * Increases the energy of the power source by the given ammount.
     * @param energy 
     */
    public void increaseEnergy(float energy){
        enhancedAbility.increaseAbilityAttribute(energy);
    }
    /**
     * Decreases the energy of the power source by the given ammount.
     * @param energy 
     */
    public void decreaseEnergy(float energy){
        if(energy>enhancedAbility.getAbilityAttribute()) {
            enhancedAbility.setAbilityAttribute(0);
        }
        else enhancedAbility.decreaseAbilityAttribute(energy);
    }
    /**
     * @return the enhancedAbility
     */
    public FightersAbility getEnhancedAbility() {
        return enhancedAbility;
    }

    /**
     * @param enhancedAbility the enhancedAbility to set
     */
    public void setEnhancedAbility(FightersAbility enhancedAbility) {
        this.enhancedAbility = enhancedAbility;
    }
}
