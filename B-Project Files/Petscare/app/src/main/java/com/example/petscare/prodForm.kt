package com.example.petscare

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.FirebaseApp
import kotlinx.serialization.Serializable

//import com.example.productform.ui.theme.ProductFormTheme
class prodForm : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)


        setContent {
            TextInputs()
        }
        // Use the email as needed in your home activity
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputs() {
    AttractiveTopBox()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE7E8D1))
    ) {
        @Serializable
        data class Products(
            val name : String = "",
            val pdid : String = "",
            val desc : String = "",
            val img : Int,
            val brand : String = "",
            val cat : String = "",
            val fea : String= "",
            val  price : Int
        )
        var eg = Products("","","",0,"","","",0);
        var name by remember { mutableStateOf(TextFieldValue("")) }
        var pdid by remember { mutableStateOf(TextFieldValue("")) }
        var desc by remember { mutableStateOf(TextFieldValue("")) }

        var brand by remember { mutableStateOf(TextFieldValue("")) }
        var cat by remember { mutableStateOf(TextFieldValue("")) }
        var fea by remember { mutableStateOf(TextFieldValue("")) }

        var price by remember { mutableStateOf(TextFieldValue("")) }



        var selectedImageUris by remember {
            mutableStateOf<List<Uri>>(emptyList())
        }

        val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = { uris -> selectedImageUris = uris }
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 30.dp)
                .shadow(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(6.dp),
                    clip = true
                )
                .background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(
                rememberScrollState()
            )) {
                val outlineBorderThickness = 12.dp
                val outlineShape = RoundedCornerShape(6.dp)
                val outlineTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Gray,
                )
                Spacer(modifier = Modifier.heightIn(10.dp))

                Text(
                    text = "ADD NEW STOCK",
                    style = typography.headlineLarge.copy(color = Color(0xFF46A683),
                        textAlign = TextAlign.Center, fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxWidth()
                        // .background(color = Color.Black)
                        .padding(6.dp),
                )
                Spacer(modifier = Modifier.heightIn(10.dp))
//NAME
                OutlinedTextField(
                    value = name,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),

                    label = {
                        Text(
                            text = "Title",
                            style = TextStyle.Default.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) },
                    onValueChange = {
                        name = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.heightIn(5.dp))
//PRODUCT ID
                OutlinedTextField(
                    value = pdid,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    label = {
                        Text(
                            text = "Product ID",
                            style = TextStyle.Default.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    onValueChange = {
                        pdid = it
                    }
                )

                Spacer(modifier = Modifier.heightIn(5.dp))
//BRAND
                OutlinedTextField(
                    value = brand,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    label = {
                        Text(
                            text = "Brand",
                            style = TextStyle.Default.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ))},
                    onValueChange = {
                        brand = it
                    }
                )
                Spacer(modifier = Modifier.heightIn(5.dp))

//PRICE
                OutlinedTextField(value = price ,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            text = "Price",
                            style = TextStyle.Default.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )) },

                    onValueChange = {
                        price = it
                    }
                )
                Spacer(modifier = Modifier.heightIn(15.dp))

//DESCRIPTION

                OutlinedTextField(value = desc ,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(250.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            text = "Description",
                            style = TextStyle.Default.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )) },

                    onValueChange = {
                        desc = it
                    }
                )

//Features
                OutlinedTextField(value = fea,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            text = "Features",
                            style = TextStyle.Default.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )) },

                    onValueChange = {
                        fea = it
                    }
                )
//Category
                OutlinedTextField(value = cat ,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            text = "Price",
                            style = TextStyle.Default.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )) },

                    onValueChange = {
                        cat = it
                    }
                )

                Button(
                    onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                        )
                    }, colors = ButtonDefaults.buttonColors(Color(0xFF46A683)),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(text = "Upload Photos", color = Color.White, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))


                for (uri in selectedImageUris)
                {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.heightIn(15.dp))
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                        .height(56.dp), border = BorderStroke(2.dp, Color.White),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF46A683)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Submit")
                }

            }
        }
    }
}


