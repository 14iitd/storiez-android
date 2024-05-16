package in.android.storiez.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import in.android.storiez.R;

public class API_Details {
    private static final String TAG = "API_Details";
    Context context;
    BasicUtils basicUtils;

    String API_Name = "";
    String API_URL = "";
    String Object = "";
    String Header = "";
    String Response = "";
    String Exception = "";
    String ErrorResponse = "";
    int ResponseLength = -1;
    public API_Details(Context context) {
        this.context = context;
        basicUtils = new BasicUtils(context);
    }

    private String getAPI_Name() {
        return API_Name;
    }

    public void setAPI_Name(String API_Name) {
        this.API_Name = API_Name;
    }

    private String getAPI_URL() {
        return API_URL;
    }

    public void setAPI_URL(String API_URL) {
        this.API_URL = API_URL;
    }

    private String getObject() {
        if (Object.trim().length() > 15000) {
            Object = Object.trim().substring(0, 15000);
        }
        return Object;
    }

    public void setObject(String object) {
        Object = object;
    }

    public void setObject(JSONObject object) {
        Object = object.toString();
    }

    private String getHeader() {
        return Header;
    }

    public void setHeader(String header) {
        Header = header;
    }

    private String getResponse() {
        if (Response.trim().length() > 15000) {
            Response = Response.trim().substring(0, 15000);
        }
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public void setResponse(JSONArray response) {
        Response = response.toString();
    }

    private int getResponseLength() {
        return ResponseLength;
    }

    public void setResponseLength(int responseLength) {
        ResponseLength = responseLength;
    }

    private String getException() {
        return Exception;
    }

    public void setException(String exception) {
        Exception = exception;
    }

    private String getErrorResponse() {
        return ErrorResponse;
    }

    public void setErrorResponse(String errorResponse) {
        ErrorResponse = errorResponse;
    }

    public void show() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_api_details);

        try {
            new DoTask(dialog).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Api_Details show: " + e.toString());
        }
    }

    private class DoTask extends AsyncTask<Void, Void, Void> {

        Dialog dialog;
        TextView tvCopy, tvOk;
        ImageView ivObject, ivResponse;
        String copyText = "";
        int width;
        ProgressDialog pd;

        public DoTask(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Getting Details...");
            pd.setCancelable(true);
            pd.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            width = metrics.widthPixels;
            LinearLayout llObject, llHeader, llResponse, llException, llErrorResponse;
            TextView tvName, tvApiUrl, tvObject, tvHeader, tvResponse, tvResponseLength, tvException, tvErrorResponse;

            llObject = dialog.findViewById(R.id.llObject);
            tvHeader = dialog.findViewById(R.id.tvHeader);
            llHeader = dialog.findViewById(R.id.llHeader);
            llResponse = dialog.findViewById(R.id.llResponse);
            llException = dialog.findViewById(R.id.llException);
            llErrorResponse = dialog.findViewById(R.id.llErrorResponse);
            tvName = dialog.findViewById(R.id.tvApiName);
            tvApiUrl = dialog.findViewById(R.id.tvApi);
            tvObject = dialog.findViewById(R.id.tvObject);
            tvResponse = dialog.findViewById(R.id.tvResponse);
            tvResponseLength = dialog.findViewById(R.id.tvLength);
            tvException = dialog.findViewById(R.id.tvException);
            tvErrorResponse = dialog.findViewById(R.id.tvErrorResponse);
            tvCopy = dialog.findViewById(R.id.tvCopy);
            tvOk = dialog.findViewById(R.id.tvOk);

            ivObject = dialog.findViewById(R.id.ivfsObject);
            ivResponse = dialog.findViewById(R.id.ivfsResponse);


            tvName.setText(getAPI_Name());
            tvApiUrl.setText(getAPI_URL());
            tvObject.setText(basicUtils.getFormatString(getObject()));
            tvHeader.setText(getHeader());
            tvResponse.setText(basicUtils.getFormatString(getResponse()));
            tvResponseLength.setText("Length : " + getResponseLength());
            tvException.setText(getException());
            tvErrorResponse.setText(getErrorResponse());


            if (tvObject.getText().toString().trim().equalsIgnoreCase("")) {
                llObject.setVisibility(View.GONE);
            }
            if (tvHeader.getText().toString().trim().equalsIgnoreCase("")) {
                llHeader.setVisibility(View.GONE);
            }
            if (tvResponse.getText().toString().trim().equalsIgnoreCase("")) {
                llResponse.setVisibility(View.GONE);
            }
            if (getResponseLength() == -1) {
                tvResponseLength.setVisibility(View.GONE);
            }
            if (tvException.getText().toString().trim().equalsIgnoreCase("")) {
                llException.setVisibility(View.GONE);
            }
            if (tvErrorResponse.getText().toString().trim().equalsIgnoreCase("")) {
                llErrorResponse.setVisibility(View.GONE);
            }


            StringBuffer buffer = new StringBuffer();

            buffer.append("Name = " + getAPI_Name() + "\n\n");

            buffer.append("=============================\n\nAPI = \n" + getAPI_URL() + "\n\n");

            if (llObject.getVisibility() == View.VISIBLE) {
                buffer.append("=============================\n\nOBJECT = \n" + basicUtils.getFormatString(getObject()) + "\n\n");
            }

            if (llHeader.getVisibility() == View.VISIBLE) {
                buffer.append("=============================\n\nHeader = \n" + basicUtils.getFormatString(getHeader()) + "\n\n");
            }

            if (llResponse.getVisibility() == View.VISIBLE) {
                buffer.append("=============================\n\nResponse = \n" + basicUtils.getFormatString(getResponse()) + "\n\n");
            }

            if (llException.getVisibility() == View.VISIBLE) {
                buffer.append("=============================\n\nException = \n" + getException() + "\n\n");
            }

            if (llErrorResponse.getVisibility() == View.VISIBLE) {
                buffer.append("=============================\n\nError Response = \n" + getErrorResponse() + "\n\n");
            }

            copyText = buffer.toString();


            dialog.getWindow().setLayout((width * 26) / 27, ViewGroup.LayoutParams.WRAP_CONTENT);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pd.isShowing())
                pd.dismiss();
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "API_Details : Exception = " + e.toString());
            }
            tvCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basicUtils.copyToClipBoard(copyText);
                }
            });

            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            ivObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Object().execute();
                }
            });

            ivResponse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Response().execute();
                }
            });
        }
    }

    private class Object extends AsyncTask<Void, Void, Void> {

        Dialog dialog;
        TextView tvCopy;
        TextView tvClose, tvValue;
        ScrollView scrollView;
//        CircleImageView civUp;
//        CircleImageView civDown;
        int width, height;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_api_details);
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();

            width = metrics.widthPixels;
            height = metrics.heightPixels;

            LinearLayout llMain, llExpanded;
            final TextView tvExpandedTitle;
            llMain = dialog.findViewById(R.id.llMain);
            llExpanded = dialog.findViewById(R.id.llExpanded);
            tvExpandedTitle = dialog.findViewById(R.id.tvExpandedTitle);
            tvValue = dialog.findViewById(R.id.tvValue);
            tvCopy = dialog.findViewById(R.id.tvExpandedCopy);
            tvClose = dialog.findViewById(R.id.tvClose);

            llMain.setVisibility(View.GONE);
            llExpanded.setVisibility(View.VISIBLE);
            tvExpandedTitle.setText("OBJECT");
            tvValue.setText(basicUtils.getFormatString(getObject()));
            tvValue.setTextColor(context.getResources().getColor(R.color.colorDeliveryGraph));

            scrollView = dialog.findViewById(R.id.scrollView);
