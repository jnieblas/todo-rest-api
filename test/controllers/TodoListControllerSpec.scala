package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

// NEED TO FIX THESE
/** Add your spec here. You can mock out a whole application including requests,
  * plugins etc.
  *
  * For more information, see
  * https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
  */
class TodoListControllerSpec
    extends PlaySpec
    with GuiceOneAppPerTest
    with Injecting {

  "TodoListController GET all" should {
    "return a list of todo items" in {
      val controller = new TodoListController(stubControllerComponents())
      val response = controller.getAll().apply(FakeRequest(GET, "/"))

      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      contentAsString(response) must include(
        "[{\"id\":1,\"description\":\"test\",\"isItDone\":false},{\"id\":2,\"description\":\"some other value\",\"isItDone\":false}]"
      )
    }

    "be routable" in {
      val request = FakeRequest(GET, "/todo")
      val response = route(app, request).get

      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      contentAsString(response) must include(
        "[{\"id\":1,\"description\":\"test\",\"isItDone\":false},{\"id\":2,\"description\":\"some other value\",\"isItDone\":false}]"
      )
    }
  }

  "TodoListController GET by id" should {
    "return a single todo item" in {
      val controller = new TodoListController(stubControllerComponents())
      val response = controller.getById(1).apply(FakeRequest(GET, "/2"))

      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      contentAsString(response) must include(
        "{\"id\":1,\"description\":\"test\",\"isItDone\":false}"
      )
    }

    "be routable" in {
      val request = FakeRequest(GET, "/todo/1")
      val response = route(app, request).get

      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      contentAsString(response) must include(
        "{\"id\":1,\"description\":\"test\",\"isItDone\":false}"
      )
    }
  }
}
