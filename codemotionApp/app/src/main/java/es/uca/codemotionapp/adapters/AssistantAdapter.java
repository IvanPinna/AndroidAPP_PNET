package es.uca.codemotionapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.uca.codemotionapp.items.Assistant;
import es.uca.codemotionapp.R;
import es.uca.codemotionapp.activities.GetAllAssistantsActivity;

public class AssistantAdapter extends RecyclerView.Adapter<AssistantAdapter.MyViewHolder> {
    private ArrayList<Assistant> assistantsList;
    private Context context;


    public AssistantAdapter(ArrayList<Assistant> myDataset,Context myContext) {
        assistantsList = myDataset;
        context = myContext;
    }

    @Override
    public AssistantAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =    LayoutInflater.from(parent.getContext()).inflate(R.layout.assistant_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        context = parent.getContext();
        return vh;
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        // Put assistants on item.xml
        holder.name.setText(assistantsList.get(position).getName());
        holder.dni.setText(assistantsList.get(position).getDni());


        // Show info of assistant
        holder.showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call get assistant
                if(context instanceof GetAllAssistantsActivity){
                   ((GetAllAssistantsActivity)context).callGetAssistant(assistantsList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return assistantsList.size();
    }


    // Asociate elements from GetAllAssistant to variables
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dni,name,surname,age;
        Button showButton;

        public MyViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            dni = (TextView) v.findViewById(R.id.dni);
            showButton = (Button) v.findViewById(R.id.show);
        }
    }
}
