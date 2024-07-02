package in.android.storiez.ui.createContent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import in.android.storiez.base.BaseActivity;
import in.android.storiez.databinding.ActivityCreateContentBinding;
import in.android.storiez.databinding.FragmentCreateContentBinding;

public class CreateContentActivity extends BaseActivity<ActivityCreateContentBinding> {
    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public ViewBinding initViewBinding(LayoutInflater inflater) {
        return ActivityCreateContentBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebSettings webSettings = viewBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        viewBinding.webView.setWebViewClient(new WebViewClient());
        viewBinding.webView.loadUrl("https://www.youtube.com/");



//        binding.webView.setPadding(0, 0, 0, 0);
//        binding.webView.setInitialScale(1);
//        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        binding.webView.setScrollbarFadingEnabled(false);
//        // Enable JavaScript (if needed)
//        binding.webView.getSettings().setJavaScriptEnabled(true);
//        binding.webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//
//        binding.webView.clearCache(true);
//
//        // Set a WebViewClient to open links within the WebView
//        binding.webView.setWebViewClient(new WebViewClient());

        // Get the URL from the intent
        String url = "http://www.youtube.com/";

        // Load the URL in the WebView
        if (url != null) {
            viewBinding.webView.loadUrl(url);
        } else {
            viewBinding.webView.loadUrl("http://www.google.com/");
        }

        viewBinding.backArrow.setOnClickListener(v -> {
            onBackPressed();
        });

    }
}
