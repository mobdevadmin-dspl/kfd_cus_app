package com.datamation.kfdsfa.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.controller.FreeMslabController;
import com.datamation.kfdsfa.controller.ItemController;
import com.datamation.kfdsfa.controller.OrderDetailController;
import com.datamation.kfdsfa.controller.PreProductController;
import com.datamation.kfdsfa.dialog.CustomKeypadDialog;
import com.datamation.kfdsfa.dialog.CustomKeypadDialogPrice;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.Item;
import com.datamation.kfdsfa.model.OrderDetail;
import com.datamation.kfdsfa.model.PreProduct;

import java.util.ArrayList;

public class PreOrderAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    Context context;
    ArrayList<PreProduct> list;
    String preText = null;
    public SharedPref mSharedPref;

    public PreOrderAdapter(Context context, final ArrayList<PreProduct> list) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.mSharedPref = SharedPref.getInstance(context);
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public PreProduct getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PreOrderAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new PreOrderAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.row_product_item_responsive_layout, parent, false);

            viewHolder.lnStripe = (LinearLayout) convertView.findViewById(R.id.lnProductStripe);
            viewHolder.itemBonus = (TextView) convertView.findViewById(R.id.row_bonus);
            viewHolder.pack = (TextView) convertView.findViewById(R.id.row_pack);
            viewHolder.ItemName = (TextView) convertView.findViewById(R.id.row_itemname);
            viewHolder.Price = (TextView) convertView.findViewById(R.id.row_price);
            viewHolder.HoQ = (TextView) convertView.findViewById(R.id.row_qoh);
            viewHolder.lblQty = (TextView) convertView.findViewById(R.id.et_qty);
            viewHolder.lblCase = (TextView) convertView.findViewById(R.id.et_case);
            viewHolder.btnPlus = (ImageButton) convertView.findViewById(R.id.btnAddition);
            viewHolder.btnMinus = (ImageButton) convertView.findViewById(R.id.btnSubtract);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PreOrderAdapter.ViewHolder) convertView.getTag();
        }
        final PreProduct product = getItem(position);

