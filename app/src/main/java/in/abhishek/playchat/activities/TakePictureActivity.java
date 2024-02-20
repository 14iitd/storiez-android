package in.abhishek.playchat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.abhishek.playchat.R;
import in.abhishek.playchat.adapter.PreviewAdapter;
import in.abhishek.playchat.libraries.ZoomageView;
import in.abhishek.playchat.utils.BasicUtils;
import in.abhishek.playchat.utils.Constants;
import in.abhishek.playchat.utils.ShowCamera;

public class TakePictureActivity extends AppCompatActivity implements View.OnClickListener{

//======== Reusable Variables ==================

    private static final String TAG = "TakePictureActivity";
    Context context;
    BasicUtils basicUtils;

    //======== Layout Variables ====================

    FrameLayout flCameraLayout;
    FrameLayout flCaptureButton;
    LinearLayout llPreview;
    FloatingActionButton fabUploadPODButton;
    public TextView capturedImageCount;
    TextView tvClickDelay;
    ImageView ivPreviewToggle;

    //================ operation Variables ==========================

    public static final int IMAGE_LIMIT = 20; // Set the Limit of the POD pages here...
    public Toast toast;
    public int count = -1;
    public int imageNameCount = -1;
    int returnStatus;
    public static final int REQUEST_CODE = 100;
    private static final int FOCUS_AREA_SIZE = 300;

    boolean isSaved = false;
    boolean isFirstOpening = true;
    boolean isFinished = false;
    public boolean isOutOfMemory = false;
    public boolean isFromCropActivity = false;
    boolean isKYC = false;

    ShowCamera showCamera;
    public Camera camera;
    String drsId;
    String drsDate;
    String imageFileName;
    String uploadPDFname;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    PreviewAdapter adapter;

    //DatabaseHandler db;
    Dialog dialog;
    public File fileDir;
    ZoomageView dialog_preview_image;
    ImageView dialog_preview_dismiss;
    ImageView dialog_preview_delete;
    ImageView dialog_preview_crop;

    //===== Arrays ============================
    public static String[] STORAGE_PERMISSIONS1 = {Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static String[] STORAGE_PERMISSIONS2 = {Manifest.permission.CAMERA};
    public static String[] STORAGE_PERMISSIONS3 = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};

    public ArrayList<File> fileOfImage = new ArrayList<>();
    public File[] imageList = new File[IMAGE_LIMIT + 30];// Excessive length just to prevent ArrayOutOfBounds Exception.

