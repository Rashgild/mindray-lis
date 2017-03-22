package Classes;

/**
 * Created by rkurbanov on 27.02.2017.
 */
public class ResultOfAnalyze {

    private String resultOfAnalyzeId;
    private String testName;
    private String testResult;
    private String testUnit;//
    private String originalRestult;//
    private String time;

    public String getResultOfAnalyzeId() {
        return resultOfAnalyzeId;
    }

    public void setResultOfAnalyzeId(String resultOfAnalyzeId) {
        this.resultOfAnalyzeId = resultOfAnalyzeId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getTestUnit() {
        return testUnit;
    }

    public void setTestUnit(String testUnit) {
        this.testUnit = testUnit;
    }

    public String getOriginalRestult() {
        return originalRestult;
    }

    public void setOriginalRestult(String originalRestult) {
        this.originalRestult = originalRestult;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
