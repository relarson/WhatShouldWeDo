package com.larson.whatshouldwedo.model

data class Activity(
  val name: String = "",
  val location: String = "",
  val travelTime: Int = 0,
  val seasons: Seasons = Seasons(),
  val tags: List<String> = emptyList(),
  val length: TripLength = TripLength.HALF_DAY,
  var lastDone: String = "Never",
  var deleted: Boolean = false)
