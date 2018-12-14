package info.hntr.universalcounter

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class MainActivity : Activity(), View.OnClickListener {

    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addElementBtn.setOnClickListener(this)
        readElementBtn.setOnClickListener(this)

        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.setFirestoreSettings(settings)

        setupListener()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun addEntryToFireStore() {
        val entry = HashMap<String, Any>()
        entry.put("first", "Mi")
        entry.put("last", "Tor")
        entry.put("born", 2012)

        db.collection("users")
            .add(entry)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "xxxx")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
            }
    }

    private fun readEntryFromFireStore() {
        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: " + task.result)
                    } else {
                        Log.d(TAG, "No such document")
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.exception)
                }
            }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.addElementBtn -> addEntryToFireStore()
            R.id.readElementBtn -> readEntryFromFireStore()
        }
    }

    fun setupListener() {
        val ref = db.collection("users")
        ref.addSnapshotListener(EventListener<QuerySnapshot> { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@EventListener
            }
        })
    }
}
