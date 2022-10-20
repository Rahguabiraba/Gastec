package es.ifp.gastec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import es.ifp.gastec.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    //Declaración de variables
    protected ImageView image;
    private TimerTask myTimerTask;
    private Intent pasarPantalla;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencia de los objetos de la interfaz grafica
        image = (ImageView) findViewById(R.id.image_start);

        //Pasar pantalla automatico en 4s
        myTimerTask = new TimerTask() {
            @Override
            public void run() {
                //Codigo pasar pantalla
                pasarPantalla = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(pasarPantalla);
            }
        };

        timer = new Timer();
        timer.schedule(myTimerTask, 4000);

    }
}