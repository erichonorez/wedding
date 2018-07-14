package models.gift

import java.util.UUID

trait Webshop {

  def createProduct(name: String, price: Double, description: String, image: Option[String], initialStock: Int)(env: Env): Option[Product] = {
    val product = Product(
      UUID.randomUUID().toString,
      name,
      price,
      description,
      image,
      initialStock
    )
    env.productRepository.persist(product)
  }

  def findAllProducts(env: Env): List[AllProductView] = {
    env.productRepository.all.map(mapAllProductView(_)(env))
  }

  def findProductById(id: String)(env: Env): Option[AllProductView] = env.productRepository.find(id).map(mapAllProductView(_)(env))

  def createOrder(tuples: List[(String, Int)])(env: Env): Option[Order] = {
    val order = new Order(
    UUID.randomUUID().toString,
    s"ref-${env.orderRepository.count}",
    tuples.map(t => Item(t._1, t._2)),
    false
    )
    env.orderRepository.persist(order)

    Some(order)
  }

  def findOrder(orderId: String)(env: Env): Option[Order] = {
    env.orderRepository.find(orderId)
  }

  def confirmOrder(id: String)(env: Env): Option[Order] = env.orderRepository.find(id).flatMap(o => {
    o.confirmed = true;
    env.orderRepository.update(o)
  })

  private def mapAllProductView(p: Product)(env: Env): AllProductView = {
    def countItemsWaitingForOrderConfirmation(orders: List[Order], productId: String): Int = {
      orders.filter(!_.confirmed).flatMap(_.items.map(i => if (i.productId == productId) i.quantity else 0 )) match {
        case Nil =>  0
        case xs: List[Int] => xs.reduce(_ + _)
      }
    }

    def countItemsWithConfirmedOrder(orders: List[Order], productId: String): Int = {
      orders.filter(_.confirmed).flatMap(_.items.map(i => if (i.productId == productId) i.quantity else 0 )) match {
        case Nil =>  0
        case xs: List[Int] => xs.reduce(_ + _)
      }
    }

    val os = env.orderRepository.findByProductId(p.id)
    val itemsWaitingForOrderConfirmationCount = countItemsWaitingForOrderConfirmation(os, p.id)
    AllProductView(
      p.id,
      p.name,
      p.price,
      p.description,
      p.image,
      p.initialStock,
      p.initialStock - countItemsWithConfirmedOrder(os, p.id) - itemsWaitingForOrderConfirmationCount,
      itemsWaitingForOrderConfirmationCount
    )
  }

}

case class Product(id: String, name: String, price: Double, description: String, image: Option[String], initialStock: Int)
class Order(val id: String, val ref: String, val items: List[Item], var confirmed: Boolean)
case class Item(productId: String, quantity: Int)
case class AllProductView(id: String, name: String, price: Double, description: String, image: Option[String], initialStock: Int, itemsAvailableCount: Int, itemsWaitingForOrderConfirmationCount: Int)

trait Env {
  val productRepository: ProductRepository
  val orderRepository: OrderRepository
}

trait ProductRepository {
  def persist(product: Product): Option[Product]
  def find(id: String): Option[Product]
  def all: List[Product]

}

trait OrderRepository {
  def persist(order: Order): Option[Order]
  def update(o: Order): Option[Order]
  def find(id: String): Option[Order]
  def findByProductId(id: String): List[Order]
  def count: Int
}