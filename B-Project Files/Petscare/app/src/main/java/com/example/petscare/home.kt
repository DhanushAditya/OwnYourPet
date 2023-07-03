package com.example.petscare

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.petscare.home.Companion.gemail
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



class home : AppCompatActivity() {

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
            Home(email)
        }
        // Use the email as needed in your home activity
    }
}


data class UserChat(
    val userid:String = "",
    val userList: List<String> = emptyList()

)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(Email : String? ) {

    var isExpanded by remember {
        mutableStateOf(false)
    }
    var showMenu by remember {
        mutableStateOf(false)
    }
    val database = Firebase.database
    val myRef = database.getReference("Profile")
    var username by remember {
        mutableStateOf("")
    }
    var phoneNo by remember {
        mutableStateOf("")
    }
    var userId by remember {
        mutableStateOf("")
    }
    val con = LocalContext.current
    val intent = Intent(con, chatusers::class.java)
    var fg by remember {
        mutableStateOf(false)
    }
    var fgl by remember {
        mutableStateOf(false)
    }


    myRef.addValueEventListener(object: ValueEventListener {

        override fun onDataChange(datasnapshot: DataSnapshot) {
            for (snapshot in datasnapshot.children) {
                val user = snapshot.getValue(User::class.java)
                if (user != null && user.email == gemail) {
                    username = user.username
                    phoneNo = user.phoneNo
                    userId = user.userId
                    fg=true
                    // Do something with the fetched username and phoneNo
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to read value.", error.toException())
        }

    })

    Scaffold {
        it
        Column(horizontalAlignment = Alignment.End,modifier = Modifier

            .fillMaxSize())  {
            AttractiveTopBox2()

            if(fg){

                SearchField(userId)
                List(userId)
            }
        }
        if (showMenu) {
            LeftSideNavigationMenu(username, phoneNo,userId)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Align items to start and end
        ) {
            IconButton(
                onClick = { /* Handle icon click */ },
                modifier = Modifier.padding(top = 10.dp, end = 6.dp)
            ) {
                ProfileIconButton(
                    onClick = {showMenu = !showMenu},
                    icon = (if(showMenu){
                        painterResource(R.drawable.undo)
                    } else{
                        painterResource(R.drawable.user)
                    }) as Painter,
                    contentDescription = "Profile"
                )
            }


            IconButton(
                onClick = {

                    intent.putExtra("ID", userId)
                    con.startActivity(intent)
                          },
                modifier = Modifier.padding(top = 12.dp, end = 8.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.chatting), // Replace with your own image resource
                    contentDescription = "Chat Icon",
                    modifier = Modifier.size(32.dp) // Adjust the size of the image
                )
            }

            // Add other items or content to the Row here
        }
        val database = Firebase.database
        val userChatRef = database.getReference("UserChat")


        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier
            .fillMaxSize()
            .padding(end = 3.dp, bottom = 3.dp), horizontalAlignment = Alignment.End) {
            Row() {
                val con = LocalContext.current
                val intent = Intent(con, chatbot::class.java)

                InfiniteLottieAnimation(
                    animationResId = R.raw.robot,
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.Transparent),
                    Email = "example@example.com",
                    onClick = {
                        // Handle animation click here
                        intent.putExtra("EMAIL", Email)
                        con.startActivity(intent)
                    }
                )

            }
        }

    }
}


