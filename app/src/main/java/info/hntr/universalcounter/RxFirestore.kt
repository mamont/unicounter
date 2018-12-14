package info.hntr.universalcounter

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.*

import io.reactivex.Observable

class RxFirestore {
    enum class EventType {
        ADDED,
        MOVED,
        CHANGED,
        REMOVED
    }
    class Event(
        val type: EventType,
        val snapshot: QuerySnapshot,
        val previousChildName: String? = null
    ) {}
}

fun Query.observeValueSnapshot(): Observable<QuerySnapshot> {
    return Observable.create { emitter ->
        addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(ContentValues.TAG, "Listen failed.", error)
                emitter.onError(error)
            }
            emitter.onNext(snapshot!!)
        }
    }
}

