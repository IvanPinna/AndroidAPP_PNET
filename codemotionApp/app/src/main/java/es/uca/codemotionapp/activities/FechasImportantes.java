package es.uca.codemotionapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import java.util.ArrayList;

import es.uca.codemotionapp.items.Date;
import es.uca.codemotionapp.adapters.DateAdapter;
import es.uca.codemotionapp.R;

public class FechasImportantes extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

}
