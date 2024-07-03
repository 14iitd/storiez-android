package in.android.storiez.ui.home.post;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import java.util.ArrayList;
import java.util.List;
import java.util.function.ObjIntConsumer;

import in.android.storiez.data.remote.UserComments;
import in.android.storiez.databinding.FragmentCommentsBottomSheetBinding;
import in.android.storiez.ui.home.HomeViewModel;
import in.android.storiez.utils.BasicUtils;


public class CommentsBottomSheet extends BottomSheetDialogFragment {


    private static final String TAG = CommentsBottomSheet.class.getSimpleName();
    private static final String ARG_POST_ID = "post_id";

    public static CommentsBottomSheet newInstance(String postId) {
        CommentsBottomSheet fragment = new CommentsBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    private String postId;

    CommentsAdapter adapter;
    ProgressDialog progressDialog;

    HomeViewModel viewModel;

    List<UserComments> comments = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(ChatSourceBottomSheet.STYLE_NORMAL, R.style.CustomBottomSheetDialog);

        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);


        if (getArguments() != null) {
            postId = getArguments().getString(ARG_POST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCommentsBottomSheetBinding binding = FragmentCommentsBottomSheetBinding.inflate(inflater, container, false);
//

        adapter = new CommentsAdapter(comments);
        binding.commentRecyclerview.setAdapter(adapter);


        viewModel.getUserCommentsObservable().observe(getViewLifecycleOwner(), comments -> {
            if (comments != null) {
                adapter.setComments(comments);
            }
        });

        viewModel.getUserAddCommentObservable().observe(getViewLifecycleOwner(), isAdded -> {
            if (isAdded != null) {
                Log.d(TAG, "onCreateView: added  comment  successfully");
            } else {

                Log.d(TAG, "onCreateView: failed to add comment");
            }
        });


        viewModel.getPostComments(postId);

        binding.commentInputLayout.setEndIconOnClickListener(v -> {

            String comment = binding.commentEditText.getText().toString();
            if (comment.isEmpty()) {
                return;
            } else {

                UserComments commentObj = new UserComments();
                commentObj.setText(comment);
                commentObj.setUserId(BasicUtils.getDeviceId(requireContext()));
                viewModel.addComment(postId, commentObj);
                adapter.addComment(comment, BasicUtils.getDeviceId(requireContext()));
                binding.commentEditText.setText("");
            }

        });


        return binding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = 1600;
            view.setLayoutParams(layoutParams);
        }
    }


}
