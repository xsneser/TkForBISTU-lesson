package com.example.tk.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tk.R;
import com.example.tk.activity.Plan;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private Context context;
    private List<Plan> planList;
    private OnDeleteClickListener listener;

    public PlanAdapter(Context context, List<Plan> planList) {
        this.context = context;
        this.planList = planList;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Plan plan = planList.get(position);
        holder.tvTime.setText(plan.getTime());
        holder.tvContent.setText(plan.getContent());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteClick(position, plan.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvContent;
        Button btnDelete;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_plan_time);
            tvContent = itemView.findViewById(R.id.tv_plan_content);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position, int planId);
    }
}