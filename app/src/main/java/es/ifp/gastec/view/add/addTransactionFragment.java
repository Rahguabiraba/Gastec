package es.ifp.gastec.view.add;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import es.ifp.gastec.BD.DataBaseHelper;
import es.ifp.gastec.R;
import es.ifp.gastec.view.dashboard.DashboardFragment;

public class addTransactionFragment extends Fragment {

    //Declaración de variables
    protected AutoCompleteTextView autoCompleteTxt1;
    protected ArrayAdapter<String> adapterItems1;
    protected String[] items1;
    protected AutoCompleteTextView autoCompleteTxt2;
    protected String[] items2;
    protected ArrayAdapter<String> adapterItems2;
    protected EditText caja1;
    protected EditText caja2;
    protected EditText caja3;
    protected Button button, button2;

    protected String contenidoCaja1;
    protected String contenidoCaja2;
    protected float numeroPos;
    protected float numeroNeg;

    protected TextView date;
    private DatePickerDialog picker;

    protected DataBaseHelper dataBase;

    private Fragment dashFrag;
    private FragmentTransaction fm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate el layout en el fragment
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        //Referencia de los objetos de la interfaz grafica
        caja1 = view.findViewById(R.id.caja1_create);
        caja2 = view.findViewById(R.id.caja2_create);
        caja3 = view.findViewById(R.id.caja3_create);
        button = view.findViewById (R.id.button_edit);
        button2 = view.findViewById (R.id.button_exit_add);
        autoCompleteTxt1 = view.findViewById (R.id.autocomplete1_create);
        autoCompleteTxt2 = view.findViewById(R.id.autocomplete2_create);
        date = view.findViewById(R.id.date_create);

        //Añadido Array de Strings en el sppiner TextInputLayout (Transactions Type)
        items1 = getResources().getStringArray(R.array.transaction_types);
        adapterItems1 = new ArrayAdapter<String>(getActivity(), R.layout.list_item, items1);
        autoCompleteTxt1.setAdapter(adapterItems1);

        //Añadido Array de Strings en el sppiner TextInputLayout (Tag)
        items2 = getResources().getStringArray(R.array.tag);
        adapterItems2 = new ArrayAdapter<String>(getActivity(), R.layout.list_item, items2);
        autoCompleteTxt2.setAdapter(adapterItems2);

        //Código para Calendario en TextView
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date Picker Dialog
                picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        //Button para añadir los datos para la base de datos
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contenidoCaja1 = autoCompleteTxt1.getText().toString();
                contenidoCaja2 = caja2.getText().toString();

                if(contenidoCaja1.equals("Income")) {
                    numeroPos = Float.parseFloat(contenidoCaja2);
                    dataBase = new DataBaseHelper(getActivity());
                    dataBase.addTransaction(caja1.getText().toString().trim(),
                            numeroPos,
                            autoCompleteTxt1.getText().toString().trim(),
                            autoCompleteTxt2.getText().toString().trim(),
                            date.getText().toString().trim(),
                            caja3.getText().toString().trim());
                } else {
                    //Añadir numero como positivo para hacer el calculo del total
                    numeroNeg = Float.parseFloat(contenidoCaja2);
                    numeroNeg = numeroNeg * (-1);

                    dataBase = new DataBaseHelper(getActivity());
                    dataBase.addTransaction(caja1.getText().toString().trim(),
                            numeroNeg,
                            autoCompleteTxt1.getText().toString().trim(),
                            autoCompleteTxt2.getText().toString().trim(),
                            date.getText().toString().trim(),
                            caja3.getText().toString().trim());
                }

                //llamada del fragment Dashboard
                dashFrag = new DashboardFragment();
                fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.mainContainer,dashFrag).commit();

            }
        });

        //Button Volver
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashFrag = new DashboardFragment();
                fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.mainContainer,dashFrag).commit();
            }
        });

        //Cambiar el titulo de la activity
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.text_title_add);

        return view;
    }
}