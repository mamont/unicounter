package info.hntr.universalcounter.views

import info.hntr.universalcounter.models.Descriptor


interface BaseView {
    val type: String

    fun setDescriptor(descriptor: Descriptor)
}