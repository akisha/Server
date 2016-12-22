import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(3090);
        while (true) {
            Socket s = ss.accept();
            SocketProcessor sp = new SocketProcessor(s);
            sp.start();
        }
    }

    private static class SocketProcessor {

        private Socket s;
        private InputStream is;
        private OutputStream os;
        private String fileName;

        private SocketProcessor(Socket s) throws Throwable {
            this.s = s;
            this.is = s.getInputStream();
            this.os = s.getOutputStream();
        }

        public String readText(String fileName) throws IOException {
            File file = new File(fileName);
            String text = "";
            if (file.exists()) {
                FileReader fr = new FileReader(file);
                BufferedReader bfr = new BufferedReader(fr);
                String i;
                while ((i = bfr.readLine()) != null) {
                    text = text + i + "\r\n";
                }
            } else {
                text = "<html><h2>404</h2></html>";
            }
            return text;
        }

        private String readInputStream() throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s = br.readLine();
            String[] s1 = s.split(" ");
            String s2 = s1[1];
            if (s2.length() > 0) {
                fileName = s2.substring(1, s2.length());
            } else fileName = "";
            fileName = "index.htm";
            System.out.println(fileName);
            return fileName;
        }

        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: YarServer/2009-09-09\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            os.write(result.getBytes());
            os.flush();
        }

        public void start() throws Throwable {
            String out;
            out = readText(readInputStream());
            writeResponse(out);
            s.close();
        }
    }
}