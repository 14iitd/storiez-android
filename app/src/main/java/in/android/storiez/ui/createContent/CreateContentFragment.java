package in.android.storiez.ui.createContent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import in.android.storiez.base.BaseFragment;
import in.android.storiez.databinding.FragmentCreateContentBinding;

public class CreateContentFragment extends BaseFragment<FragmentCreateContentBinding> {
    @Override
    public int getLayoutId() {
        return 0;
    }

    @NonNull
    @Override
    public ViewBinding initViewBinding(LayoutInflater inflater, ViewGroup parent) {
        return FragmentCreateContentBinding.inflate(inflater, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.pollCardOption.setOnClickListener(v -> {

            startActivity(new Intent(requireActivity(), CreateContentActivity.class));
        });


        binding.statusCardOption.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), CreateContentActivity.class));

        });

        binding.flashCardOption.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), CreateContentActivity.class));
        });


    }
}
