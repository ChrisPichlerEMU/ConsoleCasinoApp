package casino.games;

import casino.Player;
import casino.deckOfCards.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Blackjack implements Game{
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;
    private int playerHandTotal;
    private int dealerHandTotal;
    private int acesInPlayersHand;
    private int acesInDealersHand;
    private final Player player;

    public Blackjack(Player player) {
        this.player = player;
    }

    //Method returns how much player won (returns negative value if player lost)
    public double playGame(){
        double currentBet = getBet();
        double amountWon = 0;

        //Initialize and shuffle deck of cards
        Deck deck = new Deck();
        deck.initializeDeck();
        deck.shuffle();

        //Re-initialize all member variables involving hand
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        playerHandTotal = 0;
        dealerHandTotal = 0;
        acesInPlayersHand = 0;
        acesInDealersHand = 0;
        int numberOfHands = 1;

        //Deal initial cards to dealer and player
        dealerHand.add(deck.deck.poll());
        dealerHand.add(deck.deck.poll());
        playerHand.add(deck.deck.poll());
        playerHand.add(deck.deck.poll());

        calculateHandTotals();
        printDealerHandFirstCard();
        printPlayerHand();

        boolean playerBoughtInsurance;

        if(dealerHand.get(0).number.equals("A")){       //Dealer has an Ace showing, offer the Player insurance
            playerBoughtInsurance = getPlayerInsuranceDecision();

            if(dealerHandTotal == 21){      //Dealer has a 21, give result of hand and exit method
                System.out.println("\nThe dealer has a blackjack!");
                printDealerHandFull();
                printPlayerHand();
                if(playerBoughtInsurance){      //Player bought insurance
                    if(playerHandTotal == 21) {     //Player also has blackjack
                        System.out.println("Because you bought insurance, you have doubled your insurance bet. Also, because you also have a blackjack, the hand result is a push.");
                        return currentBet * 0.5;
                    }
                    else {      //Player doesn't have a blackjack
                        System.out.println("Because you bought insurance, you have doubled your insurance bet. You do not have a blackjack, so you have lost the hand.");
                        return currentBet * -0.5;
                    }
                }
                else{       //Player didn't buy insurance
                    if(playerHandTotal == 21){      //Player has a blackjack
                        System.out.println("Because you did not buy insurance, the dealer has a blackjack, and you have a blackjack, this hand is a push.");
                        return 0;
                    }
                    else{       //Player doesn't have a blackjack
                        System.out.println("Because you did not buy insurance, the dealer has a blackjack, and you do not have a blackjack, you have lost this hand.");
                        return currentBet * -1;
                    }
                }
            }
            else {      //Dealer has an Ace showing but doesn't have a blackjack
                if(playerBoughtInsurance){
                    System.out.println("The dealer does not have a blackjack. Because you bought insurance, you have lost your insurance side bet.");
                    amountWon -= currentBet * 0.5;
                }
                else{
                    System.out.println("The dealer does not have a blackjack.");
                }
                printDealerHandFirstCard();
                printPlayerHand();
            }
        }

        boolean playerSplitTheirHand = false;

        if(playerHand.get(0).number.equals(playerHand.get(1).number)){     //Give the user the option to split their hand if they can afford to double their bet
            if(player.balance >= currentBet * 2){   //Give the user the option to split their hand
                playerSplitTheirHand = getPlayerSplitDecision();
            }
            else{       //The user can't afford to split their hand
                System.out.println("\nYou cannot split your hand because you cannot afford to double your bet.");
            }
        }

        double secondBet = 0; //Used for the second hand in a hand that is split into two hands

        Card startingCardOfSecondHand = null;

        if(playerSplitTheirHand){
            secondBet = currentBet;
            numberOfHands++;
            startingCardOfSecondHand = playerHand.get(1);
            playerHand.remove(1);
            playerHand.add(deck.deck.poll());
        }

        boolean playerChoseToStand = false;
        boolean playerBusted = false;
        int gameNumber = 1;
        ArrayList<Card> firstHand = new ArrayList<>();
        ArrayList<Card> secondHand = new ArrayList<>();
        boolean firstHandBusted = false;
        boolean proceedToDealerHandResolve = false;

        while(numberOfHands > 0) {      //The player did not split their hand
            if (gameNumber == 2) {
                for (Card card : playerHand) {
                    firstHand.add(card);
                }

                if(playerBusted)
                    firstHandBusted = true;

                playerHand = new ArrayList<>();
                playerHand.add(startingCardOfSecondHand);
                playerHand.add(deck.deck.poll());
                playerChoseToStand = false;
                playerBusted = false;
            }
            if ((player.balance - currentBet - secondBet) >= currentBet) {      //The player has the option to double down
                char playerDecision = getPlayerHitDecisionFirstDecision();

                if (playerDecision == 'S') {      //Player chose to stand
                    playerChoseToStand = true;
                    proceedToDealerHandResolve = true;
                }
                else if (playerDecision == 'H') {     //Player chose to hit
                    playerHand.add(deck.deck.poll());
                    printPlayerHand();
                    calculatePlayerHandTotal();
                    if (playerHandTotal > 21)
                        playerBusted = true;
                } else if (playerDecision == 'D') {     //Player chose to double down
                    playerChoseToStand = true;

                    if(gameNumber == 1)
                        currentBet *= 2;
                    else    //If this is the second hand of a split hand, multiply secondBet by 2
                        secondBet *= 2;

                    playerHand.add(deck.deck.poll());
                    calculatePlayerHandTotal();
                    printPlayerHand();

                    if(playerHandTotal > 21)
                        playerBusted = true;
                    else
                        proceedToDealerHandResolve = true;

                } else {
                    throw new RuntimeException("The getPlayerHitDecisionFirstDecision() method returned a character other than 'S', 'H', or 'D'. Character returned = " + playerDecision);
                }
            }

            while (!playerChoseToStand && !playerBusted) {        //Ask the player to hit or stand until the player stands or the player busts
                char playerDecision = getPlayerHitDecision();

                if (playerDecision == 'S'){
                    playerChoseToStand = true;
                    proceedToDealerHandResolve = true;
                }
                else if (playerDecision == 'H') {
                    playerHand.add(deck.deck.poll());
                    calculatePlayerHandTotal();
                    printPlayerHand();
                    if (playerHandTotal > 21)
                        playerBusted = true;
                } else {
                    throw new RuntimeException("The getPlayerHitDecision() method returned a character other than 'S' or 'H'. Character returned = " + playerDecision);
                }
            }

            if (playerBusted) {       //The player busted, they lose this hand
                System.out.println("\nYou busted and have lost this hand.");

                if(gameNumber == 1)
                    amountWon -= currentBet;
                else        //If this is the second hand of a split hand, subtract the second bet value from amountWon
                    amountWon -= secondBet;
            }

            numberOfHands--;
            gameNumber++;
        }

        if(!proceedToDealerHandResolve)
            return amountWon;

        resolveDealersHand(deck);       //The dealer hits until their hand total is 17 or greater

        if(!playerSplitTheirHand) {
            printPlayerHand();
            printDealerHandFull();

            if (dealerHandTotal > 21) {       //The dealer busted, the player wins
                System.out.println("The dealer busted and you have won this hand!");
                amountWon += currentBet;
            }
            //Neither the player nor the dealer busted, compare totals
            else if (dealerHandTotal > playerHandTotal) {     //The dealer outscored the player, the player loses
                System.out.println("The dealer outscored you, you have lost this hand.");
                amountWon -= currentBet;
            } else if (playerHandTotal > dealerHandTotal) {        //The player outscored the dealer, the player wins
                System.out.println("You outscored the dealer, you have won this hand!");
                amountWon += currentBet;
            } else {       //The player and the dealer tied, the hand is a push
                System.out.println("You and the dealer tied. The hand result is a push.");
            }
        }
        else{   //Player chose to split, so resolve all hands that didn't bust
            for(Card card : playerHand){
                secondHand.add(card);
            }

            if(!firstHandBusted){   //Resolve first hand if it didn't bust
                playerHand = firstHand;
                calculatePlayerHandTotal();
                printPlayerHand();
                printDealerHandFull();

                if (dealerHandTotal > 21) {       //The dealer busted, the player wins
                    System.out.println("The dealer busted and you have won this hand!");
                    amountWon += currentBet;
                }
                //Neither the player nor the dealer busted, compare totals
                else if (dealerHandTotal > playerHandTotal) {     //The dealer outscored the player, the player loses
                    System.out.println("The dealer outscored you, you have lost this hand.");
                    amountWon -= currentBet;
                } else if (playerHandTotal > dealerHandTotal) {        //The player outscored the dealer, the player wins
                    System.out.println("You outscored the dealer, you have won this hand!");
                    amountWon += currentBet;
                } else {       //The player and the dealer tied, the hand is a push
                    System.out.println("You and the dealer tied. The hand result is a push.");
                }
            }

            if(!playerBusted){      //Resolve second hand if it didn't bust
                playerHand = secondHand;
                calculatePlayerHandTotal();
                printPlayerHand();
                printDealerHandFull();

                if (dealerHandTotal > 21) {       //The dealer busted, the player wins
                    System.out.println("The dealer busted and you have won this hand!");
                    amountWon += secondBet;
                }
                //Neither the player nor the dealer busted, compare totals
                else if (dealerHandTotal > playerHandTotal) {     //The dealer outscored the player, the player loses
                    System.out.println("The dealer outscored you, you have lost this hand.");
                    amountWon -= secondBet;
                } else if (playerHandTotal > dealerHandTotal) {        //The player outscored the dealer, the player wins
                    System.out.println("You outscored the dealer, you have won this hand!");
                    amountWon += secondBet;
                } else {       //The player and the dealer tied, the hand is a push
                    System.out.println("You and the dealer tied. The hand result is a push.");
                }
            }
        }

        return amountWon;
    }

    //Asks the player how much they want to bet and returns that value
    public double getBet() {
        double amountBet;

        while(true) {
            Scanner input = new Scanner(System.in);

            System.out.println("How much would you like to bet on the next game of Blackjack? Type \"Rules\" to show the rules to Blackjack.");

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
        System.out.println("The game will start by you betting a certain amount of money on the hand. The cards are shuffled, 2 cards are dealt to you, and 2 cards are dealt to the dealer. Both of the \n" +
                "cards dealt to you are dealt face-up, while only one of the cards dealt to the dealer are face-up. If the face-up card dealt to the dealer is an Ace, you are able to buy insurance.\n" +
                "If you buy insurance and the dealer's face-down card is a 10, Jack, Queen, or King, you will win half of your bet and will lose the hand, unless you have a blackjack, in which case you will tie\n" +
                "the hand. If you don't buy insurance and the dealer's face-down card is a 10, Jack, Queen, or King, you will lose the hand, unless you have a blackjack, in which case you will tie.\n" +
                "If the dealer's face-down card is any other card, the game will continue. If both of your cards are the same card, you will have the option to split (if you have enough money in your balance\n" +
                "to double your bet). If you split, you will split your initial hand into two hands, each with one of the cards in the original hand as well as a new card for each, and you will double your bet. \n" +
                "Cards 2 - 10 are worth their number, Jacks, Queens, and Kings are worth 10, and Aces are worth 1 or 11, whichever is more advantageous. You will then be asked to \n" +
                "hit, stand, or double down. If you double down, you will double your bet for that hand, be given one more card, then stand. If you hit, you will be given one card, and unless your hand is worth\n" +
                "more than 21, you will be able to continue to hit or stand. If you stand, your hand will be final and the dealer will then reveal their face-down card and hit until their hand total is 17 or\n" +
                "greater. If at any point your hand is worth more than 21, you will bust and automatically lose. If at any point the dealer's hand is worth more than 21, they will bust and you will win. If both\n" +
                "hands are worth 21 or less, the hand will the higher total wins. If you win, you will double your initial bet, if you lose you will lose your initial bet. If both hands are worth the same point\n" +
                "total, you will draw and your initial bet will be returned. Happy betting!\n");
    }

    //Asks the player if they would like to purchase insurance, returns true if they do, false if they don't
    private boolean getPlayerInsuranceDecision() {
        boolean playerDecision;

        while(true) {
            Scanner input = new Scanner(System.in);
            System.out.println("Would you like to buy insurance? Your wager will be equal to half of your bet. Type \"y\" for yes, type \"n\" for no.");

            String response = input.next();

            if(response.equals("y")) {
                playerDecision = true;
                break;
            }
            else if(response.equals("n")){
                playerDecision = false;
                break;
            }
            else{
                System.out.println("Please either type \"y\" or \"n\".");
            }
        }

        return playerDecision;
    }

    private void printPlayerHand() {
        System.out.print("Player hand: ");

        for(Card card : playerHand){
            System.out.print(card + " ");
        }
    }

    private void printDealerHandFirstCard() {
        System.out.print("Dealer hand: " + dealerHand.get(0) + "\n");
    }

    private void printDealerHandFull() {
        System.out.print("Dealer hand: ");

        for(Card card: dealerHand){
            System.out.print(card + " ");
        }
    }

    //Calculates the hand totals of the player and dealer, accounts for aces
    private void calculateHandTotals() {
        acesInPlayersHand = 0;
        acesInDealersHand = 0;
        playerHandTotal = 0;
        dealerHandTotal = 0;

        for(Card card : playerHand) {
            if(card.number.equals("A")){
                playerHandTotal += 11;
                acesInPlayersHand++;
            }
            else if(card.number.equals("J") || card.number.equals("Q") || card.number.equals("K"))
                playerHandTotal += 10;
            else
                playerHandTotal += Integer.parseInt(card.number);
        }

        while(playerHandTotal > 21 && acesInPlayersHand != 0){
            playerHandTotal -= 10;
            acesInPlayersHand--;
        }

        for(Card card: dealerHand) {
            if(card.number.equals("A")){
                dealerHandTotal += 11;
                acesInDealersHand++;
            }
            else if(card.number.equals("J") || card.number.equals("Q") || card.number.equals("K"))
                dealerHandTotal += 10;
            else
                dealerHandTotal += Integer.parseInt(card.number);
        }

        while(dealerHandTotal > 21 && acesInDealersHand != 0){
            dealerHandTotal -= 10;
            acesInDealersHand--;
        }
    }

    private void calculatePlayerHandTotal() {
        acesInPlayersHand = 0;
        playerHandTotal = 0;
        for(Card card : playerHand) {
            if(card.number.equals("A")){
                playerHandTotal += 11;
                acesInPlayersHand++;
            }
            else if(card.number.equals("J") || card.number.equals("Q") || card.number.equals("K"))
                playerHandTotal += 10;
            else
                playerHandTotal += Integer.parseInt(card.number);
        }

        while(playerHandTotal > 21 && acesInPlayersHand != 0){
            playerHandTotal -= 10;
            acesInPlayersHand--;
        }
    }

    private void calculateDealerHandTotal() {
        acesInDealersHand = 0;
        dealerHandTotal = 0;
        for(Card card: dealerHand) {
            if(card.number.equals("A")){
                dealerHandTotal += 11;
                acesInDealersHand++;
            }
            else if(card.number.equals("J") || card.number.equals("Q") || card.number.equals("K"))
                dealerHandTotal += 10;
            else
                dealerHandTotal += Integer.parseInt(card.number);
        }

        while(dealerHandTotal > 21 && acesInDealersHand != 0){
            dealerHandTotal -= 10;
            acesInDealersHand--;
        }
    }

    private boolean getPlayerSplitDecision() {
        boolean playerDecision;

        while(true) {
            System.out.println("Would you like to split your hand? Type \"y\" for yes, type \"n\" for no.");
            Scanner input = new Scanner(System.in);

            String response = input.next();

            if(response.equals("y")){
                playerDecision = true;
                break;
            }
            else if(response.equals("n")){
                playerDecision = false;
                break;
            }
            else{
                System.out.println("Invalid input. Type \"y\" to split your hand, type \"n\" to not split your hand.");
            }
        }

        return playerDecision;
    }

    //Options: Hit (H), Stand (S), Double Down (D)
    private char getPlayerHitDecisionFirstDecision() {
        printDealerHandFirstCard();
        printPlayerHand();

        while(true){
            Scanner input = new Scanner(System.in);
            System.out.println("Please decide whether you would like to hit, stand, or double down. To hit, type \"h\". To stand, type \"s\". To double down, type \"d\".");

            String response = input.next();

            if(response.equals("h"))
                return 'H';
            else if(response.equals("s"))
                return 'S';
            else if(response.equals("d"))
                return 'D';
            else
                System.out.println("Please only type \"h\", \"s\", or \"d\".");
        }
    }

    //Options: Hit (H), Stand (S)
    private char getPlayerHitDecision() {
        printDealerHandFirstCard();
        printPlayerHand();

        while(true){
            Scanner input = new Scanner(System.in);
            System.out.println("Please decide whether you would like to hit or stand. To hit, type \"h\". To stand, type \"s\".");

            String response = input.next();

            if(response.equals("h"))
                return 'H';
            else if(response.equals("s"))
                return 'S';
            else
                System.out.println("Please only type \"h\" or \"s\".");
        }
    }

    //Called when the user chooses to stand and the dealer hits until their hand total is 17 or greater
    private void resolveDealersHand(Deck deck) {
        calculateDealerHandTotal();

        while(dealerHandTotal < 17){
            dealerHand.add(deck.deck.poll());
            calculateDealerHandTotal();
        }
    }

    public String toString(){
        return "Blackjack";
    }
}
