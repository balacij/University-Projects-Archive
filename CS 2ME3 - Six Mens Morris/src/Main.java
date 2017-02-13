import controller.ControllerImpl;

import javax.swing.*;

/**
 * @author: Jason Balaci
 */
public class Main {

    /**
     * main method that java requires to run programs.
     * <p>
     * This is solely used to create an instance of the controller.ControllerImpl which will
     * take all the real actions to run the program.
     *
     * @param args default required param
     */
    public static void main(String[] args) {
        try {
            // make the UI look pretty
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new ControllerImpl(); // create the game controller and let it do the rest
    }

}
