package arslanali.ru.moneytracker.api;

import java.util.List;

import arslanali.ru.moneytracker.pojo.Item;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface LSApi {

    @Headers("Content-Type: application/json") // set header JSON

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
}
