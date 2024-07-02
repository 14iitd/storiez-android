package in.android.storiez.ui.createContent;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import in.android.storiez.R;
import in.android.storiez.data.remote.UserComments;
import in.android.storiez.databinding.FragmentCommentsBottomSheetBinding;
import in.android.storiez.databinding.FragmentCreateContentBinding;
import in.android.storiez.databinding.FragmentCreateContentBottomSheetBinding;
import in.android.storiez.ui.home.post.CommentsAdapter;
import in.android.storiez.utils.BasicUtils;

public class CreateContentBottomSheet extends BottomSheetDialogFragment {


    public static CreateContentBottomSheet newInstance() {
        return new CreateContentBottomSheet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ensure the bottom sheet dismisses when clicking outside
        if (getDialog() != null) {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) getDialog();
            bottomSheetDialog.setCanceledOnTouchOutside(true);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCreateContentBottomSheetBinding binding = FragmentCreateContentBottomSheetBinding.inflate(inflater, container, false);
//


        binding.statsFabBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), CreateContentActivity.class));
            dismiss();
        });


        binding.flashFabBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), CreateContentActivity.class));
            dismiss();
        });

        binding.createFabBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), CreateContentActivity.class));

            dismiss();
        });



        return binding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = 600;
            view.setLayoutParams(layoutParams);
        }
    }
}
