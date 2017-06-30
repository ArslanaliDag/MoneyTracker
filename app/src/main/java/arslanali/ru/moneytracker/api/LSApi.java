package arslanali.ru.moneytracker.api;

import java.util.List;

import arslanali.ru.moneytracker.pojo.Item;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface LSApi {

    @Headers("Content-Type: application/json") // set header JSON
    @GET("items")
    Call<List<Item>> items(@Query("type") String type); // call pojo object
}
