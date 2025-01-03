package casino.deckOfCards;

public class Card {
    public Suit suit;
    public String number;

    public Card(Suit suit, String number) {
        this.suit = suit;
        this.number = number;
    }

    @Override
    public String toString() {
        return number + suit;
    }
}
