package LIS_Server;

import Classes.Message;
import Parser.Parse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static Classes.Cofigure.*;

/**
 * Created by rkurbanov on 27.02.2017.
 */
public class Server {


    private ServerSocket server;

    public static void main(String[] args) throws IOException {
        new Server();
    }

    public Server() throws IOException {

        UpdateConfiguration();
        server = new ServerSocket(Integer.parseInt(port));
        System.out.println(port);
        while (true) {
            Socket socket = server.accept();
            ServerThread con = new ServerThread(socket);

            System.out.println(socket.getInetAddress().getHostName()+ " connected");
            con.start();

        }

    }
    private class ServerThread extends Thread {

        private PrintStream os;//передача
        private BufferedReader is;//чтение
        private InetAddress addr;//адрес клиента
        private PrintWriter out;

        public ServerThread(Socket s) throws IOException {

            os = new PrintStream(s.getOutputStream(),true,"UTF-8");
            is = new BufferedReader(new InputStreamReader(s.getInputStream(),StandardCharsets.UTF_8));

            out = new PrintWriter(new OutputStreamWriter(
                    s.getOutputStream(), StandardCharsets.UTF_8), true);
            addr = s.getInetAddress();

        }

        @Override
        public void run() {
            String str,temp="";
            try {
                int chk=0;
                while ((str = is.readLine()) != null) {

                    if(Parse.checkForWord(str,Parse.start)){
                        temp=str;
                    }

                    if(!Parse.checkForWord(str,Parse.start) && !Parse.checkForWord(str,Parse.finish)){
                        temp+=str;
                    }

                    if(Parse.checkForWord(str,Parse.finish)){
                        temp+=str;
                        List<Message>messages= Parse.doParse(temp);
                        //System.out.println(messages.get(0).getBarcode());
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        factory.setNamespaceAware(true);
                        Document doc = factory.newDocumentBuilder().newDocument();
                        Element root = doc.createElement("Message");
                        doc.appendChild(root);
                        String tempNameDev="";

                        for (int j = 0; j < messages.size(); j++) {

                            tempNameDev =messages.get(j).getDeviceModel();
                            Element barcode = doc.createElement("Barcode");


                            if( Parse.checkForWord(messages.get(j).getBarcode(),"null")
                                    || Parse.checkForWord(messages.get(j).getBarcode(),"")){
                                chk=1;
                            }

                            barcode.setTextContent(messages.get(j).getBarcode());
                            root.appendChild(barcode);

                            for (int k = 0; k < messages.get(j).getResultOfAnalyzes().size(); k++) {

                                Element ResultOfAnalyze = doc.createElement("ResultOfAnalyze");
                                root.appendChild(ResultOfAnalyze);

                                Element Name = doc.createElement("Name");
                                Name.setTextContent(messages.get(j).getResultOfAnalyzes().get(k).getTestName());
                                ResultOfAnalyze.appendChild(Name);

                                Element Result = doc.createElement("Result");
                                Result.setTextContent(messages.get(j).getResultOfAnalyzes().get(k).getTestResult());
                                ResultOfAnalyze.appendChild(Result);
                            }
                        }


                        String namefile = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Calendar.getInstance().getTime());
                        String tempnamefile = savepath+namefile+"-"+tempNameDev;



                        if(chk==1){
                            tempnamefile =savebadpath+namefile+"-NoBarcode";
                        }
                        //если нет PID
                        if(Parse.checkForWord(tempnamefile,"null")){
                            tempnamefile =savebadpath+namefile+"-BAD";
                        }

                        File file = new File(tempnamefile+".xml");
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                        transformer.transform(new DOMSource(doc), new StreamResult(file));

                        try(FileWriter writer = new FileWriter(savepathtxt+"/BSresult.txt", true))
                        {
                            writer.write("<Message='"+tempnamefile+"'>\n");
                            writer.write(temp+"\n");
                            writer.write("</Message>\n");
                            writer.close();
                        }catch(IOException ex){
                            System.out.println(ex.getMessage());
                        }
                        out.println("\u000BMSH|^~\\&|LIS-Server|AMOKB|Mindray|"+messages.get(0).getDeviceModel()+"|20170227143601||ACK^R01|1|P|2.3.1|MSA|AA|1||||0|");
                        out.println("\u001C");
                    }
                    System.out.println(addr.getHostName()+": " + str);
                }

            } catch (Exception e) { e.printStackTrace();}
            finally {
                disconnect();
            }

        }

        public void disconnect() {
            try {
                System.out.println(addr.getHostName()+"("+addr.getHostAddress()+") are disconnected");
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.interrupt();
            }
        }
    }
}
