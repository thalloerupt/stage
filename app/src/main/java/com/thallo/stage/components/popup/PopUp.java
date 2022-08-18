package com.thallo.stage.components.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.R;

import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoSessionSettings;
import org.mozilla.geckoview.GeckoView;
import org.mozilla.geckoview.WebExtension;

public class PopUp {
    public void popUp(WebExtension webExtension, GeckoSession session, Context context){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        View popView= LayoutInflater.from(context).inflate(R.layout.popup,null );
        GeckoView geckoView= popView.findViewById(R.id.popupGecko);
        session.setContentDelegate(new GeckoSession.ContentDelegate() {});
        session.open(GeckoRuntime.getDefault(context));
        session.getSettings().setDisplayMode(GeckoSessionSettings.DISPLAY_MODE_MINIMAL_UI);
        session.setNavigationDelegate(new GeckoSession.NavigationDelegate() {
            @Nullable
            @Override
            public GeckoResult<GeckoSession> onNewSession(@NonNull GeckoSession session1, @NonNull String uri) {
                session.loadUri(uri);

            return GeckoSession.NavigationDelegate.super.onNewSession(session, uri);
            }
        });
        geckoView.setSession(session);
        bottomSheetDialog.setContentView(popView);
        bottomSheetDialog.show();





    }
}
