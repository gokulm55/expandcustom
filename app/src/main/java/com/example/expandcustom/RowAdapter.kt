package com.example.expandcustom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class RowAdapter (val context: Context, var rowModels: MutableList<RowModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * flag to restrict expand / collapse action it is already expanding / collapsing
     */
    private var actionLock = false

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var name_tv: TextView
        internal var toggle_btn : ImageButton
        init {
            this.name_tv = itemView.findViewById(R.id.name_tv) as TextView
            this.toggle_btn = itemView.findViewById(R.id.toggle_btn) as ImageButton
        }
    }

    class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var name_tv: TextView
        internal var toggle_btn : ImageButton
        init {
            this.name_tv = itemView.findViewById(R.id.name_tv) as TextView
            this.toggle_btn = itemView.findViewById(R.id.toggle_btn) as ImageButton
        }
    }

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var name_tv: TextView
        init {
            this.name_tv = itemView.findViewById(R.id.name_tv) as TextView
        }
    }

    override fun getItemViewType(position: Int): Int {
        val type = rowModels[position].type
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder = when (viewType) {
            RowModel.COUNTRY -> CountryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.country_row, parent, false))
            RowModel.STATE -> StateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.state_row, parent, false))
            RowModel.CITY -> CityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.city_row, parent, false))
            else -> CountryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.country_row, parent, false))
        }
        return viewHolder
    }


    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val row = rowModels[p1]

        when(row.type){
            RowModel.COUNTRY -> {
                (p0 as CountryViewHolder).name_tv.setText(row.country.name)

                if(row.country.stateList == null || row.country.stateList!!.size == 0) {
                    p0.toggle_btn.visibility = View.GONE
                }
                else {
                    if(p0.toggle_btn.visibility == View.GONE){
                        p0.toggle_btn.visibility = View.VISIBLE
                    }

                    if (row.isExpanded) {
                        p0.toggle_btn.background =
                            ContextCompat.getDrawable(context, R.drawable.ic_arrow_up_24)
                    } else {
                        p0.toggle_btn.background =
                            ContextCompat.getDrawable(context, R.drawable.ic_arrow_down_24)
                    }

                    p0.toggle_btn.setOnClickListener {
                        if (!actionLock) {
                            actionLock = true
                            if (row.isExpanded) {
                                row.isExpanded = false
                                collapse(p1)
                            } else {
                                row.isExpanded = true
                                expand(p1)
                            }
                        }
                    }
                }
            }
            RowModel.STATE -> {
                (p0 as StateViewHolder).name_tv.setText(row.state.name)

                if(row.state.cityList == null || row.state.cityList!!.size == 0) {
                    p0.toggle_btn.visibility = View.GONE
                }
                else {
                    if(p0.toggle_btn.visibility == View.GONE){
                        p0.toggle_btn.visibility = View.VISIBLE
                    }

                    if (row.isExpanded) {
                        p0.toggle_btn.background =
                            ContextCompat.getDrawable(context, R.drawable.ic_arrow_up_24)
                    } else {
                        p0.toggle_btn.background =
                            ContextCompat.getDrawable(context, R.drawable.ic_arrow_down_24)
                    }
                }

                p0.toggle_btn.setOnClickListener {
                    if (!actionLock) {
                        actionLock = true
                        if (row.isExpanded) {
                            row.isExpanded = false
                            collapse(p1)
                        } else {
                            row.isExpanded = true
                            expand(p1)
                        }
                    }
                }
            }
            RowModel.CITY -> {
                (p0 as CityViewHolder).name_tv.setText(row.city.name)
            }
        }
    }


    fun expand(position: Int) {

        var nextPosition = position

        val row = rowModels[position]

        when (row.type) {

            RowModel.COUNTRY -> {

                /**
                 * add element just below of clicked row
                 */
                for (state in row.country.stateList!!) {
                    rowModels.add(++nextPosition, RowModel(RowModel.STATE, state))
                }

                notifyDataSetChanged()
            }

            RowModel.STATE -> {

                /**
                 * add element just below of clicked row
                 */
                for (city in row.state.cityList!!) {
                    rowModels.add(++nextPosition, RowModel(RowModel.CITY, city))
                }

                notifyDataSetChanged()
            }
        }

        actionLock = false
    }

    fun collapse(position: Int) {
        val row = rowModels[position]
        val nextPosition = position + 1

        when (row.type) {

            RowModel.COUNTRY -> {

                /**
                 * remove element from below until it ends or find another node of same type
                 */
                outerloop@ while (true) {
                    if (nextPosition == rowModels.size || rowModels.get(nextPosition).type === RowModel.COUNTRY) {
                        break@outerloop
                    }

                    rowModels.removeAt(nextPosition)
                }

                notifyDataSetChanged()
            }

            RowModel.STATE -> {

                /**
                 * remove element from below until it ends or find another node of same type or find another parent node
                 */
                outerloop@ while (true) {
                    if (nextPosition == rowModels.size || rowModels.get(nextPosition).type === RowModel.COUNTRY || rowModels.get(nextPosition).type === RowModel.STATE
                    ) {
                        break@outerloop
                    }

                    rowModels.removeAt(nextPosition)
                }

                notifyDataSetChanged()
            }
        }

        actionLock = false
    }


    override fun getItemCount() = rowModels.size

}