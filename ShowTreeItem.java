package com.example.administrator.testall.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class ShowTreeItem extends BaseRecyclerTreeItem<ShowTreeItem>{
    private String name;
    private int     type;   //0~3

    public ShowTreeItem(String name, int type, boolean isClickable, int level) {
        super(isClickable, level);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }


}
