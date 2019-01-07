package info.hntr.universalcounter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import info.hntr.universalcounter.models.Descriptor
import info.hntr.universalcounter.models.WidgetsModel

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import info.hntr.universalcounter.models.CounterDescriptor
import info.hntr.universalcounter.views.BaseView
import info.hntr.universalcounter.views.CounterView
import java.util.*

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var counters: List<Descriptor> = ArrayList()
    private lateinit var model: WidgetsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addElementBtn.setOnClickListener(this)
        removeElementBtn.setOnClickListener(this)

        val countersRecyclerView = findViewById<RecyclerView>(R.id.countersView)
        countersRecyclerView.layoutManager = LinearLayoutManager(parent)

        val countersAdapter = CountersAdapter()
        countersRecyclerView.adapter = countersAdapter

        model = ViewModelProviders.of(this).get(WidgetsModel::class.java)
        model.getWidgets().observe(this, Observer<List<Descriptor>>{ descriptors ->

            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() : Int {
                    return counters.size
                }
                override fun getNewListSize() : Int {
                    return descriptors.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) : Boolean {
                    return counters[oldItemPosition].id == descriptors[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) : Boolean {
                    return counters[oldItemPosition].id == descriptors[newItemPosition].id
                }
            })

            diffResult.dispatchUpdatesTo(countersAdapter)
            counters = descriptors
        })
    }

    private fun addEntryToFireStore() {
        model.addCounter(CounterDescriptor("zzz1", 222))
    }

    private fun removeEntryFromFireStore() {
        // nothing
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.addElementBtn -> addEntryToFireStore()
            R.id.removeElementBtn -> removeEntryFromFireStore()
        }
    }


    internal inner class CountersAdapter : RecyclerView.Adapter<CountersAdapter.CounterViewHolder>() {

        init {
            Log.d("CountersAdapter", "Freakin adapter")
        }

        override fun onCreateViewHolder(parent: ViewGroup, position: Int): CounterViewHolder {
            val itemView = CounterView(parent.context)
            return CounterViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CounterViewHolder, position: Int) {
            val descriptor = counters.get(position)
            holder.update(descriptor)
        }

        override fun getItemCount(): Int {
            return counters.size
        }

        inner class CounterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            //val nameTextView: TextView
            //val dateTextView: TextView

            //init {
                //nameTextView = itemView.findViewById(R.id.tvName)
                //dateTextView = itemView.findViewById(R.id.tvDate)
                //val btnDelete = itemView.findViewById<View>(R.id.btnDelete)
                //btnDelete.setOnClickListener {
                //    val pos = adapterPosition
                //    val todo = mTodos.get(pos)
                //    mTodosViewModel.removeTodo(todo.getId())
                //}
            //}

            fun update(descriptor: Descriptor) {
                (itemView as BaseView).setDescriptor(descriptor)
            }
        }
    }


}
