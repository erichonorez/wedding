import models.gift.{AllProductView, Order, Product, Webshop}
import org.scalatest._

import scala.collection.mutable

class WebshopSpec extends FlatSpec with Matchers with BeforeAndAfter with Webshop {

  def newEnv = new Env {
    override val productRepository: ProductRepository = new ProductRepository {

      private val products: mutable.MutableList[Product] = mutable.MutableList()

      override def all: List[Product] = products.toList

      override def find(id: String): Option[Product] = products.find(_.id == id)

      override def persist(product: Product): Option[Product] = {
        products += product
        Some(product)
      }
    }
    override val orderRepository: OrderRepository = new OrderRepository {

      private val orders: mutable.MutableList[Order] = mutable.MutableList()

      override def persist(order: Order): Option[Order] = {
        orders += order
        Some(order)
      }

      override def update(o: Order): Option[Order] = orders.find(_.id == o.id).map(foundO => {
        val i = orders.indexOf(foundO)
        orders.update(i, o)
        o
      })

      override def find(id: String): Option[Order] = orders.find(_.id == id)

      override def findByProductId(id: String): List[Order] = orders.filter(_.items.exists(_.productId == id)).toList
    }
  }

  var env = newEnv

  before {
    env = newEnv
  }

  "A webshop" should "create products" in {
    val optionalProduct = createProduct("Fly", 3500, "Emirates", None, 1)(env)
    assert(optionalProduct.nonEmpty)
  }

  it should "return all product from the catalog" in {
    createProduct("Fly", 3500, "Emirates", None, 1)(env)
    createProduct("Fly", 3500, "Emirates", None, 1)(env)

    assert(findAllProducts(env).size == 2)
  }

  it should "create orders" in {
    val optionalProduct = createProduct("Fly", 3500, "Emirates", None, 1)(env)
    val optionalOrder = optionalProduct.flatMap(p => createOrder(
      List(
        p.id -> 1
      )
    )(env))

    assert(optionalOrder.nonEmpty)
  }

  it should "indicate if a product is reserved" in {
    createProduct("Fly", 3500, "Emirates", None, 3)(env).flatMap(
      p => createOrder(
        List(
          p.id -> 1
        )
    )(env))

    val product: AllProductView = findAllProducts(env).head
    assert(product.itemsWaitingForOrderConfirmationCount == 1)
    assert(product.itemsAvailableCount == 2)
  }

  it should "indicate if a product out of stock when finding all products" in {
    val productId: Option[String] = for {
      p <- createProduct("Fly", 3500, "Emirates", None, 3)(env)
      o <- createOrder(
        List(
          p.id -> 1,
          p.id -> 1,
          p.id -> 1
        )
      )(env)
      r <- confirmOrder(o.id)(env)
    } yield (p.id)



    val view = findProductById(productId.get)(env).get
    assert(view.itemsAvailableCount == 0)
    assert(view.itemsWaitingForOrderConfirmationCount == 0)
  }

}
