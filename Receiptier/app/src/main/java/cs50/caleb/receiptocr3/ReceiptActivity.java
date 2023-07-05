package cs50.caleb.receiptocr3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.service.autofill.Transformation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReceiptActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap original;

    private static final int REQUEST_OPEN_DOCUMENT = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    public static final int CAMERA_PERMISSION_REQUEST = 1000;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;

    private ImageButton rotateLeftButton;
    private ImageButton rotateRightButton;

    private File photoFile = null;
    private String currentPhotoPath;
    private String returnedResponse;

    private final int DATABASE_VERSION = 1;

    private final String DATABASE_NAME = "receiptocr.db";
    private final String DATABASE_DIRECTORY = Environment.getExternalStorageDirectory() + "/mydatabase/receiptocr/";

    private String name;
    private String dateValue;
    private double spentValue;

    private BottomNavigationView bottomNavigationView;

    private Button btnUploadImage;
    private TextView txtImageAnalysed;
    TextView textViewProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_receipt);

        textViewProfileName = findViewById(R.id.profile_name);
        textViewProfileName.setText("Profile: " + UserActivity.profileName);

        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnUploadImage.setEnabled(false);

        txtImageAnalysed = findViewById(R.id.txtImageAnalysed);
        txtImageAnalysed.setText("");

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_receipt);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(ReceiptActivity.this, HomeActivity.class));
                        finish();
                        return true;
                    case R.id.navigation_history:
                        startActivity(new Intent(ReceiptActivity.this, HistoryActivity.class));
                        finish();
                        return true;
                    case R.id.navigation_user:
                        startActivity(new Intent(ReceiptActivity.this, UserActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        }

        imageView = findViewById(R.id.image_view);
        rotateLeftButton = findViewById(R.id.rotateLeftButton);
        rotateRightButton = findViewById(R.id.rotateRightButton);

    }

    public void apply(Transformation filter) {
        if (original != null) {
            Glide
                    .with(this)
                    .load(original)
                    .apply(RequestOptions.bitmapTransform((com.bumptech.glide.load.Transformation<Bitmap>) filter))
                    .into(imageView);
        }
    }


    public void choosePhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");

        // Open gallery to view image to choose
        startActivityForResult(intent, REQUEST_OPEN_DOCUMENT);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // on choosePhoto activity
        if (requestCode == REQUEST_OPEN_DOCUMENT && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri uri1 = data.getData();

                ParcelFileDescriptor parcelFileDescriptor =
                        getContentResolver().openFileDescriptor(uri1, "r");

                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

                original = BitmapFactory.decodeFileDescriptor(fileDescriptor);

                parcelFileDescriptor.close();
                imageView.setImageBitmap(original);
                btnUploadImage.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Load the image from the file into a Bitmap
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            // Set the Bitmap to the ImageView
            imageView.setImageBitmap(imageBitmap);
            btnUploadImage.setEnabled(true);

        }
    }

    public void imageRotateLeft(View view) {
        imageView.setRotation(imageView.getRotation() - 90);
    }

    public void imageRotateRight(View view) {
        imageView.setRotation(imageView.getRotation() + 90);
    }

    // View complete code at: https://github.com/Asprise/receipt-ocr/tree/main/java-receipt-ocr

    /**
     * Uploads an image for receipt OCR and gets the result in JSON.
     * Required dependencies: org.apache.httpcomponents:httpclient:4.5.13 and org.apache.httpcomponents:httpmime:4.5.13
     */


    @SuppressLint("StaticFieldLeak")
    public void uploadImage(View view) throws IOException {
        String apiKey = "TEST";
        String recognizer = "auto";
        String refNo = "ocr_java_123";
        String url = "https://ocr.asprise.com/api/v1/receipt";

        // Load the image from the ImageView into a Bitmap
        //ImageView imageView = findViewById(R.id.imageView);
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Convert the Bitmap to a file
        File file = bitmapToFile(bitmap);

        // Create a request body with the file
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_key", apiKey)
                .addFormDataPart("recognizer", recognizer)
                .addFormDataPart("ref_no", refNo)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        // Create a POST request with the request body
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // Execute the network operation in a background thread
        new AsyncTask<Void, Void, String>() {
            @Override


            protected String doInBackground(Void... voids) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(request).execute();

                    return response.body().string();
                    //return jsonString;

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    // Delete the temporary file
                    file.delete();
                }
            }

            // After doInBackground() due to permission to access internet in android,
            // it will return to onPostExecute()
            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(String jsonString) {
                super.onPostExecute(jsonString);
                Log.d("OCR", jsonString);

                DatabaseHelper databaseHelper = new DatabaseHelper(ReceiptActivity.this);
                if (!databaseHelper.addReceipt(jsonString, UserActivity.profileName)) {
                    Toast.makeText(ReceiptActivity.this, "Unable to process receipt, please retake a clear receipt image", Toast.LENGTH_SHORT).show();
                }

                btnUploadImage.setEnabled(false);
                txtImageAnalysed.setText("Image Analysed please choose other receipt!");

            }
        }.execute();

    }

    private File bitmapToFile(Bitmap bitmap) throws IOException {
        File file = new File(getCacheDir(), "image.jpg");
        OutputStream outputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();
        return file;
    }

    public void historyActivity(View view) {
        Intent historyIntent = new Intent(this, HistoryActivity.class);
        startActivity(historyIntent);
    }

    public void receiptEnterManually(View view) {

        startActivity(new Intent(ReceiptActivity.this, ReceiptActivityManually.class));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receipt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_choose_photo:
                choosePhoto(new View(this));
                return true;
            case R.id.menu_enter_manually:
                receiptEnterManually(new View(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
