package arslanali.ru.moneytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView add = (TextView) findViewById(R.id.add);

        // Поле для ввода названия
        final EditText name = (EditText) findViewById(R.id.name);

        // Добавляем слушателя на изменение ссотояния ввода текста в поле "названия"
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // при вводе текста активируем "плюс"
                add.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Поле для ввода рублей
        final EditText ruble = (EditText) findViewById(R.id.ruble);

        // Добавляем слушателя на изменение ссотояния ввода текста в поле "рубль"
        ruble.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // при вводе текста активируем "плюс"
                add.setEnabled(true);
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
}
