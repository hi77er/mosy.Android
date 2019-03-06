package com.mosy.kalin.mosy.ItemViews;

import android.content.Context;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Table;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_table)
public class TableItemView
        extends WallItemViewBase {

    private Context baseContext;
    private Table table;


    @ViewById(R.id.tableItem_tvTableName)
    TextView tableNameTextView;

    public TableItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(Table table) {
        if (table != null) {
            this.table = table;
            this.tableNameTextView.setText(table.Name);
        }
    }

}
