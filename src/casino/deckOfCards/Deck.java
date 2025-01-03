package casino.deckOfCards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Deck {
    public Queue<Card> deck;

    public Deck() {
        deck = new LinkedList<>();
    }

    public void initializeDeck() {
        for(int i = 1; i < 14; i++) {
            if(i == 1)
                deck.offer(new Card(Suit.HEART, "A"));
            else if(i < 11)
                deck.offer(new Card(Suit.HEART, (i + "")));
            else if(i == 11)
                deck.offer(new Card(Suit.HEART, "J"));
            else if(i == 12)
                deck.offer(new Card(Suit.HEART, "Q"));
            else
                deck.offer(new Card(Suit.HEART, "K"));
        }

        for(int i = 1; i < 14; i++) {
            if(i == 1)
                deck.offer(new Card(Suit.DIAMOND, "A"));
            else if(i < 11)
                deck.offer(new Card(Suit.DIAMOND, (i + "")));
            else if(i == 11)
                deck.offer(new Card(Suit.DIAMOND, "J"));
            else if(i == 12)
                deck.offer(new Card(Suit.DIAMOND, "Q"));
            else
                deck.offer(new Card(Suit.DIAMOND, "K"));
        }

        for(int i = 1; i < 14; i++) {
            if(i == 1)
                deck.offer(new Card(Suit.CLUB, "A"));
            else if(i < 11)
                deck.offer(new Card(Suit.CLUB, (i + "")));
            else if(i == 11)
                deck.offer(new Card(Suit.CLUB, "J"));
            else if(i == 12)
                deck.offer(new Card(Suit.CLUB, "Q"));
            else
                deck.offer(new Card(Suit.CLUB, "K"));
        }

        for(int i = 1; i < 14; i++) {
            if(i == 1)
                deck.offer(new Card(Suit.SPADE, "A"));
            else if(i < 11)
                deck.offer(new Card(Suit.SPADE, (i + "")));
            else if(i == 11)
                deck.offer(new Card(Suit.SPADE, "J"));
            else if(i == 12)
                deck.offer(new Card(Suit.SPADE, "Q"));
            else
                deck.offer(new Card(Suit.SPADE, "K"));
        }
    }

    public void shuffle() {
        if(deck.isEmpty())
            return;

        List<Card> list = new ArrayList<>(deck);

        Collections.shuffle(list);

        deck = new LinkedList<>(list);
    }
}
