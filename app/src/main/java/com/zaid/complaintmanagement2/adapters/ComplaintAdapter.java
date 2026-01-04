package com.zaid.complaintmanagement2.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.zaid.complaintmanagement2.R;
import com.zaid.complaintmanagement2.models.Complaint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComplaintAdapter extends ArrayAdapter<Complaint> {

    private Context context;

    public ComplaintAdapter(Context context, List<Complaint> complaintList) {
        super(context, 0, complaintList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Inflate Layout if it hasn't been created yet
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_complaint, parent, false);
        }

        // 2. Get the current complaint data
        Complaint complaint = getItem(position);

        // 3. Find Views inside the card
        TextView tvId = convertView.findViewById(R.id.tvComplaintId);
        TextView tvTitle = convertView.findViewById(R.id.tvComplaintTitle);
        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);
        TextView tvPriority = convertView.findViewById(R.id.tvPriority);
        TextView tvDate = convertView.findViewById(R.id.tvDate);

        // 4. Set Data
        if (complaint != null) {
            tvId.setText("#" + complaint.getId());
            tvTitle.setText(complaint.getTitle());
            tvCategory.setText("Category: " + complaint.getCategory());
            tvStatus.setText(complaint.getStatus());
            tvPriority.setText(complaint.getPriority());

            // Date Formatting
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                String dateString = sdf.format(new Date(complaint.getTimestamp()));
                tvDate.setText(dateString);
            } catch (Exception e) {
                tvDate.setText("");
            }

            // 5. Apply Dynamic Colors
            tvStatus.setTextColor(getStatusColor(complaint.getStatus()));
            tvPriority.setTextColor(getPriorityColor(complaint.getPriority()));

            // NOTE: No setOnClickListener here!
            // Clicks are now handled by the Activity's ListView (setOnItemClickListener)
        }

        return convertView;
    }

    // Helper method for Status Colors
    private int getStatusColor(String status) {
        if ("Pending".equalsIgnoreCase(status)) {
            return ContextCompat.getColor(context, R.color.status_pending);
        } else if ("Resolved".equalsIgnoreCase(status)) {
            return ContextCompat.getColor(context, R.color.status_resolved);
        } else {
            // Default or In Progress
            return ContextCompat.getColor(context, R.color.status_in_progress);
        }
    }

    // Helper method for Priority Colors
    private int getPriorityColor(String priority) {
        if ("High".equalsIgnoreCase(priority)) {
            return ContextCompat.getColor(context, R.color.priority_high);
        } else if ("Medium".equalsIgnoreCase(priority)) {
            return ContextCompat.getColor(context, R.color.priority_medium);
        } else {
            // Low
            return ContextCompat.getColor(context, R.color.priority_low);
        }
    }
}