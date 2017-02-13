package model;

import com.google.gson.annotations.Expose;

/**
 * @author: Jason Balaci
 */
public class GameModel {
    @Expose
    private BoardModel boardModel; // board model object
    @Expose
    private Disc actor1Colour, actor2Colour; // colours of each actor
    @Expose
    private int actor1DiscsLeft, actor2DiscsLeft; // how many discs can each actor still place
    @Expose
    private int actor1DiscsLost, actor2DiscsLost; // how many discs did each actor lose to the enemy
    @Expose
    private Disc currentActor; // the colour of the current actor
    @Expose
    private State state; // the current game state
    @Expose
    private boolean versusAI;

    public GameModel() { // constructor
        this.boardModel = new BoardModel(); // create a board model
        boardModel.reset(); // reset it
        initData(); // init the default data
    }

    private void initData() { // basic method to initiate all the data values with the default stuff
        setState(State.PLACE_DISC); // initial game state is PLACE_DISC mode
        setActor1Colour(Math.random() < 0.5 ? Disc.BLUE : Disc.RED); // randomly decide on the colours of each player
        setActor2Colour(getActor1Colour() == Disc.RED ? Disc.BLUE : Disc.RED); // actor2 is whatever actor1 isnt
        setCurrentActor(Math.random() > 0.5 ? getActor1Colour() : getActor2Colour()); // set the current actor to actor1
        setActor1DiscsLeft(6); // set actor 1's placeable count to 6
        setActor2DiscsLeft(6); // same thing but for actor 2
        setActor1DiscsLost(0); //set actor 1's lost count to 6
        setActor2DiscsLost(0); // same thing but for actor 2
    }

    /**
     * swap the current actor with the other actor
     */
    public void swapActor() {
        this.currentActor = currentActor == actor1Colour ? actor2Colour : actor1Colour; // pretty obvious
    }

    /**
     * Resets the game
     */
    public void reset() {
        boardModel.reset(); // clear the board
        initData(); // set the data to the initial data values
    }

    /**
     * Whole bunch of getters and setters, standard issue, nothing particularly special below
     */

    public BoardModel getBoardModel() {
        return boardModel;
    }

    public void setBoardModel(BoardModel boardModel) {
        this.boardModel = boardModel;
    }

    public Disc getActor1Colour() {
        return actor1Colour;
    }

    public void setActor1Colour(Disc actor1Colour) {
        this.actor1Colour = actor1Colour;
    }

    public Disc getActor2Colour() {
        return actor2Colour;
    }

    public void setActor2Colour(Disc actor2Colour) {
        this.actor2Colour = actor2Colour;
    }

    public int getActor1DiscsLeft() {
        return actor1DiscsLeft;
    }

    public void setActor1DiscsLeft(int actor1DiscsLeft) {
        this.actor1DiscsLeft = actor1DiscsLeft;
    }

    public int getActor2DiscsLeft() {
        return actor2DiscsLeft;
    }

    public void setActor2DiscsLeft(int actor2DiscsLeft) {
        this.actor2DiscsLeft = actor2DiscsLeft;
    }

    public int getActor1DiscsLost() {
        return actor1DiscsLost;
    }

    public void setActor1DiscsLost(int actor1DiscsLost) {
        this.actor1DiscsLost = actor1DiscsLost;
    }

    public int getActor2DiscsLost() {
        return actor2DiscsLost;
    }

    public void setActor2DiscsLost(int actor2DiscsLost) {
        this.actor2DiscsLost = actor2DiscsLost;
    }

    public Disc getCurrentActor() {
        return currentActor;
    }

    public void setCurrentActor(Disc currentActor) {
        this.currentActor = currentActor;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    /**
     * @return true if either actor lost due to losing too many discs
     */
    public boolean hasAnActorLost() {
        return actor1DiscsLost >= 4 || actor2DiscsLost >= 4; // ^
    }

    /**
     * The below methods are pretty obvious
     */

    public void incrementActor1DiscsLost() {
        actor1DiscsLost += 1;
    }

    public void incrementActor2DiscsLost() {
        actor2DiscsLost += 1;
    }

    public void decrementActor1DiscsLeft() {
        actor1DiscsLeft -= 1;
    }

    public void decrementActor2DiscsLeft() {
        actor2DiscsLeft -= 1;
    }

    public boolean isPVE() {
        return versusAI;
    }

    public void setPVE(boolean pve) {
        this.versusAI = pve;
    }
}
