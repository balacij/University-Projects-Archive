package gui;

import model.Depth;
import model.Direction;
import model.Disc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Jason Balaci
 */
public class BoardPanel extends JPanel implements MouseListener {

    // a ton of constants used to easily make the UI look smaller, larger, prettier, or whatever we need
    private final int XPAD = 40, // padding on the x axis
            YPAD = 40, // padding on the y axis
            BOARDLENGTH = 480, // length of the board
            HALFBOARDLENGTH = BOARDLENGTH / 2, // half ^
            QUARTERBOARDLENGTH = BOARDLENGTH / 4, // half ^
            THREEQUARTERBOARDLENGTH = QUARTERBOARDLENGTH * 3, // ^*3
            XORIGIN = HALFBOARDLENGTH + XPAD, // the middle of the board (XORIGIN, YORIGIN)
            YORIGIN = HALFBOARDLENGTH + YPAD, // ^
            EMPTYCIRCLELENGTH = 10, // diameter of an empty disc loc
            EMPTYCIRCLEHALFLENGTH = EMPTYCIRCLELENGTH / 2, // half ^
            FILLEDCIRCLELENGTH = BOARDLENGTH / 18, // diameter of a coloured disc loc
            HALFFILLEDCIRCLELENGTH = FILLEDCIRCLELENGTH / 2; // half ^

    private final double PI = Math.PI, EIGTHPI = PI / 8; // just some useful angles
    private final int OUTERPOLEHYP = HALFBOARDLENGTH, // length of hypotenuse required for outer pole from origin
            INNERPOLEHYP = QUARTERBOARDLENGTH, // ^ but for inner pole
            INNERANGLEDHYP = (int) Math.sqrt(2 * Math.pow(QUARTERBOARDLENGTH, 2)), // ^ same logic, but for outer angled directions
            OUTERANGLEDHYP = INNERANGLEDHYP * 2, // ^ but for inner angled directions
            DISCSTRENGTH = 30; // the 'anchor factor' of each disc

    private final List<DiscClickListener> discClickListeners; // a list of DiscClickListeners to be called at each valid click

    private final Map<Point, Disc> pointDiscMap; // a map containing a Point on the board and the corresponding Colour

    public BoardPanel() {
        Dimension size = new Dimension(BOARDLENGTH + 2 * XPAD, BOARDLENGTH + 2 * YPAD); // determine the size of the board
        setPreferredSize(size); // set preferred size
        setMaximumSize(size); // set max size
        setMinimumSize(size); // set min size
        setSize(size); // force size
        addMouseListener(this); // add this instance mouse listener ( to capture mouse clicks )
        this.pointDiscMap = new HashMap<>(); // instantiate with empty map
        this.discClickListeners = new ArrayList<>(); // instantiate with empty arraylist
    }

    public void paintComponent(Graphics g) { // override paintComponent method
        super.paintComponent(g);
        g.setColor(Color.WHITE); // set colour to white
        g.fillRect(0, 0, XPAD * 2 + BOARDLENGTH, YPAD * 2 + BOARDLENGTH); // colour the background
        g.setColor(Color.BLACK); // set colour to black
        drawBoxes(g); // draw the boxes
        drawDots(g); // draw the empty discs
        drawPoints(g); // draw any available coloured discs
    }

    private void drawPoints(Graphics g) { // method definition to draw all coloured discs
        for (Map.Entry<Point, Disc> pointDiscEntry : pointDiscMap.entrySet()) { // iterate over all Entry<Point, model.Disc> from pointDiscMap
            g.setColor(pointDiscEntry.getValue().getColour()); // set the Colour to the model.Disc Colour
            Point point = pointDiscEntry.getKey(); // get the Point
            g.fillOval(point.x - HALFFILLEDCIRCLELENGTH, point.y - HALFFILLEDCIRCLELENGTH, FILLEDCIRCLELENGTH, FILLEDCIRCLELENGTH); // paint a Circle at ^
        }
    }

