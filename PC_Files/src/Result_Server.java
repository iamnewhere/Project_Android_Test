import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
//import java.io.InputStreamReader;
import java.io.IOException;

public class Result_Server {
    private static ServerSocket ss;
    private static Socket cs;
    private static String str, ans, adm;
    private static Scanner fr;
    private static DataInputStream din;
    private static DataOutputStream dout;
    private static int mul[], n,mrk;

    public static void main(String[] args) throws IOException {
        // Wait for client to connect on 8080
        FileWriter fw = null;
        File f = null;

        f = new File("data.test");
        fr = new Scanner(f);
        int i,count = fr.nextInt();

        f = new File("ans.test");
        fr = new Scanner(f);
        n=fr.nextInt();
        ans = fr.next();
        mul=new int[n];
        for(i=0;i<n;i++)
            mul[i]=fr.nextInt();
        try {
            ss = new ServerSocket(8080);
            System.out.println("Result-Server Online!!");
            System.out.println("IP - " + InetAddress.getLocalHost().getHostAddress()
                    + "\nPort - 8080");
            fw = new FileWriter("Text_Result.txt");        //To print result to a file
            fw.write(" Result - \r\n\n Adm No.\t\tMarks");
            while (count > 0) {
                cs = ss.accept();
                // Create a reader
                din = new DataInputStream(cs.getInputStream());
                dout = new DataOutputStream(cs.getOutputStream());
                // Get the client message
                str = din.readUTF();
                mrk=0;
                for (i=0; i < ans.length(); i++)
                    if(str.charAt(i)==ans.charAt(i))
                        mrk+=mul[i];
                adm="";
                for(i=ans.length()+1;i<str.length();i++)
                    adm+=str.charAt(i);
                fw.append("\r\n " + adm+"\t\t"+mrk);
                System.out.println(adm+"\t\tMrks = "+mrk);
                dout.writeUTF("Marks Obtained = "+mrk);
                cs.close();
                count--;
            }
            fw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        try {
            ss.close();
            System.out.println("Connection Closed");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
