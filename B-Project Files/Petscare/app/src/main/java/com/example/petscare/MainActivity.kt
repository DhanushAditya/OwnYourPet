@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.petscare

import FirebaseUtils.sendPasswordResetEmail
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.util.PatternsCompat
import com.example.petscare.ui.theme.PetscareTheme
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.Serializable
//import com.google.android.gms.common.internal.safeparcel.SafeParcelable
//import com.google.firebase.FirebaseApp
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.util.regex.Pattern


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetscareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }

    }
}

@Serializable
data class products(
    val name : String = "",
    val pdid : String = "",
    val desc : String = "",
    val img : Int,
    val brand : String = "",
    val cat : String = "",
    val fea : String= "",
    val  price : Int
)
@Composable
fun AttractiveTopBox() {
    Card(
        modifier = Modifier
            .background(Color(0xFF46A683))
            .fillMaxWidth()
            .height(70.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF46A683))
        ) {
            Row() {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Dog Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .size(200.dp)
                        .padding(start = 10.dp)
                        .align(Alignment.Top)
                )
//                Image(
//                    painter = painterResource(R.drawable.cartoondog),
//                    contentDescription = "Dog Image",
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier
//                        .padding(top = 18.dp)
//                        .size(40.dp)
//                        .padding(start = 10.dp)
//                        .align(Alignment.Top)
//                )
            }
        }
    }
}


@Composable
fun LoginPage(onForget: () -> Unit , onLogin: () -> Unit, onSignUp: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFE7E8D1))


    ) {


        AttractiveTopBox()
        var username by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        val con = LocalContext.current

        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        Box(modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color.White))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 140.dp)
                .shadow(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(6.dp),
                    clip = true
                )
                .background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 20.dp,top=20.dp,end=20.dp, bottom = 30.dp)
            ) {

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                        .height(56.dp),
                    label = { Text(text = "E-mail") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFA7BEAE),
                        unfocusedIndicatorColor = Color(0xFFA7BEAE),
                        cursorColor = Color(0xFFA7BEAE),
                        textColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(56.dp),
                    label = { Text(text = "Password") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFA7BEAE),
                        unfocusedIndicatorColor = Color(0xFFA7BEAE),
                        cursorColor = Color(0xFFA7BEAE),
                        textColor = Color.Black
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        // Localized description for accessibility services
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        // Toggle button to hide or display password
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.padding(top = 10.dp),
                ) {
                    TextButton(
                        onClick = {
                            onForget()
                        }

                    ){
                        Text(
                            text = "Forgot Password ?",
                            color = Color(0xFF46A683), // Set the desired text color here
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                        )
                    }
                }

                var loginCheck by remember {
                    mutableStateOf(false)
                }
                Spacer(modifier = Modifier.height(12.dp))

                val intent = Intent(con, home::class.java)
                val intent2 = Intent(con, dashboard::class.java)
                Button(

                    onClick = {
                        loginCheck =! loginCheck
                        if(username == "Admin" && password =="admin@123"){
                            showToast(con, "Welcome Admin")

                            con.startActivity(intent2)
                        }
                        else {
                            try {
                                FirebaseUtils.signInWithEmailAndPassword(username, password,
                                    onSuccess = {


                                        showToast(con, "Welcome")
                                        intent.putExtra("EMAIL", username)
                                        con.startActivity(intent)


                                    },
                                    onError = { errorMessage ->
                                        val formattedErrorMessage = " ${errorMessage.removePrefix("java.lang.IllegalArgumentException:")}"
                                        showToast(con, formattedErrorMessage)
                                    }
                                )
                            } catch (e: Exception) {
                                showToast(con, e.toString())
                            }
                        }




                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF46A683)),//Color(0xFF46A683)
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 9.dp, horizontal = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Text(
                        text = "or",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = onSignUp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp)
                        .height(56.dp), border = BorderStroke(2.dp, Color(0xFF46A683)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2F2F2)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "SignUp",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF46A683)
                    )
                }

            }
        }
    }

}
data class User(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val phoneNo: String = "",
    val password: String = ""
)

