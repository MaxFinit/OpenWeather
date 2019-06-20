package com.maxfin.openweather

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var townListAdapter: TownListAdapter? = null
    private var list: List<Town> = ArrayList()
    private var townManager = TownManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        town_recycler_view.layoutManager = LinearLayoutManager(this)
        town_recycler_view.setHasFixedSize(true)

        checkPermission()

        add_town_fab.setOnClickListener {
            intent = Intent(this, AddTownActivity::class.java)
            startActivity(intent)
        }


        swipeRefreshLayout.setOnRefreshListener {

            SomeTask().execute()
            swipeRefreshLayout.isRefreshing = false
        }


    }


    fun updateUi() {
        if (townListAdapter == null) {
            townListAdapter = TownListAdapter(list)
            town_recycler_view.adapter = townListAdapter
        } else {
            townListAdapter!!.setContacts(list)
            townListAdapter!!.notifyDataSetChanged()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, ACCESS_FINE_LOCATION
                )
            ) else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(ACCESS_FINE_LOCATION), 42
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        SomeTask().execute()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            42 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                } else {
                    Toast.makeText(this, "Вы отключили местоположение", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    inner class SomeTask : AsyncTask<Void, Void, Long>() {
        override fun doInBackground(vararg params: Void?): Long {

            runOnUiThread {
                progressBar.visibility = View.VISIBLE
            }

            list = townManager.getAllTown()
            val weatherManager = WeatherManager()

            for (item in list) {
                item.weather = weatherManager.loadCurrentWeather(item.name)
                townManager.update(item)
            }
            return 0
        }

        override fun onPostExecute(result: Long?) {
            super.onPostExecute(result)
            progressBar.visibility = View.GONE
            updateUi()
        }
    }

    inner class TownListAdapter(private var townList: List<Town>) :
        RecyclerView.Adapter<TownListAdapter.TownListHolder>() {
        var townManager = TownManager()

        inner class TownListHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_card_view_town, parent, false)) {


            private lateinit var town: Town
            private var townName: TextView = itemView.findViewById(R.id.town_name_text_view)
            private var townTemperature: TextView = itemView.findViewById(R.id.town_temperature_text_view)
            private var windSpeed: TextView = itemView.findViewById(R.id.town_wind_text_view)
            private var windDirection: TextView = itemView.findViewById(R.id.town_wind_direction_image_view)
            private var deleteTown: ImageButton = itemView.findViewById(R.id.town_delete)


            fun bind(town: Town) {
                this.town = town
                townName.text = town.name
                townTemperature.text = town.weather.temperature
                windSpeed.text = town.weather.windSpeed
                windDirection.text = town.weather.windDirection

                deleteTown.setOnClickListener {
                    townManager.deleteTown(town)
                    SomeTask().execute()
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TownListHolder {

            val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
            return TownListHolder(layoutInflater, parent)

        }

        override fun onBindViewHolder(townListHolder: TownListHolder, position: Int) {
            val town = this.townList[position]
            townListHolder.bind(town)
        }

        override fun getItemCount(): Int {
            return townList.size
        }

        fun setContacts(list: List<Town>) {
            this.townList = list

        }

    }


}