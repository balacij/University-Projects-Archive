package controller;

import model.*;

import java.util.Random;

public class Bot implements Runnable {

    private final Controller controller;
    private final Thread thread;
    private final Random r;

    public Bot(Controller controller) {
        this.controller = controller;
        this.thread = new Thread(this);
        this.thread.setDaemon(true);
        this.r = new Random();
    }

    public void start() {
        if (!thread.isAlive())
            thread.start();
    }

    // todo: dont allow te user to make moves for the bot

    @Override
    public void run() {
        while (true) {
            GameModel gameModel = controller.getGameModel();
            if (gameModel.isPVE() && gameModel.getCurrentActor() == gameModel.getActor2Colour()) {
                BoardModel boardModel = controller.getGameModel().getBoardModel();
                Direction[] directions = Direction.values();
                switch (controller.getGameModel().getState()) {
                    case PLACE_DISC:
                        Depth clickDepth;
                        Direction clickDir;
                        while (true) {
                            clickDepth = r.nextBoolean() ? Depth.INNER : Depth.OUTER;
                            clickDir = directions[r.nextInt(directions.length)];

                            if (boardModel.isLocEmpty(clickDepth, clickDir)) {
                                controller.placeDiscAt(clickDepth, clickDir, gameModel.getActor2Colour());
                                break;
                            }
                        }
                        break;
                    case REMOVE_ENEMY_DISC:
                        for (Depth depth : Depth.values()) {
                            boolean stop = false;

                            for (Direction direction : directions) {
                                if (boardModel.getDiscAtLoc(depth, direction) == gameModel.getActor1Colour()) {
                                    controller.removeEnemyDiscAt(depth, direction);
                                    stop = true;
                                    break;
                                }
                            }

                            if (stop)
                                break;
                        }
                        break;
                    case MOVE_DISC:
                        Depth fromDepth = null, toDepth = null;
                        Direction fromDir = null, toDir = null;
                        boolean foundValid = false;
                        Disc botColour = gameModel.getActor2Colour();
                        while (!foundValid) {
                            fromDepth = r.nextBoolean() ? Depth.INNER : Depth.OUTER;
                            fromDir = directions[r.nextInt(directions.length)];

                            if (boardModel.getDiscAtLoc(fromDepth, fromDir) == botColour) {
                                Direction[] adjDir = fromDir.adjacentDirections();
                                for (Direction dir : adjDir) {
                                    if (boardModel.isValidMove(fromDepth, fromDir, fromDepth, dir)) {
                                        foundValid = true;
                                        toDepth = fromDepth;
                                        toDir = dir;
                                        break;
                                    }
                                }

                                if (!foundValid && fromDir.isPole()) {
                                    toDepth = fromDepth == Depth.INNER ? Depth.OUTER : Depth.INNER;
                                    toDir = fromDir;

                                    if (boardModel.isValidMove(fromDepth, fromDir, toDepth, toDir)) {
                                        foundValid = true;
                                    }
                                }
                            }
                        }

                        controller.moveDiscTo(fromDepth, fromDir, toDepth, toDir);
                        break;
                    case END:
                        break;
                }

                if (gameModel.getState() != State.REMOVE_ENEMY_DISC)
                    gameModel.setCurrentActor(gameModel.getActor1Colour());

                controller.update();
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
