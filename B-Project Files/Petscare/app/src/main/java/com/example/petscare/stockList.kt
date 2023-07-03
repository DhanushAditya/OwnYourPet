package com.example.petscare

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.data.EmptyGroup.data
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseApp


class stockList : AppCompatActivity() {

    companion object {
        var gemail: String? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)

        val email = intent.getStringExtra("EMAIL")
        gemail = email
        setContent {
            ProductList()
        }
        // Use the email as needed in your home activity
    }
}
@Composable
fun ProductList()
{
    val f= products("Toy", "7384", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus finibus ligula at ullamcorper maximus. Aenean maximus, libero vel faucibus pellentesque, nulla ex consectetur nisl, vel suscipit lacus ex sit amet leo. Duis blandit velit a sapien egestas, sit amet volutpat quam tempor. Suspendisse pellentesque massa non enim luctus, vitae porta purus finibus. Duis eu semper erat. Sed mattis facilisis tellus sit amet sodales. Duis tincidunt nibh est. ",
        R.drawable.dog1, "Brand Name", "Category", "Features Description",100);
    val plist = listOf(f,f,f,f,f,f,f,f,f,f,f)

    Box(modifier=Modifier.fillMaxSize())
    {
        Column(modifier=Modifier.background(color = Color(0xFFE7E8D1)).fillMaxSize()
            , horizontalAlignment = Alignment.CenterHorizontally) {
            AttractiveTopBox()
            Text(
                text = "STOCK LIST",
                style = MaterialTheme.typography.headlineLarge.copy(color = Color(0xFF46A683),
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    // .background(color = Color.Black)
                    .padding(6.dp),
            )
            Spacer(modifier= Modifier.height(20.dp))
            Row()
            {
                Spacer(modifier=Modifier.weight(0.2f))
                Text("Stock ID", fontSize = 20.sp)
                Spacer(modifier=Modifier.weight(0.5f))
                Text("Stock Name", fontSize = 20.sp)
                Spacer(modifier=Modifier.weight(0.5f))
                Text("Price", fontSize = 20.sp)
                Spacer(modifier=Modifier.weight(0.2f))
            }
            Box(modifier=Modifier.padding(10.dp).background(color = Color.White)) {
                LazyColumn()
                {
                    items(plist)
                    { prt ->
                        Itemp(prt)

                    }
                }
            }
        }}
}

