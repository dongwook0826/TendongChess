package chess.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MovePair {
    private final IntegerProperty moveNum;
    private final StringProperty whiteMoveNotation = new SimpleStringProperty("");
    private final StringProperty blackMoveNotation = new SimpleStringProperty("");

    public MovePair(int moveNum) {
        this(moveNum, null, null);
    }

    public MovePair(int moveNum, Move whiteMove, Move blackMove){
        this.moveNum = new SimpleIntegerProperty(moveNum);
        setWhiteMove(whiteMove);
        setBlackMove(blackMove);
    }

    public void setWhiteMove(Move whiteMove){
        if(whiteMove == null){
            setWhiteMoveNotation("");
            // whiteMoveDone = false;
        }else{
            setWhiteMoveNotation(whiteMove.getAlgebraicNotation());
            // whiteMoveDone = true;
        }
    }

    public void setBlackMove(Move blackMove){
        if(blackMove == null){
            setBlackMoveNotation("");
            // blackMoveDone = false;
        }else{
            setBlackMoveNotation(blackMove.getAlgebraicNotation());
            // blackMoveDone = true;
        }
    }

    /*
    public void setWhiteMoveDone(boolean whiteMoveDone){
        this.whiteMoveDone = whiteMoveDone;
    }

    public void setBlackMoveDone(boolean blackMoveDone){
        this.blackMoveDone = blackMoveDone;
    }
     */


    public int getMoveNum(){
        return moveNum.get();
    }

    public IntegerProperty moveNumProperty(){
        return moveNum;
    }

    public String getWhiteMoveNotation(){
        return whiteMoveNotation.get();
    }

    public String getBlackMoveNotation(){
        return blackMoveNotation.get();
    }

    public void setWhiteMoveNotation(String whiteMoveNotation){
        this.whiteMoveNotation.set(whiteMoveNotation);
    }

    public void setBlackMoveNotation(String blackMoveNotation){
        this.blackMoveNotation.set(blackMoveNotation);
    }

    public StringProperty whiteMoveNotationProperty(){
        return whiteMoveNotation;
    }

    public StringProperty blackMoveNotationProperty(){
        return blackMoveNotation;
    }

    /*
    public boolean isWholeMovePairDone(){
        return whiteMoveDone && blackMoveDone;
    }

    public boolean isMovePairEmpty(){
        return !(whiteMoveDone || blackMoveDone);
    }
     */
}
