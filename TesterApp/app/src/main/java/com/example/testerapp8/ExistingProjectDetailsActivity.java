package com.example.testerapp8;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExistingProjectDetailsActivity extends AppCompatActivity {

    TextView projectTitleTextView, projectDescTextView;
    String title, content, docId;
    ImageButton addTestCaseBtn, projectMenu;
    TestCaseAdapter testCaseAdapter;
    RecyclerView recyclerView;
    List<TestCase> testCases = new ArrayList<>();

    private static final int PERMISSION_REQUEST_CODE = 200;

    int pageHeight = 1120;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROJECT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            title = data.getStringExtra("newTitle");
            content = data.getStringExtra("newDesc");

            projectTitleTextView.setText(title);
            projectDescTextView.setText(content);

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference documentReference = db.collection("projects")
                    .document(userId)
                    .collection("my_projects")
                    .document(docId);

            Map<String, Object> updates = new HashMap<>();
            updates.put("title", title);
            updates.put("description", content);

            documentReference.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ExistingProjectDetailsActivity.this, "Project updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ExistingProjectDetailsActivity.this, "Failed to update project", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        projectDescTextView = findViewById(R.id.project_desc_text);
        projectTitleTextView = findViewById(R.id.project_title_text);
        addTestCaseBtn = findViewById(R.id.add_test_case_btn);
        recyclerView = findViewById(R.id.recycler_view_cases);
        projectMenu = findViewById(R.id.proj_menu_btn);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");


        projectTitleTextView.setText(title);
        projectDescTextView.setText(content);

        if (checkPermission()) {
        } else {
            requestPermission();
        }

        addTestCaseBtn.setOnClickListener((v) ->{
            Intent intent = new Intent(ExistingProjectDetailsActivity.this, CreateTestCaseActivity.class);
            intent.putExtra("docId1", docId);
            (ExistingProjectDetailsActivity.this).startActivity(intent);


        });
        setupRecyclerViewTestCase();
        projectMenu.setOnClickListener((v)-> showMenu());


        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.login);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

    }

    void setupRecyclerViewTestCase(){
        Query query = Utility.getCollectionReferenceForTestCases().document(docId).collection("test_cases").orderBy("caseTitle", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<TestCase> options = new FirestoreRecyclerOptions.Builder<TestCase>()
                .setQuery(query, TestCase.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        testCaseAdapter = new TestCaseAdapter(options,this,docId);
        recyclerView.setAdapter(testCaseAdapter);

        testCaseAdapter = new TestCaseAdapter(options, this, docId) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                testCases.clear();
                testCases.addAll(getSnapshots());
            }
        };
        recyclerView.setAdapter(testCaseAdapter);
    }

    void showMenu(){
        PopupMenu popupMenu = new PopupMenu(ExistingProjectDetailsActivity.this, projectMenu);
        popupMenu.getMenu().add("Print PDF");
        popupMenu.getMenu().add("Mark all as Not Executed");
        popupMenu.getMenu().add("Edit project");
        popupMenu.getMenu().add("Delete project");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle() == "Print PDF"){
                    generatePDF();
                }
                else if (menuItem.getTitle() == "Mark all as Not Executed"){
                    Utility.showToast(ExistingProjectDetailsActivity.this, "Status changed");
                    resetStatus();
                }
                else if (menuItem.getTitle() == "Delete project"){
                    new AlertDialog.Builder(ExistingProjectDetailsActivity.this)
                            .setTitle("Confirm Delete")
                            .setMessage("Are you sure you want to delete this project?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Utility.showToast(ExistingProjectDetailsActivity.this, "Project deleted!");
                                    deleteProject();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else if (menuItem.getTitle() == "Edit project"){
                    Utility.showToast(ExistingProjectDetailsActivity.this, "*edit mode*");
                    editProject();



                }
                return false;
            }

        });
    }

    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();

        int pageHeight = 1120;
        int pagewidth = 792;

        int testCaseStartX = 56;
        int testCaseStartY = 100;
        int pageNumber = 1;

        List<TestCase> sortedTestCases = sortTestCases(testCases);

        PdfDocument.Page myPage = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, pageNumber).create());
        Canvas canvas = myPage.getCanvas();

        Paint testCasePaint = new Paint();
        testCasePaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        testCasePaint.setColor(ContextCompat.getColor(this, R.color.black));
        testCasePaint.setTextSize(15);

        Paint sectionTitlePaint = new Paint();
        sectionTitlePaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        sectionTitlePaint.setColor(ContextCompat.getColor(this, R.color.black));
        sectionTitlePaint.setTextSize(20);

        Paint titlePaint = new Paint();
        titlePaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        titlePaint.setColor(ContextCompat.getColor(this, R.color.black));
        titlePaint.setTextSize(25);
        titlePaint.setTextAlign(Paint.Align.CENTER);

        float titleStartY = 30;
        float centerX = pagewidth / 2f;
        canvas.drawText("Test Report", centerX, titleStartY, titlePaint);


        boolean passedHeaderAdded = false;
        boolean failedHeaderAdded = false;
        boolean notExecutedHeaderAdded = false;

        for (int i = 0; i < sortedTestCases.size(); i++) {
            TestCase testCase = sortedTestCases.get(i);
            String status = String.valueOf(testCase.getTestStatus());

            if (!passedHeaderAdded && status.equals("PASSED")) {
                sectionTitlePaint.setColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
                canvas.drawText("Cases that passed:", testCaseStartX, testCaseStartY, sectionTitlePaint);
                testCaseStartY += 30;
                passedHeaderAdded = true;
            } else if (!failedHeaderAdded && status.equals("FAILED")) {
                sectionTitlePaint.setColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                canvas.drawText("Cases that failed:", testCaseStartX, testCaseStartY, sectionTitlePaint);
                testCaseStartY += 30;
                failedHeaderAdded = true;
            } else if (!notExecutedHeaderAdded && status.equals("NOT_EXECUTED")) {
                sectionTitlePaint.setColor(ContextCompat.getColor(this, R.color.black));
                canvas.drawText("Cases not executed:", testCaseStartX, testCaseStartY, sectionTitlePaint);
                testCaseStartY += 30;
                notExecutedHeaderAdded = true;
            }

            if (testCaseStartY > (pageHeight - 50)) {
                pdfDocument.finishPage(myPage);
                pageNumber++;
                myPage = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, pageNumber).create());
                canvas = myPage.getCanvas();
                testCaseStartY = 50;
            }

            canvas.drawText("Title: " + testCase.getCaseTitle(), testCaseStartX, testCaseStartY, testCasePaint);
            testCaseStartY += 20;

            canvas.drawText("Status: " + testCase.getTestStatus(), testCaseStartX, testCaseStartY, testCasePaint);
            testCaseStartY += 30;
        }

        int passedCount = 0;
        int failedCount = 0;
        int notExecutedCount = 0;

        for (TestCase testCase : sortedTestCases) {
            String status = String.valueOf(testCase.getTestStatus());
            switch (status) {
                case "PASSED":
                    passedCount++;
                    break;
                case "FAILED":
                    failedCount++;
                    break;
                case "NOT_EXECUTED":
                    notExecutedCount++;
                    break;
            }
        }

        if (testCaseStartY > (pageHeight - 100)) {
            pdfDocument.finishPage(myPage);
            pageNumber++;
            myPage = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, pageNumber).create());
            canvas = myPage.getCanvas();
            testCaseStartY = 50;
        }

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        Paint summaryPaint = new Paint();
        summaryPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        summaryPaint.setColor(ContextCompat.getColor(this, R.color.black));
        summaryPaint.setTextSize(20);

        canvas.drawText("Summary:", testCaseStartX, testCaseStartY, summaryPaint);
        testCaseStartY += 30;

        canvas.drawText("Passed: " + passedCount, testCaseStartX, testCaseStartY, testCasePaint);
        testCaseStartY += 20;

        canvas.drawText("Failed: " + failedCount, testCaseStartX, testCaseStartY, testCasePaint);
        testCaseStartY += 20;

        canvas.drawText("Not Executed: " + notExecutedCount, testCaseStartX, testCaseStartY, testCasePaint);
        testCaseStartY += 20;

        Paint datePaint = new Paint();
        datePaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        datePaint.setColor(ContextCompat.getColor(this, R.color.black));
        datePaint.setTextSize(12);
        datePaint.setTextAlign(Paint.Align.RIGHT);

        float dateStartY = pageHeight - 10;
        float dateStartX = pagewidth - 10;
        String dateStr = dateFormat.format(new Date());
        canvas.drawText(dateStr, dateStartX, dateStartY, datePaint);

        pdfDocument.finishPage(myPage);

        File outputDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(outputDir, "TestReport_"+formattedDate+".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(ExistingProjectDetailsActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
    }

    private List<TestCase> sortTestCases(List<TestCase> testCases) {

        List<TestCase> passedCases = new ArrayList<>();
        List<TestCase> failedCases = new ArrayList<>();
        List<TestCase> notExecutedCases = new ArrayList<>();

        for (TestCase testCase : testCases) {
            String status = String.valueOf(testCase.getTestStatus());
            switch (status) {
                case "PASSED":
                    passedCases.add(testCase);
                    break;
                case "FAILED":
                    failedCases.add(testCase);
                    break;
                case "NOT_EXECUTED":
                    notExecutedCases.add(testCase);
                    break;
            }
        }

        List<TestCase> sortedTestCases = new ArrayList<>();
        sortedTestCases.addAll(passedCases);
        sortedTestCases.addAll(failedCases);
        sortedTestCases.addAll(notExecutedCases);

        return sortedTestCases;
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private void resetStatus() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        for (int i = 0; i < testCases.size(); i++) {
            TestCase testCase = testCases.get(i);
            testCase.setTestStatus(TestStatus.NOT_EXECUTED);

            String testCaseId = testCaseAdapter.getSnapshots().getSnapshot(i).getId();

            DocumentReference documentReference = db.collection("projects")
                    .document(userId)
                    .collection("my_projects")
                    .document(docId)
                    .collection("test_cases")
                    .document(testCaseId);

            documentReference.update("testStatus", TestStatus.NOT_EXECUTED)
                    .addOnSuccessListener(aVoid -> Log.d("EPDActivity", "Status reset successfully"))
                    .addOnFailureListener(e -> Log.e("EPDActivity", "Failed to reset status", e));
        }
    }

    void deleteProject() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("projects")
                .document(userId)
                .collection("my_projects")
                .document(docId);

        documentReference.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("ExistingTestCaseDetails", "Test case deleted successfully");
                    finish();
                })
                .addOnFailureListener(e -> Log.e("ExistingTestCaseDetails", "Failed to delete test case", e));
    }

    private static final int EDIT_PROJECT_REQUEST_CODE = 300;

    void editProject() {
        Intent intent = new Intent(ExistingProjectDetailsActivity.this, EditProjectActivity.class);
        intent.putExtra("currentTitle", title);
        intent.putExtra("currentDesc", content);
        startActivityForResult(intent, EDIT_PROJECT_REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        testCaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        testCaseAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        testCaseAdapter.notifyDataSetChanged();
    }
}

