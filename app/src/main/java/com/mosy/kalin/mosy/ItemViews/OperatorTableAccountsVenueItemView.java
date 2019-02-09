package com.mosy.kalin.mosy.ItemViews;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.OperatorTablesAccountsActivity_;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_operator_table_accounts_venues)
public class OperatorTableAccountsVenueItemView
        extends WallItemViewBase {

    private Context baseContext;
    private Venue venue;

    @ViewById(R.id.operatorTableAccountsVenueItem_tvName)
    TextView nameTextView;
    @ViewById(R.id.operatorTableAccountsVenueItem_tvClass)
    TextView classTextView;

    public OperatorTableAccountsVenueItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(Venue venue) {
        if (venue != null) {
            this.venue = venue;

            this.nameTextView.setText(venue.Name);
            this.classTextView.setText(venue.Class);
        }
    }

    @Click(R.id.venueItem_btnMenu)
    public void ToTableAccountsMonitor()
    {
        Intent intent = new Intent(this.baseContext, OperatorTablesAccountsActivity_.class);
        intent.putExtra("Venue", this.venue);
        this.baseContext.startActivity(intent);
    }

}
