import model.TestResult;
import org.testng.*;
import org.testng.xml.XmlSuite;
import webprobe.utils.Assert;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NgReporter implements IReporter {


    public static String getWindows1251StringFromFile(String pathToFile){
        Path file = Paths.get(pathToFile);
        Charset windows1251 = Charset.forName("windows-1251");
        try {
            return new String(Files.readAllBytes(file), windows1251);
        } catch (IOException e) {
            Assert.pageAssert("Построение отчета: не удалось openPage файл c шаблоном " + e.getMessage());
        }
        return "";
    }

    public static void writeString1251ToFile(String str, String pathToFile){
        try {
            Writer out = new OutputStreamWriter(new FileOutputStream(pathToFile), "windows-1251");
            out.write(str);
            out.close();
        }catch (Exception e){
            Assert.pageAssert("Построение отчета: ошибка при создании файла отчета " + e.getMessage());
        }
    }

    private TestResult getTestResultFormITestResult(ITestResult iTestResult, int status){
        TestResult testResult = new TestResult();
        testResult.status = status;
        testResult.testName = iTestResult.getName();
        if (iTestResult.getAttribute("stepName") != null) {
            testResult.stepName = iTestResult.getAttribute("stepName").toString();
        }
        if (iTestResult.getThrowable() != null) {
            if (iTestResult.getThrowable().getLocalizedMessage() != null){
                testResult.log = iTestResult.getThrowable().getLocalizedMessage();
            }else if(iTestResult.getThrowable().getMessage() != null){
                testResult.log = iTestResult.getThrowable().getMessage();
            }else testResult.log = "*** Empty exception message ****";

        }

        if(status == TestResult.SKIPPED){
            testResult.duration = "0";
        }else {
            Integer duration = Math.round(iTestResult.getEndMillis()-iTestResult.getStartMillis())/1000;
            testResult.duration = duration.toString();
        }

        return testResult;
    }

    private Map<String, ArrayList<TestResult>> parseTestResults(List<ISuite> suiteList){
        Map<String, ArrayList<TestResult>> testResultsMap = new HashMap<>();

        for(ISuite suite : suiteList){
            Map<String, ISuiteResult> suiteResultMap = suite.getResults();

            for (Map.Entry<String, ISuiteResult> entry : suiteResultMap.entrySet()){
                ArrayList<TestResult> testResults = new ArrayList<>();
                ITestContext testContext = entry.getValue().getTestContext();

                testContext.getSkippedTests().getAllResults().forEach(iTestResult ->
                        testResults.add(getTestResultFormITestResult(iTestResult, TestResult.SKIPPED)));

                testContext.getFailedTests().getAllResults().forEach(iTestResult ->
                        testResults.add(getTestResultFormITestResult(iTestResult, TestResult.FAILED)));


                testContext.getPassedTests().getAllResults().forEach(iTestResult ->
                        testResults.add(getTestResultFormITestResult(iTestResult, TestResult.PASSED)));

                testResultsMap.put(testContext.getName(), testResults);
            }
        }
        return testResultsMap;
    }

    private String generateTestRecords(Map<String, ArrayList<TestResult>> testResultsMap){

        String records = "";

        for (Map.Entry<String, ArrayList<TestResult>> entry : testResultsMap.entrySet()){
            String testSuiteName = entry.getKey();
            ArrayList<TestResult>  testSuiteResults = entry.getValue();

            //Генерация заголовка
            records += "<tr class=\"test-set-name\"><td colspan=\"4\">"+ testSuiteName +"</td></tr> \r\n";

            for(TestResult testResult: testSuiteResults){
                String className = "";
                String testName = testResult.testName;
                String testMessage = "";
                String testStatus = "";
                String testDuration = testResult.duration;
                String styleValue = "";

                //Определяем класс разметки и статус в зависимости от результата теста
                if (testResult.status == TestResult.SKIPPED){
                    className = "test-skipped";
                    testStatus = "SKIPPED";
                    styleValue = "style=\"background-color:#eee\"";
                }
                if (testResult.status == TestResult.FAILED){
                    className = "test-failed";
                    testStatus = "FAILED";
                    styleValue = "style=\"background-color:#F1DDDE\"";

                    if (testResult.stepName != null){
                        testMessage += testResult.stepName + "<br>";
                    }
                    testMessage += testResult.log;

                }
                if (testResult.status == TestResult.PASSED){
                    className = "test-passed";
                    testStatus = "PASSED";
                    styleValue = "style=\"background-color:#DDEFD8\"";
                }


                String recordTemplate = "<tr class=\"test testStatusClass-value\">\r\n" +
                        "<td class=\"test-name\" style-value>testName-value</td>\r\n" +
                        "<td class=\"test-message\" style-value>testMessage-value</td>\r\n" +
                        "<td class=\"test-status\" style-value>testStatus-value</td>\r\n" +
                        "<td class=\"test-duration\" style-value>test-duration-value</td>\r\n" +
                        "</tr>\r\n";

                String result = MessageFormat.format(
                "<tr class=\"test {0}\">\r\n" +
                        "<td class=\"test-name\" {1}>{2}</td>\r\n" +
                        "<td class=\"test-message\" {1}>{3}</td>\r\n" +
                        "<td class=\"test-status\" {1}>{4}</td>\r\n" +
                        "<td class=\"test-duration\" {1}>{5}</td>\r\n" +
                        "</tr>\r\n",
                        className,
                        styleValue,
                        testName,
                        testMessage,
                        testStatus,
                        testDuration
                );

/*                recordTemplate = recordTemplate.replaceAll("testStatusClass-value",className);
                recordTemplate = recordTemplate.replaceAll("style-value",styleValue);
                recordTemplate = recordTemplate.replaceAll("testName-value",testName);
                recordTemplate = recordTemplate.replaceAll("testMessage-value",testMessage);
                recordTemplate = recordTemplate.replaceAll("testStatus-value",testStatus);
                recordTemplate = recordTemplate.replaceAll("test-duration-value",testDuration);*/

                records += result;
            }
        }
        return records;
    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuiteList, List<ISuite> suiteList, String outputDirectory) {
        Map<String, ArrayList<TestResult>> testResultsMap = parseTestResults(suiteList);

        String reportOutputDirectory = "C:\\Testing\\Testresults\\";
        String outputReport = reportOutputDirectory + suiteList.get(0).getName() + ".html";

        String reportStyles = getWindows1251StringFromFile("C:\\Testing\\Testresults\\reportTemplates\\seleniumTests\\reportStyles.html");
        String records = generateTestRecords(testResultsMap);

        String report = getWindows1251StringFromFile("C:\\Testing\\Testresults\\reportTemplates\\seleniumTests\\reportTemplate.html");
        report = report.replace("testSuite-value", suiteList.get(0).getName());
        report = report.replace("testRecords-value", records);

        report = reportStyles + "\r\n" + report;
        writeString1251ToFile(report, outputReport);

    }
}
