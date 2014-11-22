package Communication;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

public class Reciver implements Runnable {

	private Session session=null;

	private Connection connection=null;

	private MessageConsumer producer=null;

	private Destination destination=null;

	private String user;

	private String password;

	private String url;

	private String chatroom;

	private boolean runState;

	public Reciver(String user, String password, String url) {
		this.user=user;
	}

	public void Reciver(String user, String password, String url, String chatroom) {

	}

	public void start() {

	}

	public void stop() {

	}

	@Override
	public void run() {

	}
}
