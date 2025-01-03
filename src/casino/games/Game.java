package casino.games;

public interface Game {
    double playGame() throws InterruptedException;
    double getBet();
    void printRules();
    String toString();
}
