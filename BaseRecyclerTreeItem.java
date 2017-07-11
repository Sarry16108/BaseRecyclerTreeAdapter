package com.example.administrator.testall.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public abstract class BaseRecyclerTreeItem<T> {
    //是否展开
    private boolean isExpandable = false;
    //是否可点击
    private boolean isClickable = true;
    //是否是分组
    private boolean isGroup = true;
    //层级，从最外到最内，1,2,3...
    private int     level = 1;

    //child items
    private List<T> childs;

    public BaseRecyclerTreeItem(boolean isClickable, int level) {
        this.isClickable = isClickable;
        this.level = level;
    }

    public List<T> getChilds() {
        return childs;
    }

    public synchronized void initChilds() {
        if (null == this.childs) {
            this.childs = new ArrayList<>();
        }
    }

    public void setChilds(List<T> childs) {
        this.childs = childs;
        if (null != this.childs) {
            isGroup = 0 != this.childs.size();
        }
    }

    public void addChild(T child) {
        this.childs.add(child);
        if (null != this.childs) {
            isGroup = 0 != this.childs.size();
        }
    }

    public void addChilds(List<T> childs) {
        this.childs.addAll(childs);
        if (null != this.childs) {
            isGroup = 0 != this.childs.size();
        }
    }

    public boolean isGroup() {
        return isGroup;
    }


    public boolean isClickable() {
        return isClickable;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
