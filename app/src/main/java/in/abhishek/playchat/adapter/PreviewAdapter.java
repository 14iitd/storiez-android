package in.abhishek.playchat.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.abhishek.playchat.R;
import in.abhishek.playchat.activities.TakePictureActivity;
import in.abhishek.playchat.libraries.ZoomageView;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.MyViewHolder> {

    private static final String TAG = "PreviewAdapter";
    Context context;
    ArrayList<File> fileArrayList;
    public Bitmap[] thumbBitmap;
    final int THUMBSIZE = 128;
    TextView tvCount;
    public int mPosition;
    public int newPosition;
    public File mFile;
    boolean isKYC = false;

    public PreviewAdapter(Context context, ArrayList<File> fileArrayList, TextView capturedImageCount, boolean isKYC) {
        this.context = context;
        this.fileArrayList = fileArrayList;
        this.tvCount = capturedImageCount;
        this.isKYC = isKYC;
        thumbBitmap = new Bitmap[50];
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small_preview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.ivPreview.setImageResource(R.drawable.transparent);
        holder.pdProgress.setVisibility(View.VISIBLE);
        if (thumbBitmap[position] != null) {
            holder.ivPreview.setImageBitmap(thumbBitmap[position]);
            Log.i(TAG, "Bitmaps = " + thumbBitmap[position].toString());
            holder.pdProgress.setVisibility(View.GONE);
        } else {
            new MakeThumbImage(holder.ivPreview, holder.pdProgress, fileArrayList.get(position).getAbsolutePath(), position).execute();
        }
        holder.flPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "OnBindViewHolder Position = " + position);
                showPreview(fileArrayList.get(position).getAbsolutePath(), holder.ivPreview, holder.pdProgress, position);

            }
        });

        Log.i(TAG, "OnBindViewHolder Position = " + position);
    }

    @Override
    public int getItemCount() {
        return fileArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flPreview;
        ImageView ivPreview;
        ProgressBar pdProgress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            flPreview = itemView.findViewById(R.id.fl_preview);
            ivPreview = itemView.findViewById(R.id.iv_small_preview);
            pdProgress = itemView.findViewById(R.id.pd_preview);
        }
    }

    public class MakeThumbImage extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;
        ImageView imageView;
        ProgressBar progressBar;
        String imagePath;
        int position;
        boolean isOOM = false;

        public MakeThumbImage(ImageView imageView, ProgressBar progressBar, String ImagePath, int position) {
            this.imageView = imageView;
            this.progressBar = progressBar;
            this.imagePath = ImagePath;
            this.position = position;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), THUMBSIZE, THUMBSIZE);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                isOOM = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            thumbBitmap[position] = bitmap;

            imageView.setImageBitmap(bitmap);
            progressBar.setVisibility(View.GONE);
            if (isOOM) {
                Toast.makeText(context, "Out of memory, Please free up some storage!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showPreview(final String imagePath, final ImageView ivPreviews, final ProgressBar pb, final int position) {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dialog_image_preview);

        final ZoomageView ivPreview = dialog1.findViewById(R.id.ivPreview);
        ImageView ivCancel = dialog1.findViewById(R.id.ivDismiss_preview);
        ImageView ivDelete = dialog1.findViewById(R.id.ivDelete_image);
        ImageView ivCrop = dialog1.findViewById(R.id.ivCrop_image);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        //Log.i(TAG, "showPreview: Height = " + bitmap.getHeight() + " Width = " + bitmap.getWidth());
        ivPreview.setImageBitmap(bitmap);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete this Image from " + (isKYC ? "KYC" : "POD") + "?");
                builder.setIcon(R.drawable.ic_delete_alert);
                builder.setTitle("Delete?");
                builder.setCancelable(false);
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (fileArrayList.get(position).exists())
                                fileArrayList.get(position).delete();

                            fileArrayList.remove(position);
                            List<Bitmap> deletedList = new ArrayList<Bitmap>(Arrays.asList(thumbBitmap));
                            deletedList.remove(thumbBitmap[position]);
                            thumbBitmap = deletedList.toArray(new Bitmap[0]);
                            new TakePictureActivity().deletePicture(position, fileArrayList);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, fileArrayList.size());
                            tvCount.setText(getImageCount());
                            dialog1.dismiss();
                            Toast.makeText(context, "Image Deleted!", Toast.LENGTH_SHORT).show();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error deleting image!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });

        ivCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imageUri = Uri.fromFile(fileArrayList.get(position));
                File destinationFile = new File(fileArrayList.get(position).getAbsolutePath());
                Log.i(TAG, "Path = " + fileArrayList.get(position).getAbsolutePath().toString());
                UCrop ucrop = UCrop.of(imageUri, Uri.fromFile(destinationFile));
                ucrop.withOptions(getCropOptions());
                ucrop.start((AppCompatActivity) context);
               /* try {
                    UserExperior.setCustomTag("Crop Image Activity" , UeCustomType.EVENT);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                mPosition = position;
                mFile = fileArrayList.get(position);
                dialog1.dismiss();
            }
        });

        dialog1.show();

    }

    public String getImageCount() {
        int count = fileArrayList.size();
        return String.valueOf(count);
    }

    public UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionQuality(100);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);

        //UI options
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        options.setStatusBarColor(context.getResources().getColor(R.color.colorBlack));
        options.setToolbarColor(context.getResources().getColor(R.color.siteBlue));
        options.setToolbarWidgetColor(context.getResources().getColor(R.color.colorWhite));

        options.setToolbarTitle("Crop Image");

        return options;
    }
}
