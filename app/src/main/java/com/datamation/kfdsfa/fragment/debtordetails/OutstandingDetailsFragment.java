package com.datamation.kfdsfa.fragment.debtordetails;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.controller.CustomerController;
import com.datamation.kfdsfa.helpers.DatabaseHelper;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.FddbNote;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.rgb;
import static android.text.format.DateUtils.DAY_IN_MILLIS;


public class OutstandingDetailsFragment extends Fragment
{

    private DatabaseHelper dbHandler;
    private String debtorCode;
    OutletOutstandingAdapter arrayAdapter;
    ArrayList<FddbNote>historyDetail;
    

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outstanding_details, container, false);

        ListView listView = (ListView) view.findViewById(R.id.outlet_details_outstanding_listview);
        SharedPref pref = SharedPref.getInstance(getActivity());


        try {

            debtorCode = pref.getSelectedDebCode();

            if(debtorCode != null) {
                historyDetail = new CustomerController(getActivity()).getOutStandingList(debtorCode);
                arrayAdapter = new OutletOutstandingAdapter(getActivity(), historyDetail);
                listView.setAdapter(arrayAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        return view;
    }

    public void refreshValues() {
//        if(adapter != null && outlet != null) {
//            historyDetails = dbHandler.getOutstandingPayments(outlet.getOutletId());
//            adapter.setHistoryDetails(historyDetails);
//        }
    }

    private static class ViewHolder {
        LinearLayout layout;
        com.datamation.kfdsfa.utils.CustomFont invoiceId;
        com.datamation.kfdsfa.utils.CustomFont invoiceDate;
        com.datamation.kfdsfa.utils.CustomFont invoiceAmt;
        com.datamation.kfdsfa.utils.CustomFont invoiceBalance;
        com.datamation.kfdsfa.utils.CustomFont invoiceDays;

    }


    private class OutletOutstandingAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        private List<FddbNote> fddbNotes;

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        private NumberFormat numberFormat = NumberFormat.getInstance();

        private OutletOutstandingAdapter(Context context, List<FddbNote> historyDetails) {
            this.inflater = LayoutInflater.from(context);
            this.fddbNotes = historyDetails;

        }

        @Override
        public int getCount() {
            if(fddbNotes != null) return fddbNotes.size();
            return 0;
        }

        @Override
        public FddbNote getItem(int position) {
            return fddbNotes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return fddbNotes.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row_fddnote_details, parent, false);
                viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.row_layout);
                viewHolder.invoiceId = (com.datamation.kfdsfa.utils.CustomFont) convertView.findViewById(R.id.row_refno);
                viewHolder.invoiceDate = (com.datamation.kfdsfa.utils.CustomFont) convertView.findViewById(R.id.row_txndate);
                viewHolder.invoiceAmt = (com.datamation.kfdsfa.utils.CustomFont) convertView.findViewById(R.id.row_dueAmt);
                viewHolder.invoiceBalance = (com.datamation.kfdsfa.utils.CustomFont) convertView.findViewById(R.id.row_Amt);
                viewHolder.invoiceDays = (com.datamation.kfdsfa.utils.CustomFont) convertView.findViewById(R.id.days);

//                convertView.findViewById(R.id.item_payment_invoice_checkbox).setVisibility(View.INVISIBLE);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (position % 2 == 1) {
                convertView.setBackgroundColor(Color.WHITE);
            } else {
                convertView.setBackgroundColor(rgb(173,230,216));
            }


//            HistoryDetail detail = historyDetails.get(position);
//            Invoice invoice = detail.getInvoice();

            if(fddbNotes != null) {

                viewHolder.invoiceId.setText(fddbNotes.get(position).getRefNo());
                viewHolder.invoiceDate.setText(fddbNotes.get(position).getTxnDate());
                viewHolder.invoiceAmt.setText(fddbNotes.get(position).getAmt());
                viewHolder.invoiceBalance.setText(fddbNotes.get(position).getAmt());



                Date date;
                long txn = 0;
                try {
                    date = (Date)formatter.parse(fddbNotes.get(position).getTxnDate());
                    System.out.println("receipt date is " +date.getTime());
                    txn = date.getTime();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int numOfDays =   (int) ((System.currentTimeMillis()  - txn) / DAY_IN_MILLIS);
                //viewHolder.lblDays.setText(""+numOfDays);
                viewHolder.invoiceDays.setText(""+numOfDays);
                if(fddbNotes.get(position).getCreditPeriod() != null && !fddbNotes.get(position).getCreditPeriod().isEmpty())
                {
                    if(Integer.parseInt(fddbNotes.get(position).getCreditPeriod())<numOfDays){
                        //if(90<numOfDays){
                        viewHolder.invoiceId.setTextColor(getResources().getColor(R.color.red_btn_bg_color));
                        viewHolder.invoiceId.setTypeface(viewHolder.invoiceId.getTypeface(), Typeface.BOLD);
                        viewHolder.invoiceDate.setTextColor(getResources().getColor(R.color.red_btn_bg_color));
                        viewHolder.invoiceDate.setTypeface(viewHolder.invoiceDate.getTypeface(), Typeface.BOLD);
                        viewHolder.invoiceAmt.setTextColor(getResources().getColor(R.color.red_btn_bg_color));
                        viewHolder.invoiceAmt.setTypeface(viewHolder.invoiceAmt.getTypeface(), Typeface.BOLD);
                        viewHolder.invoiceBalance.setTextColor(getResources().getColor(R.color.red_btn_bg_color));
                        viewHolder.invoiceBalance.setTypeface(viewHolder.invoiceBalance.getTypeface(), Typeface.BOLD);
                        viewHolder.invoiceDays.setTextColor(getResources().getColor(R.color.red_btn_bg_color));
                        viewHolder.invoiceDays.setTypeface(viewHolder.invoiceDays.getTypeface(), Typeface.BOLD);
                    }else{
//default colours and typeface will be set
                    }
                }
            }

            return convertView;
        }

    }

}