@Composable
fun LeftSideNavigationMenu(username: String, phoneNo: String,userid:String) {

    val con = LocalContext.current
    val intent = Intent(con, MainActivity::class.java)


    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.6f)
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF2F2F2))
        ) {
            // User Image
            Row(horizontalArrangement =Arrangement.Center, verticalAlignment = Alignment.CenterVertically , modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(Color(0xFF46A683))){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 8.dp, end = 10.dp)
                ) {
                    Image(


                        painter = painterResource(R.drawable.profile2),
                        contentDescription = "User Image",
                        modifier = Modifier
                            .size(70.dp)

                    )

                    // User Name
                    Text(
                        text = username, // Replace with the actual user name
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            Column(modifier = Modifier.fillMaxSize()) {
                gemail?.let {
                    TextFieldWithLeadingIcon(
                        leadingIcon = Icons.Default.Email,
                        placeholder = it,
                        modifier = Modifier.fillMaxWidth()
                    )
                }


                TextFieldWithLeadingIcon(
                    leadingIcon = Icons.Default.Phone,
                    placeholder = phoneNo,
                    modifier = Modifier.fillMaxWidth()
                )
                val customIcon = painterResource(R.drawable.laptop)

                TextFieldWithLeadingIcon2(
                    leadingIcon = customIcon,
                    placeholder = "My Ads",

                ) {
                    // Handle the click event
                    val intent = Intent(con, Myads::class.java)
                    intent.putExtra("ID", userid)
                    con.startActivity(intent)

                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(5.dp))
                    ) {
                        var interactionSource = remember { MutableInteractionSource() }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .background(Color(0xFF46A683), shape = RoundedCornerShape(5.dp))
                                .padding(8.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    val inte = Intent(con, getProducts::class.java)
                                    inte.putExtra("ID", userid)
                                    con.startActivity(inte)
                                }
                        ) {
                            Text(
                                text = "Shop Products",
                                color = Color(0xFFF2F2F2),
                                fontWeight = FontWeight.Medium,
                                fontSize = 17.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                painter = painterResource(R.drawable.shoppingcart),
                                contentDescription = null,
                                tint = Color(0xFFF2F2F2),
                                modifier = Modifier.size(23.dp)
                            )
                        }
                    }


                }

                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()){
                    ButtonWithLeadingIcon(
                        onClick = {
                            con.startActivity(intent)
                                  },
                        leadingIcon = painterResource(R.drawable.exit) ,
                        content = {
                            Text(
                                text = "Logout",
                                color = Color.Black,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp
                            )
                        }
                    )

                }

            }



        }
    }
}
@Composable
fun ButtonWithLeadingIcon(
    onClick: () -> Unit,
    leadingIcon: Painter,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .fillMaxWidth(0.4f)
            .padding(bottom = 10.dp)
            .background(color = Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = leadingIcon,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(23.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            content()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithLeadingIcon(
    leadingIcon: ImageVector,
    placeholder: String,
    modifier: Modifier = Modifier,

) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = Color(0xFFF2F2F2)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(24.dp)
            )
            Text(
                text = placeholder,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(color = Color.Black)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithLeadingIcon2(
    leadingIcon: Painter,
    placeholder: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(
                onClick = onClick
            )
            .background(color = Color(0xFFF2F2F2)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = leadingIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(24.dp)
            )
            Text(
                text = placeholder,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(color = Color.Black)
            )
        }
    }
}


@Composable
fun InfiniteLottieAnimation(
    animationResId: Int,
    modifier: Modifier = Modifier,
    animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 700,
        easing = LinearEasing
    ),
    Email: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    var play by remember { mutableStateOf(true) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = play,
        restartOnPlay = true
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            play = true
        }
        if (progress == 0f) {
            play = true
        }
    }

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(Color.Transparent)
    ) {
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.fillMaxSize()
        )
    }
}







@Composable
fun IconButtonItem(
    onClick: () -> Unit,
    icon: Painter,
    contentDescription: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(36.dp)
            .scale(4.4f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (isPressed) 0.8f else 1.0f) // Adjust the alpha value as desired
        )
    }
}


