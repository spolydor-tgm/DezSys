package Communication;

public class Reciver implements Runnable {

	private Session session;

	private Connection connection;

	private MessageProducer producer;

	private Destination destination;

	private String user;

	private String password;

	private String url;

	private String chatroom;

	private boolean runState;

	public Reciver(String user, String password, String url) {

	}

	public void Reciver(String user, String password, String url, String chatroom) {

	}

	public void start() {

	}

	public void stop() {

	}

}
