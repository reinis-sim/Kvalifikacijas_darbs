package com.example.testerapp8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;


public class ProjectDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveProjectBtn;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEditMode = false;
    TextView deleteProjectTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        titleEditText = findViewById(R.id.project_title_text);
        contentEditText = findViewById(R.id.project_desc_text);
        saveProjectBtn = findViewById(R.id.save_project_btn);
        pageTitleTextView = findViewById(R.id.project_title_text);
        deleteProjectTextViewBtn = findViewById(R.id.delete_project_text_view_btn);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId!=null && !docId.isEmpty()){
            isEditMode = true;
            setContentView(R.layout.activity_project_details);
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if (isEditMode){
            pageTitleTextView.setText(title);
            deleteProjectTextViewBtn.setVisibility(View.VISIBLE);
        }
        saveProjectBtn.setOnClickListener((v)-> saveProject());
        deleteProjectTextViewBtn.setOnClickListener((v)-> deleteProjectFromFirebase());
    }

    void saveProject(){
        String projectTitle = pageTitleTextView.getText().toString();
        String projectContent = contentEditText.getText().toString();
        if (projectTitle== null || projectTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }
        Project project = new Project();
        project.setTitle(projectTitle);
        project.setContent(projectContent);
        project.setTimestamp(Timestamp.now());
        saveProjectToFirebase(project);
    }

    void saveProjectToFirebase(Project project){
        DocumentReference documentReference;
        if (isEditMode){
            documentReference = Utility.getCollectionReferenceForProjects().document(docId);
        }else {
            documentReference = Utility.getCollectionReferenceForProjects().document();
        }
        documentReference.set(project).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.showToast(ProjectDetailsActivity.this, "Project added!");
                    finish();
                }else{
                    Utility.showToast(ProjectDetailsActivity.this, "Failed to add projectss");
                }
            }
        });
    }
    void deleteProjectFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForProjects().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.showToast(ProjectDetailsActivity.this, "Project deleted!");
                    finish();
                }else{
                    Utility.showToast(ProjectDetailsActivity.this, "Failed to deleting projects");
                }
            }
        });
    }
}