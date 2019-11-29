package com.mapcalibration.mvvm.ui.mapActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


import android.util.Log

import com.mapcalibration.mvvm.data.db.entity.Point

import com.mapcalibration.mvvm.ui.mapActivity.MapActivity
import com.mapcalibration.mvvm.ui.mapActivity.pointDialog.PointDialog
import kotlinx.android.synthetic.main.item_point_marker_two_distances.view.*
import kotlinx.android.synthetic.main.item_point_marker_xy.view.*
import kotlinx.android.synthetic.main.item_point_marker_xy.view.buttonSelectMXY
import kotlinx.android.synthetic.main.item_point_osnowa_coordinates.view.*
import kotlinx.android.synthetic.main.item_point_osnowa_xy.view.*
import kotlinx.android.synthetic.main.item_point_osnowa_xy.view.textViewRefMarker
import kotlinx.android.synthetic.main.item_point_osnowa_xy.view.textViewYY

import androidx.appcompat.app.AppCompatActivity
import com.mapcalibration.mvvm.R

import com.mapcalibration.mvvm.util.Utility




/**
 * @author Maciej Szreter
 */
class PointsAdapter(var punkty: List<Point> = listOf(), val vm: MapViewModel) :
//    RecyclerView.Adapter<PointsAdapter.PointViewHolder>()


    RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {

        var viewHolderek : RecyclerView.ViewHolder? = null
        var parencik :  ViewGroup? = null

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
                    OsnowaCoordinatesListViewHolder(l, vm, this)
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
                    MarkerDistancesListViewHolder(l, vm, this)
                }
                //CellType.MARKER_XY.ordinal
                else -> {
                    val l = LayoutInflater.from(parent.context).inflate(R.layout.item_point_marker_xy, parent, false)
                    l.layoutParams.width = parent.width
                    MarkerXYListViewHolder(l, vm, this)
                }

            }





        viewHolder.itemView.setOnClickListener {
            //showPointEditDialog()
        };

        //vm?.points.observeForever { notifyDataSetChanged() }

        parencik = parent
        viewHolderek = viewHolder

        return viewHolder

    }

    fun showPointEditDialog(p: Point){
        Log.d(
            "koko",
            "position = " + viewHolderek?.getAdapterPosition()
        )

        //val p = punkty[viewHolderek?.getAdapterPosition()!!]
        val d =
            PointDialog(vm!!, p)
        val s: String = "dialog"
        val activity =  parencik?.context as? MapActivity
        d?.show(activity?.supportFragmentManager, s)

    }


    override fun onBindViewHolder(holder: //PointViewHolder
                                  RecyclerView.ViewHolder
                                   , position: Int) {




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



    class OsnowaXYListViewHolder(itemView: View, val vm: MapViewModel, val pa: PointsAdapter) : RecyclerView.ViewHolder(itemView) {

        fun bindView(p: Point) {

            Log.d("PointsAdapter", "binding OsnowaXY")

//            itemView.textMovieTitle.text = movieModel.movieTitle
//            itemView.textMovieViews.text = "Views: " + movieModel.movieViews
//            itemView.textReleaseDate.text = "Release Date: " + movieModel.releaseDate
//
//            Glide.with(itemView.context).load(movieModel.moviePicture!!).into(itemView.imageMovie)
            itemView.textViewPointName_OXY.text = p.name
            itemView?.textViewYY.text = p.x.toString()

            itemView?.textViewYY.requestFocus()

            itemView.buttonSet_OSX.setOnClickListener{
                vm.currentPoint.value = p
                itemView.invalidate()

            }

            itemView.textViewRefMarker.text = vm?.points.value.filter { it.id == p.referenceId }.first().name


            itemView.buttonMap_OXY.setOnClickListener {
                vm.focusMapOnPoint.value = p
                itemView.invalidate()
            }

            itemView.button_O_XY_Edit.setOnClickListener {
                pa.showPointEditDialog(p)
            }

//            itemView.buttonSelectOXY.setOnClickListener {
//                vm.currentPoint.value = p
//                pa.notifyDataSetChanged()
//            }



        }

    }

        class    OsnowaCoordinatesListViewHolder(itemView: View, val vm: MapViewModel, val pa: PointsAdapter) : RecyclerView.ViewHolder(itemView) {
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

                itemView.buttonEdit_OC.setOnClickListener {
                    pa.showPointEditDialog(p)
                }

            }
        }

        class    MarkerXYListViewHolder(itemView: View, val vm: MapViewModel, val pa: PointsAdapter) : RecyclerView.ViewHolder(itemView) {
            fun bindView(p: Point) {
                Log.d("PointsAdapter", "binding MarkerXY")
                itemView.textViewPointName_MXY.text = p.name
                itemView.textViewXX.text = p.x.toString()
                itemView.textViewYY.text = p.y.toString()

                itemView.textViewXX.requestFocus()

                itemView.textViewRefMarker.text = vm?.points.value.filter { it.id == p.referenceId }.first().name


                itemView.button_M_XY_Edit.setOnClickListener {
                    pa.showPointEditDialog(p)
                }

                itemView.buttonSelectMXY.setOnClickListener {
                    vm.currentPoint.value = p
                }

                itemView.button_M_XY_MAP.setOnClickListener {
                    vm.focusMapOnPoint.value = p
                    itemView.invalidate()
                }

            }
        }

        class    MarkerDistancesListViewHolder(itemView: View, val vm: MapViewModel, val pa: PointsAdapter) : RecyclerView.ViewHolder(itemView) {


            fun bindView(p: Point) {
                Log.d("PointsAdapter", "binding Marker2Dist!")



                itemView.textViewMarker1Dist.text = "" + p.len1
                itemView.textViewMarker1Name.text = "" + vm?.points.value.filter { it.id == p.referenceId }.first().name

                itemView.textViewMarker2Dist.text = "" + p.len2
                itemView.textViewMarker2Name.text = "" + vm?.points.value.filter { it.id == p.referenceId2 }.first().name

                itemView.textViewPointName.text = "" + p.name

                val a = itemView.context as AppCompatActivity

                if(p.rightFromLine)
                    itemView.textViewLeftFromLine.text = Utility.getStringFromResources(
                        a, "left")
                else
                    itemView.textViewLeftFromLine.text = Utility.getStringFromResources(a, "right")

                itemView.button_marker_twodist_edit.setOnClickListener{
                    pa.showPointEditDialog(p)
                }

                itemView.buttonMap_2Dist.setOnClickListener {
                    vm.focusMapOnPoint.value = p
                }

                itemView.buttonSet_2Dist.setOnClickListener {
                    vm.currentPoint.value = p
                }


                //val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                //imm!!.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
                //val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                //imm.hideSoftInputFromWindow(windowToken, 0)

                // tu chyba tylko adapter-lista, a to trzeba w dialogu?
                //itemView.textViewMarker1Dist.requestFocus()
                //val imm = itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                //imm.showSoftInput(itemView, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        enum class CellType(viewType: Int) {
        MARKER_XY(0),
        OSNOWA_COORDINATES(1),
        MARKER_DISTANCES(2),
        OSNOWA_XY(3)
    }



}