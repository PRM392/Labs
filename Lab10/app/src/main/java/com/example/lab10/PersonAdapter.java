package com.example.lab10;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {
    private Context context;
    private List<Person> personList;

    public PersonAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.name.setText(person.getFirstName() + " " + person.getLastName());
    }

    @Override
    public int getItemCount() {
        if (personList == null) {
            return 0;
        }
        return personList.size();
    }

    public void setTasks(List<Person> personList) {
        this.personList = personList;
        notifyDataSetChanged();
    }

    public List<Person> getTasks() {
        return personList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView editImage;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.person_name);
            editImage = itemView.findViewById(R.id.edit_image);
            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int elementId = personList.get(getAdapterPosition()).getUid();
                    Intent i = new Intent(context, EditPersonActivity.class);
                    i.putExtra(Constants.UPDATE_Person_Id, elementId);
                    context.startActivity(i);
                }
            });
        }
    }
}
