package es.ifp.gastec.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.ifp.gastec.BD.DataBaseSQL;
import es.ifp.gastec.R;

public class CreateLoginActivity extends AppCompatActivity {

    //Declaración de variables
    protected EditText caja1;
    protected EditText caja2;
    protected EditText caja3;
    protected Button button;
    protected Button button2;

    protected String user="";
    protected String pass="";
    protected String repass="";
    protected Boolean checkuser;
    protected Boolean insert;

    private Intent pasarPantalla;

    protected DataBaseSQL db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login);

        //Referencia de los objetos de la interfaz grafica
        caja1 = (EditText) findViewById(R.id.caja1_create_login);
        caja2 = (EditText) findViewById(R.id.caja2_create_login);
        caja3 = (EditText) findViewById(R.id.caja3_create_login);
        button = (Button) findViewById (R.id.button_create_login);
        button2 = (Button) findViewById (R.id.button2_create_login);

        //Conexion con base de datos
        db = new DataBaseSQL(this);

        //Button Create Login
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = caja1.getText().toString();
                pass = caja2.getText().toString();
                repass = caja3.getText().toString();

                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass)) {
                    Toast.makeText(CreateLoginActivity.this, R.string.toast1_create_login, Toast.LENGTH_SHORT).show();
                } else {
                    if(pass.equals(repass)) {
                        checkuser = db.checkusername(user);
                        if (checkuser==false) {
                            insert = db.insertData(user, pass);
                            if(insert==true) {
                                Toast.makeText(CreateLoginActivity.this, R.string.toast2_create_login, Toast.LENGTH_SHORT).show();
                                pasarPantalla = new Intent(CreateLoginActivity.this, LoginActivity.class);
                                finish();
                                startActivity(pasarPantalla);
                                db.close();
                            } else {
                                Toast.makeText(CreateLoginActivity.this, R.string.toast3_create_login, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CreateLoginActivity.this, R.string.toast4_create_login, Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(CreateLoginActivity.this, R.string.toast5_create_login, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Button Back
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasarPantalla = new Intent(CreateLoginActivity.this, LoginActivity.class);
                finish();
                startActivity(pasarPantalla);
            }
        });
    }
}