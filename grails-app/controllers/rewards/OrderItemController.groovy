package rewards

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class OrderItemController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond OrderItem.list(params), model:[orderItemCount: OrderItem.count()]
    }

    def show(OrderItem orderItem) {
        respond orderItem
    }

    def create() {
        respond new OrderItem(params)
    }

    @Transactional
    def save(OrderItem orderItem) {
        if (orderItem == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (orderItem.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond orderItem.errors, view:'create'
            return
        }

        orderItem.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'orderItem.label', default: 'OrderItem'), orderItem.id])
                redirect orderItem
            }
            '*' { respond orderItem, [status: CREATED] }
        }
    }

    def edit(OrderItem orderItem) {
        respond orderItem
    }

    @Transactional
    def update(OrderItem orderItem) {
        if (orderItem == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (orderItem.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond orderItem.errors, view:'edit'
            return
        }

        orderItem.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'orderItem.label', default: 'OrderItem'), orderItem.id])
                redirect orderItem
            }
            '*'{ respond orderItem, [status: OK] }
        }
    }

    @Transactional
    def delete(OrderItem orderItem) {

        if (orderItem == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        orderItem.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'orderItem.label', default: 'OrderItem'), orderItem.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'orderItem.label', default: 'OrderItem'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
