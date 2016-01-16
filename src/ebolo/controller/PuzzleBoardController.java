package ebolo.controller;

import ebolo.data.Settings;
import ebolo.data.TransitionData;
import ebolo.gui.PuzzleBoard;
import ebolo.gui.PuzzlePiece;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ebolo on 04-Jan-16.
 * This is the main Controlling system for the whole Application
 */
public class PuzzleBoardController {
    private final PuzzleBoard mBoard;
    private final Random mRandom;
    final private PuzzleBoardController thisCP;
    private PuzzlePiece[] puzzlePieces;
    private ArrayList<Integer[]> moveBackup;
    private TransitionData[] transitionDatas;
    private ArrayList<Integer> emptyIndexes;

    public PuzzleBoardController(PuzzleBoard puzzleBoard) {
        mBoard = puzzleBoard;
        thisCP = this;
        mRandom = new Random();
        initialize();
    }

    //Generate empty board
    @SuppressWarnings("unchecked") //Pretending all objects (if must) are loaded correctly
    private void initialize() {
        //Create empty lists
        moveBackup = new ArrayList<>();
        transitionDatas = new TransitionData[Settings.totalTileNumber];
        puzzlePieces = new PuzzlePiece[Settings.totalTileNumber];
        emptyIndexes = new ArrayList<>();

        for (int row = 0; row < Settings.tileNumber; row++) {
            for (int col = 0; col < Settings.tileNumber; col++) {
                int index = row * Settings.tileNumber + col;
                puzzlePieces[index] = new PuzzlePiece(index);
                mBoard.add(puzzlePieces[index], col, row, 1, 1);
                transitionDatas[index] = new TransitionData(puzzlePieces[index]);
                emptyIndexes.add(index);
            }
        }
        //Just pop, no need animation here
        if (!(new File(Settings.savePath)).isFile()) {
            popNewPiece();
            popNewPiece();
        } else {
            //If there exists a save Game, then load it
            try {
                ObjectInputStream saveFile = new ObjectInputStream(new FileInputStream(Settings.savePath));
                Integer[] savedGame = (Integer[]) saveFile.readObject();
                for (int counter = 0; counter < Settings.totalTileNumber; counter++) {
                    puzzlePieces[counter].setValue(savedGame[counter]);
                }
                moveBackup = (ArrayList<Integer[]>) saveFile.readObject();
                long score = (long) saveFile.readObject();
                long best = (long) saveFile.readObject();
                mBoard.getMainBoard().getActionMenu().loadScores(score, best);
                saveFile.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetBoard() {
        for (int counter = 0; counter < Settings.totalTileNumber; counter++) {
            puzzlePieces[counter].setValue(0, true);
        }
        moveBackup.clear();
        popNewPiece();
        popNewPiece();
    }

    //Get emptyIndexes list
    private void countEmpties() {
        emptyIndexes.clear();
        for (int counter = 0; counter < Settings.totalTileNumber; counter++) {
            if (puzzlePieces[counter].getValue() == 0) {
                emptyIndexes.add(puzzlePieces[counter].getIndex());
            }
        }
    }

    //Randomly Pop new Puzzle Piece
    private int popNewPiece() {
        countEmpties();
        Integer pos = emptyIndexes.get(mRandom.nextInt(emptyIndexes.size()));
        puzzlePieces[pos].setValue(((mRandom.nextInt(10) < 8) ? 2 : 4));
        return (pos);
    }

    //Check the available step(s) for a puzzle piece in [direction]
    //The function works like:
    //      + If UP then counts from itself upwards
    //      + If DOWN then counts from itself downwards
    //      ...
    //Mathematical calculations here are for merging the cases instead of 4 different ones
    private void stepCount(KeyCode direction, PuzzlePiece puzzlePiece) {
        int loopStart;
        puzzlePiece.setStep(direction, 0);
        if (puzzlePiece.getValue() > 0) {
            if (direction == KeyCode.UP || direction == KeyCode.LEFT) {
                loopStart = Settings.isVertical(direction) ? (puzzlePiece.getIndex() - 4) / 4 :
                        (puzzlePiece.getIndex() % 4) - 1;
                while (loopStart >= 0) {
                    if ((puzzlePiece.getIndex() / 4) == 0 && Settings.isVertical(direction)) {
                        break;
                    }
                    int nextIndex = Settings.isVertical(direction) ? (loopStart * 4) + (puzzlePiece.getIndex() % 4) :
                            4 * (puzzlePiece.getIndex() / 4) + loopStart;
                    if (isEmptyTile(nextIndex)) {
                        puzzlePiece.setStep(direction, puzzlePiece.getStep(direction) + 1);
                    } else if (puzzlePieces[nextIndex].getValue() == puzzlePiece.getValue()) {
                        puzzlePiece.setStep(direction, puzzlePiece.getStep(direction) + 1);
                        break;
                    } else {
                        break;
                    }
                    loopStart--;
                }
            } else {
                loopStart = Settings.isVertical(direction) ? (puzzlePiece.getIndex() + 4) / 4 :
                        (puzzlePiece.getIndex() % 4) + 1;
                while (loopStart < 4) {
                    int nextIndex = Settings.isVertical(direction) ? (loopStart * 4) + (puzzlePiece.getIndex() % 4) :
                            4 * (puzzlePiece.getIndex() / 4) + loopStart;
                    if (isEmptyTile(nextIndex)) {
                        puzzlePiece.setStep(direction, puzzlePiece.getStep(direction) + 1);
                    } else if (puzzlePieces[nextIndex].getValue() == puzzlePiece.getValue()) {
                        puzzlePiece.setStep(direction, puzzlePiece.getStep(direction) + 1);
                        break;
                    } else {
                        break;
                    }
                    loopStart++;
                }
            }
        }
    }

    private boolean isEmptyTile(int index) {
        return (puzzlePieces[index].getValue() == 0);
    }

    //Moving [piece] in [direction]
    //The function works like:
    //      + Checks the available step(s) in [direction]
    //      + Checks whether this is a merge or a normal move
    //      + Does the job depends on either case
    //The return MoveData is for notifying the parent function for further post-processing
    private MoveData movePiece(KeyCode direction, PuzzlePiece puzzlePiece, MoveData moveData) {
        if (puzzlePiece.getValue() != 0) {
            stepCount(direction, puzzlePiece);
            if (puzzlePiece.getStep(direction) > 0) {
                moveData.moved++;
                int targetIndex = puzzlePiece.getIndex();
                switch (direction) {
                    case UP:
                        targetIndex -= 4 * puzzlePiece.getStep(direction);
                        break;
                    case DOWN:
                        targetIndex += 4 * puzzlePiece.getStep(direction);
                        break;
                    case RIGHT:
                        targetIndex += puzzlePiece.getStep(direction);
                        break;
                    case LEFT:
                        targetIndex -= puzzlePiece.getStep(direction);
                        break;
                }
                if (moveData.specialCase) {
                    if (puzzlePiece.getValue() == puzzlePieces[targetIndex].getValue()) {
                        targetIndex += (Settings.isVertical(direction) ?
                                ((direction == KeyCode.UP) ? 4 : -4) :
                                (direction == KeyCode.LEFT) ? 1 : -1);
                    }
                }
                transitionDatas[puzzlePiece.getIndex()].lastValue = puzzlePiece.getValue();
                moveData = joinPieces(puzzlePiece.getIndex(), targetIndex, moveData);
            }
        }
        return moveData;
    }

    //Processing the moving, either as merging case or normal case
    //Then return the data after finish for further steps
    private MoveData joinPieces(int fromIndex, int toIndex, MoveData moveData) {
        int value = puzzlePieces[fromIndex].getValue();
        if (puzzlePieces[fromIndex].getValue() == puzzlePieces[toIndex].getValue()) {
            value *= 2;
            moveData.specialCase = !moveData.specialCase;
        } else {
            moveData.specialCase = false;
        }
        puzzlePieces[toIndex].setValue(value, false);
        transitionDatas[fromIndex].newValue = value;
        transitionDatas[fromIndex].setDestination(puzzlePieces[toIndex]);
        puzzlePieces[fromIndex].setValue(0, false);
        return moveData;
    }

    //The main Moving function in [direction]
    //The function works like:
    //      + Updates the data for Animations processing
    //      + Then creates 4 different threads for calculating the moves across the board
    //      + Waits for the threads to join, then executes the animations bases on the datas
    //During the animation time, the BoardKeysHandler will be detached to prevent UI crashes in case
    //keys are pressed continuously fast
    public void movePieces(KeyCode direction) {
        Integer[] moveSet = new Integer[Settings.totalTileNumber + 1];
        for (int counter = 0; counter < Settings.totalTileNumber; counter++) {
            moveSet[counter] = puzzlePieces[counter].getValue();
            transitionDatas[counter].setDestination(null);
        }
        moveSet[Settings.totalTileNumber] = 0;
        if (moveBackup.size() == 20) {
            moveBackup.remove(0);
        }
        moveBackup.add(moveSet);
        Thread[] threads = new Thread[4];
        int[] moved = new int[4];
        int movedSum = 0;
        for (int line = 0; line < 4; line++) {
            moved[line] = 0;
            //Calculate the values for the loop instead of dividing into 4 different cases
            final int loopStart = (direction == KeyCode.UP || direction == KeyCode.LEFT) ? 0 : 3;
            final int mLine = line;
            (threads[line] = new Thread(() -> {
                boolean joined = false;
                int mLoopStart = loopStart;
                int mLoopDelta = (loopStart == 0) ? 1 : -1;
                int mLoopEnd = (loopStart == 0) ? 4 : -1;
                while (mLoopStart != mLoopEnd) {
                    MoveData moveData = new MoveData();
                    moveData.moved = moved[mLine];
                    moveData.specialCase = joined;
                    int currentIndex = (Settings.isVertical(direction)) ? mLoopStart * 4 + mLine : mLine * 4 + mLoopStart;
                    moveData = movePiece(direction, puzzlePieces[currentIndex], moveData);
                    moved[mLine] = moveData.moved;
                    joined = moveData.specialCase;
                    mLoopStart += mLoopDelta;
                }
            })).start();
        }
        for (int counter = 0; counter < 4; counter++) {
            if (threads[counter].isAlive()) {
                try {
                    threads[counter].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            movedSum += moved[counter];
        }

        //Start Animations generating
        ParallelTransition finalTransition = new ParallelTransition(); //All pieces are moved at once
        //Calculating the values for the loop of adding fake tiles in the order that no pieces will be
        //override wrongly - UI artifact
        int loopStart1 = (direction == KeyCode.UP || direction == KeyCode.LEFT) ? 0 : 3;
        int loopEnd1 = (loopStart1 == 0) ? 4 : -1;
        int loopDelta1 = (loopStart1 == 0) ? 1 : -1;

        while (loopStart1 != loopEnd1) {
            int loopStart2 = (direction == KeyCode.UP || direction == KeyCode.LEFT) ? 0 : 3;
            int loopEnd2 = (loopStart2 == 0) ? 4 : -1;
            int loopDelta2 = (loopStart2 == 0) ? 1 : -1;
            while (loopStart2 != loopEnd2) {
                int index = Settings.isVertical(direction) ? loopStart1 * 4 + loopStart2 : loopStart2 * 4 + loopStart1;
                transitionDatas[index].reCalculate();
                if (transitionDatas[index].toPuzzlePiece != null) {
                    finalTransition.getChildren().add(createSlideAnim(transitionDatas[index],
                            thisCP));
                }
                loopStart2 += loopDelta2;
            }
            loopStart1 += loopDelta1;
        }
        //Create the copy of the moved sum for using in inner classes
        final int movedSumCopy = movedSum;

        finalTransition.setOnFinished(event -> {
            for (int counter = 0; counter < 16; counter++) {
                puzzlePieces[counter].setValue(null);
            }
            if (movedSumCopy > 0) {
                createAppearAnim(puzzlePieces[popNewPiece()], true).play();
            } else {
                moveBackup.remove(moveBackup.size() - 1);
                mBoard.getScene().setOnKeyPressed(new BoardKeysHandler(thisCP));
            }
        });
        mBoard.getScene().setOnKeyPressed(null);
        finalTransition.play();
    }

    public PuzzlePiece getPieceAtPos(int position) {
        return (puzzlePieces[position]);
    }

    //Use to generate Pop Up Animation
    private ScaleTransition createAppearAnim(PuzzlePiece puzzlePiece, boolean spawnNew) {
        PuzzlePiece cachePiece = new PuzzlePiece(); //Create a copycat
        this.mBoard.getMainBoard().getBoardGroup().getChildren().add(cachePiece);
        //Calculate the coordinate for copycat to be exactly over the original one
        cachePiece.setValue(puzzlePiece.getValue());
        //Lay the copycat over the real one
        cachePiece.setLayout((new Settings()).getCoordOfPiece(puzzlePiece.getIndex()));
        ScaleTransition transition = new ScaleTransition(Duration.millis(45), cachePiece);
        transition.setByX(0.11f);
        transition.setByY(0.11f);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.setCycleCount(2);
        transition.setAutoReverse(true);
        transition.setOnFinished(event -> {
            //Remove the fake after done
            mBoard.getMainBoard().getBoardGroup().getChildren().remove(cachePiece);
            //The KeysHandler was detached to prevent anything happening during the animation
            //So now, it is attached again after the animation finished
            if (spawnNew) {
                mBoard.getScene().setOnKeyPressed(new BoardKeysHandler(thisCP));
            }
        });
        return transition;
    }

    //Use to generate Sliding Animation
    private TranslateTransition createSlideAnim(TransitionData data, PuzzleBoardController controlPanel) {
        //Create the copycat over the real one
        PuzzlePiece cachePiece = new PuzzlePiece();
        int valueBakup = data.fromPuzzlePiece.getValue(); //Backup the original value for later restoring
        data.fromPuzzlePiece.setValue(0); //Make the real one disappear
        //Lay the copycat over the real one
        cachePiece.setLayout(data.origin);
        cachePiece.setValue(data.lastValue);
        this.mBoard.getMainBoard().getBoardGroup().getChildren().add(cachePiece);
        //Create the transition
        TranslateTransition transition = new TranslateTransition(Duration.millis(95), cachePiece);
        transition.setByX(data.destination.getX() - data.origin.getX());
        transition.setByY(data.destination.getY() - data.origin.getY());
        transition.setInterpolator(Interpolator.EASE_OUT);
        transition.setOnFinished(event -> {
            //Remove the fake after done
            data.fromPuzzlePiece.setValue(valueBakup);
            mBoard.getMainBoard().getBoardGroup().getChildren().remove(cachePiece);
            if (data.lastValue != data.newValue) {
                moveBackup.get(moveBackup.size() - 1)[Settings.totalTileNumber] += data.newValue;
                mBoard.getMainBoard().getActionMenu().updateScore(data.newValue);
                createAppearAnim(controlPanel.getPieceAtPos(data.toIndex), false).play();
            }
        });

        return transition;
    }

    public ArrayList<Integer[]> getMoveBackup() {
        return moveBackup;
    }

    public Integer[] getCurrentGame() {
        Integer[] currentGame = new Integer[Settings.totalTileNumber];
        for (int counter = 0; counter < Settings.totalTileNumber; counter++) {
            currentGame[counter] = puzzlePieces[counter].getValue();
        }
        return currentGame;
    }

    private class MoveData { //Works like a bus for transferring datas between functions
        int moved;
        boolean specialCase;
    }
}
