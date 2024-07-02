package in.android.storiez.items;

import com.google.gson.annotations.SerializedName;

public class PostMetaInfo {


    String postId;

    @SerializedName("liked")
    private boolean isLiked;

    @SerializedName("total_likes")
    private int likesCount;

    @SerializedName("total_comments")
    private int commentsCount;

    @SerializedName("total_share")
    private int shareCount;

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "PostMetaInfo{" +
                "postId='" + postId + '\'' +
                ", isLiked=" + isLiked +
                ", likesCount=" + likesCount +
                ", commentsCount=" + commentsCount +
                ", shareCount=" + shareCount +
                '}';
    }
}

