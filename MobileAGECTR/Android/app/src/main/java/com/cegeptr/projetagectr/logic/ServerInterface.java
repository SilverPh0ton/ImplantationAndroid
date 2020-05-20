package com.cegeptr.projetagectr.logic;


import com.cegeptr.projetagectr.logic.Entity.Book;
import com.cegeptr.projetagectr.logic.Entity.Concession;
import com.cegeptr.projetagectr.logic.Entity.GroupResult;
import com.cegeptr.projetagectr.logic.Entity.History;
import com.cegeptr.projetagectr.logic.Entity.ServerResponse;
import com.cegeptr.projetagectr.logic.Entity.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServerInterface {

    /**
     * Ajoute un utilisateur
     * @param user l'utilisateur à ajouté
     * @return l'utilisateur complet si l'ajout à fonctionné, null sinon
     */
    @POST("add_user.php")
    Call<User> add_user(@Body User user);

    /**
     * Modifie un utilisateur
     * @param user l'utilisateur à modifié
     * @return une reponse de serveur générique
     */
    @POST("update_user.php")
    Call<ServerResponse> update_user(@Body User user);

    /**
     * Modifie un mot de passe
     * @param id_user identifiant de l'utilisateur
     * @param currentPassword mot de passe actuelle chiffré
     * @param newPassword nouveau mot de passe chiffré
     * @return une reponse de serveur générique
     */
    @FormUrlEncoded
    @POST("update_password.php")
    Call<ServerResponse> update_password(@Field ("id_user") int id_user,
                                         @Field ("currentPassword") String currentPassword,
                                         @Field ("newPassword") String newPassword);

    /**
     * Ajout un concession
     * @param concession la concession à ajouté
     * @return la concession complet si l'ajout à fonctionné, null sinon
     */
    @POST("add_concession.php")
    Call<Concession> add_concession(@Body Concession concession);

    /**
     * Modifie le prix d'une concession
     * @param idConcession l'identifiant de concession à modifier
     * @param customerPrice le nouveau prix
     * @return une reponse de serveur générique
     */
    @FormUrlEncoded
    @POST("update_concession_price.php")
    Call<ServerResponse> update_concession(@Field("idConcession") int idConcession,
                                           @Field("customerPrice") double customerPrice);

    /**
     * Cherche un livre avec le code bar spécifié
     * @param barcode le code bar à chercher
     * @return le livre s'il le trouve, null sinon
     */
    @FormUrlEncoded
    @POST("try_auto_fill.php")
    Call<Book> try_auto_fill(@Field("barcode") String barcode);

    /**
     * Cherche des groupes groupe de livre selon une query
     * @param research la valeur à chercher
     * @return une liste de resulat groupé
     */
    @FormUrlEncoded
    @POST("get_groupresult.php")
    Call<List<GroupResult>> get_group_result(@Field("search") String research);

    /**
     * Cherche des concessions pour un livre en particulier
     * @param idBook l'identifiant du livre
     * @return une liste de concession
     */
    @FormUrlEncoded
    @POST("get_detail_search.php")
    Call<List<Concession>> get_detail_search(@Field("idBook") int idBook);

    /**
     * Obtient la liste de reservation d'un utilisateur
     * @param id_user identifiant de l'utilisateur
     * @return une liste de concession
     */
    @FormUrlEncoded
    @POST("fetch_my_reservations.php")
    Call<List<Concession>> fetch_my_reservations(@Field("id_user") int id_user);

    /**
     * Reserve une concession
     * @param idConcession l'identifiant de concession à reserver
     * @param id_user identifiant de l'utilisateur
     * @return une reponse de serveur générique
     */
    @FormUrlEncoded
    @POST("reserve_concession.php")
    Call<ServerResponse> reserve_concession(@Field("idConcession") int idConcession,
                                            @Field("id_user") int id_user);

    /**
     * Libère une concession reservé
     * @param idConcession l'identifiant de concession à libérer
     * @return une reponse de serveur générique
     */
    @FormUrlEncoded
    @POST("unreserve_concession.php")
    Call<ServerResponse> unreserve_concession(@Field("idConcession") int idConcession);

    /**
     * Supprime une concession non aprouvé
     * @param idConcession l'identifiant de concession à supprimer
     * @return une reponse de serveur générique
     */
    @FormUrlEncoded
    @POST("delete_concession.php")
    Call<ServerResponse> delete_concession(@Field("idConcession") int idConcession,@Field("State")String state);

    /**
     * Donne une concession aprouvé
     * @param idConcession l'identifiant de concession à donné
     * @return une reponse de serveur générique
     */
    @FormUrlEncoded
    @POST("give_concession.php")
    Call<ServerResponse> give_concession(@Field("idConcession") int idConcession);

    /**
     * Retire une concession aprouvé
     * @param idConcession l'identifiant de concession à retiré
     * @return une reponse de serveur générique
     */
    @FormUrlEncoded
    @POST("remove_concession.php")
    Call<ServerResponse> remove_concession(@Field("idConcession") int idConcession) ;

    /**
     * Renouvel une concession
     * @param idConcession l'identifiant de concession à renouvler
     * @return une reponse de serveur générique
     */
    @FormUrlEncoded
    @POST("renew_concession.php")
    Call<ServerResponse> renew_concession(@Field("idConcession") int idConcession);

    /**
     * Obtient la liste de concession appartenant à un utilisateur
     * @param id_user l'identifiant de l'utilisateur
     * @return une liste de concession
     */
    @FormUrlEncoded
    @POST("fetch_my_concession.php")
    Call<List<Concession>> fetch_my_concession(@Field("id_user") int id_user);

    /**
     * Obtient la liste d'entité d'historique d'un utilisateur
     * @param id_user l'identifiant de l'utilisateur
     * @return une liste d'entité d'historique
     */
    @FormUrlEncoded
    @POST("fetch_my_history.php")
    Call<List<History>> fetch_my_history(@Field("id_user") int id_user);

    /**
     * Téléverse un photo rataché à une concession
     * @param file
     * @param idConcession l'identifiant de concession lié à la photo
     * @return une reponse de serveur générique
     */
    @Multipart
    @POST("photo_upload.php")
    Call<ServerResponse> photo_upload(
            @Part MultipartBody.Part file,
            @Part("idConcession") RequestBody idConcession);

    /**
     * Authentifie un utilisateur
     * @param connection_info le matricule ou le couriel
     * @param password le mot de passe
     * @return l'utilisateur s'il est trouvé, null sinon
     */
    @FormUrlEncoded
    @POST("check_login.php")
    Call<User> check_login(@Field("connection_info")String connection_info,
                           @Field("password")String password);

    /**
     * Actualise le token pour les notification FireBase
     * @param id_user l'identifiant de l'utilisateur
     * @param token le nouveau token
     * @return une reponse de serveur générique
     */
    @FormUrlEncoded
    @POST("update_user_token.php")
    Call<ServerResponse> update_user_token(@Field("id_user") int id_user,
                                           @Field("token") String token);

    /**
     * Réinitialise le mot de passe
     * @param mail le couriel de l'utilisateur
     * @param password le noveau mot de passe en claire
     * @return une reponse de serveur générique
     */
    @FormUrlEncoded
    @POST("reset_password.php")
    Call<ServerResponse> reset_password(@Field("mail") String mail,
                                        @Field("password") String password);

    /**
     * Archive une concession
     * @param idConcession l'identifiant de concession à archiver
     * @return une reponse de serveur générique
     */
    @FormUrlEncoded
    @POST("archive_concession.php")
    Call<ServerResponse> archive_concession(@Field("idConcession") int idConcession);
}