@Composable
private fun Search(
    searchText: String,
    onClearSearch: () -> Unit
) {
    // Show clear icon when search text is not empty
    if (searchText.isNotEmpty()) {
        Column(horizontalAlignment = Alignment.End) {
            IconButton(
                onClick = { onClearSearch() },

                ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Search"
                )
            }
        }
    } else {
        // Show search icon when search text is empty
        Column(horizontalAlignment = Alignment.End) {
            IconButton(
                onClick = { /* Perform search action */ },

                ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"

                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(id:String) {
    // Mutable state to hold the current search text


    var isDropdownMenuVisible by remember { mutableStateOf(false) }
   val focusRequester = remember { FocusRequester() }

    // Create a FocusManager instance
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 7.dp)
            .height(50.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.width(230.dp)) {
            TextField(
                value = "",
                onValueChange = {  },
                placeholder = {
                    Text(
                        text = "Search Ads",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFA7BEAE),
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFFA7BEAE),
                    textColor = Color.Black
                ),
                trailingIcon = {
                    Search(
                        searchText = "",
                        onClearSearch = {

                            focusManager.clearFocus()
                        }
                    )
                }
            )
            IconButton(
                onClick = { isDropdownMenuVisible = !isDropdownMenuVisible },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter Icon"
                )
            }
        }
        val con = LocalContext.current
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(end = 7.dp)
                .width(140.dp),

            ) {
            Surface(shadowElevation = 15.dp) {
                DropdownMenu(
                    expanded = isDropdownMenuVisible,
                    onDismissRequest = { isDropdownMenuVisible = false },
                    modifier = Modifier
                        .padding(top = 2.dp, end = 2.dp)
                        .width(50.dp)
                        .background(color = Color.White)
                ) {
                    DropdownMenuItem(
                        onClick = {

                        },
                        text = { Text("") },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        leadingIcon = {
                            IconButtonItem(
                                onClick = {


                                    isDropdownMenuVisible = false // Close the dropdown menu
                                },
                                icon = painterResource(R.drawable.dog1),
                                contentDescription = "Custom Icon"
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            // Close the dropdown menu

                        },
                        text = { Text("") },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        leadingIcon = {
                            IconButtonItem(
                                onClick = {

                                    isDropdownMenuVisible = false // Close the dropdown menu
                                },
                                icon = painterResource(R.drawable.cat1),
                                contentDescription = "Custom Icon"
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            // Close the dropdown menu

                        },
                        text = { Text("") },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        leadingIcon = {
                            IconButtonItem(
                                onClick = {


                                    isDropdownMenuVisible = false // Close the dropdown menu
                                },
                                icon = painterResource(R.drawable.bird),
                                contentDescription = "Custom Icon"
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            // Close the dropdown menu

                        },
                        text = { Text("") },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        leadingIcon = {
                            IconButtonItem(
                                onClick = {

                                    isDropdownMenuVisible = false // Close the dropdown menu
                                },
                                icon = painterResource(R.drawable.fish1),
                                contentDescription = "Custom Icon"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AttractiveTopBox2() {
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
fun ProfileIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter,
    contentDescription: String
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Image(
            painter = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(28.dp)
        )
    }
}

data class ads(
    val cat: String = "",
    val userID: String = "",
    val date: String = "",
    val age: String = "",
    val gender: String = "",
    val breed: String = "",
    val description: String = "",
    val address: String = "",
    val price: String = "",
    val imageID: Uri?,
    val active: String = ""
)
@Composable
fun List(id:String) {
    var adDetails by remember { mutableStateOf(emptyList<ads>()) }
    var flag by remember { mutableStateOf(false) }

    val cont = LocalContext.current

    LaunchedEffect(Unit) {
        val adsRef = Firebase.database.reference.child("Ads")
        adsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val details = mutableListOf<ads>()
                for (childSnapshot in snapshot.children) {
                    val cat = childSnapshot.child("cat").getValue(String::class.java) ?: ""

                    val userID = childSnapshot.child("userID").getValue(String::class.java) ?: ""

                    val date = childSnapshot.child("date").getValue(String::class.java) ?: ""
                    val age = childSnapshot.child("age").getValue(String::class.java) ?: ""
                    val gender = childSnapshot.child("gender").getValue(String::class.java) ?: ""
                    val breed = childSnapshot.child("breed").getValue(String::class.java) ?: ""
                    val description = childSnapshot.child("descrption").getValue(String::class.java) ?: ""
                    val address = childSnapshot.child("address").getValue(String::class.java) ?: ""
                    val price = childSnapshot.child("price").getValue(String::class.java) ?: ""
                    val imageIDString = childSnapshot.child("imageID").getValue(String::class.java) ?: ""
                    val imageID = if (imageIDString.isNotEmpty()) Uri.parse(imageIDString) else null
                    val active = childSnapshot.child("active").getValue(String::class.java) ?: ""


                    if (userID != id) {

                        val ad = ads(cat, userID, date, age, gender, breed, description, address, price, imageID, active)
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
    val con = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .background(color = Color.White),
    ) {
        if (flag) {
//            var filteredAds = adDetails.filter { ad ->
//                when {
//                    filter != "" -> ad.cat == filter
//                    searchCon != "" -> ad.breed.contains(searchCon, ignoreCase = true)
//                    else -> true
//                }
//            }
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 180.dp)) {
                items(adDetails) { ad ->
                    // Show toast using coroutine
                    Surface(modifier = Modifier.padding(3.dp),shadowElevation = 5.dp) {
                        Pcard(ad,id)
                    }
                }
            }
        } else {
            // Show loading or empty state
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Loading...",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        }
    }
}






@Composable
fun Pcard(data: ads,id:String) {
    Surface(
        modifier = Modifier
            .width(128.dp)
            .height(280.dp)
            .border(width = 0.5.dp, color = Color.LightGray),

    ) {
        Column(

            modifier = Modifier.padding(15.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
            LoadImage(data.imageID, contentDescription = "", modifier = Modifier.size(162.dp))
            }

            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    text = data.breed +" - ${data.gender}" ,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(start = 0.07.dp)
                        .offset(y = 0.09.dp)
                )
            }
            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)) {
                Text(
                    text = data.description,
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(start = 0.07.dp)
                        .offset(y = 0.09.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .height(70.dp)
                    .padding(start = 0.1.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)
            ) {
                Text(
                    text = "Rs. ${data.price}",
                    fontSize = 17.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(start = 0.1.dp)
                        .width(100.dp)

                )
                val con = LocalContext.current
                val intent = Intent(con, ChatPage::class.java)
                val database = Firebase.database
                val userChatRef = database.getReference("UserChat")
                Box(
                    modifier = Modifier
                        .height(70.dp)
                        .width(70.dp)

                        .clickable(onClick = {
                            var newUserId = data.userID
                            userChatRef
                                .child(id)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // User already exists, update the userList
                                            val existingUserChat =
                                                dataSnapshot.getValue(UserChat::class.java)
                                            val userList = existingUserChat?.userList
                                                .orEmpty()
                                                .toMutableList()
                                            if (!userList.contains(newUserId)) {
                                                userList.add(newUserId) // Add the new ID to the existing userList
                                                userChatRef
                                                    .child(id)
                                                    .child("userList")
                                                    .setValue(userList)
                                            }

                                        } else {
                                            // User does not exist, create a new entry with the new ID
                                            val newUserList = listOf(newUserId)
                                            val newUserChat = UserChat(id, newUserList)
                                            userChatRef
                                                .child(id)
                                                .setValue(newUserChat)
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle any errors here
                                    }
                                })
                            intent.putExtra("SID", id)
                            intent.putExtra("RID", data.userID)
                            con.startActivity(intent)
                        })
                        .border(width = 2.dp, Color(0xFF46A683), shape = RoundedCornerShape(5.dp))
                        .background(Color(0xFF46A683), shape = RoundedCornerShape(5.dp))
                    ,
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Chat",
                        fontSize = 16.sp, // Modify the font size as desired
                        color = Color.White,
                        fontWeight = FontWeight.Medium // Modify the font weight as desired
                    )

                }
            }

        }
    }
}
@Composable
fun LoadImage(
    imageUri: Uri?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val imageBitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    if (imageUri != null) {
        LaunchedEffect(imageUri) {
            val storageRef = Firebase.storage.reference.child("images/${imageUri}")
            val maxImageSize = Int.MAX_VALUE // Accept images of any size
            try {
                val byteData = withContext(Dispatchers.IO) {
                    storageRef.getBytes(maxImageSize.toLong()).await()
                }
                val bitmap = BitmapFactory.decodeByteArray(byteData, 0, byteData.size)
                imageBitmap.value = bitmap
            } catch (e: Exception) {
                // Handle error loading image
                Log.e("LoadImage", "Error loading image: ${e.message}")
            }
        }
    }

    imageBitmap.value?.let { bitmap ->
        val imageSize = 180.dp // Adjust the desired size of the displayed image
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
                .size(imageSize)
                .clip(RoundedCornerShape(4.dp)) // Optional: Apply rounded corners to the image
        )
    } ?: run {
        // Show a placeholder or loading indicator if imageBitmap is null
        Box(
            modifier = modifier
                .size(120.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.Black)
        }
    }
}



