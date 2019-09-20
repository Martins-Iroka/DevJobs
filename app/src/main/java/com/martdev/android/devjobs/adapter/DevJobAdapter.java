package com.martdev.android.devjobs.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martdev.android.devjobs.DevJob;
import com.martdev.android.devjobs.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DevJobAdapter extends RecyclerView.Adapter<DevJobAdapter.DevJobHolder> {
    private final Context mContext;

    private final OnItemClickHandler mClickHandler;
    private List<DevJob> mDevJobList;

    public interface OnItemClickHandler {
        void clickItem(DevJob devJob);
    }

    class DevJobHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mCompanyLogo;
        private TextView mJobTitle;
        private TextView mCompany;
        private TextView mType;
        private TextView mLocation;

        DevJobHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.devjob_item_view, parent, false));
            itemView.setOnClickListener(this);

            mCompanyLogo = itemView.findViewById(R.id.company_logo);
            mJobTitle = itemView.findViewById(R.id.job_title);
            mCompany = itemView.findViewById(R.id.company_name);
            mType = itemView.findViewById(R.id.full_part_time);
            mLocation = itemView.findViewById(R.id.job_location);
        }

        private void bind(DevJob devJob) {
            Uri uri = Uri.parse(devJob.getLogo());
            Picasso.with(mContext).load(uri).placeholder(R.drawable.images).resize(200, 100)
                    .centerCrop().into(mCompanyLogo);

            mJobTitle.setText(devJob.getTitle());
            mCompany.setText(devJob.getCompany());
            mType.setText(devJob.getType());
            mLocation.setText(devJob.getLocation());
        }

        @Override
        public void onClick(View v) {
            int id = getAdapterPosition();
            DevJob devJob = mDevJobList.get(id);
            mClickHandler.clickItem(devJob);
        }
    }

    public DevJobAdapter(Context context, OnItemClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public DevJobHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new DevJobHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull DevJobHolder holder, int position) {
        DevJob devJob = mDevJobList.get(position);
        holder.bind(devJob);
    }

    public void setDevJobList(List<DevJob> devJobList) {
        mDevJobList = devJobList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDevJobList != null) {
            return mDevJobList.size();
        }
        return 0;
    }
}
