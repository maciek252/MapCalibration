package com.leopold.mvvm.ui.mackowaActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leopold.mvvm.R


import android.util.Log
import com.leopold.mvvm.data.db.entity.Point

import com.leopold.mvvm.ui.MackowaActivity.MackowaActivity
import com.leopold.mvvm.ui.mackowaActivity.mackowaActivity_pointDialog.PointDialog
import kotlinx.android.synthetic.main.item_point_marker_xy.view.*
import kotlinx.android.synthetic.main.item_point_osnowa_coordinates.view.*
import kotlinx.android.synthetic.main.item_point_osnowa_xy.view.*
import kotlinx.android.synthetic.main.item_point_osnowa_xy.view.textViewXX


/**
 * @author Leopold
 */
class PointsAdapter(var punkty: List<Point> = listOf(), val vm: MackowaViewModel) :
//    RecyclerView.Adapter<PointsAdapter.PointViewHolder>()
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {
        init{
            //vm?.points.observeForever { notifyDataSetChanged() }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):// PointsAdapter.PointViewHolder
            RecyclerView.ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(
//            R.layout.item_point,
//            parent,
//            false
//        )
//        v.setOnClickListener {
//            Toast.makeText(parent.context, "bbi", Toast.LENGTH_LONG )
//        }
//        //PointsAdapter.PointViewHolder(v)
        //setNestedScrollingEnabled(v, false);




        val viewHolder = when (viewType) {
                CellType.OSNOWA_COORDINATES.ordinal -> OsnowaCoordinatesListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_point_osnowa_coordinates, parent, false), vm)
                CellType.OSNOWA_XY.ordinal -> OsnowaXYListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_point_osnowa_xy, parent, false), vm, this)
                //CellType.MARKER_XY.ordinal
                else -> MarkerXYListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_point_marker_xy, parent, false), vm)

            }

        viewHolder.itemView.setOnClickListener {
            Log.d(
                "koko",
                "position = " + viewHolder.getAdapterPosition()
            )

            val p = punkty[viewHolder.getAdapterPosition()]
            val d =
                PointDialog(vm!!, p)
            //d.binding?.item
            val s: String = "dialog"
            val activity =  parent?.context as? MackowaActivity
            d?.show(activity?.supportFragmentManager, s)

        };

        //vm?.points.observeForever { notifyDataSetChanged() }




        return viewHolder

    }



    override fun onBindViewHolder(holder: //PointViewHolder
                                  RecyclerView.ViewHolder
                                   , position: Int) {


        when (getItemViewType(position)) {
            CellType.OSNOWA_COORDINATES.ordinal -> {
                val headerViewHolder = holder as OsnowaCoordinatesListViewHolder
                headerViewHolder.bindView(punkty[position])
            }
            CellType.OSNOWA_XY.ordinal -> {
                val headerViewHolder = holder as OsnowaXYListViewHolder
                headerViewHolder.bindView(punkty[position])
            }
            CellType.MARKER_XY.ordinal -> {
                val footerViewHolder = holder as MarkerXYListViewHolder
                footerViewHolder.bindView(punkty[position])
            }
        }


        Log.d("PointsAdapter", "binding position:${position}")
    }

    override fun getItemViewType(position: Int): Int {
        if(punkty[position].pointType == Point.PointType.OSNOWA_COORDINATES)
            return  CellType.OSNOWA_COORDINATES.ordinal
        if(punkty[position].pointType == Point.PointType.OSNOWA_MARKER_XY)
            return  CellType.OSNOWA_XY.ordinal
        return  CellType.MARKER_XY.ordinal

    }



    override fun getItemCount() = punkty.size

    class OsnowaXYListViewHolder(itemView: View, val vm: MackowaViewModel, val pa: PointsAdapter) : RecyclerView.ViewHolder(itemView) {

        fun bindView(p: Point) {
//            itemView.textMovieTitle.text = movieModel.movieTitle
//            itemView.textMovieViews.text = "Views: " + movieModel.movieViews
//            itemView.textReleaseDate.text = "Release Date: " + movieModel.releaseDate
//
//            Glide.with(itemView.context).load(movieModel.moviePicture!!).into(itemView.imageMovie)
            itemView.textViewPointName_OXY.text = p.name
            itemView?.textViewXX.text = p.x.toString()

            itemView.buttonSelectOXY.setOnClickListener {

                vm.currentPoint.value = p
                pa.notifyDataSetChanged()
            }


        }

    }

        class    OsnowaCoordinatesListViewHolder(itemView: View, val vm: MackowaViewModel) : RecyclerView.ViewHolder(itemView) {
            fun bindView(p: Point) {
//            itemView.textMovieTitle.text = movieModel.movieTitle
//            itemView.textMovieViews.text = "Views: " + movieModel.movieViews
//            itemView.textReleaseDate.text = "Release Date: " + movieModel.releaseDate
//
//            Glide.with(itemView.context).load(movieModel.moviePicture!!).into(itemView.imageMovie)
                itemView.textViewPointName_OC.text = p.name


                itemView.buttonSelectOC.setOnClickListener {

                    vm.currentPoint.value = p
                    itemView.invalidate()
                }

            }
        }

        class    MarkerXYListViewHolder(itemView: View, val vm: MackowaViewModel) : RecyclerView.ViewHolder(itemView) {
            fun bindView(p: Point) {
//            itemView.textMovieTitle.text = movieModel.movieTitle
//            itemView.textMovieViews.text = "Views: " + movieModel.movieViews
//            itemView.textReleaseDate.text = "Release Date: " + movieModel.releaseDate
//
//            Glide.with(itemView.context).load(movieModel.moviePicture!!).into(itemView.imageMovie)
                itemView.textViewPointName_MXY.text = p.name
                itemView.textViewXX.text = p.x.toString()

                itemView.buttonSelectMXY.setOnClickListener {

                    vm.currentPoint.value = p

                }

            }
        }




    enum class CellType(viewType: Int) {
        MARKER_XY(0),
        OSNOWA_COORDINATES(1),
        OSNOWA_XY(2)
    }



}