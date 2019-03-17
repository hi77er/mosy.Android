package com.mosy.kalin.mosy.ItemViews;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.OrderMenuItem;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.SignalR.AccountOperatorSignalR;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_operator_table_account_orders)
public class OperatorTableAccountOrderItemView
        extends WallItemViewBase {

    private Context baseContext;
    private AccountOperatorSignalR signalRService;
    private OrderMenuItem orderMenuItem;

    @ViewById(R.id.operatorTableAccountMenuItem_tvName)
    TextView nameTextView;
    @ViewById(R.id.operatorTableAccountMenuItem_tvStatus)
    TextView statusTextView;

    @ViewById(R.id.operatorTableAccountMenuItem_llMarkDeliveredLoading)
    LinearLayout markDeliveredProgressLayout;
    @ViewById(R.id.operatorTableAccountMenuItem_btnMarkDelivered)
    Button markDeliveredButton;

    public OperatorTableAccountOrderItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(OrderMenuItem orderMenuItem, AccountOperatorSignalR signalRService) {
        if (orderMenuItem != null) {
            this.signalRService = signalRService;
            this.orderMenuItem = orderMenuItem;

            this.nameTextView.setText(orderMenuItem.MenuListItem.Name);

            if (orderMenuItem.Status != null) {
                String statusDisplayText = StringHelper.empty();
                switch (orderMenuItem.Status) {
                    case New:
                        updateItemStatus("New", R.color.colorRed, GONE);
                        break;
                    case AwaitingAccountApproval:
                        updateItemStatus("Awaiting account approval", R.color.colorRed, GONE);
                        break;
                    case Received:
                        updateItemStatus("Received", R.color.colorPrimaryApricot, GONE);
                        break;
                    case BeingPrepared:
                        updateItemStatus("Being prepared...", R.color.colorSecondaryAmber, GONE);
                        break;
                    case ReadyForDelivery:
                        updateItemStatus("Ready to pick", R.color.colorTertiaryLight, VISIBLE);
                        break;
                    case Delivered:
                        updateItemStatus("Delivered", R.color.colorGreen, GONE);
                        break;
                }
            }
        }
    }

    private void updateItemStatus(String statusDisplayText, int colorId, int buttonVisibility) {
        this.statusTextView.setText(statusDisplayText);
        this.statusTextView.setTextColor(getResources().getColor(colorId));
        this.markDeliveredButton.setVisibility(buttonVisibility);
        this.markDeliveredProgressLayout.setVisibility(GONE);
    }

    @Click(R.id.operatorTableAccountMenuItem_btnMarkDelivered)
    public void markOrderItemDelivered(){
        this.markDeliveredButton.setVisibility(GONE);
        this.markDeliveredProgressLayout.setVisibility(VISIBLE);
        this.signalRService.updateOrderRequestablesStatus(orderMenuItem.Id);
    }

}
