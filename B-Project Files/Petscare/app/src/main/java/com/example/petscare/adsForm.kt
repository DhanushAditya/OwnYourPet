package com.example.petscare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RadioGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.android.play.integrity.internal.c
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class adsForm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)


        val ID = intent.getStringExtra("ID")
        setContent {
            if (ID != null) {
                adform(ID)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun adform(id:String){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFFF2F2F2))) {
        val con = LocalContext.current
        var age by remember { mutableStateOf("") }
        var gender by remember { mutableStateOf("") }
        var breed by remember { mutableStateOf("") }
        var address by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        var imgname   by remember { mutableStateOf("Image") }
        var selectedCategory by remember { mutableStateOf("Pet Category") }
        var imageUri: Uri? by remember { mutableStateOf(null) }
        val imageSelectionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            // Handle the selected image URI
            imageUri = uri
        }
        val database = Firebase.database
        val storage = Firebase.storage

        // Function to upload the image to Firebase Storage
        fun uploadImageToStorage(imageUri: Uri, imageName: String) {
            val storageRef = storage.reference
            val imagesRef = storageRef.child("images/$imageName")
            val uploadTask = imagesRef.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                // Image upload successful
                // Perform any additional operations or handle success event
            }.addOnFailureListener {
                // Image upload failed
                // Handle failure or display an error message
            }
        }

        // Function to update the details to Firebase Realtime Database
        fun updateDetailsToDatabase() {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            val adsRef = database.reference.child("Ads")


            val adDetails = mapOf(
                "cat" to selectedCategory,
                "userID" to id,
                "date" to dateFormat.format(Date()),
                "age" to age,
                "gender" to gender,
                "breed" to breed,
                "descrption" to description,
                "address" to address,
                "price" to price,
                "imageID" to imgname,
                "active" to "true"
            )

            adsRef.push().setValue(adDetails) { error, _ ->
                if (error != null) {
                    // Failed to update details
                    // Handle failure or display an error message
                } else {
                    showToast(con,"Success")
                }
            }
        }

        // Function to get the current date in a desired format


        // Function to handle the button click event
        fun onButtonClick() {
            if (imageUri != null) {
                val imageName = "image_${System.currentTimeMillis()}"
                uploadImageToStorage(imageUri!!, imageName)
                // Include the image name in the ad details
                imgname = imageName
            }

            updateDetailsToDatabase()
        }
        var expanded by remember {
            mutableStateOf(false)
        }

        val categories = listOf("Dog", "Cat", "Bird", "Fish")

        Row( horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Add your pet details",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(16.dp)

            )
        }
        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier= Modifier
                .fillMaxWidth(0.45f)
                .height(60.dp)
                .padding(top = 6.dp)
                .clickable { expanded = !expanded }) {


                CustomDropdown(
                    initialCategory = selectedCategory,
                    categories = categories,
                    onCategorySelected = { category -> selectedCategory = category }
                )

            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier= Modifier
                .fillMaxWidth(0.6f)
                .height(60.dp))
                {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age") },
                        placeholder= { Text("Days eg.45d") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.Black, // Color of the text
                            focusedBorderColor = Color(0xFF46A683), // Border color when focused
                            unfocusedBorderColor = Color.Black, // Border color when unfocused
                            disabledBorderColor = Color.Gray, // Border color when disabled
                            cursorColor = Color(0xFF46A683), // Color of the cursor
                            focusedLabelColor = Color(0xFF46A683), // Color of the label when focused
                            unfocusedLabelColor = Color.Black, // Color of the label when unfocused
                            disabledLabelColor = Color.Gray // Color of the label when disabled
                        ),
                        modifier = Modifier.fillMaxSize()
                    )

                }


        }

        Row(horizontalArrangement = Arrangement.Start,verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)) {
            Spacer(modifier = Modifier.width(42.dp))
            Text(
                text = "Gender :",
                style = TextStyle(fontSize = 20.sp) // Increase the font size to 18 sp
            )

            Spacer(modifier = Modifier.width(10.dp)) // Add some spacing between the label and radio button

            RadioButton(
                selected = gender == "Male",
                onClick = { gender = "Male" },
                enabled = true,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF46A683)
                ),modifier = Modifier.size(27.dp)
            )
            Text(text = "Male", style = TextStyle(fontSize = 18.sp),modifier = Modifier)
            Spacer(modifier = Modifier.width(14.dp))
            RadioButton(
                selected = gender == "Female",
                onClick = { gender = "Female" },
                enabled = true,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF46A683)
                ), modifier = Modifier.size(27.dp)
            )
            Text(text = "Female",style = TextStyle(fontSize = 18.sp), modifier = Modifier)

        }
        Row(horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()) {
            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Breed") },
                placeholder= { Text("Eg.Labrodor if Dog") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black, // Color of the text
                    focusedBorderColor = Color(0xFF46A683), // Border color when focused
                    unfocusedBorderColor = Color.Black, // Border color when unfocused
                    disabledBorderColor = Color.Gray, // Border color when disabled
                    cursorColor = Color(0xFF46A683), // Color of the cursor
                    focusedLabelColor = Color(0xFF46A683), // Color of the label when focused
                    unfocusedLabelColor = Color.Black, // Color of the label when unfocused
                    disabledLabelColor = Color.Gray // Color of the label when disabled
                ),modifier = Modifier
                    .fillMaxWidth(0.8f)

            )

        }
        Row(horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()) {
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                placeholder= { Text("Enter Your Address") },
                maxLines = 3,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black, // Color of the text
                    focusedBorderColor = Color(0xFF46A683), // Border color when focused
                    unfocusedBorderColor = Color.Black, // Border color when unfocused
                    disabledBorderColor = Color.Gray, // Border color when disabled
                    cursorColor = Color(0xFF46A683), // Color of the cursor
                    focusedLabelColor = Color(0xFF46A683), // Color of the label when focused
                    unfocusedLabelColor = Color.Black, // Color of the label when unfocused
                    disabledLabelColor = Color.Gray // Color of the label when disabled
                ),modifier = Modifier
                    .fillMaxWidth(0.8f).height(80.dp)

            )

        }
        Row(horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()) {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrption") },
                placeholder= { Text("Enter some other details about your pet ") },
                maxLines = 3,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black, // Color of the text
                    focusedBorderColor = Color(0xFF46A683), // Border color when focused
                    unfocusedBorderColor = Color.Black, // Border color when unfocused
                    disabledBorderColor = Color.Gray, // Border color when disabled
                    cursorColor = Color(0xFF46A683), // Color of the cursor
                    focusedLabelColor = Color(0xFF46A683), // Color of the label when focused
                    unfocusedLabelColor = Color.Black, // Color of the label when unfocused
                    disabledLabelColor = Color.Gray // Color of the label when disabled
                ),modifier = Modifier
                    .fillMaxWidth(0.8f).height(80.dp)

            )

        }
        Row(horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()) {
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                placeholder= { Text("Enter Price in Rs") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black, // Color of the text
                    focusedBorderColor = Color(0xFF46A683), // Border color when focused
                    unfocusedBorderColor = Color.Black, // Border color when unfocused
                    disabledBorderColor = Color.Gray, // Border color when disabled
                    cursorColor = Color(0xFF46A683), // Color of the cursor
                    focusedLabelColor = Color(0xFF46A683), // Color of the label when focused
                    unfocusedLabelColor = Color.Black, // Color of the label when unfocused
                    disabledLabelColor = Color.Gray // Color of the label when disabled
                ),modifier = Modifier
                    .fillMaxWidth(0.8f)

            )

        }


        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier

                    .clickable {
                        imageSelectionLauncher.launch("image/*")
                        imgname = "Image Select"

                    }
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    label = { Text(imgname) },
                    enabled = false,
                    placeholder = { Text("Select Image of pet") },
                    trailingIcon = {

                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Image Icon",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(25.dp)
                        )

                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black, // Color of the text
                        disabledTextColor = Color.Black,
                        focusedBorderColor = Color(0xFF46A683), // Border color when focused
                        unfocusedBorderColor = Color.Black, // Border color when unfocused
                        disabledBorderColor = Color.Black, // Border color when disabled
                        cursorColor = Color(0xFF46A683), // Color of the cursor
                        focusedLabelColor = Color(0xFF46A683), // Color of the label when focused
                        unfocusedLabelColor = Color.Black, // Color of the label when unfocused
                        disabledLabelColor = Color.Black // Color of the label when disabled
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                val intent = Intent(con, Myads::class.java)
                Button(onClick = {
                    onButtonClick()
                    intent.putExtra("ID", id)
                    con.startActivity(intent) }, modifier = Modifier.width(250.dp).height(50.dp),colors = ButtonDefaults.buttonColors(Color(0xFF46A683)),
                    shape = RoundedCornerShape(3.dp)) {
                    Text("Post Ad")
                }
            }


        }


    }
}







@Composable
fun CustomDropdown(
    initialCategory: String,
    categories: List<String>,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(initialCategory) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .border(
                width = 0.8.dp,
                color = Color.Black,
                shape = RoundedCornerShape(4.dp),

                ), contentAlignment = Alignment.Center
    ) {
        Row (modifier = Modifier.height(55.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            Text(
                text = selectedCategory,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Icon",
                modifier = Modifier
                    .size(30.dp)
                    .padding(bottom = 2.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .background(color = Color(0xFFF2F2F2))
        ) {
            categories.forEach { category ->
                DropdownMenuItem(text = { Text(category) }, onClick = {
                    selectedCategory = category
                    expanded = false
                    onCategorySelected(category)
                })

            }
        }
    }
}

