package in.abhishek.playchat.utils;

public class Constants {

    public static final String API = "https://playchat.live/";//http://200.69.21.171:8000/  https://playchat.live/


    public static final String API_LOGIN = API+"api/login";

    public static final String API_GET_QUESTIONS = API+"get-question/bulk";

    public static final String API_POST_QUESTIONS_RESPONSE = API+ "mcq/submit-response";

    public static final String API_POST_CREATE_QUESTIONS = API+ "mcq/create-question";

    public static final String API_POST_COMMENT = API+ "post/";

    public static final String API_POST_CREATE_FLASHCARD = API+ "flash/create-question";

    public static final String API_POST_CREATE_POST = API+ "api/posts";

    public static final String API_POST_IMAGE_UPLOAD = API+ "file";


    public static final boolean SUPER_USER = false;  //todo --> make hardcoded 'false' before release
    public static final String STORAGE_PATH                                                         = "/PLAYCHAT/";
}
