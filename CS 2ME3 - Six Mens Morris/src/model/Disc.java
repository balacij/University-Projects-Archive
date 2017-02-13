package model; /**
 * @author: Jason Balaci
 */

import java.awt.*;

public enum Disc {

    RED(Color.RED), // represents the RED discs
    BLUE(Color.BLUE), // represents the BLUE discs
    EMPTY(Color.BLACK); // represents the uncoloured discs

    private final Color colour; // each model.Disc must have a Colour

    Disc(Color colour) { // constructor
        this.colour = colour; // init value
    }

    public Color getColour() {
        return this.colour; // return the colour of the disc
    }

}
