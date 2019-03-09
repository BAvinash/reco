package com.cratisspace.avinashbadramoni.reco;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView  txt;
    Button btnsnap,btnreco;
    ImageView img;


    String example = "ABC";
    private  FirebaseVisionText firebaseVisionText;

    private Bitmap imageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = (TextView)findViewById(R.id.result);

        btnreco = (Button)findViewById(R.id.reco);
        btnsnap = (Button)findViewById(R.id.snapbtn);
        img = (ImageView)findViewById(R.id.snap);

        btnsnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });

        btnreco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    detectTxt();



            }
        });


    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
           // Toast.makeText(this, "camera called", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
          // Toast.makeText(this, " Second camera called", Toast.LENGTH_SHORT).show();



        }
    }

    private void detectTxt() {


        FirebaseVisionImage image =  FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {

                processTxt(firebaseVisionText);
                //Toast.makeText(MainActivity.this, "second camera called", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void processTxt(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.Block> blocks = firebaseVisionText.getBlocks();
        if (blocks.size()==0){

            Toast.makeText(this, "No Text", Toast.LENGTH_SHORT).show();
            return;
        }

        for (FirebaseVisionText.Block block :firebaseVisionText.getBlocks()){
            String result = block.getText();

            //String finalresult = result.toString();

            if (result.equals(example)){
                Toast.makeText(this, "0k" + example, Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, "Not Ok" + example, Toast.LENGTH_SHORT).show();
            }

           txt.setText(result);

        }
    }


}
