package arslanali.ru.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

import arslanali.ru.moneytracker.pojo.AuthResult;

public class AuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 999;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide ActionBAr in desing
        getSupportActionBar().hide();
        setContentView(R.layout.activity_auth);

        // Configure sign-in to request the user's ID, email address, and basic
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // current input in google API
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // auth
        findViewById(R.id.authBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(googleApiClient), RC_SIGN_IN);
            }
        });
    }

    // processing result from Google when authorizing
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // analyze the answer from Google (result)
            final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // Google return my account
            if (result.isSuccess() && result.getSignInAccount() != null) {
                final GoogleSignInAccount account = result.getSignInAccount();

                // registration in LoftSchool server
                getSupportLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<AuthResult>() {
                    @Override
                    public Loader<AuthResult> onCreateLoader(int id, Bundle args) {
                        return new AsyncTaskLoader<AuthResult>(AuthActivity.this) {
                            @Override
                            public AuthResult loadInBackground() {
                                try {
                                    return ((LSApp) getApplicationContext()).initApi().getAuth(account.getId()).execute().body();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        };
                    }

                    @Override
                    public void onLoadFinished(Loader<AuthResult> loader, AuthResult result) {
                        if (result != null && result.isSuccess()) {
                            ((LSApp) getApplicationContext()).setToken(result.getAuthToken());
                            finish();
                        } else
                            showError();
                    }

                    @Override
                    public void onLoaderReset(Loader<AuthResult> loader) {
                    }
                }).forceLoad();
            } else
                showError();
        }
    }

    private void showError() {
        // // TODO: 09.07.2017 return message server
        Toast.makeText(this, R.string.errorAuth, Toast.LENGTH_SHORT).show();
    }
}
