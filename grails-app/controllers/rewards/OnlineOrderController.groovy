package rewards

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class OnlineOrderController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond OnlineOrder.list(params), model:[onlineOrderCount: OnlineOrder.count()]
    }

    def show(OnlineOrder onlineOrder) {
        respond onlineOrder
    }

    def create() {
        respond new OnlineOrder(params)
    }

    @Transactional
    def save(OnlineOrder onlineOrder) {
        if (onlineOrder == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (onlineOrder.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond onlineOrder.errors, view:'create'
            return
        }

        onlineOrder.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'onlineOrder.label', default: 'OnlineOrder'), onlineOrder.id])
                redirect onlineOrder
            }
            '*' { respond onlineOrder, [status: CREATED] }
        }
    }

    def edit(OnlineOrder onlineOrder) {
        respond onlineOrder
    }

    @Transactional
    def update(OnlineOrder onlineOrder) {
        if (onlineOrder == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (onlineOrder.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond onlineOrder.errors, view:'edit'
            return
        }

        onlineOrder.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'onlineOrder.label', default: 'OnlineOrder'), onlineOrder.id])
                redirect onlineOrder
            }
            '*'{ respond onlineOrder, [status: OK] }
        }
    }

    @Transactional
    def delete(OnlineOrder onlineOrder) {

        if (onlineOrder == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        onlineOrder.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'onlineOrder.label', default: 'OnlineOrder'), onlineOrder.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'onlineOrder.label', default: 'OnlineOrder'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
