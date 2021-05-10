package es.uca.codemotionapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import es.uca.codemotionapp.R;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        context = this;

        switch(item.getItemId()){
            case R.id.init:
                Intent intentInicio = new Intent(MainActivity.this, MainActivity.class);
                if(!(context instanceof MainActivity)){
                    startActivity(intentInicio);
                }
                return true;

            case R.id.assistants_event:
                Intent intentAsistentes = new Intent(MainActivity.this, GetAllAssistantsActivity.class);
                startActivity(intentAsistentes);
                return true;

            case R.id.program:
                Intent intentProgram = new Intent(MainActivity.this, ProgramActivity.class);
                startActivity(intentProgram);
                return true;

            case R.id.location:
                Intent intentLocation = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intentLocation);
                return true;

            case R.id.fechas_importantes:
                Intent intentDates = new Intent(MainActivity.this, FechasImportantesActivity.class);

                startActivity(intentDates);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
