package Communication;

public class Sender implements Runnable {

	private Session session;

	private Connection connection;

	private MessageProducer producer;

	private Destination destination;

	private String user;

	private String password;

	private String url;

	private String chatroom;

	private boolean runState;

	public Sender(String user, String password, String url) {

	}

	public Sender(String user, String password, String url, String chatroom) {

	}

	public void start() {

	}

	public void stop() {

	}

}
