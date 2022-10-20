package es.ifp.gastec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import es.ifp.gastec.BD.DataBaseHelper;
import es.ifp.gastec.view.edit.editTransactionFragment;

public class DetailsActivity extends AppCompatActivity {

    //Declaración de variables
    protected Button button;
    protected TextView label1, label2, label3, label4, label5, label6;
    protected String id, title,amount, type, tag, date, note;
    private View detailView;

    protected DataBaseHelper dataBase;

    protected String allTexts = "";

    private Intent pasarPantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Referencia de los objetos de la interfaz grafica
        label1 = (TextView)findViewById(R.id.title);
        label2 = (TextView)findViewById(R.id.amount);
        label3 = (TextView)findViewById(R.id.type);
        label4 = (TextView)findViewById(R.id.tag);
        label5 = (TextView)findViewById(R.id.date);
        label6 = (TextView)findViewById(R.id.note);
        button = (Button) findViewById (R.id.details_transaction);
        detailView = (View) findViewById(R.id.detail_view);

        //llamada del método para coger los datos enviados del Adapter y poner en los TextView
        getAndSetIntentData();

        //Button Edit
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment();
                button.setVisibility(View.GONE);
                detailView.setVisibility(View.GONE);
            }
        });
    }

    //Métodos para compartir el text
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SaveImage();
        } else
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private File SaveImage() {
        if (!CheckPermission())
            return null;

        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/Screenshots";
            File fileDir = new File(path);
            if (!fileDir.exists())
                fileDir.mkdir();

            String mPath = path + "/ScreenShot_" + new Date().getTime() + ".png";

            Bitmap bitmap = screenShot();
            File file = new File(mPath);
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            Toast.makeText(this, "Image save succesfully", Toast.LENGTH_SHORT).show();

            return file;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Bitmap screenShot() {
        View v = findViewById(R.id.transactionDetails);
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private boolean CheckPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
            return false;
        }
        return true;
    }

    private void share(File file) {
        Uri uri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this,getPackageName()+".provider",file);

        } else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Screenshot");
        intent.putExtra(Intent.EXTRA_STREAM,uri);

        try {
            startActivity(Intent.createChooser(intent,"Share using"));
        }catch (ActivityNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void getAndSetIntentData() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("amount") && getIntent().hasExtra("type")
                && getIntent().hasExtra("tag") && getIntent().hasExtra("date") && getIntent().hasExtra("note")) {

            //Geting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            amount = getIntent().getStringExtra("amount");
            type = getIntent().getStringExtra("type");
            tag = getIntent().getStringExtra("tag");
            date = getIntent().getStringExtra("date");
            note = getIntent().getStringExtra("note");

            //Setting Intent Data
            label1.setText(title);
            label2.setText(amount);
            label3.setText(type);
            label4.setText(tag);
            label5.setText(date);
            label6.setText(note);

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + "?");
        builder.setMessage("Are you sure you want to delete " + title + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dataBase = new DataBaseHelper(DetailsActivity.this);
                dataBase.deleteOneRow(id);
                pasarPantalla = new Intent(DetailsActivity.this, StartActivity.class);
                startActivity(pasarPantalla);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    public void openFragment() {
        editTransactionFragment editFrag = new editTransactionFragment();
        FragmentTransaction fm = getSupportFragmentManager()
                .beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("title",title);
        bundle.putString("amount",amount);
        bundle.putString("type",type);
        bundle.putString("tag",tag);
        bundle.putString("date",date);
        bundle.putString("note",note);

        editFrag.setArguments(bundle);
        fm.replace(R.id.editContainer, editFrag).commit();
    }

   //Habilitar el menú
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        DetailsActivity.this.getSupportActionBar().setTitle("Details");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share_text) {

            allTexts= title + "\n Amount: " + amount + "," + "\n Type: " + type + "," + "\n Tag: " + tag + "," + "\n Date: " + date + "," + "\n Note: " + note;

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, allTexts);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }

        if (id == R.id.action_share_image) {
            File file = SaveImage();
            if(file!=null)
                share(file);
        }

        if (id == R.id.action_delete) {
            confirmDialog();
        }

        return super.onOptionsItemSelected(item);
    }

}