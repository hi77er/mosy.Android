package com.mosy.kalin.mosy.ItemViews;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.SignalR.SignalRService;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_operator_table_accounts)
public class OperatorTableAccountItemView
        extends WallItemViewBase {

    private Context baseContext;
    private SignalRService signalRService;
    private TableAccount tableAccount;
    private String username;

    @ViewById(R.id.operatorTableAccountItem_tvTableName)
    TextView tableNameTextView;
    @ViewById(R.id.operatorTableAccountItem_tvStatus)
    TextView statusTextView;

    @ViewById(R.id.operatorTableAccountItem_llBaseLayout)
    LinearLayout basesLayout;
    @ViewById(R.id.operatorTableAccountItem_llUpdateAccountStatusLoading)
    LinearLayout updateStatusProgressLayout;
    @ViewById(R.id.clientTableAccountItem_btnUpdateStatus)
    Button updateAccountStatusButton;

    public OperatorTableAccountItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(TableAccount tableAccount, SignalRService signalRService, String username) {
        if (tableAccount != null) {
            this.signalRService = signalRService;
            this.tableAccount = tableAccount;
            this.username = username;

            this.tableNameTextView.setText(tableAccount.TableName);

            if (tableAccount.Status != null) {
                    switch (tableAccount.Status) {
                        case New:
                            updateItemStatus("New", R.color.colorRed, GONE, "-");
                            break;
                        case AwaitingOperatorApprovement:
                            updateItemStatus("Awaiting your approvement..", R.color.colorPrimaryApricot, VISIBLE, "Approve");
                            break;
                        case Idle:
                            updateItemStatus("Enjoying your services.", R.color.colorPrimary, GONE, "-");
                            break;
                        case OrderReadyForDelivery:
                            updateItemStatus("Item(s) for delivery!", R.color.colorSecondaryAmber, VISIBLE, "Check");
                            break;
                        case NeedAttention:
                            updateItemStatus("Needs attention!", R.color.colorSecondaryAmber, VISIBLE, "Ok");
                            break;
                        case AskingToPay:
                            updateItemStatus("Asking to pay!", R.color.colorSecondaryAmber, VISIBLE, "Ok");
                            break;
                        case Closed:
                            updateItemStatus("Closed.", R.color.soft, GONE, "-");
                            break;
                    }
            }
        }
    }

    private void updateItemStatus(String statusDisplayText, int colorResourceId, int buttonVisibility, String buttonText) {
        this.statusTextView.setText(statusDisplayText);
        this.statusTextView.setTextColor(getResources().getColor(colorResourceId));
        this.updateAccountStatusButton.setVisibility(buttonVisibility);
        this.updateAccountStatusButton.setText(buttonText);
        this.updateStatusProgressLayout.setVisibility(GONE);
    }

    @Click(R.id.clientTableAccountItem_btnUpdateStatus)
    public void updateTableAccountStatus(){
        this.updateAccountStatusButton.setVisibility(GONE);
        this.updateStatusProgressLayout.setVisibility(VISIBLE);

        this.signalRService.updateTableAccountStatus(tableAccount.Id, TableAccountStatus.Idle, this.username);
    }

}
