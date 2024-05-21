package in.android.storiez.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import in.android.storiez.R;


public abstract class BaseActivity<V extends ViewBinding> extends AppCompatActivity {

    public V viewBinding;

    @LayoutRes
    abstract public int getLayoutId();

    abstract public ViewBinding initViewBinding(LayoutInflater inflater);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = (V) initViewBinding(getLayoutInflater());
        setContentView(viewBinding.getRoot());
    }

    public V getViewBinding() {
        return viewBinding;
    }

    /**
     * Show dialog with title, message, positive button, and negative button
     *
     * @param title            title of the dialog
     * @param message          message of the dialog
     * @param positiveText     text of the positive button
     * @param negativeText     text of the negative button
     * @param positiveListener listener of the positive button
     * @param negativeListener listener of the negative button
     */

}
