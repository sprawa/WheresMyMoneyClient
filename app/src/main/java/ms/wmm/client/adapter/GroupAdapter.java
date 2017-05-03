package ms.wmm.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ms.wmm.client.R;
import ms.wmm.client.bo.GroupHead;

/**
 * Created by Marcin on 20.08.2016.
 */
public class GroupAdapter extends ArrayAdapter<GroupHead> {


    public GroupAdapter(Context context, GroupHead[] groups) {
        super(context, R.layout.group_item, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View itemView=layoutInflater.inflate(R.layout.group_item,parent,false);
        GroupHead group=getItem(position);

        TextView nameTextView= (TextView) itemView.findViewById(R.id.nameText);
        TextView adminTextView= (TextView) itemView.findViewById(R.id.adminText);
        TextView usersTextView=(TextView) itemView.findViewById(R.id.usersText);

        String name=group.getName();

        if(!group.isOpen())
            name=name+" (closed)";

        nameTextView.setText(group.getName());
        adminTextView.setText("Admin: "+group.getAdminName());
        usersTextView.setText("Users: "+group.getUsersString());

        return itemView;
    }




}
