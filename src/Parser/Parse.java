package Parser;

import Classes.Message;
import Classes.ResultOfAnalyze;
import LIS_Client.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by rkurbanov on 27.02.2017.
 */
public class Parse {

    public static String start="\u000BMSH";
    public static String finish="\u001C";

    public static List<List<String>>  parse(String str)
    {
        str=str.replace('|', '/');
        String[] parts = str.split("/");
        List<String> list = new ArrayList<>();
        List<List<String>> lists = new ArrayList<>();

        for (int i = 0; i < parts.length; i++) {
            if(parts[i].equals(start)) {
                while (true)
                {
                    if(parts[i].equals(finish) || parts[i].equals("F"+finish)){
                        break;
                    }
                    list.add(parts[i]);
                    i++;
                }
            }
            lists.add(list);
            list = new ArrayList<>();
        }
        return lists;
    }


    private static int whatIsProfile(List<List<String>> lists)
    {
        for (int i = 0; i < lists.size(); i++) {
            for (int j = 0; j < lists.get(i).size(); j++) {

                if(checkForWord(lists.get(i).get(j),"BS-300")){
                    return 300;
                }
                if(checkForWord(lists.get(i).get(j),"BS-400")){
                    return 400;
                }
                if(checkForWord(lists.get(i).get(j),"BC-5300")){
                    return 5300;
                }
            }
        }
        return 0;
    }

    private static String[] Remove = {"08001^Take Mode^99MRC","08002^Blood Mode^99MRC","08003^Test Mode^99MRC",
            "01002^Ref Group^99MRC","6690-2^WBC^LN","704-7^BAS#^LN","706-2^BAS%^LN","751-8^NEU#^LN",
            "770-8^NEU%^LN","711-2^EOS#^LN","713-8^EOS%^LN","731-0^LYM#^LN","736-9^LYM%^LN",
            "742-7^MON#^LN","5905-5^MON%^LN","26477-0^*ALY#^LN","13046-8^*ALY%^LN",
            "10000^*LIC#^99MRC","10001^*LIC%^99MRC","789-8^RBC^LN","718-7^HGB^LN",
            "787-2^MCV^LN","785-6^MCH^LN","786-4^MCHC^LN","788-0^RDW-CV^LN",
            "21000-5^RDW-SD^LN","4544-3^HCT^LN","777-3^PLT^LN","32623-1^MPV^LN",
            "32207-3^PDW^LN","10002^PCT^99MRC"};
    private static String[] Replace = {"TakeMode","BloodMode","TestMode","RefGroup","WBC",
            "BAS#","Bas%","Neu#","Neu%","Eos#","Eos%","Lym#","Lym%","Mon#","Mon%","Aly#",
            "Aly%","Lic#","Lic%","RBC","HGB","MCV","MCH","MCHC","RDW-CV","RDW-SD",
            "HCT","PLT","MPV","PDW","PCT"};




    public static List<Message> doParse(String str)
    {
        List<List<String>> lists = parse(str);
        int profile=whatIsProfile(lists);
        System.out.println(profile);
        Message message = new Message();
        List<ResultOfAnalyze> resultOfAnalyzes = new ArrayList<>();
        List<Message>messages = new ArrayList<>();
        int pid = 0;
        for (int i = 0; i < lists.size(); i++) {
            message = new Message();
            for (int j = 0; j < lists.get(i).size(); j++) {


                if(checkForWord(lists.get(i).get(j),"PID")){pid=1; System.out.println("pid!!");}

                if(checkForWord(lists.get(i).get(j),"OBR") && pid==1){
                    if(profile==5300) {
                        message.setDeviceModel("BS5300");
                        message.setBarcode(lists.get(i).get(j + 3));}

                    if(profile==400 || profile==300) {
                        message.setDeviceModel("BS400");
                        message.setBarcode(lists.get(i).get(j + 2));}
                        pid=0;
                }


                if(checkForWord(lists.get(i).get(j),"OBX")){
                    ResultOfAnalyze resultOfAnalyze = new ResultOfAnalyze();
                    if(profile==5300) {
                        for (int k = 0; k < Remove.length; k++) {
                            if (lists.get(i).get(j + 3).equals(Remove[k])) {
                                resultOfAnalyze.setTestName(Replace[k]);
                            }
                        }
                        resultOfAnalyze.setTestResult(lists.get(i).get(j + 5));
                    }

                    if(profile==400 || profile==300) {
                        resultOfAnalyze.setTestName(lists.get(i).get(j + 4));
                        resultOfAnalyze.setTestResult(lists.get(i).get(j + 5));
                    }
                    resultOfAnalyzes.add(resultOfAnalyze);
                }
                message.setResultOfAnalyzes(resultOfAnalyzes);
            }
        }
        messages.add(message);
        return messages;
    }

    public static boolean checkForWord(String line, String word){
        return line.contains(word);
    }
}
