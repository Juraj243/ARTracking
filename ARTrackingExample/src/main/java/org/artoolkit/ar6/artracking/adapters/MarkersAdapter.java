package org.artoolkit.ar6.artracking.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.artoolkit.ar6.artracking.R;
import org.artoolkit.ar6.artracking.helpers.Convert;
import org.artoolkit.ar6.artracking.helpers.MsgHelper;
import org.artoolkit.ar6.artracking.models.Image;
import org.artoolkit.ar6.artracking.requests.Queue;
import org.artoolkit.ar6.artracking.requests.ThumbGetRequest;

/**
 * Created by krist on 20-Dec-17.
 */

public class MarkersAdapter extends RecyclerView.Adapter<MarkersAdapter.ViewHolder>{
    private Context mContext;
    private String[] mDataset;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String id);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public ImageView mThumb;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mThumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
        }
    }

    public MarkersAdapter(Context context, OnItemClickListener listener) {
        mContext = context;
        mDataset = new String[0];
        mListener = listener;
    }

    public void setDataset(String[] dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marker, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String id = mDataset[position];

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(mDataset[position]);
            }
        });

        holder.mTitle.setText(id);

        ThumbGetRequest request = new ThumbGetRequest(id, new Response.Listener<Image>(){
            @Override
            public void onResponse(Image response) {
                Bitmap b = Convert.toBitmap(response.getData());
                holder.mThumb.setImageBitmap(b);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MsgHelper.show(mContext, R.string.msg_error);
            }
        });
        Queue.getInstance(mContext).add(request);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
