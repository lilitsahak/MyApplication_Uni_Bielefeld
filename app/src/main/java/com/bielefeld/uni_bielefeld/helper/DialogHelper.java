package com.bielefeld.uni_bielefeld.helper;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import io.paperdb.Paper;

public class DialogHelper {
    public static void showDialog(@StringRes int stringId, @StringRes int messageId, Context context) {
        String language = Paper.book().read("language");
        Context localeContext = LocalHelper.setLocale(context, language);
        Resources resources = localeContext.getResources();

        new AlertDialog.Builder(context)
                .setTitle(resources.getString(stringId))
                .setMessage(resources.getString(messageId))
                .show();

    }
}
