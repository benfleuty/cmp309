package uk.ac.abertay.notsnapchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class ChatListActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        Button btnClose = findViewById(R.id.btnClose);
        btnClose.setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
            finish();
            return false;
        });

        LinearLayout x = findViewById(R.id.lloChatsContainer);

        View parent = findViewById(R.id.root);
        View lastNode = findViewById(R.id.toolbar);

        Bundle testBundle = new Bundle();
        testBundle.putString("username", "dion");
        testBundle.putString("message", "i love helicockters");
        testBundle.putInt("id", 69);
        ConversationBadge cb = new ConversationBadge(this, testBundle);

        x.addView(cb);

        testBundle = new Bundle();
        testBundle.putString("username", "ruben");
        testBundle.putString("message", "where my peen? 2 smol xD");
        testBundle.putInt("id", 1);
        cb = new ConversationBadge(this, testBundle);

        x.addView(cb);

        for (int i = 0; i < 0; i++) {
            ConstraintLayout clo = new ConstraintLayout(this);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            ConstraintSet constraintSet = null;
            clo.setLayoutParams(params);
            clo.setBackgroundColor(Color.RED);
            params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

            TextView username = new TextView(this);
            username.setText("username " + i);
            username.setId(View.generateViewId());
            constraintSet = new ConstraintSet();
            constraintSet.constrainWidth(username.getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainHeight(username.getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.connect(username.getId(), ConstraintSet.START, parent.getId(), ConstraintSet.START);
            constraintSet.connect(username.getId(), ConstraintSet.TOP, parent.getId(), ConstraintSet.TOP);


            TextView message = new TextView(this);
            message.setText("message " + i);
            message.setId(View.generateViewId());
            constraintSet = new ConstraintSet();
            constraintSet.constrainWidth(message.getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainHeight(message.getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.connect(message.getId(), ConstraintSet.START, parent.getId(), ConstraintSet.START);
            constraintSet.connect(message.getId(), ConstraintSet.TOP, username.getId(), ConstraintSet.BOTTOM);


            Button id = new Button(this);
            id.setId(View.generateViewId());
            id.setText("chat " + i);
            constraintSet = new ConstraintSet();
            constraintSet.constrainWidth(id.getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainHeight(id.getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.connect(id.getId(), ConstraintSet.END, parent.getId(), ConstraintSet.END);
            constraintSet.connect(id.getId(), ConstraintSet.TOP, parent.getId(), ConstraintSet.TOP);
            constraintSet.connect(id.getId(), ConstraintSet.BOTTOM, parent.getId(), ConstraintSet.BOTTOM);

            Log.e("", String.valueOf(parent.getId()));

            clo.addView(username);
            clo.addView(message);
            clo.addView(id);

            x.addView(clo);
        }
    }
}
