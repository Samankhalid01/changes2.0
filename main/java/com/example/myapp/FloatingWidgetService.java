package com.example.myapp;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.TypedValue;
import androidx.core.content.ContextCompat;
import android.widget.Button;
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
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import android.util.TypedValue;

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
                // Here Hide the logo when clicked

                chatHeadIcon.setVisibility(View.INVISIBLE);
                // Show the chat view
                showChatView();
            }
        });
    }

    private void showChatView() {
        // Create a custom dialog-like view
        final View chatView = LayoutInflater.from(this).inflate(R.layout.chat_view, null);
        final EditText userInputEditText = chatView.findViewById(R.id.user_input_edit_text);
        Button sendButton = chatView.findViewById(R.id.send_button);
        Button clearButton = chatView.findViewById(R.id.clear_button);


        // Apply custom styling to buttons
        applyButtonStyle(sendButton);
        applyButtonStyle(clearButton);
        applyButtonStyle(clearButton);

        // Handle send button click
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




        // Set up layout parameters for the chat view
        WindowManager.LayoutParams chatViewParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        // Set position for the chat view
        chatViewParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        chatViewParams.x = 0;
        chatViewParams.y = 0;

        // Add the chat view to the window manager
        windowManager.addView(chatView, chatViewParams);
    }

    private void applyButtonStyle(Button button) {
        // Apply custom styling to buttons here
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        button.setTextColor(ContextCompat.getColor(this, android.R.color.black));

        // Set background color and border radius
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(ContextCompat.getColor(this, android.R.color.white)); // Set background color
        drawable.setCornerRadius(8); // Set border radius in pixels
        button.setBackground(drawable);

        // Add padding to the button
        int paddingDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        button.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHeadView != null) {
            windowManager.removeView(chatHeadView);
        }
    }
}
