package com.user.maevis.models;

import com.user.maevis.ListItem;
import com.user.maevis.ListItemVerified;
import com.user.maevis.UserItem;

/**
 * Created by User on 1/16/2018.
 */

public class PageNavigationManager {
    private static ListItemVerified clickedTabHomeListItemVerified;
    private static ListItemVerified clickedTabLocListItemVerified;
    private static ListItem clickedTabLocListItemPending; //
    private static ListItem clickedTabNotifListItem;
    private static ListItemVerified clickedTabNotifRegListItem; //
    private static UserItem clickedReportPageUserItem;
    private static UserItem clickedVerifyReportUserItem;
    private static UserItem clickedUMBlockUserItem;

    public static final String KEY_TABHOME = "TAB_HOME";
    public static final String KEY_TABLOCVERIFIED = "TAB_LOCVERIFIED";
    public static final String KEY_TABLOCPENDING = "TAB_LOCPENDING";
    public static final String KEY_TABNOTIF = "TAB_NOTIF";
    public static final String KEY_REPORTPAGE = "REPORTPAGE";
    public static final String KEY_VERIFYREPORT = "VERIFYREPORT";
    public static final String KEY_UM = "UM";

    public static String fromTab = "";
    public static String clickedUserID = "";
    public static ListItem listItemTemp = null;
    public static ListItemVerified listItemVerifiedTemp = null;
    public static ListItemVerified userItemTemp = null;


    /*public static void markTab(String clickedUserID, String tab, ListItem listItemTemp, ListItemVerified listItemVerifiedTemp, UserItem userItemTemp) {
        PageNavigationManager.setClickedUserID(clickedUserID);

        switch(tab) {
            case KEY_TABHOME:
                PageNavigationManager.setFromTab(KEY_TABHOME);
                PageNavigationManager.setClickedTabHomeListItemVerified(listItemVerifiedTemp);
                break;
            case KEY_TABLOCVERIFIED:
                PageNavigationManager.setFromTab(KEY_TABLOCVERIFIED);
                PageNavigationManager.setClickedTabLocListItemVerified(listItemVerifiedTemp);
                break;
            case KEY_TABLOCPENDING:
                PageNavigationManager.setFromTab(KEY_TABLOCPENDING);
                PageNavigationManager.setClickedTabLocListItemPending(listItemTemp);
                break;
            case KEY_TABNOTIF:
                PageNavigationManager.setFromTab(KEY_TABNOTIF);
                PageNavigationManager.setClickedTabNotifListItem(listItemTemp);
                break;
            case KEY_REPORTPAGE:
                PageNavigationManager.setFromTab(KEY_REPORTPAGE);
                PageNavigationManager.setClickedReportPageUserItem(userItemTemp);
                break;
            case KEY_VERIFYREPORT:
                PageNavigationManager.setFromTab(KEY_VERIFYREPORT);
                PageNavigationManager.setClickedVerifyReportUserItem(userItemTemp);
                break;
            case KEY_UM: PageNavigationManager.setFromTab(KEY_UM);
                PageNavigationManager.setClickedUMBlockUserItem(userItemTemp);
                break;
        }
    }*/

    public static void markTab(String clickedUserID, String tab, ListItemVerified listItemVerifiedTemp) {
        PageNavigationManager.setClickedUserID(clickedUserID);

        switch(tab) {
            case KEY_TABHOME:
                PageNavigationManager.setFromTab(KEY_TABHOME);
                PageNavigationManager.setClickedTabHomeListItemVerified(listItemVerifiedTemp);
                break;
            case KEY_TABLOCVERIFIED:
                PageNavigationManager.setFromTab(KEY_TABLOCVERIFIED);
                PageNavigationManager.setClickedTabLocListItemVerified(listItemVerifiedTemp);
                break;
        }
    }

    public static void markTab(String clickedUserID, String tab, ListItem listItemTemp) {
        PageNavigationManager.setClickedUserID(clickedUserID);

        switch(tab) {
            case KEY_TABLOCPENDING:
                PageNavigationManager.setFromTab(KEY_TABLOCPENDING);
                PageNavigationManager.setClickedTabLocListItemPending(listItemTemp);
                break;
            case KEY_TABNOTIF:
                PageNavigationManager.setFromTab(KEY_TABNOTIF);
                PageNavigationManager.setClickedTabNotifListItem(listItemTemp);
                break;
            case KEY_REPORTPAGE:
        }
    }

    public static void markTab(String clickedUserID, String tab, UserItem userItemTemp) {
        PageNavigationManager.setClickedUserID(clickedUserID);

        switch(tab) {
            case KEY_REPORTPAGE:
                PageNavigationManager.setFromTab(KEY_REPORTPAGE);
                PageNavigationManager.setClickedReportPageUserItem(userItemTemp);
                break;
            case KEY_VERIFYREPORT:
                PageNavigationManager.setFromTab(KEY_VERIFYREPORT);
                PageNavigationManager.setClickedVerifyReportUserItem(userItemTemp);
                break;
            case KEY_UM: PageNavigationManager.setFromTab(KEY_UM);
                PageNavigationManager.setClickedUMBlockUserItem(userItemTemp);
                break;
        }
    }

    public static void initializePageNavigationManager() {
        clickedTabHomeListItemVerified = null;
        clickedTabLocListItemVerified = null;
        clickedTabLocListItemPending = null;
        clickedTabNotifListItem = null;
        clickedTabNotifRegListItem = null;
        clickedReportPageUserItem = null;
        clickedVerifyReportUserItem = null;
        clickedUMBlockUserItem = null;
    }

