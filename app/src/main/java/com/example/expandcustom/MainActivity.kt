package com.example.expandcustom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity()
{
    lateinit var rowAdapter: RowAdapter
    lateinit var rows : MutableList<RowModel>
    lateinit var recyclerView : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycle)
        rows = mutableListOf()
        rowAdapter = RowAdapter(this, rows)

        recyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )

        recyclerView.adapter = rowAdapter

        populateData()
    }


    fun populateData(){
        val cityList1 : MutableList<City> = mutableListOf()
        cityList1.add(City("chennai"))
        cityList1.add(City("Trichy"))
        cityList1.add(City("Coimbatore"))
        cityList1.add(City("Karur"))
        cityList1.add(City("Erode"))

        val cityList2 : MutableList<City> = mutableListOf()
        cityList2.add(City("Thiruvananthapuram"))
        cityList2.add(City("Kochi"))
        cityList2.add(City("Alapuzha"))
        cityList2.add(City("Trissur"))
        cityList2.add(City("Kozhikode"))

        var stateList1 : MutableList<State> = mutableListOf()
        stateList1.add(State("TamilNadu", cityList1))
        stateList1.add(State("Kerala", cityList2))

        var stateList2 : MutableList<State> = mutableListOf()
        stateList2.add(State("Mascow", null))


        rows.add(RowModel(RowModel.COUNTRY, Country("India", stateList1)))
        rows.add(RowModel(RowModel.COUNTRY, Country("Russia", stateList2)))

        rowAdapter.notifyDataSetChanged()
    }


}