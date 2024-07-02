package in.android.storiez.ui.home.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.android.storiez.R;
import in.android.storiez.data.remote.UserComments;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {


    private List<UserComments> comments;

    public CommentsAdapter(List<UserComments> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {

        holder.bind(comments.get(position));

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<UserComments> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public void addComment(String comment, String deviceId) {
        UserComments newComment = new UserComments();
        newComment.setText(comment);
        newComment.setUserId(deviceId);


        if (comments != null) {
            comments.add(0, newComment);  // Add the new comment at the 0th position
            notifyItemInserted(0);  // Notify the adapter that an item has been inserted at position 0
            notifyDataSetChanged();  // Notify the adapter that the data set has changed
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView userName;
        TextView comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            comment = itemView.findViewById(R.id.user_comment);

        }

        public void bind(UserComments userComments) {

            userName.setText(userComments.getUserId());
            comment.setText(userComments.getText());
        }
    }
}
