package in.android.storiez.data.remote;

import com.google.gson.annotations.SerializedName;

public class UserComments {


    @SerializedName("_id")
    private String postId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("text")
    private String text;

    @SerializedName("parent_id")
    private String parentId;

    @SerializedName("parent_type")
    private String parentType;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    @Override
    public String toString() {
        return "UserComments{" +
                "postId='" + postId + '\'' +
                ", userId='" + userId + '\'' +
                ", text='" + text + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentType='" + parentType + '\'' +
                '}';
    }
}
