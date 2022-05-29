package uk.ac.abertay.notsnapchat;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;

import com.google.android.material.divider.MaterialDivider;

public class ConversationBadge extends ConstraintLayout implements View.OnTouchListener {
    Context context;

    public ConversationBadge(Context context, Bundle data) {
        super(context);
        this.context = context;

        setId(View.generateViewId());
        ConstraintLayout.LayoutParams layoutParams =
                new Constraints.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        layoutParams.bottomMargin = 25;
        setLayoutParams(layoutParams);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        constraintSet.setMargin(ConstraintSet.PARENT_ID,ConstraintSet.TOP,100);

        setup(context, constraintSet, data);
        constraintSet.applyTo(this);
    }

    void setup(Context context, ConstraintSet constraintSet, Bundle data) {
        TextView tvUsername = new TextView(context);
        tvUsername.setId(View.generateViewId());
        tvUsername.setText(data.getString("username"));
        addView(tvUsername);
        constraintSet.constrainWidth(tvUsername.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(tvUsername.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(tvUsername.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(tvUsername.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);


        TextView tvMessage = new TextView(context);
        tvMessage.setId(View.generateViewId());
        tvMessage.setText(data.getString("message"));
        addView(tvMessage);
        constraintSet.constrainWidth(tvMessage.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(tvMessage.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(tvMessage.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(tvMessage.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);


        Button chatButton = new Button(context);
        chatButton.setId(View.generateViewId());
        chatButton.setText(String.valueOf(data.getInt("id")));
        addView(chatButton);
        constraintSet.constrainWidth(chatButton.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(chatButton.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(chatButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(chatButton.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(chatButton.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        MaterialDivider divider = new MaterialDivider(context);
        divider.setId(View.generateViewId());
        addView(divider);
        constraintSet.constrainWidth(divider.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(divider.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(divider.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(context, "x", Toast.LENGTH_SHORT).show();
        return false;
    }
}
