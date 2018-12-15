package info.hntr.universalcounter.views

import android.content.Context
import android.view.View

abstract class BaseView(context: Context) : View(context) {
    abstract val type: String
}