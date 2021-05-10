package es.uca.codemotionapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import es.uca.codemotionapp.R;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Button inscriptionButton;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Asociate variables
        inscriptionButton = (Button) findViewById(R.id.buttonInscription);

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCreate = new Intent(LocationActivity.this, CreateAssistantActivity.class);
                startActivity(intentCreate);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // MENU SECTION

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
                Intent intentInicio = new Intent(LocationActivity.this, MainActivity.class);
                startActivity(intentInicio);
                return true;

            case R.id.assistants_event:
                Intent intentAsistentes = new Intent(LocationActivity.this, GetAllAssistantsActivity.class);
                startActivity(intentAsistentes);
                return true;

            case R.id.program:
                Intent intentProgram = new Intent(LocationActivity.this, ProgramActivity.class);
                startActivity(intentProgram);
                return true;

            case R.id.location:
                Intent intentLocation = new Intent(LocationActivity.this, LocationActivity.class);
                if(!(context instanceof LocationActivity)){
                    startActivity(intentLocation);
                }
                return true;

            case R.id.fechas_importantes:
                Intent intentDates = new Intent(LocationActivity.this, FechasImportantesActivity.class);

                startActivity(intentDates);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // END MENU SECTION

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng codemotion = new LatLng(40.394271, -3.796601);
        mMap.addMarker(new MarkerOptions().position(codemotion).title("Evento Codemotion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(codemotion));

        // Automatic zoom in
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.394271, -3.796601),16.0f));

        // Set a listener on marker click
        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer clickCount = (Integer) marker.getTag();
        if(clickCount != null){
            clickCount += 1;
            marker.setTag(clickCount);
            Toast.makeText(this,marker.getTitle()+ "lo has pulsado",Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}
