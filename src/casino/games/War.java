package casino.games;

import casino.Player;
import casino.deckOfCards.Card;
import casino.deckOfCards.Deck;

import java.util.Scanner;

public class War implements Game{
    private final Player player;
    private final String[] priorityOrder;

    public War(Player player){
        this.player = player;
        priorityOrder = new String[]{"Ace", "King", "Queen", "Jack", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
    }

    public double playGame(){
        double currentBet = getBet();

        //Initialize and shuffle deck of cards
        Deck deck = new Deck();
        deck.initializeDeck();
        deck.shuffle();

        Card playerCard = deck.deck.poll();
        Card dealerCard = deck.deck.poll();

        System.out.println("Player card: " + playerCard);
        System.out.println("Dealer card: " + dealerCard);

        int playerWon = resolveGame(playerCard, dealerCard);

        if(playerWon == 0)      //Dealer won
            return currentBet * -1;

        if(playerWon == 1)      //Player won
            return currentBet;

        return 0.0;     //Draw
    }

    public double getBet(){
        double amountBet;

        while(true) {
            Scanner input = new Scanner(System.in);

            System.out.println("How much would you like to bet on the next game of War? Type \"Rules\" to show the rules to War.");

            String response = input.next();

            if(response.equalsIgnoreCase("Rules")){
                printRules();
                continue;
            }

            try{
                amountBet = Double.parseDouble(response);
                if(amountBet > player.balance){
                    System.out.printf("You are attempting to bet more than your balance. Your current balance is $%.2f. Please try again.\n", player.balance);
                    continue;
                }

                if(amountBet < 0.01){
                    System.out.println("Your bet must be at least $0.01. Please try again.");
                    continue;
                }

                break;
            }catch(NumberFormatException ex){
                System.out.println("Please enter a number between 0.01 and " + player.balance);
            }
        }

        return amountBet;
    }

    public void printRules(){
        System.out.println("You will first be asked to place a wager on the next game. Once a wager is placed, you will be dealt one card, and the dealer will be dealt\n" +
                "one card. If your card has a higher priority, you will win and double your bet. If the dealer's card has a higher priority, they will win and you\n" +
                "will lose your bet. If both cards are teh same card, you will tie and your bet will be returned to you. Card priorities are in the following order:\n" +
                "Ace, King, Queen, Jack, 10, 9, 8, 7, 6, 5, 4, 3, 2. Happy betting!\n");
    }

    //0 = dealer win, 1 = player win, 2 = draw
    private int resolveGame(Card playerCard, Card dealerCard){
        int playerCardPriority = -1;
        int dealerCardPriority = -1;

        for(int i = 0; i < priorityOrder.length; i++){
            if(priorityOrder[i].equals(playerCard.number))
                playerCardPriority = i;

            if(priorityOrder[i].equals(dealerCard.number))
                dealerCardPriority = i;
        }

        if(dealerCardPriority < playerCardPriority)
            return 0;
        else if(playerCardPriority < dealerCardPriority)
            return 1;
        else
            return 2;
    }

    public String toString(){
        return "War";
    }
}
