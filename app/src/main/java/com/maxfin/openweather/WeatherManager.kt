package com.maxfin.openweather

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.util.*


class WeatherManager {

    private val TAG = "WeatherManager"
    private val apiKey = "aff9d742074cb0f517e3a2d83c4c1926"

    fun loadCurrentWeather(townName: String): Weather {

        val okHttpClient = OkHttpClient()
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$townName&appid=$apiKey&units=metric"
        val request: Request = Request.Builder().url(url).build()
        val response: Response = okHttpClient.newCall(request).execute()
        val json = response.body()?.string()
        val temp = (JSONObject(json).getJSONObject("main").get("temp")).toString() + "°С"
        val windSpeed = (JSONObject(json).getJSONObject("wind").get("speed")).toString() + "м/с"
        val windDeg = (JSONObject(json).getJSONObject("wind").get("deg")).toString()



        return Weather(temp, windSpeed, windDegConverter(windDeg))

    }


    fun loadCurrentWeatherByCoord(latitude: String, longitude: String): Town {

        val okHttpClient = OkHttpClient()
        val url =
            "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric"
        val request: Request = Request.Builder().url(url).build()
        val response: Response = okHttpClient.newCall(request).execute()
        val json = response.body()?.string()
        val temp = (JSONObject(json).getJSONObject("main").get("temp")).toString() + "°С"
        val windSpeed = (JSONObject(json).getJSONObject("wind").get("speed")).toString() + "м/с"
        val windDeg = (JSONObject(json).getJSONObject("wind").get("deg")).toString()
        val townName = (JSONObject(json).get("name")).toString()

        return Town(UUID.randomUUID().toString(), townName, Weather(temp, windSpeed, windDegConverter(windDeg)))

    }


    private fun windDegConverter(deg: String): String {

        val degInt: Float = deg.toFloat()


        if (degInt > 337.5 || degInt < 22.5)
            return "Север"

        if (degInt > 22.5 && degInt < 67.5)
            return "Север-Восток"

        if (degInt > 67.5 && degInt < 112.5)
            return "Восток"

        if (degInt > 112.5 && degInt < 157.5)
            return "Юго-Восток"

        if (degInt > 157.5 && degInt < 202.5)
            return "Юг"

        if (degInt > 202.5 && degInt < 247.5)
            return "Юго-Запад"

        if (degInt > 247.5 && degInt < 292.5)
            return "Запад"

        if (degInt > 292.5 && degInt < 337.5)
            return "Север-Запад"

        return "Ошибка"

    }
}
