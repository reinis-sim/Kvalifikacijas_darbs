package com.example.testerapp8;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProjectAdapter extends FirestoreRecyclerAdapter<Project, ProjectAdapter.ProjectViewHolder> {
    Context context;

    public ProjectAdapter(@NonNull FirestoreRecyclerOptions<Project> options, Context context) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull ProjectViewHolder holder, int position, @NonNull Project project) {
        holder.titleTextView.setText(project.title);
        holder.contentTextView.setText(project.content);
        holder.timestampTextView.setText("Created: "+Utility.timestampToString(project.timestamp));

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, ExistingProjectDetailsActivity.class);
            intent.putExtra("title", project.title);
            intent.putExtra("content", project.content);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_project_item,parent,false);
        return new ProjectViewHolder(view);
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView, contentTextView, timestampTextView;
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.project_title_text_view);
            contentTextView = itemView.findViewById(R.id.project_content_text_view);
            timestampTextView = itemView.findViewById(R.id.project_timestamp_text_view);
        }
    }
}
