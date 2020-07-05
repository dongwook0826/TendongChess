package chess.model;

import chess.model.board.*;
import chess.model.piece.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;

public class ChessGame {

    String title;

    public static final int DIM = 8;
    public static final int[][] INITIAL_BOARD = {
            /*
             * white : positive / black : negative / empty : 0
             * pawn   : 1
             * rook   : 2
             * knight : 3
             * bishop : 4
             * queen  : 5
             * king   : 6
             */
            {-2,-3,-4,-5,-6,-4,-3,-2},
            {-1,-1,-1,-1,-1,-1,-1,-1},
            { 0, 0, 0, 0, 0, 0, 0, 0},
            { 0, 0, 0, 0, 0, 0, 0, 0},
            { 0, 0, 0, 0, 0, 0, 0, 0},
            { 0, 0, 0, 0, 0, 0, 0, 0},
            { 1, 1, 1, 1, 1, 1, 1, 1},
            { 2, 3, 4, 5, 6, 4, 3, 2}
    };

    private final int[][] board = new int[DIM][];

    private PieceColor turn = PieceColor.WHITE;
    private final HashMap<String, Square> allSquares = new HashMap<>();
    private final HashMap<String, Piece> whitePieces = new HashMap<>();
    private final HashMap<String, Piece> blackPieces = new HashMap<>();
    private int whiteAliveCnt, blackAliveCnt;
    private final ArrayList<Move> moves = new ArrayList<>();
    // todo : define moveTableData
    private final ObservableList<MovePair> moveTableData = FXCollections.observableArrayList();
    private Move lastMove = null;
    private int moveCount = 0;
    private int reversibleMoveStack = 0;
    // +1 whenever the move is not one of the following : capture(incl. e.p.), pawn maneuver(incl. prom), castling
    // 3회 동형반복 확인에 사용; 막 실시한 Move부터 이 횟수만큼 undo해봐서 똑같은 board가 2번 나오는 순간 무승부
    private int fiftyMoveStack = 0;
    // +1 whenever the move is not a capture, nor a pawn maneuver
    // 50수 무승부 확인에 사용; 100을 찍으면 무승부

    private final int[][] noMoreCastlingMoveCount = {{-1, -1}, {-1, -1}};
    // white-ks/qs, black-ks/qs
    // 백10수에 퀸룩이 처음 움직였으면 [0][1] 성분을 10*2-1 = 19로
    // rollback 후 킹에 대한 캐슬링 권한 주입에 사용
    private final HashMap<String, Integer> whitePawnsFirstMoveCount = new HashMap<>();
    private final HashMap<String, Integer> blackPawnsFirstMoveCount = new HashMap<>();
    // 흑9수에 b폰이 처음 이동했으면 .get("QNP")[1] 성분을 9*2 = 18로
    // rollback 후 폰에 대한 2칸 이동 권한 주입에 사용

    private final String[] pieceIds = {
            "QR", "QN", "QB", "Q", "K", "KB", "KN", "KR",
            "QRP", "QNP", "QBP", "QP", "KP", "KBP", "KNP", "KRP"
    };// if promoted, then =Q, =R, =N, =B added to the new piece's Id
    private final String[][] startPositions = {
            {
                    "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
                    "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"
            }, {
                    "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                    "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"
            }
    };

    private int result = Integer.MIN_VALUE;
    // 1, 0, -1 for white win, draw, black win respectively
    private String resultInfo = "";
    // win : checkmate / resignation
    // draw : repetition / fifty move / material insufficiency / stalemate

    private Piece genPiece(PieceColor color, String pieceId){
        return switch (pieceId.charAt(pieceId.length() - 1)) {
            case 'K' -> new King(this, color, pieceId);
            case 'Q' -> new Queen(this, color, pieceId);
            case 'R' -> new Rook(this, color, pieceId);
            case 'N' -> new Knight(this, color, pieceId);
            case 'B' -> new Bishop(this, color, pieceId);
            /* case 'P' */
            default -> new Pawn(this, color, pieceId);
        };
    }

