package chess.model.board;

import chess.model.ChessGame;
import chess.model.piece.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Square {
    protected ChessGame game;
    protected int file, rank;
    protected Piece piece = null;
    protected boolean isFilled = false;
    // piece on this cell
    // occupied iff this.piece != null
    protected String acnCoord;
    // square coordinate according to algebraic chess notation
    // such as a1, a2, ... and h8
    // file a~h for index 0~7
    // rank 1~8 for index 7~0
    protected HashSet<Square> reachable; // 해당 move에 여기로 올 수 있는 말들의 위치; 여기로 movable한 말들이 있는 칸들의 집합
    // protected HashSet<Square> attacking = null; // 해당 move에 king이 여기로 움직이면 안됨; 상대방이 공격 중인 칸들의 집합; self checking 비종속적
    protected boolean isAttacked = false;

    public Square(ChessGame game, int file, int rank){
        this.game = game;
        this.file = file;
        this.rank = rank;
        acnCoord = String.format("%c%d", 'a'+file, ChessGame.DIM-rank);
        // System.out.printf("gen square %s\n", acnCoord);
    }
    /*
    public Square(ChessGame game, int file, int rank, Piece piece){
        this(game, file, rank);
        this.piece = piece;
    }*/

    public void setPiece(Piece piece){
        this.piece = piece;
        isFilled = true;
        if(piece.getSquare() != this) piece.setSquare(this);
        // System.out.printf("--setPiece-- %s on %s\n", piece.toString(), acnCoord);
    }

    public void setNullPiece(){
        piece = null;
        isFilled = false;
    }

    public Piece getPiece(){
        return piece;
    }

    public boolean isFilled(){
        return isFilled;
    }

    public boolean isEmpty(){
        return !isFilled;
    }

    public int file() { return file; }

    public int rank() { return rank; }

    @Override
    public String toString() { return acnCoord; }

    public void setReachable(){ // must be called after all movable's called
        // based on each piece.movable
        reachable = new HashSet<>();
        HashMap<String, Piece> pieces = game.getPieces(game.getTurn());
        pieces.forEach((id, piece) -> {
            if(!(piece.isTaken() || piece instanceof Pawn && ((Pawn) piece).isPromoted())
                    && piece.getMovable().contains(this))
                reachable.add(piece.getSquare());
        });
    }

    public void setIsAttacked(){
        // based on game.board
        int[][] board = game.getBoard();
        int oc = game.getTurn().opponent().toInteger();

        // pawn check
        if(game.inRange(rank+oc)){
            if(game.inRange(file-1) && board[rank+oc][file-1] == Pawn.INDICATOR*oc){
                isAttacked = true;
                return;
            }else if(game.inRange(file+1) && board[rank+oc][file+1] == Pawn.INDICATOR*oc){
                isAttacked = true;
                return;
            }
        }
        // king check
        for(int[] mv : Queen.getMovableDirection()){
            if(!(game.inRange(file+mv[0]) && game.inRange(rank+mv[1]))) continue;
            if(board[rank+mv[1]][file+mv[0]] == King.INDICATOR*oc){
                isAttacked = true;
                return;
            }
        }
        // knight check
        for(int[] mv : Knight.getMovableSquares()){
            if(!(game.inRange(file+mv[0]) && game.inRange(rank+mv[1]))) continue;
            if(board[rank+mv[1]][file+mv[0]] == Knight.INDICATOR*oc){
                isAttacked = true;
                return;
            }
        }
        // vertical move - rook and queen - check
        for(int[] mv : Rook.getMovableDirection()){
            for(int f = file+mv[0], r = rank+mv[1]; game.inRange(f) && game.inRange(r); f+=mv[0], r+=mv[1]){
                int tind = board[r][f]*oc;
                if(tind < 0) break; // our piece
                else if(tind > 0){ // opponent's piece
                    if(tind == Rook.INDICATOR || tind == Queen.INDICATOR){
                        isAttacked = true;
                        return;
                    }else break;
                }
            }
        }
        // diagonal move - bishop and queen - check
        for(int[] mv : Bishop.getMovableDirection()){
            for(int f = file+mv[0], r = rank+mv[1]; game.inRange(f) && game.inRange(r); f+=mv[0], r+=mv[1]){
                int tind = board[r][f]*oc;
                if(tind < 0) break; // our piece
                else if(tind > 0){ // opponent's piece
                    if(tind == Bishop.INDICATOR || tind == Queen.INDICATOR){
                        isAttacked = true;
                        return;
                    }else break;
                }
            }
        }
        isAttacked = false;
    }

    public HashSet<Square> getReachable(){
        return reachable;
    }

    public boolean isAttacked(){
        return isAttacked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return file == square.file &&
                rank == square.rank &&
                Objects.equals(game, square.game) &&
                Objects.equals(piece, square.piece);
    }

    @Override
    public int hashCode() {
        return acnCoord.hashCode();
    }
}
