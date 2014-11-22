package Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cli implements Runnable{

	private Sender chatroomSender;

	private Receiver chatroomReceiver;

	private boolean connectedToChatroom=false;

	private BufferedReader stdIn;

	public Cli() {
		stdIn = new BufferedReader(new InputStreamReader(System.in));
	}

	public void connectToChatroom(String ip_broker, String username, String chatroom,String ip_user) {

		chatroomReceiver = new Receiver(username,ip_broker,chatroom);
		chatroomSender = new Sender(username,ip_broker,chatroom,ip_user);

		Thread chatroomRec = new Thread(chatroomReceiver);
		//Thread chatroomSen = new Thread(chatroomSender);

		chatroomReceiver.start();
		chatroomRec.start();

		chatroomSender.start();
		//chatroomSen.start();

		connectedToChatroom=true;
	}

	public void exitChatroom() {
		this.chatroomSender.stop();
		this.chatroomReceiver.stop();
		connectedToChatroom = false;
	}

	public void exitProgram() {
		System.exit(0);
	}


	@Override
	public void run() {

		try {
			String input="";
			while(input != null) {
				input = stdIn.readLine();
				System.out.println(input);
				input=input.trim();
				String[] inputInformation;

				if(input.contains("vsdbchat") && connectedToChatroom == false){
					inputInformation = input.split(" ");
					this.connectToChatroom(inputInformation[1],inputInformation[2],inputInformation[3],inputInformation[4]);
					connectedToChatroom=true;
				}
				if (input.equals("EXIT") && connectedToChatroom == false) {
					this.exitProgram();
				}
				if (input.equals("EXIT") && connectedToChatroom == true) {
					this.exitChatroom();
				}

				if(connectedToChatroom) {
					chatroomSender.setText(input);
				}

			}
		} catch (IOException e) {

		}
	}

}
