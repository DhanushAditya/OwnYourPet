package com.example.petscare

import android.content.Intent
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseApp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entriesOf



class dashboard : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)


        setContent {
            Dashboard()
        }
        // Use the email as needed in your home activity
    }
}

@Composable
fun Dashboard()
{
    val con = LocalContext.current
    Column(modifier = Modifier
        .background(color = Color(0xFFE7E8D1))
        .fillMaxSize())
    {
        AttractiveTopBox()
        Box(){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(5.dp)
                    .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                val name: String = "Admin"
                Text("Welcome $name ,",
                    fontSize = 25.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 25.dp, start = 22.dp))

                Spacer(modifier = Modifier.height(35.dp))
                // ProfessionalCard

                Box(modifier= Modifier
                    .padding(6.dp)
                    .border(width = 0.5.dp, color = Color.Gray)
                    .wrapContentHeight())
                {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                        ,
                        //.wrapContentSize(Alignment.Center),
                        verticalArrangement = Arrangement.Top
                        , horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Sales Today",
                            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = "Rs.25,000",
                            style = TextStyle(fontSize = 20.sp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(22.dp))
                        Text(
                            text = "Total Revenue Generated",
                            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Rs. 2,000",
                            style = TextStyle(fontSize = 20.sp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier=Modifier.height(30.dp))
                Text("Sales : ",
                    fontSize = 25.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 25.dp, start = 22.dp))
                Spacer(modifier=Modifier.height(20.dp))
                Box(modifier= Modifier.padding(10.dp))
                {
                    Salesgraph()
                }
                Spacer(modifier=Modifier.height(0.dp))
                Text("Services : ",
                    fontSize = 25.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 25.dp, start = 22.dp))
                Spacer(modifier=Modifier.height(20.dp))
                val intent = Intent(con, prodForm::class.java)
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .padding(30.dp)){
                    Button(onClick = {
                        //Use form
                        con.startActivity(intent)


                    },modifier= Modifier
                        .width(145.dp)
                        .height(85.dp), colors = ButtonDefaults.buttonColors(Color(0xFF46A683)),
                        shape = RoundedCornerShape(0.dp))
                    {
                        Text("Add Stocks")
                    }
                    Spacer(modifier=Modifier.weight(1f))
                    val intent2 = Intent(con, stockList::class.java)
                    Button(onClick = {
                        //Stock List
                        con.startActivity(intent2)


                                     },modifier= Modifier
                        .width(145.dp)
                        .height(85.dp), colors = ButtonDefaults.buttonColors(Color(0xFF46A683)),
                        shape = RoundedCornerShape(0.dp))
                    {
                        Text("Stocks List")
                    }
                }
                Text("Recent Products :  ",
                    fontSize = 25.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 25.dp, start = 22.dp))
                Spacer(modifier=Modifier.height(20.dp))
                Row()
                {
                    Spacer(modifier=Modifier.weight(0.2f))
                    Text("ID")
                    Spacer(modifier=Modifier.weight(0.5f))
                    Text("Name")
                    Spacer(modifier=Modifier.weight(0.5f))
                    Text("Price")
                    Spacer(modifier=Modifier.weight(0.2f))
                }
                Spacer(modifier=Modifier.height(10.dp))
                Box(modifier=Modifier.padding(10.dp).background(color = Color.White)) {
                    val f= products("Toy", "7384", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus finibus ligula at ullamcorper maximus. Aenean maximus, libero vel faucibus pellentesque, nulla ex consectetur nisl, vel suscipit lacus ex sit amet leo. Duis blandit velit a sapien egestas, sit amet volutpat quam tempor. Suspendisse pellentesque massa non enim luctus, vitae porta purus finibus. Duis eu semper erat. Sed mattis facilisis tellus sit amet sodales. Duis tincidunt nibh est. ",
                        R.drawable.profile2, "Brand Name", "Category", "Features Description",100);
                    val plist = listOf(f,f,f   )
                    Column() {
                        for (prt in plist) {
                            Itemp(prt)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Itemp(data:products)
{
    Column() {
        Spacer(modifier= Modifier.height(10.dp))
        Row()
        {
            Spacer(modifier=Modifier.weight(0.2f))
            Text("${data.pdid}")
            Spacer(modifier=Modifier.weight(0.5f))
            Text("${data.name}")
            Spacer(modifier=Modifier.weight(0.5f))
            Text("Rs. ${data.price}")
            Spacer(modifier=Modifier.weight(0.2f))
        }
        Spacer(modifier= Modifier.height(20.dp))
        Divider(color = Color.Gray, thickness = 0.5.dp)
    }
}
@Composable
fun Salesgraph()
{
    val chartEntryModelProducer1 = ChartEntryModelProducer(entriesOf(4f, 12f, 8f, 16f))
    val chartEntryModelProducer2 = ChartEntryModelProducer(entriesOf(16f, 8f, 12f, 4f))
    val composedChartEntryModelProducer = chartEntryModelProducer1 + chartEntryModelProducer2

    val columnChart = columnChart()
    val lineChart = lineChart( lines = listOf(
        lineSpec(
            lineColor = Color(0xFF46A683),
            lineBackgroundShader = verticalGradient(
                colors = arrayOf(
                    Color(0xFF46A683).copy(alpha = 0.4f),
                    Color(0xFF46A683).copy(alpha = 0f),
                ),
            ),
        ),
    ),)
    Chart(
        chart = remember(lineChart) {lineChart },
        chartModelProducer = chartEntryModelProducer2,
        startAxis = startAxis(),
        bottomAxis = bottomAxis()
    )
}











