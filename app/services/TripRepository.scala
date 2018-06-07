package services

import models.Trip

trait TripRepository {

  def get: Option[Trip]

}
