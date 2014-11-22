package Communication;

public class Main {
	public static void main(String[] args) {
		Cli run = new Cli();
		Thread cliprog = new Thread(run);
		cliprog.start();
	}
}
