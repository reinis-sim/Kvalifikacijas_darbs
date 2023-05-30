package com.example.testerapp8;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ExistingTestCaseDetailsActivity extends AppCompatActivity {

    TextView caseTitle, casePrecon, caseVersion;
    String caseTitleStr, casePreconStr, caseVersionStr;
    private ArrayList<TestStep> testSteps;
    Button deleteCaseBtn;
    String docId;
    String projectId;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_case_details);
        testSteps = (ArrayList<TestStep>) getIntent().getSerializableExtra("steps");

        caseTitle = findViewById(R.id.page_title_text);
        casePrecon = findViewById(R.id.page_precon_text);
        caseVersion = findViewById(R.id.page_version_text);
        recyclerView = findViewById(R.id.recycler_view_steps);
        deleteCaseBtn = findViewById(R.id.dlt_case_btn);

        caseTitleStr = getIntent().getStringExtra("caseTitle");
        casePreconStr = getIntent().getStringExtra("precondition");
        caseVersionStr = getIntent().getStringExtra("version");
        docId = getIntent().getStringExtra("docId");
        projectId = getIntent().getStringExtra("projectId");

        caseTitle.setText(caseTitleStr);
        casePrecon.setText(casePreconStr);
        caseVersion.setText(caseVersionStr);

        TestStepDetailsAdapter testStepAdapter = new TestStepDetailsAdapter(testSteps, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(testStepAdapter);
        deleteCaseBtn.setOnClickListener((v)-> deleteTestCase());

    }

    void deleteTestCase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("projects")
                .document(userId)
                .collection("my_projects")
                .document(projectId)
                .collection("test_cases")
                .document(docId);

        new AlertDialog.Builder(this)
                .setTitle("Delete Test Case")
                .setMessage("Are you sure you want to delete this test case?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        documentReference.delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("ExistingTestCaseDetails", "Test case deleted successfully");
                                    finish();
                                })
                                .addOnFailureListener(e -> Log.e("ExistingTestCaseDetails", "Failed to delete test case", e));
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void sendSMS(String smsText) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        } else {
            String phoneNumber = getDevicePhoneNumber();
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, smsText, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Phone number not available", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDevicePhoneNumber() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
            return null;
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                return telephonyManager.getLine1Number();
            }
        }
        return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "SMS permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "SMS permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }





}
