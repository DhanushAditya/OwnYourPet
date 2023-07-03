package com.example.petscare

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseApp
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject


class getProducts : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)


        val ID = intent.getStringExtra("ID")
        setContent {
            if (ID != null) {
                Lists(ID)
            }
        }

    }
}


@Composable
fun Lists(id:String)
{

    val a= products("Puppy Dry Dog Food", "7384",
        "Provides dogs the 5 Signs of Good Health - a PEDIGREE assurance!" +
                "Puppies need 24x Protein, 9.5x Calcium & 53x Iron B12 than human babies, an ideal treat" +
                "Contains 24% crude Protein, 10% crude Fat, and 5% crude Fibre" +
                "Ideal for Pugs, Beagle to Labrador, Golden Retriever & German shepherd" +
                "Developed by experts at the Waltham Centre for Pet Nutrition",
        R.drawable.i1, "Pedigree", "Dog Food", "Chicken, Non Vegetarian, Baby",330);


    val b= products("Training Leash", "1150",
        "This Leash Is The Best Choice For Tracking, Patrolling, Walking, Training And Just For Daily Use.It Doesn't Lose Its Shape So If Your Dog Pulls It Too Much This Leash Won't Stretch",
        R.drawable.i2, "Quickato", "Dog Leash", " Stylish Nylon Black Rope",195);

    val c= products("Pet Carrier", "1820",
        "Included screws can be used to further secure the top and bottom of kennel for added reinforcement" +
                "Made of durable plastic with a steel-wire front door",
        R.drawable.i3, "AmazonBasics", "Pet Cage", "Two Door Top Load Pet Carrier, Small Size (1 Piece,19-inch)",1290);
    val plist = listOf(a,b,c,a,b,c)
    Column(            modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)
        .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 250.dp))
        {
            items(plist)
            { prt ->
                Pcards(prt)
                //Text("Hello")

            }
        }
    }
}





@Composable
fun Pcards(data: products)
{
    Box(modifier = Modifier
        .width(128.dp)
        .height(285.dp)
        .border(width = 0.5.dp, color = Color.LightGray)
    ){
        val con = LocalContext.current
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .padding(15.dp)) {
            Text("${data.name}",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 0.07.dp)
                    .offset(y = 0.09.dp))
            Text("${data.brand}",
                fontSize = 12.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 0.07.dp)
                    .offset(y = 0.09.dp))
            Box(contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = data.img), contentDescription = "", modifier = Modifier
                    .size(120.dp))}
            Text("${data.fea}",
                fontSize = 10.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 0.07.dp)
                    .offset(y = 0.09.dp))
            Spacer(modifier = Modifier.weight(1f))
            Row(){
                Text("Rs. ${data.price}",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(start = 0.1.dp)
                        .offset(y = 10.dp))
                Spacer(modifier=Modifier.weight(1f))
                val intent = Intent(con, projectView::class.java)
                Button(onClick = {
                    val prodString = json.encodeToString(data)
                    intent.putExtra("Prod", prodString)


                }, colors = ButtonDefaults.buttonColors(Color(0xFF46A683)),
                    shape = RoundedCornerShape(0.dp)) {
                    Text("View")
                }
            }
        }

    }

}



