package arslanali.ru.moneytracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import arslanali.ru.moneytracker.R;
import arslanali.ru.moneytracker.diagram.DiagramView;

public class BalanceFragment extends Fragment {


    private TextView balance;
    private TextView expense;
    private TextView income;
    private DiagramView diagram;

    public static final String ARG_TYPE = "type";

    private String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.balance, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Init data in
        //final TextView balans = (TextView) view.findViewById(R.id.my_balans);
        //balans.setText("15000 Р");
    }
}
