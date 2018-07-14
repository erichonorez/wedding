package services.gift

import javax.inject.Inject

import models.gift.{Env, OrderRepository, ProductRepository}

class RuntimeEnv @Inject()(val productRepository: ProductRepository, val orderRepository: OrderRepository) extends Env
