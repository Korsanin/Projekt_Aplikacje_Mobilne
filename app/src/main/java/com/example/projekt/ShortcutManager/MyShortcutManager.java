package com.example.projekt.ShortcutManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;

import com.example.projekt.R;
import com.example.projekt.ReceivedSms;
import com.example.projekt.SentSms;

import java.util.ArrayList;
import java.util.List;

public class MyShortcutManager{
    public static void createDynamicShortcuts(Context context){
        ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
        ShortcutInfo smsSent = new ShortcutInfo.Builder(context,"id1")
                .setShortLabel(context.getString(R.string.shortcut_sms_sent_short_label))
                .setLongLabel(context.getString(R.string.shortcut_sms_sent_long_label))
                .setIcon(Icon.createWithResource(context,R.drawable.ic_baseline_list_24))
                .setIntent(new Intent(Intent.ACTION_VIEW,null,context,SentSms.class))
                .build();

        ShortcutInfo smsReceived = new ShortcutInfo.Builder(context,"id2")
                .setShortLabel(context.getString(R.string.shortcut_sms_received_short_label))
                .setLongLabel(context.getString(R.string.shortcut_sms_received_long_label))
                .setIcon(Icon.createWithResource(context,R.drawable.ic_baseline_list_24))
                .setIntent(new Intent(Intent.ACTION_VIEW,null,context, ReceivedSms.class))
                .build();

        List<ShortcutInfo> shortcutInfoList = new ArrayList<>();
        shortcutInfoList.add(smsSent);
        shortcutInfoList.add(smsReceived);

        shortcutManager.addDynamicShortcuts(shortcutInfoList);
    }
    public static void deleteDynamicShortcuts(Context context){
        ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
        List<String> shortcutIdsList = new ArrayList<>();
        shortcutIdsList.add("id1");
        shortcutIdsList.add("id2");
        shortcutManager.disableShortcuts(shortcutIdsList);
    }
}
