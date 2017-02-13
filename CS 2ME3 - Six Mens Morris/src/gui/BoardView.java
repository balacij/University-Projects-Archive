package gui;

import controller.Controller;
import model.GameModel;
import model.State;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author: Jason Balaci
 */
public class BoardView {

    private final JFrame frame;

    private final JPanel currentActorPanel, actor1ColourPanel, actor2ColourPanel;
    private final JLabel stateLabel, statusLabel, actor1DiscsLeftLabel, actor2DiscsLeftLabel, actor1DiscsLostLabel, actor2DiscsLostLabel;
    private final BoardPanel boardPanel; // boardPanel object

    public BoardView(Controller controller) { // constructor
        frame = new JFrame(); // create a jframe instance
        frame.setResizable(false); // dont allow resizing
        frame.setTitle("Six Men's Morris"); // set the title
        frame.setLayout(new GridLayout(1, 2)); // set the layout to |Component|Component| grid layout
        boardPanel = new BoardPanel(); // instantiate board panel
        frame.add(boardPanel); // add the board
        JPanel rightSidePanel = new JPanel(); // create a Jpanel containing everything on the right panel
        rightSidePanel.setLayout(new GridLayout(2, 1)); // set its layout

        JPanel statusPanel = new JPanel(new GridLayout(9, 2)); // create a status panel
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status")); // add a titled border to the status panel
        statusPanel.add(new JLabel("Status:")); //  add a label
        statusPanel.add(statusLabel = new JLabel("Starting")); //  add a label
        statusPanel.add(new JLabel("State:")); //  add a label
        statusPanel.add(stateLabel = new JLabel("")); //  add a label
        currentActorPanel = new JPanel(); // create a Panel containing the colour of the current actor
        currentActorPanel.setBackground(Color.BLACK); // ^
        statusPanel.add(new JLabel("Actor 1:")); //  add a label
        actor1ColourPanel = new JPanel(); // create a coloured panel for actor 1
        actor1ColourPanel.setBackground(controller.getGameModel().getActor1Colour().getColour()); // ^
        statusPanel.add(actor1ColourPanel); // ^
        statusPanel.add(new JLabel("Actor 2:")); //  add a label
        actor2ColourPanel = new JPanel(); // create a coloured panel for actor 2
        actor2ColourPanel.setBackground(controller.getGameModel().getActor2Colour().getColour()); // ^
        statusPanel.add(actor2ColourPanel); // ^
        statusPanel.add(new JLabel("Current Actor:"));//  add a label
        statusPanel.add(currentActorPanel); // add the current actor panel to the status panel
        statusPanel.add(new JLabel("Actor 1 Placeables Left:"));//  add a label
        statusPanel.add(actor1DiscsLeftLabel = new JLabel(""));//  add a label
        statusPanel.add(new JLabel("Actor 2 Placeables Left:"));//  add a label
        statusPanel.add(actor2DiscsLeftLabel = new JLabel(""));//  add a label
        statusPanel.add(new JLabel("Actor 1 Discs Lost:"));//  add a label
        statusPanel.add(actor1DiscsLostLabel = new JLabel(""));//  add a label
        statusPanel.add(new JLabel("Actor 2 Discs Lost:"));//  add a label
        statusPanel.add(actor2DiscsLostLabel = new JLabel(""));//  add a label
        rightSidePanel.add(statusPanel); // add the status panel to the right side pane;

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1)); // create an options panel
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options")); // add a border to the options panel
        // create the buttons
        JButton newGameButton = new JButton("New Game (1v1)"), newGameAgainstAIButton = new JButton("New Game (1vAI)"), saveButton = new JButton("Save"), loadButton = new JButton("Load");
        newGameButton.addActionListener(l -> {
            controller.restartPVP();
            forceRedraw();
        }); // make the new game button restartPVP the board
        // make the new game against AI button tell the user it doesnt do anything yet
        newGameAgainstAIButton.addActionListener(l -> {
            controller.restartPVE();
            forceRedraw();
        });
        saveButton.addActionListener(l -> {
            /*JFileChooser jfc = new JFileChooser("Choose a file to save to."); //Make the user choose a file to save the data to
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY); // ^
            jfc.setAcceptAllFileFilterUsed(false); // ^
            jfc.setFileFilter(new FileNameExtensionFilter(".txt", ".txt")); // ^
            jfc.addChoosableFileFilter(new FileNameExtensionFilter(".txt", "txt")); // ^
            int code = jfc.showOpenDialog(frame); // get the return code
            if (code == JFileChooser.APPROVE_OPTION) { // if its Approve
                File file = jfc.getSelectedFile(); // get the chosen file
                controller.saveGameTo(file); // save the data to this file
            }*/

            FileDialog fd = new FileDialog(frame);
            fd.setFilenameFilter((dir, name) -> name.endsWith(".txt"));
            fd.setVisible(true);
            String filename = fd.getFile();
            if (filename != null) {
                File file = new File(fd.getDirectory(), fd.getFile()); // get the chosen file
                controller.saveGameTo(file); // save the data to this file
            }
        });
        loadButton.addActionListener(l -> {
            FileDialog fd = new FileDialog(frame);
            fd.setFilenameFilter((dir, name) -> name.endsWith(".txt"));
            fd.setVisible(true);
            String filename = fd.getFile();
            if (filename != null) {
                File file = new File(fd.getDirectory(), fd.getFile()); // get the chosen file
                if (file.exists())
                    controller.loadGameFrom(file);
                else
                    JOptionPane.showMessageDialog(null, "File does not exist", "Load", JOptionPane.ERROR_MESSAGE);
            }
            /*
            JFileChooser jfc = new JFileChooser("Choose a file to load from.");//Make the user choose a file to save the data to
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY); // ^
            jfc.setAcceptAllFileFilterUsed(false); // ^
            jfc.setFileFilter(new FileNameExtensionFilter(".txt", ".txt")); // ^
            jfc.addChoosableFileFilter(new FileNameExtensionFilter(".txt", "txt")); // ^
            int code = jfc.showOpenDialog(frame); // get the return code
            if (code == JFileChooser.APPROVE_OPTION) { // if its Approve
                File file = jfc.getSelectedFile(); // get the chosen File
                if (file.exists()) // if the file exists
                    controller.loadGameFrom(file); // load the game from the file
                else // otherwise, tell the user that the file doesnt exist
                    JOptionPane.showMessageDialog(null, "File does not exist", "Load", JOptionPane.ERROR_MESSAGE);
            }*/
        });
        optionsPanel.add(newGameButton); // add the buttons
        optionsPanel.add(newGameAgainstAIButton); // ^
        optionsPanel.add(saveButton); // ^
        optionsPanel.add(loadButton); // ^
        rightSidePanel.add(optionsPanel); // add the options panel to the right side panel

        frame.add(rightSidePanel); // add the right side panel to the frame
        frame.pack(); // pack the frame
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // make the program dispose the Java program once the frame has been closed
        frame.setLocationRelativeTo(null); // move the frame to the middle of the screen
        frame.setVisible(true); // make the frame visible to the user
    }

    /**
     * @return the board panel instance
     */
    public BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    public void update(Controller controller) { // update the information displayed on the jframe
        GameModel model = controller.getGameModel(); // get the game model
        currentActorPanel.setBackground(model.getCurrentActor().getColour()); // set the current actor panel to the colour of the current actor
        actor1ColourPanel.setBackground(model.getActor1Colour().getColour()); // ^
        actor2ColourPanel.setBackground(model.getActor2Colour().getColour()); // ^

        String state = ""; // get a message explaing what the current stage is
        switch (model.getState()) { // ^
            case PLACE_DISC:
                state = "Place Disc"; // ^
                break;
            case REMOVE_ENEMY_DISC:
                state = "Remove Enemy Disc"; // ^
                break;
            case MOVE_DISC:
                state = "Move Disc"; // ^
                break;
            case END:
                state = "Game ended! " + controller.getWinner().name() + " wins!"; // ^
                break;
        }
        stateLabel.setText(state); // update the state text
        statusLabel.setText(controller.getGameModel().getState() == State.END ? "Game Ended" : "Game in Progress"); // update the game status message
        actor1DiscsLeftLabel.setText(String.valueOf(model.getActor1DiscsLeft())); // update the values displayed on the board
        actor2DiscsLeftLabel.setText(String.valueOf(model.getActor2DiscsLeft())); // ^
        actor1DiscsLostLabel.setText(String.valueOf(model.getActor1DiscsLost())); // ^
        actor2DiscsLostLabel.setText(String.valueOf(model.getActor2DiscsLost())); // ^
        //boardPanel.paintAll(boardPanel.getGraphics()); // force the boardPanel to be repainted immediately
        // ^ this line causes graphical glitches sometimes...leave it out... let the components be painted a bit slowly
    }

    /**
     * Forces the screen to be redrawn
     */
    private void forceRedraw() {
        boardPanel.paintAll(boardPanel.getGraphics()); // force the boardPanel to be repainted immediately
    }

}
