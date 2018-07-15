package services.gift

import javax.inject.Inject

import anorm._
import anorm.SqlParser._
import models.gift.{Item, Order, OrderRepository}
import play.api.db.Database

class SQLOrderRepository @Inject() (implicit db: Database) extends OrderRepository {

  override def persist(order: Order) = {
    db.withConnection { implicit c => {
      SQL(
        """
          INSERT INTO ORDERS (ID, REF, CONFIRMED)
          VALUES ({id}, {ref}, {confirmed})
        """).on(
        'id         -> order.id,
        'ref        -> order.ref,
        'confirmed  -> order.confirmed
      ).execute()
      order.items.foreach(item => {
        SQL(
          """
            INSERT INTO ORDER_ITEMS (ORDER_ID, PRODUCT_ID, QUANTITY)
            VALUES ({orderId}, {productId}, {quantity})
          """).on(
          'orderId      -> order.id,
          'productId    -> item.productId,
          'quantity     -> item.quantity
        ).execute()
      })
      Some(order)
    } }
  }

  override def update(order: Order) = {
    db.withConnection { implicit c => {
      val isSuccessful = SQL(
        """
          UPDATE ORDERS SET REF = {ref}, CONFIRMED = {confirmed} WHERE ID = {id}
        """).on(
        'id         -> order.id,
        'ref        -> order.ref,
        'confirmed  -> order.confirmed
      ).executeUpdate() > 0
      if (isSuccessful) Some(order) else None
    } }
  }

  override def find(id: String) = {
    val itemParser: RowParser[Item] =  get[String]("PRODUCT_ID") ~ get[Int]("QUANTITY") map {
      case productId ~ quantity => Item(productId, quantity)
    }
    val orderParser: RowParser[(String, String, Boolean)] =  get[String]("ID") ~ get[String]("REF") ~ get[Int]("CONFIRMED") map {
      case orderId ~ ref ~ confirmed => (orderId, ref, confirmed > 0)
    }

    db.withConnection { implicit c => {
      val maybeTuple: Option[(String, String, Boolean)] = SQL(
        """
        SELECT ID, REF, CONFIRMED FROM ORDERS WHERE ID = {id}
        """
      ).on(
        'id -> id
      )
      .as(orderParser.singleOpt)

      maybeTuple.map(t => {
        val items = SQL(
          """
          SELECT PRODUCT_ID, QUANTITY FROM ORDER_ITEMS WHERE ORDER_ID = {orderId}
        """).on(
          'orderId -> id
        ).as(itemParser.*)

        new Order(t._1, t._2, items, t._3)
      })
    } }
  }

  override def findByProductId(id: String) = {
    db.withConnection { implicit c => {
      val orderIds = SQL(
        """
        SELECT ORDERS.ID FROM ORDERS
        JOIN ORDER_ITEMS on ORDERS.ID = ORDER_ITEMS.ORDER_ID
        WHERE ORDER_ITEMS.PRODUCT_ID = {productId}
      """).on(
        'productId -> id
      ).as(SqlParser.str("ORDERS.ID").*)

      orderIds.map(find(_)).filter(!_.isEmpty).map(_.get)
    } }
  }

  override def count = {
    db.withConnection { implicit c => {
      SQL(
        """
          SELECT COUNT(*) as count FROM ORDERS
        """).as(SqlParser.int("count").single)
    } }
  }

}
