package arslanali.ru.moneytracker;

import android.app.Application;

import arslanali.ru.moneytracker.api.LSApi;

public class LSApp extends Application {

    private LSApi api;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    // init interface
    public LSApp(LSApi api) {
        this.api = api;
    }
}
