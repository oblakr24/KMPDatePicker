package com.rokoblak.kmpdatepicker

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import di.ComponentCreator
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ref = WeakReference(this as ComponentActivity)
        val componentCreator = ComponentCreator(application.applicationContext, ref)
        val component = componentCreator.createAppComponent()

        setContent {
            App(component = component)
        }
    }
}