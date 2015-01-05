package javafxgame;

public class PowerSource extends Entity{
    static int ID_count=0;
    private int ID;
    
    private FightersAbility enhancedAbility;
    //zdolnosc ktora wzmacnia

    public PowerSource(String name) {
//        super(name);
        ID_count++;
        ID=ID_count;
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
    public void increaseEnergy(float energy){
        enhancedAbility.increaseAbilityAttribute(energy);
    }
    public float decreaseEnergy(float energy){
        if(energy>enhancedAbility.getAbilityAttribute()) {
            energy=enhancedAbility.getAbilityAttribute();
            enhancedAbility.setAbilityAttribute(0);
        }
        else enhancedAbility.decreaseAbilityAttribute(energy);
        return energy;
        //if(energy<=0) destroy
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