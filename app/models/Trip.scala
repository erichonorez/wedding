package models

case class Trip(steps: List[Step])
case class Step(name: String, slug: String, order: Int, location: Location, description: String, pictures: List[String])
case class Location(lat: Double, lng: Double)