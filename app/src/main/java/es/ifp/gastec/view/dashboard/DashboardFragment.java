package es.ifp.gastec.view.dashboard;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import es.ifp.gastec.BD.DataBaseHelper;
import es.ifp.gastec.R;
import es.ifp.gastec.custom.CustomAdapter;
import es.ifp.gastec.view.about.aboutTransactionFragment;
import es.ifp.gastec.view.add.addTransactionFragment;

public class DashboardFragment extends Fragment {

    //Declaración de variables
    private FloatingActionButton button;
    protected RecyclerView recyclerView;
    private ViewStub myViewStub;
    private View dashView;
    protected TextView amount_txt, totalIncome_txt, totalExpense_txt, totalBalance_txt;

    private Fragment addFrag, aboutFrag;
    private FragmentTransaction fm;

    protected CustomAdapter customAdapter;

    protected DataBaseHelper database;
    protected ArrayList<String> id, title, type, tag, date, note;
    protected ArrayList<Float> amount;

    protected float sumIncome = 0;
    protected float sumExpense = 0;
    protected float sumTotal;

    private boolean isNight = false;

    protected int[] programImages = {
            R.drawable.ic_entertainment,
            R.drawable.ic_food,
            R.drawable.ic_housing,
            R.drawable.ic_medical,
            R.drawable.ic_insurance,
            R.drawable.ic_others,
            R.drawable.ic_savings,
            R.drawable.ic_personal_spending,
            R.drawable.ic_transport,
            R.drawable.ic_utilities
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Poner el layout del fragment en la activity
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Declaración de variables
        button = view.findViewById(R.id.btn_add_transaction);
        recyclerView = view.findViewById(R.id.transaction_rv);
        myViewStub = view.findViewById(R.id.emptyStateLayout);
        dashView = view.findViewById(R.id.dashboard_group);
        totalIncome_txt = view.findViewById(R.id.total_income);
        totalExpense_txt = view.findViewById(R.id.total_expense);
        totalBalance_txt = view.findViewById(R.id.total_balance);
        amount_txt = view.findViewById(R.id.transactionAmount);

        //Button para añadir Transactions
        button.setOnClickListener(v -> {
            addFrag = new addTransactionFragment();
            fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.mainContainer,addFrag).commit();
        });

        //Conexion con base de datos
        database = new DataBaseHelper(getActivity());
        id = new ArrayList<>();
        title = new ArrayList<>();
        amount = new ArrayList<>();
        type = new ArrayList<>();
        tag = new ArrayList<>();
        date = new ArrayList<>();
        note = new ArrayList<>();

        //llamada del método para coger los datos de la base de datos
        storeDataArrays();

        //llamada del adaptador. Los datos aquí son enviados para el Adaptador y después son puestos en el recycler view
        customAdapter = new CustomAdapter(getActivity(), id, title, amount, type, tag, date, note, programImages);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Bucle para coger cada elemento del ArrayList amount y ponerlo en las vistas de Total Income, Expense y Balance
        for (float element : amount) {

            if (element > 0) {
                sumIncome = sumIncome + element;
            } else {
                sumExpense = sumExpense + (-element);
            }
        }
        totalIncome_txt.setText("+ " + Float.toString(sumIncome) + "€");
        totalExpense_txt.setText("- " + Float.toString(sumExpense) + "€");

        sumTotal = sumIncome - sumExpense;

        totalBalance_txt.setText(Float.toString(sumTotal) + "€");

        return view;
    }

    //Métodos Próprios

    // Método para coger los datos de la base de datos
    void storeDataArrays() {
        Cursor cursor = database.readAllData();

        //ViewSub para poner Lottlie image (el GIF con el emoticon)
        myViewStub.inflate();
        if (cursor.getCount() == 0) {
            myViewStub.setVisibility(View.VISIBLE);
            dashView.setVisibility(View.INVISIBLE);
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                title.add(cursor.getString(1));
                amount.add(Float.parseFloat(cursor.getString(2)));
                type.add(cursor.getString(3));
                tag.add(cursor.getString(4));
                date.add(cursor.getString(5));
                note.add(cursor.getString(6));

                //Visibilidad de las vistas
                myViewStub.setVisibility(View.GONE);
                dashView.setVisibility(View.VISIBLE);
            }
        }
    }

    //Habilitar el menú
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    //Inflate menu en la activity
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ui, menu);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");

        super.onCreateOptionsMenu(menu, inflater);
    }

    //Creacion del Listener para las opciones del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_nightMode) {
            if (!isNight) {
                ((AppCompatActivity)getActivity()).getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                isNight = true;
            } else {
                ((AppCompatActivity)getActivity()).getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                isNight = false;
            }
        }

        if (id == R.id.action_about) {
            aboutFrag = new aboutTransactionFragment();
            fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.mainContainer,aboutFrag).commit();
        }

        if (id == R.id.action_exit) {
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }
}