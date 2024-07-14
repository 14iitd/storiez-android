package in.android.storiez.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.android.storiez.R;
import in.android.storiez.items.PostMetaInfo;
import in.android.storiez.items.QuestionItem;
import in.android.storiez.items.RepliesItem;
import in.android.storiez.utils.BasicUtils;
import in.android.storiez.utils.StoriezApp;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {
    List<QuestionItem> questionItems;
    static ArrayList<RepliesItem> areaList;

    static Context context;
    static BasicUtils basicUtils;

    public QuestionAdapter(Context context) {
        questionItems = new ArrayList<>();
        areaList = new ArrayList<>();
        this.context = context;
        basicUtils = new BasicUtils(StoriezApp.getInstance());
    }

    public void addData(List<QuestionItem> newData) {
        questionItems.addAll(newData);
        notifyDataSetChanged();
    }

    private OnLikePostClicked onLikePostClicked;
    private OnSharePostClicked onSharePostClicked;
    private OnCommentPostClicked onCommentPostClicked;

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.post_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
        holder.bind(questionItems.get(position), null);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            holder.bind(questionItems.get(position), null);
        } else {
            holder.bind(questionItems.get(position), payloads.get(0));
        }
    }

    @Override
    public int getItemCount() {
        return questionItems.size();
    }

    public QuestionItem getPostItemByPos(int position) {
        if (position >= 0 && position < getItemCount()) {
            return questionItems.get(position);
        } else {
            return null;
        }
    }

    public void likePost(int likePos) {
        if (likePos < questionItems.size()) {
            questionItems.get(likePos).setPostLiked(true);
            questionItems.get(likePos).setPostLikeCount(questionItems.get(likePos).getPostLikeCount() + 1);
            notifyItemChanged(likePos, "like");
        }
    }

    public void removeLikeFromPost(int likePos) {
        if (likePos < questionItems.size()) {
            questionItems.get(likePos).setPostLiked(false);
            questionItems.get(likePos).setPostLikeCount(questionItems.get(likePos).getPostLikeCount() - 1);
            notifyItemChanged(likePos, "like");
        }
    }

    public void updatePostMetaInfo(int currentPostItemPos, PostMetaInfo postMetaInfo) {

        if (currentPostItemPos < questionItems.size()) {
            questionItems.get(currentPostItemPos).setPostMetaInfo(postMetaInfo);
            notifyItemChanged(currentPostItemPos, "meta");
        }
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        WebView webView;
        ImageView heartImg, shareImg, commentImg;
        TextView likeCount, commentCount, shareCount;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);

            webView = itemView.findViewById(R.id.webView);
            heartImg = itemView.findViewById(R.id.heart_img);
            shareImg = itemView.findViewById(R.id.share_img);
            commentImg = itemView.findViewById(R.id.comment_img);
            likeCount = itemView.findViewById(R.id.likes_count_txt);
            commentCount = itemView.findViewById(R.id.comment_count_txt);
            shareCount = itemView.findViewById(R.id.share_count_txt);

            setUpListeners();
        }

        private void setUpListeners() {
            heartImg.setOnClickListener(v -> {
                onLikePostClicked.onClick(questionItems.get(getAdapterPosition()), getAdapterPosition());
            });

            commentImg.setOnClickListener(v -> {
                onCommentPostClicked.onClick(questionItems.get(getAdapterPosition()));
            });

            shareImg.setOnClickListener(v -> {
                onSharePostClicked.onClick(questionItems.get(getAdapterPosition()));
            });
        }

        void bind(QuestionItem questionItem, Object payload) {
            if (payload == null) {
                // Full bind
                if (Objects.equals(questionItem.getType(), "html5")) {
                    webView.setVisibility(View.VISIBLE);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            webView.setVisibility(View.VISIBLE);
                        }
                    });
                    webView.loadUrl(questionItem.getSource());
                    webView.getSettings().setJavaScriptEnabled(true);
                }
                if (questionItem.getPostMetaInfo() !=null){
                    likeCount.setText(String.valueOf(questionItem.getPostMetaInfo().getLikesCount()));
                    commentCount.setText(String.valueOf(questionItem.getPostMetaInfo().getCommentsCount()));
                    shareCount.setText(String.valueOf(questionItem.getPostMetaInfo().getShareCount()));
                }
            } else {
                // Partial bind
                switch (payload.toString()) {
                    case "like":
                        likeCount.setText(String.valueOf(questionItem.getPostLikeCount()));
                        heartImg.setImageResource(questionItem.isPostLiked() ? R.drawable.heart_filled_icon : R.drawable.heart_icon);
                        break;
                    case "comment":
                        commentCount.setText(String.valueOf(questionItem.getPostCommentCount()));
                        break;
                    case "share":
                        shareCount.setText(String.valueOf(questionItem.getPostShareCount()));
                        break;

                    case "meta":
                        likeCount.setText(String.valueOf(questionItem.getPostMetaInfo().getLikesCount()));
                        heartImg.setImageResource(questionItem.isPostLiked() ? R.drawable.heart_filled_icon : R.drawable.heart_icon);
                        commentCount.setText(String.valueOf(questionItem.getPostMetaInfo().getCommentsCount()));
                        shareCount.setText(String.valueOf(questionItem.getPostMetaInfo().getShareCount()));
                        Log.d("TAG", "bind: value of particular post is id ");
                }
            }
        }
    }

    public void setOnLikePostClicked(OnLikePostClicked onLikePostClicked) {
        this.onLikePostClicked = onLikePostClicked;
    }

    public void setOnSharePostClicked(OnSharePostClicked onSharePostClicked) {
        this.onSharePostClicked = onSharePostClicked;
    }

    public void setOnCommentPostClicked(OnCommentPostClicked onCommentPostClicked) {
        this.onCommentPostClicked = onCommentPostClicked;
    }

    public interface OnSharePostClicked {
        void onClick(QuestionItem questionItem);
    }

    public interface OnCommentPostClicked {
        void onClick(QuestionItem questionItem);
    }

    public interface OnLikePostClicked {
        void onClick(QuestionItem questionItem, int pos);
    }
}
