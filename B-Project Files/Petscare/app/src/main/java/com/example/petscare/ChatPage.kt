package com.example.petscare

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ChatPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)


        val sID = intent.getStringExtra("SID")
        val rID = intent.getStringExtra("RID")
        setContent {
            if (sID != null) {
                if (rID != null) {
                    ChatScreen(sID,rID)
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(sID:String,rID:String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        // Top bar with attractive TpBar
        AttractiveTopBox()
        val con = LocalContext.current

        var question by remember {
            mutableStateOf("")
        }
        val database = Firebase.database
        val messagesRef = database.reference.child("Chats")
        var flag by remember {
            mutableStateOf(false)
        }
        // Fetch messages based on sender and receiver IDs
        val chatMessages = remember { mutableStateListOf<Pair<String, String>>() }
        LaunchedEffect(Unit) {
            messagesRef.orderByChild("time")
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val message = snapshot.child("message").getValue(String::class.java) ?: ""
                        val sender = snapshot.child("sender").getValue(String::class.java) ?: ""
                        val receiver = snapshot.child("receiver").getValue(String::class.java) ?: ""
                        if (sender == sID && receiver == rID) {
                            chatMessages.add(Pair(message, "s"))
                        } else if (sender == rID && receiver == sID) {
                            chatMessages.add(Pair(message, "r"))
                        }
                        flag = true
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        TODO("Not yet implemented")
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                        TODO("Not yet implemented")
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancellation
                    }
                })
        }
        // Chatbox
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2F2F2))
        ) {

            val coroutineScope = rememberCoroutineScope()
            val scrollState = rememberScrollState()

            BoxWithConstraints(
                modifier = Modifier
                    .padding(top = 1.dp, bottom = 62.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),contentAlignment = Alignment.BottomEnd
            ) {
                val boxHeight = constraints.maxHeight

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp)
                        .wrapContentHeight(align = Alignment.Bottom)
                ) {
                    if(flag){
                        chatMessages.forEach { (question, answer) ->
                            ChatUserMessage(question,answer)
                        }

                    }
                    // Scroll to the bottom when a new item is added
                    LaunchedEffect(Unit) {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }

                // Scroll to the bottom on box height change (new item added)
                SideEffect {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
            }


            // Chat input
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp)
            ) {
                TextField(
                    value = question,
                    onValueChange = { newValue ->
                        question = newValue
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .shadow(elevation = 10.dp, clip = true),
                    placeholder = { Text(text = "Send Message") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF2F2F2),
                        focusedIndicatorColor = Color(0xFF46A683),
                        unfocusedIndicatorColor = Color(0xFF242124),
                        cursorColor = Color(0xFF46A683),
                        textColor = Color.Black
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (question.trim().isNotEmpty()) {
                                    val sender = sID // Replace sID with the appropriate sender ID
                                    val receiver = rID // Replace rID with the appropriate receiver ID
                                    val message = question
                                    val timestamp = System.currentTimeMillis()
                                    val database = Firebase.database
                                    val messagesRef = database.reference.child("Chats")

                                    val newMessageRef = messagesRef.push()
                                    newMessageRef.setValue(
                                        mapOf(
                                            "sender" to sender,
                                            "receiver" to receiver,
                                            "message" to message,
                                            "time" to timestamp
                                        )
                                    ).addOnSuccessListener {
                                        // Message sent successfully
                                    }.addOnFailureListener { exception ->
                                        // Handle failure
                                    }

                                    // Clear the input field after sending the message
                                    question = ""
                                } else {
                                    showToast(con, "**Please add your query**")
                                }
                                addUser(rID,sID)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sendmsg),
                                contentDescription = null,
                                tint = Color(0xFF46A683),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}


fun addUser(userId:String , newUserId:String){
    val database = Firebase.database
    val userChatRef = database.getReference("UserChat")

    userChatRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                // User already exists, update the userList
                val existingUserChat = dataSnapshot.getValue(UserChat::class.java)
                val userList = existingUserChat?.userList.orEmpty().toMutableList()
                userList.add(newUserId) // Add the new ID to the existing userList
                userChatRef.child(userId).child("userList").setValue(userList)
            } else {
                // User does not exist, create a new entry with the new ID
                val newUserList = listOf(newUserId)
                val newUserChat = UserChat(userId, newUserList)
                userChatRef.child(userId).setValue(newUserChat)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle any errors here
        }
    })
}
@Composable
fun ChatUserMessage(question: String, answer: String) {
    if(answer == "s"){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp, start = 10.dp),
            contentAlignment = Alignment.CenterEnd
        ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .border(1.dp, Color.Transparent)
                        .padding(start = 20.dp),

                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(shadowElevation = 4.dp) {
                    Text(
                        text = question,
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .background(Color(0xFF46A683))
                            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                            .border(1.dp, Color.Transparent)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
    if(answer == "r") {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .border(1.dp, Color.Transparent),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(shadowElevation = 4.dp) {
                    Text(
                        text = question,
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .background(Color(0xFFF2F2F2))
                            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                            .border(1.dp, Color.Transparent)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}