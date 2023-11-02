package com.datamation.kfdsfa.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datamation.kfdsfa.R;

import com.datamation.kfdsfa.controller.FreeMslabController;
import com.datamation.kfdsfa.controller.ItemController;
import com.datamation.kfdsfa.controller.OrderDetailController;
import com.datamation.kfdsfa.dialog.CustomKeypadDialog;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.OrderDetail;
import com.datamation.kfdsfa.model.PreProduct;
import com.datamation.kfdsfa.model.Product;

import java.util.ArrayList;

public class PreOrderRecycleAdapter extends RecyclerView.Adapter<PreProduct1> {
    LayoutInflater inflater;
    Context context;
    ArrayList<PreProduct> list;
    String preText = null;
    public SharedPref mSharedPref;


    public PreOrderRecycleAdapter(Context context, final ArrayList<PreProduct> list) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.mSharedPref = SharedPref.getInstance(context);
    }

    @Override
    public PreProduct1 onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.row_product_item_responsive_layout, viewGroup, false);

        return new PreProduct1(itemView);
    }

    @Override
    public void onBindViewHolder(final PreProduct1 holder, final int position) {
        final PreProduct product = list.get(position);

        holder.pack.setText(product.getPREPRODUCT_PACK());
        holder.ItemName.setText(product.getPREPRODUCT_ITEMCODE() + " : " + product.getPREPRODUCT_ITEMNAME());
        holder.Price.setText(product.getPREPRODUCT_PRICE());
        holder.HoQ.setText(product.getPREPRODUCT_QOH());
        holder.lblQty.setText(product.getPREPRODUCT_QTY());
        holder.lblCase.setText(product.getPREPRODUCT_CASE());
        holder.itemBonus.setText(product.getPREPRODUCT_Bonus());

        /*Change colors*/
        if (Double.parseDouble(holder.lblQty.getText().toString()) > 0)
            holder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));
        else
            holder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setPREPRODUCT_Bonus(new FreeMslabController(context).getFreeDetails(list.get(position).getPREPRODUCT_ITEMCODE(), mSharedPref.getSelectedDebCode()));
                String freestructure = list.get(position).getPREPRODUCT_Bonus();

                if (freestructure.equals("")) {

                    holder.itemBonus.setText("*NO BONUS*");
                } else {
                    holder.itemBonus.setText(freestructure);

                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
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

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPref(context).setDiscountClicked("0");
                int qty = Integer.parseInt(holder.lblQty.getText().toString());
                qty = qty - 1;
                if (qty >= 0) {
                    holder.lblQty.setText(qty + "");
                } else {
                    qty = Integer.parseInt(holder.lblQty.getText().toString());
                    Toast.makeText(context, "Cannot allow minus values", Toast.LENGTH_SHORT).show();
                    holder.lblQty.setText(holder.lblQty.getText().toString());
                }
                if ((new SharedPref(context).generateOrderId() == new SharedPref(context).getEditOrderId())) {

                    list.get(position).setPREPRODUCT_QTY(holder.lblQty.getText().toString());
                    list.get(position).setPREPRODUCT_BALQTY("0");

                } else {
                    list.get(position).setPREPRODUCT_BALQTY("" + (Integer.parseInt(holder.lblQty.getText().toString()) - Integer.parseInt(product.getPREPRODUCT_QTY())));
                    list.get(position).setPREPRODUCT_QTY(holder.lblQty.getText().toString());

                    qty = Integer.parseInt(holder.lblQty.getText().toString());

                }
                ArrayList<OrderDetail> toSaveOrderDetails = new OrderDetailController(context).mUpdatePrsSales(list.get(position));
                if (new OrderDetailController(context).createOrUpdateOrdDet(toSaveOrderDetails) > 0) {
                    Log.d("ORDER_DETAILS", "Order det saved successfully...");
                } else {
                    Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
                }
                /*Change colors*/
                if (qty == 0)
                    holder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));
            }
        });

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPref(context).setDiscountClicked("0");
                int qty = Integer.parseInt(holder.lblQty.getText().toString());
                mSharedPref.setHeaderNextClicked("1");
                holder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));

                if (qty < (Double.parseDouble(holder.HoQ.getText().toString()))) {
                    qty = qty + 1;
//                    holder.itemBonus.setText(qty+"");
                    holder.lblQty.setText(qty + "");
                    if (new SharedPref(context).generateOrderId() == new SharedPref(context).getEditOrderId()) {
                        list.get(position).setPREPRODUCT_QTY(holder.lblQty.getText().toString());
                        list.get(position).setPREPRODUCT_BALQTY("0");
                        Log.d(">>>qtysettextplus", ">>>" + (Integer.parseInt(product.getPREPRODUCT_QTY()) + " Balqty" + Integer.parseInt(product.getPREPRODUCT_BALQTY())));

                    } else {
                        list.get(position).setPREPRODUCT_BALQTY("" + (Integer.parseInt(holder.lblQty.getText().toString()) - Integer.parseInt(product.getPREPRODUCT_QTY())));
                        list.get(position).setPREPRODUCT_QTY(holder.lblQty.getText().toString());

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

        holder.btnPlus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        holder.btnPlus.setBackground(context.getResources().getDrawable(R.drawable.icon_plus_d));
                    }
                    break;

                    case MotionEvent.ACTION_UP: {
                        holder.btnPlus.setBackground(context.getResources().getDrawable(R.drawable.icon_plus));
                    }
                    break;
                }
                return false;
            }
        });

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        holder.btnMinus.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        holder.btnMinus.setBackground(context.getResources().getDrawable(R.drawable.icon_minus_d));
                    }
                    break;

                    case MotionEvent.ACTION_UP: {
                        holder.btnMinus.setBackground(context.getResources().getDrawable(R.drawable.icon_minus));
                    }
                    break;
                }
                return false;
            }
        });

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


        //--------------------------------------------------------------------------------------------------------------------------
        holder.lblQty.setOnClickListener(new View.OnClickListener() {
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
                            holder.lblQty.setText("0");
                            Toast.makeText(context, "Exceeds available  stock", Toast.LENGTH_SHORT).show();
                        } else {
                            holder.lblQty.setText(String.valueOf(enteredQty));
                            if (new SharedPref(context).generateOrderId() == new SharedPref(context).getEditOrderId()) {
                                list.get(position).setPREPRODUCT_QTY(holder.lblQty.getText().toString());
                                list.get(position).setPREPRODUCT_BALQTY("0");
                            } else {
                                list.get(position).setPREPRODUCT_BALQTY("" + (Integer.parseInt(holder.lblQty.getText().toString()) - Integer.parseInt(product.getPREPRODUCT_QTY())));
                                list.get(position).setPREPRODUCT_QTY(holder.lblQty.getText().toString());

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
                        if (Integer.parseInt(holder.lblQty.getText().toString()) > 0) {
                            holder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));
                        } else {
                            holder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));
                        }

                    }
                });

                keypad.show();

                keypad.setHeader("SELECT QUANTITY");
                keypad.loadValue(Double.parseDouble(product.getPREPRODUCT_QTY()));
                if (Integer.parseInt(holder.lblQty.getText().toString()) > 0) {
                    holder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));
                } else {
                    holder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


}


class PreProduct1 extends RecyclerView.ViewHolder {
    LinearLayout lnStripe;
    TextView itemBonus, pack, ItemName, Price, HoQ, lblQty, lblCase;
    ImageButton btnPlus, btnMinus;


    public PreProduct1(View itemView) {
        super(itemView);

        lnStripe = (LinearLayout) itemView.findViewById(R.id.lnProductStripe);
        itemBonus = (TextView) itemView.findViewById(R.id.row_bonus);
        pack = (TextView) itemView.findViewById(R.id.row_pack);
        ItemName = (TextView) itemView.findViewById(R.id.row_itemname);
        Price = (TextView) itemView.findViewById(R.id.row_price);
        HoQ = (TextView) itemView.findViewById(R.id.row_qoh);
        lblQty = (TextView) itemView.findViewById(R.id.et_qty);
        lblCase = (TextView) itemView.findViewById(R.id.et_case);
        btnPlus = (ImageButton) itemView.findViewById(R.id.btnAddition);
        btnMinus = (ImageButton) itemView.findViewById(R.id.btnSubtract);

    }
}

