package services.gift

import javax.inject.Inject

import anorm._
import anorm.SqlParser._
import models.gift
import models.gift.{Product, ProductRepository}
import play.api.db.Database

class SQLProductRepository @Inject() (implicit db: Database) extends ProductRepository {

  val parser: RowParser[gift.Product] =
    get[String]("ID") ~
    get[String]("NAME") ~
    get[Double]("PRICE") ~
    get[String]("DESCRIPTION") ~
    get[String]("IMAGE") ~
    get[Int]("INITIAL_STOCK") map {
      case id ~ name ~ price ~ description ~ image ~ initialStock => Product(id, name, price, description, Some(image), initialStock)
    }

  override def persist(product: gift.Product) = {
    db.withConnection { implicit c => {
      val isSuccessful = SQL(
        """
          INSERT INTO PRODUCTS (ID, NAME, PRICE, DESCRIPTION, IMAGE, INITIAL_STOCK)
          VALUES({id}, {name}, {price}, {description}, {image}, {initialStock})
          """).on(
          'id -> product.id,
          'name -> product.name,
          'price -> product.price,
          'description -> product.description,
          'image -> product.image.getOrElse(""),
          'initialStock -> product.initialStock
        ).execute()
      if (isSuccessful) Some(product) else None
    } }
  }

  override def find(id: String) = {
    db.withConnection { implicit c => {
      SQL(
        """
        SELECT ID, NAME, PRICE, DESCRIPTION, IMAGE, INITIAL_STOCK
        FROM PRODUCTS WHERE ID = {id}
        """).on(
        'id -> id
      ).as(parser.singleOpt)
    } }
  }

  override def all = {
    db.withConnection { implicit c => {
      SQL(
        """
        SELECT ID, NAME, PRICE, DESCRIPTION, IMAGE, INITIAL_STOCK
        FROM PRODUCTS
        """).as(parser.*)
    } }
  }

}
