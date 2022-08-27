package com.thallo.stage.components.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thallo.stage.R;
import com.thallo.stage.database.bookmark.Bookmark;
import com.thallo.stage.database.bookmark.BookmarkViewModel;
import com.thallo.stage.databinding.DiaBookmarkBinding;

public class BookmarkDialog extends MaterialAlertDialogBuilder {
    DiaBookmarkBinding diaBookmarkBinding;
    public BookmarkDialog(@NonNull Context context, String title, String url, BookmarkViewModel bookmarkViewModel) {
        super(context);
        setTitle(R.string.dia_add_bookmark_title);
        diaBookmarkBinding=DiaBookmarkBinding.inflate(LayoutInflater.from(context));
        diaBookmarkBinding.diaBookmarkTitle.setText(title);
        diaBookmarkBinding.diaBookmarkUrl.setText(url);
        setView(diaBookmarkBinding.getRoot());
        setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Bookmark bookmark=new Bookmark(diaBookmarkBinding.diaBookmarkUrl.getText().toString(),diaBookmarkBinding.diaBookmarkTitle.getText().toString(),"默认",diaBookmarkBinding.radioButton.isChecked());
                bookmarkViewModel.insertWords(bookmark);

            }
        });
        setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

    }

    @Override
    public AlertDialog show() {
        return super.show();
    }
}