    { // ----- instance initialization block -----
        for(int i=0; i<DIM; i++){
            board[i] = INITIAL_BOARD[i].clone();
        }
        for(int r=0; r<DIM; r++){
            for(int f=0; f<DIM; f++){
                Square sq = new Square(this, f, r);
                allSquares.put(sq.toString(), sq);
            }
        }
        for(int p=0; p<pieceIds.length; p++){
            whitePieces.put(pieceIds[p], genPiece(PieceColor.WHITE, pieceIds[p]));
            whitePieces.get(pieceIds[p]).setSquare(allSquares.get(startPositions[0][p]));
            // allSquares.get(startPositions[0][p]).setPiece(whitePieces.get(pieceIds[p]));
            blackPieces.put(pieceIds[p], genPiece(PieceColor.BLACK, pieceIds[p]));
            blackPieces.get(pieceIds[p]).setSquare(allSquares.get(startPositions[1][p]));
            // allSquares.get(startPositions[1][p]).setPiece(blackPieces.get(pieceIds[p]));
        }
        whitePieces.forEach((id, piece) -> piece.thisIsMyKing());
        blackPieces.forEach((id, piece) -> piece.thisIsMyKing());
        /*
        for(int i=0; i<pieceIds.length; i++){
            whitePieces.get(pieceIds[i]).setSquare(allSquares.get(startPositions[0][i]));
            blackPieces.get(pieceIds[i]).setSquare(allSquares.get(startPositions[1][i]));
        }*/
        allSquares.forEach((id, square) -> square.setIsAttacked());
        whitePieces.forEach((id, piece) -> piece.setMovableTakeable());
        allSquares.forEach((id, square) -> square.setReachable());
        whiteAliveCnt = whitePieces.size();
        blackAliveCnt = blackPieces.size();
        for(int i=DIM; i<pieceIds.length; i++){
            whitePawnsFirstMoveCount.put(pieceIds[i], -1);
            blackPawnsFirstMoveCount.put(pieceIds[i], -1);
        }
    }

    // ------------ generator ---------------

    public ChessGame() {
        title = String.format("NewGame_%d", System.currentTimeMillis());
        // System.out.printf("Let's play a chess!\n%s\n", title);
    }
    /*
    public ChessGame(String title){
        this.title = title;
        System.out.printf("Let's play a chess!\n%s\n", title);
    }
     */

    // ------------- making move ---------------

    public boolean move(String uci){
        if(uci.length() < 4 || uci.length() > 5){
            // System.out.printf("invalid notation length : %d\n", uci.length());
            return false;
        }
        if(!(inRange(uci.charAt(0)-'a') && inRange('8'-uci.charAt(1))
                && inRange(uci.charAt(2)-'a') && inRange('8'-uci.charAt(3)))){
            // System.out.println("incorrect UCI notation format");
            return false;
        }
        Square sq = squareOn(uci.charAt(0)-'a', '8'-uci.charAt(1));
        if(sq.isEmpty() || sq.getPiece().color() != turn){
            // System.out.println("Not your turn!");
            return false;
        }
        if(uci.length() == 5){
            /*
            only promotion
            ex) f2f1q : move f2 pawn to f1, and promote to queen
                b7a8n : move b7 pawn and take a8, and promote to knight
             */
            if(sq.isEmpty()){
                // System.out.printf("no piece on the square : %s\n", uci.substring(0,2));
                return false;
            }
            Piece piece = sq.getPiece();
            if(!(piece instanceof Pawn)){
                // System.out.println("piece on the square is not a pawn");
                return false;
            }
            int prom = switch(uci.charAt(uci.length()-1)){
                case 'r' -> 2;
                case 'n' -> 3;
                case 'b' -> 4;
                /* case 'q' */
                default -> 5;
            };
            move((Pawn)piece, squareOn(uci.charAt(2)-'a', '8'-uci.charAt(3)), prom);
        }else {
            if(sq.isEmpty()){
                // System.out.printf("no piece on the square : %s\n", uci.substring(0,2));
                return false;
            }
            Piece piece = sq.getPiece();
            move(piece, squareOn(uci.charAt(2)-'a', '8'-uci.charAt(3)));
        }
        return true;
    }

