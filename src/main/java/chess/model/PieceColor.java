package chess.model;

public enum PieceColor {
    WHITE(true, 1), BLACK(false, -1);

    private final boolean boolColor;
    private final int intColor;

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
}
