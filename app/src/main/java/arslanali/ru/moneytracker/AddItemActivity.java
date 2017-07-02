package arslanali.ru.moneytracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        final TextView add = (TextView) findViewById(R.id.add);

        final EditText name = (EditText) findViewById(R.id.name);
        // add listener on enter text
        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (isEmpty(name)) {
                    add.setEnabled(false);
                } else {
                    add.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final EditText ruble = (EditText) findViewById(R.id.ruble);
        // add listener on enter text
        ruble.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (isEmpty(ruble)) {
                    add.setEnabled(false);
                } else {
                    add.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // Check EditText is empty or not
    private boolean isEmpty(EditText etText) {

        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}
