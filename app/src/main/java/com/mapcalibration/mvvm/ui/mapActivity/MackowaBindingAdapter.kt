package com.mapcalibration.mvvm.ui.mapActivity

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mapcalibration.mvvm.data.db.entity.Point


/**
 * @author Maciej Szreter
 */
// wywolane automatycznie
// binduje RepositoryAdapter - wywolane gdy trzeba wypelnic repositories
//app:repositories="@{vm.items}"
// bez tego nie pokazuje wypelnionej listy
// items - info pochodzi z pliku xml
// vm - tez? - tak:
//app:repositories="@{vm.items}"
//app:viewModel="@{vm}" />

// ten wywolany automatycznie, i wywoluje RepositoryAdapter
@BindingAdapter(value = ["punkty", "viewModel"])
fun setPoints(view: RecyclerView, points: List<Point>, vm: MapViewModel) {

    view.adapter?.run {
        if (this is PointsAdapter) {
            this.punkty = points
            //this.notifyDataSetChanged()
//            for(i in 0..punkty.size-1)
//                this.notifyItemChanged(i)
//            //this.ite
            //this.notifyItemChanged(0)
            Log.d("MackowaBindingAdapter", "dane zmienione, punkty.size=" + points.size)
            //vm.points.observeForever { notifyDataSetChanged() }
        }

    } ?: run {
        PointsAdapter(points, vm).apply { view.adapter = this }
    }

}