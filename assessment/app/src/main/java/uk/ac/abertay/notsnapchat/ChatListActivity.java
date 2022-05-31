package uk.ac.abertay.notsnapchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChatListActivity extends AppCompatActivity {
    LinearLayout lloChatsContainer;

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

        lloChatsContainer = findViewById(R.id.lloChatsContainer);

        for (int i = 0; i < 10; i++) {
            Bundle testBundle = new Bundle();
            testBundle.putString("username", "user " + i);
            testBundle.putString("message", "this is msg #" + i);
            testBundle.putInt("id", i);
            ConversationBadge cb = new ConversationBadge(this, testBundle);

            lloChatsContainer.addView(cb);
        }
    }
}
