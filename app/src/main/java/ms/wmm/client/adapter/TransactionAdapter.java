package ms.wmm.client.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import ms.wmm.client.R;
import ms.wmm.client.bo.GroupHead;
import ms.wmm.client.bo.Transaction;

/**
 * Created by Marcin on 16.10.2016.
 */
public class TransactionAdapter extends ArrayAdapter<Transaction> {

    public TransactionAdapter(Context context, Transaction[] transactions) {
        super(context, R.layout.transaction_item, transactions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View itemView=layoutInflater.inflate(R.layout.transaction_item,parent,false);
        Transaction transaction=getItem(position);

        TextView valueTxt= (TextView) itemView.findViewById(R.id.valueTxt);
        TextView userTxt= (TextView) itemView.findViewById(R.id.userTxt);
        TextView descTxt=(TextView) itemView.findViewById(R.id.descTxt);
        LinearLayout mainLayout=(LinearLayout) itemView.findViewById(R.id.mainLayout);

        BigDecimal value=transaction.getValue();
        value.setScale(2);

       if(value.compareTo(BigDecimal.ZERO) > 0){
           userTxt.setText("od "+transaction.getUser());
           valueTxt.setTextColor(ContextCompat.getColor(getContext(),R.color.positiveTransaction));

       }else{
           userTxt.setText("dla  "+transaction.getUser());
           valueTxt.setTextColor(ContextCompat.getColor(getContext(),R.color.negativeTransaction));
       }

           valueTxt.setText(value.toString());
        descTxt.setText(transaction.getDesc());
        valueTxt.setText(value.toString());

        return itemView;
    }
}
