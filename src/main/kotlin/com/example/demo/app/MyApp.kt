package com.example.demo.app

import com.example.demo.view.MainView
import javafx.stage.Stage
import tornadofx.*

class MyApp: App(MainView::class, Styles::class) {
  override fun start(stage: Stage) {
    with(stage) {
      minWidth = 900.0
      minHeight = 400.0
      super.start(this)
    }
  }
}
