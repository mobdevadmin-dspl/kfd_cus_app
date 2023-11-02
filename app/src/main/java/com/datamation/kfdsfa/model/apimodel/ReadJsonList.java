package com.datamation.kfdsfa.model.apimodel;

import com.datamation.kfdsfa.model.Bank;
import com.datamation.kfdsfa.model.CompanyBranch;
import com.datamation.kfdsfa.model.CompanySetting;
import com.datamation.kfdsfa.model.Control;
import com.datamation.kfdsfa.model.DbNames;
import com.datamation.kfdsfa.model.Debtor;
import com.datamation.kfdsfa.model.Discdeb;
import com.datamation.kfdsfa.model.Discdet;
import com.datamation.kfdsfa.model.Disched;
import com.datamation.kfdsfa.model.Discslab;
import com.datamation.kfdsfa.model.Expense;
import com.datamation.kfdsfa.model.FInvhedL3;
import com.datamation.kfdsfa.model.FItenrDet;
import com.datamation.kfdsfa.model.FItenrHed;
import com.datamation.kfdsfa.model.FddbNote;
import com.datamation.kfdsfa.model.FinvDetL3;
import com.datamation.kfdsfa.model.FreeDeb;
import com.datamation.kfdsfa.model.FreeDet;
import com.datamation.kfdsfa.model.FreeHed;
import com.datamation.kfdsfa.model.FreeItem;
import com.datamation.kfdsfa.model.FreeMslab;
import com.datamation.kfdsfa.model.FreeSlab;
import com.datamation.kfdsfa.model.IsConfirm;
import com.datamation.kfdsfa.model.Item;
import com.datamation.kfdsfa.model.ItemBundle;
import com.datamation.kfdsfa.model.ItemLoc;
import com.datamation.kfdsfa.model.ItemPri;
import com.datamation.kfdsfa.model.ItenrDeb;
import com.datamation.kfdsfa.model.Locations;
import com.datamation.kfdsfa.model.NearDebtor;
import com.datamation.kfdsfa.model.Reason;
import com.datamation.kfdsfa.model.Route;
import com.datamation.kfdsfa.model.RouteDet;
import com.datamation.kfdsfa.model.SalRep;
import com.datamation.kfdsfa.model.SalesPrice;
import com.datamation.kfdsfa.model.Tax;
import com.datamation.kfdsfa.model.TaxDet;
import com.datamation.kfdsfa.model.TaxHed;
import com.datamation.kfdsfa.model.Town;
import com.datamation.kfdsfa.model.VatMaster;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReadJsonList {

    @SerializedName("GetdatabaseNamesResult")//01GetdatabaseNamesResult
    private ArrayList<DbNames> dbResult = null;
    @SerializedName("fSalRepResult")//01
    private ArrayList<SalRep> salRepResult = null;
    @SerializedName("fControlResult")//02
    private ArrayList<Control> controlResult = null;
    @SerializedName("fItemLocResult")//03
    private ArrayList<ItemLoc> itemLocResult = null;
    @SerializedName("fItemPriResult")//05
    private ArrayList<ItemPri> itemPriResult = null ;
    @SerializedName("fItemsResult")//06
    private ArrayList<Item> itemsResult = null ;
    @SerializedName("fLocationsResult")//07
    private ArrayList<Locations> locationsResult = null ;
    @SerializedName("fTaxResult")//08
    private ArrayList<Tax> taxResult = null ;
    @SerializedName("fTaxHedResult")//09
    private ArrayList<TaxHed> taxHedResult = null ;
    @SerializedName("fTaxDetResult")//10
    private ArrayList<TaxDet> taxDetResult = null ;
    @SerializedName("FnearDebtorResult")//11
    private ArrayList<NearDebtor> nearDebtorResult = null ;
    @SerializedName("FCompanyBranchResult")//12
    private ArrayList<CompanyBranch> companyBranchResult = null ;
    @SerializedName("fCompanySettingResult")//13
    private ArrayList<CompanySetting> companySettingResult = null ;
    @SerializedName("fReasonResult")//14
    private ArrayList<Reason> reasonResult = null ;
    @SerializedName("fExpenseResult")//15
    private ArrayList<Expense> expenseResult = null ;
    @SerializedName("FfreeslabResult")//16
    private ArrayList<FreeSlab> freeSlabResult = null ;
    @SerializedName("FfreedetResult")//17
    private ArrayList<FreeDet> freeDetResult = null ;
    @SerializedName("FfreedebResult")//18
    private ArrayList<FreeDeb> freeDebResult = null ;
    @SerializedName("fFreeItemResult")//19
    private ArrayList<FreeItem> freeItemResult = null ;
    @SerializedName("FfreehedResult")//20
    private ArrayList<FreeHed> freeHedResult = null ;
    @SerializedName("fFreeMslabResult")//21
    private ArrayList<FreeMslab> freeMslabResult = null ;
    @SerializedName("fbankResult")//22
    private ArrayList<Bank> bankResult = null ;
    @SerializedName("FdiscdetResult")//23
    private ArrayList<Discdet> discDetResult = null ;
    @SerializedName("FdiscslabResult")//24
    private ArrayList<Discslab> discSlabResult = null ;
    @SerializedName("FdiscdebResult")//25
    private ArrayList<Discdeb> discDebResult = null ;
    @SerializedName("FDischedResult")//26
    private ArrayList<Disched> discHedResult = null ;
    @SerializedName("fTownResult")//27
    private ArrayList<Town> townResult = null ;
    @SerializedName("fRouteResult")//28
    private ArrayList<Route> routeResult = null ;
    @SerializedName("fRouteDetResult")//29
    private ArrayList<RouteDet> routeDetResult = null ;
    @SerializedName("fItenrHedResult")//30
    private ArrayList<FItenrHed> itenrHedResult = null ;
    @SerializedName("fItenrDetResult")//31
    private ArrayList<FItenrDet> itenrDetResult = null ;
    @SerializedName("RepLastThreeInvDetResult")//32
    private ArrayList<FinvDetL3> lastThreeInvDetResult = null ;
    @SerializedName("RepLastThreeInvHedResult")//33
    private ArrayList<FInvhedL3> lastThreeInvHedResult = null ;
    @SerializedName("fDdbNoteWithConditionResult")//34
    private ArrayList<FddbNote> outstandingResult = null ;
    @SerializedName("FdebtorResult")//35
    private ArrayList<Debtor> debtorResult = null ;
    @SerializedName("BundleBarCodeResult")//36
    private ArrayList<ItemBundle> itemBundleResult = null ;
    @SerializedName("VATMasterResult")//37
    private ArrayList<VatMaster> vatMasterList = null ;
    @SerializedName("fIteDebDetResult")//38
    private ArrayList<ItenrDeb> iteaneryDebList = null ;
    @SerializedName("SalesPriceResult")//39
    private ArrayList<SalesPrice> salesPriceResult = null ;
    @SerializedName("CusProductDisResult")//39
    private ArrayList<SalesPrice> discountResult = null ;
    @SerializedName("IsOrderConfirmedResult")//39
    private ArrayList<IsConfirm> confirmResult = null ;

    public ArrayList<IsConfirm> getConfirmResult() {
        return confirmResult;
    }

    public ArrayList<DbNames> getDbResult() {
        return dbResult;
    }

    public ArrayList<SalRep> getSalRepResult() {
        return salRepResult;
    }

    public ArrayList<Control> getControlResult() {
        return controlResult;
    }

    public ArrayList<ItemLoc> getItemLocResult() {
        return itemLocResult;
    }

    public ArrayList<ItemPri> getItemPriResult() {
        return itemPriResult;
    }

    public ArrayList<Item> getItemsResult() {
        return itemsResult;
    }

    public ArrayList<Locations> getLocationsResult() {
        return locationsResult;
    }

    public ArrayList<Tax> getTaxResult() {
        return taxResult;
    }

    public ArrayList<TaxHed> getTaxHedResult() {
        return taxHedResult;
    }

    public ArrayList<TaxDet> getTaxDetResult() {
        return taxDetResult;
    }

    public ArrayList<NearDebtor> getNearDebtorResult() {
        return nearDebtorResult;
    }

    public ArrayList<CompanyBranch> getCompanyBranchResult() {
        return companyBranchResult;
    }

    public ArrayList<CompanySetting> getCompanySettingResult() {
        return companySettingResult;
    }

    public ArrayList<Reason> getReasonResult() {
        return reasonResult;
    }

    public ArrayList<Expense> getExpenseResult() {
        return expenseResult;
    }

    public ArrayList<FreeSlab> getFreeSlabResult() {
        return freeSlabResult;
    }

    public ArrayList<FreeDet> getFreeDetResult() {
        return freeDetResult;
    }

    public ArrayList<FreeDeb> getFreeDebResult() {
        return freeDebResult;
    }

    public ArrayList<FreeItem> getFreeItemResult() {
        return freeItemResult;
    }

    public ArrayList<FreeHed> getFreeHedResult() {
        return freeHedResult;
    }

    public ArrayList<FreeMslab> getFreeMslabResult() {
        return freeMslabResult;
    }

    public ArrayList<Bank> getBankResult() {
        return bankResult;
    }

    public ArrayList<Discdet> getDiscDetResult() {
        return discDetResult;
    }

    public ArrayList<Discslab> getDiscSlabResult() {
        return discSlabResult;
    }

    public ArrayList<Discdeb> getDiscDebResult() {
        return discDebResult;
    }

    public ArrayList<Disched> getDiscHedResult() {
        return discHedResult;
    }

    public ArrayList<Town> getTownResult() {
        return townResult;
    }

    public ArrayList<Route> getRouteResult() {
        return routeResult;
    }

    public ArrayList<RouteDet> getRouteDetResult() {
        return routeDetResult;
    }

    public ArrayList<FItenrHed> getItenrHedResult() {
        return itenrHedResult;
    }

    public ArrayList<FItenrDet> getItenrDetResult() {
        return itenrDetResult;
    }

    public ArrayList<FinvDetL3> getLastThreeInvDetResult() {
        return lastThreeInvDetResult;
    }

    public ArrayList<FInvhedL3> getLastThreeInvHedResult() {
        return lastThreeInvHedResult;
    }

    public ArrayList<FddbNote> getOutstandingResult() {
        return outstandingResult;
    }

    public ArrayList<Debtor> getDebtorResult() {
        return debtorResult;
    }


    public ArrayList<ItemBundle> getItemBundleResult() {
        return itemBundleResult;
    }

    public ArrayList<VatMaster> getVatMasterList() {
        return vatMasterList;
    }

    public ArrayList<ItenrDeb> getIteaneryDebList() {
        return iteaneryDebList;
    }

    public ArrayList<SalesPrice> getSalesPriceResult() {
        return salesPriceResult;
    }

    public ArrayList<SalesPrice> getDiscountResult() {
        return discountResult;
    }
}

