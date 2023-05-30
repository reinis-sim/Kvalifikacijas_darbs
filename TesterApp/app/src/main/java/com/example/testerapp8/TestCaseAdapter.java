package com.example.testerapp8;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class TestCaseAdapter extends FirestoreRecyclerAdapter<TestCase, TestCaseAdapter.TestCaseViewHolder> {
    Context context;
    String projectId;

    public TestCaseAdapter(@NonNull FirestoreRecyclerOptions<TestCase> options, Context context, String projectId) {
        super(options);
        this.context = context;
        this.projectId = projectId;
    }
    @Override
    protected void onBindViewHolder(@NonNull TestCaseAdapter.TestCaseViewHolder holder, int position, @NonNull TestCase testCase) {
        holder.titleTextView.setText(testCase.caseTitle);

        holder.statusSpinner.setSelection(testCase.testStatus.ordinal());
        holder.currentTestCase = testCase;

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, ExistingTestCaseDetailsActivity.class);
            intent.putExtra("caseTitle", testCase.caseTitle);
            intent.putExtra("precondition", testCase.precondition);
            intent.putExtra("version", testCase.version);
            intent.putExtra("steps", (Serializable) testCase.testSteps);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            intent.putExtra("projectId", projectId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public TestCaseAdapter.TestCaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_case_item,parent,false);
        return new TestCaseAdapter.TestCaseViewHolder(view);
    }

    class TestCaseViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView, contentTextView;
        Spinner statusSpinner;
        TestCase currentTestCase;

        public TestCaseViewHolder(@NonNull View itemView) {

            super(itemView);
            titleTextView = itemView.findViewById(R.id.project_title_text_view);
            contentTextView = itemView.findViewById(R.id.project_content_text_view);
            statusSpinner = itemView.findViewById(R.id.project_status_spinner);

            ArrayAdapter<TestStatus> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_item, TestStatus.values());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            statusSpinner.setAdapter(adapter);
            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TestStatus selectedStatus = (TestStatus) parent.getItemAtPosition(position);
                    if (currentTestCase.getTestStatus() != selectedStatus) {
                        currentTestCase.setTestStatus(selectedStatus);
                        String testCaseId = getSnapshots().getSnapshot(getAdapterPosition()).getId();
                        updateTestCaseStatusInFirebase(testCaseId, selectedStatus);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void updateTestCaseStatusInFirebase(String testCaseId, TestStatus newStatus) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("projects")
                .document(userId)
                .collection("my_projects")
                .document(projectId)
                .collection("test_cases")
                .document(testCaseId);

        documentReference.update("testStatus", newStatus)
                .addOnSuccessListener(aVoid -> Log.d("TestCaseAdapter", "Status updated successfully"))
                .addOnFailureListener(e -> Log.e("TestCaseAdapter", "Failed to update status", e));
    }


}
