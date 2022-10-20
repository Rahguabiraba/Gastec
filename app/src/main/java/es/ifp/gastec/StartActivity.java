package es.ifp.gastec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import es.ifp.gastec.view.dashboard.DashboardFragment;

public class StartActivity extends AppCompatActivity {

    //Declaración de variables
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Cambio del titulo en el actionBar
        StartActivity.this.getSupportActionBar().setTitle("");

        //llamada para los fragmentos del app
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, new DashboardFragment()).commit();
    }
}