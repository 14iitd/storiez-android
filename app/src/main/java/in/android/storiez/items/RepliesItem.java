package in.android.storiez.items;

import java.util.Comparator;

public class RepliesItem {
    String _id;
    String user_id;
    String text;
    String created_at;
    String parent_id;
    String parent_type;

    public static final Comparator<RepliesItem> SORT_BY_ALPHABET =new Comparator<RepliesItem>() {
        @Override
        public int compare(RepliesItem areaItem, RepliesItem t1) {
            return areaItem.text.compareTo(t1.text);
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_type() {
        return parent_type;
    }

    public void setParent_type(String parent_type) {
        this.parent_type = parent_type;
    }

    @Override
    public String toString() {
        return "RepliesItem{" +
                "_id='" + _id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", text='" + text + '\'' +
                ", created_at='" + created_at + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", parent_type='" + parent_type + '\'' +
                '}';
    }
}
