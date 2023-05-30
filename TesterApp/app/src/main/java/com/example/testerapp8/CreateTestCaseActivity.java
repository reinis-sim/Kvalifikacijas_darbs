package com.example.testerapp8;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class CreateTestCaseActivity extends AppCompatActivity {

    FloatingActionButton addStepBtn;
    EditText caseTitle, casePrecon, caseVersion;
    Button saveCaseBtn;
    String docId;
    RecyclerView recyclerViewSteps;

    TestStepAdapter adapter = new TestStepAdapter();
    private ArrayList<TestStep> testSteps = new ArrayList<>();
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test_case);

        addStepBtn = findViewById(R.id.add_step_btn);
        saveCaseBtn = findViewById(R.id.add_test_case_btn);
        caseTitle = findViewById(R.id.page_title_text);
        casePrecon = findViewById(R.id.page_precon_text);
        caseVersion = findViewById(R.id.page_version_text);
        recyclerViewSteps = findViewById(R.id.recycler_view_steps);

        docId = getIntent().getStringExtra("docId1");

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                TestStep receivedTestStep = (TestStep) result.getData().getSerializableExtra("new_test_step");
                if (receivedTestStep != null) {
                    addTestStep(receivedTestStep);
                }
            }
        });

        addStepBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(CreateTestCaseActivity.this, CreateTestStepActivity.class);
            activityResultLauncher.launch(intent);
        });

        saveCaseBtn.setOnClickListener((v)-> saveCase());

        recyclerViewSteps = findViewById(R.id.recycler_view_steps);
        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSteps.setAdapter(adapter);
    }

    public void addTestStep(TestStep testStep) {
        testSteps.add(testStep);
        adapter.addItem(testStep);
    }

    void saveCase(){
        String caseTitleStr = caseTitle.getText().toString();
        String casePreconStr = casePrecon.getText().toString();
        String caseVersionStr = caseVersion.getText().toString();

        if (caseTitleStr== null || caseTitleStr.isEmpty()){
            caseTitle.setError("Title is required");
            return;
        }
        else if (casePreconStr== null || casePreconStr.isEmpty()){
            casePrecon.setError("Precondition is required");
            return;
        }
        else if (caseVersionStr== null || caseVersionStr.isEmpty()){
            caseVersion.setError("App version is required");
            return;
        }

        TestCase testCase = new TestCase();
        testCase.setCaseTitle(caseTitleStr);
        testCase.setPrecondition(casePreconStr);
        testCase.setVersion(caseVersionStr);
        testCase.setTestSteps(testSteps);
        testCase.setTestStatus(TestStatus.NOT_EXECUTED);

        saveCaseToFirebase(testCase);
    }

    void saveCaseToFirebase(TestCase testCase){
        CollectionReference collectionReference;
        collectionReference = Utility.getCollectionReferenceForTestCases().document(docId).collection("test_cases");

        collectionReference.add(testCase).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Utility.showToast(CreateTestCaseActivity.this, "Test case added!");
                    finish();
                }else{
                    Utility.showToast(CreateTestCaseActivity.this, "Failed to add test case");
                }
            }
        });
    }

}