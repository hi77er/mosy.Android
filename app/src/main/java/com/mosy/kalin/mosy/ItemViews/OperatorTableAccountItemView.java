package com.mosy.kalin.mosy.ItemViews;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Enums.TableAccountStatus;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.Helpers.StringHelper;
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


    @ViewById(R.id.operatorTableAccountItem_tvTableName)
    TextView tableNameTextView;
    @ViewById(R.id.operatorTableAccountItem_tvStatus)
    TextView statusTextView;


    @ViewById(R.id.operatorTableAccountItem_llMarkApprovedLoading)
    LinearLayout markApprovedProgressLayout;
    @ViewById(R.id.clientTableAccountItem_btnMarkApproved)
    Button markApprovedButton;

    public OperatorTableAccountItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(TableAccount tableAccount, SignalRService signalRService) {
        if (tableAccount != null) {
            this.signalRService = signalRService;
            this.tableAccount = tableAccount;

            this.tableNameTextView.setText(tableAccount.TableName);

            String statusDisplayText = StringHelper.empty();
            if (tableAccount.Status != null) {
                switch (tableAccount.Status) {
                    case New:
                        updateItemStatus("New", R.color.colorRed, GONE);
                        break;
                    case AwaitingOperatorApprovement:
                        updateItemStatus("Awaiting your approvement", R.color.colorPrimaryApricot, VISIBLE);
                        break;
                    case Idle:
                        updateItemStatus("Enjoying your services", R.color.colorTertiaryLight, GONE);
                        break;
                    case Closed:
                        updateItemStatus("Closed.", R.color.softer, GONE);
                        break;
                }
            }
        }
    }

    private void updateItemStatus(String statusDisplayText, int colorId, int buttonVisibility) {
        this.statusTextView.setText(statusDisplayText);
        this.statusTextView.setTextColor(getResources().getColor(colorId));
        this.markApprovedButton.setVisibility(buttonVisibility);
        this.markApprovedProgressLayout.setVisibility(GONE);
    }

    @Click(R.id.clientTableAccountItem_btnMarkApproved)
    public void approveTableAccount(){
        this.markApprovedButton.setVisibility(GONE);
        this.markApprovedProgressLayout.setVisibility(VISIBLE);
        this.signalRService.updateTableAccountStatus(tableAccount.Id, TableAccountStatus.Idle);
    }

}
