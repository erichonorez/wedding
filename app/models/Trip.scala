package models

case class Trip(steps: List[Step])
case class Step(name: String, location: Location, description: String, activities: List[String], pictures: List[String])
case class Location(lat: Double, lng: Double)