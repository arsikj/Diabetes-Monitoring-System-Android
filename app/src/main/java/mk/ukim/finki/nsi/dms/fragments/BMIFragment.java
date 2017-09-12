package mk.ukim.finki.nsi.dms.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

import mk.ukim.finki.nsi.dms.R;

/**
 * Created by dejan on 04.9.2017.
 */

public class BMIFragment extends Fragment {

    private EditText height;
    private EditText weight;
    private Button calculate;
    private TextView totalBMI;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        initComponents(view);
        initListeners();

        return view;
    }

    private void initComponents(View view){
        height = (EditText) view.findViewById(R.id.height);
        weight = (EditText) view.findViewById(R.id.weight);
        calculate = (Button) view.findViewById(R.id.calculate);
        totalBMI = (TextView) view.findViewById(R.id.totalBMI);
    }

    private void initListeners(){
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat df = new DecimalFormat("0.00");
                String result = df.format(calculateBMI());
                totalBMI.setText("YOUR BMI: " + result);
            }
        });
    }

    private double calculateBMI(){

        if(height.getText() != null && weight.getText() != null && weight.getText().length() > 0 && height.getText().length() > 0) {
            double heightValue = Double.parseDouble(height.getText().toString()) / 100;
            double weightValue = Double.parseDouble(weight.getText().toString());

            if (heightValue != 0)
                return weightValue / heightValue / heightValue;
            else
                return 0;
        } else {
            return 0;
        }
    }
}
