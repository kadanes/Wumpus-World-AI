package Whmpus;

class Percept {

    boolean stench;
    final boolean breeze;
    final boolean glitter;
    final boolean bump;
    final Coordinates position;

    public Percept(boolean breeze, boolean bump,  boolean glitter, boolean steanch, Coordinates position ) {
        this.stench = steanch;
        this.breeze = breeze;
        this.glitter = glitter;
        this.bump = bump;
        this.position = position;
        
    }
    
    
    
}