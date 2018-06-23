package com.example.amgadrady.chatapp.models;

import com.google.gson.annotations.SerializedName;

public class ChatRoom {

    @SerializedName("id")
    public String id;
    @SerializedName("room_name")
    public String room_name;
    @SerializedName("room_desc")
    public String room_desc;

}