    public void move(Piece piece, Square square){
        if(!piece.getMovable().contains(square)){
            // System.out.println("Impossible move!");
            return;
        }if(piece.color() != turn){
            // System.out.println("Not your turn!");
            return;
        }if(piece instanceof Pawn && square.rank() == (piece.color().isWhite() ? 0 : DIM-1)){
            // System.out.println("Try again : the pawn should promote to another material");
            return;
        }
        Move thisMove = new Move(piece, piece.getSquare(), square);
        String pid = piece.toString();
        boolean ambiguous = false;
        boolean fileAmbiguous = false;
        boolean rankAmbiguous = false;
        for(Square sq : square.getReachable()){
            if(sq.getPiece() == piece) continue;
            Piece ambpc = sq.getPiece();
            String amb = ambpc.toString();
            if(amb.charAt(amb.length()-1) != pid.charAt(pid.length()-1))
                continue; // different kind of piece
            // from here, ambiguous
            if(!ambiguous){
                ambiguous = true;
                fileAmbiguous = true;
                rankAmbiguous = true;
            }if(piece.rank() == ambpc.rank() && rankAmbiguous){
                rankAmbiguous = false;
            }else if(piece.file() == ambpc.file() && fileAmbiguous){
                fileAmbiguous = false;
            }
        }
        /* (fileAmbi - rankAmbi)
        해당 file과 rank에 자기 혼자 있으면 true-true --> file 표기
        해당 rank에 다른 말이 있고 file엔 혼자면 true-false --> file 표기
        해당 file에 다른 말이 있고 rank엔 혼자면 false-true --> rank 표기
        file에도 rank에도 다른 말이 각각 있을 경우 false-false --> file, rank 병기
         */
        if(ambiguous){
            if(fileAmbiguous){
                thisMove.fileIsAmbiguous();
            }else{
                thisMove.rankIsAmbiguous();
                if(!rankAmbiguous)
                    thisMove.fileIsAmbiguous();
            }
        }
        piece.makeMove(thisMove, square);
        completeMove(thisMove);
        if(piece instanceof Pawn || thisMove.isCapture()){
            fiftyMoveStack = 0;
            reversibleMoveStack = 0;
        }else{
            fiftyMoveStack++;
            if(thisMove.isCastling()){
                reversibleMoveStack = 0;
            }else{
                reversibleMoveStack++;
            }
        }
        setResult(result, false);
    }

    // overload; if promotion
    public void move(Pawn piece, Square square, int promotedPieceIndic){
        if(!piece.getMovable().contains(square)){
            // System.out.println("Impossible move!");
            return;
        } // 폰은 잡는 순간 pgn상 disambiguation이 이미 되기 때문에 굳이 관련해서 체크할 필요 x
        Move thisMove = new Move(piece, piece.getSquare(), square);
        piece.promoteTo(thisMove, square, promotedPieceIndic);
        completeMove(thisMove);
        reversibleMoveStack = 0;
        fiftyMoveStack = 0;
        setResult(result, false);
    }

