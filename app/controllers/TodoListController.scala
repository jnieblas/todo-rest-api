package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.collection.mutable
import models.TodoListItem
import play.api.libs.json._

@Singleton
class TodoListController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  
    // implicit to avoid passing to Json.toJson() all the time
    implicit val todoListJson: OFormat[TodoListItem] = Json.format[TodoListItem]

    private val todoList = new mutable.ListBuffer[TodoListItem]()
    todoList += TodoListItem(1, "test", true)
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
            case None => NotFound
        }
    }
}
