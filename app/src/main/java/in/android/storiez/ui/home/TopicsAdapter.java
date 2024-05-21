package in.android.storiez.ui.home;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import in.android.storiez.R;
import in.android.storiez.data.local.model.ContentTopic;
import in.android.storiez.utils.StoriezApp;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicsViewHolder> {

    private static final String TAG = TopicsAdapter.class.getSimpleName();


    private OnBookmarkedClicked onBookmarkedClicked;
    List<ContentTopic> topics;

    public TopicsAdapter(List<ContentTopic> topics) {
        this.topics = topics;
    }

    @NonNull
    @Override
    public TopicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topics_layout, parent, false);
        return new TopicsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicsAdapter.TopicsViewHolder holder, int position) {
        holder.bind(topics.get(position));
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void updateTopics(List<ContentTopic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    public class TopicsViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView topicImg;
        TextView topic;
        ImageView bookmarkImg;

        public TopicsViewHolder(@NonNull View itemView) {
            super(itemView);
            topicImg = itemView.findViewById(R.id.shapeableImageView);
            topic = itemView.findViewById(R.id.topics_txt);
            bookmarkImg = itemView.findViewById(R.id.bookmark_img);
            setUpListeners();
        }

        private void setUpListeners() {
            bookmarkImg.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ContentTopic currentTopic = topics.get(position);
                    boolean newSelectionState = !currentTopic.isSelected();

                    // Update database
                    StoriezApp.getAppDatabase().contentTopicDao().updateSelection(currentTopic.getId(), newSelectionState);

                    // Update object state and notify
                    currentTopic.setSelected(newSelectionState);
                    notifyItemChanged(position);

                    // Log for debugging
                    Log.d(TAG, "Bookmark clicked for position: " + position + ", new selection state: " + newSelectionState);
                }
                onBookmarkedClicked.onClick();
            });
        }

        public void bind(ContentTopic contentTopic) {
            topic.setText(contentTopic.getTitle());

            Glide.with(itemView.getContext())
                    .load(contentTopic.getImgUrl())
                    .into(topicImg);

            topicImg.setBackgroundColor(Color.parseColor(contentTopic.getColor()));

            if (contentTopic.isSelected()) {
                bookmarkImg.setImageResource(R.drawable.baseline_bookmark_24);
            } else {
                bookmarkImg.setImageResource(R.drawable.baseline_bookmark_border_24);
            }
        }
    }


    public void setOnBookmarkedClicked(OnBookmarkedClicked onBookmarkedClicked) {
        this.onBookmarkedClicked = onBookmarkedClicked;
    }

    public interface OnBookmarkedClicked {
        void onClick();
    }
}
