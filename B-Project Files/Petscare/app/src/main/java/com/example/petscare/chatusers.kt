package com.example.petscare

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.common.util.WorkSourceUtil.getNames
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class chatusers : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)


        val ID = intent.getStringExtra("ID")

        setContent {
            if (ID != null) {
                ChatScreen(ID)
            }
        }

    }
}


@Composable
fun ChatScreen(id:String) {
    var userIdList: MutableList<String> = mutableListOf()
    var userId: String = ""
    val userChatRef = FirebaseDatabase.getInstance().getReference("UserChat")
    val query = userChatRef.orderByChild("userid").equalTo(id)
    val con = LocalContext.current
    var gotval by remember {
        mutableStateOf(false)
    }
    fun handleDataRetrievalComplete() {
        // Perform any operations with userIdList here, as it is now accessible outside the query block

        gotval = true
    }

    query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val userChat = snapshot.getValue(UserChat::class.java)
                    userId = userChat?.userid ?: ""
                    userIdList.addAll(userChat?.userList.orEmpty())
                }
                // Perform any further operations with the userIdList here
                handleDataRetrievalComplete()
            } else {
                // Handle the case where no data matches the condition
                showToast(con, "No match")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle any errors here
            showToast(con, databaseError.toString())
        }
    })


    Surface {
        Column (modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF2F2F2))){
            AttractiveTopBox()
            if(gotval){
                LazyColumn (modifier = Modifier.fillMaxSize()){
                    items(userIdList) { userIDs ->

                        // Retrieve the username based on the userId
                        val profileRef = FirebaseDatabase.getInstance().getReference("Profile").child(userIDs)
                        var username by remember {
                            mutableStateOf("")
                        }
                        var getval2 by remember {
                            mutableStateOf(false)
                        }
                        profileRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    val profile = dataSnapshot.getValue(User::class.java)
                                    username = profile?.username ?: ""
                                }
                                getval2 = true

                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle any errors here
                            }
                        })
                        if(getval2){
                            val intent = Intent(con, ChatPage::class.java)
                            // Create the text element with icon
                            val textElement = createTextElementWithIcon(
                                icon = painterResource(R.drawable.chatprofile),
                                username = username,
                                onClick = {
                                    intent.putExtra("SID", id)
                                    intent.putExtra("RID", userIDs)
                                    con.startActivity(intent)
                                }
                            )

                            // Display the text element
                            textElement()
                        }



                    }
                }
            }



        }
    }
}

@Composable
fun createTextElementWithIcon(
    icon: Painter,
    username: String,
    onClick: () -> Unit
): @Composable () -> Unit {
    return {
        Surface(
            color = Color.White,
            shadowElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp).clickable { onClick() },
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 30.dp, end = 5.dp)
                            .size(37.dp)
                    )
                    Spacer(modifier = Modifier.width(25.dp))
                    Text(
                        text = username,
                        modifier = Modifier.fillMaxWidth(0.5f).align(CenterVertically),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp // Adjust the font size here
                        )
                    )
                    Spacer(modifier = Modifier.width(100.dp))
                    Image(
                        painter = painterResource(R.drawable.comment),// Provide the painter for the additional image here,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}





