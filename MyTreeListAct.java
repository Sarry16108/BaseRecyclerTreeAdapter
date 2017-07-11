package com.example.administrator.testall.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.testall.BR;
import com.example.administrator.testall.R;
import com.example.administrator.testall.adapter.BaseRecyclerTreeAdapter;
import com.example.administrator.testall.entity.ShowTreeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class MyTreeListAct extends AppCompatActivity {

    private BaseRecyclerTreeAdapter<ShowTreeItem> mAdapter;
    private List<ShowTreeItem>      mItems;
    private RecyclerView        mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.showList);

        mItems = new ArrayList<>(10);
        for (int i = 0; i < 10; ++i) {
            ShowTreeItem treeItem = new ShowTreeItem("i=" + i + i + i + i + i + i + i, i, true, 1);
            treeItem.initChilds();
            for (int j = 0; j < i / 3 + 1; ++j) {
                ShowTreeItem childItem = new ShowTreeItem("i=" + i + i + i + i + j + j + j, j, true, 2);
                childItem.initChilds();
                for (int k = 0; k < j; ++k) {
                    ShowTreeItem childItem3 = new ShowTreeItem("i=" + i + i + j + j + k + k + k, k, true, 3);
                    childItem.addChild(childItem3);
                }
                treeItem.addChild(childItem);
            }
            mItems.add(treeItem);
        }

        mAdapter = new BaseRecyclerTreeAdapter<ShowTreeItem>(this, mItems) {
            @Override
            public int getViewType(ShowTreeItem value, int position) {
                return value.getLevel() - 1;
            }

            @Override
            public int getLayoutByType(int viewType) {
                if (0 == viewType) {
                    return R.layout.layout_item_type_1;
                } else if (1 == viewType) {
                    return R.layout.layout_item_type_0;
                } else {
                    return R.layout.layout_item_type_2;
                }
            }

            @Override
            public void showData(BaseRecyclerTreeHolder viewHolder, int position, ShowTreeItem data) {
                viewHolder.setVariable(BR.value, data.getName());
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }
}
