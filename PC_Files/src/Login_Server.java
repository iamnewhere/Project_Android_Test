import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
//import java.io.InputStreamReader;
import java.io.IOException;

public class Login_Server {
	private static ServerSocket ss;
	private static Socket cs;
	private static Scanner scn;
	private static String str;
	private static DataInputStream din;
	private static DataOutputStream dout;
	private static int count;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// Wait for client to connect on 8080
		System.out.print("Login-Server Online!!\nEnter no. of clients = ");
		scn = new Scanner(System.in);
		count = 0;
		FileWriter f = null;
		int n=scn.nextInt();
		try {
			ss = new ServerSocket(8080);
			System.out.println("IP - " + InetAddress.getLocalHost().getHostAddress() + "\nPort - 8080");
			while (n>0) {
				cs = ss.accept();
				count++;
				// Create a reader
				din = new DataInputStream(cs.getInputStream());
				dout = new DataOutputStream(cs.getOutputStream());
				f = new FileWriter("data.test");		// save no. of clients entered yet
				f.write(String.valueOf(count));
				// Get the client message
				str = din.readUTF();
				System.out.println(str);
				if (false)		//for debugging
					str = scn.nextLine();
				else
					str = "pass";
				dout.writeUTF(str);
				cs.close();
				f.close();
				n--;
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		try {
			ss.close();
			System.out.println("Connection Closed\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
