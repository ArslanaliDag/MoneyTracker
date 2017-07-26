package arslanali.ru.moneytracker.api;

import java.util.List;

import arslanali.ru.moneytracker.pojo.AuthResult;
import arslanali.ru.moneytracker.pojo.BalanceResult;
import arslanali.ru.moneytracker.pojo.Item;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LSApi {

    @Headers("Content-Type: application/json") // set header JSON

/* API in my test ser server: http://arslanali.getsandbox.com/

    // get all items
    @GET("items/get")
        // http://arslanali.getsandbox.com/items/get
    Call<List<Item>> getItems(@Query("type") String type); // call pojo object

    // add item
    @GET("items/add")
    // http://arslanali.getsandbox.com/items/add - query
    Call<List<Item>> addItem(@Query("name") String name,
                             @Query("price") int price,
                             @Query("type") String type);
*/

    // auth in server
    @GET("auth")
    Call<AuthResult> getAuth(@Query("social_user_id") String socialUserId);

    // get all items
    @GET("items")
    Call<List<Item>> getItems(@Query("type") String type);

    // add item
    @POST("items/add")
    Call<List<Item>> addItem(@Query("price") int price,
                             @Query("name") String name,
                             @Query("type") String type);

    // delete item
    @POST("items/remove")
    Call<List<Item>> removeItem(@Query("id") int idItem);
//    Call<Item> removeItem(@Query("id") int idItem);

    // get balance and view in my diagram
    @GET("balance")
    Call<BalanceResult> getBalance();
}
