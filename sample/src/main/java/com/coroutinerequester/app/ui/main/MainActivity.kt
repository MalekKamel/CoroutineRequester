package com.coroutinerequester.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coroutinerequester.app.R
import com.coroutinerequester.app.ui.restaurants.RestaurantsFrag
import com.sha.kamel.navigator.FragmentNavigator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            FragmentNavigator(this, R.id.mainFrame)
                    .add(RestaurantsFrag.newInstance(), false)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}
