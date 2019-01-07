package info.hntr.universalcounter.views

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.widget.Button

import info.hntr.universalcounter.R
import info.hntr.universalcounter.models.Descriptor

class CounterView(context: Context) : BaseView, LinearLayout(context) {
    override val type = "counter"

    private val label : TextView = TextView(context)

    private lateinit var mPreviousButton: Button
    private lateinit var mNextButton: Button

    init {
        initializeViews(context)
    }

    private fun initializeViews(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.counter_layout, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mPreviousButton = this
            .findViewById<View>(R.id.sidespinner_view_previous) as Button
        mPreviousButton
            .setBackgroundResource(android.R.drawable.ic_media_previous)

        mNextButton = this
            .findViewById<View>(R.id.sidespinner_view_next) as Button
        mNextButton
            .setBackgroundResource(android.R.drawable.ic_media_next)
    }

    override fun setDescriptor(descriptor: Descriptor) {
        label.text = "Counter value: " + descriptor.id
    }
}

