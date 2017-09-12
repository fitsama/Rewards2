package rewards

class Award {
    Date awardDate
    String awardType
    Integer points
    static belongsTo = [customer:Customer]
    static constraints = {
        awardType(inList:["Purchase","Reward"])
    }
}
