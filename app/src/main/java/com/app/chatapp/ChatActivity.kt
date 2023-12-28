package com.app.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var messageList: ArrayList<Message>
    private lateinit var adapter: MessageAdapter
    private lateinit var boxMessage: EditText
    private lateinit var sendMessage: ImageView
    private lateinit var dbRef: DatabaseReference

    var receiveRoom: String? = null
    var sendRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recycler = findViewById(R.id.chatRecycler)
        messageList = ArrayList()
        adapter = MessageAdapter(this@ChatActivity, messageList)
        boxMessage = findViewById(R.id.message)
        sendMessage = findViewById(R.id.send)
        dbRef = FirebaseDatabase.getInstance().reference

        val name = intent.getStringExtra("name")
        val receiveUid = intent.getStringExtra("uid")
        val sendUid = FirebaseAuth.getInstance().currentUser?.uid

        receiveRoom = sendUid + receiveUid
        sendRoom = receiveUid + sendUid

        supportActionBar?.title = name

        recycler.layoutManager = LinearLayoutManager(this@ChatActivity)
        recycler.adapter = adapter

        dbRef.child("chats").child(sendRoom!!).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        sendMessage.setOnClickListener {
            if (boxMessage.text.isEmpty() || boxMessage.text.isBlank()) {
                sendMessage.setImageResource(R.drawable.mic)
                Toast.makeText(this@ChatActivity, "Mic Access", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage.setImageResource(R.drawable.send)
                val message = boxMessage.text.toString()
                val messageObject = Message(message, sendUid)

                dbRef.child("chats").child(sendRoom!!).child("message").push()
                    .setValue(messageObject).addOnSuccessListener {
                        dbRef.child("chats").child(receiveRoom!!).child("message").push()
                            .setValue(messageObject)
                    }
                boxMessage.setText("")
            }
        }
    }
}