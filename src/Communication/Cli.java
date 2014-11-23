package Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cli implements Runnable{

	private Sender chatroomSender;

	private Receiver chatroomReceiver;

	private Mail mail;

	private boolean connectedToChatroom=false;

	private BufferedReader stdIn;

	public Cli() {
		stdIn = new BufferedReader(new InputStreamReader(System.in));
	}

	public void connectToChatroom(String ip_broker, String username, String chatroom,String ip_user) {

		chatroomReceiver = new Receiver(username,ip_broker,chatroom);
		chatroomSender = new Sender(username,ip_broker,chatroom,ip_user);

		Thread chatroomRec = new Thread(chatroomReceiver);

		chatroomReceiver.start();
		chatroomRec.start();

		chatroomSender.start();


		connectedToChatroom=true;
	}

	public void exitChatroom() {

		this.chatroomSender.stop();
		this.chatroomReceiver.stop();
		this.mail.mailStop();
		connectedToChatroom = false;
	}

	public void exitProgram() {
		System.exit(0);
	}


	@Override
	public void run() {

		try {
			String input="";
			// System.out.println("vsdbchat ip_broker username chatroom eigene_ip");
			while(input != null) {
				input = stdIn.readLine();
				// System.out.println(input);
				input=input.trim();
				String[] inputInformation;

				if(input.contains("vsdbchat") && connectedToChatroom == false){
					inputInformation = input.split(" ");
					if(inputInformation.length == 5) {
						String ipbroker = "tcp://"+inputInformation[1]+":61616";
						this.connectToChatroom(ipbroker, inputInformation[2], inputInformation[3], inputInformation[4]);
						connectedToChatroom = true;
					}else {
						System.out.println('\n' + "Falsche Eingabe, bitte wie folgt eingeben");
						System.out.println("vsdbchat ip_broker username chatroom eigene_ip" + '\n');
					}
				}

				if (input.contains("MAIL")) {
					inputInformation = input.split(" ");
					String nachricht = "";
					if (inputInformation.length >= 3) {
						if (mail != null)
							mail = new Mail();

						for (int x = 2; x < inputInformation.length; x++)
							nachricht += inputInformation[x];

						mail.sendMail(inputInformation[2], nachricht);
					} else {
						System.out.println('\n' + "Falsche Eingabe, bitte wie folgt eingeben");
						System.out.printf("MAIL ip_des_benutzers nachricht" + '\n');
					}
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
