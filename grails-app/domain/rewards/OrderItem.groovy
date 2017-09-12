package rewards

class OrderItem {
    Integer qty
    Float total
    static belongsTo = [product:Product,onlineOrder:OnlineOrder]
    static constraints = {
    }
}
