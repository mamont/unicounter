package info.hntr.universalcounter

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.*

import io.reactivex.Observable

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
