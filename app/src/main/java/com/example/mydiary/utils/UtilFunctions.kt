package com.example.mydiary.utils


import io.realm.kotlin.types.RealmInstant
import java.time.Instant

fun RealmInstant.toInstant():Instant {
    val sec = this.epochSeconds
    val nano = this.nanosecondsOfSecond
    return if(sec>=0){
        Instant.ofEpochSecond(sec,nano.toLong())
    }else{
        Instant.ofEpochSecond(sec-1,1_000_000+nano.toLong())
    }

}

fun Instant.toRealmInstant():RealmInstant{
    val sec = this.epochSecond
    val nano = this.nano

    return if(sec>=0) RealmInstant.from(sec,nano)
    else RealmInstant.from(sec+1,-1_000_000 + nano)
}