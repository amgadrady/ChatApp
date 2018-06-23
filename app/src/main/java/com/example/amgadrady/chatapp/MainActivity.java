package com.example.amgadrady.chatapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.amgadrady.chatapp.adapters.ChatRoomsAdapter;
import com.example.amgadrady.chatapp.fragments.AddChatRoomFragment;
import com.example.amgadrady.chatapp.models.ChatRoom;
import com.example.amgadrady.chatapp.models.MainResponse;

import com.example.amgadrady.chatapp.models.User;
import com.example.amgadrady.chatapp.utils.Session;
import com.example.amgadrady.chatapp.webservices.WebService;
import com.fourhcode.forhutils.FUtilsProgress;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @BindView(R.id.recycler_chat_rooms)
    RecyclerView recyclerChatRooms;
    @BindView(R.id.content_main)
    RelativeLayout contentMain;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_desc)
    TextView toolbarDesc;
    @BindView(R.id.add_char_room_fab)
    FloatingActionButton addCharRoomFab;

    private FUtilsProgress progress;
    private Call<List<ChatRoom>> getChatRoomsCall;
    private List<ChatRoom> chatRooms;
    private ChatRoomsAdapter adapter;



   /* ItemTouchHelper.SimpleCallback swipChatRoomCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // get adapter position
           final int position = viewHolder.getAdapterPosition();
            // get chat room id

            int chatRoomID =  Integer.parseInt(chatRooms.get(position).id);
            // start Retrofit call to delete chat room
            WebService.getInstance().getApi().deleteChatRoom(chatRoomID).enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    if (response.body().status == 1) {
                        // toast message result
                        Toast.makeText(MainActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        // delete message from local chat room list which showed on adapter  now
                        chatRooms.remove(position);
                        // notify adapter that chat room deleted so its delete it
                        adapter.notifyItemRemoved(position);

                    } else {
                        // toast message if status 0 it will be error
                        Toast.makeText(MainActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    // toast message of fail
                    Toast.makeText(MainActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


                }
            });

        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
            {
                View itemView = viewHolder.itemView;

                Paint p = new Paint();

                if(dX <= 0 )
                {

                    p.setColor(Color.parseColor("#ED1220"));
                    c.drawRect((float)itemView.getRight()+ dX ,(float)itemView.getTag(),
                            (float)itemView.getRight(),(float)itemView.getBottom(),p);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);




            }
        }
    };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progress = FUtilsProgress.newProgress(this ,contentMain );
        recyclerChatRooms.setLayoutManager(new LinearLayoutManager(this));
        getChatRooms();

        User user = Session.getInstance().getUser();

        if(user != null)
        {
            toolbarTitle.setText(getString(R.string.welcome)+user.username);
            toolbarDesc.setText(getString(R.string.nice_to_meet_you));

            //check if admin
            if(user.isAdmin )
            {

                toolbarDesc.setText(getString(R.string.nice_to_meet_you_admin));
                addCharRoomFab.setVisibility(View.VISIBLE);

                // add ItemTouchHelper
               // ItemTouchHelper itemTouchHelper =new ItemTouchHelper(swipChatRoomCallBack);
                //itemTouchHelper.attachToRecyclerView(recyclerChatRooms);

            }
            else
            {

                addCharRoomFab.setVisibility(View.GONE);
            }


        }

    }

    // get chat rooms using retrofit
    private void getChatRooms() {
        progress.showTransparentProgress();
        getChatRoomsCall = WebService.getInstance().getApi().getAllChatRooms();

        // retrofit call start in background
        getChatRoomsCall.enqueue(new Callback<List<ChatRoom>>() {
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response) {

                chatRooms =  response.body();
                adapter = new ChatRoomsAdapter(chatRooms , MainActivity.this);
                recyclerChatRooms.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<ChatRoom>> call, Throwable t) {

                progress.dismiss();
                Log.e(TAG, "Error " + t.getLocalizedMessage());

                Toast.makeText(MainActivity.this, "Error " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });





    }

    public void reloadChatRooms() {

        getChatRooms();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getChatRoomsCall.cancel();
    }


    @OnClick({R.id.toolbar_tv_logout , R.id.add_char_room_fab})
    public void onClick(View view)
    {

        switch (view.getId())
        {

            case R.id.toolbar_tv_logout:
                Session.getInstance().logoutAndGoToLogin(this);
                break;
            case R.id.add_char_room_fab:
                AddChatRoomFragment addChatRoomFragment = new AddChatRoomFragment();
                addChatRoomFragment.show(getSupportFragmentManager(), addChatRoomFragment.TAG);
                break;
        }


    }
}
