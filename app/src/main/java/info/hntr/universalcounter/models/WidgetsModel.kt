package info.hntr.universalcounter.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.*
import info.hntr.universalcounter.observeValueSnapshot
import java.util.HashMap

data class Descriptor(val id : String)

class WidgetsModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "WidgetsModel"

    private lateinit var descriptors: MutableLiveData<List<Descriptor>>
    private lateinit var db : FirebaseFirestore

    private val descriptorsRaw: ArrayList<Descriptor> = ArrayList()

    fun getWidgets() : LiveData<List<Descriptor>> {
        if (!::descriptors.isInitialized) {
            init()
        }
        return descriptors
    }

    fun addCounter(counter: Descriptor) {
        val entry = HashMap<String, Any>()
        entry.put("first", "Mi")
        entry.put("last", "Tor")
        entry.put("born", 2012)

        db.collection("users")
            .add(entry)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Entry added successfully")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error while adding entry", e)
            }
    }

    private fun init() {
        descriptors = MutableLiveData()
        setupFirebase()
        setupListener()
    }

    private fun setupFirebase() {
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
    }

    private fun cloneDescriptorsList() : List<Descriptor> {
        return ArrayList(descriptorsRaw)
    }

    private fun setupListener() {
        db.collection("users")
            .observeValueSnapshot()
            .flatMapIterable { snapshot -> snapshot.documentChanges }
            .subscribe {
                x -> when(x.type) {
                    DocumentChange.Type.ADDED -> addElement(x.document)
                    DocumentChange.Type.REMOVED -> removeElement(x.document)
                }
                descriptors.postValue(cloneDescriptorsList())
            }
    }

    private fun addElement(snapshot: QueryDocumentSnapshot) {
        descriptorsRaw.add(Descriptor(snapshot.id))
    }

    private fun removeElement(snapshot: QueryDocumentSnapshot) {
        descriptorsRaw.removeIf { el -> el.id == snapshot.id }
    }
}