//fun navigateToDetailScreen(data :products){
//    DetailScreen(data)
//}
//
//@Composable
//fun DetailScreen(data:products) {
//    Box(modifier = Modifier.fillMaxSize())
//    {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(20.dp)
//                .background(color = White),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(20.dp))
//            Text(
//                "${data.name}",
//                fontSize = 40.sp,
//                color = Color.Black,
//                fontWeight = FontWeight.Normal,
//                modifier = Modifier
//                    .padding(start = 0.07.dp)
//                    .offset(y = 0.09.dp)
//            )
//            Text(
//                "${data.brand}",
//                fontSize = 25.sp,
//                color = Color.Black,
//                fontWeight = FontWeight.Normal,
//                modifier = Modifier
//                    .padding(start = 0.07.dp)
//                    .offset(y = 0.09.dp)
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//            Box(contentAlignment = Alignment.Center) {
//                Image(painter = painterResource(id = data.img), contentDescription = "")
//            }
//            Text("Rs. ${data.price}",
//                fontSize = 25.sp,
//                color = Color.Black,
//                fontWeight = FontWeight.Normal,
//                modifier = Modifier
//                    .padding(start = 0.07.dp)
//                    .offset(y = 0.09.dp))
//
//            var quantity by remember { mutableStateOf(1) }
//
//            Column(modifier = Modifier.padding(16.dp)) {
//                QuantitySelector(
//                    initialQuantity = quantity,
//                    onQuantityChange = { it: Int ->
//                        quantity = it
//                    }
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                Box(modifier = Modifier.fillMaxWidth())
//                {
//                    Column(){
//                        Row(modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(20.dp))
//                        {
//                            Column() {
//                                Text(
//                                    "Features",
//                                    fontSize = 25.sp,
//                                    color = Color.Black,
//                                    fontWeight = FontWeight.Bold,
//                                    modifier = Modifier
//                                        .padding(start = 0.07.dp)
//                                        .offset(y = 0.09.dp)
//                                )
//                                Text(
//                                    "${data.fea}",
//                                    fontSize = 20.sp,
//                                    color = Color.Black,
//                                    fontWeight = FontWeight.Normal,
//                                    modifier = Modifier
//                                        .padding(start = 0.07.dp)
//                                        .offset(y = 0.09.dp)
//                                )
//                            }
//
//                            Spacer(modifier = Modifier.weight(1f))
//                            Column() {
//                                Text(
//                                    "Category",
//                                    fontSize = 25.sp,
//                                    color = Color.Black,
//                                    fontWeight = FontWeight.Bold,
//                                    modifier = Modifier
//                                        .padding(start = 0.07.dp)
//                                        .offset(y = 0.09.dp)
//                                )
//                                Text(
//                                    "${data.cat}",
//                                    fontSize = 20.sp,
//                                    color = Color.Black,
//                                    fontWeight = FontWeight.Normal,
//                                    modifier = Modifier
//                                        .padding(start = 0.07.dp)
//                                        .offset(y = 0.09.dp)
//                                )
//                            }
//                        }
//                        Spacer(modifier = Modifier.height(20.dp))
//                        Text("${data.desc}")
//                        Spacer(modifier = Modifier.height(30.dp))
//
//                        Spacer(modifier=Modifier.weight(1f))
//                        Button(onClick = { /*TODO*/ },modifier=Modifier.fillMaxWidth(),
//                            colors = ButtonDefaults.buttonColors(Color(0xFF46A683))) {
//                            Text("Buy Now")
//                        }
//                    }
//                }
//
//            }
//        }
//    }
//}
//
//
//@Composable
//fun QuantitySelector(
//    initialQuantity: Int,
//    onQuantityChange: (Int) -> Unit
//) {
//    var quantity by remember { mutableStateOf(initialQuantity) }
//
//    Row(
//        horizontalArrangement = Arrangement.Center
//    ) {
//        Spacer(modifier = Modifier.weight(1f))
//        Button(
//            onClick = {
//                if (quantity > 1) {
//                    quantity -= 1
//                    onQuantityChange(quantity)
//                }
//            },
//            shape = RoundedCornerShape(0.dp),
//            colors = ButtonDefaults.buttonColors(Color(0xFF46A683))
//        ) {
//            Text(text = "-", fontSize = 20.sp)
//        }
//
//        Text(
//            text = quantity.toString(),
//            modifier = Modifier.padding(horizontal = 16.dp), fontSize = 30.sp
//        )
//
//        Button(
//            onClick = {
//                quantity += 1
//                onQuantityChange(quantity)
//            },
//            shape = RoundedCornerShape(0.dp),
//            colors = ButtonDefaults.buttonColors(Color(0xFF46A683))
//        ) {
//            Text(text = "+", fontSize = 20.sp)
//        }
//        Spacer(modifier = Modifier.weight(1f))
//    }
//}
//
//
//