    //================ AWS Variables ================================================
    private static final String MY_BUCKET = "scancopyofdrs";
//    AmazonS3Client s3Client;
//    PutObjectRequest putObjectRequests[] = new PutObjectRequest[IMAGE_LIMIT], por;
//    private CognitoCachingCredentialsProvider credentialsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.whiteTransparent));
        }
        init();
        getPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isFinished && camera != null) {
            camera.stopPreview();
        }
        isFirstOpening = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstOpening) {
            openCamera();
        }
    }

    private void init() {

        context = TakePictureActivity.this;
        basicUtils = new BasicUtils(TakePictureActivity.this);
        flCameraLayout = findViewById(R.id.flCameraLayout);
        flCaptureButton = findViewById(R.id.flCaptureImage);
        llPreview = findViewById(R.id.llPreview);
        fabUploadPODButton = findViewById(R.id.fabUploadPod);
        capturedImageCount = findViewById(R.id.tvAvailableClick);
        tvClickDelay = findViewById(R.id.tvClickDelayTimer);
        ivPreviewToggle = findViewById(R.id.ivPreviewToggle);

        ivPreviewToggle.setOnClickListener(this);


        toast = new Toast(TakePictureActivity.this);
        //db = new DatabaseHandler(TakePictureActivity.this);

//        isKYC = getIntent().getBooleanExtra("KYC", false);
//        drsId = getIntent().getExtras().getString("drsId");
//        drsDate = getIntent().getExtras().getString("drsDate");

        Log.i(TAG, "Drs Id = " + drsId);

        recyclerView = findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        adapter = new PreviewAdapter(context, fileOfImage, capturedImageCount, isKYC);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);

        File customDir = new File(getExternalFilesDir(null) + Constants.STORAGE_PATH + "POD/");
        if (!customDir.exists()) {
            customDir.mkdirs();
        }

        initDialog();
        previewToggleHandler();
    }

    private void previewToggleHandler() {
        if (isArrayNull()) {
            ivPreviewToggle.setVisibility(View.GONE);
        } else {
            ivPreviewToggle.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                previewToggleHandler();
            }
        }, 200);
    }

    private boolean isArrayNull() {
        if (adapter.getImageCount().equals("0")) {
            return true;
        } else {
            return false;
        }
    }

    private void initDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_image_preview);

        dialog_preview_dismiss = dialog.findViewById(R.id.ivDismiss_preview);
        dialog_preview_crop = dialog.findViewById(R.id.ivCrop_image);
        dialog_preview_delete = dialog.findViewById(R.id.ivDelete_image);
        dialog_preview_image = dialog.findViewById(R.id.ivPreview);

        dialog_preview_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void getPermission() {
        if (isFirstOpening) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT<33) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TakePictureActivity.this, STORAGE_PERMISSIONS1, REQUEST_CODE);
                } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TakePictureActivity.this, STORAGE_PERMISSIONS2, REQUEST_CODE);
                } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TakePictureActivity.this, STORAGE_PERMISSIONS3, REQUEST_CODE);
                } else {
                    openCamera();
                }
            } else {
                openCamera();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length != 0) {
            if (grantResults.length == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Intent i = new Intent(context, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    TakePictureActivity.this.finish();
                }
            } else if (grantResults.length == 2) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Intent i = new Intent(context, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    TakePictureActivity.this.finish();
                }
            } else if (grantResults.length == 3) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Intent i = new Intent(context, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    TakePictureActivity.this.finish();
                }
            }
        } else {
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            TakePictureActivity.this.finish();
        }
    }

    //============================= Methods Related to Capture Image  =====================================================

    private void openCamera() {
        try {
            camera = Camera.open();
        } catch (Exception e) {
            basicUtils.showCustomAlert(e.toString());
            e.printStackTrace();
        }
        if (camera != null) {
            Camera.Parameters params = null;
            try {
                params = camera.getParameters();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (params != null && params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            List<Camera.Size> sizes = null;
            try {
                sizes = params.getSupportedPictureSizes();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (sizes == null) {
                Toast.makeText(context, "Error loading camera, please try again...", Toast.LENGTH_LONG).show();
                finish();
            }
            Camera.Size size = null;
            boolean isPerfectSizeFound = false;
            ArrayList<SizeItem> sizeList = new ArrayList<>();
            try {
                size = sizes.get(0);
                for (int i = 0; i < sizes.size(); i++) {
                   /* if ((float)(sizes.get(i).width/sizes.get(i).height) > 1.4){
                        if (sizes.get(i).width > size.width)
                            size = sizes.get(i);
                    }*/
                    if ( ((float)sizes.get(i).width / sizes.get(i).height) > 1.4) {
                        SizeItem item = new SizeItem();
                        item.setWidth(sizes.get(i).width);
                        item.setHeight(sizes.get(i).height);
                        sizeList.add(item);
                    }
                    Log.i(TAG, "openCamera: Sizes = " + sizes.get(i).width + " Height = " + sizes.get(i).height + " Divide = " + (float)(sizes.get(i).width / sizes.get(i).height) + "\n CWidth = " + flCameraLayout.getWidth() + " CHeight = " + flCameraLayout.getHeight());
                    Log.i(TAG, "openCamera: Sizes2 = " + sizeList.toString());
                }
                try {
                    SizeItem item = sizeList.get(0);
                    size.width = item.getWidth();
                    size.height = item.getHeight();
                    Log.i(TAG, "openCamera: Sizes3 = " + size.width + " Height = " + size.height);
                    for (int i = 0; i < sizeList.size(); i++) {
                        if (item.getWidth() < sizeList.get(i).getWidth()) {
                            size.width = sizeList.get(i).getWidth();
                            size.height = sizeList.get(i).getHeight();
                            Log.i(TAG, "openCamera: Sizes3 = " + size.width + " Height = " + size.height);
                            isPerfectSizeFound = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    for (int i = 0; i < sizes.size(); i++) {
                        if (sizes.get(i).width > size.width)
                            size = sizes.get(i);
                    }
                }
                if (!isPerfectSizeFound) {
                    for (int i = 0; i < sizes.size(); i++) {
                        if (sizes.get(i).width > size.width)
                            size = sizes.get(i);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //Manage Camera Orientation
            try {
                if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    params.set("orientation", "portrait");
                    camera.setDisplayOrientation(90);
                    params.setRotation(90);
                } else {
                    params.set("orientation", "landscape");
                    camera.setDisplayOrientation(0);
                    params.setRotation(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                params.setPictureSize(size.width, size.height);
                Log.i(TAG, "Size : " + "Width = " + size.width + " Height = " + size.height);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //params.setPictureSize(700,1024);
            try {
                camera.setParameters(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            showCamera = new ShowCamera(context, camera);
            camera.startPreview();
            flCameraLayout.addView(showCamera);
            flCaptureButton.setOnClickListener(this);
            fabUploadPODButton.setOnClickListener(this);
            setFocus();
        }
    }

    private void captureImage() {
        if (camera != null) {

            new TakePicture().execute();

//            credentialsProvider = new CognitoCachingCredentialsProvider(
//                    this,
//                    Constants.AWS_IDENTITY_POOL_ID,
//                    Regions.AP_SOUTHEAST_1
//            );
//            s3Client = new AmazonS3Client(credentialsProvider);
        }
    }

    private File getOutputImageFile() {
        imageNameCount++;
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SSS", Locale.ENGLISH).format(new Date());
        fileDir = new File(getExternalFilesDir(null) + Constants.STORAGE_PATH + (isKYC ? "KYC/" : "POD/"));
        if (!fileDir.exists())
            fileDir.mkdirs();
        imageFileName = "skyking_branch_" + drsId + "_" + timeStamp + "_" + imageNameCount + ".png";
        File file = new File(fileDir, imageFileName);
        return file;
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            count++;
            capturedImageCount.setText(String.valueOf(Integer.parseInt(adapter.getImageCount()) + 1));
            File image = getOutputImageFile();
            fileOfImage.add(image);
            imageList[count] = image;
            Log.i(TAG, "Output Image = " + image.toString());
            try {
                FileOutputStream outputStream = new FileOutputStream(image);
                outputStream.write(data);
                outputStream.close();
                camera.startPreview();
                Toast.makeText(context, "Captured", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(RecyclerView.FOCUS_RIGHT);

                showCropActivityAfterPhotoClick(true); // todo false this if you don't want to open Crop Activity After Each Photo Click

                adapter.mPosition = adapter.getItemCount() - 1;
                adapter.mFile = fileOfImage.get(adapter.getItemCount() - 1);
                new CountDownTimer(2000, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvClickDelay.setVisibility(View.VISIBLE);
                        tvClickDelay.setText(String.valueOf((int) millisUntilFinished / 500));
                    }

                    @Override
                    public void onFinish() {
                    }
                }.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvClickDelay.setVisibility(View.GONE);
                        flCaptureButton.setEnabled(true);
                    }
                }, 2000);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void showCropActivityAfterPhotoClick(boolean show) {
        if (show) {
            Uri imageUri = Uri.fromFile(fileOfImage.get(adapter.getItemCount() - 1));
            File destinationFile = new File(fileOfImage.get(adapter.getItemCount() - 1).getAbsolutePath());
            Log.i(TAG, "Path = " + fileOfImage.get(adapter.getItemCount() - 1).getAbsolutePath().toString());
            UCrop ucrop = UCrop.of(imageUri, Uri.fromFile(destinationFile));
            ucrop.withOptions(adapter.getCropOptions());
            ucrop.start(TakePictureActivity.this);
        }
    }

    //======= Methods Related to Focus of Tap to Focus Feature =========================

    private void setFocus() {

        flCameraLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    focusOnTouch(event);
                }
                return false;
            }
        });
    }

    private void focusOnTouch(MotionEvent event) {
        if (camera != null) {
            Camera.Parameters parameters = null;
            try {
                parameters = camera.getParameters();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (parameters.getMaxNumMeteringAreas() > 0) {
                Log.i(TAG, "fancy !");
                Rect rect = calculateFocusArea(event.getX(), event.getY());

                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);
                try {
                    camera.setParameters(parameters);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //camera.getParameters();
                camera.autoFocus(mAutoFocusTakePictureCallback);
            } else {
                camera.autoFocus(mAutoFocusTakePictureCallback);
            }
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / flCameraLayout.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / flCameraLayout.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);

        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if (touchCoordinateInCameraReper > 0) {
                result = 1000 - focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }
        return result;
    }

    private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                Log.i("tap_to_focus", "success!");
            } else {
                Log.i("tap_to_focus", "fail!");
            }
        }
    };

    //====================================================================================
//    private String createPDF() {
//        String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SSS", Locale.ENGLISH).format(new Date());
//        String pdfName = "skyking_branch_" + drsId + "_" + timeStamp + ".pdf";
//        try {
//            File customDir = new File(getExternalFilesDir(null) + Constants.STORAGE_PATH + (isKYC ? "KYC/" : "POD/"));
//            if (!customDir.exists()) {
//                customDir.mkdirs();
//            }
//            String pdfPath = customDir.getAbsolutePath() + "/" + pdfName;
//            Document document = new Document();
//            try {
//                FileOutputStream fos = new FileOutputStream(new File(pdfPath), true);
//                PdfWriter pdfWriter = PdfWriter.getInstance(document, fos);
//                pdfWriter.open();
//                document.open();
//
//                for (File imagepath : fileOfImage) {
//                    if (imagepath.exists()) {
//                        BitmapFactory.Options opts = new BitmapFactory.Options();
//                        Bitmap bmp = BitmapFactory.decodeFile(imagepath.getPath(), opts);
//
//                        ExifInterface exif = new ExifInterface(imagepath.getPath());
//                        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
//                        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
//
//                        int rotationAngle = 0;
//                        if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
//                            rotationAngle = 90;
//                        if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
//                            rotationAngle = 180;
//                        if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
//                            rotationAngle = 270;
//
//                        Matrix matrix = new Matrix();
//                        matrix.setRotate(rotationAngle, (float) bmp.getWidth() / 2, (float) bmp.getHeight() / 2);
//                        Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
//
//                        rotatedBitmap = Bitmap.createScaledBitmap(rotatedBitmap, 700, 1024, true);
//                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
//
//                        Image myImg = null;
//                        myImg = Image.getInstance(bytes.toByteArray());
//                        myImg.scaleToFit(550f, 800f);
//                        myImg.setAlignment(Image.ALIGN_CENTER);
//                        document.add(myImg);
//                    }
//                }
//
//            } catch (FileNotFoundException e) {
//                Toast.makeText(TakePictureActivity.this, "File Not Found", Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            document.close();
//            por = new PutObjectRequest(MY_BUCKET, pdfName, new File(fileDir, pdfName));
//            putObjectRequests[0] = por;
//            Log.d("CHECK", "put object request array" + putObjectRequests.toString());
//            Log.d("AWS_NAME", "createPdf: " + pdfName);
//        } catch (OutOfMemoryError error) {
//            try {
//                isOutOfMemory = true;
//                //UserExperior.setCustomTag("Out of Memory Error!", UeCustomType.MSG);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            error.printStackTrace();
//        }
//        return pdfName;
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.flCameraLayout:
                break;
            case R.id.flCaptureImage:
                if (!adapter.getImageCount().equals(String.valueOf(IMAGE_LIMIT))) {
                    captureImage();
                    flCaptureButton.setEnabled(false);
                } else {
                    basicUtils.showCustomAlert("You can upload " + IMAGE_LIMIT + " images at a time!");
                }
                break;
            case R.id.fabUploadPod:
                if (!adapter.getImageCount().equals("0")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Upload?");
                    if (adapter.getImageCount().equals("1")) {
                        builder.setMessage("Upload " + (isKYC ? "KYC" : "POD") + " of " + adapter.getImageCount() + " Image?");
                    } else {
                        builder.setMessage("Upload " + (isKYC ? "KYC" : "POD") + " of " + adapter.getImageCount() + " Images?");
                    }
                    builder.setIcon(R.drawable.ic_upload);
                    builder.setPositiveButton("UPLOAD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new MakePDF(isOutOfMemory).execute();
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    basicUtils.showCustomAlert("No Picture taken yet to Upload!");
                }
                break;
            case R.id.ivPreviewToggle:
                if (llPreview.getVisibility() == View.VISIBLE) {
                    ivPreviewToggle.setImageResource(R.drawable.ic_hide_small_preview);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.preview_down);
                    llPreview.startAnimation(animation);
                    ivPreviewToggle.startAnimation(animation);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            llPreview.setVisibility(View.GONE);
                        }
                    }, 200);
                } else {
                    ivPreviewToggle.setImageResource(R.drawable.ic_hide_small_preview);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.preview_up);
                    llPreview.startAnimation(animation);
                    ivPreviewToggle.startAnimation(animation);
                    llPreview.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP) {
            if (data != null) {
                final Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    Toast.makeText(context, "Image Cropped Successfully!", Toast.LENGTH_SHORT).show();
                    List<Bitmap> croppedList = new ArrayList<Bitmap>(Arrays.asList(adapter.thumbBitmap));
                    croppedList.remove(adapter.thumbBitmap[adapter.mPosition]);
                    try {
                        new MakeThumbImage(croppedList, adapter.mFile.getAbsolutePath(), adapter.mPosition).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Cannot retrieve image!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == UCrop.RESULT_ERROR) {
            final Throwable error = UCrop.getError(data);
            if (error != null) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Unexpected Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deletePicture(int position, ArrayList<File> fileOfImages) {

        fileOfImage.addAll(fileOfImages);

        List<File> fileList = new ArrayList<File>(Arrays.asList(imageList));
        fileList.remove(imageList[position]);
        imageList = fileList.toArray(new File[0]);
    }

    public void setPdfName(String name) {
        uploadPDFname = name;
    }

    public String getPdfName() {
        return uploadPDFname;
    }

    public class MakePDF extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd = new ProgressDialog(context);
        public String pdfName;
        boolean isMemoryFull;

        public MakePDF(boolean memoryStatus) {
            this.isMemoryFull = memoryStatus;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Creating " + (isKYC ? "KYC file, " : "POD, ") + "please wait...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //pdfName = createPDF();
            setPdfName(pdfName);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isMemoryFull) {
                Toast.makeText(context, "Out of Memory, Can't Upload " + (isKYC ? "KYC" : "POD") + "!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(context, (isKYC ? "KYC file" : "POD") + " Created!", Toast.LENGTH_SHORT).show();
                if (isKYC) {
                    String path = getExternalFilesDir(null) + Constants.STORAGE_PATH + "KYC/" + pdfName;
                    Log.i(TAG, "onPostExecute : PDF Path = " + path);
//                    CustomerAddActivity.KYCFile = new File(path);
//                    if (!CustomerAddActivity.isMergeEnable)
//                        CustomerAddActivity.pdfFilePath = path;
//                    else {
//                        CustomerAddActivity.toBeMergedPdfFilePath = path;
//                        CustomerAddActivity.MergePDF mergePDF = new CustomerAddActivity.MergePDF();
//                        mergePDF.execute();
//                    }
//                    CustomerAddActivity.tvKYC.setTextColor(getResources().getColor(R.color.green));
//                    CustomerAddActivity.ivShowKYC.setVisibility(View.VISIBLE);
//                    CustomerAddActivity.tvKYC.setText("KYC Received");
//                    CustomerAddActivity.tvUploadKyc.setText("Re-Upload");
                    finish();
                } else {
                    new PutPODInAws().execute();
                }
            }
            if (pd.isShowing())
                pd.dismiss();
            new DeleteUnUploadedImages(false).execute();
        }
    }

    public class PutPODInAws extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd = new ProgressDialog(context);
        boolean isUploaded;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isUploaded = false;
            pd.setMessage("Uploading POD...");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground: UPLOAD");
                //s3Client.putObject(putObjectRequests[0]);
                isUploaded = true;
            } catch (Exception e) {
                isUploaded = false;
                Log.e("Upload to AWS", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isSaved = true;
            if (isUploaded) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //volleyCloseDRS(getPdfName());
                        Log.i(TAG, "Name of PDf = " + getPdfName());
                        finish();
                    }
                }, 500);

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Something went wrong. Please Try again!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 500);
            }
            if (pd.isShowing())
                pd.dismiss();

            camera.stopPreview();
            camera.release();
            camera = null;
            isFinished = true;
        }


    }

    public class TakePicture extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                camera.takePicture(null, null, mPictureCallback);
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class MakeThumbImage extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;
        String imagePath;
        int position;
        List<Bitmap> bitmapList;

        public MakeThumbImage(List<Bitmap> croppedList, String imagePath, int position) {
            this.position = position;
            this.bitmapList = croppedList;
            this.imagePath = imagePath;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), 128, 128);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            bitmapList.add(position, bitmap);
            adapter.thumbBitmap = bitmapList.toArray(new Bitmap[0]);
            adapter.notifyDataSetChanged();
        }
    }

    public class DeleteUnUploadedImages extends AsyncTask<Void, Void, Void> {
        boolean isDeleted = false;
        boolean isFromBackPressed;
        ProgressDialog pd = new ProgressDialog(context);

        public DeleteUnUploadedImages(boolean isFromBackPressed) {
            this.isFromBackPressed = isFromBackPressed;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isFromBackPressed) {
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.setMessage("Cancelling...");
                pd.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int i = 0;
            for (File image : imageList) {
                i++;
                if (image != null) {
                    if (image.exists())
                        image.delete();
                }
                if (i == imageList.length) {
                    isDeleted = true;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isDeleted && isFromBackPressed) {
                camera.stopPreview();
                camera.release();
                camera = null;
                isFinished = true;
                if (pd.isShowing())
                    pd.dismiss();
                finish();
            }
        }
    }

//    public void volleyCloseDRS(String output) {
//        final API_Details details = new API_Details(context);
//        details.setAPI_Name("volleyCloseDRS");
//        String url = ApiProcessing.DrsInfo.URL;
//        Log.i(TAG, "volleyCloseDRS: URL = " + url);
//
//        JSONObject object = ApiProcessing.DrsInfo.constructDrs(1,
//                db.getBranchName(),
//                db.getCompanyName(),
//                Double.parseDouble(drsId),
//                output);
//        Log.d(TAG, "volleyCloseDRS: SEND = " + object.toString());
//        Log.i(TAG, "Post Link = " + output);
//        details.setAPI_URL(url);
//        details.setObject(object.toString());
//        PostJsonArrayRequest jsonArrayR = new PostJsonArrayRequest(
//                Request.Method.POST,
//                url,
//                object,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        details.setResponse(response.toString());
//                        Log.i(TAG, "volleyCloseDRS: Response = " + response.toString());
//                        try {
//                            returnStatus = ApiProcessing.DrsInfo.parseResponse(response, context);
//                            if (returnStatus == 1)
//                                Toast.makeText(context, "POD Uploaded Successfully!", Toast.LENGTH_SHORT).show();
//                            else
//                                Toast.makeText(context, "POD Upload Failed, Please Try Again!", Toast.LENGTH_SHORT).show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            details.setException(e.toString());
//                        }
//                        if (Constants.SUPER_USER)
//                            details.show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        details.setErrorResponse(error.toString());
//                        if (Constants.SUPER_USER)
//                            details.show();
//                        Log.e(TAG, "volleyCloseDRS: Error = " + error.toString());
//                        Log.e(TAG, "volleyCloseDRS: Error Message = " + error.getMessage());
//                        Toast.makeText(context, "POD upload failed due to poor Internet connection!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//        RequestQueue queue = Volley.newRequestQueue(context);
//        queue.add(jsonArrayR);
//        jsonArrayR.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//    }

    @Override
    public void onBackPressed() {
        if (!isSaved) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Cancel " + (isKYC ? "KYC" : "POD") + " Upload?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DeleteUnUploadedImages(true).execute();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    public class SizeItem {
        int width;
        int height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return "SizeItem{" +
                    "width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

}