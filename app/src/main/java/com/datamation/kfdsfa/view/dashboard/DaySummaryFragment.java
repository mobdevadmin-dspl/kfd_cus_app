package com.datamation.kfdsfa.view.dashboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.api.ApiCllient;
import com.datamation.kfdsfa.api.ApiInterface;
import com.datamation.kfdsfa.controller.AreaController;
import com.datamation.kfdsfa.controller.BankController;
import com.datamation.kfdsfa.controller.BrandController;
import com.datamation.kfdsfa.controller.CompanyDetailsController;
import com.datamation.kfdsfa.controller.DashboardController;
import com.datamation.kfdsfa.controller.FItenrDetController;
import com.datamation.kfdsfa.controller.FreeDebController;
import com.datamation.kfdsfa.controller.FreeDetController;
import com.datamation.kfdsfa.controller.FreeHedController;
import com.datamation.kfdsfa.controller.FreeItemController;
import com.datamation.kfdsfa.controller.FreeMslabController;
import com.datamation.kfdsfa.controller.GroupController;
import com.datamation.kfdsfa.controller.ItemController;
import com.datamation.kfdsfa.controller.ItemLocController;
import com.datamation.kfdsfa.controller.ItemPriceController;
import com.datamation.kfdsfa.controller.LocationsController;
import com.datamation.kfdsfa.controller.OutstandingController;
import com.datamation.kfdsfa.controller.PaymentController;
import com.datamation.kfdsfa.controller.PaymentDetailController;
import com.datamation.kfdsfa.controller.PaymentHeaderController;
import com.datamation.kfdsfa.controller.PushMsgHedDetController;
import com.datamation.kfdsfa.controller.ReasonController;
import com.datamation.kfdsfa.controller.RepDetailsController;
import com.datamation.kfdsfa.controller.RouteController;
import com.datamation.kfdsfa.controller.RouteDetController;
import com.datamation.kfdsfa.controller.TownController;
import com.datamation.kfdsfa.controller.TypeController;
import com.datamation.kfdsfa.dialog.CustomProgressDialog;
import com.datamation.kfdsfa.helpers.NetworkFunctions;
import com.datamation.kfdsfa.helpers.SharedPref;
import com.datamation.kfdsfa.model.BarChartDetail;
import com.datamation.kfdsfa.settings.TaskTypeDownload;
import com.datamation.kfdsfa.utils.NetworkUtil;
import com.datamation.kfdsfa.utils.UtilityContainer;
import com.datamation.kfdsfa.view.ActivityHome;
import com.datamation.kfdsfa.view.OrderActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.round;

public class DaySummaryFragment  extends Fragment {

    private static final String LOG_TAG = DaySummaryFragment.class.getSimpleName();
    private TextView tvDate;

    private TextView tvSalesGross, tvSalesReturn, tvDiscount, tvNetValue, tvTarget,tvProductive,tvNonprdctive;
    private TextView tvDayCredit, tvDayCreditPercentage, tvDayCash, tvDayCashPercentage, tvDayCheque, tvDayChequePercentage;
    //    private TextView tvPreviousCredit, tvPreviousCreditPercentage, tvPreviousCash, tvPreviousCashPercentage, tvPreviousCheque, tvPreviousChequePercentage;
    private TextView tvPreviousCredit, tvPreviousCash, tvPreviousCheque;
    private TextView tvCashTotal, tvChequeTotal;
    private TextView tvTMGross, tvTMNet, tvTMRfree, tvTMDiscount;
    private TextView tvPMGross, tvPMNet, tvPMfree, tvPMDiscount;
    private TextView tvDownload;

    // private CalendarDatePickerDialog calendarDatePickerDialog;
    //    private Calendar nowCalendar;
    private int mYear, mMonth, mDay;
    private long timeInMillis;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aaa", Locale.getDefault());
    private NumberFormat format = NumberFormat.getInstance();
    private ArrayList<Double> targetValues;
    BarChart chart ;
    SharedPref pref;
    ApiInterface apiInterface;
    NetworkFunctions networkFunctions;

//    private DatabaseHandler dbHandler;
//
//    private List<PaymentPinHolder> pinHolders;
//    private List<Outlet> outlets;

    //private DaySummaryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.day_summary_responsive_layout, container, false);

        timeInMillis = System.currentTimeMillis();

        pref = SharedPref.getInstance(getActivity());
        networkFunctions = new NetworkFunctions(getActivity());

