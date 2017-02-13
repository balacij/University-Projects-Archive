package model; /**
 * @author: Jason Balaci
 */

import static java.lang.Math.PI;

public enum Direction {

    EAST(0, true), // represents the EAST angle
    NORTH_EAST(PI / 4, false), // ^ NE
    NORTH(PI / 2, true), // N
    NORTH_WEST(3 * PI / 4, false), // NW
    WEST(PI, true), // W
    SOUTH_WEST(5 * PI / 4, false), // SW
    SOUTH(3 * PI / 2, true), // S
    SOUTH_EAST(7 * PI / 4, false); // SE

    private final double theta; // each model.Direction has an angle off the pos x-axis (in dir of the y-axis)
    private final boolean pole; // each model.Direction is either a pole (/dominant direction) or composed of them
    private Direction[] adjacent = null; // variable containing this direction's adjacent directions

    Direction(double theta, boolean pole) { // create constructor
        this.theta = theta; // init values
        this.pole = pole; // ^
    }

    public double toAngle() {
        return theta; // return the angle off the pos x-axis (in dir of the y-axis)
    }

    public boolean isPole() {
        return this.pole; // return whether the model.Direction is dominant/a pole
    }

    /**
     * Determines if the direction is adjacent to this one
     *
     * @param dir the target
     * @return if dir is adjacent
     */
    public boolean isAdjacentTo(Direction dir) {
        if (dir == this) // if dir is this direction
            return false; // return false

        // determine if the direction is adjacent to this one through math, except for EASTERLY  directions
        boolean allow = (this == EAST && dir == SOUTH_EAST) || (this == SOUTH_EAST && dir == EAST);
        return allow || Math.abs(dir.toAngle() - this.toAngle()) <= ((PI / 4) + 0.1);
    }

    /**
     * Get the adjacent directions
     *
     * @return the adjacent directions
     */
    public Direction[] adjacentDirections() {
        if (this.adjacent != null) // if the adjacent directions were cached
            return adjacent; // return the cache

        Direction[] directions = new Direction[2]; // create an empty direction array
        Direction[] possible = Direction.values(); // get all possible directions
        int i = 0;
        for (int j = 0; j < possible.length && i < directions.length; j++) { // iterate over all needed possible directions
            if (this.isAdjacentTo(possible[j])) { // if this direction is adjacent to that one
                directions[i] = possible[j]; // add it to the directions
                i++; // increment i
            }
        }

        this.adjacent = directions; // cache the found adjacent directions
        return directions; // return the directions
    }

}
