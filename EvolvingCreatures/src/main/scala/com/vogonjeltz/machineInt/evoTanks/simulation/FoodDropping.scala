package com.vogonjeltz.machineInt.evoTanks.simulation

import com.vogonjeltz.machineInt.evoTanks.core.{Action, GameConstants, SimulationObject}
import com.vogonjeltz.machineInt.evoTanks.gfx.{Colour, RenderParams, ShapeRenderer}
import com.vogonjeltz.machineInt.evoTanks.physics.{Box, Circle, Vect}

/**
  * FoodDropping
  *
  * Created by fredd
  */
class FoodDropping(val position: Vect, val size: Double) extends SimulationObject {

  lazy val shape: Box = Box(10,10, position)

  val layer = GameConstants.FOOD_LAYER

  override def update():List[Action] = List()

  override def render():Unit =
    ShapeRenderer.renderBox(shape, RenderParams(filled = true, colour = Colour.RED))

}
