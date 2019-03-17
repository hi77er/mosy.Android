package com.mosy.kalin.mosy.ItemViews;

import android.content.Context;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.OrderMenuItem;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.SignalR.AccountOpenerSignalR;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_client_table_account_orders)
public class ClientTableAccountOrderItemView
        extends WallItemViewBase {

    private Context baseContext;
    private AccountOpenerSignalR signalRService;
    private OrderMenuItem orderMenuItem;

    @ViewById(R.id.clientTableAccountMenuItem_tvName)
    TextView nameTextView;
    @ViewById(R.id.clientTableAccountMenuItem_tvStatus)
    TextView statusTextView;

//    @ViewById(R.Id.clientTableAccountMenuItem_btnPlusOneItem)
//    Button buttonPlusOneItem;
//    @ViewById(R.Id.clientTableAccountMenuItem_btnMinusOneItem)
//    Button buttonMinusOneItem;

    public ClientTableAccountOrderItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(OrderMenuItem orderMenuItem, AccountOpenerSignalR signalRService) {
        if (orderMenuItem != null) {
            this.signalRService = signalRService;
            this.orderMenuItem = orderMenuItem;

            String itemName = (orderMenuItem.MenuListItem != null && StringHelper.isNotNullOrEmpty(orderMenuItem.MenuListItem.Name))
                    ? orderMenuItem.MenuListItem.Name
                    : orderMenuItem.ItemName;
            this.nameTextView.setText(itemName);

            if (orderMenuItem.Status != null) {
                switch (orderMenuItem.Status) {
                    case New:
                        updateItemStatus("New", R.color.colorRed, VISIBLE);
                        break;
                    case AwaitingAccountApproval:
                        updateItemStatus("Awaiting account approval", R.color.colorRed, VISIBLE);
                        break;
                    case Received:
                        updateItemStatus("Received", R.color.colorPrimaryApricot, GONE);
                        break;
                    case BeingPrepared:
                        updateItemStatus("Being prepared...", R.color.colorSecondaryAmber, GONE);
                        break;
                    case ReadyForDelivery:
                        updateItemStatus("Being delivered...", R.color.colorTertiaryLight, GONE);
                        break;
                    case Delivered:
                        updateItemStatus("Delivered", R.color.colorGreen, GONE);
                        break;
                }
            }
        }
    }

    private void updateItemStatus(String statusDisplayText, int colorId, int buttonsVisibility) {
        this.statusTextView.setText(statusDisplayText);
        this.statusTextView.setTextColor(getResources().getColor(colorId));
//        this.buttonPlusOneItem.setVisibility(buttonsVisibility);
//        this.buttonMinusOneItem.setVisibility(buttonsVisibility);
    }
//
//    @Click(R.Id.clientTableAccountMenuItem_btnPlusOneItem)
//    public void removeItem(){
//
//    }
//
//    @Click(R.Id.clientTableAccountMenuItem_btnMinusOneItem)
//    public void addOneMoreItem(){
//
//    }
}
