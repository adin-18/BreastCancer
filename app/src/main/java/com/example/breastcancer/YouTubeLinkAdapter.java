package com.example.breastcancer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class YouTubeLinkAdapter extends RecyclerView.Adapter<YouTubeLinkAdapter.ViewHolder> {
    private List<YouTubeLink> youtubeLinks; // List of YouTubeLink objects
    private Context context;
    private OnItemClickListener listener;

    // Define the interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(YouTubeLink link);
    }

    public YouTubeLinkAdapter(Context context, List<YouTubeLink> youtubeLinks) {
        this.context = context;
        this.youtubeLinks = youtubeLinks;
    }

    // Setter for the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_link, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YouTubeLink link = youtubeLinks.get(position);
        holder.youtubeLinkTextView.setText(link.getTitle());

        // Set click listener on the item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(link); // Notify the listener of the click event
            }
        });
    }

    @Override
    public int getItemCount() {
        return youtubeLinks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView youtubeLinkTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            youtubeLinkTextView = itemView.findViewById(R.id.youtubeLinkTextView);
        }
    }
}
