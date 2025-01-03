package casino.games;

import casino.Player;

import java.util.*;

public class SlotMachine implements Game{
    private final ArrayList<String> symbols = new ArrayList<>(List.of(
            "\uD83C\uDF52", // 🍒
            "\uD83C\uDF4B", // 🍋
            "\uD83D\uDD14", // 🔔
            "\u2B50",       // ⭐
            "\uD83C\uDF40", // 🍀
            "\uD83D\uDC8E"   // 💎
    ));

    private final Player player;

    public SlotMachine(Player player){
        this.player = player;
    }

    public double playGame() throws InterruptedException {
        double currentBet = getBet();
        printAnimation();

        int[] indexesOfFinalSpin = getFinalSpin();

        int indexOfDiamond = symbols.indexOf("\uD83D\uDC8E");

        if(indexesOfFinalSpin[0] == indexOfDiamond && indexesOfFinalSpin[1] == indexOfDiamond && indexesOfFinalSpin[2] == indexOfDiamond){
            System.out.println("You've matched three symbols, and they are all \uD83D\uDC8Es, so you've hit the jackpot and multiplied your bet by 100. Congrats!!!");
            return currentBet * 99;
        }

        if(indexesOfFinalSpin[0] == indexesOfFinalSpin[1] && indexesOfFinalSpin[1] == indexesOfFinalSpin[2]){
            System.out.println("You matched three symbols, you've won your bet times 10, congrats!");
            return currentBet * 9;
        }

        if(indexesOfFinalSpin[0] == indexesOfFinalSpin[1] || indexesOfFinalSpin[0] == indexesOfFinalSpin[2] || indexesOfFinalSpin[1] == indexesOfFinalSpin[2]){
            System.out.println("You matched two symbols, you've doubled your bet, congrats!");
            return currentBet;
        }

        System.out.println("You unfortunately lost this spin, please try again!");
        return currentBet * -1;
    }

    public double getBet(){
        while(true){
            System.out.println("How much would you like to bet on the next spin? To read the rules, type \"Rules\".");
            Scanner input = new Scanner(System.in);

            String userResponse = input.next();

            if(userResponse.equalsIgnoreCase("Rules")){
                printRules();
                continue;
            }

            try{
                double response = Double.parseDouble(userResponse);

                if(response > player.balance){
                    System.out.printf("Please enter a bet that is less than your balance of $%.2f \n", player.balance);
                    continue;
                }

                return response;
            }catch(NumberFormatException ex){
                System.out.println("Please only enter a number as your bet.");
            }
        }
    }

    public void printRules(){
        System.out.println("When you place a bet on the slot machine, the Console will shuffle through different combinations of the 6 symbols for 4 seconds before " +
                "stopping on a set of 3 symbols. \nIf 2 of the 3 symbols match, you will double your bet for that spin. If all 3 symbols, match you will win your bet" +
                "times 10. \nIf all 3 symbols are the \uD83D\uDC8E symbol, you will have hit the jackpot and you will win your bet times 100 for that spin. Happy spinning!\n");
    }

    private void printAnimation() throws InterruptedException {
        Long fourSecondsFromNow = System.currentTimeMillis() + 4000;

        while(System.currentTimeMillis() < fourSecondsFromNow){
            Collections.shuffle(symbols);
            int randomNumberOne = (int)(Math.random() * symbols.size());    //Pick 3 random indexes in symbols and print those symbols
            int randomNumberTwo = (int)(Math.random() * symbols.size());
            int randomNumberThree = (int)(Math.random() * symbols.size());
            System.out.println(symbols.get(randomNumberOne) + " | " + symbols.get(randomNumberTwo) + " | " + symbols.get(randomNumberThree));
            Thread.sleep(200);
        }
    }

    private int[] getFinalSpin(){
        int randomNumberOne = (int)(Math.random() * symbols.size());    //Pick 3 random indexes in symbols and print those symbols
        int randomNumberTwo = (int)(Math.random() * symbols.size());
        int randomNumberThree = (int)(Math.random() * symbols.size());
        System.out.println("\n\n\n\n\n\n\n\n\n\n" + symbols.get(randomNumberOne) + " | " + symbols.get(randomNumberTwo) + " | " + symbols.get(randomNumberThree));

        return new int[]{randomNumberOne, randomNumberTwo, randomNumberThree};
    }

    public String toString(){
        return "Slots";
    }
}
