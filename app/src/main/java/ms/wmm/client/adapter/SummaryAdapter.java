package ms.wmm.client.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import ms.wmm.client.R;
import ms.wmm.client.bo.Summary;

/**
 * Created by Marcin on 16.10.2016.
 */
public class SummaryAdapter extends ArrayAdapter<Summary> {

    public SummaryAdapter(Context context, Summary[] summaries) {
        super(context, R.layout.summary_item, summaries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View itemView=layoutInflater.inflate(R.layout.summary_item,parent,false);
        Summary summary=getItem(position);

        TextView valueTxt= (TextView) itemView.findViewById(R.id.valueTxt);
        TextView userTxt= (TextView) itemView.findViewById(R.id.userTxt);
        TextView descTxt=(TextView) itemView.findViewById(R.id.descTxt);
        LinearLayout mainLayout=(LinearLayout) itemView.findViewById(R.id.mainLayout);

        BigDecimal value=summary.getValue();
        value.setScale(2);

       if(summary.getValue().signum()>0){
           userTxt.setText("od "+summary.getUser());
           valueTxt.setTextColor(ContextCompat.getColor(getContext(),R.color.positiveTransaction));

       }else{
           userTxt.setText("dla  "+summary.getUser());
           valueTxt.setTextColor(ContextCompat.getColor(getContext(),R.color.negativeTransaction));
       }

           valueTxt.setText(value.toString());

        return itemView;
    }
}
