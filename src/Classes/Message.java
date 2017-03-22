package Classes;

import java.util.List;

/**
 * Created by rkurbanov on 27.02.2017.
 */
public class Message {
    private String deviceModel;
    private String sysemDate;
    private String MessageType;
    private String MessageId;
    private String Barcode;

    private List<ResultOfAnalyze> resultOfAnalyzes;

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getSysemDate() {
        return sysemDate;
    }

    public void setSysemDate(String sysemDate) {
        this.sysemDate = sysemDate;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public List<ResultOfAnalyze> getResultOfAnalyzes() {
        return resultOfAnalyzes;
    }

    public void setResultOfAnalyzes(List<ResultOfAnalyze> resultOfAnalyzes) {
        this.resultOfAnalyzes = resultOfAnalyzes;
    }
}
