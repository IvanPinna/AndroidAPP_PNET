package es.uca.codemotionapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import es.uca.codemotionapp.R;

public class GetAssistantActivity extends AppCompatActivity {

    private TextView dni,name,surname,age,email,phone,birthDate,startDate,endDate;
    private Button btnDelete,btnUpdate;
    private String id,ipAddress;
    private LongRunningDelete deleteAssistant;
    private Context context;
    private LongRunningGetAssistant getAssistant;
    private Bundle bundleUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_assistant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Asociate variables with layout
        dni = (TextView)findViewById(R.id.inputDNI);
        name = (TextView)findViewById(R.id.inputName);
        surname = (TextView)findViewById(R.id.inputSurname);
        age = (TextView)findViewById(R.id.inputAge);
        email = (TextView)findViewById(R.id.inputEmail);
        phone = (TextView)findViewById(R.id.inputPhone);
        birthDate = (TextView)findViewById(R.id.inputBirthDate);
        startDate = (TextView)findViewById(R.id.inputStartDate);
        endDate = (TextView)findViewById(R.id.inputEndDate);
        btnUpdate = (Button)findViewById(R.id.buttonUpdate);
        btnDelete = (Button)findViewById(R.id.buttonDelete);

        // Put your ip here
        ipAddress = "192.168.1.39";


        // Recover id for current assistant
        final Bundle bundleGet = this.getIntent().getExtras();
        assert bundleGet != null;
        id = bundleGet.getString("Id");

        // Recover Information of current assistant
        getAssistant = new LongRunningGetAssistant();
        getAssistant.execute();



       btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdateAssistant = new Intent (GetAssistantActivity.this, UpdateAssistantActivity.class);
                intentUpdateAssistant.putExtras(bundleUpdate);
                startActivity(intentUpdateAssistant);
            }
        });

       btnDelete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               id = bundleGet.getString("Id");
               deleteAssistant = new LongRunningDelete();
               deleteAssistant.execute();

           }
       });

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
                Intent intentInicio = new Intent(GetAssistantActivity.this, MainActivity.class);
                if(!(context instanceof MainActivity)){
                    startActivity(intentInicio);
                }

                return true;
            case R.id.assistants_event:
                Intent intentAsistentes = new Intent(GetAssistantActivity.this, GetAllAssistantsActivity.class);
                if(!(context instanceof GetAllAssistantsActivity)){
                    startActivity(intentAsistentes);
                }
                return true;
            case R.id.program:
                Intent intentProgram = new Intent(GetAssistantActivity.this, ProgramActivity.class);
                if(!(context instanceof ProgramActivity)){
                    startActivity(intentProgram);
                }
                return true;

            case R.id.location:
                Intent intentLocation = new Intent(GetAssistantActivity.this, LocationActivity.class);
                if(!(context instanceof LocationActivity)){
                    startActivity(intentLocation);
                }
                return true;

            case R.id.fechas_importantes:
                Intent intentDates = new Intent(GetAssistantActivity.this, FechasImportantesActivity.class);
                if(!(context instanceof FechasImportantesActivity)){

                    startActivity(intentDates);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // END MENU SECTION

    private class LongRunningDelete extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection = null;
        int _iResponseCode;

        @Override
        protected String doInBackground(String... params){
            String sText = null;
            try{
                URL urlToRequest = new URL("http://" + ipAddress + ":3000/assistants/" + id);
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                _iResponseCode =  urlConnection.getResponseCode();

            } catch (Exception e) {
                System.out.println("EXCEPTION WHEN DELECTION AN ASSISTANT");
                sText = e.toString();
                e.printStackTrace();
            }finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }

            return sText;
        }

        @Override
        protected void onPostExecute(String results) {
            Toast notification;
            Intent intentDeleteAssistant = new Intent(GetAssistantActivity.this, GetAllAssistantsActivity.class);

            if(_iResponseCode == 200)
            {
                notification = Toast.makeText(getApplicationContext(), "¡Asistente eliminado!", Toast.LENGTH_SHORT);
                startActivity(intentDeleteAssistant);
            }
            else
                notification = Toast.makeText(getApplicationContext(), results, Toast.LENGTH_SHORT);

            notification.show();
        }

    }

     private class LongRunningGetAssistant extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        int _iResponseCode;

        @Override
        protected String doInBackground(Void... params){
            String text = null;

            try{

                URL urlToRequest = new URL("http://" + ipAddress +":3000/assistants/"+ id);
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                text = new Scanner(in).useDelimiter("\\A").next();
                _iResponseCode = urlConnection.getResponseCode();


            } catch (Exception e) {
                System.out.println("EXCEPTION TRYING TO CONNECT GET ASSISTANT");
                text = e.toString();
                e.printStackTrace();

            }

            finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return text;
        }

        @Override
        protected void onPostExecute(String results) {
            Toast notification = null;
            notification = Toast.makeText(getApplicationContext(), results, Toast.LENGTH_LONG);
            //Intent intentGetAssistant = new Intent(GetAllAssistantsActivity.this, GetAssistantActivity.class);

            if (results != null && _iResponseCode == 200) {
                JSONObject respJSON = null;
                JSONArray array = null;
                try {
                    array = new JSONArray(results);
                    respJSON = array.getJSONObject(0);

                    // Get information of assistant
                    dni.setText(respJSON.getString("dni"));
                    name.setText(respJSON.getString("name"));
                    surname.setText(respJSON.getString("surname"));
                    age.setText(respJSON.getString("age"));
                    email.setText(respJSON.getString("email"));
                    phone.setText(respJSON.getString("phone"));
                    birthDate.setText(respJSON.getString("birthDate"));
                    startDate.setText(respJSON.getString("startDate"));
                    endDate.setText(respJSON.getString("endDate"));

                    // Create bundle with  new Information for update assistant
                    bundleUpdate = new Bundle();
                    bundleUpdate.putString("Id",id);
                    bundleUpdate.putString("DNI",dni.getText().toString());
                    bundleUpdate.putString("Name",name.getText().toString());
                    bundleUpdate.putString("Surname",surname.getText().toString());
                    bundleUpdate.putInt("Age",Integer.parseInt(age.getText().toString()));
                    bundleUpdate.putString("Email",email.getText().toString());
                    bundleUpdate.putInt("Phone",Integer.parseInt(phone.getText().toString()));
                    bundleUpdate.putString("Birth",birthDate.getText().toString());
                    bundleUpdate.putString("Start",startDate.getText().toString());
                    bundleUpdate.putString("End",endDate.getText().toString());

                } catch (Exception e) {
                    notification = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
                    notification.show();
                    e.printStackTrace();
                }
            }
            else{
               notification.show();
            }
        }

    } // End class LongRunningGetAssistant

}
