package es.ifp.gastec.view.about;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.ifp.gastec.R;
import es.ifp.gastec.view.dashboard.DashboardFragment;

public class aboutTransactionFragment extends Fragment {

    //Declaración de variables
    protected Button button;
    private Fragment dashFrag;
    private FragmentTransaction fm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate el layout en el fragment
        View view = inflater.inflate(R.layout.fragment_about_transaction, container, false);

        //Referencia de los objetos de la interfaz grafica
        button = view.findViewById (R.id.button_exit_about);

        //Button Volver
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashFrag = new DashboardFragment();
                fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.mainContainer,dashFrag).commit();
            }
        });

        //Cambiar el titulo del actionBar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.text_title_about);

        return view;
    }
}