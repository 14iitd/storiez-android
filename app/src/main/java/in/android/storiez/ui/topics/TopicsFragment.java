package in.android.storiez.ui.topics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import in.android.storiez.base.BaseFragment;
import in.android.storiez.databinding.FragmentTopicsBinding;

public class TopicsFragment extends BaseFragment<FragmentTopicsBinding> {
    @Override
    public int getLayoutId() {
        return 0;
    }

    @NonNull
    @Override
    public ViewBinding initViewBinding(LayoutInflater inflater, ViewGroup parent) {
        return FragmentTopicsBinding.inflate(inflater, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
