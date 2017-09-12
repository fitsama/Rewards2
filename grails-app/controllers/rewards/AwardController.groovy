package rewards

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AwardController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Award.list(params), model:[awardCount: Award.count()]
    }

    def show(Award award) {
        respond award
    }

    def create() {
        respond new Award(params)
    }

    @Transactional
    def save(Award award) {
        if (award == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (award.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond award.errors, view:'create'
            return
        }

        award.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'award.label', default: 'Award'), award.id])
                redirect award
            }
            '*' { respond award, [status: CREATED] }
        }
    }

    def edit(Award award) {
        respond award
    }

    @Transactional
    def update(Award award) {
        if (award == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (award.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond award.errors, view:'edit'
            return
        }

        award.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'award.label', default: 'Award'), award.id])
                redirect award
            }
            '*'{ respond award, [status: OK] }
        }
    }

    @Transactional
    def delete(Award award) {

        if (award == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        award.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'award.label', default: 'Award'), award.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'award.label', default: 'Award'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
