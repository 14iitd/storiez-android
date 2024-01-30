package in.abhishek.playchat.items;

public class LoginItem {
    String _id;
    String device_id;
    String id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LoginItem{" +
                "_id='" + _id + '\'' +
                ", device_id='" + device_id + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
