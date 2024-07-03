package in.android.storiez.data.local.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PostTypeUrls {
    @SerializedName("data")
    private List<PostType> data;

    public List<PostType> getData() {
        return data;
    }

    public void setData(List<PostType> data) {
        this.data = data;
    }
}

