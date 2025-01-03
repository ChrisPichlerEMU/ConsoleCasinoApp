package casino.deckOfCards;

public enum Suit {
    HEART("\u2665"),
    DIAMOND("\u2666"),
    CLUB("\u2663"),
    SPADE("\u2660");

    private final String symbol;

    Suit(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
