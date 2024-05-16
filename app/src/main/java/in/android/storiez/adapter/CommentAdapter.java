package in.android.storiez.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.android.storiez.R;
import in.android.storiez.items.RepliesItem;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<RepliesItem> list;
    Context context;

    public CommentAdapter(Context context, ArrayList<RepliesItem> list){
        this.context = context;
        this.list = list;
    }
    public void addData(List<RepliesItem> newData) {
        // Add the new data to the existing dataset
        list.addAll(newData);
        notifyDataSetChanged();
    }
    public static final String TAG = "NotifAdapter";
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView;

        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_item_comment, parent, false);

        MyViewHolder holder = new MyViewHolder(rowView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder mvHolder = (MyViewHolder) holder;
        final RepliesItem pi = list.get(position);

        ((MyViewHolder) holder).tv_comment.setText(pi.getText());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tv_comment;

        public MyViewHolder(View view) {
            super(view);
            tv_comment = (TextView) view.findViewById(R.id.text_comment);

        }

    }
}
