package com.example.myapp;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;

public class FloatingWidgetService extends Service {

    private WindowManager windowManager;
    private View chatHeadView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showChatHead();
        return START_STICKY;
    }

    private void showChatHead() {
        chatHeadView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);

        final WindowManager.LayoutParams params;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        }

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(chatHeadView, params);

        // Set click listener for the chat head icon
        ImageView chatHeadIcon = chatHeadView.findViewById(R.id.widget_image);
        chatHeadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a simple dialog-like view for user input
                showChatView();
            }
        });
    }

    private void showChatView() {
        final LinearLayout chatView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.chat_view, null);
        final EditText userInputEditText = chatView.findViewById(R.id.user_input_edit_text);
        Button sendButton = chatView.findViewById(R.id.send_button);
        Button cancelButton = chatView.findViewById(R.id.cancel_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = userInputEditText.getText().toString().trim();
                if (!userMessage.isEmpty()) {
                    // Implement chatbot logic and response handling here
                    // ...

                    // Remove the chat view
                    windowManager.removeView(chatView);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the chat view
                windowManager.removeView(chatView);
            }
        });

        // Add the chat view to the window manager
        WindowManager.LayoutParams chatViewParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        windowManager.addView(chatView, chatViewParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHeadView != null) {
            windowManager.removeView(chatHeadView);
        }
    }
}
