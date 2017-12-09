package com.user.maevis;
import android.content.Context;
import android.view.View;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;

public class TabNotifBadge extends AppCompatTextView {

    private View target;

    public TabNotifBadge(Context context, View target) {
        super(context);
        init(context, target);
    }

    private void init(Context context, View target) {
        this.target = target;
    }

    public void updateTabBadge(int badgeNumber) {
        if (badgeNumber > 0) {
            target.setVisibility(View.VISIBLE);
            ((TextView) target).setText(Integer.toString(badgeNumber));
        }
        else {
            target.setVisibility(View.GONE);
        }
    }
}