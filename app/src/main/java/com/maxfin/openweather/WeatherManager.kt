package com.maxfin.openweather

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject


class WeatherManager {

    private val TAG = "WeatherManager"
    private val apiKey = "aff9d742074cb0f517e3a2d83c4c1926"

    fun loadCurrentWeather(townName: String): Weather {

        val okHttpClient = OkHttpClient()
        val url = "http://api.openweathermap.org/data/2.5/weather?q=$townName&appid=$apiKey&units=metric"
        val request: Request = Request.Builder().url(url).build()
        val response: Response = okHttpClient.newCall(request).execute()
        val json = response.body()?.string()
        val temp = (JSONObject(json).getJSONObject("main").get("temp")).toString()+"°С"
        val windSpeed = (JSONObject(json).getJSONObject("wind").get("speed")).toString()+"м/с"
        val windDeg = (JSONObject(json).getJSONObject("wind").get("deg")).toString()

        return Weather(temp, windSpeed, windDeg)

    }
}
