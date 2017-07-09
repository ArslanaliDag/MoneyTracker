package arslanali.ru.moneytracker;

import android.app.Application;
import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import arslanali.ru.moneytracker.api.LSApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LSApp extends Application {

    private static final String PREFERENCES_SESSION = "session";
    private static final String KEY_AUTH_TOKEN = "auth-token";
    private LSApi lsApi;

    @Override
    public void onCreate() {
        super.onCreate();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ?
                        HttpLoggingInterceptor.Level.HEADERS :
                        HttpLoggingInterceptor.Level.NONE))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://arslanali.getsandbox.com/") // my test server
                .baseUrl("http://android.loftschool.com/basic/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        // Interface implementation LSApi, create getItems list
        lsApi = retrofit.create(LSApi.class);
    }

    // init interface
    public LSApi initApi() {
        return lsApi;
    }

    // save token
    public void setToken(String authToken) {
        getSharedPreferences(PREFERENCES_SESSION, MODE_PRIVATE).edit().putString(KEY_AUTH_TOKEN, authToken).apply();
    }

    // get saved token in SharedPref
    public String getToken() {
        return getSharedPreferences(PREFERENCES_SESSION, MODE_PRIVATE).getString(KEY_AUTH_TOKEN, "");
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(getToken());
    }
}
