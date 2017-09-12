package rewards

import grails.gorm.transactions.Transactional

@Transactional
class CalculationsService {

    def calculateTotalPoints(customer) {
        //def awards = Customer.list(customer.awards)
        def totalAward = 0
        customer.awards.each{
            totalAward += it.points
        }
        customer.totalPoints = totalAward

       return customer
    }
}
