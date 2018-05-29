package controllers

import javax.inject._

import play.api.mvc._

class TripController @Inject()(cc: ControllerComponents)
 extends AbstractController(cc) {

    def index = Action {
        Ok(views.html.trip.index())
    }

}