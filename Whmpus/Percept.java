package Whmpus;

class Percept {

    boolean stench;
    final boolean breez;
    final boolean glitter;
    final boolean bump;
    final Coordinates position;

    public Percept(boolean breez, boolean bump,  boolean glitter, boolean steanch, Coordinates position ) {
        this.stench = steanch;
        this.breez = breez;
        this.glitter = glitter;
        this.bump = bump;
        this.position = position;
        
    }
    
    
    
}