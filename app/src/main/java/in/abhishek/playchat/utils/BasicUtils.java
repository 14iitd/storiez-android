package in.abhishek.playchat.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import in.abhishek.playchat.R;


public class BasicUtils extends Activity {

    public Toast toast;
    Context context;
    Activity activity;
    String value;
    Dialog customProgressDialog;
    boolean doublePressExit = false;
    private static final String TAG = "BasicUtils";
    public BasicUtils(Context context) {
        this.toast = new Toast(context);
        this.context = context;
        try {
            this.activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public void showSnackAlert(CoordinatorLayout coordinatorLayout, String msg) {
//
//        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT);
//
//        View sbView = snackbar.getView();
//        sbView.bringToFront();
//        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
//        textView.setTextColor(Color.WHITE);
//
//        snackbar.show();
//    }

    public void showCustomAlerts(String sText, Context context) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View toastRoot = inflater.inflate(R.layout.toast, null);
        toast.setView(toastRoot);

        // set a message
        TextView text = (TextView) toastRoot.findViewById(R.id.tvToast);
        text.setText(sText);

        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
                0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /*public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }*/
    public void hideKeyboard() {
        try {
            Activity activity = (Activity) context;
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void showKeyboard(View editText) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "showKeyboard: Error = " + e.toString());
        }
    }

    public void showKeyboardForced() {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCustomAlert(String sText) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View toastRoot = inflater.inflate(R.layout.toast, null);
        toast.setView(toastRoot);

        // set a message
        TextView text = (TextView) toastRoot.findViewById(R.id.tvToast);
        text.setText(sText);

        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
                0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showLongCustomAlert(String sText) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View toastRoot = inflater.inflate(R.layout.toast, null);
        toast.setView(toastRoot);

        // set a message
        TextView text = (TextView) toastRoot.findViewById(R.id.tvToast);
        text.setText(sText);

        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
                0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static boolean hasExceedTime(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = format.parse(sDate);
            Calendar c = Calendar.getInstance();
            String date = format.format(c.getTime());
            endDate = format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            format = new SimpleDateFormat("dd/mm/yyyy' 'HH:mm:ss");
            try {
                startDate = format.parse(sDate);
                Calendar c = Calendar.getInstance();
                String date = format.format(c.getTime());
                endDate = format.parse(date);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedHours = different / hoursInMilli;

        if (elapsedHours > 48)
            return true;
        else
            return false;
    }

    public static boolean isStringNull(String s) {
        if (s == null || s.isEmpty()
                || s.equalsIgnoreCase("") || s.equalsIgnoreCase("null"))
            return true;
        return false;
    }

    public void placeCallToN(String i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("BasicUtils", "Calling: " + i);
            onCall(i);
        } else {
            Log.d("BasicUtils", "Calling: " + i);

            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + i));
            try {
                ((Activity) context).startActivity(callIntent);
            } catch (SecurityException se) {
            }
        }
    }

//    public void placeMultipleCallTon(String[] contacts) {
//        List<String> list = new ArrayList<String>(Arrays.asList(contacts));
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).trim().equalsIgnoreCase("")) {
//                list.remove(i);
//            }
//        }
//        contacts = list.toArray(new String[0]);
//
//        Dialog dialog = new Dialog(context);
//        Activity activity = (Activity) context;
//        DialogChooseContactBinding b = DialogChooseContactBinding.inflate(activity.getLayoutInflater());
//        setDialogAttributes(dialog, true, true, b.getRoot(), 6, 7);
//        ContactsAdapter adapter = new ContactsAdapter(contacts, dialog);
//        b.lvContacts.setAdapter(adapter);
//        dialog.show();
//    }

    public void placeEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:" + email + "?subject=" + Uri.encode("") + "&body=" + Uri.encode(""));
        intent.setData(data);
        context.startActivity(intent);
    }

    public void onCall(String n) {
       /* int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            showCustomAlerts("Cannot place call", context);

        } else {*/

        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + n));
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        //}
    }

    //Adapter ======
//    public class ContactsAdapter extends BaseAdapter {
//
//        String[] list;
//        Dialog d;
//
//        public ContactsAdapter(String[] list, Dialog d) {
//            this.list = list;
//            this.d = d;
//        }
//
//        @Override
//        public int getCount() {
//            return list.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_number, parent, false);
//            LinearLayout llRoot = view.findViewById(R.id.llRoot);
//            TextView tvContactNumber = view.findViewById(R.id.tvContactNumber);
//            tvContactNumber.setText(list[position].trim());
//            llRoot.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    placeCallToN(list[position].trim());
//                    if (d.isShowing())
//                        d.dismiss();
//                }
//            });
//            return view;
//        }
//    }

    public String getRandomNumber() {
        Random random = new Random();
        final int max = 9999999;
        final int min = 1000000;
        int randomNumber = random.nextInt((max - min) - 1) + min;
        return String.valueOf(randomNumber);
    }

    public String getRandomColor() {
        Random random = new Random();
        final int max = 999999;
        final int min = 100000;
        int randomColor = random.nextInt((max - min) - 1) + min;
        return "#" + String.valueOf(randomColor);
    }

    public void hideNavigationButtons() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        Activity activity = (Activity) context;
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

