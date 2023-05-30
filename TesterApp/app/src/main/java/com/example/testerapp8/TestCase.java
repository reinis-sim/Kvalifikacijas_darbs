package com.example.testerapp8;

import java.util.ArrayList;
import java.util.List;

public class TestCase {

    TestStatus testStatus;
    String caseTitle;
    String version;
    String precondition;
    List<TestStep> testSteps = new ArrayList<TestStep>();

    public TestCase() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public List<TestStep> getTestSteps() {
        return testSteps;
    }

    public void setTestSteps(List<TestStep> testSteps) {
        this.testSteps = testSteps;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public TestStatus getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(TestStatus testStatus) {
        this.testStatus = testStatus;
    }
}
