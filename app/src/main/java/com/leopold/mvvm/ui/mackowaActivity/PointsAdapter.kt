package com.leopold.mvvm.ui.mackowaActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


import android.util.Log
import com.leopold.mvvm.R
import com.leopold.mvvm.data.db.entity.Point

import com.leopold.mvvm.ui.MackowaActivity.MackowaActivity
import com.leopold.mvvm.ui.mackowaActivity.mackowaActivity_pointDialog.PointDialog
import kotlinx.android.synthetic.main.item_point_marker_two_distances.view.*
import kotlinx.android.synthetic.main.item_point_marker_xy.view.buttonSelectMXY
import kotlinx.android.synthetic.main.item_point_marker_xy.view.textViewPointName_MXY
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


        fun selectItem(p: Point){

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
                CellType.OSNOWA_COORDINATES.ordinal -> {
                    val l = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_point_osnowa_coordinates, parent, false)
                    l.layoutParams.width = parent.width
                    OsnowaCoordinatesListViewHolder(l, vm)
                }
                CellType.OSNOWA_XY.ordinal -> {
                    val l = LayoutInflater.from(parent.context).inflate(
                        R.layout.item_point_osnowa_xy, parent, false
                    )
                    l.layoutParams.width = parent.width
                    OsnowaXYListViewHolder(
                        l, vm, this
                    )
                }
                CellType.MARKER_DISTANCES.ordinal -> {
                    val l = LayoutInflater.from(parent.context).inflate(R.layout.item_point_marker_two_distances, parent, false)
                    l.layoutParams.width = parent.width
                    MarkerDistancesListViewHolder(l, vm)
                }
                //CellType.MARKER_XY.ordinal
                else -> {
                    val l = LayoutInflater.from(parent.context).inflate(R.layout.item_point_marker_xy, parent, false)
                    l.layoutParams.width = parent.width
                    MarkerXYListViewHolder(l, vm)
                }

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


//        holder.itemView.visibility =  View.GONE
//        holder.itemView.visibility =  View.VISIBLE
//
//        holder.setIsRecyclable(false);


        //.getRecycledViewPool().clear();




        Log.d("PointsAdapter", "binding position:${position}")


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
            CellType.MARKER_DISTANCES.ordinal -> {
                val footerViewHolder = holder as MarkerDistancesListViewHolder
                footerViewHolder.bindView(punkty[position])
            }
        }



    }

    override fun getItemViewType(position: Int): Int {
        if(punkty[position].pointType == Point.PointType.OSNOWA_COORDINATES)
            return  CellType.OSNOWA_COORDINATES.ordinal
        else
        if(punkty[position].pointType == Point.PointType.OSNOWA_MARKER_XY)
            return  CellType.OSNOWA_XY.ordinal
        else
        if(punkty[position].pointType == Point.PointType.TARGET_TWO_DISTANCES)
            return  CellType.MARKER_DISTANCES.ordinal

        return  CellType.MARKER_XY.ordinal

    }



    override fun getItemCount() = punkty.size



    class OsnowaXYListViewHolder(itemView: View, val vm: MackowaViewModel, val pa: PointsAdapter) : RecyclerView.ViewHolder(itemView) {

        fun bindView(p: Point) {

            Log.d("PointsAdapter", "binding OsnowaXY")

//            itemView.textMovieTitle.text = movieModel.movieTitle
//            itemView.textMovieViews.text = "Views: " + movieModel.movieViews
//            itemView.textReleaseDate.text = "Release Date: " + movieModel.releaseDate
//
//            Glide.with(itemView.context).load(movieModel.moviePicture!!).into(itemView.imageMovie)
            itemView.textViewPointName_OXY.text = p.name
            itemView?.textViewXX.text = p.x.toString()

            itemView.buttonSet_OSX.setOnClickListener{
                vm.currentPoint.value = p
                itemView.invalidate()
            }

            itemView.buttonMap_OXY.setOnClickListener {
                vm.focusMapOnPoint.value = p
                itemView.invalidate()

            }

//            itemView.buttonSelectOXY.setOnClickListener {
//                vm.currentPoint.value = p
//                pa.notifyDataSetChanged()
//            }

            itemView.textViewXX.text = "" + p.len1

        }

    }

        class    OsnowaCoordinatesListViewHolder(itemView: View, val vm: MackowaViewModel) : RecyclerView.ViewHolder(itemView) {
            fun bindView(p: Point) {
                Log.d("PointsAdapter", "binding OsnowaCoo")
//            itemView.textMovieTitle.text = movieModel.movieTitle
//            itemView.textMovieViews.text = "Views: " + movieModel.movieViews
//            itemView.textReleaseDate.text = "Release Date: " + movieModel.releaseDate
//
//            Glide.with(itemView.context).load(movieModel.moviePicture!!).into(itemView.imageMovie)
                itemView.textViewPointName_OC.text = p.name


                itemView.buttonSet_OC.setOnClickListener {
                    vm.currentPoint.value = p
                    itemView.invalidate()
                }
                itemView.buttonMap_OC.setOnClickListener {
                    vm.focusMapOnPoint.value = p
                    itemView.invalidate()
                }


            }
        }

        class    MarkerXYListViewHolder(itemView: View, val vm: MackowaViewModel) : RecyclerView.ViewHolder(itemView) {
            fun bindView(p: Point) {
                Log.d("PointsAdapter", "binding MarkerXY")
                itemView.textViewPointName_MXY.text = p.name
                itemView.textViewXX.text = p.x.toString()



                itemView.buttonSelectMXY.setOnClickListener {

                    vm.currentPoint.value = p

                }

            }
        }

        class    MarkerDistancesListViewHolder(itemView: View, val vm: MackowaViewModel) : RecyclerView.ViewHolder(itemView) {
            fun bindView(p: Point) {
                Log.d("PointsAdapter", "binding Marker2Dist!")

                itemView.textViewMarker1Dist.text = "" + p.len1
                itemView.textViewMarker1Name.text = "" + p.referenceId

                itemView.textViewMarker2Dist.text = "" + p.len2
                itemView.textViewMarker2Name.text = "" + p.referenceId2

                itemView.textViewPointName.text = "" + p.name

                itemView.buttonMap_2Dist.setOnClickListener {
                    vm.focusMapOnPoint.value = p
                }

                itemView.buttonSet_2Dist.setOnClickListener {
                    vm.currentPoint.value = p
                }


            }
        }





        enum class CellType(viewType: Int) {
        MARKER_XY(0),
        OSNOWA_COORDINATES(1),
        MARKER_DISTANCES(2),
        OSNOWA_XY(3)
    }



}