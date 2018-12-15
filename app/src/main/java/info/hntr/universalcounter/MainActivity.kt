package info.hntr.universalcounter

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.firebase.firestore.*
import info.hntr.universalcounter.models.Descriptor
import info.hntr.universalcounter.models.WidgetsModel
import info.hntr.universalcounter.views.CounterFragment
import kotlinx.android.synthetic.main.activity_main.*

import androidx.lifecycle.ViewModelProvider

class MainActivity : FragmentActivity(), View.OnClickListener {

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
        db.firestoreSettings = settings

        setupListener()
        setupRxSource()


        val model = ViewModelProvider.NewInstanceFactory().create(WidgetsModel::class.java)
        model.getWidgets().observe(this, Observer<List<Descriptor>>{ descriptor ->
            // update UI
            Log.d(TAG, "xxx")
        })

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

    private fun setupRxSource() {
        val ref = db.collection("users")
        ref.observeValueSnapshot()
            .flatMapIterable { snapshot -> snapshot.documentChanges }
            .subscribe {
                x -> when(x.type) {
                    DocumentChange.Type.ADDED -> addWidget(x.document)
                }
            }
    }

    private fun addWidget(doc : QueryDocumentSnapshot) {
        Log.d(TAG, "Got document: ${doc.id}")

        //val widget = createWidget(doc)
        //if (widget != null) {
        //    val transaction = supportFragmentManager.beginTransaction()
        //    transaction.add(R.id.widgetsLayout, widget)
        //    transaction.commit()
        //}
    }

    private fun createWidget(doc : QueryDocumentSnapshot) : Fragment? {
        return when(doc.get("type")) {
            //"counter" -> CounterFragment.newInstance("a", "b")
            else -> CounterFragment.newInstance("a", "b")
        }
    }

}
