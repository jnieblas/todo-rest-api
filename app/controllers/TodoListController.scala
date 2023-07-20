package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.collection.mutable
import models._
import play.api.libs.json._

@Singleton
class TodoListController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {

  // implicit to avoid passing to Json.toJson() all the time
  implicit val todoListJson: OFormat[TodoListItem] = Json.format[TodoListItem]
  implicit val newTodoListJson: OFormat[NewTodoListItem] =
    Json.format[NewTodoListItem]

  private val todoList = new mutable.ListBuffer[TodoListItem]()
  todoList += TodoListItem(1, "test", false)
  todoList += TodoListItem(2, "some other value", false)

  def getAll(): Action[AnyContent] = Action {
    if (todoList.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(todoList))
    }
  }

  def getById(itemId: Long): Action[AnyContent] = Action {
    val foundItem = todoList.find(_.id == itemId)
    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None       => NotFound
    }
  }

  def addNewItem(): Action[AnyContent] = Action { implicit request =>
    val jsonObject = request.body.asJson
    val todoListItem: Option[NewTodoListItem] =
      jsonObject.flatMap(
        Json.fromJson[NewTodoListItem](_).asOpt
      )

    todoListItem match {
      case Some(newItem) =>
        val nextId = todoList.map(_.id).max + 1
        val toBeAdded = TodoListItem(nextId, newItem.description, false)
        todoList += toBeAdded
        Created(Json.toJson(toBeAdded))
      case None => BadRequest
    }
  }

  def markAsDone(itemId: Long): Action[AnyContent] = Action {
    val foundItem = todoList.find(_.id == itemId)

    foundItem match {
      case Some(item: TodoListItem) =>
        val updatedItem = item.copy(isItDone = true)
        todoList -= item
        todoList += updatedItem
        Created(Json.toJson(updatedItem))
      case None => NotFound
    }
  }

  def deleteAllDone(): Action[AnyContent] = Action {
    try {
      todoList --= todoList.filter(_.isItDone == true)
      NoContent
    } catch {
      case e: Exception => BadRequest("Unable to delete items")
    }
  }
}
