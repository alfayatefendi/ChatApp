package com.app.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context: Context, val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val RECEIVE = 1
    val SEND = 2

    inner class ReceiveViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val receiveMessage = view.findViewById<TextView>(R.id.receive)
    }
    inner class SendViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val sendMessage = view.findViewById<TextView>(R.id.send)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
            return SendViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass == SendViewHolder::class.java) {
            val sendHolder = holder as SendViewHolder
            holder.sendMessage.text = currentMessage.message
        } else if (holder.javaClass == ReceiveViewHolder::class.java) {
            val receiveHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            return SEND
        } else {
            return RECEIVE
        }
    }
}