//    public void showSmallProgressDialog(boolean isShow) {
//        if (isShow) {
//            customProgressDialog = new Dialog(context, R.style.DialogSlideAnim);
//            customProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            customProgressDialog.getWindow().setBackgroundDrawable
//                    (new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            customProgressDialog.setCanceledOnTouchOutside(false);
//            customProgressDialog.setContentView(R.layout.dialog_progress);
//            ArcProgressLoader pb = customProgressDialog.findViewById(R.id.pb);
//            Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotate_pb);
//            pb.startAnimation(rotate);
//            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//            int width = metrics.widthPixels;
//            try {
//                if (customProgressDialog != null && customProgressDialog.isShowing())
//                    customProgressDialog.dismiss();
//                customProgressDialog.show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            customProgressDialog.getWindow().setLayout((width * 6) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
//        } else {
//            try //new
//            {
//                if (customProgressDialog != null && customProgressDialog.isShowing())
//                    customProgressDialog.dismiss();
//            } catch (IllegalArgumentException i) {
//                i.printStackTrace();
//            }
//
//        }
//    }



    public String getFormatString(String text) {

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n" + indentString + letter + "\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }

    public boolean validateMobile(String number) {
        if (number.length() == 10) {
            String regex = "(0/91)?[6-9][0-9]{9}";

            Pattern pat = Pattern.compile(regex);
            if (number == null)
                return false;
            return pat.matcher(number).matches();
        }
        return false;
    }

    public boolean validatePinCode(String number) {
        if (number.length() == 6) {
            String regex = "[1-9][0-9]{5}";
            Pattern pat = Pattern.compile(regex);
            if (number == null)
                return false;
            return pat.matcher(number).matches();
        }
        return false;
    }

    public boolean validateEmail(String emailid) {
        String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9]+[.]+[a-zA-Z0-9]+$";
        Pattern pat = Pattern.compile(regex);
        if (emailid.trim() == null) {
            return false;
        }
        return pat.matcher(emailid).matches();
    }

    public String getDuration(String startDate, String endDate, String format, boolean showSeconds) {
        String DURATION = "";

        String s_years = " years ";
        String s_months = " months ";
        String s_days = " days ";
        String s_hours = " hours ";
        String s_minutes = " minutes ";
        String s_seconds = " seconds ";

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);

        /*String creationDate = "03-03-2014 14:46:34";
        String updateDate = "02-06-2020 13:56:38";*/

        Date cDate = new Date();
        Date uDate = new Date();

        try {
            cDate = sdf.parse(startDate);
            uDate = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "getDuration: " + e.toString());
        }

        long diff = uDate.getTime() - cDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;
        long years = months / 12;

        while (seconds >= 60) {
            seconds = seconds - 60;
        }
        while (minutes >= 60) {
            minutes = minutes - 60;
        }
        while (hours >= 24) {
            hours = hours - 24;
        }
        while (days >= 30) {
            days = days - 30;
        }
        while (months >= 12) {
            months = months - 12;
        }

        Log.i(TAG, "getDuration : " + diff + " " + years + " Years\n" + months + " Months\n" +
                days + " Days\n" + hours + " Hours\n" + minutes + " Minutes\n" + seconds + " Seconds");

        if (years <= 1) {
            s_years = " year ";
        }
        if (months <= 1) {
            s_months = " month ";
        }
        if (days <= 1) {
            s_days = " day ";
        }
        if (hours <= 1) {
            s_hours = " hour ";
        }
        if (minutes <= 1) {
            s_minutes = " minute ";
        }
        if (seconds <= 1) {
            s_seconds = " second ";
        }


        DURATION = years + s_years
                + months + s_months + days
                + s_days + hours + s_hours
                + minutes + s_minutes
                + seconds + s_seconds;

        if (years == 0) {
            DURATION = DURATION.replace(years + s_years, "");
        }
        if (months == 0) {
            DURATION = DURATION.replace(months + s_months, "");
        }
        if (days == 0) {
            DURATION = DURATION.replace(days + s_days, "");
        }
        if (hours == 0) {
            DURATION = DURATION.replace(hours + s_hours, "");
        }
        if (minutes == 0) {
            DURATION = DURATION.replace(minutes + s_minutes, "");
        }
        if (seconds == 0) {
            DURATION = DURATION.replace(seconds + s_seconds, "");
        }

        if (!showSeconds) {
            if (DURATION.contains(seconds + s_seconds)) {
                DURATION = DURATION.replace(seconds + s_seconds, "");
            }
        }

        Log.i(TAG, "getDuration : Formatted : " + DURATION);

        return DURATION;
    }

    public static String getNullChecked(String text) {
        if (isStringNull(text)) {
            return "N/A";
        } else {
            return text;
        }
    }

    public String getFormattedDate(String inputDate) {
        Date parsedDate = null;
        String outputDate = "";
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        try {
            parsedDate = oldFormat.parse(inputDate);
            outputDate = newFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    public String getCustomFormattedDate(String inputDate, String inputFormat, String outPutFormat) {
        Date parsedDate = null;
        String outputDate = "";
        SimpleDateFormat oldFormat = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
        SimpleDateFormat newFormat = new SimpleDateFormat(outPutFormat, Locale.ENGLISH);

        try {
            parsedDate = oldFormat.parse(inputDate);
            outputDate = newFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    public String getFullFormattedDateTime(String date) {
        String formttedDate = getFormattedDate(date.substring(0, date.lastIndexOf("T")));
        String formattedTime = date.substring(date.lastIndexOf("T") + 1, date.length());
        if (formattedTime.contains(".")) {
            formattedTime = formattedTime.substring(0, formattedTime.lastIndexOf("."));
        }
        return formttedDate + " " + formattedTime;
    }

    public String[] concatTwoStringArrays(String[] s1, String[] s2) {
        String[] sumArray = new String[s1.length + s2.length];
        int tempPosition = s1.length;

        for (int i = 0; i < s1.length; i++) {
            sumArray[i] = s1[i];
        }
        for (int j = tempPosition; j < tempPosition + s2.length; j++) {
            sumArray[j] = s2[j - tempPosition];
        }
        return sumArray;
    }

    public void copyToClipBoard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Text Copied", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
    }

    public void setDialogAttributes(Dialog dialog, boolean setCanceledOnTouchOutside, boolean setCancelable, int contentView, int multiplyDialogWidthWith, int dividedBy) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(setCanceledOnTouchOutside);
        dialog.setCancelable(setCancelable);
        dialog.setContentView(contentView);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((width * multiplyDialogWidthWith) / dividedBy, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    //Method Overloading.....(Same name but different parameter type)
    public void setDialogAttributes(Dialog dialog, boolean setCanceledOnTouchOutside, boolean setCancelable, View contentView, int multiplyDialogWidthWith, int dividedBy) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(setCanceledOnTouchOutside);
        dialog.setCancelable(setCancelable);
        dialog.setContentView(contentView);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((width * multiplyDialogWidthWith) / dividedBy, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setDialogAttributesExtra(Dialog dialog, boolean setCanceledOnTouchOutside, boolean setCancelable, View contentView, int multiplyDialogWidthWith, int dividedBy, int multiplyDialogHeightWith, int heightDividedBy) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(setCanceledOnTouchOutside);
        dialog.setCancelable(setCancelable);
        dialog.setContentView(contentView);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((width * multiplyDialogWidthWith) / dividedBy, (height * multiplyDialogHeightWith) / heightDividedBy);
    }

    public static String getTimeStamp(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdf.format(new Date());
    }

    public Bitmap getBarcode(String text) {
        Bitmap correctBitmap = null;
        try {
            byte[] bytes = Base64.decode(text, Base64.DEFAULT);
            Bitmap barcodeBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            correctBitmap = Bitmap.createBitmap(barcodeBitmap, (int) (barcodeBitmap.getWidth() * (2.0f / 100.0f)), 0, (int) (barcodeBitmap.getWidth() * (90.0f / 100.0f)), (int) (barcodeBitmap.getHeight() * (33.0f / 100.0f)));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getBarcode : " + e.toString());
        }
        return correctBitmap;
    }

    public void vibrate() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }

    public void playBeep() {
        MediaPlayer m = new MediaPlayer();
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = activity.getAssets().openFd("beep.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void adjustScannerPreview(BarcodeReader barcodeReader) {
//        try {
//            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//            int width = metrics.widthPixels;
//            int height = metrics.heightPixels;
//            Log.i(TAG, "onCreate: Sizes = " + "Width = " + width + " Height = " + height);
//
//            ViewGroup.LayoutParams params = barcodeReader.getView().getLayoutParams();
//            params.height = height;
//            params.width = (int) (height + (height * 0.3));
//            barcodeReader.getView().setLayoutParams(params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public SpannableString getUnderlineText(String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }

    public static void setStatusBarColor(Activity a, int Color) {
        a.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = a.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(a.getResources().getColor(Color));
        }
    }

    public void highlightText(String text, String searchText, TextView tv) {
        if (searchText.equals("")) {
            tv.setText(text);
            return;
        }

        int start = text.toLowerCase().indexOf(searchText.toLowerCase());
        int end = start + searchText.length();
        if (start != -1) {
            SpannableString sp = new SpannableString(text);
            BackgroundColorSpan bcs = new BackgroundColorSpan(Color.rgb(255, 229, 152));
            sp.setSpan(bcs, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv.setText(sp);
        } else {
            tv.setText(text);
        }
    }

    public void setDoublePressExit() {
        if (doublePressExit == true) {
            toast.cancel();
            activity.finish();
        } else {
            showCustomAlert("Press back again to exit");
            doublePressExit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doublePressExit = false;
                }
            }, 2000);
        }
    }

    public void setDoublePressExit(String text) {
        if (doublePressExit == true) {
            toast.cancel();
            activity.finish();
        } else {
            showCustomAlert(text);
            doublePressExit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doublePressExit = false;
                }
            }, 2000);
        }
    }

    public static String getDeviceId(Context context){
        String unique_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return unique_id;
    }

}