    public static void clickTabHomeListItemVerified(ListItemVerified listItemVerified) {
        resetClickedPages();
        clickedTabHomeListItemVerified = listItemVerified;
    }

    public static void clickTabLocListItemVerified(ListItemVerified listItemVerified) {
        resetClickedPages();
        clickedTabLocListItemVerified = listItemVerified;
    }

    public static void clickTabLocListItemPending(ListItem listItem) {
        resetClickedPages();
        clickedTabLocListItemPending = listItem;
    }

    public static void clickTabNotifListItem(ListItem listItem) {
        resetClickedPages();
        clickedTabNotifListItem = listItem;
    }

    public static void clickReportPageUserItem(UserItem userItem) {
        resetClickedPages();
        clickedReportPageUserItem = userItem;
    }

    public static void clickTabNotifRegListItem(ListItemVerified listItemVerified) {
        resetClickedPages();
        clickedTabNotifRegListItem = listItemVerified;
    }

    public static void clickVerifyReportUserItem(UserItem userItem) {
        resetClickedPages();
        clickedVerifyReportUserItem = userItem;
    }

    public static void clickUMBlockUserItem(UserItem userItem) {
        resetClickedPages();
        clickedUMBlockUserItem = userItem;
    }

    public static void resetClickedPages() {
        clickedTabHomeListItemVerified = null;
        clickedTabLocListItemVerified = null;
        clickedTabLocListItemPending = null;
        clickedTabNotifListItem = null;
        clickedTabNotifRegListItem = null;
        clickedReportPageUserItem = null;
        clickedVerifyReportUserItem = null;
        clickedUMBlockUserItem = null;
    }

    public static ListItemVerified getClickedTabHomeListItemVerified() {
        return clickedTabHomeListItemVerified;
    }

    public static ListItemVerified getClickedTabLocListItemVerified() {
        return clickedTabLocListItemVerified;
    }

    public static ListItem getClickedTabLocListItemPending() {
        return clickedTabLocListItemPending;
    }

    public static ListItem getClickedTabNotifListItem() {
        return clickedTabNotifListItem;
    }

    public static ListItemVerified getClickedTabNotifRegListItem() {
        return clickedTabNotifRegListItem;
    }

    public static UserItem getClickedReportPageUserItem() {
        return clickedReportPageUserItem;
    }

    public static UserItem getClickedVerifyReportUserItem() {
        return clickedVerifyReportUserItem;
    }

    public static UserItem getClickedUMBlockUserItem() {
        return clickedUMBlockUserItem;
    }

    public static void setClickedTabHomeListItemVerified(ListItemVerified clickedTabHomeListItemVerified) {
        PageNavigationManager.clickedTabHomeListItemVerified = clickedTabHomeListItemVerified;
    }

    public static void setClickedTabLocListItemVerified(ListItemVerified clickedTabLocListItemVerified) {
        PageNavigationManager.clickedTabLocListItemVerified = clickedTabLocListItemVerified;
    }

    public static void setClickedTabLocListItemPending(ListItem clickedTabLocListItemPending) {
        PageNavigationManager.clickedTabLocListItemPending = clickedTabLocListItemPending;
    }

    public static void setClickedTabNotifListItem(ListItem clickedTabNotifListItem) {
        PageNavigationManager.clickedTabNotifListItem = clickedTabNotifListItem;
    }

    public static void setClickedTabNotifRegListItem(ListItemVerified clickedTabNotifRegListItem) {
        PageNavigationManager.clickedTabNotifRegListItem = clickedTabNotifRegListItem;
    }

    public static void setClickedReportPageUserItem(UserItem clickedReportPageUserItem) {
        PageNavigationManager.clickedReportPageUserItem = clickedReportPageUserItem;
    }

    public static void setClickedVerifyReportUserItem(UserItem clickedVerifyReportUserItem) {
        PageNavigationManager.clickedVerifyReportUserItem = clickedVerifyReportUserItem;
    }

    public static void setClickedUMBlockUserItem(UserItem clickedUMBlockUserItem) {
        PageNavigationManager.clickedUMBlockUserItem = clickedUMBlockUserItem;
    }

    public static String getFromTab() {
        return fromTab;
    }

    public static void setFromTab(String fromTab) {
        PageNavigationManager.fromTab = fromTab;
    }

    public static String getClickedUserID() {
        return clickedUserID;
    }

    public static void setClickedUserID(String clickedUserID) {
        PageNavigationManager.clickedUserID = clickedUserID;
    }

    public static ListItem getListItemTemp() {
        return listItemTemp;
    }

    public static void setListItemTemp(ListItem listItemTemp) {
        PageNavigationManager.listItemTemp = listItemTemp;
    }

    public static ListItemVerified getListItemVerifiedTemp() {
        return listItemVerifiedTemp;
    }

    public static void setListItemVerifiedTemp(ListItemVerified listItemVerifiedTemp) {
        PageNavigationManager.listItemVerifiedTemp = listItemVerifiedTemp;
    }

    public static ListItemVerified getUserItemTemp() {
        return userItemTemp;
    }

    public static void setUserItemTemp(ListItemVerified userItemTemp) {
        PageNavigationManager.userItemTemp = userItemTemp;
    }
}
