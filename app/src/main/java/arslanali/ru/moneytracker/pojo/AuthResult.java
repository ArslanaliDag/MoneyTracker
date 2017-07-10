package arslanali.ru.moneytracker.pojo;

import arslanali.ru.moneytracker.api.Result;

public class AuthResult extends Result {
    private String authToken;

    // need an empty constructor to initialize
    // without initialization, it does not go to the main window
    // can not see variable authToken
    AuthResult() {
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
