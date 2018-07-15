package controllers

import javax.inject.Inject

import models.KeepInTouchFormData
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import play.api.mvc._
import models.gift._
import scala.concurrent.ExecutionContext

class GiftController @Inject()(env: Env, cc: ControllerComponents)(implicit assetsFinder: AssetsFinder, executionContext: ExecutionContext)
  extends AbstractController(cc)
    with I18nSupport
    with Webshop {

  def index = Action { implicit  request =>
    val products = findAllProducts(env)

    val itemCount: Int = cartInSession(request)
        .size

    Ok(views.html.gift.products(products, itemCount))
  }

  def addToCart = Action { implicit request =>
    addToCartForm.bindFromRequest().fold(formWithError => {
      Redirect(controllers.routes.GiftController.index())
    }, form => {
      val productIds = form.id :: cartInSession

      val serializedProductIds = productIds.toSet.toList mkString ","
      Redirect(controllers.routes.GiftController.index())
          .withSession("products" -> serializedProductIds)
    })
  }

  private def cartInSession(implicit request: Request[AnyContent]) = {
    request.session.get("products").fold(List[String]())(serializedProducts => {
      serializedProducts.split(",")
        .filterNot(_.isEmpty)
        .toList
    })
  }

  def cart = Action { implicit request =>
    val products: List[AllProductView] = productsInCart(request)
    val total: Double = cartTotalAmount(products)
    Ok(views.html.gift.cart(products, total))
  }

  def confirmCart = Action { implicit request =>
    confirmCartForm.bindFromRequest().fold(formWithError => {
      val products = productsInCart
      BadRequest(views.html.gift.cart(products, cartTotalAmount(products)))
    }, form => {
      val tuples = form.ids zip form.quantities
      (createOrder(tuples)(env)).fold({
        val products = productsInCart
        BadRequest(views.html.gift.cart(products, cartTotalAmount(products)))
      })(order => {
        Redirect(controllers.routes.GiftController.confirmation(order.id))
            .withNewSession
      })
    })
  }

  def confirmation(orderId: String) = Action { implicit request =>
    findOrder(orderId)(env).fold(NotFound("Order not found"))(order => {
      Ok(views.html.gift.confirmation(order.ref))
    })
  }

  def removeFromCart = Action { implicit request =>
    removeFromCartForm.bindFromRequest().fold(_ => {
      Redirect(routes.GiftController.cart())
    }, form => {
      Redirect(routes.GiftController.cart)
        .withSession("products" -> (cartInSession.toSet - form.id).toList.mkString(","))
    })
  }

  def keepInTouch = Action { implicit  request =>
    NotFound("Not found")
  }

  private def cartTotalAmount(products: List[AllProductView]) = {
    val total = products match {
      case Nil => 0
      case xs => xs.map(_.price).reduce(_ + _)
    }
    total
  }

  private def productsInCart(implicit request: Request[AnyContent]) = {
    val products: List[AllProductView] = cartInSession(request).map(productId => findProductById(productId)(env))
      .filter(!_.isEmpty)
      .map(_.get)
    products
  }

  private def keepInTouchForm = Form(
    mapping(
      "email" -> email
    )(KeepInTouchFormData.apply)(KeepInTouchFormData.unapply)
  )

  private def addToCartForm = Form(
    mapping(
      "id" -> nonEmptyText(36, 36)
    )(AddToCartFormData.apply)(AddToCartFormData.unapply)
  )

  private def removeFromCartForm = Form(
    mapping(
      "id" -> nonEmptyText(36, 36)
    )(RemoveFromCartFormData.apply)(RemoveFromCartFormData.unapply)
  )

  private def confirmCartForm = Form(
    mapping(
      "id" -> list(nonEmptyText(36, 36)),
      "quantity" -> list(number(0))
    )(CartData.apply)(CartData.unapply)
  )

  case class AddToCartFormData(id: String)
  case class RemoveFromCartFormData(id: String)
  case class CartData(ids: List[String], quantities: List[Int])
}
