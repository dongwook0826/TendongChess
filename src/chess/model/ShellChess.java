package chess.model;

import java.util.Scanner;

public class ShellChess {
    public static void main(String[] args){
        System.out.println("******************************");
        System.out.println("*                            *");
        System.out.println("*       A Mug of Chess       *");
        System.out.println("*                            *");
        System.out.println("*      Made by. TenDong      *");
        System.out.println("*                            *");
        System.out.println("******************************");

        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("New game? (Y/n) : ");
            String flow = sc.nextLine();
            if(flow.length() <= 0 || (flow.charAt(0) != 'Y' && flow.charAt(0) != 'y')) break;
            ChessGame game = new ChessGame();
            String uciMove;
            boolean valid;
            while(game.getResult() > 1 || game.getResult() < -1){
                game.printBoard(false);
                do {
                    System.out.printf("%s's move : ", game.getTurn().isWhite() ? "white" : "black");
                    uciMove = sc.nextLine();
                    // draw offer : 0000
                    // resign : ----
                    // takeback : <<<<
                    switch (uciMove) {
                        case "----" -> {
                            game.setResult(game.getTurn().opponent().toInteger(), true);
                            System.out.printf("%s resigned\n", game.getTurn().isWhite() ? "white" : "black");
                            valid = true;
                            continue;
                        }
                        case "0000" -> {
                            System.out.printf("%s offered a draw to %s; accept it?(Y/n) : ",
                                    game.getTurn().isWhite() ? "white" : "black",
                                    game.getTurn().opponent().isWhite() ? "white" : "black");
                            String accept = sc.nextLine();
                            if (accept.length() > 0 && (accept.charAt(0) == 'Y' || accept.charAt(0) == 'y')) {
                                game.setResult(0, true);
                                System.out.println("each side agreed to draw");
                                valid = true;
                            } else {
                                System.out.printf("%s refused the draw offer\n",
                                        game.getTurn().opponent().isWhite() ? "white" : "black");
                                valid = false;
                            }
                            continue;
                        }
                        case "<<<<" -> {
                            System.out.print("To which move do you want to take back? : ");
                            int tbMove = sc.nextInt(); // pos. for white, neg. for black
                            if (tbMove == 0) {
                                System.out.printf("invalid input : %d\n", tbMove);
                            } else {
                                game.takeback(Math.abs(tbMove), tbMove > 0);
                                System.out.printf("takeback complete : %s's move %d\n",
                                        tbMove > 0 ? "white" : "black", Math.abs(tbMove));
                                System.out.println("piece instances rearrangement complete");
                                System.out.println("instance board :");
                                game.printInstanceBoard(false);
                                System.out.println("array board :");
                                game.printBoard(false);
                            }
                            valid = false;
                            continue;
                        }
                    }
                    valid = game.move(uciMove);
                }while(!valid);
            } // game finished
            game.printBoard(false);
            int result = game.getResult();
            System.out.printf("Result : %s\n",
                    result == 0 ? "draw" :
                    result == 1 ? "white win" : "black win");
            System.out.print("\nPrint the PGN record?(Y/n) : ");
            String prt = sc.nextLine();
            if(prt.length() > 0 && (prt.charAt(0) == 'Y' || prt.charAt(0) == 'y'))
                game.printPGN();
        }
        System.out.println("Game finished; good bye!");
    }
}
