package es.ifp.gastec.custom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.ifp.gastec.DetailsActivity;
import es.ifp.gastec.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    //Declaración de variables
    protected Context context;
    protected ArrayList id, title, amount, type, tag, date, note;
    protected int[] images;
    protected int position;
    private Intent pasarPantalla;

    //Constructor
    public CustomAdapter(Context context, ArrayList id, ArrayList title, ArrayList amount, ArrayList type, ArrayList tag, ArrayList date, ArrayList note , int[] images) {
        this.context = context;
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.tag = tag;
        this.date = date;
        this.note = note;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_transaction_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        this.position = position;

        String typeName = String.valueOf(type.get(position));
        String tagName = String.valueOf(tag.get(position));

        holder.tag_txt.setText(String.valueOf(tag.get(position)));
        holder.title_txt.setText(String.valueOf(title.get(position)));

        //Cambiar la imagen de acuerdo con el type puesto
        if (tagName.equals("Entertainment")) {

            holder.rowImage.setImageResource(images[0]);

        } else if (tagName.equals("Food")) {

            holder.rowImage.setImageResource(images[1]);

        } else if (tagName.equals("Housing")) {

            holder.rowImage.setImageResource(images[2]);

        } else if (tagName.equals("Healthcare")) {

            holder.rowImage.setImageResource(images[3]);

        } else if (tagName.equals("Insurance")) {

            holder.rowImage.setImageResource(images[4]);

        } else if (tagName.equals("Others")) {

            holder.rowImage.setImageResource(images[5]);

        } else if (tagName.equals("Saving & Debts")) {

            holder.rowImage.setImageResource(images[6]);

        } else if (tagName.equals("Personal Spending")) {

            holder.rowImage.setImageResource(images[7]);

        } else if (tagName.equals("Transportation")) {

            holder.rowImage.setImageResource(images[8]);

        } else if (tagName.equals("Utilities")) {

            holder.rowImage.setImageResource(images[9]);

        }

        //Cambiar color del valor añadido
        if(typeName.equals("Income")) {

            holder.amount_txt.setText("+" + String.valueOf(amount.get(position)) + "€");
            holder.amount_txt.setTextColor(context.getResources().getColor(R.color.income));

        } else {
            holder.amount_txt.setText(String.valueOf(amount.get(position)) + "€");
            holder.amount_txt.setTextColor(context.getResources().getColor(R.color.expense));
        }

        //Enviar paquetes del CustomAdapter para Activity
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasarPantalla = new Intent(context, DetailsActivity.class);
                pasarPantalla.putExtra("id", String.valueOf(id.get(position)));
                pasarPantalla.putExtra("title", String.valueOf(title.get(position)));
                pasarPantalla.putExtra("amount", String.valueOf(amount.get(position)));
                pasarPantalla.putExtra("type", String.valueOf(type.get(position)));
                pasarPantalla.putExtra("tag", String.valueOf(tag.get(position)));
                pasarPantalla.putExtra("date", String.valueOf(date.get(position)));
                pasarPantalla.putExtra("note", String.valueOf(note.get(position)));
                context.startActivity(pasarPantalla);
            }
        });
    }

    @Override
    public int getItemCount() {
         return title.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected TextView title_txt, amount_txt, tag_txt;
        protected ImageView rowImage;
        public LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title_txt = itemView.findViewById(R.id.transactionName);
            amount_txt = itemView.findViewById(R.id.transactionAmount);
            tag_txt = itemView.findViewById(R.id.transactionCategory);
            rowImage = itemView.findViewById(R.id.transactionIconView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
