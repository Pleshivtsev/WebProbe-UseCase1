package model;

public class TestResult {

    public static final int PASSED = 1;
    public static final int FAILED = 2;
    public static final int SKIPPED = 3;

    public int status = 0;

    public String testName = "";
    public String methodName = "";
    public String stepName;
    public boolean passed = false;
    public String log = "";
    public String stackTrace;
    public String duration;
}