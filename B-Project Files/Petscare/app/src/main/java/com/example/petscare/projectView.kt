package com.example.petscare

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseApp
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class projectView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)

        val prodString = intent.getStringExtra("Prod")
        val prod: products? = prodString?.let { json.decodeFromString(it) }
        setContent {
            if (prod != null) {
                DetailScreen(prod)
            }
        }
    }
}


val json = Json {
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

@Composable
fun DetailScreen(data:products) {
    Box()
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .background(color = White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "${data.name}",
                fontSize = 40.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 0.07.dp)
                    .offset(y = 0.09.dp)
            )
            Text(
                "${data.brand}",
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 0.07.dp)
                    .offset(y = 0.09.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = data.img), contentDescription = "")
            }
            Text("Rs. ${data.price}",
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 0.07.dp)
                    .offset(y = 0.09.dp))

            var quantity by remember { mutableStateOf(1) }

            Column(modifier = Modifier.padding(16.dp)) {
                QuantitySelector(
                    initialQuantity = quantity,
                    onQuantityChange = { it: Int ->
                        quantity = it
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(modifier = Modifier.fillMaxWidth())
                {
                    Column(){
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp))
                        {
                            Column() {
                                Text(
                                    "Features",
                                    fontSize = 25.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(start = 0.07.dp)
                                        .offset(y = 0.09.dp)
                                )
                                Text(
                                    "${data.fea}",
                                    fontSize = 20.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                        .padding(start = 0.07.dp)
                                        .offset(y = 0.09.dp)
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            Column() {
                                Text(
                                    "Category",
                                    fontSize = 25.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(start = 0.07.dp)
                                        .offset(y = 0.09.dp)
                                )
                                Text(
                                    "${data.cat}",
                                    fontSize = 20.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                        .padding(start = 0.07.dp)
                                        .offset(y = 0.09.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("${data.desc}")
                        Spacer(modifier = Modifier.height(30.dp))

                        Spacer(modifier=Modifier.weight(1f))
                        Button(onClick = { /*TODO*/ },modifier=Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(Color(0xFF46A683))) {
                            Text("Buy Now")
                        }
                    }
                }

            }
        }
    }
}


@Composable
fun QuantitySelector(
    initialQuantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf(initialQuantity) }

    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if (quantity > 1) {
                    quantity -= 1
                    onQuantityChange(quantity)
                }
            },
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF46A683))
        ) {
            Text(text = "-", fontSize = 20.sp)
        }

        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(horizontal = 16.dp), fontSize = 30.sp
        )

        Button(
            onClick = {
                quantity += 1
                onQuantityChange(quantity)
            },
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF46A683))
        ) {
            Text(text = "+", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
