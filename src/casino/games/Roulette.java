package casino.games;

import casino.Player;

import java.util.*;

public class Roulette implements Game{
    private ArrayList<Double> betAmounts;
    private ArrayList<String> bets;
    private boolean[] betsWon;
    private ArrayList<String> legalBets;
    private ArrayList<String> winningBets;
    private int[] redNumbers;
    private int[] blackNumbers;
    private Double amountWon;
    private Double totalAmountBetOnThisSpin;
    private final Player player;

    public Roulette(Player player){
        this.player = player;
    }

    public double playGame(){
        initializeMemberVariables();
        initializeLegalBetsArrayList();
        printBoard();
        placeChips();
        int winningNumber = spinWheel();
        initializeWinningBetsArrayList(winningNumber);
        resolveBets();
        return amountWon;
    }

    public double getBet(){
        double amountBet;

        while(true) {
            Scanner input = new Scanner(System.in);

            System.out.println("How much would you like to bet on your bet of " + bets.get(bets.size() - 1) + "?");

            try{
                amountBet = input.nextDouble();
                if(amountBet > player.balance - totalAmountBetOnThisSpin){
                    System.out.printf("You are attempting to bet more than your balance. Your current balance is $%.2f. Please try again.\n", player.balance - totalAmountBetOnThisSpin);
                    continue;
                }

                if(amountBet < 0.01){
                    System.out.println("Your bet must be at least $0.01. Please try again.");
                    continue;
                }

                betAmounts.add(amountBet);
                totalAmountBetOnThisSpin += amountBet;
                break;
            }catch(InputMismatchException ex){
                System.out.println("Please enter a number between 0.01 and " + player.balance);
            }
        }

        return amountBet;
    }

    public void printRules(){
        System.out.println("You will first be asked to place your chips on your first bet. Valid bets are number 0 - 36, \"00\", \"1st 12\", \"2nd 12\", \"3rd 12\", \"1 to 18\"\n" +
                "\"19to36\", \"EVEN\", \"ODD\", \"RED\", and \"BLACK\". Once you type what you want your bet to be, you will then be asked for how much money you'd like to bet on that bet.\n" +
                "You will be then be continuously asked to place your chips and supply a bet amount until you either bet all of your money, or type \"Spin\".\n" +
                "The roulette wheel will then spin, and land on a number 0 - 36 or on 00. If you bet on a number 0 - 36 or 00, and the roulette spin lands on that number, \n" +
                "you will win your bet times 36. 1st 12 wins if the spin lands on 1 - 12, 2nd 12 wins if the spin lands on 13 - 24, 3rd 12 wins if the spin lands on 25 - 36.\n" +
                "Best of luck!\n");
    }

