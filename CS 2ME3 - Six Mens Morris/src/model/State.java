package model;

/**
 * @author: Jason Balaci
 */
public enum State {

    PLACE_DISC, // players placing discs phase
    REMOVE_ENEMY_DISC, // actor removing enemy actor's disc phase
    MOVE_DISC, // actors moving discs phase
    END // end phase

}
