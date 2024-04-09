package com.example.practicaaaron.clases.usuarios

import com.google.gson.annotations.SerializedName

data class Data (
    @SerializedName("data")
    val dataUser: DataUser
)