package chess.model;

import chess.model.board.*;
import chess.model.piece.*;

// examples : Qxe6 dxe7=Q Rd7#

public class Move {

    /*
    todo
    말들의 권한 주입 :
    킹/룩 -> 캐슬링 가능 여부 -> 어느 순간 이후로 사라지는 권한이므로 정수형으로 ChessGame에 넣어두면 ㅇㅋ
    폰 -> 앙파상 가능 여부 -> 이건 ind-1째 움직임에서 파악 가능하므로 여긴 안넣어도 ㅇㅋ
     */

    private String algebraicNotation = "";
    private int[][] boardHistory;

    private final Piece movedPiece;
    private final Square fromSquare;
    private final Square toSquare;

    private boolean isKingsideCastling = false;
    private boolean isQueensideCastling = false;
    private boolean fileAmbiguous = false;
    private boolean rankAmbiguous = false;
    /*
    toSquare에 올 수 있는 말들 중, movedPiece와 종류가 같은 것이 있으면 아래 탐색 시작
    이 과정상의 의미론을 확실히 해보자
    1) fromSquare와 같은 file의 다른 칸에는 그런 말들이 없을 경우 : fileAmbiguous
    2) fromSquare와 같은 rank의 다른 칸에는 그런 말들이 없을 경우 : rankAmbiguous
    3) 둘 다 아닐 경우, 즉 fromSquare의 file에도 rank에도 그런 말들이 있을 경우 : fileAmb. && rankAmb.
     */
    private boolean isCapture = false;
    private boolean isEnPassant = false;
    private Piece capturedPiece; // notnull(?) iff isCapture
    // en passant이어도 ok; capturedPiece의 위치가 자기 자신 안에 저장된 상태이므로
    private boolean isPromotion = false;
    private Piece promotedPiece;
    private boolean isCheck = false;
    private boolean isMate = false; // && isCheck -> checkmate(#); else -> stalemate(1/2-1/2)

    public Move(Piece movedPiece, Square fromSquare, Square toSquare){
        this.movedPiece = movedPiece;
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        if(toSquare.getPiece() != null){
            isCapture = true;
            capturedPiece = toSquare.getPiece();
            capturedPiece.setTaken();
        }
    }

    public void setAlgebraicNotation(){
        StringBuilder sb = new StringBuilder();
        if(isKingsideCastling){
            sb.append("O-O");
        }else if(isQueensideCastling){
            sb.append("O-O-O");
        }else{
            if(movedPiece instanceof Pawn){
                if(isCapture)
                    sb.append(fromSquare.toString().charAt(0)).append('x');
            }else{
                String pid = movedPiece.toString();
                sb.append(pid.charAt(pid.length()-1));
                if(fileAmbiguous)
                    sb.append(fromSquare.toString().charAt(0));
                if(rankAmbiguous)
                    sb.append(fromSquare.toString().charAt(1));
                if(isCapture)
                    sb.append('x');
            }sb.append(toSquare.toString());
            if(isPromotion){
                String pid = promotedPiece.toString();
                sb.append('=').append(pid.charAt(pid.length()-1));
            }
        }
        if(isCheck){
            if(isMate){
                sb.append('#');
            }else sb.append('+');
        }
        algebraicNotation = sb.toString();
    }

    public String getAlgebraicNotation(){
        if(algebraicNotation == null) setAlgebraicNotation();
        return algebraicNotation;
    }

    public void setBoardHistory(int[][] board){
        boardHistory = new int[board.length][];
        for(int i=0; i<board.length; i++){
            boardHistory[i] = board[i].clone();
        }
    }

    public int[][] getBoardHistory(){
        return boardHistory;
    }

    public Piece getMovedPiece(){
        return movedPiece;
    }

    public Square getFromSquare(){
        return fromSquare;
    }

    public Square getToSquare(){
        return toSquare;
    }

    public void thisIsCastling(boolean isKingside){
        if(isKingside){
            isKingsideCastling = true;
        }else{
            isQueensideCastling = true;
        }
    }

    public boolean isCastling(){
        return isKingsideCastling || isQueensideCastling;
    }

    public boolean isKingsideCastling(){
        return isKingsideCastling;
    }

    public boolean isQueensideCastling(){
        return isQueensideCastling;
    }

    public void rankIsAmbiguous(){
        rankAmbiguous = true;
    }

    public void fileIsAmbiguous(){
        fileAmbiguous = true;
    }

    public void thisIsEnPassant(Piece capturedPiece){
        // manual setting for special capture, such as en passant
        isCapture = true;
        isEnPassant = true;
        this.capturedPiece = capturedPiece;
        capturedPiece.setTaken();
    }

    public boolean isCapture(){
        return isCapture;
    }

    public boolean isEnPassant(){
        return isEnPassant;
    }

    public Piece getCapturedPiece(){
        return capturedPiece;
    }

    public void thisIsPromotion(Piece promotedPiece){
        isPromotion = true;
        this.promotedPiece = promotedPiece;
    }

    public Piece getPromotedPiece(){
        return promotedPiece;
    }

    public boolean isPromotion(){
        return isPromotion;
    }

    public void thisIsCheck(){
        isCheck = true;
    }

    public boolean isCheck(){
        return isCheck;
    }

    public void thisIsMate(){
        isMate = true;
    }

    public boolean isMate(){
        return isMate;
    }
}
