package casino;

import casino.games.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Casino {
    public Player player;
    public ArrayList<Game> games;

    public Casino(Player player){
        this.player = player;
        games = new ArrayList<>();
    }

    public void runSimulation() throws InterruptedException {
        initializeGames();
        player.timeWhenEnteredCasino = System.currentTimeMillis();
        player.balance = 1000;
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("Welcome to the casino!");

        String playerResponse = "";

        while(player.balance > 0) {
            System.out.printf("Your current balance is: $%.2f. You've been playing for: %s.\n", player.balance, player.timeElapsedSinceEnteringCasino());
            int gameSelection = getGameSelection() - 1;

            if(gameSelection == -2){
                System.out.println("Thank you for coming to the casino. Please come back again soon and have a great day!");
                break;
            }

            Game currentGame = games.get(gameSelection);
            do {
                player.balance += currentGame.playGame();
                System.out.printf("Your current balance is: $%.2f. You've been playing for: %s.\n", player.balance, player.timeElapsedSinceEnteringCasino());
            } while (player.balance > 0 && getPlayerResponseToKeepPlaying(currentGame).equals("y"));
        }

        if(player.balance <= 0)
            System.out.println("You are unfortunately out of money, you've been kicked out of the casino. Please come back when you've replenished your funds. Have a great day!");

    }

    public void initializeGames() {
        games.add(new Blackjack(player));
        games.add(new SlotMachine(player));
        games.add(new Roulette(player));
        games.add(new War(player));
        games.add(new Keno(player));
    }

    public int getGameSelection() {
        System.out.println("\nPlease choose a number corresponding to the game you'd like to play, or type \"Exit\" to leave the casino:");
        int index = 0;
        for(Game game : games){
            System.out.println((index + 1) + ": " + game.toString());
            index++;
        }

        String response = "";
        int userSelection;
        String errorMessage = "Please enter a whole number between 1 and " + (games.size());

        while(true) {
            try{
                Scanner input = new Scanner(System.in);
                response = input.next();

                if(response.equalsIgnoreCase("Exit")){
                    return -1;
                }

                userSelection = Integer.parseInt(response);
                if(userSelection < 1 || userSelection > games.size()){
                    System.out.println(errorMessage);
                    continue;
                }

                break;

            }catch(NumberFormatException ex){
                System.out.println(errorMessage);
            }
        }

        return userSelection;
    }

    public String getPlayerResponseToKeepPlaying(Game game){
        while(true) {
            System.out.println("\nWould you like to continue playing " + game.toString() + "? Type \"y\" for yes, type \"n\" for no:");
            Scanner input = new Scanner(System.in);
            String response = input.next();
            if(response.equals("y") || response.equals("n"))
                return response;

            System.out.println("The only valid inputs are \"y\" and \"n\". Please try again.");
        }
    }
}
