package es.uca.codemotionapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import es.uca.codemotionapp.items.Date;
import es.uca.codemotionapp.adapters.DateAdapter;
import es.uca.codemotionapp.R;

public class FechasImportantesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fechas_importantes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true); //Better perfomance

        //Created to manage date.xml
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager); //linked with the recycledView..

        ArrayList<Date> alDates = new ArrayList<Date>();
        mAdapter = new DateAdapter(alDates, (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){ //This method inflates the menu.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        context = this;

        switch(item.getItemId()){
            case R.id.init:
                Intent intentInicio = new Intent(FechasImportantesActivity.this, MainActivity.class);
                startActivity(intentInicio);
                return true;
            case R.id.assistants_event:
                Intent intentAsistentes = new Intent(FechasImportantesActivity.this, GetAllAssistantsActivity.class);
                startActivity(intentAsistentes);
                return true;
            case R.id.program:
                Intent intentProgram = new Intent(FechasImportantesActivity.this, ProgramActivity.class);
                startActivity(intentProgram);
                return true;

            case R.id.location:
                Intent intentLocation = new Intent(FechasImportantesActivity.this, LocationActivity.class);
                if(!(context instanceof LocationActivity)){
                    startActivity(intentLocation);
                }
                return true;

            case R.id.fechas_importantes:
                Intent intentDates = new Intent(FechasImportantesActivity.this, FechasImportantesActivity.class);
                if(!(context instanceof FechasImportantesActivity)) {
                    startActivity(intentDates);
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