    public void completeMove(Move move){
        move.setBoardHistory(board);
        turn = turn.opponent();
        allSquares.forEach((acn, sq) -> sq.setIsAttacked());
        // -------- mate checking --------
        boolean movePossible = false;
        // getPieces(turn).forEach((id, pc) -> pc.setMovableTakeable());
        for(String id : getPieces(turn).keySet()){
            Piece pc = getPieces(turn).get(id);
            if(pc.isTaken() || (pc instanceof Pawn && ((Pawn)pc).isPromoted())) continue;
            pc.setMovableTakeable();
            movePossible = movePossible || !pc.getMovable().isEmpty();
        }
        if(!movePossible){ // no legal move
            move.thisIsMate();
        }
        // -------- check checking --------
        allSquares.forEach((acn, sq) -> sq.setReachable());
        if(getPieces(turn).get("K").getSquare().isAttacked()){
            move.thisIsCheck();
        }
        if(!moves.isEmpty() && moves.get(moves.size()-1).isEnPassant())
            reversibleMoveStack = -1; // later +=1
        moves.add(move);
        lastMove = move;
        move.setAlgebraicNotation();
        if(turn.isWhite()){
            // black has done its move
            MovePair mvpr = moveTableData.get(moveTableData.size()-1);
            mvpr.setBlackMove(move);
        }else{
            // white has done its move
            MovePair mvpr = new MovePair(moveCount/2+1);
            mvpr.setWhiteMove(move);
            moveTableData.add(mvpr);
        }
        moveCount++;
        /*
        System.out.printf("your move was %d%s%s\n",
                (moveCount+1)/2,
                turn.isWhite() ? "..." : ".",
                move.getAlgebraicNotation());
        System.out.printf("Kingside : %s / Queenside : %s\n",
                ((King)getPieces(turn).get("K")).isKingsideCastlingAvailable() ? "O" : "X",
                ((King)getPieces(turn).get("K")).isQueensideCastlingAvailable() ? "O" : "X");
         */
    }

    // ----------- win & draw condition check -------------

    public void setResult(int result, boolean forceFinish){
        // must be checked after stack cnt fields are renewed
        if(isWin()){
            this.result = turn.opponent().toInteger();
        }else if(isDraw()){
            this.result = 0;
        }else if(forceFinish){
            this.result = result;
            if(result == 0){
                resultInfo = "mutual agreement";
            }else if(result == 1){
                resultInfo = "black's resignation";
            }else if(result == -1){
                resultInfo = "white's resignation";
            }
        }
    }

    public int getResult(){
        return result;
    }

    public String getResultInfo(){
        return resultInfo;
    }

    public boolean isWin(){
        // win of last moved player(=turn.opponent())
        if(lastMove == null) return false;
        if(lastMove.isMate() && lastMove.isCheck()){
            resultInfo = "checkmate";
            return true;
        }else return false;
    }

    public boolean isDraw() {
        if(lastMove == null) return false;
        if(countPreviousEqualPosition() >= 2){
            resultInfo = "threefold repetition";
            return true;
        }else if(fiftyMoveStack >= 100){
            resultInfo = "fifty move rule";
            return true;
        }else if(!isMaterialSufficient()){
            resultInfo = "material insufficiency";
            return true;
        }else if(lastMove.isMate() && !lastMove.isCheck()){
            resultInfo = "stalemate";
            return true;
        }else return false;
        /*
        return countPreviousEqualPosition() >= 2 // threefold repetition
                || fiftyMoveStack >= 100 // fifty move rule
                || !isMaterialSufficient() // insufficiency of material
                || (lastMove.isMate() && !lastMove.isCheck()); // stalemate
         */
    }

    public int countPreviousEqualPosition(){
        if(reversibleMoveStack < 8) return -1;
        int[][] copyb = new int[DIM][];
        int repetition = 0;
        int stack = reversibleMoveStack;
        for(int i=0; i<DIM; i++)
            copyb[i] = board[i].clone();
        reverseMove :
        for(int it = moves.size()-1; stack>0; stack--){
            assert it>0 : "threefold rep. check : no more searchable move";
            Move mv = moves.get(--it);
            copyb[mv.getToSquare().rank()][mv.getToSquare().file()] = 0;
            copyb[mv.getFromSquare().rank()][mv.getFromSquare().file()] = mv.getMovedPiece().indicator();
            for(int i=0; i<DIM; i++){
                for(int j=0; j<DIM; j++){
                    if(copyb[i][j] != board[i][j])
                        continue reverseMove;
                }
            }repetition++;
            if(repetition >= 2) break;
            /*
            if(mv.isKingsideCastling()){
                copyb[tRank][7] = copyb[tRank][5];
                copyb[tRank][5] = 0;
            }else if(mv.isQueensideCastling()){
                copyb[tRank][0] = copyb[tRank][3];
                copyb[tRank][3] = 0;
            }else if(mv.isEnPassant()){
                copyb[tRank+mv.getMovedPiece().color().toInteger()][tFile]
                        = mv.getCapturedPiece().indicator();
            }
            copyb[tRank][tFile] = mv.isCapture() ? mv.getCapturedPiece().indicator() : 0;
            copyb[fRank][fFile] = mv.getMovedPiece().indicator();
             */
        }return repetition;
    }