@Composable
fun signup(onLogin: () -> Unit, onSignUp: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE7E8D1))
    ) {
        AttractiveTopBox()

        var username by remember {
            mutableStateOf("")
        }
        var email by remember {
            mutableStateOf("")
        }
        var phoneNo by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        var confpass by remember {
            mutableStateOf("")
        }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        var confpassVisible by rememberSaveable { mutableStateOf(false) }
        Box(modifier = Modifier.clip(RoundedCornerShape(16.dp))
            .background(color = Color.White))
        Box(
            modifier = Modifier
                .fillMaxWidth().padding(start = 22.dp, end = 22.dp, top = 90.dp)
                .shadow(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(6.dp),
                    clip = true
                ).background(color = Color.White),
            contentAlignment = Alignment.Center
        )  {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 4.dp,end=4.dp)
            ) {

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                        .height(56.dp),
                    label = { Text(text = "Enter Name") },
                    visualTransformation = VisualTransformation.None,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.textFieldColors(

                        containerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFA7BEAE),
                        unfocusedIndicatorColor = Color(0xFFA7BEAE),
                        cursorColor = Color(0xFFA7BEAE),
                        textColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .height(56.dp),
                    label = { Text(text = "E-mail") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFA7BEAE),
                        unfocusedIndicatorColor = Color(0xFFA7BEAE),
                        cursorColor = Color(0xFFA7BEAE),
                        textColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = phoneNo,
                    onValueChange = { phoneNo = it },
                    label = { Text(text = "Phone Number") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFA7BEAE),
                        unfocusedIndicatorColor = Color(0xFFA7BEAE),
                        cursorColor = Color(0xFFA7BEAE),
                        textColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    keyboardActions = KeyboardActions(onDone = { /* Handle the Done action if needed */ }),
                    leadingIcon = {
                        Text(
                            text = "+91",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                )
                var passwordsMatch by remember { mutableStateOf(true) }
                var passlen by remember { mutableStateOf(true) }
                var cpasslen by remember { mutableStateOf(true) }
                val con = LocalContext.current

                DisposableEffect(confpass) {
                    if (password != confpass) {
                        passwordsMatch = false

                    } else {
                        passwordsMatch = true
                    }
                    if (confpass.length >=8) {
                        cpasslen = true

                    } else {
                        cpasslen = false
                    }

                    onDispose { }
                }

                DisposableEffect(password) {
                    if (password.length >=8) {
                        passlen = true

                    } else {
                        passlen = false
                    }

                    onDispose { }
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(56.dp).height(56.dp).border(
                            width = 2.dp,
                            color = if (passlen) Color(0xFF46A683) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    label = { Text(text = "Password") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFA7BEAE),
                        unfocusedIndicatorColor = Color(0xFFA7BEAE),
                        cursorColor = Color(0xFFA7BEAE),
                        textColor = Color.Black
                    ),

                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        // Localized description for accessibility services
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        // Toggle button to hide or display password
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = confpass,
                    onValueChange = { confpass = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(56.dp).border(
                            width = 2.dp,
                            color = (
                                    (if (passwordsMatch ) {

                                        if(cpasslen){
                                            Color(0xFF46A683)
                                        } else {
                                            Color.Transparent
                                        }
                                    }
                                    else{
                                        Color.Red
                                    }) as Color),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    label = { Text(text = "Confirm Password") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFA7BEAE),
                        unfocusedIndicatorColor = Color(0xFFA7BEAE),
                        cursorColor = Color(0xFFA7BEAE),
                        textColor = Color.Black
                    ),
                    visualTransformation = if (confpassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confpassVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        // Localized description for accessibility services
                        val description = if (confpassVisible) "Hide password" else "Show password"

                        // Toggle button to hide or display password
                        IconButton(onClick = { confpassVisible = !confpassVisible }) {
                            Icon(imageVector = image, description)
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                )

                val database = Firebase.database
                val myRef = database.getReference("Profile")
                val intent = Intent(con, home::class.java)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (!isAllFieldsEntered(username, email, phoneNo, password, confpass)) {
                            Toast.makeText(con, "Please fill in all fields", Toast.LENGTH_LONG).show()
                        } else if (!isValidEmail(email)) {
                            Toast.makeText(con, "Invalid email format", Toast.LENGTH_LONG).show()
                        } else if (!isValidPhoneNumber(phoneNo)) {
                            Toast.makeText(con, "Invalid phone number", Toast.LENGTH_LONG).show()
                        }
                        else if(!passwordsMatch){
                            Toast.makeText(
                                con,
                                "Password Mismatch",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else if(!passlen){
                            Toast.makeText(
                                con,
                                "Password Length Must be atleast 8",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else{
                            val userId = myRef.push().key // Generate unique ID for the user
                            val user = userId?.let { User(it, username, email, phoneNo, password) }
                            if (userId != null) {
                                myRef.child(userId).setValue(user)
                            }
                            // Optional: You can also use `setValue` with a specific key if you have a user ID.
                            // myRef.child(userId).setValue(user)
                            FirebaseUtils.signUpWithEmailAndPassword(email, password,
                                onSuccess = {
                                    intent.putExtra("EMAIL", email)
                                    con.startActivity(intent)
                                },
                                onError = { errorMessage ->
                                    showToast(con, errorMessage)
                                }
                            )
                            // Call the onSignUp callback if needed

                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF46A683)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "SignUp",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 9.dp, horizontal = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Text(
                        text = "or",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Divider(
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = onLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                        .height(56.dp), border = BorderStroke(2.dp, Color(0xFF46A683)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2F2F2)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF46A683)
                    )
                }
            }

        }

    }
}
private fun isAllFieldsEntered(vararg fields: String): Boolean {
    return fields.all { it.isNotEmpty() }
}
private fun isValidEmail(email: String): Boolean {
    val pattern = PatternsCompat.EMAIL_ADDRESS
    return pattern.matcher(email).matches() && email.endsWith("gmail.com")
}

private fun isValidPhoneNumber(phone: String): Boolean {
    val pattern = Pattern.compile("\\d{10}")
    return pattern.matcher(phone).matches()
}

@Composable
fun ForgotPasswordPage( onLogin: () -> Unit) {
    // State variables

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE7E8D1))
    ) {
        AttractiveTopBox()
        var email by remember {
            mutableStateOf("")
        }
        val con = LocalContext.current
        Box(
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
                .background(color = Color.White)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth().padding(start = 22.dp, end = 22.dp, top = 160.dp)
                .shadow(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(6.dp),
                    clip = true
                ).background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(

                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)

            ) {
                // Email field
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp,top=20.dp, bottom = 20.dp, end = 20.dp)
                        .height(56.dp),
                    label = { Text(text = "E-mail",color= Color.Black) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFE7E8D1),
                        focusedIndicatorColor = Color(0xFFA7BEAE),
                        unfocusedIndicatorColor = Color(0xFFA7BEAE),
                        cursorColor = Color(0xFFA7BEAE),
                        textColor = Color.Black
                    ),

                )

                // Reset Password button

                Button(
                    onClick = {
                        if (email.isNotEmpty()) {
                            sendPasswordResetEmail(email) { isSuccess ->
                                if (isSuccess) {
                                    showToast(con, "Reset link sent to $email")
                                    onLogin()
                                } else {
                                    showToast(con, "Reset link not sent to $email")
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(start = 20.dp, top=10.dp,end = 20.dp, bottom = 10.dp)
                        .height(56.dp), border = BorderStroke(2.dp, Color(0xFF46A683)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF46A683)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Reset Password",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                }
                // Back to login button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        TextButton(
                            onClick = onLogin,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(
                                text = "Back to Login",
                                color = Color(0xFF46A683),
                                fontSize = 14.sp,
                            )
                        }
                    }

            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp() {


    var currentScreen by remember { mutableStateOf("login") }
    when (currentScreen) {
        "login" -> LoginPage(onForget={currentScreen="forgetpass"} ,onLogin = { currentScreen = "home" }, onSignUp = { currentScreen = "signup" })
        "signup" -> signup(onLogin = { currentScreen = "login" }, onSignUp = { currentScreen = "home" })
        "forgetpass" -> ForgotPasswordPage(onLogin = { currentScreen = "login" })
    }


}

@Composable
fun MainScreen() {
    MyApp()
}