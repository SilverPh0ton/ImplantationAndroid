package com.cegeptr.projetagectr.logic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.cegeptr.projetagectr.ui.NoConnection;
import com.cegeptr.projetagectr.logic.Entity.Book;
import com.cegeptr.projetagectr.logic.Entity.Concession;
import com.cegeptr.projetagectr.logic.Entity.GroupResult;
import com.cegeptr.projetagectr.logic.Entity.History;
import com.cegeptr.projetagectr.logic.Entity.ServerResponse;
import com.cegeptr.projetagectr.logic.Entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Le singleton s'occupe de tous la gestion de donnée
 */
public class DataSingleton {
    private ServerInterface serveur = RetrofitInstance.getInstance().create(ServerInterface.class);
    private static DataSingleton ourInstance = null;
    private static Context mainContext;

    private User connectedUser = null;
    private ArrayList<GroupResult> groupResults = new ArrayList<>();
    private boolean groupResultsHasReturn = false;
    private String groupResultsQuery = "";
    private ArrayList<Concession> detailsResults = new ArrayList<>();
    private boolean detailsResultsHasReturn = false;
    private GroupResult detailsResultsGroupResult;
    private ArrayList<Concession> myConcessions = new ArrayList<>();
    private ArrayList<Concession> myReservation = new ArrayList<>();
    private ArrayList<History> myHistory = new ArrayList<>();
    private Book autoFillResult = null;
    private ArrayList<GroupResult> lstBookPop = new ArrayList<>();
    private ArrayList<GroupResult> lstBookRecent = new ArrayList<>();

    /**
     * Permet d'avoir un reference du singleton
     * @return l'instance du singleton
     */
    public static DataSingleton getInstance() {

        if(ourInstance == null){
            ourInstance = new DataSingleton(null);
        }
        return ourInstance;
    }

