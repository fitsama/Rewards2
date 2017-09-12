package rewards

import com.sun.istack.internal.Nullable

class Customer {
    String firstName
    String lastName
    Long phone
    String email
    Integer totalPoints
    static hasMany = [awards:Award,onlineOrder:OnlineOrder]
    static constraints = {
        phone()
        firstName(nullable:true)
        lastName(nullable:true)
        email(nullable:true,email:true)
        totalPoints(nullable:true,max:100)
    }
}