    private void initializeMemberVariables(){
        betAmounts = new ArrayList<>();
        bets = new ArrayList<>();
        legalBets = new ArrayList<>();
        winningBets = new ArrayList<>();
        redNumbers = new int[]{1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        blackNumbers = new int[]{2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
        amountWon = 0.0;
        totalAmountBetOnThisSpin = 0.0;
    }

    //An ArrayList of Strings representing bets to place chips on
    private void initializeLegalBetsArrayList(){
        for(int i = 0; i < 37; i++){
            legalBets.add(i + "");
        }

        legalBets.add("00");
        legalBets.add("1st 12");
        legalBets.add("2nd 12");
        legalBets.add("3rd 12");
        legalBets.add("1to18");
        legalBets.add("EVEN");
        legalBets.add("RED");
        legalBets.add("BLACK");
        legalBets.add("ODD");
        legalBets.add("19to36");
    }

    private void printBoard(){
        System.out.print("\n\n|00 |");
        int num = 0;

        while(num < 3) {
            for (int i = 3 - num; i < 37; i += 3) {
                boolean redNumber = false;

                if (i > 12){
                    for(int redNum : redNumbers){
                        if(i == redNum){
                            redNumber = true;
                        }
                    }

                    if(redNumber)
                        System.out.print("\u001B[31m " + (i + "") + "\u001B[0m|");
                    else
                        System.out.print("\u001B[90m " + (i + "") + "\u001B[0m|");
                }
                else{
                    for(int redNum : redNumbers){
                        if(i == redNum){
                            redNumber = true;
                        }
                    }

                    if(redNumber)
                        System.out.print("\u001B[31m " + (i + "") + "\u001B[0m|");
                    else
                        System.out.print("\u001B[90m " + (i + "") + "\u001B[0m|");
                }
            }

            System.out.print("\u001B[0m\n");

            if(num == 0){
                System.out.print("|0  |");
            }

            if(num == 1){
                System.out.print("    |");
            }

            num++;
        }

        System.out.println("\u001B[4m    |   1st 12   |     2nd 12    |     3rd 12    |\u001B[0m");
        System.out.println("\u001B[4m    |1to18| EVEN |  RED  | BLACK |  ODD  | 19to36|\u001B[0m");
    }

    private void placeChips(){
        String response = "";
        int num = 0;

        while(true){
            Scanner input = new Scanner(System.in);

            if(player.balance - totalAmountBetOnThisSpin <= 0){
                System.out.println("You have gone all-in on this spin. Best of luck.");
                break;
            }

            System.out.println("Please type where you want to place your chips. To spin, type \"Spin\". To read the rules, type \"Rules\".");
            response = input.nextLine();

            if(response.equalsIgnoreCase("Spin"))
                break;

            if(response.equalsIgnoreCase("Rules")){
                printRules();
                continue;
            }

            int sizeOfBetsArrayListBeforeCheckingResponse = bets.size();

            for(String bet : legalBets){
                if(response.equalsIgnoreCase(bet)){
                    bets.add(response);
                    break;
                }
            }

            if(bets.size() == sizeOfBetsArrayListBeforeCheckingResponse)
                System.out.println("Your response is not a valid bet. Please try again.");
            else
                getBet();
        }
    }

    //37 represents "00"
    private int spinWheel(){
        int winningNumber = (int)(Math.random() * 38);      //If winningNumber = 37, the winning number is "00"

        if(winningNumber == 37){
            System.out.println("\n\nThe winning number is 00.");
            return winningNumber;
        }

        if(winningNumber == 0){
            System.out.println("\n\nThe winning number is 0.");
            return winningNumber;
        }

        for(int num : redNumbers){
            if(num == winningNumber){
                System.out.println("\n\nThe winning number is " + winningNumber + " RED");
                return winningNumber;
            }
        }

        System.out.println("\n\nThe winning number is " + winningNumber + " BLACK");

        return winningNumber;
    }

    private void initializeWinningBetsArrayList(int winningNumber){
        if(winningNumber == 0){
            winningBets.add("0");
            return;
        }

        if(winningNumber == 37){
            winningBets.add("00");
            return;
        }

        if(winningNumber < 13)
            winningBets.add("1st 12");

        if(winningNumber > 12 && winningNumber < 25)
            winningBets.add("2nd 12");

        if(winningNumber > 24)
            winningBets.add("3rd 12");

        if(winningNumber < 19)
            winningBets.add("1to18");

        if(winningNumber > 18)
            winningBets.add("19to36");

        if(winningNumber % 2 == 0)
            winningBets.add("EVEN");

        if(winningNumber % 2 == 1)
            winningBets.add("ODD");

        for(int redNumber : redNumbers){
            if(winningNumber == redNumber) {
                winningBets.add("RED");
                break;
            }
        }

        for(int blackNumber : blackNumbers){
            if(winningNumber == blackNumber){
                winningBets.add("BLACK");
                break;
            }
        }
    }

    private void resolveBets(){
        betsWon = new boolean[bets.size()];

        Arrays.fill(betsWon, false);

        for(int i = 0; i < bets.size(); i++){
            for(String winningBet : winningBets){
                if(bets.get(i).equalsIgnoreCase(winningBet)){
                    betsWon[i] = true;
                    break;
                }
            }
        }

        for(int i = 0; i < bets.size(); i++){
            if(betsWon[i]){
                String currentBetBeingResolved = bets.get(i);
                if(currentBetBeingResolved.equals("00")){
                    amountWon += 36 * betAmounts.get(i);
                    continue;
                }
                try{
                    Integer integer = Integer.parseInt(currentBetBeingResolved);    //If no NumberFormatException is thrown, the bet was on a number 0 - 36
                    amountWon += 36 * betAmounts.get(i);
                    continue;
                }catch(NumberFormatException ex) {}     //If a NumberFormatException is thrown, the bet was not on a number 0-36, continue to test for what bet was on

                if(currentBetBeingResolved.equalsIgnoreCase("1st 12") || currentBetBeingResolved.equalsIgnoreCase("2nd 12") || currentBetBeingResolved.equalsIgnoreCase("3rd 12")){
                    amountWon += 2 * betAmounts.get(i);
                    continue;
                 }

                amountWon += betAmounts.get(i);     //All other bets pay 1:1
            }
            else
                amountWon -= betAmounts.get(i);
        }
    }

    public String toString(){
        return "Roulette";
    }
}
