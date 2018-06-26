package controllers

import javax.inject.Inject

import models.KeepInTouchFormData
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import play.api.mvc._
import anorm._
import play.api.db.Database

import scala.concurrent.ExecutionContext

class GiftController @Inject()(db: Database, cc: ControllerComponents)(implicit assetsFinder: AssetsFinder, executionContext: ExecutionContext)
  extends AbstractController(cc)
    with I18nSupport {

  def index = Action { implicit  request =>
    Ok(views.html.gift.index(keepInTouchForm))
  }

  def keepInTouch = Action { implicit  request =>
    keepInTouchForm.bindFromRequest().fold(formWithError => {
      BadRequest(views.html.gift.index(formWithError))
    }, form => {
        db.withConnection { implicit c =>
          SQL("insert into KEEP_IN_TOUCH_REQUESTS (email) values ({email})")
            .on("email" -> form.email)
            .executeInsert()
        }

      Redirect(controllers.routes.TripController.index())
        .flashing(
          "success" -> "On a bien enregistré ta demande, on te tiendra au courant très vite ;)")
    } )
  }

  private def keepInTouchForm = Form(
    mapping(
      "email" -> email
    )(KeepInTouchFormData.apply)(KeepInTouchFormData.unapply)
  )

}
