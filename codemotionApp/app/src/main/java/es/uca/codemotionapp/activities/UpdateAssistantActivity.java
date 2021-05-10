package es.uca.codemotionapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.uca.codemotionapp.R;

public class UpdateAssistantActivity extends AppCompatActivity {

    private EditText dni,name,surname,age,email,phone,birthDate,startDate,endDate;
    private Button btnUpdate;
    private String id,ipAddress;
    private LongRunningUpdate updateAssistant;
    private Context context;
    private Calendar birthCalendar = Calendar.getInstance();
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_assistant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cross Variables with Layout
        dni = (EditText) findViewById(R.id.inputDNI);
        name = (EditText) findViewById(R.id.inputName);
        surname = (EditText) findViewById(R.id.inputSurname);
        age = (EditText) findViewById(R.id.inputAge);
        email = (EditText) findViewById(R.id.inputEmail);
        phone = (EditText) findViewById(R.id.inputPhone);
        birthDate = (EditText) findViewById(R.id.inputBirthDate);
        startDate = (EditText) findViewById(R.id.inputStartDate);
        endDate = (EditText) findViewById(R.id.inputEndDate);
        btnUpdate = (Button)findViewById(R.id.buttonUpdate);

        // Put your ip address here
        ipAddress = "192.168.1.39";

        // Fill Data with the bundle
        final Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;

        dni.setText(bundle.getString("DNI"));
        name.setText(bundle.getString("Name"));
        surname.setText(bundle.getString("Surname"));
        age.setText(String.valueOf(bundle.getInt("Age")));
        email.setText(bundle.getString("Email"));
        phone.setText(String.valueOf(bundle.getInt("Phone")));
        birthDate.setText(bundle.getString("Birth"));
        startDate.setText(bundle.getString("Start"));
        endDate.setText(bundle.getString("End"));

        // Data pickers. Its possible that an user modifies his dates
        // Set DataPickers to the input forms
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateAssistantActivity.this, dateDialogBirth, birthCalendar
                        .get(Calendar.YEAR), birthCalendar.get(Calendar.MONTH),
                        birthCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateAssistantActivity.this, dateDialogStart, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateAssistantActivity.this, dateDialogEnd, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs()){
                    //Snackbar.make(v, "Formulario listo para actualizar", Snackbar.LENGTH_LONG).show();
                    id = bundle.getString("Id");
                    updateAssistant = new LongRunningUpdate();
                    updateAssistant.execute(
                            dni.getText().toString(),
                            name.getText().toString(),
                            surname.getText().toString(),
                            age.getText().toString(),
                            email.getText().toString(),
                            phone.getText().toString(),
                            birthDate.getText().toString(),
                            startDate.getText().toString(),
                            endDate.getText().toString()
                    );

                }
            }
        });
    }

    // Get information from DataPickers
    // Get data from DataPickers
    DatePickerDialog.OnDateSetListener dateDialogBirth = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            birthCalendar.set(Calendar.YEAR, year);
            birthCalendar.set(Calendar.MONTH, monthOfYear);
            birthCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateInput(birthDate,birthCalendar);
        }
    };

    DatePickerDialog.OnDateSetListener dateDialogStart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH, monthOfYear);
            startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateInput(startDate,startCalendar);
        }
    };


    DatePickerDialog.OnDateSetListener dateDialogEnd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH, monthOfYear);
            endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateInput(endDate,endCalendar);
        }
    };

    private void updateDateInput(EditText date, Calendar myCalendar) {
        String dateFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat transformDate = new SimpleDateFormat(dateFormat, Locale.US);
        date.setText(transformDate.format(myCalendar.getTime()));
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
                Intent intentInicio = new Intent(UpdateAssistantActivity.this, MainActivity.class);
                startActivity(intentInicio);
                return true;

            case R.id.assistants_event:
                Intent intentAsistentes = new Intent(UpdateAssistantActivity.this, GetAllAssistantsActivity.class);
                startActivity(intentAsistentes);
                return true;

            case R.id.program:
                Intent intentProgram = new Intent(UpdateAssistantActivity.this, ProgramActivity.class);
                startActivity(intentProgram);
                return true;

            case R.id.location:
                Intent intentLocation = new Intent(UpdateAssistantActivity.this, LocationActivity.class);
                startActivity(intentLocation);
                return true;

            case R.id.fechas_importantes:
                Intent intentDates = new Intent(UpdateAssistantActivity.this, FechasImportantesActivity.class);

                startActivity(intentDates);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // END MENU SECTION

    boolean validateInputs(){
        if(dni.getText().toString().matches("") ||
                name.getText().toString().matches("") ||
                surname.getText().toString().matches("") ||
                age.getText().toString().matches("") ||
                email.getText().toString().matches("") ||
                phone.getText().toString().matches("") ||
                birthDate.getText().toString().matches("") ||
                startDate.getText().toString().matches("") ||
                endDate.getText().toString().matches(""))
        {
            Toast.makeText(this,"Introduce todos los campos",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{return true;}
    }

    private class LongRunningUpdate extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection = null;
        int _iResponseCode;

        @Override
        protected String doInBackground(String... params){
            String sText = null;
            try{
                URL urlToRequest = new URL("http://" + ipAddress + ":3000/assistants/"+id);
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                // Params to update
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("dni",params[0]);
                jsonParam.put("name",params[1]);
                jsonParam.put("surname",params[2]);
                jsonParam.put("age",params[3]);
                jsonParam.put("email", params[4]);
                jsonParam.put("phone", params[5]);
                jsonParam.put("birthDate",params[6]);
                jsonParam.put("startDate",params[7]);
                jsonParam.put("endDate",params[8]);

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(jsonParam.toString());
                wr.flush();
                _iResponseCode =  urlConnection.getResponseCode();

            } catch (Exception e) {
                System.out.println("EXCEPTION TRYING TO UPDATE AN ASSISTANT");
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
            Intent intentUpdateAssistant = new Intent(UpdateAssistantActivity.this, GetAllAssistantsActivity.class);

            if(_iResponseCode == 200){
                notification = Toast.makeText(getApplicationContext(), "¡Asistente actualizado!", Toast.LENGTH_SHORT);
                startActivity(intentUpdateAssistant);
            }

            else
                notification = Toast.makeText(getApplicationContext(), results, Toast.LENGTH_SHORT);

            notification.show();
        }

    }
}
