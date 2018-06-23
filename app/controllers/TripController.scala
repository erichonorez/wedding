package controllers

import javax.inject._

import play.api.mvc._
import services.TripRepository

class TripController @Inject()(tripRepository: TripRepository, cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
 extends AbstractController(cc) {

    def index = Action {
      val trip = tripRepository.get.get
      Ok(views.html.trip.index(trip))
    }

    def step(slug: String) = Action { request =>
      val trip = tripRepository.get.get
      val step = trip.steps.find(s => s.slug.equals(slug)).get
      Ok(views.html.trip.step(step, request.headers.get("referrer").getOrElse(routes.TripController.index().url)))
    }
}