package cs50.caleb.receiptocr3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context historyContext;
    private ArrayList<History> historyArrayList;

    public HistoryAdapter(Context historyContext, ArrayList<History> historyArrayList) {
        this.historyContext = historyContext;
        this.historyArrayList = historyArrayList;
    }



    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View historyView = LayoutInflater.from(historyContext).inflate(R.layout.row_history, parent, false);

        return new HistoryViewHolder(historyView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = historyArrayList.get(position);

        holder.txtUserName.setText(    "User:          " + history.getUserName());
        holder.txtMerchantName.setText("Merchant:  " + history.getMerchantName());
        holder.txtDate.setText(        "Date:          " + history.getDate());
        holder.txtTime.setText(        "Time:          " + history.getTime());
        holder.txtSpent.setText(       "Spent:         " + history.getSpent().toString());
    }


    @Override
    public int getItemCount() {
        return (historyArrayList != null) ? historyArrayList.size() : 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView txtUserName, txtMerchantName, txtDate, txtTime, txtSpent, txtRawData;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txt_user_name);
            txtMerchantName = itemView.findViewById(R.id.txt_merchant_name);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtSpent = itemView.findViewById(R.id.txt_spent);

        }
    }
}