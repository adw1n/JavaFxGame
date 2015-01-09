package javafxgame;
/**
 * 
 * @author adwin_
 */
/**
 * Stores an object name. Makes all that polymorphic magic possible to happen. Creates an interface for stopping/suspending/resuming the travel.
 * @author adwin_
 */
public abstract class Entity {
    Entity thisEntity;
    /**
     * Constructs and initializes an Entity object.
     */
    /**
     * Creates an entity
     */
    public Entity(){
        thisEntity=this;
    }
    private String name;
    /**
     * Constructs and initializes an Entity object. Stores its name.
     * @param name 
     */
    public Entity(String name) {
        this();
        this.name = name;
    }
    /** 
     * Returns all the info that u are ever going to need about this object in a String.
     * @return 
     */
    @Override
    public String toString() {
        return getName();
    }
    
//
//    /**
//     * @return the name
//     */
//    public String getName() {
//        return name;
//    }
    abstract  void myStop() ;
    
    void mySuspend() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void myResume() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
