package casino.games;

import casino.Player;

import java.util.ArrayList;
import java.util.Scanner;

public class Keno implements Game{
    private final Player player;
    private boolean[] winningNumbers;
    private ArrayList<Integer> playerNumbers;
    private Integer numberOfMatches;

    public Keno(Player player){
        this.player = player;
        winningNumbers = new boolean[80];
    }

    public double playGame(){
        numberOfMatches = 0;
        double currentBet = getBet();

        initializeWinningNumbers();
        getPlayerNumbersFromPlayer();
        selectWinningNumbers();
        checkPlayerNumbers();
        printWinningNumbers();
        double amountWon = resolveBet(currentBet);

        System.out.print("You matched " + numberOfMatches + " out of " + playerNumbers.size() + ". ");

        if(amountWon < 0)
            System.out.println("Unfortunately you lost this round, please try again!\n");
        else
            System.out.println("You won " + amountWon + ", congrats!\n");

        return amountWon;
    }

    public double getBet(){
        double amountBet;

        while(true) {
            Scanner input = new Scanner(System.in);

            System.out.println("How much would you like to bet on the next game of Keno? Type \"Rules\" to show the rules to Keno.");

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
        System.out.println("You will first be asked for the wager on your next bet. Then, you will be asked to type 3, 5, or 10 numbers separated by a space between 1 and 80. 20 numbers out of the 80 numbers\n" +
                "between 1 and 80 will be selected as winning numbers, and your numbers will then be compared to the winning numbers. The payout for each game is as follows:\n" +
                "For 3 guesses: 0 and 1 match = No payout, 2 matches = Bet doubled, all 3 matching = Bet * 25 returned.\n" +
                "For 5 guesses: 0, 1, and 2 matches = No payout, 3 matches = Bet * 3 returned, 4 matches = Bet * 10 returned, all 5 matching = Bet * 250 returned.\n" +
                "For 10 guesses: 0, 1, 2, 3, 4, and 5 matches = No payout, 6 matches = Bet * 15 returned, 7 matches = Bet * 150 returned, 8 matches = Bet * 1,000 returned, 9 matches = Bet * 4,000 returned, all 10 matching = bet * 10,000 returned.\n" +
                "Happy betting!\n");
    }

    //Initialize each index of winningNumbers to false before each play
    private void initializeWinningNumbers(){
        for(int i = 0; i < 80; i++){
            winningNumbers[i] = false;
        }
    }

    private void getPlayerNumbersFromPlayer(){
        boolean validInput = false;

        while(!validInput){
            playerNumbers = new ArrayList<>();
            Scanner input = new Scanner(System.in);
            System.out.println("\nPlease select 3, 5, or 10 different numbers between 1 and 80, separated by a space:");

            String response = input.nextLine();

            String[] numbersInResponse = response.split("\\s+");

            if(!(numbersInResponse.length == 3 || numbersInResponse.length == 5 || numbersInResponse.length == 10)){
                System.out.println("\nPlease only type 3, 5, or 10 numbers.");
                continue;
            }

            boolean breakOuterLoop = false;

            try{
                for(int i = 0; i < numbersInResponse.length + 1; i++){
                    if(breakOuterLoop)      //Is true only if player selects two of same number, get new numbers from player
                        break;

                    if(i == numbersInResponse.length){
                        validInput = true;
                        break;
                    }

                    String nextNumber = numbersInResponse[i];
                    int num = Integer.parseInt(nextNumber);

                    if(num < 1 || num > 80){
                        System.out.println("\nPlease only enter numbers between 1 and 80 separated by a space.");
                        break;
                    }

                    for(int numberInPlayerNumbers : playerNumbers){
                        if(num == numberInPlayerNumbers){
                            System.out.println("Please don't select the same number more than once and try again.");
                            breakOuterLoop = true;
                            break;
                        }
                    }

                    playerNumbers.add(num);
                }
            }catch(NumberFormatException ex){
                System.out.println("\nPlease only enter numbers between 1 and 80 separated by a space.\n");
            }
        }
    }

    private void selectWinningNumbers(){
        for(int i = 0; i < 20; i++){
            int randomNumber = (int)(Math.random() * 80);       //Random number between 0 and 79

            if(winningNumbers[randomNumber]){       //If this number has already been selected, select a new number and don't count this as 1 of the 20 selected winners
                i--;
                continue;
            }

            winningNumbers[randomNumber] = true;       //Index i becomes a winning number
        }
    }

    private void checkPlayerNumbers(){
        for(int i = 0; i < playerNumbers.size(); i++){
            if(winningNumbers[playerNumbers.get(i) - 1])
                numberOfMatches++;
        }
    }

    private void printWinningNumbers(){
        System.out.print("Winning numbers: ");
        for(int i = 0; i < winningNumbers.length; i++){
            if(winningNumbers[i])
                System.out.print((i + 1) + " ");
        }
    }

    private double resolveBet(double currentBet){
        if(playerNumbers.size() == 3){
            if(numberOfMatches < 2)
                return currentBet * -1;

            if(numberOfMatches == 2)
                return currentBet;

            return currentBet * 25;     //All 3 numbers are winners
        }
        else if(playerNumbers.size() == 5){
            if(numberOfMatches < 3)
                return currentBet * -1;

            if(numberOfMatches == 3)
                return currentBet * 2;

            if(numberOfMatches == 4)
                return currentBet * 10;

            return currentBet * 250;    //All 5 numbers are winners
        }
        else{       //playerNumbers.size() = 10
            if(numberOfMatches < 6)
                return currentBet * -1;

            if(numberOfMatches == 6)
                return currentBet * 15;

            if(numberOfMatches == 7)
                return currentBet * 150;

            if(numberOfMatches == 8)
                return currentBet * 1000;

            if(numberOfMatches == 9)
                return currentBet * 4000;

            return currentBet * 10000;  //All 10 numbers are winners
        }
    }

    public String toString(){
        return "Keno";
    }
}
