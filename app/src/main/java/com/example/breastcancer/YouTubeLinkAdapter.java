package com.example.breastcancer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class YouTubeLinkAdapter extends RecyclerView.Adapter<YouTubeLinkAdapter.ViewHolder> {

    private Context context;
    private List<YouTubeLink> youTubeLinks;
    private OnItemClickListener onItemClickListener;

    public YouTubeLinkAdapter(Context context, List<YouTubeLink> youTubeLinks) {
        this.context = context;
        this.youTubeLinks = youTubeLinks;
    }

    public interface OnItemClickListener {
        void onItemClick(YouTubeLink link);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.youtube_link, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YouTubeLink link = youTubeLinks.get(position);
        holder.titleTextView.setText(link.getTitle());

        // Set the item click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    Log.d("YouTubeLinkAdapter", "Item clicked: " + link.getTitle());
                    onItemClickListener.onItemClick(link);  // Trigger the click callback
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return youTubeLinks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.youtubeLinkTextView);
        }
    }
}
