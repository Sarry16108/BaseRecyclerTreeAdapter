package com.example.administrator.testall.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.testall.entity.BaseRecyclerTreeItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 按类型显示item
 */
public abstract class BaseRecyclerTreeAdapter<T extends BaseRecyclerTreeItem> extends RecyclerView.Adapter<BaseRecyclerTreeAdapter.BaseRecyclerTreeHolder> {
    private List<T> mDatas;
    private Context mContext;
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public static class BaseRecyclerTreeHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public BaseRecyclerTreeHolder(View itemView) {
            super(itemView);
        }

        public void setDataBinding(ViewDataBinding viewDataBinding) {
            this.binding = viewDataBinding;
        }

        public void setVariable(int variableId, Object value) {
            binding.setVariable(variableId, value);
        }
    }

    public BaseRecyclerTreeAdapter(Context context, Collection<T> datas){
        if (datas == null) {
            mDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            mDatas = (List<T>) datas;
        } else {
            mDatas = new ArrayList<>(datas);
        }

        this.mContext = context;
    }

    /**
     * 动态增加一条数据
     *
     * @param itemDataType 数据实体类对象
     */
    public void append(T itemDataType) {
        if (itemDataType != null) {
            mDatas.add(itemDataType);
            notifyDataSetChanged();
        }
    }


    /**
     * 动态增加一组数据集合
     *
     * @param itemDataTypes 数据实体类集合
     */
    public void append(Collection<T> itemDataTypes) {
        if (itemDataTypes.size() > 0) {
            for (T itemDataType : itemDataTypes) {
                mDatas.add(itemDataType);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 替换全部数据
     *
     * @param itemDataTypes 数据实体类集合
     */
    public void replace(Collection<T> itemDataTypes) {
        mDatas.clear();
        if (itemDataTypes.size() > 0) {
            mDatas.addAll(itemDataTypes);
            notifyDataSetChanged();
        }
    }

    public List<T> getData(){
        return mDatas;
    }
    /**
     * 移除一条数据集合
     *
     * @param position
     */
    public void remove(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 移除所有数据
     */
    public void removeAll() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public T getItem(int position){
        if(mDatas != null && mDatas.size() > position) {
            return mDatas.get(position);
        }else{
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(mDatas.get(position), position);
    }

    abstract public int getViewType(T value, int position);
    abstract public int getLayoutByType(int viewType);

    @Override
    public BaseRecyclerTreeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = getLayoutByType(viewType);
        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), layout, parent, false);
        BaseRecyclerTreeHolder holder = new BaseRecyclerTreeHolder(dataBinding.getRoot());
        holder.setDataBinding(dataBinding);
        return holder;
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerTreeHolder holder, final int position) {
        final T value = mDatas.get(position);
        showData(holder, position, value);
        if (longListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.onLongClick(holder, value, position);
                    return true;
                }
            });
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当允许点击，才可以进行展开折叠操作
                if (value.isClickable()) {
                    if (value.isExpandable()) {
                        collapseGroup(position);
                    } else {
                        expandGroup(position);
                    }
                }

                if (listener != null) {
                    listener.onItemClick(holder, value, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener l){
        this.listener = l;

    }

    public void setOnItemLongClickListener(OnItemLongClickListener l){
        this.longListener = l;
    }

    /**
     * 显示数据抽象函数
     *
     * @param viewHolder 基类ViewHolder,需要向下转型为对应的ViewHolder（example:MainRecyclerViewHolder mainRecyclerViewHolder=(MainRecyclerViewHolder) viewHolder;）
     * @param position   位置
     * @param data
     */
    public abstract void showData(BaseRecyclerTreeHolder viewHolder, int position, T data);

    public interface OnItemClickListener<T> {
        boolean onItemClick(BaseRecyclerTreeHolder viewHolder, T data, int position);
    }

    public interface OnItemLongClickListener<T>{
        boolean onLongClick(BaseRecyclerTreeHolder viewHolder, T data, int position);
    }

    //展开分组
    public void expandGroup(int itemPos) {
        T data = mDatas.get(itemPos);
        if (null == data || data.isExpandable() || null == data.getChilds() || 0 == data.getChilds().size()) {
            return;
        }

        //如果是最后一项，则直接添加到末尾，否则插入到该项之后位置
        if (itemPos + 1 == mDatas.size()) {
            mDatas.addAll(data.getChilds());
        } else {
            mDatas.addAll(itemPos + 1, data.getChilds());
        }

        data.setExpandable(true);
        notifyDataSetChanged();
    }

    private int calculateExpandedCount(T data) {
        //没有展开或者子项为0，返回0
        if (!data.isExpandable() || 0 == data.getChilds().size()) {
            return 0;
        }

        int count = data.getChilds().size();
        int curCount = 0;
        for (int i = 0; i < count; ++i) {
            curCount += calculateExpandedCount((T) data.getChilds().get(i));
        }

        data.setExpandable(false);
        return curCount + count;
    }

    //折叠分组
    public void collapseGroup(int itemPos) {
        T data = mDatas.get(itemPos);
        if (null == data || !data.isExpandable() || null == data.getChilds() || 0 == data.getChilds().size()) {
            return;
        }

        //计算需要折叠项数，反向删除，因为正向会改变删除后的列表项pos
        int count = calculateExpandedCount(data);
        for (int i = count; i > 0; --i) {
            mDatas.remove(itemPos + i);
        }
        data.setExpandable(false);
        notifyDataSetChanged();
    }
}
