package com.example.amgadrady.chatapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amgadrady.chatapp.R;
import com.example.amgadrady.chatapp.models.Message;
import com.example.amgadrady.chatapp.models.MessageType;
import com.example.amgadrady.chatapp.utils.Session;
import com.example.amgadrady.chatapp.webservices.Urls;

import java.security.PrivateKey;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messages;
    private Context context;

    public MessagingAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MessageType.SENT_TEXT) {
            return new SentTextHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sent_message_text, parent, false));
        } else if (viewType == MessageType.SENT_IMAGE) {
            return new SentImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sent_message_img, parent, false));
        } else if (viewType == MessageType.RECEIVED_TEXT) {
            return new ReceivedTextHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_received_message_text, parent, false));
        } else if (viewType == MessageType.RECEIVED_IMAGE) {
            return new ReceivedImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_received_message_img, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mHolder, int position) {

        int type = getItemViewType(position);
        Message message = messages.get(position);

        if(type == MessageType.SENT_TEXT)
        {

            SentTextHolder holder = (SentTextHolder) mHolder;
            holder.tvTime.setText(message.getTimestamp());
            holder.tvMessageContent.setText(message.getContent());
        }
        else if(type == MessageType.SENT_IMAGE){

            SentImageHolder holder = (SentImageHolder) mHolder;
            holder.tvTime.setText(message.getTime());
            Glide.with(context).load(Urls.IMAGES_URL + message.getContent()).into(holder.imgMsg);

        }

        else if (type == MessageType.RECEIVED_TEXT) {
            ReceivedTextHolder holder = (ReceivedTextHolder) mHolder;
            holder.tvTime.setText(message.getTime());
            holder.tvUsername.setText(message.getUsername());
            holder.tvMessageContent.setText(message.getContent());


        } else if (type == MessageType.RECEIVED_IMAGE) {
            ReceivedImageHolder holder = (ReceivedImageHolder) mHolder;
            holder.tvTime.setText(message.getTime());
            holder.tvUsername.setText(message.getUsername());
            Glide.with(context).load(Urls.IMAGES_URL + message.getContent()).into(holder.imgMsg);

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    @Override
    public int getItemViewType(int position) {


        int userID = Session.getInstance().getUser().id;
        Message message = messages.get(position);

        if(userID == Integer.parseInt(message.getUserId()))
        {
            if(message.getType().equals("1"))
            {

                return MessageType.SENT_TEXT;
            }else if(message.getType().equals("2"))
            {

                return MessageType.SENT_IMAGE;
            }
         }else {

            if (message.getType().equals("1"))
            {

                return MessageType.RECEIVED_TEXT;
            }else if(message.getType().equals("2"))
            {
                return MessageType.RECEIVED_IMAGE;
            }
        }

        return super.getItemViewType(position);
    }


    // sent message holder

    class SentMessageHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_time)
        TextView tvTime;

        public SentMessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this ,itemView );
        }
    }

    class SentTextHolder extends SentMessageHolder {

        @BindView(R.id.tv_message_content)
        TextView tvMessageContent;

        public SentTextHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this ,itemView );
        }
    }


    class SentImageHolder extends SentMessageHolder
    {

          @BindView(R.id.img_msg)
        ImageView imgMsg;

        public SentImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this ,itemView );

        }
    }



    // received message holders
    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ReceivedTextHolder extends ReceivedMessageHolder {
        @BindView(R.id.tv_message_content)
        TextView tvMessageContent;

        public ReceivedTextHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // received message with type image

    class ReceivedImageHolder extends ReceivedMessageHolder {
        @BindView(R.id.img_msg)
        ImageView imgMsg;

        public ReceivedImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }




}
