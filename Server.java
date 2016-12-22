import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(3090);
        while (true) {
            Socket s = ss.accept();
            new Thread(new SocketProcessor(s)).start();
        }
    }

    private static class SocketProcessor implements Runnable {

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
            fileName = "index.html";
            System.out.println(s2);
            return fileName;
        }

        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Connection: continue\r\n\r\n";
            String result = response + s;
            os.write(result.getBytes());
            os.flush();
        }

        public void run() {
            try {
                String out = "";
                out = readText(readInputStream());
                writeResponse(out);
            } catch (Throwable t) {
            } finally {
                try {
                    s.close();
                } catch (Throwable t) {
                }
            }
        }
    }
}