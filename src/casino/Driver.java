package casino;

public class Driver {
    public static void main(String[] args) throws InterruptedException {
        Casino casino = new Casino(new Player());
        casino.runSimulation();
    }
}
