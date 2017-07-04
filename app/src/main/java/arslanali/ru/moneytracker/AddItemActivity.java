package arslanali.ru.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import arslanali.ru.moneytracker.pojo.Item;

public class AddItemActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "type";
    public static final String RESULT_ITEM = "item";
    public static final int RC_ADD_ITEM = 99;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        type = getIntent().getStringExtra(EXTRA_TYPE); // get type of income or expense

        // add textview-button
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

        // add listener to add textview-button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                // // TODO: 03.07.2017 Process input of null values
                result.putExtra(RESULT_ITEM, new Item(name.getText().toString(),
                        Integer.parseInt(ruble.getText().toString()), type));
                setResult(RESULT_OK, result); // send the result to the called fragment
                finish();
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
