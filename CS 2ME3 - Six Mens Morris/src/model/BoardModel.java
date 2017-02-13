package model;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

import static model.Direction.*;

public class BoardModel {

    // map containing all the discs with their locations on the board as the key
    @Expose
    private final Map<Depth, Map<Direction, Disc>> BOARD;

    /**
     * Constructor
     */
    public BoardModel() {
        BOARD = new HashMap<>(); // init BOARD with empty hashmap
        for (Depth depth : Depth.values()) { // ^
            HashMap<Direction, Disc> sub = new HashMap<>(); // ^
            BOARD.put(depth, sub); // ^
            for (Direction direction : Direction.values()) { // ^
                sub.put(direction, Disc.EMPTY); // ^ ... not really empty, but EMPTY ;)
            }
        }
    }

    /**
     * Determines if a particular location is EMPTY.
     *
     * @param depth
     * @param dir
     * @return true if the disc at this location is EMPTY (not coloured)
     */
    public boolean isLocEmpty(Depth depth, Direction dir) {
        return BOARD.get(depth).get(dir) == Disc.EMPTY; // check if the disc loc is EMPTY
    }

    /**
     * @param depth
     * @param dir
     * @return the model.Disc at a particular location
     */
    public Disc getDiscAtLoc(Depth depth, Direction dir) {
        return BOARD.get(depth).get(dir); // return the model.Disc at the particular loc
    }

    /**
     * Sets a model.Disc at a particular location
     *
     * @param depth - loc
     * @param dir   - loc
     * @param disc  - set disc
     */
    public void setDiscAt(Depth depth, Direction dir, Disc disc) {
        BOARD.get(depth).put(dir, disc); // put the disc in a particular location
    }

    /**
     * Removes a model.Disc from a particular location.
     *
     * @param depth
     * @param dir
     */
    public void removeDiscAt(Depth depth, Direction dir) {
        BOARD.get(depth).put(dir, Disc.EMPTY); // put an EMPTY model.Disc in a particular location
    }

    /**
     * Determines if a particular disc is in a Mill (/Morris... three in a straight line adjacent to each other)
     *
     * @param depth
     * @param dir
     * @return true if a particular model.Disc is in a mill
     */
    public boolean isDiscInMill(Depth depth, Direction dir) {
        Map<Direction, Disc> layer = BOARD.get(depth); // layer
        Disc req = layer.get(dir); // req
        switch (dir) { // switch model.Direction - dir
            // this logic is pretty easy
            // if the disc Loc is a pole, then check its adjacent locations
            // otherwise, get that angled directions composed Poles and
            // do the same
            case NORTH:
                return layer.get(NORTH_EAST) == req && layer.get(NORTH_WEST) == req;
            case EAST:
                return layer.get(NORTH_EAST) == req && layer.get(SOUTH_EAST) == req;
            case SOUTH:
                return layer.get(SOUTH_WEST) == req && layer.get(SOUTH_EAST) == req;
            case WEST:
                return layer.get(NORTH_WEST) == req && layer.get(SOUTH_WEST) == req;
            case NORTH_EAST:
                return isDiscInMill(depth, NORTH) || isDiscInMill(depth, EAST);
            case SOUTH_EAST:
                return isDiscInMill(depth, SOUTH) || isDiscInMill(depth, EAST);
            case NORTH_WEST:
                return isDiscInMill(depth, NORTH) || isDiscInMill(depth, WEST);
            case SOUTH_WEST:
                return isDiscInMill(depth, SOUTH) || isDiscInMill(depth, WEST);
            default:
                return false;
        }
    }

    /**
     * Determine if a particular disc can move to anothe rlocation
     *
     * @param fromDepth the base disc's depth
     * @param fromDir   the base disc's direction
     * @param toDepth   the target disc's depth
     * @param toDir     the target disc's direction
     * @return true if the move is possible
     */
    public boolean isValidMove(Depth fromDepth, Direction fromDir, Depth toDepth, Direction toDir) {
        if (getDiscAtLoc(toDepth, toDir) == Disc.EMPTY && getDiscAtLoc(fromDepth, fromDir) != Disc.EMPTY) {
            if (toDepth == fromDepth)
                return fromDir.isAdjacentTo(toDir);
            else
                return fromDir.isPole() && toDir.isPole() && fromDir == toDir;
        }
        return false;
    }

    /**
     * Resets the board.
     */
    public void reset() {
        for (Depth depth : Depth.values()) { // iterate over the model.Depth values
            Map<Direction, Disc> sub = BOARD.get(depth); // get the layer
            for (Direction direction : Direction.values()) { // iterate over the model.Direction values
                sub.put(direction, Disc.EMPTY); // force each possible location to be EMPTY
            }
        }
    }

}
