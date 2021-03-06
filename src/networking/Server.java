package networking;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import controllers.WorldController;

/**
 * Simple Server for distributing game information with Clients
 * @author neal and myles
 */

public class Server implements Runnable{

	private static final int USER_LIMIT = 4;
	public static final int PORT = 45812;
	private static final int MOVE_PORT = 45612;

	//host socket and also list of client sockets.
	private static ServerSocket serverMessageSocket;
	private static ServerSocket serverMoveSocket;



	//list of running serverprotocol threads. each protocol dealing with one connected user.
	// private static ServerMessagingProtocol[] users = new ServerMessagingProtocol[10];
	private static Connection[] users = new Connection[10];


	private static BufferedReader bufferedReader;
	private static String inputLine;

	//input and outputstreams for messaging
	private DataInputStream input;
	private DataOutputStream out;

	//input output object streams for move
	private ObjectInputStream moveInput;
	private ObjectOutputStream moveOutput;


	private WorldController world;

	public Server(WorldController world) throws IOException{

		this.world = world;

		// Initialise Server Socket
		initServer();

		// Wait for client
		




		//Wait for input/check for disconnect
		//waitForInput();
	}

	/**
	 * Initialise server
	 * Starts the server socket and not much else
	 */
	private void initServer() {


		try {

			//Print Information
			System.out.println("Simple Server - by Myles and Neal");
			System.out.println("IP Address : " + InetAddress.getLocalHost());
			System.out.println("MessageSocket : "+ PORT);
			System.out.println("MoveSocket : " + MOVE_PORT);
			System.out.println("-------------------------------------------");

			//Create server socket
			serverMessageSocket = new ServerSocket(PORT);
			serverMoveSocket = new ServerSocket(MOVE_PORT);

		} catch(Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Connect client
	 */

	@Override
	public void run() {
		// TODO Auto-generated method stub


		while(true){

			// Accept Client Connection
			Socket temp;
			try {
				temp = serverMessageSocket.accept();


				System.out.println("accepted message socket");

				Socket beast = serverMoveSocket.accept();

				System.out.println("accepted move socket");



				for(int i =0 ; i < 10; i++){
					System.out.println("got connection socket to client: "+ temp.getInetAddress());

					//create messsaging inputs and outputs
					input = new DataInputStream(temp.getInputStream());
					out = new DataOutputStream(temp.getOutputStream());

					//create movement inputs and outputs.
					moveOutput = new ObjectOutputStream(beast.getOutputStream());
					out.flush();
					moveInput = new ObjectInputStream(beast.getInputStream());






					//first checks if any previous connections have become unusable.
					if(users[i]!=null && !users[i].getMessageProt().getStatus() )
						//if not in use is destroyed.
						users[i]=null;


					//if this connection spot is vacant fills it with the incoming request.
					if(users[i] == null){





						users[i] = new Connection (new ServerMessagingProtocol(input, out, users, i),new ServerMovementProtocol(moveInput, moveOutput, users, i, world) );

						Thread messageThread = new Thread(users[i].getMessageProt());
						Thread moveThread = new Thread(users[i].getMoveProt());

						messageThread.start();
						moveThread.start();

						break;
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//just testing connections.



		}
	}
}

		//
		//	public static void main(String[] args) {
		//		try {
		//			new Server();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		//	}


