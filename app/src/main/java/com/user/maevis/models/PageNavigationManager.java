package com.user.maevis.models;

import com.user.maevis.ListItem;
import com.user.maevis.ListItemVerified;
import com.user.maevis.UserItem;

/**
 * Created by User on 1/16/2018.
 */

public class PageNavigationManager {
    private static ListItemVerified clickedTabHomeListItemVerified;
    private static boolean clickedTabHomeAdapter;

    private static ListItemVerified clickedTabLocListItemVerified;
    private static ListItem clickedTabLocListItemPending;

    private static ListItem clickedTabNotifListItem;
    private static boolean clickedTabNotifAdapter;

    private static ListItemVerified clickedTabNotifRegListItem;

    private static UserItem clickedReportPageUserItem;
    private static boolean clickedReportPage;

    private static UserItem clickedVerifyReportUserItem;
    private static boolean clickedVerifyReport;


    public static void initializePageNavigationManager() {
        clickedTabHomeListItemVerified = null;

        clickedTabLocListItemVerified = null;

        clickedTabLocListItemPending = null;

        clickedTabNotifListItem = null;

        clickedTabNotifRegListItem = null;

        clickedReportPageUserItem = null;

        clickedVerifyReportUserItem = null;
    }

    public static void clickTabHomeListItemVerified(ListItemVerified listItemVerified) {
        clickedTabHomeListItemVerified = listItemVerified;
        clickedTabLocListItemVerified = null;
        clickedTabLocListItemPending = null;
        clickedTabNotifListItem = null;
        clickedTabNotifRegListItem = null;
        clickedReportPageUserItem = null;
        clickedVerifyReportUserItem = null;
    }

    public static void clickTabLocListItemVerified(ListItemVerified listItemVerified) {
        clickedTabHomeListItemVerified = null;
        clickedTabLocListItemVerified = listItemVerified;
        clickedTabLocListItemPending = null;
        clickedTabNotifListItem = null;
        clickedTabNotifRegListItem = null;
        clickedReportPageUserItem = null;
        clickedVerifyReportUserItem = null;
    }

    public static void clickTabLocListItemPending(ListItem listItem) {
        clickedTabHomeListItemVerified = null;
        clickedTabLocListItemVerified = null;
        clickedTabLocListItemPending = listItem;
        clickedTabNotifListItem = null;
        clickedTabNotifRegListItem = null;
        clickedReportPageUserItem = null;
        clickedVerifyReportUserItem = null;
    }

    public static void clickTabNotifListItem(ListItem listItem) {
        clickedTabHomeListItemVerified = null;
        clickedTabLocListItemVerified = null;
        clickedTabLocListItemPending = null;
        clickedTabNotifListItem = listItem;
        clickedTabNotifRegListItem = null;
        clickedReportPageUserItem = null;
        clickedVerifyReportUserItem = null;
    }

    public static void clickReportPageUserItem(UserItem userItem) {
        clickedTabHomeListItemVerified = null;
        clickedTabLocListItemVerified = null;
        clickedTabLocListItemPending = null;
        clickedTabNotifListItem = null;
        clickedTabNotifRegListItem = null;
        clickedReportPageUserItem = userItem;
        clickedVerifyReportUserItem = null;
    }

    public static void clickTabNotifRegListItem(ListItemVerified listItemVerified) {
        clickedTabHomeListItemVerified = null;
        clickedTabLocListItemVerified = null;
        clickedTabLocListItemPending = null;
        clickedTabNotifListItem = null;
        clickedTabNotifRegListItem = listItemVerified;
        clickedReportPageUserItem = null;
        clickedVerifyReportUserItem = null;
    }

    public static void clickVerifyReportUserItem(UserItem userItem) {
        clickedTabHomeListItemVerified = null;
        clickedTabLocListItemVerified = null;
        clickedTabLocListItemPending = null;
        clickedTabNotifListItem = null;
        clickedTabNotifRegListItem = null;
        clickedReportPageUserItem = null;
        clickedVerifyReportUserItem = userItem;
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
}
