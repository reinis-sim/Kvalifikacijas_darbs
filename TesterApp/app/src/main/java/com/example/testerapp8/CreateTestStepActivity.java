package com.example.testerapp8;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.ArrayList;

public class CreateTestStepActivity extends AppCompatActivity {

    EditText senderID, msgText, testData, expectedResult, stepDesc;
    Switch isSMS;
    Button addStepBtn1;

    private boolean isValidInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test_step);

        stepDesc = findViewById(R.id.step_title_text);
        testData = findViewById(R.id.test_data_text);
        expectedResult = findViewById(R.id.expected_result_text);
        isSMS = findViewById(R.id.sms_switch);
        senderID = findViewById(R.id.sms_sender_text);
        msgText = findViewById(R.id.sms_msg_text);
        addStepBtn1 = findViewById(R.id.add_step_btn);

        isSMS.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                msgText.setVisibility(View.VISIBLE);
                senderID.setVisibility(View.VISIBLE);
            } else {
                msgText.setVisibility(View.GONE);
                senderID.setVisibility(View.GONE);
            }
        });

        addStepBtn1.setOnClickListener((v) -> {
            isValidInput = true;
            TestStep testStep = createFromInput();
            if (isValidInput) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("new_test_step", (Parcelable) testStep);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }

    public TestStep createFromInput() {
        String stepDescStr = stepDesc.getText().toString();
        String testDataStr = testData.getText().toString();
        String expectedResultStr = expectedResult.getText().toString();
        String senderIDStr = senderID.getText().toString();
        String msgTextStr = msgText.getText().toString();

        if (stepDescStr== null || stepDescStr.isEmpty()){
            stepDesc.setError("Title is required");
            isValidInput = false;
            return null;
        }

        TestStep testStep = new TestStep();
        testStep.setStepDesc(stepDescStr);
        testStep.setTestData(testDataStr);
        testStep.setExpectedResult(expectedResultStr);
        testStep.setSMSSender(senderIDStr);
        testStep.setSMSText(msgTextStr);
        if (!senderIDStr.trim().isEmpty() && !msgTextStr.trim().isEmpty())
        {
            testStep.setContainsSMS(true);
        }
        else
            testStep.setContainsSMS(false);

        return testStep;
    }
}
