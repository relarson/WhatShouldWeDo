package com.example.demo.model

class Seasons(
  private val spring: Boolean = false,
  private val summer: Boolean = false,
  private val autumn: Boolean = false,
  private val winter: Boolean = false
) {
  private var bitfield = 0

  init {
    if (spring) {
      bitfield = bitfield or Season.SPRING.bitwiseRep
    }
    if (summer) {
      bitfield = bitfield or Season.SUMMER.bitwiseRep
    }
    if (autumn) {
      bitfield = bitfield or Season.AUTUMN.bitwiseRep
    }
    if (winter) {
      bitfield = bitfield or Season.WINTER.bitwiseRep
    }
  }

  fun addSeason(season: Season) : Seasons {
    bitfield = bitfield or season.bitwiseRep
    return this
  }

  fun removeSeason(season: Season) : Seasons {
    bitfield = bitfield and season.bitwiseRep.inv()
    return this
  }

  fun hasOverlap(other: Seasons) : Boolean {
    return (this.bitfield and other.bitfield) != 0
  }

  fun isEmpty() : Boolean {
    return this.bitfield == 0
  }

  override fun toString() : String {
    val seasons: MutableList<String> = mutableListOf()
    for (season in Season.values()) {
      if (bitfield or season.bitwiseRep == bitfield) {
        seasons.add(season.name.toLowerCase().capitalize())
      }
    }
    return seasons.joinToString(", ")
  }
}
