package chess.model.piece;

import chess.model.*;
import chess.model.board.*;

// import java.util.Arrays;
import java.util.HashSet;

public class Knight extends Piece {

    public static final int INDICATOR = 3;

    private static final int[][] MOVABLE_SQUARES = {
            {2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2},{2,-1}
    };

    public Knight(ChessGame game, PieceColor pieceColor){
        super(game, pieceColor);
    }

    public Knight(ChessGame game, PieceColor pieceColor, String pieceId){
        super(game, pieceColor, pieceId);
    }

    public static int[][] getMovableSquares(){
        return MOVABLE_SQUARES;
    }

    @Override
    public int indicator(){
        return INDICATOR * color.toInteger();
    }

    @Override
    public void setMovableTakeable(){
        movable = new HashSet<>();
        takeable = new HashSet<>();
        int[][] board = game.getBoard();
        int rank = square.rank(), file = square.file();
        int col = color.toInteger();
        for (int[] msq : MOVABLE_SQUARES) {
            int f = msq[0]+file;
            int r = msq[1]+rank;
            if(game.inRange(f) && game.inRange(f)){
                if (isMovableTo(f, r)) {
                    Square sq = game.squareOn(f, r);
                    movable.add(sq);
                    if (board[r][f]*col < 0) {
                        takeable.add(sq);
                    }
                }
            }
        }
    }

    @Override
    public void makeMove(Move move, Square square){
        assert movable.contains(square) : "Impossible move, I told you!";
        int[][] board = game.getBoard();
        int pRank = rank(), pFile = file();
        int sRank = square.rank(), sFile = square.file();
        board[sRank][sFile] = board[pRank][pFile];
        board[pRank][pFile] = 0;
        square.setPiece(this);
    }
}