//        viewHolder.itemBonus.setText(product.getPREPRODUCT_REACODE());
//        viewHolder.itemBonus.setText(new FreeMslabController(context).getFreeDetails(product.getPREPRODUCT_ITEMCODE() , mSharedPref.getSelectedDebCode()));


        viewHolder.pack.setText(product.getPREPRODUCT_PACK());
        // viewHolder.unit.setText("Units("+product.getPREPRODUCT_UNIT()+")");
        viewHolder.ItemName.setText(product.getPREPRODUCT_ITEMCODE() + " : " + product.getPREPRODUCT_ITEMNAME());
        viewHolder.Price.setText(product.getPREPRODUCT_PRICE());
        viewHolder.HoQ.setText(product.getPREPRODUCT_QOH());
        // viewHolder.lblQty.setText(""+(Integer.parseInt(product.getPREPRODUCT_QTY())+Integer.parseInt(product.getPREPRODUCT_BALQTY())));
        viewHolder.lblQty.setText(product.getPREPRODUCT_QTY());
        // Log.d(">>>qtysettext",">>>"+(Integer.parseInt(product.getPREPRODUCT_QTY())+Integer.parseInt(product.getPREPRODUCT_BALQTY())));
        viewHolder.lblCase.setText(product.getPREPRODUCT_CASE());

        /*Change colors*/
        if (Double.parseDouble(viewHolder.lblQty.getText().toString()) > 0)
            viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));
        else
            viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


            viewHolder.itemBonus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.itemBonus.setText(new FreeMslabController(context).getFreeDetails(product.getPREPRODUCT_ITEMCODE(), mSharedPref.getSelectedDebCode()));

                }
            });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.generic_item_name, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Generic Item Name");
                alertDialogBuilder.setView(promptView);

                TextView itemName = (TextView) promptView.findViewById(R.id.itemName);

                String genericItemName = new ItemController(context).getGenericItemName(list.get(position).getPREPRODUCT_ITEMCODE());
                itemName.setText(genericItemName);

                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();
                return false;
            }
        });


        viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPref(context).setDiscountClicked("0");
                int qty = Integer.parseInt(viewHolder.lblQty.getText().toString());
                qty = qty - 1;
                if (qty >= 0) {
                    viewHolder.lblQty.setText(qty + "");
                } else {
                    qty = Integer.parseInt(viewHolder.lblQty.getText().toString());
                    Toast.makeText(context, "Cannot allow minus values", Toast.LENGTH_SHORT).show();
                    viewHolder.lblQty.setText(viewHolder.lblQty.getText().toString());
                }
                if ((new SharedPref(context).generateOrderId() == new SharedPref(context).getEditOrderId())) {

                    list.get(position).setPREPRODUCT_QTY(viewHolder.lblQty.getText().toString());
                    list.get(position).setPREPRODUCT_BALQTY("0");

                } else {
                    list.get(position).setPREPRODUCT_BALQTY("" + (Integer.parseInt(viewHolder.lblQty.getText().toString()) - Integer.parseInt(product.getPREPRODUCT_QTY())));
                    list.get(position).setPREPRODUCT_QTY(viewHolder.lblQty.getText().toString());

                    qty = Integer.parseInt(viewHolder.lblQty.getText().toString());

                }
                ArrayList<OrderDetail> toSaveOrderDetails = new OrderDetailController(context).mUpdatePrsSales(list.get(position));
                if (new OrderDetailController(context).createOrUpdateOrdDet(toSaveOrderDetails) > 0) {
                    Log.d("ORDER_DETAILS", "Order det saved successfully...");
                } else {
                    Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
                }
                /*Change colors*/
                if (qty == 0)
                    viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));
            }
        });

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        viewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPref(context).setDiscountClicked("0");
                int qty = Integer.parseInt(viewHolder.lblQty.getText().toString());
                mSharedPref.setHeaderNextClicked("1");
                viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));

                if (qty < (Double.parseDouble(viewHolder.HoQ.getText().toString()))) {
                    qty = qty + 1;
                    viewHolder.lblQty.setText(qty + "");
                    if (new SharedPref(context).generateOrderId() == new SharedPref(context).getEditOrderId()) {
                        list.get(position).setPREPRODUCT_QTY(viewHolder.lblQty.getText().toString());
                        list.get(position).setPREPRODUCT_BALQTY("0");
                        Log.d(">>>qtysettextplus", ">>>" + (Integer.parseInt(product.getPREPRODUCT_QTY()) + " Balqty" + Integer.parseInt(product.getPREPRODUCT_BALQTY())));

                    } else {
                        list.get(position).setPREPRODUCT_BALQTY("" + (Integer.parseInt(viewHolder.lblQty.getText().toString()) - Integer.parseInt(product.getPREPRODUCT_QTY())));
                        list.get(position).setPREPRODUCT_QTY(viewHolder.lblQty.getText().toString());

                        Log.d(">>>qtysettextplus", ">>>" + (Integer.parseInt(product.getPREPRODUCT_QTY()) + " Balqty" + Integer.parseInt(product.getPREPRODUCT_BALQTY())));
                        Log.d(">>>balqtyinplus", ">>>" + list.get(position).getPREPRODUCT_BALQTY());

                    }
                    ArrayList<OrderDetail> toSaveOrderDetails = new OrderDetailController(context).mUpdatePrsSales(list.get(position));
                    if (new OrderDetailController(context).createOrUpdateOrdDet(toSaveOrderDetails) > 0) {
                        Log.d("ORDER_DETAILS", "Order det saved successfully...");
                    } else {
                        Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
                    }
                } else {
                    Toast.makeText(context, "Exceeds available  stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        viewHolder.btnPlus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        viewHolder.btnPlus.setBackground(context.getResources().getDrawable(R.drawable.icon_plus_d));
                    }
                    break;

                    case MotionEvent.ACTION_UP: {
                        viewHolder.btnPlus.setBackground(context.getResources().getDrawable(R.drawable.icon_plus));
                    }
                    break;
                }
                return false;
            }
        });

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        viewHolder.btnMinus.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        viewHolder.btnMinus.setBackground(context.getResources().getDrawable(R.drawable.icon_minus_d));
                    }
                    break;

                    case MotionEvent.ACTION_UP: {
                        viewHolder.btnMinus.setBackground(context.getResources().getDrawable(R.drawable.icon_minus));
                    }
                    break;
                }
                return false;
            }
        });

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


        //--------------------------------------------------------------------------------------------------------------------------
        viewHolder.lblQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomKeypadDialog keypad = new CustomKeypadDialog(context, false, new CustomKeypadDialog.IOnOkClickListener() {
                    @Override
                    public void okClicked(double value) {
                        double distrStock = Double.parseDouble(product.getPREPRODUCT_QOH());
                        int enteredQty = (int) value;
                        Log.d("<>+++++", "" + distrStock);
                        mSharedPref.setHeaderNextClicked("1");
                        new SharedPref(context).setDiscountClicked("0");

                        if (enteredQty > (int) distrStock) {
                            viewHolder.lblQty.setText("0");
                            Toast.makeText(context, "Exceeds available  stock", Toast.LENGTH_SHORT).show();
                        } else {
                            viewHolder.lblQty.setText(String.valueOf(enteredQty));
                            if (new SharedPref(context).generateOrderId() == new SharedPref(context).getEditOrderId()) {
                                list.get(position).setPREPRODUCT_QTY(viewHolder.lblQty.getText().toString());
                                list.get(position).setPREPRODUCT_BALQTY("0");
                            } else {
                                list.get(position).setPREPRODUCT_BALQTY("" + (Integer.parseInt(viewHolder.lblQty.getText().toString()) - Integer.parseInt(product.getPREPRODUCT_QTY())));
                                list.get(position).setPREPRODUCT_QTY(viewHolder.lblQty.getText().toString());

                                Log.d(">>>balqtyinqtydlg", ">>>" + list.get(position).getPREPRODUCT_BALQTY());

                            }
                            //   have a logic for qty - and qty +

                            ArrayList<OrderDetail> toSaveOrderDetails = new OrderDetailController(context).mUpdatePrsSales(list.get(position));
                            if (new OrderDetailController(context).createOrUpdateOrdDet(toSaveOrderDetails) > 0) {
                                Log.d("ORDER_DETAILS", "Order det saved successfully...");
                            } else {
                                Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
                            }
                        }

                        //*Change colors*//**//*
                        if (Integer.parseInt(viewHolder.lblQty.getText().toString()) > 0) {
                            viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));
                        } else {
                            viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));
                        }

                    }
                });

                keypad.show();

                keypad.setHeader("SELECT QUANTITY");
                keypad.loadValue(Double.parseDouble(product.getPREPRODUCT_QTY()));
                if (Integer.parseInt(viewHolder.lblQty.getText().toString()) > 0) {
                    viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));
                } else {
                    viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));
                }

            }
        });

        return convertView;
    }

    private static class ViewHolder {
        LinearLayout lnStripe;
        TextView itemBonus;
        TextView unit;
        TextView pack;
        TextView ItemName;
        TextView Price;
        TextView HoQ;
        TextView lblQty;
        TextView lblCase;
        ImageButton btnPlus;
        ImageButton btnMinus;

    }
}
