package in.android.storiez.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment<B extends ViewBinding> extends Fragment {

    public B binding;

    public ProgressDialog mProgressDialog;

    private Activity activity;

    @LayoutRes
    abstract public int getLayoutId();

    @NonNull
    abstract public ViewBinding initViewBinding(LayoutInflater inflater, ViewGroup parent);

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(requireActivity()); // todo: ProgressDialog is deprecated
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (B) initViewBinding(inflater, container);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public B getViewBinding() {
        return binding;
    }

    public void showProgress() {
        showProgress("Please wait...");
    }

    public void showProgress(String message) {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    public void hideProgress() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void runOnUiThread(Runnable runnable) {
        try {
            activity.runOnUiThread(runnable);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


}
