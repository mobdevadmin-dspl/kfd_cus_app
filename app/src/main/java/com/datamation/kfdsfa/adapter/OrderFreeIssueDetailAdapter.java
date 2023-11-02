package com.datamation.kfdsfa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.controller.ItemController;
import com.datamation.kfdsfa.model.FreeHed;
import com.datamation.kfdsfa.model.OrderDetail;

import java.util.ArrayList;

public class OrderFreeIssueDetailAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    ArrayList<OrderDetail> list;
    ArrayList<FreeHed> arrayList;
    Context context;
    String debCode;

    public OrderFreeIssueDetailAdapter(Context context, ArrayList<OrderDetail> list, String debCode){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.debCode = debCode;
    }
    @Override
    public int getCount() {
        if (list != null) return list.size();
        return 0;
    }
    @Override
    public OrderDetail getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        OrderFreeIssueDetailAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new OrderFreeIssueDetailAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.row_free_issue_details, parent, false);
            viewHolder.lblItem = (TextView) convertView.findViewById(R.id.row_item);
            viewHolder.lblQty = (TextView) convertView.findViewById(R.id.row_cases);
            viewHolder.lblAMt = (TextView) convertView.findViewById(R.id.row_piece);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderFreeIssueDetailAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.lblItem.setText(list.get(position).getFORDDET_ITEM_CODE() + " - " + new ItemController(convertView.getContext()).getItemNameByCode(list.get(position).getFORDDET_ITEM_CODE()));
        viewHolder.lblQty.setText(list.get(position).getFORDDET_QTY());
        viewHolder.lblAMt.setVisibility(View.GONE);
        viewHolder.lblAMt.setText("");

        return convertView;
    }
    private  static  class  ViewHolder{
        TextView lblItem;
        TextView lblQty;
        TextView lblAMt;
    }

}
