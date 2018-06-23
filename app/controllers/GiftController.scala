package controllers

import javax.inject.Inject

import play.api.mvc._

class GiftController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.gift.index(assetsFinder))
  }

  def keepInTouch = Action {
    NotFound
  }

}