    public boolean isMaterialSufficient(){
        if(whiteAliveCnt > 2 || blackAliveCnt > 2) return true;
        // from now on, whiteAliveCnt <= 2 && blackAliveCnt <= 2
        if(whiteAliveCnt == 2){
            for(String wpid : whitePieces.keySet()){
                Piece wpc = whitePieces.get(wpid);
                if(wpc.isTaken() || wpc instanceof King
                        || (wpc instanceof Pawn && ((Pawn)wpc).isPromoted())) continue;
                // last piece(not king) alive
                if(wpc instanceof Queen || wpc instanceof Rook || wpc instanceof Pawn) return true;
                // ...is bishop or knight, guaranteed
                if(blackAliveCnt <= 1) return false;
                for(String bpid : blackPieces.keySet()){
                    Piece bpc = blackPieces.get(bpid);
                    if(bpc.isTaken() || bpc instanceof King
                            || (bpc instanceof Pawn && ((Pawn)bpc).isPromoted())) continue;
                    if(bpc instanceof Queen || bpc instanceof Rook || bpc instanceof Pawn) return true;
                    // last piece is bishop or knight
                    if(wpc instanceof Knight || bpc instanceof Knight) return true;
                    return wpid.charAt(0) == bpid.charAt(0); // bishops on diff. colored square
                }
            }
        }else{
            // if white only has king left
            if(blackAliveCnt <= 1) return false;
            for(String bpid : blackPieces.keySet()){
                Piece bpc = blackPieces.get(bpid);
                if(bpc.isTaken() || bpc instanceof King
                        || (bpc instanceof Pawn && ((Pawn)bpc).isPromoted())) continue;
                return bpc instanceof Queen || bpc instanceof Rook || bpc instanceof Pawn;
            }
        }return true; // maybe unreachable
    }

    public void onePieceTaken(PieceColor color){
        onePieceTaken(color, false);
    }

    public void onePieceTaken(PieceColor color, boolean isUndo){
        if(isUndo){
            switch(color){
                case WHITE -> whiteAliveCnt++;
                case BLACK -> blackAliveCnt++;
            }
        }else{
            switch(color){
                case WHITE -> whiteAliveCnt--;
                case BLACK -> blackAliveCnt--;
            }
        }
    }

    // ---------- takeback & other game condition injection ------------

    public void takeback(int moveNum, boolean isWhite){
        takeback(moveNum, isWhite ? PieceColor.WHITE : PieceColor.BLACK);
    }

