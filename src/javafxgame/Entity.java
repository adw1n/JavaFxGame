package javafxgame;

public abstract class Entity {
    Entity thisEntity;
    public Entity(){
        thisEntity=this;
    }
    private String name;
    public Entity(String name) {
        this();
        this.name = name;
        System.out.println(name);
    }

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
