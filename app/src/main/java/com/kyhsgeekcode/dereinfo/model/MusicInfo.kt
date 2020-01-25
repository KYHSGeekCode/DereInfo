package com.kyhsgeekcode.dereinfo.model

class MusicInfo(
    val id: Int,
    val name: String,
    val bpm: Int,
    val composer: String,
    val lyricist: String,
    val soundOffset: Int,
    val soundLength: Int,
    val circleType: Int = 4
)