    public void takeback(int moveNum, PieceColor turn){
        // todo
        // moveNum starts from 1, not 0
        // go back to just before moves.get(ind) is played(= turn is to make next move)
        // that is, moveCount == ind after its execution
        int ind = moveNum * 2 - (turn.isWhite() ? 2 : 1);
        if(ind<0 || ind>=moves.size()){
            // System.out.printf("input out of bound : %d\n", moveNum);
            return;
        }
        Move undoMove;
        while(moveCount > ind){
            undoMove = moves.get(--moveCount);
            // take <- move <- promote
            Piece movedPiece = undoMove.getMovedPiece();
            PieceColor color = movedPiece.color();
            if(undoMove.isPromotion()){
                getPieces(color).remove(undoMove.getPromotedPiece().toString());
                ((Pawn)movedPiece).undoPromotion();
                undoMove.getFromSquare().setNullPiece();
            }
            movedPiece.setSquare(undoMove.getFromSquare());
            if(undoMove.isCapture()){
                Piece capturedPiece = undoMove.getCapturedPiece();
                capturedPiece.undoTaken();
                capturedPiece.setSquare(undoMove.getToSquare());
            }else if(undoMove.isKingsideCastling()){
                getPieces(color).get("KR").setSquare(squareOn(7, color.isWhite() ? 7 : 0));
                movedPiece.setSquare(squareOn(4, color.isWhite() ? 7 : 0));
            }else if(undoMove.isQueensideCastling()){
                getPieces(color).get("QR").setSquare(squareOn(0, color.isWhite() ? 7 : 0));
                movedPiece.setSquare(squareOn(4, color.isWhite() ? 7 : 0));
            }
            if(color.isWhite()){
                moveTableData.remove(moveCount/2);
            }else{ // color == BLACK
                moveTableData.get(moveCount/2).setBlackMove(null);
            }
            moves.remove(moveCount);
        }
        // board initialization
        if(ind == 0){
            for(int i=0; i<DIM; i++){
                board[i] = INITIAL_BOARD[i].clone();
            }
        }else{ // if ind > 0
            undoMove = moves.get(ind-1);
            for(int i=0; i<DIM; i++){
                board[i] = undoMove.getBoardHistory()[i].clone();
            }
            // en passant availability injection
            if(undoMove.getMovedPiece() instanceof Pawn){
                int rk = undoMove.getToSquare().rank();
                if(Math.abs(undoMove.getFromSquare().rank() - rk) == 2){
                    int fl = undoMove.getToSquare().file();
                    if(inRange(fl-1) && !squareOn(fl-1, rk).isEmpty()){
                        Piece piece = squareOn(fl-1, rk).getPiece();
                        if(piece instanceof Pawn){
                            ((Pawn) piece).setRightEnPassantAvailable();
                        }
                    }
                    if(inRange(fl+1) && !squareOn(fl+1, rk).isEmpty()){
                        Piece piece = squareOn(fl+1, rk).getPiece();
                        if(piece instanceof Pawn){
                            ((Pawn) piece).setLeftEnPassantAvailable();
                        }
                    }
                }
            }
        }
        // pawns' two sq move & kings' castling availability injection
        for(String pid : whitePawnsFirstMoveCount.keySet()){
            if(ind < whitePawnsFirstMoveCount.get(pid)){
                whitePawnsFirstMoveCount.put(pid, -1);
                ((Pawn) whitePieces.get(pid)).setTwoSquareMovable();
            }
        }
        for(String pid : blackPawnsFirstMoveCount.keySet()){
            if(ind < blackPawnsFirstMoveCount.get(pid)){
                blackPawnsFirstMoveCount.put(pid, -1);
                ((Pawn) blackPieces.get(pid)).setTwoSquareMovable();
            }
        }
        for(int co=0; co<noMoreCastlingMoveCount.length; co++){
            for(int sd=0; sd<noMoreCastlingMoveCount[co].length; sd++){
                if(ind < noMoreCastlingMoveCount[co][sd]){
                    noMoreCastlingMoveCount[co][sd] = -1;
                    Piece king = co == 0 ? whitePieces.get("K") : blackPieces.get("K");
                    switch(sd){
                        case 0 -> ((King)king).setKingsideCastlingAvailable();
                        case 1 -> ((King)king).setQueensideCastlingAvailable();
                    }
                }
            }
        }
        // reset draw check factors
        boolean reversibleCountDone = false;
        reversibleMoveStack = 0;
        fiftyMoveStack = 0;
        for(int i=ind-1; i>=0; i--){
            Move move = moves.get(i);
            if(move.getMovedPiece() instanceof Pawn || move.isCapture()) break;
            fiftyMoveStack++;
            if(reversibleCountDone) continue;
            if(move.isCastling()) reversibleCountDone = true;
            reversibleMoveStack++;
        }
        // set the color of turn
        this.turn = turn;
        // set isAttacked, movable, takeable & reachable
        allSquares.forEach((acn, sq) -> sq.setIsAttacked());
        for(String id : getPieces(turn).keySet()){
            Piece pc = getPieces(turn).get(id);
            if(pc.isTaken() || (pc instanceof Pawn && ((Pawn)pc).isPromoted())) continue;
            pc.setMovableTakeable();
        }
        allSquares.forEach((acn, sq) -> sq.setReachable());
        result = Integer.MIN_VALUE;
    }