    private void drawBoxes(Graphics g) { // method definition to draw the board
        g.drawRect(XPAD, YPAD, BOARDLENGTH, BOARDLENGTH); // draw the outermost board
        g.drawRect(XPAD + QUARTERBOARDLENGTH, YPAD + QUARTERBOARDLENGTH, HALFBOARDLENGTH, HALFBOARDLENGTH); // draw the inner board
        g.drawLine(XPAD + HALFBOARDLENGTH, YPAD, XPAD + HALFBOARDLENGTH, YPAD + QUARTERBOARDLENGTH); // draw the lines connecting the inner and outer boards
        g.drawLine(XPAD + HALFBOARDLENGTH, YPAD + THREEQUARTERBOARDLENGTH, XPAD + HALFBOARDLENGTH, YPAD + BOARDLENGTH); // ^
        g.drawLine(XPAD, YPAD + HALFBOARDLENGTH, XPAD + QUARTERBOARDLENGTH, YPAD + HALFBOARDLENGTH); // ^
        g.drawLine(XPAD + THREEQUARTERBOARDLENGTH, YPAD + HALFBOARDLENGTH, XPAD + BOARDLENGTH, YPAD + HALFBOARDLENGTH); // ^
    }

    private void drawDots(Graphics g) { // draw the empty dots
        int xpad2 = XPAD - EMPTYCIRCLEHALFLENGTH, ypad2 = YPAD - EMPTYCIRCLEHALFLENGTH; // get the offset padding values

        // draw the dots on the...

        // top row
        g.fillOval(xpad2, ypad2, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
        g.fillOval(xpad2 + HALFBOARDLENGTH, ypad2, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
        g.fillOval(xpad2 + BOARDLENGTH, ypad2, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);

        // bottom row
        g.fillOval(xpad2, ypad2 + BOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
        g.fillOval(xpad2 + HALFBOARDLENGTH, ypad2 + BOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
        g.fillOval(xpad2 + BOARDLENGTH, ypad2 + BOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);

        // mid row
        g.fillOval(xpad2, ypad2 + HALFBOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
        g.fillOval(xpad2 + QUARTERBOARDLENGTH, ypad2 + HALFBOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
        g.fillOval(xpad2 + THREEQUARTERBOARDLENGTH, ypad2 + HALFBOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
        g.fillOval(xpad2 + BOARDLENGTH, ypad2 + HALFBOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);

        // upper mid row
        g.fillOval(xpad2 + QUARTERBOARDLENGTH, ypad2 + QUARTERBOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
        g.fillOval(xpad2 + HALFBOARDLENGTH, ypad2 + QUARTERBOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
        g.fillOval(xpad2 + THREEQUARTERBOARDLENGTH, ypad2 + QUARTERBOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);

        // lower mid row
        g.fillOval(xpad2 + QUARTERBOARDLENGTH, ypad2 + THREEQUARTERBOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
        g.fillOval(xpad2 + HALFBOARDLENGTH, ypad2 + THREEQUARTERBOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
        g.fillOval(xpad2 + THREEQUARTERBOARDLENGTH, ypad2 + THREEQUARTERBOARDLENGTH, EMPTYCIRCLELENGTH, EMPTYCIRCLELENGTH);
    }

    public Point toPoint(Depth depth, Direction direction) { // math function to convert model.Disc Location to a point on the board
        int x, y; // the point's x,y coordinates
        int delta = depth == Depth.INNER ? QUARTERBOARDLENGTH : HALFBOARDLENGTH; // if delta is the hypotenuse length from origin (create an imaginary right angled triangle using the model.Disc Location and the depth)
        delta = direction.isPole() ? delta : (int) Math.sqrt(Math.pow(delta, 2) * 2); // if the direction isnt a pole, then delta is wrong and should be a hypotenuse calculated the same way as regular right angled triangle
        x = (int) (XORIGIN + Math.cos(direction.toAngle()) * delta); // apply SOHCAHTOA isolating for x
        y = (int) (YORIGIN - Math.sin(direction.toAngle()) * delta); // ^ for y
        return new Point(x, y); // return a new instance of Point using x,y
    }

    public void placeDiscAt(Depth depth, Direction direction, Disc colour) { // place a disc a particular location
        this.pointDiscMap.put(toPoint(depth, direction), colour); // put a disc at a location
        repaint(); // repaint this component
    }

    public void removeDiscAt(Depth depth, Direction direction) { // remove a disc from a particular location
        this.pointDiscMap.remove(toPoint(depth, direction)); // ^
        repaint(); // repaint this component
    }

    @Override
    public void mouseClicked(MouseEvent e) { // override the mouseClicked method to handle mouse clicks on the board
        int x = e.getX() - XORIGIN; // get the offset x coord using the middle of the board as the new origin of an imaginary new xy-plane
        int y = (-1) * (e.getY() - YORIGIN); // ^ get the offset y....

        double hyp = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); // get the length of the hypotenuse of right angled triangle formed with the point and the origin
        double theta = Math.acos(x / hyp); // calculate the angle from the positive x-axis in the direction of the positive y-axis
        if (y < 0) // acos is only defined on [-1,1] -> [0,PI], so we need to fix that so we get real mathematical angle
            theta = 2 * Math.PI - theta; // ^

        Direction dir = null; // create dir
        Depth depth = null; // create depth

        for (Direction poss : Direction.values()) { // iterate over all possible directions
            if (theta < poss.toAngle() + EIGTHPI) { // if the theta is less than the possible direction + EIGTHPI (halfway between each major direction)
                dir = poss; // then it is the correct possibility
                break; // stop iterating, we found the right one
            }
        }
        if (dir == null) // if the dir is still null, then it must have been eastward
            dir = Direction.EAST; // ^

        if (dir.isPole()) { // if the direction is a pole
            if (inrangeof(hyp, INNERPOLEHYP, DISCSTRENGTH)) // make sure the hyp is in the right area
                depth = Depth.INNER; // then its INNER
            else if (inrangeof(hyp, OUTERPOLEHYP, DISCSTRENGTH)) // make sure the hyp is in the right area
                depth = Depth.OUTER; // then its OUTER
        } else if (inrangeof(hyp, INNERANGLEDHYP, DISCSTRENGTH)) { // make sure the hyp is in the right area
            depth = Depth.INNER; // then its INNER
        } else if (inrangeof(hyp, OUTERANGLEDHYP, DISCSTRENGTH)) { // make sure the hyp is in the right area
            depth = Depth.OUTER; // then its OUTER
        }

        if (depth == null) // if a depth couldnt be found, then the mouse click is invalid, dont do anything
            return; // return, dont do anything else

        for (DiscClickListener dcl : discClickListeners) { // iterate over the disc click listeners
            dcl.onClick(depth, dir); // calling each one with the clicked disc location
        }
    }

    /**
     * @param x
     * @param target
     * @param epsilon
     * @return true if x is within epsilon of target
     */
    private boolean inrangeof(double x, double target, double epsilon) {
        return Math.abs(target - x) < epsilon; // |target - x| < epsilon ???
    }

    public void mousePressed(MouseEvent e) {
    } // empty methods required to be overriden for this class to be non-abstract to implement MouseListener

    public void mouseReleased(MouseEvent e) {
    }  // ^

    public void mouseEntered(MouseEvent e) {
    } // ^

    public void mouseExited(MouseEvent e) {
    } // ^

    /**
     * Basic method to register a gui.DiscClickListener
     *
     * @param listener
     */
    public void registerDiscClickListener(DiscClickListener listener) {
        this.discClickListeners.add(listener); // add the listener to the registry
    }

    public void reset() {
        pointDiscMap.clear();
    }

}
