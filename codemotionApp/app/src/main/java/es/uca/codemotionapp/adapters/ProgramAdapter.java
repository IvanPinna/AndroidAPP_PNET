package es.uca.codemotionapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import es.uca.codemotionapp.items.Program;
import es.uca.codemotionapp.R;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.MyViewHolder> {
    private ArrayList<Program> _alProgram;


    public static class MyViewHolder extends RecyclerView.ViewHolder{ //Here creates the elements that appear in program.xml
        private TextView programHour, programTitle, programUbication, programSpeaker;

        public MyViewHolder(View v){
            super(v);

            programHour = (TextView)v.findViewById(R.id.programHour);
            programSpeaker = (TextView)v.findViewById(R.id.programSpeaker);
            programTitle = (TextView)v.findViewById(R.id.programTitle);
            programUbication = (TextView)v.findViewById(R.id.programUbication);

        }
    }

    public ProgramAdapter(ArrayList<Program> myDataset){ //You must difference between friday, saturday and sunday.
        _alProgram = myDataset;
    }

    @Override
    public ProgramAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position){
        holder.programHour.setText(_alProgram.get(position).getHour());
        holder.programTitle.setText(_alProgram.get(position).getTitle());
        holder.programSpeaker.setText(_alProgram.get(position).getSpeaker());
        holder.programUbication.setText(_alProgram.get(position).getLocation());
    }

    @Override
    public int getItemCount() {return _alProgram.size();}
}
