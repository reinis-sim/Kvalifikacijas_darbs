package com.example.testerapp8;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TestStep implements Parcelable, Serializable {

    String stepDesc;
    String testData;
    String expectedResult;
    boolean containsSMS = false;
    String SMSSender;
    String SMSText;

    public TestStep(){

    }

    public TestStep(String stepDesc, String testData, String expectedResult, boolean containsSMS, String SMSSender, String SMSText) {
        this.stepDesc = stepDesc;
        this.testData = testData;
        this.expectedResult = expectedResult;
        this.containsSMS = containsSMS;
        this.SMSSender = SMSSender;
        this.SMSText = SMSText;
    }

    public TestStep(String stepDesc, String testData, String expectedResult) {
        this.stepDesc = stepDesc;
        this.testData = testData;
        this.expectedResult = expectedResult;
    }

    protected TestStep(Parcel in) {
        stepDesc = in.readString();
        testData = in.readString();
        expectedResult = in.readString();
        containsSMS = in.readByte() != 0;
        SMSSender = in.readString();
        SMSText = in.readString();
    }

    public static final Creator<TestStep> CREATOR = new Creator<TestStep>() {
        @Override
        public TestStep createFromParcel(Parcel in) {
            return new TestStep(in);
        }

        @Override
        public TestStep[] newArray(int size) {
            return new TestStep[size];
        }
    };

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }

    public String getTestData() {
        return testData;
    }

    public void setTestData(String testData) {
        this.testData = testData;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public boolean isContainsSMS() {
        return containsSMS;
    }

    public void setContainsSMS(boolean containsSMS) {
        this.containsSMS = containsSMS;
    }

    public String getSMSSender() {
        return SMSSender;
    }

    public void setSMSSender(String SMSSender) {
        this.SMSSender = SMSSender;
    }

    public String getSMSText() {
        return SMSText;
    }

    public void setSMSText(String SMSText) {
        this.SMSText = SMSText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(stepDesc);
        parcel.writeString(testData);
        parcel.writeString(expectedResult);
        parcel.writeByte((byte) (containsSMS ? 1 : 0));
        parcel.writeString(SMSSender);
        parcel.writeString(SMSText);
    }
}
