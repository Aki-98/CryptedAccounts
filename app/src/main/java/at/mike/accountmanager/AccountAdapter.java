package at.mike.accountmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private Context context;
    private List<Account> accounts = new ArrayList<>();
    private ActivityCallback activityCallbackListener;
    private String masterKey;

    public AccountAdapter(String masterKey) {
        this.masterKey = masterKey;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        context = parent.getContext();
        activityCallbackListener = (ActivityCallback) context;

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = accounts.get(position);

        holder.textViewPlatform.setText(account.getPlatform());
        holder.itemView.setOnClickListener((View v) -> {
            // open AddOrUpdAccountFragment
            activityCallbackListener.onCallback(masterKey, Constants.UPD_ACC, account);
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPlatform;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewPlatform = itemView.findViewById(R.id.textViewPlatform);
        }
    }
}
