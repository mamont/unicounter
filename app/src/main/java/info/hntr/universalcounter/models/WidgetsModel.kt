package info.hntr.universalcounter.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Descriptor(val id : String)

class WidgetsModel : ViewModel() {
    private lateinit var descriptors: MutableLiveData<List<Descriptor>>

    fun getWidgets() : LiveData<List<Descriptor>> {
        if (!::descriptors.isInitialized) {
            descriptors = MutableLiveData()
            load()
        }
        return descriptors
    }

    private fun load() {}
}