package controller;

import com.google.gson.*;
import gui.BoardPanel;
import gui.BoardView;
import gui.DiscClickListener;
import model.*;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: Jason Balaci
 */
public class ControllerImpl implements DiscClickListener, Controller {

    private final Gson gson; // gson object, used for serializing and deserializing GameModel and all its components
    private final BoardView boardView; // board view object
    private final Bot bot;
    private GameModel gameModel; // gamemodel object

    private Depth move_lastClickDepth; // variable used specifically during move phase, used to cache origin of disc being moved
    private Direction move_lastClickDirection; // ^

    private boolean versusAI;

    /**
     * Constructor
     * <p>
     * Initialize all variables and randomly choose the first player.
     */
    public ControllerImpl() {
        // create the gson instance, enable pretty printing, complex map key serialization, excluding non-exposed fields,
        // and registering 3 type adapters for BoardModel.class since it has some special inner-workings (HashMap inside
        // of another HashMap)
        this.gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .registerTypeAdapter(BoardModel.class, (InstanceCreator<BoardModel>) type -> new BoardModel())
                .registerTypeAdapter(BoardModel.class, (JsonSerializer<BoardModel>) (src, typeOfSrc, context) -> {
                    JsonObject jsonObject = new JsonObject(); // create empty Jsonobject
                    for (Depth depth : Depth.values()) { // iterate over directions
                        JsonObject depthObject = new JsonObject(); // create empty depth object
                        jsonObject.add(depth.name(), depthObject); // add the depthobject to the jsonobject
                        for (Direction dir : Direction.values()) { // iterate over the directions
                            depthObject.addProperty(dir.name(), src.getDiscAtLoc(depth, dir).name()); // add each direction to the depthObject
                        }
                    }
                    return jsonObject;
                })
                .registerTypeAdapter(BoardModel.class, (JsonDeserializer<BoardModel>) (json, typeOfT, context) -> {
                    BoardModel boardModel = new BoardModel(); // create empty board model
                    JsonObject boardObject = json.getAsJsonObject(); // retrieve data as jsonObject
                    for (Depth depth : Depth.values()) { // iterate over the depths
                        JsonObject depthObject = boardObject.getAsJsonObject(depth.name()); // get the directions as a jsonobject
                        for (Direction dir : Direction.values()) { // iterate over the directions
                            boardModel.setDiscAt(depth, dir, Disc.valueOf(depthObject.get(dir.name()).getAsString())); // place each disc where it was stored
                        }
                    }
                    return boardModel;
                })
                .create();
        this.bot = new Bot(this);
        this.gameModel = new GameModel(); // create a new game model
        this.boardView = new BoardView(this); // instantiate the board view
        this.boardView.getBoardPanel().registerDiscClickListener(this); // register this instance as a gui.DiscClickListener for the board panel
        update();
    }

    /**
     * Implements Controller.placeDiscAt
     */
    @Override
    public void placeDiscAt(Depth depth, Direction dir, Disc disc) {
        if (gameModel.getState() == State.PLACE_DISC) { // ensure the state of the game is in the PLACE_DISC phase
            BoardModel boardModel = gameModel.getBoardModel(); // board model
            if (boardModel.isLocEmpty(depth, dir)) { // make sure the disc loc is empty
                boardModel.setDiscAt(depth, dir, disc); // set the disc in the boardModel
                boardView.getBoardPanel().placeDiscAt(depth, dir, disc); // place the disc in the view

                if (gameModel.getCurrentActor() == gameModel.getActor1Colour()) // remove 1 from the corresponding actor's disc placeables cache
                    gameModel.decrementActor1DiscsLeft(); // ^
                else // ^
                    gameModel.decrementActor2DiscsLeft(); // ^

                if (boardModel.isDiscInMill(depth, dir)) { // if the placed disc is in a mill, then set phase to REMOVE_ENEMY_DISC
                    gameModel.setState(State.REMOVE_ENEMY_DISC); // ^
                } else { // otherwise
                    if (gameModel.getActor1DiscsLeft() == 0 && gameModel.getActor2DiscsLeft() == 0) {// but if both players dont have anymore placeable discs
                        if (canOpponentMakeAnyMoves()) { // if the opponent can make moves, then
                            gameModel.setState(State.MOVE_DISC); // set the phase to MOVE_DISC
                        } else {
                            lossByTrap(); // otherwise, the opponent has lost because they cant move anywhere
                        }
                    }

                    if (gameModel.getState() != State.END) // if lossByTrap occurs, state is set to end, in which case, we shouldnt swapActors so as to leave current actor as the winner
                        gameModel.swapActor(); // swap actors
                }
            }
        }
    }

    /**
     * Called whenever a loss by trap occurs
     */
    private void lossByTrap() {
        gameModel.setState(State.END); // set the state to end
        if (gameModel.getActor1Colour() == gameModel.getCurrentActor()) { // if the current actor is actor 1
            gameModel.setActor2DiscsLost(6); // set actor 2's discs lost to 6
        } else {
            gameModel.setActor1DiscsLost(6); // otherwise do the same for actor 1
        }
        boardView.update(this); // update the board
        JOptionPane.showMessageDialog(null, gameModel.getCurrentActor().name() + " WINS BY TRAPPING ENEMY DISCS!", "Game End", JOptionPane.INFORMATION_MESSAGE); // notify the users that someone won
    }

