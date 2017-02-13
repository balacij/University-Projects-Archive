package controller;

import model.Depth;
import model.Direction;
import model.Disc;
import model.GameModel;

import java.io.File;

public interface Controller {

    /**
     * Logic behind PLACE_DISC state.
     *
     * @param depth depth of disc
     * @param dir   direction of disc
     * @param disc  disc colour
     */
    void placeDiscAt(Depth depth, Direction dir, Disc disc);

    /**
     * Performs the actions required for the removal of an enemy's disc phase.
     *
     * @param depth
     * @param dir
     */
    void removeEnemyDiscAt(Depth depth, Direction dir);

    /**
     * Performs the actions required for the moveDiscTo phase
     *
     * @param fromDepth disc being moved's depth
     * @param fromDir   disc being moved's direction
     * @param toDepth   target location's depth
     * @param toDir     target location's direction
     */
    void moveDiscTo(Depth fromDepth, Direction fromDir, Depth toDepth, Direction toDir);

    /**
     * Restarts the game
     */
    void restart();

    /**
     * Restarts the game against another play
     */
    void restartPVP();

    /**
     * Restarts the game against an AI
     */
    void restartPVE();

    /**
     * Saves the game state to a file.
     *
     * @param file the target file
     */
    void saveGameTo(File file);

    /**
     * Loads the game state from a file.
     *
     * @param file the file containing the stored game data.
     */
    void loadGameFrom(File file);

    /**
     * @return the winner of the game
     */
    Disc getWinner();

    /**
     * @return the game model instance
     */
    GameModel getGameModel();

    void update();
}
