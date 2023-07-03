package com.example.petscare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Myads : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)


        val ID = intent.getStringExtra("ID")
        setContent {
            if (ID != null) {
                getAds(ID)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getAds(id:String){
    val con = LocalContext.current
    val intent = Intent(con, adsForm::class.java)
    homeicon()
    Scaffold {
        it
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF2F2F2))
        ) {
            AttractiveTopBox()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent)
            ) {
                AddButton {
                    intent.putExtra("ID", id)
                    con.startActivity(intent)

                }
                Row(

                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Your Ads",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                        tint = Color.Gray
                    )
                }

                var adDetails by remember { mutableStateOf(emptyList<ads>()) }
                var flag by remember { mutableStateOf(false) }

                val con = LocalContext.current

                LaunchedEffect(Unit) {
                    val adsRef = Firebase.database.reference.child("Ads")
                    adsRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val details = mutableListOf<ads>()
                            for (childSnapshot in snapshot.children) {
                                val cat =
                                    childSnapshot.child("cat").getValue(String::class.java) ?: ""
                                val userID =
                                    childSnapshot.child("userID").getValue(String::class.java) ?: ""
                                val date =
                                    childSnapshot.child("date").getValue(String::class.java) ?: ""
                                val age =
                                    childSnapshot.child("age").getValue(String::class.java) ?: ""
                                val gender =
                                    childSnapshot.child("gender").getValue(String::class.java) ?: ""
                                val breed =
                                    childSnapshot.child("breed").getValue(String::class.java) ?: ""
                                val description =
                                    childSnapshot.child("descrption").getValue(String::class.java)
                                        ?: ""
                                val address =
                                    childSnapshot.child("address").getValue(String::class.java)
                                        ?: ""
                                val price =
                                    childSnapshot.child("price").getValue(String::class.java) ?: ""
                                val imageIDString =
                                    childSnapshot.child("imageID").getValue(String::class.java)
                                        ?: ""
                                val imageID =
                                    if (imageIDString.isNotEmpty()) Uri.parse(imageIDString) else null
                                val active =
                                    childSnapshot.child("active").getValue(String::class.java) ?: ""

                                val ad = ads(
                                    cat,
                                    userID,
                                    date,
                                    age,
                                    gender,
                                    breed,
                                    description,
                                    address,
                                    price,
                                    imageID,
                                    active
                                )
                                if (userID == id) {
                                    details.add(ad)
                                }
                            }
                            adDetails = details
                            flag = true
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle database read error
                        }
                    })
                }
                LazyColumn {
                    var i = 1
                    items(adDetails) { ad ->
                        AdItemRow(ad, i)
                        i++
                        Divider(color = Color.LightGray, thickness = 1.dp)

                    }


                }
            }
        }
    }
}



@Composable
fun AddButton(onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .height(65.dp)
                .fillMaxWidth(0.65f)
                .padding(start = 16.dp, end = 16.dp)
                .border(
                    width = 3.dp,
                    color = Color(0xFF46A683),
                    shape = RoundedCornerShape(8.dp)
                )
                .background(color = Color.Transparent),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2F2F2)),//Color(0xFF46A683)

            shape = RoundedCornerShape(8.dp),

            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.plusic),
                        contentDescription = null,
                        tint = Color(0xFF46A683),
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add New Ads",
                        color = Color(0xFF46A683),
                        fontSize = 20.sp
                    )
                }

        }

    }


}

@Composable
fun AdItemRow(ad: ads, i: Int) {
    // Modify the appearance of each ad item row as per your requirements
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$i",
            modifier = Modifier.weight(0.1f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = ad.date,
            modifier = Modifier.weight(0.3f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.width(35.dp))

        Text(
            text = "${ad.cat} - ${ad.breed} ",
            modifier = Modifier
                .weight(0.6f)
                .width(200.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .width(88.dp)
                .height(34.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            border = BorderStroke(1.dp, if ((ad.active).toBoolean()) Color.Green else Color.Red)

        ) {
            Text(text = if ((ad.active).toBoolean()) "Active" else "Sold", color = if ((ad.active).toBoolean()) Color.Green else Color.Red)
        }

    }
}

@Composable
fun homeicon(){

        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
            IconButton(
                onClick = { /* Handle home icon click */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            }
        }

}