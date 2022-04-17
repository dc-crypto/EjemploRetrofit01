package com.diegocastro.ejemploretrofit01

import com.google.gson.annotations.SerializedName

data class DogsResponse (
    @SerializedName("status") var status:String,
    @SerializedName("message")var images:List<String>
    )
