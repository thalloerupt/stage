package com.thallo.stage.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liulishuo.okdownload.DownloadTask

class UserConverters {
    @TypeConverter
    fun stringToUser(value: String):DownloadTask{
        val type = object : TypeToken<DownloadTask>(){

        }.type
        return Gson().fromJson(value,type)
    }
    @TypeConverter
    fun userToString(user:DownloadTask): String {
        val gson = Gson()
        return gson.toJson(user)
    }
}
