package com.example.amgadrady.chatapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.amgadrady.chatapp.ChatActivity;
import com.example.amgadrady.chatapp.R;
import com.example.amgadrady.chatapp.models.ChatRoom;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ChatRoomHolder> {

    private List<ChatRoom> chatRooms;
    private Context context;

    public ChatRoomsAdapter(List<ChatRoom> chatRooms, Context context) {
        this.chatRooms = chatRooms;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatRoomHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.row_chat_room , parent ,false)) ;
    }


    @Override
    public void onBindViewHolder(@NonNull ChatRoomHolder holder, int position) {

        final ChatRoom room = chatRooms.get(position);
        holder.tvTitle.setText(room.room_name);
        holder.tvDesc.setText(room.room_desc);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context , ChatActivity.class);
                intent.putExtra("room_id" , Integer.parseInt(room.id));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public class ChatRoomHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_group_icon)
        ImageView imgGroupIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.rllt_body)
        RelativeLayout rlltBody;

        public ChatRoomHolder(View inflate) {
        super(inflate);
            ButterKnife.bind(this , inflate);

        }
    }
}
