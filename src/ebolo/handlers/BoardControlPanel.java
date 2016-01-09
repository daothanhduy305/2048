package ebolo.handlers;

import ebolo.parts.PuzzleBoard;
import ebolo.parts.PuzzlePiece;
import ebolo.parts.TransitionData;
import javafx.animation.ParallelTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ebolo on 04-Jan-16.
 * This is the main Controlling system for the whole Application
 */
public class BoardControlPanel {
    private PuzzleBoard mBoard;
    private ArrayList<PuzzlePiece> puzzlePieces;
    private Random mRandom;
    private double pieceSize;
    private TransitionData[] transitionDatas;
    final private BoardControlPanel thisCP;

    public BoardControlPanel(PuzzleBoard puzzleBoard) {
        mBoard = puzzleBoard;
        thisCP = this;
        mRandom = new Random();
        pieceSize = (mBoard.getmDimension().getWidth() - 40)/4;
        initialize();
    }

    //Generate empty board
    private void initialize() {
        transitionDatas = new TransitionData[16];
        puzzlePieces = new ArrayList<>();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int index = row * 4 + col;
                PuzzlePiece mPiece = new PuzzlePiece(pieceSize);
                mPiece.setIndex(index);
                puzzlePieces.add(index, mPiece);
                mBoard.add(puzzlePieces.get(index).getShape(), col, row, 1, 1);
                transitionDatas[index] = new TransitionData(puzzlePieces.get(index), -1);
            }
        }
        //Just pop, no need animation here
        popNewPiece();
        popNewPiece();
    }

    //Randomly Pop new Puzzle Piece
    private int popNewPiece() {
        boolean poped = false;
        int pos = 0;
        while (!poped) {
            if (puzzlePieces.get(pos = mRandom.nextInt(16)).getValue() == 0) {
                puzzlePieces.get(pos).setValue(((mRandom.nextInt(10) < 8)? 2 : 4), null);
                poped = true;
            }
        }
        return pos;
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
                loopStart = PuzzleBoard.isVertical(direction)? (puzzlePiece.getIndex() - 4) / 4 :
                        (puzzlePiece.getIndex() % 4) - 1;
                while (loopStart >= 0) {
                    if ((puzzlePiece.getIndex() / 4) == 0 && PuzzleBoard.isVertical(direction)) {
                        break;
                    }
                    int nextIndex = PuzzleBoard.isVertical(direction)? (loopStart * 4) + (puzzlePiece.getIndex() % 4) :
                            4 * (puzzlePiece.getIndex() / 4) + loopStart;
                    if (isEmptyTile(nextIndex)) {
                        puzzlePiece.setStep(direction, puzzlePiece.getStep(direction) + 1);
                    } else if (puzzlePieces.get(nextIndex).getValue() == puzzlePiece.getValue()) {
                        puzzlePiece.setStep(direction, puzzlePiece.getStep(direction) + 1);
                        break;
                    } else {
                        break;
                    }
                    loopStart--;
                }
            } else {
                loopStart = PuzzleBoard.isVertical(direction)? (puzzlePiece.getIndex() + 4) / 4 :
                        (puzzlePiece.getIndex() % 4) + 1;
                while (loopStart < 4) {
                    int nextIndex = PuzzleBoard.isVertical(direction)? (loopStart * 4) + (puzzlePiece.getIndex() % 4) :
                            4 * (puzzlePiece.getIndex() / 4) + loopStart;
                    if (isEmptyTile(nextIndex)) {
                        puzzlePiece.setStep(direction, puzzlePiece.getStep(direction) + 1);
                    }  else if (puzzlePieces.get(nextIndex).getValue() == puzzlePiece.getValue()) {
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

    private boolean isEmptyTile (int index) {
        return (puzzlePieces.get(index).getValue() == 0);
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
                    if (puzzlePiece.getValue() == puzzlePieces.get(targetIndex).getValue()) {
                        targetIndex += (PuzzleBoard.isVertical(direction) ?
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
        int value = puzzlePieces.get(fromIndex).getValue();
        if (puzzlePieces.get(fromIndex).getValue() == puzzlePieces.get(toIndex).getValue()) {
            value *= 2;
            moveData.specialCase = !moveData.specialCase;
        } else {
            moveData.specialCase = false;
        }
        puzzlePieces.get(toIndex).setValue(value, false);
        transitionDatas[fromIndex].newValue = value;
        transitionDatas[fromIndex].toIndex = toIndex;
        puzzlePieces.get(fromIndex).setValue(0, false);
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
        for (int counter = 0; counter < 16; counter++) {
            transitionDatas[counter].toIndex = -1;
        }
        Thread[] threads = new Thread[4];
        int[] moved = new int[4];
        int movedSum = 0;
        for (int line = 0; line < 4; line++) {
            moved[line] = 0;
            //Calculate the values for the loop instead of dividing into 4 different cases
            final int loopStart = (direction == KeyCode.UP || direction == KeyCode.LEFT)? 0 : 3;
            final int mLine = line;
            (threads[line] = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean joined = false;
                    int mLoopStart = loopStart;
                    int mLoopDelta = (loopStart == 0)? 1 : -1;
                    int mLoopEnd = (loopStart == 0)? 4 : -1;
                    while (mLoopStart != mLoopEnd) {
                        MoveData moveData = new MoveData();
                        moveData.moved = moved[mLine];
                        moveData.specialCase = joined;
                        int currentIndex = (PuzzleBoard.isVertical(direction))? mLoopStart * 4 + mLine : mLine * 4 + mLoopStart;
                        moveData = movePiece(direction, puzzlePieces.get(currentIndex), moveData);
                        moved[mLine] = moveData.moved;
                        joined = moveData.specialCase;
                        mLoopStart += mLoopDelta;
                    }
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
        int loopStart1 = (direction == KeyCode.UP || direction == KeyCode.LEFT)? 0 : 3;
        int loopEnd1 = (loopStart1 == 0)? 4 : -1;
        int loopDelta1 = (loopStart1 == 0)? 1 : -1;

        while (loopStart1 != loopEnd1) {
            int loopStart2 = (direction == KeyCode.UP || direction == KeyCode.LEFT)? 0 : 3;
            int loopEnd2 = (loopStart2 == 0)? 4 : -1;
            int loopDelta2 = (loopStart2 == 0)? 1 : -1;
            while (loopStart2 != loopEnd2) {
                int index = PuzzleBoard.isVertical(direction)? loopStart1 * 4 + loopStart2 : loopStart2 * 4 + loopStart1;
                transitionDatas[index].reCalculate();
                if (transitionDatas[index].toIndex != -1) {
                    finalTransition.getChildren().add(mBoard.getmPuzzleBoardGroup().createSlideAnim(transitionDatas[index]));
                }
                loopStart2 += loopDelta2;
            }
            loopStart1 += loopDelta1;
        }
        //Create the copy of the moved sum for using in inner classes
        final int movedSumCopy = movedSum;

        finalTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int counter = 0; counter < 16; counter++) {
                    puzzlePieces.get(counter).refresh();
                }
                if (movedSumCopy > 0) {
                    mBoard.getmPuzzleBoardGroup().playAppearAnim(puzzlePieces.get(popNewPiece()), thisCP).play();
                } else {
                    mBoard.getScene().setOnKeyPressed(new BoardKeysHandler(thisCP));
                }
            }
        });
        mBoard.getScene().setOnKeyPressed(null);
        finalTransition.play();
    }

    private class MoveData { //Works like a bus for transferring datas between functions
        protected int moved;
        protected boolean specialCase;
    }
}
