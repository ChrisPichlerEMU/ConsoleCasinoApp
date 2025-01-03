package casino;

public class Player {
    public double balance;
    public long timeWhenEnteredCasino;

    public String timeElapsedSinceEnteringCasino() {
        long millisecondsInCasino = System.currentTimeMillis() - timeWhenEnteredCasino;

        if(millisecondsInCasino < 1000)
            return "0 seconds";

        int secondsInCasino = (int)(millisecondsInCasino / 1000);

        if(secondsInCasino < 60 && secondsInCasino != 1)
            return secondsInCasino + " seconds";
        else if(secondsInCasino == 1)
            return "1 second";

        int minutesInCasino = secondsInCasino / 60;
        secondsInCasino = secondsInCasino % 60;

        if(minutesInCasino < 60 && minutesInCasino != 1 && secondsInCasino != 1)
            return minutesInCasino + " minutes, " + secondsInCasino + " seconds";
        else if(minutesInCasino < 60 && minutesInCasino == 1 && secondsInCasino != 1)
            return minutesInCasino + " minute, " + secondsInCasino + " seconds";
        else if(minutesInCasino < 60 && minutesInCasino != 1 && secondsInCasino == 1)
            return minutesInCasino + " minutes, " + secondsInCasino + " second";
        else if(minutesInCasino < 60 && minutesInCasino == 1 && secondsInCasino == 1)
            return minutesInCasino + " minute, " + secondsInCasino + " second";

        int hoursInCasino = minutesInCasino / 60;
        minutesInCasino = minutesInCasino % 60;

        return hoursInCasino + " hours, " + minutesInCasino + " minutes, " + secondsInCasino;
    }
}
