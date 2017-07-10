package arslanali.ru.moneytracker.api;

import android.text.TextUtils;

public class Result {

    private String status;

    public boolean isSuccess() {
        return TextUtils.equals(getStatus(), "success");
    }

    public String getStatus() {
        return status;
    }
}
