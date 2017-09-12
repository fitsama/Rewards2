package rewards

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CustomerController {

    def calculationsService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Customer.list(params), model:[customerCount: Customer.count()]
    }

   /* def index(){
        params.max = 10
        [customerList: Customer.list(params),customerCount: Customer.count()]
    }*/

    def updateProfile(Customer customerInstance){
        customerInstance.save()
        render(view: "profile",model: [customerInstance: customerInstance])
    }

    def profile(){
        def customerInstance = Customer.findByPhone(params.id)
        [customerInstance:customerInstance]
    }
    def calculate(long id){
        def customer = Customer.get(id)
        customer = calculationsService.calculateTotalPoints(customer)
        redirect(action: "show",customer: customer)
    }

    def show(Customer customer) {
        respond customer
    }

    def create(){
        //def customer = new Customer(params)
        //[customer:customer]
        [customer:new Customer()]
    }

    def save2(Customer customer){
        def cust = customer.save()
        //redirect(action:"show",id:customer.id)
        redirect(action: "show",customer: cust)
    }

    /*def show(long id){
        [customer: customer.get(id)]
    }*/

   /* def create() {
       respond new Customer()
    }
*/
    /*def lookup(){
        def customerInstanceList = Customer.findAllByLastNameIlikeAndTotalPointsGreaterThanEquals("b%",3)
        [customerInstanceList:customerInstanceList]
    }*/

    @Transactional
    def save(Customer customer) {
        if (customer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (customer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond customer.errors, view:'create'
            return
        }

        customer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'customer.label', default: 'Customer'), customer.id])
                redirect customer
            }
            '*' { respond customer, [status: CREATED] }
        }
    }

    def edit(Customer customer) {
        respond customer
    }

    @Transactional
    def update(Customer customer) {
        if (customer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (customer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond customer.errors, view:'edit'
            return
        }

        customer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'customer.label', default: 'Customer'), customer.id])
                redirect customer
            }
            '*'{ respond customer, [status: OK] }
        }
    }

    @Transactional
    def delete(Customer customer) {

        if (customer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        customer.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'customer.label', default: 'Customer'), customer.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
