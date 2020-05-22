package com.cegeptr.projetagectr.logic;

public class Const {
    /**
     * Information du serveur
     */
    public static final String SERVER_ADDRESS = "https://ogy.fr/AGECTR";
    public static final String END_POINTS_ADDRESS = SERVER_ADDRESS + "/MobileAGECTR/ServerSide/functions/";
    public static final String BOOK_IMG_ADDRESS = SERVER_ADDRESS + "/GlobalAGECTR/upload_photo_book/";
    public static final String CONCESSION_IMG_ADDRESS = SERVER_ADDRESS + "/GlobalAGECTR/upload_photo_concession/";
    public static final String RECEPTION_IMG_ADDRESS = SERVER_ADDRESS + "/GlobalAGECTR/upload_photo_reception/";

    /**
     * Code de broadcast pour le serveur
     */
    public final static String broadcastcheckLogin = "com.cegeptr.projetagectr.broadcastcheckLogin";
    public final static String broadcastaddUser = "com.cegeptr.projetagectr.broadcastaddUser";
    public final static String broadcastupdateUser = "com.cegeptr.projetagectr.broadcastupdateUser";
    public final static String broadcastsearch = "com.cegeptr.projetagectr.broadcastsearch";
    public final static String broadcastdetailSearch = "com.cegeptr.projetagectr.broadcastdetailSearch";
    public final static String broadcastfetchMyReservations = "com.cegeptr.projetagectr.broadcastfetchMyReservations";
    public final static String broadcastfetchMyConcessions = "com.cegeptr.projetagectr.broadcastfetchMyConcessions";
    public final static String broadcastupload_photo = "com.cegeptr.projetagectr.broadcastupload_photo";
    public final static String broadcastaddConcession = "com.cegeptr.projetagectr.broadcastResearch";
    public final static String broadcastupdateConcession = "com.cegeptr.projetagectr.broadcastupdateConcession";
    public final static String broadcastTryAutoFill = "com.cegeptr.projetagectr.broadcastTryAutoFill";
    public final static String broadcastreserveConcession = "com.cegeptr.projetagectr.broadcastreserveConcession";
    public final static String broadcastunreserveConcession = "com.cegeptr.projetagectr.broadcastunreserveConcession";
    public final static String broadcastfetchMyHistory = "com.cegeptr.projetagectr.broadcastfetchMyHistory";
    public final static String broadcastupdatePassword = "com.cegeptr.projetagectr.broadcastupdatePassword";
    public final static String broadcastupdateAvatar = "com.cegeptr.projetagectr.broadcastupdateAvatar";
    public final static String broadcastResetPassword = "com.cegeptr.projetagectr.broadcastResetPassword";
    public final static String broadcastArchiveConcession = "com.cegeptr.projetagectr.broadcastArchiveConcession";
    public final static String broadcastBooksPopular = "com.cegeptr.projetagectr.broadcastBooksPopular";
    public final static String broadcastBooksRecent = "com.cegeptr.projetagectr.broadcastBooksRecent";


    /**
     * Code de broadcast à l'interne
     */
    public final static String broadcastChangeConcessionState = "com.cegeptr.projetagectr.broadcastChangeConcessionState";
    public final static String broadcastChangeFragment = "com.cegeptr.projetagectr.broadcastChangeFragment";
    public final static String broadcastRefreshDrawer = "com.cegeptr.projetagectr.broadcastRefreshDrawer";

    /**
     * État
     */
    public final static String STATE_DENIED = "refuser";
    public final static String STATE_PENDING = "validation";

    public final static String STATE_ACCEPT = "disponible";
    public final static String STATE_TO_RENEW = "renouveler";
    public final static String STATE_UPDATE = "modification";
    public final static String STATE_TO_REMOVE = "a_retirer";
    public final static String STATE_TO_PAY = "a_paye";

    public final static String STATE_REMOVED = "retirer";
    public final static String STATE_PAYED = "paye";
    public final static String STATE_GIVEN = "dons";
    public final static String STATE_UNPAYED = "nonpaye";

    /**
     * Extra
     */
    public final static String CONCESSION_TO_DISPLAY = "CONCESSION_TO_DISPLAY";
    public final static String BARCODE_TO_SEARCH = "BARCODE_TO_SEARCH";
    public final static String ID_OF_ADDED_CONCESSION = "ID_OF_ADDED_CONCESSION";
    public final static String REQUEST_SUCCES = "REQUEST_SUCCES";
    public final static String REQUEST_MSG = "REQUEST_MSG";
    public final static String DISPLAY_FRAGMENT = "DISPLAY_FRAGMENT";

    /**
     * Valeur d'extra
     */
    public final static String DISPLAY_FRAGMENT_SEARCH = "DISPLAY_FRAGMENT_SEARCH";
    public final static String DISPLAY_FRAGMENT_ABOUT = "DISPLAY_FRAGMENT_ABOUT";
    public final static String DISPLAY_FRAGMENT_INFO = "DISPLAY_FRAGMENT_INFO";
    public final static String DISPLAY_FRAGMENT_RESERVATION = "DISPLAY_FRAGMENT_RESERVATION";
    public final static String DISPLAY_FRAGMENT_MY_BOOK = "DISPLAY_FRAGMENT_MY_BOOK";
    public final static String DISPLAY_FRAGMENT_MY_HYSTORY = "DISPLAY_FRAGMENT_MY_HYSTORY";

    /**
     * Code de permissions
     */
    public static final int IMAGE_PERMISSION_CODE = 1000;
    public static final int IMAGE_CAPTURE_CODE = 1001;
    public static final int GALLERY_REQUEST_CODE = 1002;

    /**
     * Préférence partagé
     */
    public static final String MY_PREFERENCES = "MY_PREFERENCES";
    public static final String PREFERENCE_HASHED_PASSWORD = "PREFERENCE_HASHED_PASSWORD";
    public static final String PREFERENCES_USERNAME = "PREFERENCES_USERNAME";
    public static final String PREFERENCES_USER_TOKEN = "PREFERENCES_USER_TOKEN";

    /**
     * Divers
     */
    public final static int PASSWORD_GENERATED_LENGTH = 8;
    public final static int MIN_QUERY_SIZE = 2;
    public final static String MAX_SERVER_RESPONSE = "MAX"; //Reponse du serveur si le nombre de reservation est atteint
    public final static String DENY_EMAIL = "@edu.cegeptr.qc.ca";
}
