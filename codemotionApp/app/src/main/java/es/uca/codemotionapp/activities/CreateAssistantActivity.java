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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import es.uca.codemotionapp.R;

public class CreateAssistantActivity extends AppCompatActivity {

    private EditText dni,name,surname,age,email,phone,birthDate,startDate,endDate;
    private Button btnCreate;
    private LongRunningPost myInvokeTask;
    private Calendar birthCalendar = Calendar.getInstance();
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private String ipAddress;
    private LocalDate today = LocalDate.now();
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private String currentDateString;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assistant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get current date
        currentDateString = (today).format(format);

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
        btnCreate = (Button) findViewById(R.id.buttonCreate);

        // Put your ip here
        ipAddress = "192.168.1.39";

        // Set DataPickers to the input forms
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateAssistantActivity.this, dateDialogBirth, birthCalendar
                        .get(Calendar.YEAR), birthCalendar.get(Calendar.MONTH),
                        birthCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateAssistantActivity.this, dateDialogStart, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateAssistantActivity.this, dateDialogEnd, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });



        // What happen when you click on create button?
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs()){
                     //Snackbar.make(v, "Formulario válido", Snackbar.LENGTH_LONG).show();

                     // Insert the new assistant on database
                     myInvokeTask = new LongRunningPost();
                     myInvokeTask.execute(
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
                Intent intentInicio = new Intent(CreateAssistantActivity.this, MainActivity.class);
                startActivity(intentInicio);
                return true;

            case R.id.assistants_event:
                Intent intentAsistentes = new Intent(CreateAssistantActivity.this, GetAllAssistantsActivity.class);
                startActivity(intentAsistentes);
                return true;

            case R.id.program:
                Intent intentProgram = new Intent(CreateAssistantActivity.this, ProgramActivity.class);
                startActivity(intentProgram);
                return true;

            case R.id.location:
                Intent intentLocation = new Intent(CreateAssistantActivity.this, LocationActivity.class);
                startActivity(intentLocation);
                return true;

            case R.id.fechas_importantes:

                Intent intentDates = new Intent(CreateAssistantActivity.this, FechasImportantesActivity.class);

                startActivity(intentDates);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // END MENU SECTION

    private void updateDateInput(EditText date, Calendar myCalendar) {
        String dateFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat transformDate = new SimpleDateFormat(dateFormat, Locale.US);
        date.setText(transformDate.format(myCalendar.getTime()));
    }



    private boolean validateInputs(){
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

    private class LongRunningPost extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection = null;
        int _iResponseCode;

        @Override
        protected String doInBackground(String... params){
            String sText = null;
            try{

                URL urlToRequest = new URL("http://" + ipAddress +":3000/assistants/");

                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //Put params from sending with POST
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
                jsonParam.put("inscriptionDate",currentDateString);

                // Sending...
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(jsonParam.toString());
                wr.flush();

                // Get code response from petition
                _iResponseCode =  urlConnection.getResponseCode();

            } catch (Exception e) {
                System.out.println("EXCEPTION TRYING TO CREATE AN ASSISTANT!");
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
            Toast notification = null;

            Intent intentBack = new Intent(CreateAssistantActivity.this, GetAllAssistantsActivity.class);
            // If you are wondering how to check if the user you had just created is in the DB, just
            // put this on your browser "http://localhost:3000/assistants/"

            if(_iResponseCode == 201){
                notification = Toast.makeText(getApplicationContext(), "¡Asistente creado!", Toast.LENGTH_SHORT);
                startActivity(intentBack);
            }

            else
                notification = Toast.makeText(getApplicationContext(), results, Toast.LENGTH_LONG);

            notification.show();
        }

    }


}