        tvTMGross        = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_gross_sale);
        tvTMNet          = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_net_sale);
        tvTMDiscount     = (TextView) rootView.findViewById(R.id.dashboard_tv_card_this_month_discount);


        tvPMGross        = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_gross_sale);
        tvPMNet          = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_net_sale);
        tvPMDiscount     = (TextView) rootView.findViewById(R.id.dashboard_tv_card_prev_month_discount);

        int curYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int curMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int curDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        String curdate = curYear+"-"+ String.format("%02d", curMonth) + "-" + String.format("%02d", curDate);


        double thisMonthDiscount = new DashboardController(getActivity()).getTMDiscounts();
        double preMonthDiscount = new DashboardController(getActivity()).getPMDiscounts();
        double thisMonthTax = new DashboardController(getActivity()).getMonthTax();
        double preMonthTax = new DashboardController(getActivity()).getPMTax();
        double thisMonthAchieve = new DashboardController(getActivity()).getMonthAchievement();
        double preMonthAchieve = new DashboardController(getActivity()).getPMonthAchievement();


        tvTMGross.setText(""+format.format(thisMonthAchieve));
        tvTMNet.setText(""+format.format(thisMonthAchieve-thisMonthDiscount+thisMonthTax));
        tvTMDiscount.setText(""+format.format(thisMonthDiscount));


        tvPMGross.setText(""+format.format(preMonthAchieve));
        tvPMNet.setText(""+format.format(preMonthAchieve-preMonthDiscount+preMonthTax));
        tvPMDiscount.setText(""+format.format(preMonthDiscount));

        //*********************bar chart*******************************************************************************************

        targetValues = new ArrayList<Double>();

        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setGroupingUsed(true);

        chart = rootView.findViewById(R.id.barChart);
        ArrayList monthTvA = new ArrayList();

        chart.setDescription("");

        //-----------kaveesha------------03/11/2020-----------------
        double Jan_Gross_Sale = new DashboardController(getActivity()).getJanGrossSale();
        double Feb_Gross_Sale = new DashboardController(getActivity()).getFebGrossSale();
        double Mar_Gross_Sale = new DashboardController(getActivity()).getMarGrossSale();
        double Apr_Gross_Sale = new DashboardController(getActivity()).getAprGrossSale();
        double May_Gross_Sale = new DashboardController(getActivity()).getMayGrossSale();
        double Jun_Gross_Sale = new DashboardController(getActivity()).getJuneGrossSale();
        double Jul_Gross_Sale = new DashboardController(getActivity()).getJulyGrossSale();
        double Aug_Gross_Sale = new DashboardController(getActivity()).getAugGrossSale();
        double Sep_Gross_Sale = new DashboardController(getActivity()).getSepGrossSale();
        double Oct_Gross_Sale = new DashboardController(getActivity()).getOctGrossSale();
        double Nov_Gross_Sale = new DashboardController(getActivity()).getNovGrossSale();
        double Dec_Gross_Sale = new DashboardController(getActivity()).getDecGrossSale();


        //chart.set
        monthTvA.add(new BarEntry((float)Jan_Gross_Sale, 0));
        monthTvA.add(new BarEntry((float)Feb_Gross_Sale, 1));
        monthTvA.add(new BarEntry((float)Mar_Gross_Sale, 2));
        monthTvA.add(new BarEntry((float)Apr_Gross_Sale, 3));
        monthTvA.add(new BarEntry((float)May_Gross_Sale, 4));
        monthTvA.add(new BarEntry((float)Jun_Gross_Sale, 5));
        monthTvA.add(new BarEntry((float)Jul_Gross_Sale, 6));
        monthTvA.add(new BarEntry((float)Aug_Gross_Sale, 7));
        monthTvA.add(new BarEntry((float)Sep_Gross_Sale, 8));
        monthTvA.add(new BarEntry((float)Oct_Gross_Sale, 9));
        monthTvA.add(new BarEntry((float)Nov_Gross_Sale, 10));
        monthTvA.add(new BarEntry((float)Dec_Gross_Sale, 11));

        ArrayList titl = new ArrayList();
        titl.add("Jan");
        titl.add("Feb");
        titl.add("Mar");
        titl.add("Apr");
        titl.add("May");
        titl.add("Jun");
        titl.add("Jul");
        titl.add("Aug");
        titl.add("Sep");
        titl.add("Oct");
        titl.add("Nov");
        titl.add("Dec");



        BarDataSet bardataset = new BarDataSet(monthTvA, "values");
        //  bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        bardataset.setColors(new int[]{ContextCompat.getColor(getActivity(), R.color.achievecolor),
                ContextCompat.getColor(getActivity(), R.color.achievecolor),
                ContextCompat.getColor(getActivity(), R.color.achievecolor),
                ContextCompat.getColor(getActivity(), R.color.achievecolor),
                ContextCompat.getColor(getActivity(), R.color.achievecolor),
                ContextCompat.getColor(getActivity(), R.color.achievecolor),
                ContextCompat.getColor(getActivity(), R.color.achievecolor),
                ContextCompat.getColor(getActivity(), R.color.achievecolor),
                ContextCompat.getColor(getActivity(), R.color.achievecolor),
                ContextCompat.getColor(getActivity(), R.color.achievecolor),
                ContextCompat.getColor(getActivity(), R.color.achievecolor),
                ContextCompat.getColor(getActivity(), R.color.achievecolor)});
        chart.animateY(2000);
        chart.setDrawGridBackground(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        // chart.xAxis.isEnabled = false;
        //chart.getXAxis().setEnabled(false);

        //chart.getAxisLeft().setDrawAxisLine(false);
        BarData data = new BarData(titl, bardataset);
        bardataset.setBarSpacePercent(15f);
        chart.setData(data);

        return rootView;
    }

}


