package chess.model;

public enum PieceColor {
    WHITE(true, 1), BLACK(false, -1);

    private boolean boolColor;
    private int intColor;

    PieceColor(boolean boolColor, int intColor) {
        this.boolColor = boolColor;
        this.intColor = intColor;
    }

    public PieceColor opponent(){
        return switch(this){
            case WHITE -> BLACK;
            case BLACK -> WHITE;
        };
    }

    public boolean isWhite(){
        return boolColor;
    }

    public int toInteger(){
        return intColor;
    }

    public static PieceColor boolToColor(boolean boolColor){
        if(boolColor) return WHITE;
        else return BLACK;
    }

    public static PieceColor intToColor(int index){
        if(index == 0) return WHITE;
        else return BLACK;
    }
}
