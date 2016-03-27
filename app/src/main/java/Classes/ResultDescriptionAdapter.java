package Classes;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syncbridge.bestconnections.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pasan Eramusugoda on 7/15/2015.
 */
public class ResultDescriptionAdapter extends RecyclerView.Adapter<ResultDescriptionAdapter.RecyclerViewHolder> {

    private List<Data> mData = new ArrayList<>();

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.layout_life_foundation_body_result_description_item, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if(mData.get(position).mIsTitle) {
            holder.row.setVisibility(View.GONE);
            holder.title.setVisibility(View.VISIBLE);

            holder.titleText.setText(mData.get(position).mTitleText);

        } else {
            holder.title.setVisibility(View.GONE);
            holder.row.setVisibility(View.VISIBLE);

            holder.rowHead.setText(mData.get(position).mRowHead);
            holder.rowText.setText(mData.get(position).mRowText, TextView.BufferType.SPANNABLE);
            holder.rowText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(Data data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    public void addItem(int position, Data data) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void updateList(List<Data> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout title, row;
        public TextView titleText, rowHead, rowText;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title = (LinearLayout) itemView.findViewById(R.id.title);
            row = (LinearLayout) itemView.findViewById(R.id.row);
            titleText = (TextView) itemView.findViewById(R.id.titleText);
            rowHead = (TextView) itemView.findViewById(R.id.rowHead);
            rowText = (TextView) itemView.findViewById(R.id.rowText);
        }
    }

}

