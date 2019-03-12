package com.example.demo.view

import com.example.demo.model.Activity
import com.example.demo.model.Season
import com.example.demo.model.Seasons
import com.example.demo.model.TripLength
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.ToggleGroup
import tornadofx.*
import java.time.LocalDate
import java.util.logging.Logger

class MainView : View("What Should We Do v0.1") {

    val LOG = Logger.getLogger(this.javaClass.name)

    private var activityList = getActivities()
    private var tableData = SortedFilteredList(activityList.observable())

    // filters
    private var seasonFilter: Seasons = Seasons()
    private var maxLength : TripLength = TripLength.WEEKEND
    private var maxTravel : Int = 0
    private var tags : String = ""
    private val locations = SimpleStringProperty("")

    // groups
    private val lengthGroup = ToggleGroup()
    private val distanceGroup = ToggleGroup()

    override val root = borderpane {
        setPrefSize(1000.0, 400.0)
        top = vbox {
            hbox {
                vbox {
                    label("Desired Seasons")
                    hbox {
                        togglebutton("Spring") {
                            action {
                                seasonFilter.apply {
                                    if (isSelected) addSeason(Season.SPRING) else removeSeason(Season.SPRING)
                                }
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                        togglebutton("Summer") {
                            action {
                                seasonFilter.apply {
                                    if (isSelected) addSeason(Season.SUMMER) else removeSeason(Season.SUMMER)
                                }
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                        togglebutton("Autumn") {
                            action {
                                seasonFilter.apply {
                                    if (isSelected) addSeason(Season.AUTUMN) else removeSeason(Season.AUTUMN)
                                }
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                        togglebutton("Winter") {
                            action {
                                seasonFilter.apply {
                                    if (isSelected) addSeason(Season.WINTER) else removeSeason(Season.WINTER)
                                }
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                    }
                    hboxConstraints {
                        marginRight = 20.0
                    }
                }
                vbox {
                    label("Max Trip Length")
                    hbox {
                        togglebutton("Half-Day", lengthGroup) {
                            action {
                                maxLength = if (isSelected) TripLength.HALF_DAY else TripLength.WEEKEND
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                        togglebutton("Day Trip", lengthGroup) {
                            action {
                                maxLength = if (isSelected) TripLength.DAY else TripLength.WEEKEND
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                        togglebutton("Weekend", lengthGroup) {
                            action {
                                maxLength = TripLength.WEEKEND
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                    }
                    hboxConstraints {
                        marginRight = 20.0
                    }
                }
                vbox {
                    label("Max Travel Time")
                    hbox {
                        togglebutton("30m", distanceGroup) {
                            action {
                                maxTravel = if (isSelected) 30 else 0
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                        togglebutton("1hr", distanceGroup) {
                            action {
                                maxTravel = if (isSelected) 60 else 0
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                        togglebutton("2hr", distanceGroup) {
                            action {
                                maxTravel = if (isSelected) 120 else 0
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                        togglebutton("3hr", distanceGroup) {
                            action {
                                maxTravel = if (isSelected) 180 else 0
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                        togglebutton(">3hr", distanceGroup) {
                            action {
                                maxTravel = 0
                                tableData.refilter()
                            }
                            isSelected = false
                        }
                    }
                }
                vboxConstraints {
                    marginTop = 5.0
                    marginLeft = 5.0
                    marginBottom = 5.0
                }
            }
            hbox {
                textfield(tags) {
                    promptText = "Desired tag(s) ..."
                    hboxConstraints {
                        marginRight = 20.0
                    }
                }
                textfield(locations) {
                    promptText = "Desired location(s) ..."
                }
                vboxConstraints {
                    marginLeft = 5.0
                    marginBottom = 5.0
                }
            }
        }

        center = tableview(tableData) {
            readonlyColumn("TITLE", Activity::name)
            readonlyColumn("LOCATION", Activity::location)
            readonlyColumn("TRAVEL TIME", Activity::travelTime)
            readonlyColumn("SEASONS", Activity::seasons)
            readonlyColumn("TAGS", Activity::tags)
            readonlyColumn("LENGTH", Activity::length)
            readonlyColumn("LAST DONE", Activity::lastDone)

            contextmenu {
                item("Mark Done").action {
                    // TODO: pop form with date picker
                    selectedItem?.lastDone = LocalDate.now().toString()
                    tableData.refilter()
                }
                item("Edit").action {
                    // TODO: pop form for editing
                    println("Editing $selectedItem")
                }
                item("Remove").action {
                    println("Removing $selectedItem")
                    selectedItem?.deleted = true
                    tableData.refilter()
                }
            }

            width
        }
    }

    init {
        tableData.predicate = {
            LOG.info(seasonFilter.toString())
            var shouldShow = true
            if (it.deleted) {
                shouldShow = false
            } else {
                val seasonShow = seasonFilter.isEmpty() or seasonFilter.hasOverlap(it.seasons)
                val lengthShow = it.length <= maxLength
                val travelShow = (maxTravel == 0) or (it.travelTime <= maxTravel)

                shouldShow = seasonShow and lengthShow and travelShow
            }

            LOG.info("Predicate for ${it.name}: ${shouldShow}")
            shouldShow
        }
    }

    private fun getActivities() : List<Activity> {
        return listOf(
          Activity(
            "Snowshoe", "Snoqualmie", 60, Seasons(winter = true),
            listOf("winter", "outdoors"), TripLength.HALF_DAY
          ),
          Activity("Hike", "Issaquah", 45, Seasons(spring = true, summer = true, autumn = true, winter = false),
            listOf("hiking", "outdoors"), TripLength.HALF_DAY
          ),
          Activity(
            "Ski", "Snoqualmie", 60, Seasons(winter = true),
            listOf("winter", "outdoors"), TripLength.DAY
          ),
          Activity("Long Named Activity", "Long Location Name", 120,
            Seasons(spring = true, summer = true, autumn = true, winter = true), listOf("tag A", "tag B", "tag C", "tag D"), TripLength.WEEKEND
          )
        )
    }
}
