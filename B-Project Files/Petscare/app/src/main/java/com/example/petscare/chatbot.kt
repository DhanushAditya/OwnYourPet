package com.example.petscare

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class chatbot : AppCompatActivity() {
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val email = intent.getStringExtra("EMAIL")
        setContent {
            ChatBotScreen()
        }

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF494F55))
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        // Top bar with attractive TpBar
        AttractiveTopBox()
        val con = LocalContext.current

        val chatMessages = remember { mutableStateListOf<Pair<String, String>>() }
        var question by remember {
            mutableStateOf("")
        }
        // Chatbox
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF414A4C))
        ) {

            val coroutineScope = rememberCoroutineScope()
            val scrollState = rememberScrollState()

            BoxWithConstraints(
                modifier = Modifier
                    .padding(top = 1.dp, bottom = 62.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),contentAlignment = Alignment.BottomEnd
            ) {
                val boxHeight = constraints.maxHeight

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp)
                        .wrapContentHeight(align = Alignment.Bottom)
                ) {
                    chatMessages.forEach { (question, answer) ->
                        ChatMessage(question, answer)
                    }

                    // Scroll to the bottom when a new item is added
                    LaunchedEffect(Unit) {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }

                // Scroll to the bottom on box height change (new item added)
                SideEffect {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
            }


            // Chat input
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp)
            ) {
                TextField(
                    value = question,
                    onValueChange = { newValue ->
                        question = newValue
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .shadow(elevation = 10.dp, clip = true),
                    placeholder = { Text(text = "Type your Query") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFF242124),
                        focusedIndicatorColor = Color(0xFF46A683),
                        unfocusedIndicatorColor = Color(0xFF242124),
                        cursorColor = Color(0xFF46A683),
                        textColor = Color(0xFFacacbe)
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {

                                if(question.trim() != ""){
                                    getResponse(question) { response ->
                                        val chatMessage = Pair(question, response)
                                        chatMessages.add(chatMessage)
                                        question = ""
                                    }
                                }
                                else{
                                    showToast(con, "**Please add your query**")
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sendmsg),
                                contentDescription = null,
                                tint = Color(0xFF46A683),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChatMessage(question: String, answer: String) {

    Box(
        modifier = Modifier.fillMaxWidth().padding(end = 10.dp, start = 10.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .border(1.dp, Color.Transparent)
                .padding(start = 20.dp),

            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .background(Color(0xFF1F8E7D))
                    .padding(start=8.dp,top=8.dp,end=8.dp, bottom = 8.dp)
                    .border(1.dp, Color.Transparent)
            )
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
    Box(
        modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .border(1.dp, Color.Transparent)
                ,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = answer,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .background(Color(0xFF122E32))
                    .padding(start=8.dp,top=8.dp,end=8.dp, bottom = 8.dp)
                    .border(1.dp, Color.Transparent)
            )
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}


private val client = OkHttpClient()

fun getResponse(question:String, callback: (String)-> Unit){
    val ques =  question.trim()
    val url = "https://api.openai.com/v1/completions"
    val key = "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"
    val requestBody = """
        {
          "model": "text-ada-001",
          "prompt": "$ques",
          "max_tokens": 2000,
          "temperature": 0
        }
    """.trimIndent()
    val request = Request.Builder()
        .url(url)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer $key")
        .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
        .build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("error", "API Failed: ${e.message}", e)
        }

        override fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            if(body != null){
                Log.v("data",body)
            }
            else{
                Log.v("data","empty")
            }
            val JSONobj = JSONObject(body)
            val jsonArray:JSONArray = JSONobj.getJSONArray("choices")
            val textResult = jsonArray.getJSONObject(0).getString("text").trimStart { it == '\n' }

            callback(textResult)
        }
    })



}

