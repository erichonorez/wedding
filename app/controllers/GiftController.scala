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

//  val env = new Env {
//    override val productRepository: ProductRepository = new ProductRepository {
//
//      private val products: mutable.MutableList[Product] = mutable.MutableList()
//
//      override def all: List[Product] = products.toList
//
//      override def find(id: String): Option[Product] = products.find(_.id == id)
//
//      override def persist(product: Product): Option[Product] = {
//        products += product
//        Some(product)
//      }
//    }
//    override val orderRepository: OrderRepository = new OrderRepository {
//
//      private val orders: mutable.MutableList[Order] = mutable.MutableList()
//
//      override def persist(order: Order): Option[Order] = {
//        orders += order
//        Some(order)
//      }
//
//      override def update(o: Order): Option[Order] = orders.find(_.id == o.id).map(foundO => {
//        val i = orders.indexOf(foundO)
//        orders.update(i, o)
//        o
//      })
//
//      override def find(id: String): Option[Order] = orders.find(_.id == id)
//
//      override def findByProductId(id: String): List[Order] = orders.filter(_.items.exists(_.productId == id)).toList
//
//      override def count = orders.size
//    }
//  }
//
//  val productId1 = createProduct(
//    "1 mile d'avion",
//    0.25,
//    "Notre voyage en avion compte plus de 11,000 miles!",
//    None,
//    11000
//  )(env).get.id
//
//  val productId2 = createProduct(
//    "1 mile d'avion",
//    0.25,
//    "Notre voyage en avion compte plus de 11,000 miles!",
//    None,
//    11000
//  )(env).get.id
//  createOrder(List(productId2 -> 11000))(env)
//
//  val productId3 = createProduct(
//    "1 mile d'avion",
//    0.25,
//    "Notre voyage en avion compte plus de 11,000 miles!",
//    None,
//    11000
//  )(env).get.id
//  val orderId = createOrder(List(productId3 -> 11000))(env).get.id
//  confirmOrder(orderId)(env)
//
//  val productId4 = createProduct(
//    "1 mile d'avion",
//    0.25,
//    "Notre voyage en avion compte plus de 11,000 miles!",
//    None,
//    11000
//  )(env)

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

  def confirmCart = Action { implicit request =>
    confirmCartForm.bindFromRequest().fold(formWithError => {
      val products = productsInCart
      BadRequest(views.html.gift.cart(products, cartTotalAmount(products)))
    }, form => {
      val tuples = form.items.map(item => (item.id -> item.quantity))
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
//    keepInTouchForm.bindFromRequest().fold(formWithError => {
//      BadRequest(views.html.gift.index(formWithError))
//    }, form => {
//        db.withConnection { implicit c =>
//          SQL("insert into KEEP_IN_TOUCH_REQUESTS (email) values ({email})")
//            .on("email" -> form.email)
//            .executeInsert()
//        }
//
//      Redirect(controllers.routes.TripController.index())
//        .flashing(
//          "success" -> "On a bien enregistré ta demande, on te tiendra au courant très vite ;)")
//    } )
    NotFound("Not found")
  }

  private def keepInTouchForm = Form(
    mapping(
      "email" -> email
    )(KeepInTouchFormData.apply)(KeepInTouchFormData.unapply)
  )
0
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
      "items" -> list(
        mapping(
          "id" -> nonEmptyText(36, 36),
          "quantity" -> number(0)
        )(CartItemData.apply)(CartItemData.unapply)
      )
    )(CartData.apply)(CartData.unapply)
  )

  case class AddToCartFormData(id: String)
  case class RemoveFromCartFormData(id: String)
  case class CartItemData(id: String, quantity: Int)
  case class CartData(items: List[CartItemData])
}
