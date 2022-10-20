package es.ifp.gastec.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import es.ifp.gastec.BD.DataBaseSQL;
import es.ifp.gastec.R;
import es.ifp.gastec.StartActivity;

public class LoginActivity extends AppCompatActivity {

    //Declaración de variables
    protected ImageView image;
    protected EditText caja1;
    protected EditText caja2;
    protected Button button;
    protected Button button2;

    private Intent pasarPantalla;

    protected String user="";
    protected String pass="";
    protected Boolean checkuserpass;

    protected DataBaseSQL db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Referencia de los objetos de la interfaz grafica
        image = (ImageView) findViewById(R.id.image_login);
        caja1 = (EditText) findViewById(R.id.caja1_login);
        caja2 = (EditText) findViewById(R.id.caja2_login);
        button = (Button) findViewById(R.id.button_login);
        button2 = (Button) findViewById(R.id.button2_login);

        //Conexion con base de datos
        db = new DataBaseSQL(this);

        //Evento del button Login
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = caja1.getText().toString();
                pass = caja2.getText().toString();

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(LoginActivity.this, R.string.toast1_create_login, Toast.LENGTH_SHORT).show();
                } else {
                    checkuserpass = db.checkusernamepassword(user, pass);
                    if(checkuserpass==true) {
                        Toast.makeText(LoginActivity.this, R.string.toast6_create_login, Toast.LENGTH_SHORT).show();
                        pasarPantalla = new Intent(LoginActivity.this, StartActivity.class);
                        finish();
                        startActivity(pasarPantalla);
                        db.close();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.toast7_create_login, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        //Button Create Login
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasarPantalla = new Intent(LoginActivity.this, CreateLoginActivity.class);
                finish();
                startActivity(pasarPantalla);
            }
        });


    }
}