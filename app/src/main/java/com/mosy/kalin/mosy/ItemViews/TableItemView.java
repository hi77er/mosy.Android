package com.mosy.kalin.mosy.ItemViews;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;
import com.mosy.kalin.mosy.DTOs.Table;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.SignalR.SignalRService;

import org.androidannotations.annotations.Click;
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
            this.tableNameTextView.setText(table.name);
        }
    }

}
