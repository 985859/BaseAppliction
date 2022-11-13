package com.example.base.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class BaseAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

    private final List<T> data;
    private final int layoutId;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemChildClickListener onItemChildClickListener;
    private OnItemChildLongClickListener onItemChildLongClickListener;
    private HashSet<Integer> childViewIds;
    private HashSet<Integer> childLongViewIds;

    public BaseAdapter(int layoutId) {
        this.layoutId = layoutId;
        data = new ArrayList<>();
    }

    public BaseAdapter(int layoutId, List<T> list) {
        this.layoutId = layoutId;
        data = list != null ? list : new ArrayList<>();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        VH vh = (VH) new BaseViewHolder<>(view);
        bindViewClickListener(vh, viewType);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        onBindItem(holder, getItem(position), position);
    }

    private void bindViewClickListener(VH vh, int viewType) {
        vh.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(this, v, vh.getAdapterPosition());
            }
        });
        vh.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(this, v, vh.getAdapterPosition());
            }
            return false;
        });
        if (childViewIds != null && !childViewIds.isEmpty()) {
            for (Integer id : childViewIds) {
                View childView = vh.itemView.findViewById(id);
                if (childView != null) {
                    childView.setOnClickListener(v -> {
                        if (onItemChildClickListener != null) {
                            onItemChildClickListener.onItemChildClick(this, childView, vh.getAdapterPosition());
                        }
                    });
                }
            }
        }

        if (childLongViewIds != null && !childLongViewIds.isEmpty()) {
            for (Integer id : childLongViewIds) {
                View childView = vh.itemView.findViewById(id);
                if (childView != null) {
                    childView.setOnLongClickListener(v -> {
                        if (onItemChildLongClickListener != null) {
                            onItemChildLongClickListener.onItemChildLongClick(this, childView, vh.getAdapterPosition());
                        }
                        return false;
                    });
                }
            }
        }
    }


    protected abstract void onBindItem(@NonNull VH holder, T t, int position);

    public Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    protected void setList(List<T> list) {
        if (list != this.data) {
            this.data.clear();
            if (list != null && !list.isEmpty()) {
                this.data.addAll(list);
            }
        } else {
            if (!list.isEmpty()) {
                List newList = new ArrayList(list);
                this.data.clear();
                this.data.addAll(newList);
            }
        }
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        if (isChanged(position)) {
            return getItem(position);
        } else {
            return null;
        }
    }

    public void setItem(int position, T t) {
        if (isChanged(position)) {
            data.set(position, t);
            notifyItemChanged(position);
        }
    }

    public void addItem(T t) {
        data.add(t);
        notifyItemRangeChanged(data.size() - 1, 1, 0);
    }

    public void addItem(int position, T t) {
        if (isChanged(position)) {
            data.add(position, t);
            notifyItemInserted(position);
        }
    }

    public void addAll(List<T> data) {
        if (data != null && data.isEmpty()) {
            int startPosition = this.data.size();
            this.data.addAll(data);
            notifyItemRangeChanged(startPosition, data.size(), 0);
        }
    }


    public void removeItem(int position) {
        if (isChanged(position)) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount() - position);

        }

    }

    private boolean isChanged(int position) {
        return position >= 0 && position < getItemCount();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    /***
     *
     *  在设置数据之前设置 或在构造方法中设置
     * **/
    public void addClickChildViewIds(int... ids) {
        if (childViewIds == null) {
            childViewIds = new HashSet<>();
        }
        for (int id : ids) {
            childViewIds.add(id);
        }
    }

    /***
     *
     *  在设置数据之前设置 或在构造方法中设置
     * **/
    public void addClickLongChildViewIds(int... ids) {
        if (childLongViewIds == null) {
            childLongViewIds = new HashSet<>();
        }
        for (int id : ids) {
            childLongViewIds.add(id);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BaseAdapter adapter, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(BaseAdapter adapter, View view, int position);
    }

    public interface OnItemChildClickListener {
        void onItemChildClick(BaseAdapter adapter, View view, int position);
    }

    public interface OnItemChildLongClickListener {
        void onItemChildLongClick(BaseAdapter adapter, View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener onItemChildLongClickListener) {
        this.onItemChildLongClickListener = onItemChildLongClickListener;
    }


}
