package com.example.campusconnect;

import static android.content.ContentValues.TAG;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class GenerateDTR extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    Read read = new Read();

    private static final int PERMISSION_REQUEST_CODE = 10;
    //private static final String TAG
    int pdfHeight = 1080;
    int pdfWidth = 720;
    private PdfDocument document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_dtr);

        // Find button in the Layout
        Button generate = findViewById(R.id.generateDTR_Button);

        //Find the spinner in the layout
        Spinner monthSpinner = findViewById(R.id.month_Spinner);
        Spinner yearSpinner = findViewById(R.id.year_Spinner);

        // Create an ArrayList for the month
        ArrayList<String> monthList = new ArrayList<>();
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        // Create an ArrayAdapter for the month spinner
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // Create an ArrayList for the year
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = Calendar.getInstance().get(Calendar.YEAR)-5; i <= Calendar.getInstance().get(Calendar.YEAR) + 10; i++) {
            yearList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the year spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // TODO Generate DTR
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String month = monthSpinner.getSelectedItem().toString();
                String year = yearSpinner.getSelectedItem().toString();
                int day = DateUtils.getNumberOfDays(year, month);

                final Dialog dtrdialog = new Dialog(GenerateDTR.this);
                dtrdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dtrdialog.setContentView(R.layout.activity_dtr_layout);
                dtrdialog.setCancelable(true);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dtrdialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dtrdialog.getWindow().setAttributes(lp);
                Button downloaddtr = dtrdialog.findViewById(R.id.downloadDTR_Button);

                dtrdialog.show();

                downloaddtr.setOnClickListener(v1 ->{
                    generatePdfView(dtrdialog.findViewById(R.id.scrollView));
                });

                // TODO do not delete this you will be able to use this code later on
                /*for(int i = 1 ; i <= day ; i++) {
                    int j = i;
                    read.readRecord("employee/" + save.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + i, new Read.OnGetDataListener() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            try {
                                String timeAM_In = dataSnapshot.child("timeAM_In").getValue(String.class);
                                String timeAM_Out = dataSnapshot.child("timeAM_Out").getValue(String.class);
                                String timePM_In = dataSnapshot.child("timePM_In").getValue(String.class);
                                String timePM_Out = dataSnapshot.child("timePM_Out").getValue(String.class);

                                // Display in Log
                                Log.d(TAG, String.valueOf(j));
                                Log.d(TAG, "timeAM_In: " + timeAM_In);
                                Log.d(TAG, "timeAM_Out: " + timeAM_Out);
                                Log.d(TAG, "timePM_In: " + timePM_In);
                                Log.d(TAG, "timePM_Out: " + timePM_Out);
                            } catch (NullPointerException e) {
                                Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                 */
            }

            private void generatePdfView(View view) {
                Bitmap bitmap = getBitmapView(view);
                document = new PdfDocument();
                PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
                PdfDocument.Page myPage = document.startPage(mypageInfo);
                Canvas canvas = myPage.getCanvas();
                canvas.drawBitmap(bitmap,0,0,null);
                document.finishPage(myPage);
                createFile();
            }

            private void createFile() {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_TITLE, "dtr.pdf");
                startActivityForResult(intent, CREATE_FILE);
            }

            private Bitmap getBitmapView(View view) {
                Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(returnedBitmap);
                Drawable bgDrawable = view.getBackground();
                if(bgDrawable != null){
                    bgDrawable.draw(canvas);
                }
                else{
                    canvas.drawColor(Color.WHITE);
                }
                view.draw(canvas);
                return returnedBitmap;
            }
        });
    }
}