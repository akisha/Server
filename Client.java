import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket c = new Socket(InetAddress.getByName("127.0.0.1"), 3090);
        ClientP m = new ClientP(c);
        m.run();
    }

    private static class ClientP implements Runnable{

        private Socket c;
        private InputStream is;
        private OutputStream os;

        public ClientP(Socket c) throws IOException {
            this.c = c;
            this.is = c.getInputStream();
            this.os = c.getOutputStream();
        }

        public void run() {
            try {
                writeOutputStream();
                readInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void writeOutputStream() throws IOException {
            String s = "GET /server_client.html HTTP/1.0\r\n\r\n";
            os.write(s.getBytes());
            os.flush();
        }

        public void readInputStream() throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s = br.readLine();
            while (s != null) {
                System.out.println(s);
                s = br.readLine();
            }
        }
    }
}