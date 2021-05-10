package es.uca.codemotionapp.activities;

import android.content.ActivityNotFoundException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;

import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import es.uca.codemotionapp.items.Program;
import es.uca.codemotionapp.adapters.ProgramAdapter;
import es.uca.codemotionapp.R;

public class ProgramActivity extends AppCompatActivity {
    private ImageButton myUndoButton, myRedoButton;
    private FloatingActionButton myPdfButton;

    private static TextView myTextViewDay;
    private static int day = 0; //0->friday, 1 -> saturday, 2 >

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Program> alProgram;

    private Bitmap bitmap;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Attach the buttons and textfield in the header
        myUndoButton = (ImageButton)findViewById(R.id.myUndoButton);
        myTextViewDay = (TextView)findViewById(R.id.myTextViewDay);
        myRedoButton = (ImageButton)findViewById(R.id.myRedoButton);
        myPdfButton = (FloatingActionButton)findViewById(R.id.buttonPdf);

        switch(day){
            case 0:
                myTextViewDay.setText(getText(R.string.titleFriday));
                break;
            case 1:
                myTextViewDay.setText(getText(R.string.titleSaturday));
                break;
            case 2:
                myTextViewDay.setText(getText(R.string.titleSunday));
                break;
        }

        myUndoButton.setOnClickListener(v -> {
            if(day == 0){ //If we are in friday, throw a toast
                Toast myToast = Toast.makeText(ProgramActivity.this, "El viernes es el primer dia", Toast.LENGTH_SHORT);
                myToast.show();
            }else {
                day = day - 1;
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
            }

        });

        myRedoButton.setOnClickListener(v -> {
            if(day == 2){ //If we are in sunday, throw a toast
                Toast myToast = Toast.makeText(ProgramActivity.this, "El domingo es el último día.", Toast.LENGTH_SHORT);
                myToast.show();
            }else {
                day = day + 1;
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view_program);
        mRecyclerView.setHasFixedSize(true); //Better performance.

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        alProgram = new ArrayList<Program>(); //Aquí debes pasarle solamente los datos correspondientes al día que este fijado.
        switch(day){
            case 0:
                alProgram.add(new Program("10.00-11.00", "En busca de la IA", "David Holmes", "Sala 1"));
                alProgram.add(new Program("10.00-11.00", "Computación, nuevos horizontes", "Bill Gates", "Sala 3"));
                alProgram.add(new Program("11.00-12.00", "Buenas prácticas en Android", "Larry Pages", "Sala 2"));
                alProgram.add(new Program("12.00-12.30", "Desayuno", "","Sala 3"));
                alProgram.add(new Program("10.00-11.00", "PS5, Xbox y novedades", "Richard Stallman", "Sala 1"));
                break;
            case 1:
                alProgram.add(new Program("10.00-11.00", "Teoría de la computación", "Javier Rodríguez", "Sala 1"));
                alProgram.add(new Program("10.00-11.00", "Arquitectura Von Neumann", "Alfred Neumann", "Sala 3"));
                alProgram.add(new Program("11.00-12.00", "IOT y la 4º revolución", "Scot Ken", "Sala 1"));
                alProgram.add(new Program("12.00-12.30", "Desayuno", "" ,"Sala 3"));
                alProgram.add(new Program("10.00-11.00", "Videojuegos en 4D", "Regina C. Roberson" ,"Sala 1"));
                break;
            case 2:
                alProgram.add(new Program("10.00-11.00", "Mecánica cuántica", "Justine R. Shehan",  "Sala 1"));
                alProgram.add(new Program("10.00-11.00", "Modelos teóricos de computación", "Gregory S. Thrasher", "Sala 3"));
                alProgram.add(new Program("11.00-12.00", "Informatica aplicada a la medicina", "Brandi F. Fisher", "Sala 1"));
                alProgram.add(new Program("12.00-12.30", "Desayuno", "","Sala 3"));
                alProgram.add(new Program("10.00-11.00", "Despedida y cierre", "Linus Torvalds", "Sala 3"));

                break;

        }
        mAdapter = new ProgramAdapter(alProgram);
        mRecyclerView.setAdapter(mAdapter);

        //Finally, we can generate the pdf.
        myPdfButton.setOnClickListener(v -> {
            bitmap = getScreenshotFromRecyclerView(mRecyclerView);
            //loadBitMapFromView(mRecyclerView, mRecyclerView.getWidth(), mRecyclerView.getHeight());

            createPdf();
        });

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
                Intent intentInicio = new Intent(ProgramActivity.this, MainActivity.class);
                startActivity(intentInicio);
                return true;

            case R.id.assistants_event:
                Intent intentAsistentes = new Intent(ProgramActivity.this, GetAllAssistantsActivity.class);
                startActivity(intentAsistentes);
                return true;

            case R.id.program:
                Intent intentProgram = new Intent(ProgramActivity.this, ProgramActivity.class);
                if(!(context instanceof ProgramActivity)){
                    startActivity(intentProgram);
                }
                return true;

            case R.id.location:
                Intent intentLocation = new Intent(ProgramActivity.this, LocationActivity.class);
                startActivity(intentLocation);
                return true;

            case R.id.fechas_importantes:
                Intent intentDates = new Intent(ProgramActivity.this, FechasImportantesActivity.class);
                startActivity(intentDates);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }



    public static Bitmap loadBitMapFromView(View v, int witdth, int height){
        Bitmap bmpImg = Bitmap.createBitmap(witdth, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpImg);
        v.draw(c);
        return bmpImg;
    }

    //Hey bro, nasty thing. This function generates a bitmap with the full content of the recyclerview. 
    public Bitmap getScreenshotFromRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }

                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }

        }
        return bigBitmap;
    }

    private void createPdf(){
        boolean except = false;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float hight = displayMetrics.heightPixels,
              width = displayMetrics.widthPixels;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder((int)width,

                (int)hight, 2).create();

        PdfDocument.Page page = document.startPage(pageInfo);

        Paint paint = new Paint();
        Canvas canvas = page.getCanvas();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, (int)width, (int)hight, true);

        canvas.drawBitmap(bitmap, 0,0, null);
        document.finishPage(page);
        String targetPdf = "/sdcard/PDFGenerated.pdf";
        File filepath = new File(targetPdf);

        try{
            document.writeTo(new FileOutputStream(filepath));
        }catch (IOException e) {
            except = true;
            Toast.makeText(ProgramActivity.this, "Error en la creacion del pdf. Debes conceder permiso de almacenamiento a la app", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }finally {
            document.close();
        }

        if(!except) {
            Toast.makeText(ProgramActivity.this, "PDF generado correctamente", Toast.LENGTH_LONG).show();
            openPdf();
        }
    }

    private void openPdf(){
        File file = new File("/sdcard/PDFGenerated.pdf");

        if(file.exists()){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(ProgramActivity.this, getPackageName() + ".provider",file); //Uri.fromFile(file);

            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try{
                startActivity(intent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(ProgramActivity.this, "No tiene una app predefinida para pdf",
                        Toast.LENGTH_LONG);
            }
        }
    }
}
