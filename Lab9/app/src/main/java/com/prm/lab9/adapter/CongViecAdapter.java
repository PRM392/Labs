package com.prm.lab9.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prm.lab9.R;
import com.prm.lab9.entity.CongViec;

import java.util.List;

public class CongViecAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<CongViec> congViecList;

    public CongViecAdapter(Context context, int layout, List<CongViec> congViecList) {
        this.context = context;
        this.layout = layout;
        this.congViecList = congViecList;
    }

    @Override
    public int getCount() {
        return congViecList != null ? congViecList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return congViecList != null ? congViecList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.txtTen = (TextView) view.findViewById(R.id.texviewTen);
            holder.imgDelete = (ImageView) view.findViewById(R.id.imageviewDelete);
            holder.imgEdit = (ImageView) view.findViewById(R.id.imageviewEdit);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        CongViec congViec = congViecList.get(i);
        holder.txtTen.setText(congViec.getTenCV());

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof com.prm.lab9.Ex3Activity) {
                    ((com.prm.lab9.Ex3Activity) context).DialogThem();
                }

                if(context instanceof com.prm.lab9.Ex4Activity) {
                    ((com.prm.lab9.Ex4Activity) context).DialogSuaCongViec(congViec.getTenCV(), congViec.getIdCV());
                }
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof com.prm.lab9.Ex5Activity) {
                    ((com.prm.lab9.Ex5Activity) context).DialogXoaCongViec(congViec.getTenCV(), congViec.getIdCV());
                }
            }
        });

        return view;
    }

    private class ViewHolder {
        TextView txtTen;
        ImageView imgDelete, imgEdit;
    }
}
