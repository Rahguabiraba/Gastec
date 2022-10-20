package es.ifp.gastec.view.edit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import es.ifp.gastec.StartActivity;

public class editTransactionFragment extends Fragment {

    //Declaración de variables
    protected AutoCompleteTextView autoCompleteTxt1, autoCompleteTxt2;
    protected ArrayAdapter<String> adapterItems1, adapterItems2;
    protected String[] items1, items2;
    protected EditText caja1, caja2, caja3;
    protected TextView datetime;
    protected Button button;

    protected String id, title, amount, type, tag, date, note;

    private DatePickerDialog picker;

    protected DataBaseHelper dataBase;

    protected String contenidoCaja1 = "";
    protected String contenidoCaja2 = "";
    protected float numeroPos = 0;
    protected float numeroNeg = 0;
    protected float numero = 0;

    private Intent pasarPantalla;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate del layout en el fragment
        View view = inflater.inflate(R.layout.fragment_edit_transaction, container, false);

        //Referencia de los objetos de la interfaz grafica
        caja1 = view.findViewById(R.id.caja1_create);
        caja2 = view.findViewById(R.id.caja2_create);
        autoCompleteTxt1 = view.findViewById(R.id.autocomplete1_create);
        autoCompleteTxt2 = view.findViewById(R.id.autocomplete2_create);
        datetime = view.findViewById(R.id.date_create);
        caja3 = view.findViewById(R.id.caja3_create);
        button = view.findViewById (R.id.button_edit);

        //Bundle para coger los datos del CustomAdapter
        Bundle bundle = getArguments();

        if (bundle != null) {
            //Coger Bundle data
            id = bundle.getString("id");
            title = bundle.getString("title");
            amount = bundle.getString("amount");
            type = bundle.getString("type");
            tag = bundle.getString("tag");
            date = bundle.getString("date");
            note = bundle.getString("note");
        }

        //Poner Bundle data
        caja1.setText(title);
        autoCompleteTxt1.setText(type);
        autoCompleteTxt2.setText(tag);
        datetime.setText(date);
        caja3.setText(note);

        numero = Float.parseFloat(amount);

        if(numero < 0) {
            caja2.setText(Float.toString(-numero));
        } else {
            caja2.setText(Float.toString(numero));
        }

        //Añadido Array de Strings en el sppiner TextInputLayout (Transactions Type)
        items1 = getResources().getStringArray(R.array.transaction_types);
        adapterItems1 = new ArrayAdapter<String>(getActivity(), R.layout.list_item, items1);
        autoCompleteTxt1.setAdapter(adapterItems1);

        //Añadido Array de Strings en el sppiner TextInputLayout (Tag)
        items2 = getResources().getStringArray(R.array.tag);
        adapterItems2 = new ArrayAdapter<String>(getActivity(), R.layout.list_item, items2);
        autoCompleteTxt2.setAdapter(adapterItems2);

        //Código para Calendario en TextView
        datetime.setOnClickListener(new View.OnClickListener() {
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
                        datetime.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        //Button Edit
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                contenidoCaja1 = autoCompleteTxt1.getText().toString();
                contenidoCaja2 = caja2.getText().toString();

                if(contenidoCaja1.equals("Income")) {
                    numeroPos = Float.parseFloat(contenidoCaja2);
                    //Conexion con base de datos
                    dataBase = new DataBaseHelper(getActivity());
                    dataBase.updateData(id,
                            caja1.getText().toString().trim(),
                            numeroPos,
                            autoCompleteTxt1.getText().toString().trim(),
                            autoCompleteTxt2.getText().toString().trim(),
                            datetime.getText().toString().trim(),
                            caja3.getText().toString().trim());

                } else {
                    numeroNeg = Float.parseFloat(contenidoCaja2);
                    numeroNeg = numeroNeg * (-1);

                    dataBase = new DataBaseHelper(getActivity());
                    dataBase.updateData(id,
                            caja1.getText().toString().trim(),
                            numeroNeg,
                            autoCompleteTxt1.getText().toString().trim(),
                            autoCompleteTxt2.getText().toString().trim(),
                            datetime.getText().toString().trim(),
                            caja3.getText().toString().trim());
                }
                pasarPantalla = new Intent(getContext(), StartActivity.class);
                getContext().startActivity(pasarPantalla);
            }
        });

        return view;
    }

    //Habilitar el menú
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
        menu.removeItem(R.id.action_share);
        menu.removeItem(R.id.action_share_image);
        menu.removeItem(R.id.action_share_text);
        menu.removeItem(R.id.action_delete);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Transaction");
        super.onCreateOptionsMenu(menu, inflater);
    }

    
}