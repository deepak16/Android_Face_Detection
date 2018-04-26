package com.example.deepak.facedetector2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int global_size;

   // ImageView imageView = (ImageView)findViewById(R.id.imageView);

    //list to store the captured all the detected faces in an image
    List<Bitmap> imgList = new ArrayList<Bitmap>();

   /* for (int i=1 ; i<=n ; i++)
    {
        croppedfaces.add(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("num" + i, "drawable", getPackageName())));
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView = (ImageView)findViewById(R.id.imageView);
        Button btnProgress = (Button)findViewById(R.id.btnProgress);


        final Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.hi2);
        imageView.setImageBitmap(myBitmap);

        final Paint rectPaint = new Paint();
        rectPaint.setStrokeWidth(5);
        rectPaint.setColor(Color.WHITE);
        rectPaint.setStyle(Paint.Style.STROKE);
        final Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(),myBitmap.getHeight(),Bitmap.Config.RGB_565); //RGB 565 is some format

        final Canvas canvas = new Canvas(tempBitmap);
        //draw bitmap for canvas
        canvas.drawBitmap(myBitmap,0,0,null);


        if (btnProgress != null) {
            btnProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                            .setTrackingEnabled(false)
                            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                            .setMode(FaceDetector.FAST_MODE)
                            .build();
                    if(!faceDetector.isOperational()){


                        Toast.makeText(MainActivity.this , "FaceDetector could not be setup in your Device" ,Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                    SparseArray<Face> sparseArray = faceDetector.detect(frame);


                    float smilingProbability;
                    float leftEyeOpenProbability;
                    float rightEyeOpenProbability;
                    float eulerY;
                    float eulerZ;
                   /* for (int i = 0; i < sparseArray.size(); i++) {
                        Face face = sparseArray.valueAt(i);

                        smilingProbability = face.getIsSmilingProbability();
                        leftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
                        rightEyeOpenProbability = face.getIsRightEyeOpenProbability();
                        eulerY = face.getEulerY();
                        eulerZ = face.getEulerZ();

                       /* Toast.makeText(getApplicationContext(),
                                "Smiling: " + smilingProbability
                                        + "Left eye open: " + leftEyeOpenProbability
                                        + "Right eye open: " + rightEyeOpenProbability
                                        + "Euler Y: " + eulerY
                                        + "Euler Z: " + eulerZ,
                                Toast.LENGTH_LONG).show();*/

                      /*  Log.d("Tuts+ Face Detection", "Smiling: " + smilingProbability);
                        Log.d("Tuts+ Face Detection", "Left eye open: " + leftEyeOpenProbability);
                        Log.d("Tuts+ Face Detection", "Right eye open: " + rightEyeOpenProbability);
                        Log.d("Tuts+ Face Detection", "Euler Y: " + eulerY);
                        Log.d("Tuts+ Face Detection", "Euler Z: " + eulerZ);
                    }*/






                    global_size = sparseArray.size();


                    for( int i = 0; i < sparseArray.size(); i++ ) {
                        Face face = sparseArray.valueAt(i);

                        float x1 = face.getPosition().x;
                        float y1 = face.getPosition().y;
                        float x2 = x1 + face.getWidth();
                        float y2 = y1 + face.getHeight();

                        smilingProbability = face.getIsSmilingProbability();
                        leftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
                        rightEyeOpenProbability = face.getIsRightEyeOpenProbability();
                        eulerY = face.getEulerY();
                        eulerZ = face.getEulerZ();

                        Toast.makeText(getApplicationContext(),
                                "Smiling: " + smilingProbability
                                        + "Left eye open: " + leftEyeOpenProbability
                                        + "Right eye open: " + rightEyeOpenProbability
                                        + "Euler Y: " + eulerY
                                        + "Euler Z: " + eulerZ,
                                Toast.LENGTH_LONG).show();

                        RectF rectF = new RectF(x1,y1,x2,y2);
                        canvas.drawRoundRect(rectF,2,2,rectPaint);

                        // And this is for cropping the detected face from the
                        // image
                        Bitmap croppedBitmap = Bitmap.createBitmap(myBitmap,
                                (int)x1,
                                (int) y1,
                                (int) x2,
                                (int)y2);
//                                (int) (midPoint.x - (eyeDistance * 2)),
//                                (int) (midPoint.y - (eyeDistance * 2)),
//                                (int) (eyeDistance * 4),
//                                (int) (eyeDistance * 4));
                        imgList.add(croppedBitmap);


                    }
                     imageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));



                }
            });
        }
    }

    /*public void postoutput()
    {


        for (int i=0 ; i<=global_size ; i++) {


            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filename);
                imgList.get(i).compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }*/

    /* String filename = "pippo.png";
File sd = Environment.getExternalStorageDirectory();
File dest = new File(sd, filename);

Bitmap bitmap = (Bitmap)data.getExtras().get("data");
try {
     FileOutputStream out = new FileOutputStream(dest);
     bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
     out.flush();
     out.close();
} catch (Exception e) {
     e.printStackTrace();
}*/





}