    /**
     * @return true if the opponent has any valid moves they can make else false
     */
    private boolean canOpponentMakeAnyMoves() {
        BoardModel model = gameModel.getBoardModel(); // get the boardmodel
        Disc opponent = gameModel.getCurrentActor() == Disc.RED ? Disc.BLUE : Disc.RED; // determine who the enemy is
        for (Depth baseDepth : Depth.values()) { // iterate over the depths
            for (Direction baseDir : Direction.values()) { // iterate over the directions
                if (model.getDiscAtLoc(baseDepth, baseDir) == opponent) { // get the disc at each loc, check if its the opponent's
                    boolean moveable = false; // create a boolean called moveable
                    if (baseDir.isPole()) { // if the dir is a Pole, then it might be possible to move to the other depth
                        moveable = model.getDiscAtLoc(baseDepth == Depth.INNER ? Depth.OUTER : Depth.INNER, baseDir) == Disc.EMPTY; // ^
                    }
                    Direction[] adjacent = baseDir.adjacentDirections(); // get the adjacent directions
                    // moveable = moveable or whether or not the disc can move to one of the adjacent directions on the same depth
                    moveable = moveable
                            || model.isValidMove(baseDepth, baseDir, baseDepth, adjacent[0])
                            || model.isValidMove(baseDepth, baseDir, baseDepth, adjacent[1]);
                    if (moveable) // only if moveable is true, do we return true
                        return true; // because if moveable is not true, we should continue iterating and make sure we've gone over all possible positions
                }
            }
        }
        return false; // return false since we checked all possible positions
    }

    /**
     * Implements what occurs during the REMOVE_ENEMY_DISC phase
     *
     * @param depth the depth of the disc to be removed
     * @param dir   the direction of the disc to be removed
     */
    @Override
    public void removeEnemyDiscAt(Depth depth, Direction dir) {
        if (gameModel.getState() == State.REMOVE_ENEMY_DISC) {
            BoardModel boardModel = gameModel.getBoardModel(); // get the board model
            if (boardModel.getDiscAtLoc(depth, dir) != gameModel.getCurrentActor() && !boardModel.isLocEmpty(depth, dir)) {
                boardModel.removeDiscAt(depth, dir); // remove the disc from the board model
                boardView.getBoardPanel().removeDiscAt(depth, dir); // remove the disc from the view

                // increments the disc loss count of the owner of the disc
                if (gameModel.getCurrentActor() == gameModel.getActor1Colour())
                    gameModel.incrementActor2DiscsLost();
                else
                    gameModel.incrementActor1DiscsLost();

                if (hasAnActorLost()) { // if either actor lost
                    gameModel.setState(State.END); // set the state to end
                    JOptionPane.showMessageDialog(null, gameModel.getCurrentActor().name() + " WINS BY REMOVING 4 ENEMY DISCS!", "Game End", JOptionPane.INFORMATION_MESSAGE); // notify the users that someone won
                } else if (!canOpponentMakeAnyMoves()) { // if someone can no longer make any moves
                    lossByTrap(); // loss by trap occurs
                } else { // otherwise the game continues
                    gameModel.setState(gameModel.getActor1DiscsLeft() == 0 && gameModel.getActor2DiscsLeft() == 0 ? State.MOVE_DISC : State.PLACE_DISC); // set the state intelligently to either MOVE or PLACE
                    gameModel.swapActor(); // swap actors
                }
            }
        }
    }

    @Override
    public void moveDiscTo(Depth fromDepth, Direction fromDir, Depth toDepth, Direction toDir) {
        if (gameModel.getState() == State.MOVE_DISC) { // make sure we are in the right stage
            BoardModel boardModel = gameModel.getBoardModel();
            // make sure the new loc is empty and the old loc was occupied by the current actor's disc
            if (boardModel.isLocEmpty(toDepth, toDir) && !boardModel.isLocEmpty(fromDepth, fromDir) && boardModel.getDiscAtLoc(fromDepth, fromDir) == gameModel.getCurrentActor()) {
                boardModel.removeDiscAt(fromDepth, fromDir); // remove the old location in the boardModel
                boardView.getBoardPanel().removeDiscAt(fromDepth, fromDir); // ^ in the view
                boardModel.setDiscAt(toDepth, toDir, gameModel.getCurrentActor()); // set the new location in the boardModel
                boardView.getBoardPanel().placeDiscAt(toDepth, toDir, gameModel.getCurrentActor()); // ^ in the view

                if (boardModel.isDiscInMill(toDepth, toDir)) { // if the placed disc creates a mill
                    gameModel.setState(State.REMOVE_ENEMY_DISC); // set the state to REMOVE_ENEMY_DISC
                } else if (canOpponentMakeAnyMoves()) { // otherwise, if the opponent can make moves
                    gameModel.swapActor(); // swap actors
                } else {
                    lossByTrap(); // otherwise, the opponent just lost due to being trapped
                }
            }
        }
    }

