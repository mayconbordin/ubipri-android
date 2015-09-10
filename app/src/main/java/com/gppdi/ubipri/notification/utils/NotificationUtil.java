package com.gppdi.ubipri.notification.utils;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.notification.data.models.Notification;

public class NotificationUtil {

    public static int getNotificationIcon(Notification notification) {
        return getNotificationIcon(notification.getFormat());
    }

    public static int getNotificationIcon(int format) {
        int icon;
        switch (format) {
            case Notification.FORMAT_NONE:
                icon = R.drawable.ic_notifications_black_24dp;
                break;
            case Notification.FORMAT_GCM:
                icon = R.drawable.ic_notifications_black_24dp;
                break;
            case Notification.FORMAT_SMS:
                icon = R.drawable.ic_sms_black_24dp;
                break;
            case Notification.FORMAT_EMAIL:
                icon = R.drawable.ic_email_black_24dp;
                break;
            case Notification.FORMAT_HISTORY:
                icon = R.drawable.ic_notifications_black_24dp;
                break;
            default:
                icon = R.drawable.ic_notifications_black_24dp;
                break;
        }
        return icon;
    }

    public static int getNotificationFormatStringRes(Notification notification) {
        return getNotificationFormatStringRes(notification.getFormat());
    }

    public static int getNotificationFormatStringRes(int format) {
        int stringResource;
        switch (format) {
            case Notification.FORMAT_NONE:
                stringResource = R.string.notification_type_none;
                break;
            case Notification.FORMAT_GCM:
                stringResource = R.string.notification_type_gcm;
                break;
            case Notification.FORMAT_SMS:
                stringResource = R.string.notification_type_sms;
                break;
            case Notification.FORMAT_EMAIL:
                stringResource = R.string.notification_type_email;
                break;
            case Notification.FORMAT_HISTORY:
                stringResource = R.string.notification_type_history;
                break;
            default:
                stringResource = R.string.notification_type_none;
                break;
        }
        return stringResource;
    }

}
