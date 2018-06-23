package com.example.amgadrady.chatapp;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.amgadrady.chatapp.adapters.MessagingAdapter;
import com.example.amgadrady.chatapp.models.MainResponse;
import com.example.amgadrady.chatapp.models.Message;
import com.example.amgadrady.chatapp.utils.Session;
import com.example.amgadrady.chatapp.webservices.WebService;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {


    @BindView(R.id.recycler_chat)
    RecyclerView recyclerChat;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.img_attachment)
    ImageView imgAttachment;
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.img_send)
    ImageView imgSend;
    @BindView(R.id.rllt_text_box)
    RelativeLayout rlltTextBox;
    @BindView(R.id.content_chat)
    LinearLayout contentChat;

    private int roomId = 0;
    private int userId = 0;
    private String username;
    private MessagingAdapter adapter;
    private List<Message> messages;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Message message = intent.getParcelableExtra("msg");
            if (message != null)
            {
                messages.add(message);
                adapter.notifyItemInserted(messages.size() - 1);

                recyclerChat.scrollToPosition(messages.size() - 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        roomId = getIntent().getExtras().getInt("room_id");
        userId = Session.getInstance().getUser().id;
        username =  Session.getInstance().getUser().username;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerChat.setLayoutManager(layoutManager);
        getMessages(roomId);

        FirebaseMessaging.getInstance().subscribeToTopic("room"+roomId);
        Log.e("room topic is" , "room"+roomId);
    }



    private void getMessages(int roomId)
    {

        WebService.getInstance().getApi().getMessages(roomId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {

                messages = response.body();
                adapter = new MessagingAdapter(messages , ChatActivity.this);
                recyclerChat.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error:" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void addMessage(Message message)
    {

        WebService.getInstance().getApi().addMessage(message).enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if(response.body().status == 0)
                {

                    Toast.makeText(ChatActivity.this, "Error while trying to send message", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {

                Toast.makeText(ChatActivity.this, "Error " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick({R.id.img_attachment , R.id.img_send})

    public void onClick(View view)
    {

       switch (view.getId())
       {

           case R.id.img_attachment:
               break;

           case R.id.img_send:
               if(etMessage.getText().toString().isEmpty())
                   return;

               String msg = etMessage.getText().toString();
               Message message = new Message();
               message.setType("1");
               message.setRoomId(String.valueOf(roomId));
               message.setUserId(String.valueOf(userId));
               message.setUsername(username);
               message.setContent(msg);
               messages.add(message);
               adapter.notifyItemInserted(messages.size()-1);
               recyclerChat.scrollToPosition(messages.size()-1);
               etMessage.setText("");
               addMessage(message);
               break;

       }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageReceiver , new IntentFilter("UpdateChateActivity"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(messageReceiver);
    }
}