    public void setNoMoreCastlingMoveCount(PieceColor color, int sideIndic){
        // sideIndic : 0, 1, 2 for Ks, Qs, both respectively
        switch(color){
            case WHITE :
                switch(sideIndic){
                    case 0, 1 :
                        if(noMoreCastlingMoveCount[0][sideIndic] < 0){
                            noMoreCastlingMoveCount[0][sideIndic] = moveCount+1;
                        }break;
                    default :
                        setNoMoreCastlingMoveCount(color, 0);
                        setNoMoreCastlingMoveCount(color, 1);
                }break;
            case BLACK :
                switch(sideIndic){
                    case 0, 1 :
                        if(noMoreCastlingMoveCount[1][sideIndic] < 0){
                            noMoreCastlingMoveCount[1][sideIndic] = moveCount+1;
                        }break;
                    default :
                        setNoMoreCastlingMoveCount(color, 0);
                        setNoMoreCastlingMoveCount(color, 1);
                }
        }
    }

    public void setPawnsTwoSquareMoveCount(PieceColor color, String pieceId){
        switch(color){
            case WHITE :
                if(whitePawnsFirstMoveCount.get(pieceId) < 0){
                    whitePawnsFirstMoveCount.put(pieceId, moveCount+1);
                }break;
            case BLACK :
                if(blackPawnsFirstMoveCount.get(pieceId) < 0){
                    blackPawnsFirstMoveCount.put(pieceId, moveCount+1);
                }
        }
    }

    /*
    public void undo(){
        // todo : undo the last move in this.moves
        if(lastMove == null){
            System.out.println("No more undoable move!");
            return;
        }

    }

    public void redo(){
        // todo : redo the last undone move
        if(lastMove == moves.getLast()){
            System.out.println("No more redoable move!");
            return;
        }

    }*/

    // ----------- getter & setter ------------

    public Square squareOn(int file, int rank){
        return allSquares.get(String.format("%c%d", 'a'+file, DIM-rank));
    }

    public int[][] getBoard(){
        return board;
    }

    public PieceColor getTurn(){
        return turn;
    }

    public ArrayList<Move> getMoves(){
        return moves;
    }

    public int getMoveCount(){
        return moveCount;
    }

    /*
    public HashMap<String, Square> getAllSquares(){
        return allSquares;
    }*/

    public HashMap<String, Piece> getPieces(PieceColor color){
        return switch(color){
            case WHITE -> whitePieces;
            case BLACK -> blackPieces;
        };
    }

    public ObservableList<MovePair> getMoveTableData(){
        return moveTableData;
    }

    public boolean inRange(int i){
        return 0 <= i && i < DIM;
    }

    public char pieceIntToChar(int pieceIndic){
        return switch(pieceIndic){
            case 1 -> 'P';
            case 2 -> 'R';
            case 3 -> 'N';
            case 4 -> 'B';
            case 5 -> 'Q';
            case 6 -> 'K';
            case -1 -> 'p';
            case -2 -> 'r';
            case -3 -> 'n';
            case -4 -> 'b';
            case -5 -> 'q';
            case -6 -> 'k';
            /* case 0 */
            default -> '.';
        };
    }

    // -------------- for printing on console ---------------

