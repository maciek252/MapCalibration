package com.leopold.mvvm.ui.mackowaActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.leopold.mvvm.R


import com.leopold.mvvm.ui.BindingViewHolder
import android.util.Log
import androidx.core.view.ViewCompat.setNestedScrollingEnabled
import com.leopold.mvvm.data.db.entity.Point
import com.leopold.mvvm.databinding.ItemPointBinding
import com.leopold.mvvm.ui.MackowaActivity.MackowaActivity
import com.leopold.mvvm.ui.search.SearchActivity


/**
 * @author Leopold
 */
class PointsAdapter(var punkty: List<Point> = listOf(), val vm: MackowaViewModel) :
    RecyclerView.Adapter<PointsAdapter.PointViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsAdapter.PointViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_point,
            parent,
            false
        )
        v.setOnClickListener {
            Toast.makeText(parent.context, "bbi", Toast.LENGTH_LONG )
        }
        val viewHolder = PointsAdapter.PointViewHolder(v)

        viewHolder.itemView.setOnClickListener {
            Log.d(
                "koko",
                "position = " + viewHolder.getAdapterPosition()
            )

            val p = punkty[viewHolder.getAdapterPosition()]
            val d = PointDialog(vm!!, p)
            //d.binding?.item
            val s: String = "dialog"
            val activity =  v.context as? MackowaActivity
            d?.show(activity?.supportFragmentManager, s)

        };

        setNestedScrollingEnabled(v, false);
        return viewHolder
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {

        //holder.binding. = punkty[position]
        Log.d("PointsAdapter", "binding position:${position}")

        holder.binding.textViewId.setText("n=" + punkty[position].id)
        holder.binding.textViewType.setText("lat" + punkty[position].pointType)
        holder.binding.textViewName.setText("name=" + punkty[position].name)
        //holder.binding.textView2.invalidate()
        //holder.binding.invalidateAll()
        //holder.binding.invalidateAll()




        //holder.binding.vm = vm

    }


    fun update(){

    }

    override fun getItemCount() = punkty.size
    class PointViewHolder(view: View) : BindingViewHolder<ItemPointBinding>(view)
}