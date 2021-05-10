package es.uca.codemotionapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import es.uca.codemotionapp.items.Assistant;
import es.uca.codemotionapp.adapters.AssistantAdapter;
import es.uca.codemotionapp.R;

public class GetAllAssistantsActivity extends AppCompatActivity {

    private LongRunningGetAllAssistants getAllAssistants;
    private String idAssistant,ipAddress;
    private ProgressBar myProgressBar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistants_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.createAssistant);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCreateAssistant = new Intent(GetAllAssistantsActivity.this, CreateAssistantActivity.class);
                startActivity(intentCreateAssistant);

            }
        });

        // Put your ip here
        ipAddress = "192.168.1.39";

        myProgressBar = (ProgressBar) findViewById(R.id.myProgressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Get all assistants from BD
        getAllAssistants = new LongRunningGetAllAssistants();
        getAllAssistants.execute();

    }

    public void callGetAssistant(Assistant selected){

        Intent intentGetAssistant = new Intent(GetAllAssistantsActivity.this, GetAssistantActivity.class);
        idAssistant = selected.getId();
        Bundle b = new Bundle();
        b.putString("Id",idAssistant);
        intentGetAssistant.putExtras(b);
        startActivity(intentGetAssistant);


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
                Intent intentInicio = new Intent(GetAllAssistantsActivity.this, MainActivity.class);
                startActivity(intentInicio);
                return true;

            case R.id.assistants_event:
                Intent intentAsistentes = new Intent(GetAllAssistantsActivity.this, GetAllAssistantsActivity.class);
                if(!(context instanceof GetAllAssistantsActivity)){
                    startActivity(intentAsistentes);
                }
                return true;
            case R.id.program:
                Intent intentProgram = new Intent(GetAllAssistantsActivity.this, ProgramActivity.class);
                startActivity(intentProgram);
                return true;

            case R.id.location:
                Intent intentLocation = new Intent(GetAllAssistantsActivity.this, LocationActivity.class);
                startActivity(intentLocation);
                return true;

            case R.id.fechas_importantes:
                Intent intentDates = new Intent(GetAllAssistantsActivity.this, FechasImportantesActivity.class);

                startActivity(intentDates);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // END MENU SECTION

    private void largeTask() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class LongRunningGetAllAssistants extends AsyncTask<Void, Integer, String> {
        HttpURLConnection urlConnection = null;
        int _iResponseCode;
        @Override
        protected String doInBackground(Void... params){
            String text = null;

            try{

                URL urlToRequest = new URL("http://" + ipAddress +":3000/assistants/");
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                text = new Scanner(in).useDelimiter("\\A").next();

                // Get code response from GET petition
                _iResponseCode =  urlConnection.getResponseCode();

                // Progress bar time
                for (int i = 1; i <= 5; i++) {
                    largeTask();
                    publishProgress(i );
                    if (isCancelled())
                        break;
                }


            } catch (Exception e) {
                System.out.println("EXCEPTION TRYING TO CONNECT ALL ASSISTANTS");
                e.printStackTrace();
                text = e.toString();

            }

            finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return text;
        }

        // Functions for Progress bar
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0].intValue();
            myProgressBar.setProgress(progress);
        }

        @Override
        protected void onPreExecute() {
            myProgressBar.setMax(5);
            myProgressBar.setProgress(0);
        }


        private  ArrayList<Assistant> convertJSONtoAssistants(JSONArray assistantsJSON){
            JSONObject respJSON = null;
            ArrayList<Assistant> assistants = new ArrayList<Assistant>();

            for(int i = 0; i < assistantsJSON.length(); i++){
                try{
                    respJSON = assistantsJSON.getJSONObject(i);
                    String dni = respJSON.getString("dni");
                    String id = respJSON.getString("_id");
                    String name = respJSON.getString("name");
                    String surname = respJSON.getString("surname");
                    int age = Integer.parseInt(respJSON.getString("age"));
                    String email = respJSON.getString("email");
                    int phone = Integer.parseInt(respJSON.getString("phone"));
                    String birthString = respJSON.getString("birthDate");
                    String startString = respJSON.getString("startDate");
                    String endString = respJSON.getString("endDate");
                    String inscriptionString = respJSON.getString("inscriptionDate");
                    System.out.println(inscriptionString);
                    System.out.println(endString);
                    Date birth = new SimpleDateFormat("yyyy/MM/dd").parse(birthString);
                    Date start =  new SimpleDateFormat("yyyy/MM/dd").parse(startString);
                    Date end =  new SimpleDateFormat("yyyy/MM/dd").parse(endString);
                    Date inscription = new SimpleDateFormat("yyyy/MM/dd").parse(inscriptionString);
                    Assistant aux = new Assistant(id,dni,name,surname,email,phone,age,birth,start,end,inscription);
                    assistants.add(aux);
                    //System.out.println(respJSON);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            return assistants;
        }

        @Override
        protected void onPostExecute(String results) {
            Toast notification = null;
            notification = Toast.makeText(getApplicationContext(), results, Toast.LENGTH_LONG);

            if (results!=null && _iResponseCode == 200) {
                JSONArray array;
                ArrayList<Assistant> assistants;
                try{
                    array = new JSONArray(results);
                    assistants = convertJSONtoAssistants(array);
                    mAdapter = new AssistantAdapter(assistants, mRecyclerView.getContext());
                    mRecyclerView.setAdapter(mAdapter);

                }catch(Exception e){
                    System.out.println("EXCEPTION CREATING ARRAY OF ASSISTANTS!");
                    notification = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
                    notification.show();
                    e.printStackTrace();
                }

            }
            else{
                notification.show();
            }

        }

    } // End class LongRunningGetAll

}
