package com.example.testerapp8;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TestStepDetailsAdapter extends RecyclerView.Adapter<TestStepDetailsAdapter.TestStepViewHolder> {

    private ArrayList<TestStep> testSteps;
    private ExistingTestCaseDetailsActivity activity;

    public TestStepDetailsAdapter(ArrayList<TestStep> testSteps, ExistingTestCaseDetailsActivity activity) {
        this.testSteps = testSteps;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TestStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_step_in_details_item, parent, false);
        return new TestStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestStepViewHolder holder, int position) {
        TestStep item = testSteps.get(position);
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

        if (item.isContainsSMS()) {
            holder.smsBtn.setVisibility(View.VISIBLE);
        } else {
            holder.smsBtn.setVisibility(View.GONE);
        }

        holder.smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsText = item.getSMSText();
                activity.sendSMS(smsText);
            }
        });
    }
    @Override
    public int getItemCount() {
        return testSteps.size();
    }

    public static class TestStepViewHolder extends RecyclerView.ViewHolder {

        public TextView stepTitle;
        public TextView testData;
        public TextView stepExpected;
        public TextView senderID;
        public TextView senderMsg;
        public TextView stepNumber;
        Button smsBtn;

        public TestStepViewHolder(@NonNull View view) {
            super(view);
            stepTitle = view.findViewById(R.id.step_title_text_view);
            stepExpected = view.findViewById(R.id.step_content_text_view);
            senderID = view.findViewById(R.id.step_sender_text_view);
            senderMsg = view.findViewById(R.id.step_sms_text_view);
            testData = view.findViewById(R.id.step_data_text_view);
            smsBtn = view.findViewById(R.id.sms_btn);
            stepNumber = view.findViewById(R.id.step_number_text_view);
        }
    }



}

