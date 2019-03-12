package com.larson.whatshouldwedo.app

import com.larson.whatshouldwedo.view.MainView
import javafx.stage.Stage
import tornadofx.*

class WhatShouldWeDo: App(MainView::class, Styles::class) {
  override fun start(stage: Stage) {
    with(stage) {
      minWidth = 900.0
      minHeight = 400.0
      super.start(this)
    }
  }
}
