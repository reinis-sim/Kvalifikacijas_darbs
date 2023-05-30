package com.example.testerapp8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestStepAdapter extends RecyclerView.Adapter<TestStepAdapter.ViewHolder> {
    private List<TestStep> items = new ArrayList<>();

    public void addItem(TestStep item) {
        items.add(item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TestStep item = items.get(position);
        holder.stepTitle.setText(item.getStepDesc());
        holder.stepExpected.setText("Expected result: "+ item.getExpectedResult());
        holder.senderID.setText("Sender ID: "+ item.SMSSender);
        holder.senderMsg.setText("Message: " + item.SMSText);
        holder.testData.setText("Test data: " + item.testData);
        holder.stepNumber.setText(String.valueOf(position + 1));

        if (!(item.getTestData().trim().isEmpty()))
        {
            holder.testData.setVisibility(View.VISIBLE);
        }else
        {
            holder.testData.setVisibility(View.GONE);
        }

        if (!(item.getSMSText().trim().isEmpty()) && !(item.getSMSSender().trim().isEmpty())) {
            holder.senderMsg.setVisibility(View.VISIBLE);
            holder.senderID.setVisibility(View.VISIBLE);
        } else {
            holder.senderMsg.setVisibility(View.GONE);
            holder.senderID.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView stepTitle,testData,stepExpected,senderID,senderMsg,stepNumber;

        public ViewHolder(View view) {
            super(view);
            stepTitle = view.findViewById(R.id.step_title_text_view);
            stepExpected = view.findViewById(R.id.step_content_text_view);
            senderID = view.findViewById(R.id.step_sender_text_view);
            senderMsg = view.findViewById(R.id.step_sms_text_view);
            testData = view.findViewById(R.id.step_data_text_view);
            stepNumber = view.findViewById(R.id.step_number_text_view);
        }
    }
}