//            civUp = dialog.findViewById(R.id.civUp);
//            civDown = dialog.findViewById(R.id.civDown);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            civUp.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    scrollView.scrollTo(0, 0);
//                }
//            });

//            civDown.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    scrollView.scrollTo(0, tvValue.getHeight());
//                }
//            });

            tvCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basicUtils.copyToClipBoard(basicUtils.getFormatString(getObject()));
                }
            });
            tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            try {
                dialog.show();
                dialog.getWindow().setLayout(width, (height * 26) / 27);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class Response extends AsyncTask<Void, Void, Void> {

        Dialog dialog;
        TextView tvCopy;
        TextView tvClose, tvValue;
        ScrollView scrollView;
//        CircleImageView civUp;
//        CircleImageView civDown;
        int width, height;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_api_details);
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();

            width = metrics.widthPixels;
            height = metrics.heightPixels;

            LinearLayout llMain, llExpanded;
            final TextView tvExpandedTitle;
            llMain = dialog.findViewById(R.id.llMain);
            llExpanded = dialog.findViewById(R.id.llExpanded);
            tvExpandedTitle = dialog.findViewById(R.id.tvExpandedTitle);
            tvValue = dialog.findViewById(R.id.tvValue);
            tvCopy = dialog.findViewById(R.id.tvExpandedCopy);
            tvClose = dialog.findViewById(R.id.tvClose);

            llMain.setVisibility(View.GONE);
            llExpanded.setVisibility(View.VISIBLE);
            tvExpandedTitle.setText("Response");
            tvValue.setText(basicUtils.getFormatString(getResponse()));
            tvValue.setTextColor(context.getResources().getColor(R.color.green));

            scrollView = dialog.findViewById(R.id.scrollView);
//            civUp = dialog.findViewById(R.id.civUp);
//            civDown = dialog.findViewById(R.id.civDown);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            civUp.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    scrollView.scrollTo(0, 0);
//                }
//            });
//
//            civDown.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    scrollView.scrollTo(0, tvValue.getHeight());
//                }
//            });

            tvCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    basicUtils.copyToClipBoard(basicUtils.getFormatString(getResponse()));
                }
            });
            tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            try {
                dialog.show();
                dialog.getWindow().setLayout(width, (height * 26) / 27);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
