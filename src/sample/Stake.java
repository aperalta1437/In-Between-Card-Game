package sample;
/**
 * Stake.java
 * <p>
 * <h1>Stake</h1>
 * Object of this class represents a stake of chips for use in the In-Between game of cards. 
 * <p>
 * @author Angel Peralta
 * @since 28MAR2019 17:40 EST
 */
public class Stake {		// Class definition.
    // Declaring instance variables.
    private int bettedChips;	// Chips put on bet.
    private int totalChips;		// Total chips on in the stake.
    /**
     * Class constructor initializes the the total number of chips in stake.
     * @param totalChips total number of chips.
     */
    public Stake(int totalChips) {
        this.totalChips = totalChips;
    }
    /**
     * @return the bet
     */
    public int getBettedChips() {
        return bettedChips;
    }
    /**
     * @param bettedChips the bet to set
     */
    public void setBettedChips(int bettedChips) {
        totalChips -= bettedChips;
        this.bettedChips = bettedChips;
    }
    /**
     * @return total number of chips in the stake.
     */
    public int getTotalChips() {
        return totalChips;
    }
    /**
     * Doubles the amount of chips put on bet and adds it to the stake.
     */
    public void doubleBettedChips() {
        totalChips -= bettedChips;
        bettedChips *= 2;
    }
    /**
     * Increases the stake by the amount of the bet.
     */
    public void increaseTotalChips() {
        totalChips += (2 * bettedChips);
        bettedChips = 0;
    }
    /**
     * Decreases the total number of chips by the amount of the bet.
     */
    public void decreaseTotalChips() {
        this.bettedChips = 0;
    }
    /**
     * Called upon tie hand. Increases the total number of chips by 2.
     */
    public void tieHand() {
        totalChips = totalChips + bettedChips + 2;
        bettedChips = 0;
    }
    /**
     * @return true of there still are chips in the stake. Otherwise false.
     */
    public boolean hasChips() {
        return (totalChips != 0);
    }
    /**
     * Puts bet back in the stake.
     */
    public void putBack() {
        totalChips += bettedChips;
        bettedChips = 0;
    }
}