    @Override
    public void restart() {
        gameModel.reset();
        boardView.getBoardPanel().reset();
        update();
    }

    /**
     * Implements restartPVP, restarts the game
     */
    @Override
    public void restartPVP() {
        restart();
        this.getGameModel().setPVE(false);
    }

    @Override
    public void restartPVE() {
        restart();
        this.getGameModel().setPVE(true);
        bot.start();
    }

    /**
     * Implements sameGameTo
     *
     * @param file the target file
     */
    @Override
    public void saveGameTo(File file) {
        try {
            FileWriter writer = new FileWriter(file); // create a file writer
            gson.toJson(gameModel, writer); // write the json data to the writer
            writer.close(); // close the writer
        } catch (IOException e) { // catch all exceptions
            e.printStackTrace(); // and just print them how they come
        }
    }

    /**
     * Implements loadGameFrom
     *
     * @param file the file containing the stored game data.
     */
    @Override
    public void loadGameFrom(File file) {
        try {
            FileReader reader = new FileReader(file); // create a file reader
            gameModel = gson.fromJson(reader, GameModel.class); // parse the gamemodel
            if (gameModel.isPVE())
                bot.start();
            reader.close(); // close the reader
            BoardModel boardModel = gameModel.getBoardModel(); // get the board model
            BoardPanel boardPanel = boardView.getBoardPanel(); // get the board panel
            boardPanel.reset(); // reset the board panel
            for (Depth depth : Depth.values()) { // iterate over the depths
                for (Direction dir : Direction.values()) { // iterate over the directions
                    Disc atPos = boardModel.getDiscAtLoc(depth, dir); // get the disc at a particular board model location (from the stored data)
                    if (atPos != Disc.EMPTY) { // if it isnt empty, then
                        boardPanel.placeDiscAt(depth, dir, atPos); // place a disc of the same colour onto the board panel
                    }
                }
            }
        } catch (Exception e) { // catch all exceptions
            e.printStackTrace(); // print them as they come
        }
        update();
    }

    /**
     * @return true if either actor lost due to too many discs lost
     */
    private boolean hasAnActorLost() {
        return gameModel.hasAnActorLost(); // ^
    }

    /**
     * Implement the onClick method from the gui.DiscClickListener interface
     *
     * @param depth     clicked disc's depth
     * @param direction clicked disc's direction
     */
    @Override
    public void onClick(Depth depth, Direction direction) {
        if (gameModel.isPVE() && gameModel.getCurrentActor() == gameModel.getActor2Colour())
            return;

        switch (gameModel.getState()) {
            case PLACE_DISC: // if in PLACE_DISC phase
                placeDiscAt(depth, direction, gameModel.getCurrentActor()); // do place_disc phase stuff
                break; // no more
            case REMOVE_ENEMY_DISC: // if in REMOVE_ENEMY_DISC phase
                removeEnemyDiscAt(depth, direction); // do remove_enemy_disc phase
                break; // no more
            case MOVE_DISC: // not implemented yet
                if (gameModel.getBoardModel().getDiscAtLoc(depth, direction) == gameModel.getCurrentActor()) { //if the clicked disc is of the current actor's
                    move_lastClickDepth = depth; // cache it because its the disc that they want to move
                    move_lastClickDirection = direction; // ^
                } else if (gameModel.getBoardModel().getDiscAtLoc(depth, direction) == Disc.EMPTY && move_lastClickDepth != null && move_lastClickDirection != null) { // if the cached disc exists and the desired target location is empty, then
                    if (gameModel.getBoardModel().isValidMove(move_lastClickDepth, move_lastClickDirection, depth, direction)) { // if its a valid move
                        moveDiscTo(move_lastClickDepth, move_lastClickDirection, depth, direction); // move the cached disc to the new location
                        move_lastClickDepth = null; // remove it from the cache
                        move_lastClickDirection = null; // ^
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid movement.", "Error", JOptionPane.ERROR_MESSAGE); // tell the user that we wont let them pull a fast one on us nor fly
                    }
                } else {
                    JOptionPane.showMessageDialog(null, move_lastClickDepth == null ? "Please choose one of your discs to move." : "An enemy disc occupies that location.", "Error", JOptionPane.ERROR_MESSAGE); //tell the user the appropriate error message
                }
                break; // no more
            case END: // nothing to do
                break; // no more
        }
        update();
    }

    @Override
    public void update() {
        boardView.update(this);
    }

    /**
     * Implements getWinner
     *
     * @return winner's disc colour
     */
    @Override
    public Disc getWinner() {
        return gameModel.getCurrentActor();
    }

    /**
     * Implements getGameModel
     *
     * @return the currently used game model
     */
    @Override
    public GameModel getGameModel() {
        return gameModel;
    }

}