    /**
     * Initialise le singleton avec un reference de context
     * @return l'instance du singleton
     */
    public static DataSingleton getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DataSingleton(context);
        }
        mainContext = context;
        return ourInstance;
    }

    private DataSingleton(Context context) {
        mainContext = context;
    }

    /**********************User**********************/
    /**
     *Vérifie les informations de l'utilisateur et le connecte s'il existe
     * @param givenConnectionInfo
     * @param givenPassword
     */
    public void checkLogin(String givenConnectionInfo, String givenPassword) { //SQL
        Call<User> call = serveur.check_login(givenConnectionInfo, givenPassword);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    Log.d("Data--login_s", (response.body() == null) ? "empty" : response.body().getFirstName());
                    connectedUser = response.body();
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastcheckLogin);
                    mainContext.sendBroadcast(intent);

                    if(connectedUser != null){
                        updateUserToken();
                    }
                } catch (Exception e) {
                    Log.d("Data--login_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<User> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--login_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--login_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Connecte l'utilisateur automatiquement s'il a coché l'option à la connexion
     * @param givenConnectionInfo
     * @param givenPassword
     */
    public void autoLogin(String givenConnectionInfo, String givenPassword) { //SQL
        Call<User> call = serveur.check_login(givenConnectionInfo, givenPassword);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    Log.d("Data--autoLogin_s", (response.body() == null) ? "empty" : response.body().getFirstName());
                    connectedUser = response.body();

                    if(connectedUser != null){
                        updateUserToken();
                    }
                } catch (Exception e) {
                    Log.d("Data--autoLogin_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<User> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--autoLogin_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--autoLogin_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Déconnecte l'utilisateur actuelle
     */
    public void disconnectUser() {
        connectedUser = null;
        SharedPreferences sharedpreferences = mainContext.getSharedPreferences(Const.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Ajoute l'utilisateur
     * @param user
     */
    public void addUser(User user) { //SQL
        Call<User> call = serveur.add_user(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    Log.d("Data--addUser_s", (response.body() == null) ? "empty" : response.body().getFirstName());
                    connectedUser = response.body();
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastaddUser);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--addUser_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<User> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--addUser_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--addUser_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Modifie les informations de l'utilisateur
     * @param user
     */
    public void updateUser(User user) { //SQL
        Call<ServerResponse> call = serveur.update_user(user);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--updateUser_s", response.body().getMessage());
                    Intent intent = new Intent();
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.setAction(Const.broadcastupdateUser);
                    mainContext.sendBroadcast(intent);

                    if(response.body().getSucces()){
                        connectedUser = user;
                    }
                } catch (Exception e) {
                    Log.d("Data--updateUser_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--updateUser_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--updateUser_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Sauvegarde les changement d'avatar
     * @param user
     */
    public void updateAvatar(User user) { //SQL
        Call<ServerResponse> call = serveur.update_user(user);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--updateAvatar_s", response.body().getMessage());
                    Intent intent = new Intent();
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.setAction(Const.broadcastupdateAvatar);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--updateAvatar_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--updateAvatar_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--updateAvatar_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Modifie le mot de passe
     * @param id_user
     * @param currentPassword Mot de passe actuel en clair
     * @param newPassword Nouveau mot de passe en clair
     */
    public void updatePassword(int id_user, String currentPassword, String newPassword) { //SQL
        Call<ServerResponse> call = serveur.update_password(
                id_user,
                currentPassword,
                newPassword
        );

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--updatePasword_s", response.body().getMessage());
                    Intent intent = new Intent();
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.setAction(Const.broadcastupdatePassword);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--updatePasword_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--updatePasword_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--updatePasword_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Envoi un email avec un nouveau mot de passe
     * @param mail
     */
    public void resetPassword(String mail) { //SQL
        String newPassword = generatePassword();
        Call<ServerResponse> call = serveur.reset_password(mail, newPassword);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--resetPassword_s", response.body().getMessage());
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastResetPassword);
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--resetPassword_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--resetPassword_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--resetPassword_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Sauvegarde le token pour FireBase dans la DB
     */
    public void updateUserToken() { //SQL
        SharedPreferences sharedPreferences = mainContext.getSharedPreferences(Const.MY_PREFERENCES, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Const.PREFERENCES_USER_TOKEN, null);

        Call<ServerResponse> call = serveur.update_user_token(connectedUser.getIdUser(), token);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--updateUserToken_s", "Token:"+ token + "  |  " +response.body().getMessage());
                } catch (Exception e) {
                    Log.d("Data--updateUserToken_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--updateUserToken_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--updateUserToken_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Sauvegarde le token dans les préférence partagé
     * @param token
     */
    public void catchUserToken(String token) {
        SharedPreferences sharedPreferences = mainContext.getSharedPreferences(Const.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.PREFERENCES_USER_TOKEN, token);
        editor.apply();
        Log.d("Data--catchUserToken", "Token:"+ token);
    }

    /**
     * Génère un mot de passe temporaire
     * @return
     */
    private  String generatePassword() {
        final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Const.PASSWORD_GENERATED_LENGTH; i++) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }

        return builder.toString();
    }

    /**********************GroupSearch**********************/
    /**
     * Effectue une recherche générique et obtient une liste de résultat groupé
     */
    public void search() { //SQL

        groupResultsHasReturn = false;

        Call<List<GroupResult>> call = serveur.get_group_result(groupResultsQuery);
        call.enqueue(new Callback<List<GroupResult>>() {
            @Override
            public void onResponse(Call<List<GroupResult>> call, Response<List<GroupResult>> response) {
                try {
                    Log.d("Data--search_s", (response.body() == null) ? "Liste reçu vide" : "Liste reçu de taille: " + response.body().size());
                    groupResults = (ArrayList<GroupResult>) response.body();
                    groupResultsHasReturn = true;
                    if (groupResults == null) {
                        groupResults = new ArrayList<>();
                    }
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastsearch);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--search_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<GroupResult>> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--search_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--search_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Actualise le nombre de livre dans un groupe de livre pour l'ID précisée
     * @param idBook
     * @param amount
     */
    public void updateBookAmount(int idBook, int amount) {
        for (GroupResult groupResult : groupResults) {
            if (groupResult.getBook().getIdBook() == idBook) {
                if (amount == 0) {
                    groupResults.remove(groupResult);
                }
                groupResult.setAmount(amount);
                return;
            }
        }
    }

    /**********************DetailSearch**********************/
    /**
     * Effectue une recherche pour un livre précis (par code bar)
     */
    public void detailSearch() { //SQL

        detailsResultsHasReturn = false;

        Call<List<Concession>> call = serveur.get_detail_search(detailsResultsGroupResult.getBook().getIdBook());
        call.enqueue(new Callback<List<Concession>>() {
            @Override
            public void onResponse(Call<List<Concession>> call, Response<List<Concession>> response) {
                try {
                    Log.d("Data--detailSearch_s", (response.body() == null) ? "Liste reçu vide" : "Liste reçu de taille: " + response.body().size());
                    detailsResults = (ArrayList<Concession>) response.body();
                    detailsResultsHasReturn = true;
                    if (detailsResults == null) {
                        detailsResults = new ArrayList<>();
                    }
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastdetailSearch);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--detailSearch_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Concession>> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--detailSearch_f", t.toString());
                } catch (Exception e) {
                    Log.d("Data--detailSearch_f", e.getMessage());
                }
            }
        });
    }

    /**********************MyReservation**********************/

    /**
     * Obtient tout mes reservations
     */
    public void fetchMyReservations() { //SQL
        Call<List<Concession>> call = serveur.fetch_my_reservations(connectedUser.getIdUser());
        call.enqueue(new Callback<List<Concession>>() {
            @Override
            public void onResponse(Call<List<Concession>> call, Response<List<Concession>> response) {
                try {
                    Log.d("Data--fetchReservatio_s", (response.body() == null) ? "Liste reçu vide" : "Liste reçu de taille: " + response.body().size());
                    myReservation = (ArrayList<Concession>) response.body();
                    if (myReservation == null) {
                        myReservation = new ArrayList<>();
                    }
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastfetchMyReservations);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--fetchReservatio_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Concession>> call, java.lang.Throwable t) {
                try {
                    Log.d("Data--fetchReservatio_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--fetchReservatio_f", e.getMessage());
                }
            }
        });

    }

    /**
     * Reserve une concession
     * @param idConcession
     */
    public void reserveConcession(int idConcession) { //SQL
        Call<ServerResponse> call = serveur.reserve_concession(idConcession, connectedUser.getIdUser());
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--reserve_s", response.body().getMessage());
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastreserveConcession);
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.putExtra(Const.REQUEST_MSG, response.body().getMessage());
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--reserve_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--reserve_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--reserve_f", e.getMessage());
                }
            }
        });

    }

    /**
     * Libère une concession
     * @param idConcession
     */
    public void unreserveConcession(int idConcession) { //SQL
        Call<ServerResponse> call = serveur.unreserve_concession(idConcession);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--reserve_s", response.body().getMessage());
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastunreserveConcession);
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--reserve_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--reserve_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--reserve_f", e.getMessage());
                }
            }
        });

    }

    /**********************ManageConcession**********************/
    /**
     * Obtient tout mes concessions
     */
    public void fetchMyConcessions() { //SQL
        Call<List<Concession>> call = serveur.fetch_my_concession(connectedUser.getIdUser());
        call.enqueue(new Callback<List<Concession>>() {
            @Override
            public void onResponse(Call<List<Concession>> call, Response<List<Concession>> response) {
                try {
                    Log.d("Data--fetchConcessio_s", (response.body() == null) ? "Liste reçu vide" : "Liste reçu de taille: " + response.body().size());
                    myConcessions = (ArrayList<Concession>) response.body();
                    if (myConcessions == null) {
                        myConcessions = new ArrayList<>();
                    }
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastfetchMyConcessions);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--fetchConcessio_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Concession>> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--fetchConcessio_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--fetchConcessio_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Téléverse une photo pour un livre
     * @param uriImage
     * @param idConsession
     */
    public void uploadPhoto(Uri uriImage, int idConsession) {//SQL
        File imageFile = new File(getRealPathFromUri(mainContext, uriImage));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);
        RequestBody idConsessionToUpload = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idConsession));

        Call<ServerResponse> call = serveur.photo_upload(fileToUpload, idConsessionToUpload);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Intent intent = new Intent();
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.setAction(Const.broadcastupload_photo);
                    mainContext.sendBroadcast(intent);
                    Log.d("Data--upload_photo_s", "Photo upload:" + response.body().getMessage());
                } catch (Exception e) {
                    Log.d("Data--upload_photo_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--upload_photo_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--upload_photo_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Obtient une concession selon son ID
     * @param idConcession
     * @return
     */
    public Concession getMyConcessionById(int idConcession) {
        for (Concession concession : myConcessions) {
            if (concession.getIdConcession() == idConcession) {
                return concession;
            }
        }
        return null;
    }

    /**
     * Convertie un URL relatif en URL absolue
     * @param context
     * @param contentUri
     * @return
     */
    private static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Ajout un concession pour l'utilisateur actuel
     * @param concession
     */
    public void addConcession(Concession concession) { //SQL
        Call<Concession> call = serveur.add_concession(concession);
        call.enqueue(new Callback<Concession>() {
            @Override
            public void onResponse(Call<Concession> call, Response<Concession> response) {
                try {
                    Log.d("Data--addConcession_s", (response.body() == null) ? "empty" : "" + response.body().getIdConcession());
                    Intent intent = new Intent();
                    if (response.body() != null) {
                        intent.putExtra(Const.ID_OF_ADDED_CONCESSION, response.body().getIdConcession());
                    } else {
                        intent.putExtra(Const.ID_OF_ADDED_CONCESSION, 0);
                    }
                    intent.setAction(Const.broadcastaddConcession);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--addConcession_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Concession> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--addConcession_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--addConcession_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Modifie la concession
     * @param concession
     */
    public void updateConcession(Concession concession) { //SQL
        Call<ServerResponse> call = serveur.update_concession(concession.getIdConcession(), concession.getCustomerPrice());
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--updateConcessio_s", "Photo upload");
                    Intent intent = new Intent();
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.setAction(Const.broadcastupdateConcession);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--updateConcessio_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--updateConcessio_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--updateConcessio_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Tente d'obtenir les informations
     * @param qrCode
     */
    public void tryAutoFill(String qrCode) { //SQL
        Call<Book> call = serveur.try_auto_fill(qrCode);
        Log.d("Data--TryAutoFill_s", qrCode);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                try {
                    Log.d("Data--TryAutoFill_s", (response.body() == null) ? "Aucun livre reçu" : "Livre reçu " + response.body());
                    autoFillResult = (Book) response.body();
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastTryAutoFill);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--TryAutoFill_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Book> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--TryAutoFill_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--TryAutoFill_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Retire une concession qui n'a pas encore été validé
     * @param idConcession
     */
    public void deleteConcession(int idConcession,String state) {
        Call<ServerResponse> call = serveur.delete_concession(idConcession,state);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--deleteConcessio_s", "Photo upload");
                    Intent intent = new Intent();
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.setAction(Const.broadcastChangeConcessionState);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--deleteConcessio_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--deleteConcessio_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--deleteConcessio_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Marque une concession comme étant "à donné"
     * @param idConcession
     */
    public void giveConcession(int idConcession) {
        Call<ServerResponse> call = serveur.give_concession(idConcession);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--giveConcession_s", "Photo upload");
                    Intent intent = new Intent();
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.setAction(Const.broadcastChangeConcessionState);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--giveConcession_s", e.getMessage());
                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--giveConcession_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--giveConcession_f", e.getMessage());
                }
            }
        });
    }
    /**
     * Marque une historique comme étant "unpayed"
     * @param idHistory
     */
    public void leaveUnpayed(int idHistory) {
        Call<ServerResponse> call = serveur.unpay_concession(idHistory);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--leaveUnpay", "Photo upload");
                    Intent intent = new Intent();
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.setAction(Const.broadcastChangeConcessionState);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--leaveUnpayed", e.getMessage());
                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--leaveUnpayed", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--leaveUnpayed", e.getMessage());
                }
            }
        });
    }

    /**
     * Marque une concession comme étant "à retiré"
     * @param idConcession
     */
    public void removeConcession(int idConcession) {
        Call<ServerResponse> call = serveur.remove_concession(idConcession);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--removeConcessio_s", "Photo upload");
                    Intent intent = new Intent();
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.setAction(Const.broadcastChangeConcessionState);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--removeConcessio_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--removeConcessio_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--removeConcessio_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Renouvèle la concession selon l'ID
     * @param idConcession
     */
    public void renewConcession(int idConcession) {
        Call<ServerResponse> call = serveur.renew_concession(idConcession);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--renewConcession_s", "Photo upload");
                    Intent intent = new Intent();
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.setAction(Const.broadcastChangeConcessionState);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--renewConcession_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--renewConcession_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--renewConcession_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Renouvèle toutes les concessions de l'utilisateur
     * @param idCustomer
     */
    public void renewConcessionAll(int idCustomer) {
        Call<ServerResponse> call = serveur.renew_concession_all(idCustomer);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--renewConcession_s", "Photo upload");
                    Intent intent = new Intent();
                    intent.putExtra(Const.REQUEST_SUCCES, response.body().getSucces());
                    intent.setAction(Const.broadcastChangeConcessionState);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--renewConcession_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--renewConcession_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--renewConcession_f", e.getMessage());
                }
            }
        });
    }

    /**
     * Archive une concession selon l'ID
     * @param idConcession
     */
    public void archiveConcession(int idConcession) {
        Call<ServerResponse> call = serveur.archive_concession(idConcession);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Log.d("Data--archiveConcessi_s", "Photo upload");
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastArchiveConcession);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--archiveConcessi_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--archiveConcessi_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--archiveConcessi_f", e.getMessage());
                }
            }
        });
    }

    /**********************History**********************/
    /**
     * Obtient tout mon historiques
     */
    public void fetchMyHistory() { //SQL
        Call<List<History>> call = serveur.fetch_my_history(connectedUser.getIdUser());
        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                try {
                    Log.d("Data--fetchMyHistory_s", (response.body() == null) ? "Liste reçu vide" : "Liste reçu de taille: " + response.body().size());
                    myHistory = (ArrayList<History>) response.body();
                    if (myHistory == null) {
                        myHistory = new ArrayList<>();
                    }
                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastfetchMyHistory);
                    mainContext.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.d("Data--fetchMyHistory_s", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<History>> call, java.lang.Throwable t) {
                try {
                    mainContext.startActivity(new Intent(mainContext, NoConnection.class));
                    Log.d("Data--fetchMyHistory_f", t.getMessage());
                } catch (Exception e) {
                    Log.d("Data--fetchMyHistory_f", e.getMessage());
                }
            }
        });
    }

    public void refreshListPop()
    {
        lstBookPop.clear();

        Call<ResponseBody> call = serveur.getBestSellers();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseString = response.body().string();
                    JSONArray json_arr = new JSONArray(responseString);

                    for (int i=0; i < json_arr.length(); i++){
                        JSONObject jsonObj = json_arr.getJSONObject(i);
                        String id = jsonObj.getString("id");
                        String title = jsonObj.getString("title");
                        String author = jsonObj.getString("author");
                        String publisher = jsonObj.getString("publisher");
                        String edition = jsonObj.getString("edition");
                        String barcode = jsonObj.getString("barcode");
                        int amount = Integer.parseInt(jsonObj.getString("amount"));
                        String image = jsonObj.getString("image");

                        lstBookPop.add(new GroupResult(new Book(Integer.parseInt(id) , title, author, publisher, edition, barcode, image + ".png"), amount));
                    }

                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastBooksPopular);
                    mainContext.sendBroadcast(intent);
                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }

        });
    }

    public void refreshListRecent()
    {
        lstBookRecent.clear();

        Call<ResponseBody> call = serveur.getMostRecentlyAdded();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseString = response.body().string();
                    JSONArray json_arr = new JSONArray(responseString);

                    for (int i=0; i < json_arr.length(); i++){
                        JSONObject jsonObj = json_arr.getJSONObject(i);
                        String id = jsonObj.getString("id");
                        String title = jsonObj.getString("title");
                        String author = jsonObj.getString("author");
                        String publisher = jsonObj.getString("publisher");
                        String edition = jsonObj.getString("edition");
                        String barcode = jsonObj.getString("barcode");
                        int amount = Integer.parseInt(jsonObj.getString("amount"));
                        String image = jsonObj.getString("image");

                        lstBookRecent.add(new GroupResult(new Book(Integer.parseInt(id) , title, author, publisher, edition, barcode, image + ".png"), amount));
                    }

                    Intent intent = new Intent();
                    intent.setAction(Const.broadcastBooksRecent);
                    mainContext.sendBroadcast(intent);
                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }


        });
    }

    /**********************SetterAndGetter**********************/
    public User getConnectedUser() {
        return connectedUser;
    }

    public ArrayList<GroupResult> getGroupResults() {
        return groupResults;
    }

    public ArrayList<Concession> getDetailsResults() {
        return detailsResults;
    }

    public ArrayList<Concession> getMyConcessions() {
        return myConcessions;
    }

    public ArrayList<Concession> getMyReservation() {
        return myReservation;
    }

    public ArrayList<History> getMyHistory() {
        return myHistory;
    }

    public Book getAutoFillResult() {
        return autoFillResult;
    }

    public boolean isGroupResultsHasReturn() {
        return groupResultsHasReturn;
    }

    public boolean isDetailsResultsHasReturn() {
        return detailsResultsHasReturn;
    }

    public String getGroupResultsQuery() {
        return groupResultsQuery;
    }

    public void setGroupResultsQuery(String groupResultsQuery) {
        this.groupResultsQuery = groupResultsQuery;
    }

    public GroupResult getDetailsResultsGroupResult() {
        return detailsResultsGroupResult;
    }

    public void setDetailsResultsGroupResult(GroupResult detailsResultsGroupResult) {
        this.detailsResultsGroupResult = detailsResultsGroupResult;
    }

    public ArrayList<GroupResult> getLstBookPop(){
        return lstBookPop;
    }

    public ArrayList<GroupResult> getLstBookRecent(){
        return lstBookRecent;
    }
}
