package com.maxfin.openweather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_add_town.*
import org.json.JSONException

class AddTownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_town)
        val actionBar = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)


        addTownButton.setOnClickListener {
            AddTownTask().execute()
        }

        geoButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                val fusedLocationClient: FusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
                    val longitude = location.longitude.toString()
                    val latitude = location.latitude.toString()
                    GeoTask().execute(arrayOf(latitude, longitude))
                }
            }
        }

    }


    inner class GeoTask : AsyncTask<Array<String>, Void, Boolean>() {
        override fun doInBackground(vararg params: Array<String>?): Boolean {
            val array = params[0]
            val weatherManager = WeatherManager()
            val town = weatherManager.loadCurrentWeatherByCoord(array!![0], array[1])
            val townManager = TownManager()
            townManager.addTown(town)
            return true
        }


        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            Toast.makeText(applicationContext, "Город добавлен", Toast.LENGTH_SHORT).show()
        }
    }


    inner class AddTownTask : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {

            try {
                val townManager = TownManager()
                val weatherManager = WeatherManager()
                val townName = townNameEditText.text.toString()
                townManager.addTown(townName, weatherManager.loadCurrentWeather(townName))

                runOnUiThread {
                    townNameEditText.text.clear()
                }

            } catch (e: JSONException) {
                return false
            }
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result!!) {
                Toast.makeText(this@AddTownActivity, "Город успешно добавлен", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this@AddTownActivity, "Ошибка", Toast.LENGTH_SHORT).show()

        }
    }


}
