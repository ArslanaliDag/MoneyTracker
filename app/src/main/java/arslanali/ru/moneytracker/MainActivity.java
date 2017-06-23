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

        // "кнопка" плюс
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
                // add.setEnabled(true);
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
                // add.setEnabled(true);

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

    // Проверка пустоеполе ввода или нет
    private boolean isEmpty(EditText etText) {

        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}
