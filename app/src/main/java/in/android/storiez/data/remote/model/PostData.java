package in.android.storiez.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class PostData {

    @SerializedName("type")
    String type;

    @SerializedName("color")
    String color;

    @SerializedName("source")
    private SourceMetaInfo source;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public SourceMetaInfo getSource() {
        return source;
    }

    public void setSource(SourceMetaInfo source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "PostData{" +
                "type='" + type + '\'' +
                ", color='" + color + '\'' +
                ", source=" + source +
                '}';
    }

    private class SourceMetaInfo {
        @SerializedName("_id")
        String id;
        @SerializedName("lang")
        String lang;
        @SerializedName("cat")
        String cat;

        @SerializedName("img")
        String img;

        @SerializedName("texts")
        String[] texts;

        @SerializedName("template")
        String template;

        @SerializedName("options")
        String[] options;

        @SerializedName("user_id")
        String userId;

        @SerializedName("created_at")
        long createdAt;

        @SerializedName("title")
        String title;

        @SerializedName("url")
        String url;

        @SerializedName("content")
        String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getCat() {
            return cat;
        }

        public void setCat(String cat) {
            this.cat = cat;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String[] getTexts() {
            return texts;
        }

        public void setTexts(String[] texts) {
            this.texts = texts;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String[] getOptions() {
            return options;
        }

        public void setOptions(String[] options) {
            this.options = options;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "SourceMetaInfo{" +
                    "id='" + id + '\'' +
                    ", lang='" + lang + '\'' +
                    ", cat='" + cat + '\'' +
                    ", img='" + img + '\'' +
                    ", texts=" + Arrays.toString(texts) +
                    ", template='" + template + '\'' +
                    ", options=" + Arrays.toString(options) +
                    ", userId='" + userId + '\'' +
                    ", createdAt=" + createdAt +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