    public void printBoard(boolean flipBoard){
        // StringBuilder sb = new StringBuilder();
        if(turn == PieceColor.BLACK && flipBoard){
            for(int r=DIM-1; r>=0; r--){
                for(int f=DIM-1; f>=0; f--){
                    System.out.print(pieceIntToChar(board[r][f]));
                    // sb.append(pieceIntToChar(board[r][f]));
                    if(f > 0)
                        System.out.print(" ");
                }System.out.println();
            }
        }else{
            for(int r=0; r<DIM; r++){
                for(int f=0; f<DIM; f++){
                    System.out.print(pieceIntToChar(board[r][f]));
                    // sb.append(pieceIntToChar(board[r][f]));
                    if(f < DIM-1)
                        System.out.print(" ");
                }System.out.println();
            }
        }// System.out.println(sb);
        System.out.println();
    }

    public void printInstanceBoard(boolean flipBoard){
        // todo : correct printing format(especially new line)
        int[][] instBoard = new int[DIM][DIM];
        whitePieces.forEach((id, pc) -> {
            if(!(pc.isTaken() || (pc instanceof Pawn && ((Pawn)pc).isPromoted()))){
                // String pcid = pc.toString();
                switch(id.charAt(id.length()-1)){
                    case 'K' -> instBoard[pc.rank()][pc.file()] = 6;
                    case 'Q' -> instBoard[pc.rank()][pc.file()] = 5;
                    case 'R' -> instBoard[pc.rank()][pc.file()] = 2;
                    case 'N' -> instBoard[pc.rank()][pc.file()] = 3;
                    case 'B' -> instBoard[pc.rank()][pc.file()] = 4;
                    // case 'P'
                    default -> instBoard[pc.rank()][pc.file()] = 1;
                }
            }
        });
        blackPieces.forEach((id, pc) -> {
            if(!(pc.isTaken() || (pc instanceof Pawn && ((Pawn)pc).isPromoted()))) {
                // String pcid = pc.toString();
                switch (id.charAt(id.length() - 1)) {
                    case 'K' -> instBoard[pc.rank()][pc.file()] = -6;
                    case 'Q' -> instBoard[pc.rank()][pc.file()] = -5;
                    case 'R' -> instBoard[pc.rank()][pc.file()] = -2;
                    case 'N' -> instBoard[pc.rank()][pc.file()] = -3;
                    case 'B' -> instBoard[pc.rank()][pc.file()] = -4;
                    // case 'P'
                    default -> instBoard[pc.rank()][pc.file()] = -1;
                }
            }
        });
        StringBuilder sb = new StringBuilder();
        if(turn == PieceColor.BLACK && flipBoard){
            for(int r=DIM-1; r>=0; r--){
                for(int f=DIM-1; f>=0; f--){
                    sb.append(pieceIntToChar(instBoard[r][f]));
                    sb.append(" ");
                }if(r > 0){
                    sb.append("\n");
                }/*else{
                    sb.append("\n");
                }*/
            }
        }else{
            for(int r=0; r<DIM; r++){
                for(int f=0; f<DIM; f++){
                    sb.append(pieceIntToChar(instBoard[r][f]));
                    sb.append(" ");
                }if(r < DIM-1){
                    sb.append("\n");
                }/*else{
                    sb.append("\n");
                }*/
            }
        }System.out.println(sb);
    }

    public void printPGN(){
        int turn=1;
        StringBuilder sb = new StringBuilder();
        int len = 0;
        for (Move mv : moves) {
            if (++turn % 2 == 0){
                String tn = turn/2 + ".";
                sb.append(tn);
                len+=tn.length();
            }
            sb.append(mv.getAlgebraicNotation()).append(' ');
            len+=(mv.getAlgebraicNotation().length()+1);
            if(turn%2 != 0 && len >= 65) {
                sb.append("\n");
                len = 0;
            }
        }
        switch(result){
            case 1 -> sb.append("1-0");
            case 0 -> sb.append("1/2-1/2");
            case -1 -> sb.append("0-1");
        }
        System.out.println(sb);
    }
}
