package com.datamation.kfdsfa.view;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.datamation.kfdsfa.R;
import com.datamation.kfdsfa.controller.OrderDetailController;
import com.datamation.kfdsfa.helpers.OrderResponseListener;
import com.datamation.kfdsfa.order.OrderDetailFragment;
import com.datamation.kfdsfa.order.OrderDetailFragmentOld;
import com.datamation.kfdsfa.order.OrderHeaderFragment;
import com.datamation.kfdsfa.order.OrderMainFragment;
import com.datamation.kfdsfa.order.OrderSummaryFragment;
import com.datamation.kfdsfa.utils.CustomViewPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class OrderActivity<extras> extends AppCompatActivity implements OrderResponseListener {
    private OrderMainFragment orderMainFragment;
    private OrderHeaderFragment orderHeaderFragment;
    private OrderDetailFragment orderDetailFragment;
    private OrderSummaryFragment orderSummaryFragment;
    private CustomViewPager viewPager;
//    public Customer selectedDebtor = null;
//    public Customer selectedRetDebtor = null;
//    public Order selectedPreHed = null;
//    public FInvRHed selectedReturnHed = null;
//    public FInvRDet selectedReturnDet = null;
//    public OrderDetail selectedOrderDet = null;
    Context context;
    boolean status = false;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
      //  return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sales);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("SALES ORDER" + "  -  " + getVersionCode());
        context = this;

        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.presale_tab_strip);
        viewPager = (CustomViewPager) findViewById(R.id.presale_viewpager);

     //   slidingTabStrip.setBackgroundColor(getResources().getColor(R.color.theme_color));
        slidingTabStrip.setTextColor(getResources().getColor(android.R.color.black));
        slidingTabStrip.setIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        slidingTabStrip.setDividerColor(getResources().getColor(R.color.half_black));
        slidingTabStrip.setClickable(false);

        PreSalesPagerAdapter adapter = new PreSalesPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        slidingTabStrip.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 2)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TAG_PRE_SUMMARY"));
                else if (position == 0)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TAG_PRE_HEADER"));
                else if (position == 1)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TAG_PRE_DETAILS"));


            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Intent dataIntent = getIntent();
        String From = dataIntent.getStringExtra("From");

        if(From.equals("edit")) {
            status = true;
        }else if(From.equals("new")){
            status = false;
        }

        status = new OrderDetailController(getApplicationContext()).isAnyActiveOrders();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (status){
            viewPager.setCurrentItem(1);
        }else {
            viewPager.setCurrentItem(0);
        }

    }

    @Override
    public void moveBackToFragment(int index) {
        if (index == 0)
        {
            viewPager.setCurrentItem(0);
        }

        if (index == 1)
        {
            viewPager.setCurrentItem(1);
        }

        if (index == 2)
        {
            viewPager.setCurrentItem(2);
        }
    }

    @Override
    public void moveNextToFragment(int index) {
        if (index == 0)
        {
            viewPager.setCurrentItem(0);
        }

        if (index == 1)
        {
            viewPager.setCurrentItem(1);
        }

        if (index == 2)
        {
            viewPager.setCurrentItem(2);
        }
    }

    private class PreSalesPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = {"ORDER HEADER", "ORDER DETAILS", "ORDER SUMMARY"};
      //  private final String[] titles = {"MAIN","HEADER", "ORDER DETAILS", "ORDER SUMMARY"};
      //  private final String[] titles = {"HEADER", "ORDER DETAILS", "ORDER RETURN", "ORDER SUMMARY"};

        public PreSalesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
//                case 0:
//                    if(orderMainFragment == null) orderMainFragment = new OrderMainFragment();
//                    return orderMainFragment;
               case 0:
                    if(orderHeaderFragment == null) orderHeaderFragment = new OrderHeaderFragment();
                    return orderHeaderFragment;
                case 1:
                    if(orderDetailFragment == null) orderDetailFragment = new OrderDetailFragment();
                    return orderDetailFragment;
                case 2:
                    if(orderSummaryFragment == null) orderSummaryFragment = new OrderSummaryFragment();
                    return orderSummaryFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public String getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "0";

    }

}




